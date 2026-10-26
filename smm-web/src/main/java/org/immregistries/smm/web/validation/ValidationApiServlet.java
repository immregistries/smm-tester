package org.immregistries.smm.web.validation;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import org.immregistries.smm.validation.MessageValidation;
import org.immregistries.smm.web.SmmWebConfig;
import org.immregistries.smm.web.auth.InteropHubAuthenticationProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The public HL7 validation API. No sign-in is needed; {@link PublicApiFilter} limits use.
 *
 * <ul>
 * <li><code>POST /api/validate</code> validates one or more messages. The body is either JSON
 * (<code>{"message": "...", "profile": "vxu-z22", "includeAllFindings": false}</code>) or the HL7
 * text itself, with <code>profile</code> and <code>includeAllFindings</code> as query parameters.
 * <li><code>GET /api/profiles</code> lists the validators and profiles.
 * <li><code>GET /api/openapi.json</code> describes the API, for example for a ChatGPT action.
 * </ul>
 */
public class ValidationApiServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    ValidationGateway gateway = ValidationGateway.get(getServletContext());
    String path = request.getPathInfo() == null ? "" : request.getPathInfo();
    if (path.equals("/profiles")) {
      ApiResponses.writeJson(response, HttpServletResponse.SC_OK,
          ValidationJson.profilesToJson(gateway.getService().getSources()));
    } else if (path.equals("/openapi.json")) {
      ApiResponses.writeJson(response, HttpServletResponse.SC_OK,
          OpenApiDocument.build(getBaseUrl(request), gateway));
    } else if (path.equals("/validate")) {
      response.setHeader("Allow", "POST");
      ObjectNode body = ValidationJson.MAPPER.createObjectNode();
      body.put("error", "Send the message with POST.");
      ApiResponses.writeJson(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, body);
    } else {
      writeNotFound(response);
    }
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    String path = request.getPathInfo() == null ? "" : request.getPathInfo();
    if (!path.equals("/validate")) {
      writeNotFound(response);
      return;
    }
    ValidationGateway gateway = ValidationGateway.get(getServletContext());
    try {
      String body = ApiResponses.readBody(request, PublicApiFilter.maxBodyBytes(gateway));
      String text;
      String profile;
      boolean includeAll;
      String contentType = request.getContentType() == null ? ""
          : request.getContentType().toLowerCase(Locale.ROOT);
      if (contentType.startsWith("application/json")) {
        JsonNode json = parseJson(body);
        text = json.path("message").asText(null);
        profile = json.path("profile").asText(null);
        includeAll = json.path("includeAllFindings").asBoolean(false);
      } else {
        text = body;
        profile = request.getParameter("profile");
        includeAll = Boolean.parseBoolean(request.getParameter("includeAllFindings"));
      }
      List<MessageValidation> validations =
          gateway.validate(text, profile, gateway.getClientAddress(request));
      boolean validatorDown =
          validations.stream().allMatch(MessageValidation::isValidatorUnavailable);
      ObjectNode result = ValidationJson.toJson(validations, includeAll);
      ApiResponses.writeJson(response,
          validatorDown ? HttpServletResponse.SC_BAD_GATEWAY : HttpServletResponse.SC_OK, result);
    } catch (ValidationApiException e) {
      ApiResponses.writeError(response, e);
    }
  }

  private static JsonNode parseJson(String body) throws ValidationApiException {
    try {
      JsonNode json = ValidationJson.MAPPER.readTree(body);
      if (json == null || !json.isObject() || !json.path("message").isTextual()) {
        throw ValidationApiException
            .badRequest("Send a JSON object with the HL7 text in a \"message\" string.");
      }
      return json;
    } catch (JsonProcessingException e) {
      throw ValidationApiException.badRequest("The request body is not valid JSON.");
    }
  }

  private static void writeNotFound(HttpServletResponse response) throws IOException {
    ObjectNode body = ValidationJson.MAPPER.createObjectNode();
    body.put("error", "Not found. Available: POST /api/validate, GET /api/profiles, "
        + "GET /api/openapi.json");
    ApiResponses.writeJson(response, HttpServletResponse.SC_NOT_FOUND, body);
  }

  private String getBaseUrl(HttpServletRequest request) {
    return InteropHubAuthenticationProvider
        .getExternalUrl(SmmWebConfig.get(getServletContext()), request);
  }
}
