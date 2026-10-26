package org.immregistries.smm.validation;

/**
 * Validates messages against the profiles of one validator source.
 */
public interface HL7Validator {

  ValidatorSource getSource();

  /**
   * @param profile one of this validator's profiles
   * @param message one HL7 v2 message with segments separated by carriage returns
   */
  ValidationResult validate(ValidationProfile profile, String message) throws ValidationException;
}
