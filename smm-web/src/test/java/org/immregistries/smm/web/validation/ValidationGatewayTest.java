package org.immregistries.smm.web.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import org.immregistries.smm.validation.MessageValidation;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class ValidationGatewayTest {

  private static final String PASSING_VXU = ValidationTestSupport.VXU.replace(
      "\nOBX|1|CE|64990-5^Grade in school^LN|1|3^Third grade^L||||||F", "");

  private static int statusOf(ValidationGateway gateway, String text, String profile) {
    try {
      gateway.validate(text, profile, "client");
      fail("Expected the request to be refused");
      return 0;
    } catch (ValidationApiException e) {
      return e.getStatus();
    }
  }

  @Test
  public void refusesUnusableRequests() {
    ValidationGateway gateway = ValidationTestSupport.gateway(100, 2);
    assertEquals(400, statusOf(gateway, "   ", null));
    assertEquals(400, statusOf(gateway, null, null));
    assertEquals(400, statusOf(gateway, PASSING_VXU, "vxu-z99"));
    assertEquals(413, statusOf(gateway, PASSING_VXU + "\n" + PASSING_VXU + "\n" + PASSING_VXU,
        null));
    assertEquals(413, statusOf(gateway, "MSH|" + "x".repeat(10_001), null));
  }

  @Test
  public void limitsMessagesPerMinute() throws Exception {
    ValidationGateway gateway = ValidationTestSupport.gateway(3, 5);
    assertEquals(2, gateway.validate(PASSING_VXU + "\n" + PASSING_VXU, null, "a").size());
    try {
      gateway.validate(PASSING_VXU + "\n" + PASSING_VXU, null, "a");
      fail("Expected the rate limit");
    } catch (ValidationApiException e) {
      assertEquals(429, e.getStatus());
      assertTrue(e.getRetryAfterSeconds() > 0);
    }
    assertEquals(1, gateway.validate(PASSING_VXU, null, "b").size());
  }

  @Test
  public void limitsRequestsPerMinute() throws Exception {
    ValidationGateway gateway = ValidationTestSupport.gateway(100, 5);
    for (int i = 0; i < 5; i++) {
      gateway.checkRequestRate("a");
    }
    try {
      gateway.checkRequestRate("a");
      fail("Expected the request limit");
    } catch (ValidationApiException e) {
      assertEquals(429, e.getStatus());
    }
  }

  @Test
  public void writesCompactJson() throws Exception {
    ValidationGateway gateway = ValidationTestSupport.gateway(100, 5);
    List<MessageValidation> validations =
        gateway.validate(ValidationTestSupport.VXU + "\n" + PASSING_VXU, null, "a");
    ObjectNode json = ValidationJson.toJson(validations, false);
    assertFalse(json.get("pass").asBoolean());
    assertEquals(1, json.get("passed").asInt());
    assertEquals(1, json.get("failed").asInt());
    JsonNode first = json.get("messages").get(0);
    assertEquals("FAIL", first.get("status").asText());
    assertEquals("CTRL-1", first.get("messageControlId").asText());
    assertEquals("vxu-z22", first.get("profile").get("key").asText());
    assertEquals(1, first.get("findings").size());
    assertEquals("OBX[1]-3[1]", first.get("findings").get(0).get("path").asText());
    assertEquals(1, first.get("routineFindings").get("reUsageWarnings").asInt());
    assertEquals(1, first.get("routineFindings").get("valueSetsNotChecked").asInt());
    assertEquals("PASS", json.get("messages").get(1).get("status").asText());

    ObjectNode all = ValidationJson.toJson(validations, true);
    assertEquals(3, all.get("messages").get(0).get("findings").size());
  }

  @Test
  public void writesTextReport() throws Exception {
    ValidationGateway gateway = ValidationTestSupport.gateway(100, 5);
    String text =
        ValidationJson.toText(gateway.validate(ValidationTestSupport.VXU, null, "a"), false);
    assertTrue(text.startsWith("The message failed validation."));
    assertTrue(text.contains("Message 1 (MSH-10 CTRL-1): FAIL against vxu-z22"));
    assertTrue(text.contains("[Error/Code Not Found] line 3 OBX[1]-3[1]"));
    assertFalse(text.contains("PID[1]-7[1]"));
  }

  @Test
  public void readsMessageControlId() {
    assertEquals("CTRL-1", ValidationJson.readMessageControlId(PASSING_VXU.replace('\n', '\r')));
    assertEquals(null, ValidationJson.readMessageControlId("MSH|^~\\&|A"));
    assertEquals(null, ValidationJson.readMessageControlId("PID|1"));
  }
}
