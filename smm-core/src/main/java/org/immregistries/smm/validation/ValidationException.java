package org.immregistries.smm.validation;

/**
 * A message could not be validated, either because the validator rejected it as unreadable or
 * because the validator could not be reached.
 */
public class ValidationException extends Exception {
  private static final long serialVersionUID = 1L;

  private final boolean validatorUnavailable;

  public ValidationException(String message, boolean validatorUnavailable) {
    super(message);
    this.validatorUnavailable = validatorUnavailable;
  }

  public ValidationException(String message, boolean validatorUnavailable, Throwable cause) {
    super(message, cause);
    this.validatorUnavailable = validatorUnavailable;
  }

  /**
   * @return true when the problem is the validator service (unreachable or failing), false when it
   *         is the message itself
   */
  public boolean isValidatorUnavailable() {
    return validatorUnavailable;
  }
}
