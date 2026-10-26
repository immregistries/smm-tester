package org.immregistries.smm.validation;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Validates messages with a Valitheus (NIST HL7 v2 test framework) tool through its public REST
 * API. No account or key is needed. Each validation is stored as a report on the Valitheus site,
 * so only synthetic or test data should be sent.
 *
 * <p>
 * Profiles are addressed by test context id. If a call fails, the client looks the ids up again
 * from the site's published test plans, in case the site was republished, and retries once.
 */
public class ValitheusClient implements HL7Validator {

  private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);
  private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(60);
  private static final ObjectMapper MAPPER = new ObjectMapper();

  private final ValidatorSource source;
  private final HttpClient httpClient;
  private final Map<String, Long> discoveredContextIds = new ConcurrentHashMap<>();

  public ValitheusClient(ValidatorSource source) {
    this(source, HttpClient.newBuilder().connectTimeout(CONNECT_TIMEOUT)
        .followRedirects(HttpClient.Redirect.NORMAL).build());
  }

  public ValitheusClient(ValidatorSource source, HttpClient httpClient) {
    this.source = source;
    this.httpClient = httpClient;
  }

  @Override
  public ValidatorSource getSource() {
    return source;
  }

  @Override
  public ValidationResult validate(ValidationProfile profile, String message)
      throws ValidationException {
    long contextId =
        discoveredContextIds.getOrDefault(profile.getTestObjectName(), profile.getTestContextId());
    HttpResponse<String> response = post(contextId, message);
    if (response.statusCode() == 400) {
      throw new ValidationException(describeRejection(response.body()), false);
    }
    if (response.statusCode() != 200) {
      // Either the message could not be processed or the context id is out of date
      Long rediscoveredId = discoverContextIds().get(profile.getTestObjectName());
      if (rediscoveredId != null && rediscoveredId != contextId) {
        response = post(rediscoveredId, message);
      }
      if (response.statusCode() == 400) {
        throw new ValidationException(describeRejection(response.body()), false);
      }
      if (response.statusCode() != 200) {
        throw new ValidationException("The validator could not process this message (HTTP "
            + response.statusCode() + "). Check that it is a complete HL7 v2 message that starts "
            + "with a well-formed MSH segment.", false);
      }
    }
    return parseResponse(response.body());
  }

  private HttpResponse<String> post(long contextId, String message) throws ValidationException {
    ObjectNode body = MAPPER.createObjectNode();
    body.put("content", message);
    body.put("contextType", "Free");
    HttpRequest request = HttpRequest
        .newBuilder(URI.create(
            source.getApiBaseUrl() + "/hl7v2/testcontext/" + contextId + "/validateMessage"))
        .timeout(REQUEST_TIMEOUT).header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(body.toString(), StandardCharsets.UTF_8))
        .build();
    return send(request);
  }

  private HttpResponse<String> send(HttpRequest request) throws ValidationException {
    try {
      return httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new ValidationException(
          "Could not reach the validator at " + source.getApiBaseUrl() + ": " + e.getMessage(),
          true, e);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new ValidationException("Interrupted while waiting for the validator", true, e);
    }
  }

  /**
   * Reads the source's published test plans and maps each test object name to its test context
   * id. Results are remembered for later calls.
   */
  Map<String, Long> discoverContextIds() throws ValidationException {
    Map<String, Long> found = new HashMap<>();
    JsonNode testPlans =
        getJson(source.getApiBaseUrl() + "/cf/testplans?scope=GLOBAL&domain=" + source.getDomain());
    for (JsonNode testPlan : testPlans) {
      collectContextIds(getJson(source.getApiBaseUrl() + "/cf/testplans/" + testPlan.path("id")),
          found);
    }
    discoveredContextIds.putAll(found);
    return found;
  }

  private static void collectContextIds(JsonNode node, Map<String, Long> found) {
    if (node.isObject()) {
      JsonNode testContext = node.path("testContext");
      if (testContext.isObject() && testContext.has("id") && node.hasNonNull("name")) {
        found.put(node.get("name").asText(), testContext.get("id").asLong());
      }
      for (Entry<String, JsonNode> field : node.properties()) {
        if (!field.getKey().equals("testContext")) {
          collectContextIds(field.getValue(), found);
        }
      }
    } else if (node.isArray()) {
      for (JsonNode child : node) {
        collectContextIds(child, found);
      }
    }
  }

  private JsonNode getJson(String url) throws ValidationException {
    HttpRequest request = HttpRequest.newBuilder(URI.create(url)).timeout(REQUEST_TIMEOUT)
        .header("Accept", "application/json").GET().build();
    HttpResponse<String> response = send(request);
    if (response.statusCode() != 200) {
      throw new ValidationException(
          "The validator returned HTTP " + response.statusCode() + " for " + url, true);
    }
    try {
      return MAPPER.readTree(response.body());
    } catch (IOException e) {
      throw new ValidationException("The validator returned unreadable JSON for " + url, true, e);
    }
  }

  /**
   * Turns the validator's explanation of a rejected message into one readable sentence, dropping
   * its debugging details.
   */
  static String describeRejection(String body) {
    String text = body == null ? "" : body.trim();
    try {
      JsonNode node = MAPPER.readTree(text);
      if (node.isTextual()) {
        text = node.asText();
      }
    } catch (IOException e) {
      // Not JSON; use the text as it is
    }
    String reason = "";
    int exceptionStart = text.indexOf("Exception: ");
    if (exceptionStart >= 0) {
      reason = text.substring(exceptionStart + "Exception: ".length()).trim();
    }
    int debugStart = text.indexOf("DEBUG:");
    String summary = (debugStart >= 0 ? text.substring(0, debugStart) : text).trim();
    if (summary.isEmpty()) {
      summary = "The validator could not read the message.";
    }
    return reason.isEmpty() ? summary : summary + " " + reason;
  }

  /**
   * Parses a validateMessage response: an object whose <code>json</code> field holds the report
   * as a JSON string.
   */
  static ValidationResult parseResponse(String body) throws ValidationException {
    try {
      JsonNode response = MAPPER.readTree(body);
      JsonNode reportJson = response.path("json");
      if (!reportJson.isTextual()) {
        throw new ValidationException("The validator's response did not include a report", true);
      }
      JsonNode report = MAPPER.readTree(reportJson.asText());
      JsonNode metaData = report.path("metaData");
      JsonNode counts = metaData.path("counts");
      List<ValidationFinding> findings = new ArrayList<>();
      for (Entry<String, JsonNode> classification : report.path("detections").properties()) {
        if (classification.getKey().equals("Affirmative")) {
          continue;
        }
        for (Entry<String, JsonNode> category : classification.getValue().properties()) {
          for (JsonNode finding : category.getValue()) {
            findings.add(new ValidationFinding(classification.getKey(), category.getKey(),
                finding.path("line").asInt(), finding.path("column").asInt(),
                finding.path("path").asText(""), finding.path("description").asText("")));
          }
        }
      }
      // The report groups findings by classification in no set order; list errors first
      findings.sort(Comparator.comparingInt(ValitheusClient::severityRank)
          .thenComparingInt(ValidationFinding::getLine));
      String reportId = response.hasNonNull("reportId") ? response.get("reportId").asText() : null;
      return new ValidationResult(metaData.path("profile").path("identifier").asText(""),
          metaData.path("profile").path("name").asText(""), reportId,
          counts.path("error").asInt(), counts.path("warning").asInt(),
          counts.path("alert").asInt(), counts.path("spec-error").asInt(), findings);
    } catch (IOException e) {
      throw new ValidationException("The validator returned an unreadable report", true, e);
    }
  }

  private static int severityRank(ValidationFinding finding) {
    switch (finding.getClassification()) {
      case ValidationFinding.ERROR:
        return 0;
      case ValidationFinding.SPEC_ERROR:
        return 1;
      case ValidationFinding.WARNING:
        return 2;
      case ValidationFinding.ALERT:
        return 3;
      default:
        return 4;
    }
  }
}
