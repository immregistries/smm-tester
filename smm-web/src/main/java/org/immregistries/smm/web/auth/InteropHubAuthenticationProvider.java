package org.immregistries.smm.web.auth;

import java.io.IOException;
import java.util.logging.Logger;
import org.immregistries.interophub.client.HubClientConfig;
import org.immregistries.interophub.client.HubExchangeResult;
import org.immregistries.interophub.client.HubRedirectDecision;
import org.immregistries.interophub.client.HubUserInfo;
import org.immregistries.interophub.client.InteropHubClient;
import org.immregistries.interophub.client.InteropHubClientFactory;
import org.immregistries.smm.web.SmmWebConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Signs people in through InteropHub. InteropHub authenticates the person and returns the browser
 * to <code>/login</code> with a one-time code, which is exchanged with InteropHub for the person's
 * identity.
 */
public class InteropHubAuthenticationProvider implements AuthenticationProvider {

  private static final Logger LOG =
      Logger.getLogger(InteropHubAuthenticationProvider.class.getName());
  private static final int CONNECT_TIMEOUT_MS = 8000;
  private static final int READ_TIMEOUT_MS = 12000;

  private final SmmWebConfig config;
  private volatile InteropHubClient client;
  private volatile String clientExternalUrl;

  public InteropHubAuthenticationProvider(SmmWebConfig config) {
    this.config = config;
  }

  @Override
  public String getName() {
    return "InteropHub";
  }

  @Override
  public void startSignIn(HttpServletRequest request, HttpServletResponse response,
      String requestedUrl) throws IOException {
    response.sendRedirect(getClient(request).buildLoginUrl(requestedUrl));
  }

  @Override
  public SignInResult completeSignIn(HttpServletRequest request) {
    String code = request.getParameter("code");
    if (code == null || code.trim().isEmpty()) {
      return SignInResult.failure("InteropHub did not return a sign-in code.");
    }
    InteropHubClient hubClient = getClient(request);
    HubExchangeResult result = hubClient.exchangeCode(code.trim(), request.getRemoteAddr());
    if (!result.isSuccess()) {
      LOG.warning("InteropHub code exchange failed: httpStatus=" + result.getHttpStatus()
          + " error=" + result.getErrorMessage());
      return SignInResult.failure("InteropHub could not confirm the sign-in (HTTP "
          + result.getHttpStatus() + "): " + result.getErrorMessage());
    }
    if (!result.hasRequiredUserInfo()) {
      return SignInResult.failure(
          "InteropHub did not provide a name, organization, title, and email for this account. "
              + "Please complete your InteropHub profile and sign in again.");
    }
    HubUserInfo user = result.getUserInfo();
    AuthenticatedIdentity identity = new AuthenticatedIdentity(getName(), user.getHubUserId(),
        user.getEmail(), user.getName(), user.getOrganization(), user.getTitle());
    HubRedirectDecision decision = hubClient.determineRedirectDecision(
        result.getRequestedUrlFromHub(), request.getContextPath() + "/HomeServlet");
    return SignInResult.success(identity, decision.getTarget());
  }

  @Override
  public String getSignOutUrl(HttpServletRequest request) {
    return getClient(request).getHubHomeUrl();
  }

  /**
   * Returns the public base URL of this application: the configured value, or one derived from
   * the request when none is configured.
   */
  public static String getExternalUrl(SmmWebConfig config, HttpServletRequest request) {
    String externalUrl = config.getExternalUrl();
    if (externalUrl.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      sb.append(request.getScheme()).append("://").append(request.getServerName());
      int port = request.getServerPort();
      boolean defaultPort = ("http".equals(request.getScheme()) && port == 80)
          || ("https".equals(request.getScheme()) && port == 443);
      if (!defaultPort) {
        sb.append(':').append(port);
      }
      sb.append(request.getContextPath());
      externalUrl = sb.toString();
    }
    return externalUrl.endsWith("/") ? externalUrl.substring(0, externalUrl.length() - 1)
        : externalUrl;
  }

  private InteropHubClient getClient(HttpServletRequest request) {
    String externalUrl = getExternalUrl(config, request);
    InteropHubClient current = client;
    if (current == null || !externalUrl.equals(clientExternalUrl)) {
      synchronized (this) {
        if (client == null || !externalUrl.equals(clientExternalUrl)) {
          client = InteropHubClientFactory.create(new HubClientConfig(externalUrl,
              config.getHubUrl(), config.getAppCode(), CONNECT_TIMEOUT_MS, READ_TIMEOUT_MS));
          clientExternalUrl = externalUrl;
          LOG.info("InteropHub client ready: externalUrl=" + externalUrl + " hubUrl="
              + config.getHubUrl() + " appCode=" + config.getAppCode());
        }
        current = client;
      }
    }
    return current;
  }
}
