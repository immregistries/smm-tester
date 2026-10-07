package org.immregistries.smm.web.auth;

import java.io.IOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The boundary between SMM and the service that authenticates people. SMM only needs to start a
 * sign-in, receive the result at <code>/login</code>, and know where to send someone after they
 * sign out; everything else (sessions, workspaces, permissions) is handled by SMM itself. A
 * deployment that uses a different credential method provides another implementation.
 */
public interface AuthenticationProvider {

  /**
   * @return a short name for the provider, shown to users and in logs
   */
  String getName();

  /**
   * Sends the browser to the provider to sign in. After signing in the provider returns the
   * browser to <code>/login</code>.
   *
   * @param requestedUrl the absolute URL to return to once sign-in is complete
   */
  void startSignIn(HttpServletRequest request, HttpServletResponse response, String requestedUrl)
      throws IOException;

  /**
   * Completes a sign-in when the provider returns the browser to <code>/login</code>.
   */
  SignInResult completeSignIn(HttpServletRequest request);

  /**
   * @return where to send the browser after the SMM session ends
   */
  String getSignOutUrl(HttpServletRequest request);
}
