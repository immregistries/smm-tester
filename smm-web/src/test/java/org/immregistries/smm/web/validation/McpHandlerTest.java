package org.immregistries.smm.web.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class McpHandlerTest {

  private final McpHandler handler = new McpHandler(ValidationTestSupport.gateway(2, 5));

  private JsonNode call(String json) throws Exception {
    return handler.handle(ValidationJson.MAPPER.readTree(json), "client");
  }

  private ObjectNode toolCall(String arguments) {
    ObjectNode request = ValidationJson.MAPPER.createObjectNode();
    request.put("jsonrpc", "2.0");
    request.put("id", 3);
    request.put("method", "tools/call");
    ObjectNode params = request.putObject("params");
    params.put("name", McpHandler.TOOL_VALIDATE);
    try {
      params.set("arguments", ValidationJson.MAPPER.readTree(arguments));
    } catch (Exception e) {
      throw new IllegalArgumentException(e);
    }
    return request;
  }

  @Test
  public void initializesWithRequestedOrLatestVersion() throws Exception {
    JsonNode response = call("{\"jsonrpc\":\"2.0\",\"id\":1,\"method\":\"initialize\","
        + "\"params\":{\"protocolVersion\":\"2025-03-26\",\"capabilities\":{},"
        + "\"clientInfo\":{\"name\":\"test\",\"version\":\"1\"}}}");
    assertEquals(1, response.get("id").asInt());
    JsonNode result = response.get("result");
    assertEquals("2025-03-26", result.get("protocolVersion").asText());
    assertTrue(result.get("capabilities").has("tools"));
    assertEquals("smm-hl7-validation", result.get("serverInfo").get("name").asText());

    JsonNode future = call("{\"jsonrpc\":\"2.0\",\"id\":\"a\",\"method\":\"initialize\","
        + "\"params\":{\"protocolVersion\":\"2099-01-01\"}}");
    assertEquals("a", future.get("id").asText());
    assertEquals(McpHandler.LATEST_PROTOCOL_VERSION,
        future.get("result").get("protocolVersion").asText());
  }

  @Test
  public void ignoresNotificationsAndResponses() throws Exception {
    assertNull(call("{\"jsonrpc\":\"2.0\",\"method\":\"notifications/initialized\"}"));
    assertNull(call("{\"jsonrpc\":\"2.0\",\"id\":5,\"result\":{}}"));
  }

  @Test
  public void listsTools() throws Exception {
    JsonNode tools = call("{\"jsonrpc\":\"2.0\",\"id\":2,\"method\":\"tools/list\"}")
        .get("result").get("tools");
    assertEquals(2, tools.size());
    JsonNode validate = tools.get(0);
    assertEquals(McpHandler.TOOL_VALIDATE, validate.get("name").asText());
    assertEquals("message", validate.get("inputSchema").get("required").get(0).asText());
    assertEquals(8, validate.get("inputSchema").get("properties").get("profile").get("enum")
        .size());
    assertTrue(validate.get("annotations").get("readOnlyHint").asBoolean());
  }

  @Test
  public void validatesMessage() throws Exception {
    JsonNode result =
        handler.handle(toolCall("{\"message\":" + ValidationJson.MAPPER
            .writeValueAsString(ValidationTestSupport.VXU) + "}"), "client").get("result");
    assertFalse(result.get("isError").asBoolean());
    assertTrue(result.get("content").get(0).get("text").asText().contains("FAIL against vxu-z22"));
    assertEquals("FAIL",
        result.get("structuredContent").get("messages").get(0).get("status").asText());
  }

  @Test
  public void reportsToolProblemsAsToolErrors() throws Exception {
    String vxu = ValidationJson.MAPPER.writeValueAsString(ValidationTestSupport.VXU);
    JsonNode unknownProfile = handler
        .handle(toolCall("{\"message\":" + vxu + ",\"profile\":\"nope\"}"), "client")
        .get("result");
    assertTrue(unknownProfile.get("isError").asBoolean());
    assertTrue(unknownProfile.get("content").get(0).get("text").asText().contains("vxu-z22"));

    // The fake gateway allows two messages a minute
    handler.handle(toolCall("{\"message\":" + vxu + "}"), "client");
    handler.handle(toolCall("{\"message\":" + vxu + "}"), "client");
    JsonNode limited = handler.handle(toolCall("{\"message\":" + vxu + "}"), "client")
        .get("result");
    assertTrue(limited.get("isError").asBoolean());
    assertTrue(limited.get("content").get(0).get("text").asText().contains("Try again"));
  }

  @Test
  public void reportsProtocolErrors() throws Exception {
    assertEquals(McpHandler.METHOD_NOT_FOUND,
        call("{\"jsonrpc\":\"2.0\",\"id\":9,\"method\":\"resources/list\"}").get("error")
            .get("code").asInt());
    assertEquals(McpHandler.INVALID_PARAMS, handler.handle(toolCall("{}"), "client")
        .get("error").get("code").asInt());
    assertEquals(McpHandler.INVALID_REQUEST, call("[1]").get("error").get("code").asInt());
  }

  @Test
  public void listsProfiles() throws Exception {
    JsonNode result = call("{\"jsonrpc\":\"2.0\",\"id\":4,\"method\":\"tools/call\","
        + "\"params\":{\"name\":\"" + McpHandler.TOOL_LIST_PROFILES + "\",\"arguments\":{}}}")
        .get("result");
    assertTrue(result.get("content").get(0).get("text").asText().contains("rsp-z42"));
    assertEquals(8, result.get("structuredContent").get("validators").get(0).get("profiles")
        .size());
  }
}
