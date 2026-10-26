package org.immregistries.smm.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class ValidationServiceTest {

  private static final String VXU = "MSH|^~\\&|SMM|DOE-DISTRICT-0123|CHIRP|ISDH|20261026100000-0600"
      + "||VXU^V04^VXU_V04|SMM-ROSTER-1|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS|DOE-SCHOOL-0456|CHIRP";
  private static final String PID = "PID|1||SMM-000001^^^EXAMPLE^MR||Richardson^Russell^^^^^L";
  private static final String QBP = "MSH|^~\\&|EHR|EHRFAC|IIS|IISFAC|20241031145233-0500"
      + "||QBP^Q11^QBP_Q11|Q-1|P|2.5.1|||ER|AL|||||Z44^CDCPHINVS";

  /** Records what it is asked to validate and returns a passing result. */
  private static class FakeValidator implements HL7Validator {
    private final List<String> validated = new ArrayList<>();
    private final ValidationException failure;

    FakeValidator(ValidationException failure) {
      this.failure = failure;
    }

    @Override
    public ValidatorSource getSource() {
      return ValidatorSource.VALITHEUS_IZ_EDITION2;
    }

    @Override
    public ValidationResult validate(ValidationProfile profile, String message)
        throws ValidationException {
      if (failure != null) {
        throw failure;
      }
      validated.add(profile.getKey());
      return new ValidationResult("id", "name", "1", 0, 0, 0, 0, List.of());
    }
  }

  @Test
  public void splitsMessagesWithAnyLineEndingsAndDropsBatchSegments() {
    String text = "﻿FHS|^~\\&|X\r\nBHS|^~\\&|X\r\n" + VXU + "\r\n" + PID + "\r\n\r\n" + QBP
        + "\n" + "QPD|Z44^Request^CDCPHINVS\rBTS|2\nFTS|1\n";
    List<String> messages = ValidationService.splitMessages(text);
    assertEquals(2, messages.size());
    assertEquals(VXU + "\r" + PID, messages.get(0));
    assertEquals(QBP + "\rQPD|Z44^Request^CDCPHINVS", messages.get(1));
  }

  @Test
  public void splitsEmptyTextIntoNoMessages() {
    assertTrue(ValidationService.splitMessages("  \n\n").isEmpty());
    assertTrue(ValidationService.splitMessages(null).isEmpty());
  }

  @Test
  public void readsEachMsh21Repetition() {
    assertEquals(List.of("Z22"), ValidationService.readMessageProfileIds(VXU + "\r" + PID));
    String twoProfiles = VXU.replace("Z22^CDCPHINVS", "Z99^LOCAL~z34^CDCPHINVS");
    assertEquals(List.of("Z99", "Z34"), ValidationService.readMessageProfileIds(twoProfiles));
    assertTrue(ValidationService.readMessageProfileIds("MSH|^~\\&|A|B").isEmpty());
    assertTrue(ValidationService.readMessageProfileIds(PID).isEmpty());
  }

  @Test
  public void detectsProfileFromMsh21() {
    ValidationService service = new ValidationService(List.of(new FakeValidator(null)));
    assertEquals("vxu-z22", service.detectProfile(VXU).getKey());
    assertEquals("qbp-z44", service.detectProfile(QBP).getKey());
    String twoProfiles = VXU.replace("Z22^CDCPHINVS", "Z99^LOCAL~Z34^CDCPHINVS");
    assertEquals("qbp-z34", service.detectProfile(twoProfiles).getKey());
    assertNull(service.detectProfile(VXU.replace("Z22", "Z99")));
  }

  @Test
  public void findsProfileIgnoringCase() {
    ValidationService service = new ValidationService(List.of(new FakeValidator(null)));
    assertEquals("rsp-z42", service.findProfile(" RSP-Z42 ").getKey());
    assertNull(service.findProfile("vxu-z99"));
    assertNull(service.findProfile(null));
  }

  @Test
  public void validatesEachMessageWithItsDetectedProfile() {
    FakeValidator validator = new FakeValidator(null);
    ValidationService service = new ValidationService(List.of(validator));
    List<MessageValidation> results = service.validate(List.of(VXU, QBP), null);
    assertEquals(List.of("vxu-z22", "qbp-z44"), validator.validated);
    assertEquals(2, results.size());
    assertEquals(2, results.get(1).getIndex());
    assertTrue(results.get(0).isPass());
    assertSame(ValidatorSource.VALITHEUS_IZ_EDITION2, results.get(0).getSource());
  }

  @Test
  public void usesChosenProfileForEveryMessage() {
    FakeValidator validator = new FakeValidator(null);
    ValidationService service = new ValidationService(List.of(validator));
    service.validate(List.of(VXU, QBP), "qbp-z34");
    assertEquals(List.of("qbp-z34", "qbp-z34"), validator.validated);
  }

  @Test(expected = IllegalArgumentException.class)
  public void rejectsUnknownProfile() {
    new ValidationService(List.of(new FakeValidator(null))).validate(List.of(VXU), "nope");
  }

  @Test
  public void reportsMessagesThatCannotBeValidated() {
    FakeValidator validator = new FakeValidator(null);
    ValidationService service = new ValidationService(List.of(validator));
    List<MessageValidation> results =
        service.validate(List.of(PID, VXU.replace("Z22", "Z99")), null);
    assertTrue(validator.validated.isEmpty());
    assertFalse(results.get(0).isPass());
    assertTrue(results.get(0).getProblem().contains("MSH"));
    assertNull(results.get(1).getProfile());
    assertTrue(results.get(1).getProblem().contains("vxu-z22"));
  }

  @Test
  public void reportsValidatorFailures() {
    ValidationService service = new ValidationService(
        List.of(new FakeValidator(new ValidationException("Could not reach", true))));
    MessageValidation result = service.validate(List.of(VXU), null).get(0);
    assertNull(result.getResult());
    assertEquals("Could not reach", result.getProblem());
    assertTrue(result.isValidatorUnavailable());
    assertEquals("vxu-z22", result.getProfile().getKey());
  }
}
