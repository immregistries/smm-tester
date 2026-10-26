package org.immregistries.smm.web.validation;

import java.util.List;
import org.immregistries.smm.SoftwareVersion;
import org.immregistries.smm.validation.MessageValidation;
import org.immregistries.smm.validation.ValidationProfile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Handles Model Context Protocol (MCP) JSON-RPC messages for the HL7 validation tools. The server
 * is stateless: it keeps no sessions and answers each request on its own, so it runs over plain
 * HTTP POST (the Streamable HTTP transport) without server-sent events.
 */
class McpHandler {

  static final String LATEST_PROTOCOL_VERSION = "2025-06-18";
  private static final List<String> SUPPORTED_PROTOCOL_VERSIONS =
      List.of(LATEST_PROTOCOL_VERSION, "2025-03-26", "2024-11-05");

  static final String TOOL_VALIDATE = "validate_hl7_message";
  static final String TOOL_LIST_PROFILES = "list_validation_profiles";

  static final int PARSE_ERROR = -32700;
  static final int INVALID_REQUEST = -32600;
  static final int METHOD_NOT_FOUND = -32601;
  static final int INVALID_PARAMS = -32602;

  private static final String INSTRUCTIONS = "Validates HL7 v2.5.1 immunization messages (VXU, "
      + "ACK, QBP, RSP) against the CDC Immunization IG Release 1.5 conformance profiles. Call "
      + TOOL_VALIDATE + " on every HL7 v2 message you write or change. A message passes when it "
      + "has no errors; fix only what each error names, then validate again. Use synthetic test "
      + "data only: messages are sent to an outside validation service.";

  private final ValidationGateway gateway;

  McpHandler(ValidationGateway gateway) {
    this.gateway = gateway;
  }

  /**
   * @param message one JSON-RPC message from the client
   * @param client identifies the caller for rate limiting
   * @return the response, or null for notifications and responses, which get no reply
   */
  ObjectNode handle(JsonNode message, String client) {
    if (!message.isObject() || !message.path("method").isTextual()) {
      if (message.isObject() && (message.has("result") || message.has("error"))) {
        return null;
      }
      return error(null, INVALID_REQUEST, "Not a JSON-RPC request");
    }
    JsonNode id = message.get("id");
    if (id == null || id.isNull()) {
      // Notifications, such as notifications/initialized, need no reply
      return null;
    }
    String method = message.get("method").asText();
    JsonNode params = message.path("params");
    switch (method) {
      case "initialize":
        return result(id, initialize(params));
      case "ping":
        return result(id, ValidationJson.MAPPER.createObjectNode());
      case "tools/list":
        return result(id, listTools());
      case "tools/call":
        return callTool(id, params, client);
      default:
        return error(id, METHOD_NOT_FOUND, "Method not found: " + method);
    }
  }

  private ObjectNode initialize(JsonNode params) {
    String requested = params.path("protocolVersion").asText("");
    ObjectNode result = ValidationJson.MAPPER.createObjectNode();
    result.put("protocolVersion", SUPPORTED_PROTOCOL_VERSIONS.contains(requested) ? requested
        : LATEST_PROTOCOL_VERSION);
    result.putObject("capabilities").putObject("tools").put("listChanged", false);
    ObjectNode serverInfo = result.putObject("serverInfo");
    serverInfo.put("name", "smm-hl7-validation");
    serverInfo.put("title", "SMM HL7 v2 Validation");
    serverInfo.put("version", SoftwareVersion.VERSION);
    result.put("instructions", INSTRUCTIONS);
    return result;
  }

  private ObjectNode listTools() {
    ObjectNode result = ValidationJson.MAPPER.createObjectNode();
    ArrayNode tools = result.putArray("tools");

    ObjectNode validate = tools.addObject();
    validate.put("name", TOOL_VALIDATE);
    validate.put("title", "Validate HL7 v2 message");
    validate.put("description", "Validates one or more HL7 v2.5.1 immunization messages against "
        + "the CDC Immunization IG Release 1.5 conformance profiles (Valitheus Immunization Test "
        + "Suite). The profile is detected from MSH-21.1 (for example Z22) unless one is given. "
        + "Returns PASS or FAIL for each message with every error (segment line, element path, "
        + "description). Segments may be separated by new lines. At most "
        + gateway.getMaxMessages() + " messages per call. Use synthetic test data only.");
    ObjectNode schema = validate.putObject("inputSchema");
    schema.put("type", "object");
    ObjectNode properties = schema.putObject("properties");
    properties.putObject("message").put("type", "string").put("description",
        "One or more complete HL7 v2 messages, each starting with an MSH segment");
    ObjectNode profile = properties.putObject("profile");
    profile.put("type", "string");
    profile.put("description",
        "Profile to validate against; leave out to detect it from MSH-21. " + profileList());
    ArrayNode keys = profile.putArray("enum");
    for (ValidationProfile p : gateway.getProfiles()) {
      keys.add(p.getKey());
    }
    properties.putObject("include_all_findings").put("type", "boolean").put("description",
        "Also list routine findings (empty RE elements, value sets not checked). Default false.");
    schema.putArray("required").add("message");
    ObjectNode annotations = validate.putObject("annotations");
    annotations.put("title", "Validate HL7 v2 message");
    annotations.put("readOnlyHint", true);
    annotations.put("idempotentHint", true);
    annotations.put("openWorldHint", true);

    ObjectNode list = tools.addObject();
    list.put("name", TOOL_LIST_PROFILES);
    list.put("title", "List validation profiles");
    list.put("description",
        "Lists the HL7 v2 conformance profiles messages can be validated against.");
    list.putObject("inputSchema").put("type", "object").putObject("properties");
    ObjectNode listAnnotations = list.putObject("annotations");
    listAnnotations.put("title", "List validation profiles");
    listAnnotations.put("readOnlyHint", true);
    listAnnotations.put("idempotentHint", true);
    listAnnotations.put("openWorldHint", false);
    return result;
  }

  private ObjectNode callTool(JsonNode id, JsonNode params, String client) {
    String name = params.path("name").asText("");
    JsonNode arguments = params.path("arguments");
    if (name.equals(TOOL_LIST_PROFILES)) {
      ObjectNode profiles = ValidationJson.profilesToJson(gateway.getService().getSources());
      return result(id, toolResult(profilesText(), profiles, false));
    }
    if (!name.equals(TOOL_VALIDATE)) {
      return error(id, INVALID_PARAMS, "Unknown tool: " + name);
    }
    if (!arguments.path("message").isTextual()) {
      return error(id, INVALID_PARAMS, "The 'message' argument (HL7 text) is required");
    }
    boolean includeAll = arguments.path("include_all_findings").asBoolean(false);
    try {
      List<MessageValidation> validations = gateway.validate(arguments.get("message").asText(),
          arguments.path("profile").asText(null), client);
      return result(id, toolResult(ValidationJson.toText(validations, includeAll),
          ValidationJson.toJson(validations, includeAll), false));
    } catch (ValidationApiException e) {
      // Tool errors are reported in the result so the model can read them and adjust
      return result(id, toolResult(e.getMessage(), null, true));
    }
  }

  private static ObjectNode toolResult(String text, ObjectNode structured, boolean isError) {
    ObjectNode result = ValidationJson.MAPPER.createObjectNode();
    result.putArray("content").addObject().put("type", "text").put("text", text);
    if (structured != null) {
      result.set("structuredContent", structured);
    }
    result.put("isError", isError);
    return result;
  }

  private String profileList() {
    StringBuilder text = new StringBuilder();
    for (ValidationProfile p : gateway.getProfiles()) {
      text.append(text.length() == 0 ? "" : "; ").append(p.getKey()).append(" = ")
          .append(p.getDescription());
    }
    return text.toString();
  }

  private String profilesText() {
    StringBuilder text = new StringBuilder("Profiles (detected from MSH-21.1 unless chosen):\n");
    for (ValidationProfile p : gateway.getProfiles()) {
      text.append("- ").append(p.getKey()).append(" (MSH-21 ").append(p.getMessageProfileId())
          .append(", ").append(p.getMessageType()).append("): ").append(p.getDescription())
          .append("\n");
    }
    return text.toString();
  }

  private static ObjectNode result(JsonNode id, JsonNode result) {
    ObjectNode response = ValidationJson.MAPPER.createObjectNode();
    response.put("jsonrpc", "2.0");
    response.set("id", id);
    response.set("result", result);
    return response;
  }

  static ObjectNode error(JsonNode id, int code, String message) {
    ObjectNode response = ValidationJson.MAPPER.createObjectNode();
    response.put("jsonrpc", "2.0");
    response.set("id", id == null ? ValidationJson.MAPPER.nullNode() : id);
    ObjectNode error = response.putObject("error");
    error.put("code", code);
    error.put("message", message);
    return response;
  }
}
