package org.immregistries.smm.web.validation;

import org.immregistries.smm.SoftwareVersion;
import org.immregistries.smm.validation.ValidationProfile;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Builds the OpenAPI 3.1 description of the validation API. A ChatGPT custom GPT can import it
 * from <code>/api/openapi.json</code> to call the API as an action.
 */
final class OpenApiDocument {

  private OpenApiDocument() {}

  static ObjectNode build(String baseUrl, ValidationGateway gateway) {
    ObjectNode doc = ValidationJson.MAPPER.createObjectNode();
    doc.put("openapi", "3.1.0");
    ObjectNode info = doc.putObject("info");
    info.put("title", "SMM HL7 v2 Validation API");
    info.put("version", SoftwareVersion.VERSION);
    info.put("description", "Validates HL7 v2.5.1 immunization messages against the CDC "
        + "Immunization IG Release 1.5 conformance profiles, using the Valitheus Immunization "
        + "Test Suite. Provided by the AIRA Simple Message Mover. No sign-in is needed. Up to "
        + gateway.getMaxMessages() + " messages per request and " + gateway.getMessagesPerMinute()
        + " messages per minute per client. Use synthetic test data only: messages are sent to an "
        + "outside validation service.");
    doc.putArray("servers").addObject().put("url", baseUrl + "/api");

    ObjectNode paths = doc.putObject("paths");
    ObjectNode validate = paths.putObject("/validate").putObject("post");
    validate.put("operationId", "validateHl7Message");
    validate.put("summary", "Validate HL7 v2 immunization messages");
    validate.put("description", "Validates one or more HL7 v2 messages (VXU, ACK, QBP, RSP). The "
        + "profile is detected from MSH-21.1 (for example Z22) unless one is given. A message "
        + "passes when it has no errors; fix each listed error and validate again. Segments may "
        + "be separated by new lines.");
    ObjectNode requestSchema = validate.putObject("requestBody").put("required", true)
        .putObject("content").putObject("application/json").putObject("schema");
    requestSchema.put("type", "object");
    requestSchema.putArray("required").add("message");
    ObjectNode properties = requestSchema.putObject("properties");
    properties.putObject("message").put("type", "string").put("description",
        "One or more complete HL7 v2 messages. Each message starts with an MSH segment.");
    ObjectNode profile = properties.putObject("profile");
    profile.put("type", "string");
    profile.put("description", "Profile to validate against. Leave out to detect it from MSH-21. "
        + profileDescriptions(gateway));
    ArrayNode profileKeys = profile.putArray("enum");
    for (ValidationProfile p : gateway.getProfiles()) {
      profileKeys.add(p.getKey());
    }
    properties.putObject("includeAllFindings").put("type", "boolean").put("description",
        "List routine findings (empty RE elements, value sets not checked) too. Default false.");

    ObjectNode responses = validate.putObject("responses");
    responses.putObject("200").put("description", "Validation results. 'pass' is true when "
        + "every message passed; each message lists its errors and other notable findings.")
        .putObject("content").putObject("application/json").putObject("schema")
        .put("$ref", "#/components/schemas/ValidationResponse");
    responses.putObject("400").put("description", "The request could not be used");
    responses.putObject("413").put("description", "Too many messages or too much text");
    responses.putObject("429").put("description", "Rate limit reached; see Retry-After");
    responses.putObject("502").put("description", "The validation service is unavailable");

    ObjectNode profiles = paths.putObject("/profiles").putObject("get");
    profiles.put("operationId", "listValidationProfiles");
    profiles.put("summary", "List the validators and profiles messages can be checked against");
    profiles.putObject("responses").putObject("200").put("description", "The profiles");

    ObjectNode schemas = doc.putObject("components").putObject("schemas");
    ObjectNode response = schemas.putObject("ValidationResponse");
    response.put("type", "object");
    ObjectNode responseProperties = response.putObject("properties");
    responseProperties.putObject("pass").put("type", "boolean");
    responseProperties.putObject("summary").put("type", "string");
    responseProperties.putObject("messageCount").put("type", "integer");
    responseProperties.putObject("passed").put("type", "integer");
    responseProperties.putObject("failed").put("type", "integer");
    responseProperties.putObject("notValidated").put("type", "integer");
    responseProperties.putObject("guidance").put("type", "string");
    responseProperties.putObject("messages").put("type", "array").putObject("items")
        .put("$ref", "#/components/schemas/MessageResult");

    ObjectNode message = schemas.putObject("MessageResult");
    message.put("type", "object");
    ObjectNode messageProperties = message.putObject("properties");
    messageProperties.putObject("index").put("type", "integer");
    messageProperties.putObject("messageControlId").put("type", "string");
    messageProperties.putObject("status").put("type", "string").putArray("enum")
        .add(ValidationJson.STATUS_PASS).add(ValidationJson.STATUS_FAIL)
        .add(ValidationJson.STATUS_NOT_VALIDATED);
    messageProperties.putObject("problem").put("type", "string")
        .put("description", "Why the message could not be validated");
    messageProperties.putObject("profile").put("type", "object");
    messageProperties.putObject("counts").put("type", "object");
    ObjectNode finding = messageProperties.putObject("findings").put("type", "array")
        .putObject("items");
    finding.put("type", "object");
    ObjectNode findingProperties = finding.putObject("properties");
    findingProperties.putObject("classification").put("type", "string")
        .put("description", "Error, Warning, Alert, Spec Error, or Informational");
    findingProperties.putObject("category").put("type", "string");
    findingProperties.putObject("line").put("type", "integer")
        .put("description", "Segment number in the message, starting at 1");
    findingProperties.putObject("path").put("type", "string")
        .put("description", "Element location, for example PID[1]-3[2].5");
    findingProperties.putObject("description").put("type", "string");
    messageProperties.putObject("routineFindings").put("type", "object");
    return doc;
  }

  private static String profileDescriptions(ValidationGateway gateway) {
    StringBuilder text = new StringBuilder();
    for (ValidationProfile p : gateway.getProfiles()) {
      text.append(text.length() == 0 ? "" : "; ").append(p.getKey()).append(" = ")
          .append(p.getDescription()).append(" (MSH-21 ").append(p.getMessageProfileId())
          .append(")");
    }
    return text.toString();
  }
}
