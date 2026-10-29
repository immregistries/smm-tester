package org.immregistries.smm.web.auth;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * DEVELOPMENT ONLY. Signs everyone in as a fixed local demo user without contacting InteropHub.
 * It is used only when the setting <code>smm.dev.login</code> (system property, environment
 * variable <code>SMM_DEV_LOGIN</code> or context parameter) is <code>true</code>. It is off by
 * default. Never enable it on a server other people can reach.
 */
public class DevLoginAuthenticationProvider implements AuthenticationProvider {

  private static final Logger LOG = Logger.getLogger(DevLoginAuthenticationProvider.class.getName());

  public static final String DEV_CODE = "dev-login";
  public static final String DEV_EMAIL = "demo@localhost";
  public static final String DEV_NAME = "Demo User";
  public static final String DEV_ORGANIZATION = "Local Demo";
  public static final String DEV_TITLE = "Developer";

  public DevLoginAuthenticationProvider() {
    LOG.warning("*** SMM_DEV_LOGIN is ON: every visitor is signed in as " + DEV_EMAIL
        + " without authentication. For local demos only, turn it off (smm.dev.login) for anything"
        + " else. ***");
  }

  @Override
  public String getName() {
    return "Dev Login";
  }

  @Override
  public void startSignIn(HttpServletRequest request, HttpServletResponse response,
      String requestedUrl) throws IOException {
    String target = request.getContextPath() + "/login?code=" + DEV_CODE;
    String path = toLocalPath(requestedUrl, request.getContextPath());
    if (path != null) {
      target += "&return=" + URLEncoder.encode(path, StandardCharsets.UTF_8);
    }
    response.sendRedirect(target);
  }

  @Override
  public SignInResult completeSignIn(HttpServletRequest request) {
    if (!DEV_CODE.equals(request.getParameter("code"))) {
      return SignInResult.failure("Dev login: unexpected sign-in code.");
    }
    LOG.warning("Dev login: signing in " + DEV_EMAIL + " from " + request.getRemoteAddr());
    String target = toLocalPath(request.getParameter("return"), request.getContextPath());
    if (target == null) {
      target = request.getContextPath() + "/HomeServlet";
    }
    AuthenticatedIdentity identity = new AuthenticatedIdentity(getName(), "dev-user", DEV_EMAIL,
        DEV_NAME, DEV_ORGANIZATION, DEV_TITLE);
    return SignInResult.success(identity, target);
  }

  @Override
  public String getSignOutUrl(HttpServletRequest request) {
    return request.getContextPath() + "/HomeServlet";
  }

  /**
   * Reduces a URL to a path (and query) inside this application, so a sign-in can never redirect
   * to another site. Returns null when the URL does not point into the application.
   */
  static String toLocalPath(String url, String contextPath) {
    if (url == null || url.isEmpty()) {
      return null;
    }
    try {
      URI uri = new URI(URLDecoder.decode(url, "UTF-8").replace(" ", "%20"));
      String path = uri.getRawPath();
      if (path == null || !(path.equals(contextPath) || path.startsWith(contextPath + "/"))) {
        return null;
      }
      return uri.getRawQuery() == null ? path : path + "?" + uri.getRawQuery();
    } catch (Exception e) {
      return null;
    }
  }
}
