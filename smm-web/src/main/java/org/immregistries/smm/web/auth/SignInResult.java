package org.immregistries.smm.web.auth;

/**
 * The outcome of completing a sign-in: either an identity and where to go next, or the reason it
 * failed.
 */
public class SignInResult {

  private final AuthenticatedIdentity identity;
  private final String redirectTarget;
  private final String errorMessage;

  private SignInResult(AuthenticatedIdentity identity, String redirectTarget,
      String errorMessage) {
    this.identity = identity;
    this.redirectTarget = redirectTarget;
    this.errorMessage = errorMessage;
  }

  public static SignInResult success(AuthenticatedIdentity identity, String redirectTarget) {
    return new SignInResult(identity, redirectTarget, null);
  }

  public static SignInResult failure(String errorMessage) {
    return new SignInResult(null, null, errorMessage);
  }

  public boolean isSuccess() {
    return identity != null;
  }

  public AuthenticatedIdentity getIdentity() {
    return identity;
  }

  /**
   * @return the URL to send the browser to after a successful sign-in
   */
  public String getRedirectTarget() {
    return redirectTarget;
  }

  public String getErrorMessage() {
    return errorMessage;
  }
}
