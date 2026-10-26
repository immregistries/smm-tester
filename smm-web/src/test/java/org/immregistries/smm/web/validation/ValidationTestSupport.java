package org.immregistries.smm.web.validation;

import java.util.List;
import org.immregistries.smm.validation.HL7Validator;
import org.immregistries.smm.validation.RateLimiter;
import org.immregistries.smm.validation.ValidationException;
import org.immregistries.smm.validation.ValidationFinding;
import org.immregistries.smm.validation.ValidationProfile;
import org.immregistries.smm.validation.ValidationResult;
import org.immregistries.smm.validation.ValidationService;
import org.immregistries.smm.validation.ValidatorSource;

/**
 * A gateway backed by a fake validator, so tests never call the real validation service.
 */
final class ValidationTestSupport {

  static final String VXU = "MSH|^~\\&|SMM|DIST|CHIRP|ISDH|20261026100000-0600||VXU^V04^VXU_V04"
      + "|CTRL-1|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS|SCHOOL|CHIRP\n"
      + "PID|1||SMM-1^^^EX^MR||Richardson^Russell^^^^^L\n"
      + "OBX|1|CE|64990-5^Grade in school^LN|1|3^Third grade^L||||||F";

  private ValidationTestSupport() {}

  /** Fails messages that contain a grade OBX, the way the Z22 profile does. */
  static final HL7Validator FAKE_VALIDATOR = new HL7Validator() {
    @Override
    public ValidatorSource getSource() {
      return ValidatorSource.VALITHEUS_IZ_EDITION2;
    }

    @Override
    public ValidationResult validate(ValidationProfile profile, String message)
        throws ValidationException {
      if (message.contains("64990-5")) {
        return new ValidationResult("VXU-Z22-Send", "Z22", "7", 1, 1, 1, 0, List.of(
            new ValidationFinding(ValidationFinding.ERROR, "Code Not Found", 3, 1, "OBX[1]-3[1]",
                "The code '64990-5' is not member of the value set [NIP003_Z22]"),
            new ValidationFinding(ValidationFinding.WARNING, ValidationFinding.CATEGORY_RE_USAGE,
                2, 1, "PID[1]-7[1]", "Field PID-7 is missing"),
            new ValidationFinding(ValidationFinding.ALERT, "Value Set", 1, 1, "MSH[1]-3[1]",
                "The value set HL70361 has been excluded from the validation")));
      }
      return new ValidationResult("VXU-Z22-Send", "Z22", "8", 0, 0, 0, 0, List.of());
    }
  };

  static ValidationGateway gateway(int messagesPerMinute, int maxMessages) {
    return new ValidationGateway(new ValidationService(List.of(FAKE_VALIDATOR)),
        new RateLimiter(messagesPerMinute, 1000), new RateLimiter(5, Integer.MAX_VALUE),
        maxMessages, 10_000, false);
  }
}
