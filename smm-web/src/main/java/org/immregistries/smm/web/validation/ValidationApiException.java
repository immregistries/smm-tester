package org.immregistries.smm.web.validation;

/**
 * A validation request that cannot be carried out, with the HTTP status that describes why.
 */
public class ValidationApiException extends Exception {
  private static final long serialVersionUID = 1L;

  private final int status;
  private final long retryAfterSeconds;

  private ValidationApiException(int status, String message, long retryAfterSeconds) {
    super(message);
    this.status = status;
    this.retryAfterSeconds = retryAfterSeconds;
  }

  public static ValidationApiException badRequest(String message) {
    return new ValidationApiException(400, message, 0);
  }

  public static ValidationApiException tooLarge(String message) {
    return new ValidationApiException(413, message, 0);
  }

  public static ValidationApiException tooManyRequests(String message, long retryAfterSeconds) {
    return new ValidationApiException(429, message, retryAfterSeconds);
  }

  public int getStatus() {
    return status;
  }

  /**
   * @return seconds to wait before retrying, or 0 when not rate limited
   */
  public long getRetryAfterSeconds() {
    return retryAfterSeconds;
  }
}
