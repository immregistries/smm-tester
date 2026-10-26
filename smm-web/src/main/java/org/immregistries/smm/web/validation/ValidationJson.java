package org.immregistries.smm.web.validation;

import java.util.List;
import java.util.regex.Pattern;
import org.immregistries.smm.validation.MessageValidation;
import org.immregistries.smm.validation.ValidationFinding;
import org.immregistries.smm.validation.ValidationProfile;
import org.immregistries.smm.validation.ValidationResult;
import org.immregistries.smm.validation.ValidatorSource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Writes validation results in the compact form the public API and MCP tools return: errors and
 * other notable findings are listed, routine findings are counted.
 */
public final class ValidationJson {

  public static final ObjectMapper MAPPER = new ObjectMapper();

  static final String STATUS_PASS = "PASS";
  static final String STATUS_FAIL = "FAIL";
  static final String STATUS_NOT_VALIDATED = "NOT_VALIDATED";

  private static final String GUIDANCE = "A message passes when it has no errors. Fix each error "
      + "named in the findings and validate again. Routine findings are only counted: RE-usage "
      + "warnings (a 'required, but may be empty' element is empty) and alerts that a value set "
      + "was not checked. Messages are sent to an outside validation service, so use synthetic "
      + "test data only.";

  private ValidationJson() {}

  /**
   * @param includeAll true to list every finding, including routine ones
   */
  public static ObjectNode toJson(List<MessageValidation> validations, boolean includeAll) {
    int passed = count(validations, STATUS_PASS);
    int failed = count(validations, STATUS_FAIL);
    int notValidated = count(validations, STATUS_NOT_VALIDATED);
    ObjectNode result = MAPPER.createObjectNode();
    result.put("pass", passed == validations.size());
    result.put("summary", summary(validations.size(), passed, failed, notValidated));
    result.put("messageCount", validations.size());
    result.put("passed", passed);
    result.put("failed", failed);
    result.put("notValidated", notValidated);
    ArrayNode messages = result.putArray("messages");
    for (MessageValidation validation : validations) {
      messages.add(toJson(validation, includeAll));
    }
    result.put("guidance", GUIDANCE);
    return result;
  }

  private static ObjectNode toJson(MessageValidation validation, boolean includeAll) {
    ObjectNode node = MAPPER.createObjectNode();
    node.put("index", validation.getIndex());
    String controlId = readMessageControlId(validation.getMessage());
    if (controlId != null) {
      node.put("messageControlId", controlId);
    }
    node.put("status", status(validation));
    if (validation.getProfile() != null) {
      node.set("profile", toJson(validation.getProfile()));
    }
    if (validation.getSource() != null) {
      ObjectNode validator = node.putObject("validator");
      validator.put("name", validation.getSource().getName());
      validator.put("website", validation.getSource().getWebsiteUrl());
    }
    ValidationResult result = validation.getResult();
    if (result == null) {
      node.put("problem", validation.getProblem());
      return node;
    }
    node.put("reportedProfile", result.getProfileIdentifier() + " - " + result.getProfileName());
    ObjectNode counts = node.putObject("counts");
    counts.put("errors", result.getErrorCount());
    counts.put("warnings", result.getWarningCount());
    counts.put("alerts", result.getAlertCount());
    counts.put("specErrors", result.getSpecErrorCount());
    ArrayNode findings = node.putArray("findings");
    for (ValidationFinding finding : includeAll ? result.getFindings()
        : result.getNotableFindings()) {
      ObjectNode findingNode = findings.addObject();
      findingNode.put("classification", finding.getClassification());
      findingNode.put("category", finding.getCategory());
      findingNode.put("line", finding.getLine());
      findingNode.put("path", finding.getPath());
      findingNode.put("description", finding.getDescription());
    }
    if (!includeAll) {
      ObjectNode routine = node.putObject("routineFindings");
      routine.put("reUsageWarnings", result.getReUsageWarningCount());
      routine.put("valueSetsNotChecked", result.getExcludedValueSetAlertCount());
    }
    if (result.getReportId() != null) {
      node.put("validatorReportId", result.getReportId());
    }
    return node;
  }

  public static ObjectNode toJson(ValidationProfile profile) {
    ObjectNode node = MAPPER.createObjectNode();
    node.put("key", profile.getKey());
    node.put("messageProfileId", profile.getMessageProfileId());
    node.put("messageType", profile.getMessageType());
    node.put("description", profile.getDescription());
    return node;
  }

  /**
   * @return the validator sources and their profiles
   */
  public static ObjectNode profilesToJson(List<ValidatorSource> sources) {
    ObjectNode root = MAPPER.createObjectNode();
    ArrayNode sourcesNode = root.putArray("validators");
    for (ValidatorSource source : sources) {
      ObjectNode sourceNode = sourcesNode.addObject();
      sourceNode.put("key", source.getKey());
      sourceNode.put("name", source.getName());
      sourceNode.put("website", source.getWebsiteUrl());
      ArrayNode profiles = sourceNode.putArray("profiles");
      for (ValidationProfile profile : source.getProfiles()) {
        profiles.add(toJson(profile));
      }
    }
    root.put("note", "The profile is detected from MSH-21.1 (for example Z22) unless one is "
        + "chosen. Only these profiles are available.");
    return root;
  }

  /**
   * @return a plain text report for people and language models, listing errors and notable
   *         findings and counting routine ones
   */
  public static String toText(List<MessageValidation> validations, boolean includeAll) {
    StringBuilder text = new StringBuilder();
    text.append(summary(validations.size(), count(validations, STATUS_PASS),
        count(validations, STATUS_FAIL), count(validations, STATUS_NOT_VALIDATED))).append("\n");
    for (MessageValidation validation : validations) {
      text.append("\nMessage ").append(validation.getIndex());
      String controlId = readMessageControlId(validation.getMessage());
      if (controlId != null) {
        text.append(" (MSH-10 ").append(controlId).append(")");
      }
      text.append(": ").append(status(validation));
      if (validation.getProfile() != null) {
        text.append(" against ").append(validation.getProfile().getKey());
      }
      text.append("\n");
      ValidationResult result = validation.getResult();
      if (result == null) {
        text.append("  ").append(validation.getProblem()).append("\n");
        continue;
      }
      text.append("  errors=").append(result.getErrorCount()).append(" warnings=")
          .append(result.getWarningCount()).append(" alerts=").append(result.getAlertCount())
          .append("\n");
      for (ValidationFinding finding : includeAll ? result.getFindings()
          : result.getNotableFindings()) {
        text.append("  [").append(finding.getClassification()).append("/")
            .append(finding.getCategory()).append("] line ").append(finding.getLine())
            .append(" ").append(finding.getPath()).append(": ").append(finding.getDescription())
            .append("\n");
      }
      if (!includeAll) {
        text.append("  Routine: ").append(result.getReUsageWarningCount())
            .append(" empty RE elements, ").append(result.getExcludedValueSetAlertCount())
            .append(" value sets not checked\n");
      }
    }
    text.append("\n").append(GUIDANCE);
    return text.toString();
  }

  static String status(MessageValidation validation) {
    if (validation.getResult() == null) {
      return STATUS_NOT_VALIDATED;
    }
    return validation.isPass() ? STATUS_PASS : STATUS_FAIL;
  }

  private static int count(List<MessageValidation> validations, String status) {
    return (int) validations.stream().filter(v -> status(v).equals(status)).count();
  }

  private static String summary(int total, int passed, int failed, int notValidated) {
    if (total == 1) {
      return passed == 1 ? "The message passed."
          : failed == 1 ? "The message failed validation." : "The message could not be validated.";
    }
    return total + " messages: " + passed + " passed, " + failed + " failed"
        + (notValidated > 0 ? ", " + notValidated + " could not be validated" : "") + ".";
  }

  /**
   * @return MSH-10, or null if the message has none
   */
  static String readMessageControlId(String message) {
    if (message == null || !message.startsWith("MSH") || message.length() < 4) {
      return null;
    }
    int segmentEnd = message.indexOf('\r');
    String msh = segmentEnd < 0 ? message : message.substring(0, segmentEnd);
    String[] fields = msh.split(Pattern.quote(String.valueOf(msh.charAt(3))), -1);
    return fields.length > 9 && !fields[9].isBlank() ? fields[9].trim() : null;
  }
}
