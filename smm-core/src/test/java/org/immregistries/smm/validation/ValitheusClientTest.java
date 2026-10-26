package org.immregistries.smm.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.Test;

public class ValitheusClientTest {

  private static String readResource(String name) throws IOException {
    try (InputStream in = ValitheusClientTest.class.getResourceAsStream(name)) {
      return new String(in.readAllBytes(), StandardCharsets.UTF_8);
    }
  }

  @Test
  public void parsesReportWithErrors() throws Exception {
    // A real Valitheus response (informational and affirmative entries trimmed) for a roster VXU
    // with a non-standard PID-3.5 code and a grade OBX that the Z22 profile does not allow
    ValidationResult result =
        ValitheusClient.parseResponse(readResource("/validation/valitheus-report-two-errors.json"));

    assertFalse(result.isPass());
    assertEquals("VXU-Z22-Send", result.getProfileIdentifier());
    assertEquals(2, result.getErrorCount());
    assertEquals(11, result.getWarningCount());
    assertEquals(15, result.getAlertCount());
    assertEquals(0, result.getSpecErrorCount());
    assertEquals(11, result.getReUsageWarningCount());
    assertEquals(15, result.getExcludedValueSetAlertCount());

    // Errors are listed first even though the report lists warnings first
    assertEquals(ValidationFinding.ERROR, result.getFindings().get(0).getClassification());
    assertEquals(ValidationFinding.ERROR, result.getFindings().get(1).getClassification());

    List<ValidationFinding> notable = result.getNotableFindings();
    assertEquals(2, notable.size());
    for (ValidationFinding finding : notable) {
      assertEquals(ValidationFinding.ERROR, finding.getClassification());
      assertEquals("Code Not Found", finding.getCategory());
    }
    assertTrue(notable.stream().anyMatch(
        f -> f.getPath().equals("PID[1]-3[2].5") && f.getDescription().contains("'STU'")));
    assertTrue(notable.stream()
        .anyMatch(f -> f.getLine() == 5 && f.getDescription().contains("NIP003_Z22")));
  }

  @Test(expected = ValidationException.class)
  public void rejectsResponseWithoutReport() throws Exception {
    ValitheusClient.parseResponse("{\"type\":\"danger\",\"text\":\"internalError\"}");
  }

  @Test
  public void describesRejectionWithoutDebugDetails() {
    String body = "\"Sorry, cannot validate the message provided.\\nDEBUG:\\n"
        + "gov.nist.hit.core.service.exception.MessageValidationException: "
        + "No MSH Segment found in the message.\"";
    assertEquals(
        "Sorry, cannot validate the message provided. No MSH Segment found in the message.",
        ValitheusClient.describeRejection(body));
  }
}
