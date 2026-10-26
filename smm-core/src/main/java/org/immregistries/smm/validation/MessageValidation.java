package org.immregistries.smm.validation;

/**
 * The validation of one message from a submission: either a result from the validator, or the
 * reason no result could be produced.
 */
public final class MessageValidation {

  private final int index;
  private final String message;
  private final ValidationProfile profile;
  private final ValidatorSource source;
  private final ValidationResult result;
  private final String problem;
  private final boolean validatorUnavailable;

  private MessageValidation(int index, String message, ValidationProfile profile,
      ValidatorSource source, ValidationResult result, String problem,
      boolean validatorUnavailable) {
    this.index = index;
    this.message = message;
    this.profile = profile;
    this.source = source;
    this.result = result;
    this.problem = problem;
    this.validatorUnavailable = validatorUnavailable;
  }

  static MessageValidation completed(int index, String message, ValidationProfile profile,
      ValidatorSource source, ValidationResult result) {
    return new MessageValidation(index, message, profile, source, result, null, false);
  }

  static MessageValidation notValidated(int index, String message, ValidationProfile profile,
      ValidatorSource source, String problem, boolean validatorUnavailable) {
    return new MessageValidation(index, message, profile, source, null, problem,
        validatorUnavailable);
  }

  /**
   * @return the message's position in the submission, starting at 1
   */
  public int getIndex() {
    return index;
  }

  /**
   * @return the message as it was sent for validation, with segments separated by carriage
   *         returns
   */
  public String getMessage() {
    return message;
  }

  /**
   * @return the profile used, or null if none could be chosen
   */
  public ValidationProfile getProfile() {
    return profile;
  }

  /**
   * @return the validator source that owns the profile, or null if no profile was chosen
   */
  public ValidatorSource getSource() {
    return source;
  }

  /**
   * @return the validation result, or null if the message could not be validated
   */
  public ValidationResult getResult() {
    return result;
  }

  /**
   * @return why the message could not be validated, or null if it was
   */
  public String getProblem() {
    return problem;
  }

  /**
   * @return true when the message was not validated because the validator was unavailable
   */
  public boolean isValidatorUnavailable() {
    return validatorUnavailable;
  }

  /**
   * @return true when the message was validated and has no errors
   */
  public boolean isPass() {
    return result != null && result.isPass();
  }
}
