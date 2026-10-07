package org.immregistries.smm.web.auth;

import java.io.IOException;
import org.immregistries.smm.web.SmmWebConfig;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Requires a signed-in user for every page except the sign-in and sign-out endpoints, the
 * endpoints that other systems call (the CDC WSDL test services and the message catcher), and
 * static files.
 */
public class AuthenticationFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    if (isPublicPath(getPath(httpRequest))) {
      chain.doFilter(request, response);
      return;
    }

    if (SmmSession.getUser(httpRequest) == null) {
      AuthenticationProvider provider =
          SmmSession.getAuthenticationProvider(httpRequest.getServletContext());
      String requestedUrl = getRequestedUrl(httpRequest);
      if ("GET".equals(httpRequest.getMethod())) {
        provider.startSignIn(httpRequest, httpResponse, requestedUrl);
      } else {
        // Don't replay a form submission after sign-in; return to the home page instead
        provider.startSignIn(httpRequest, httpResponse,
            getExternalUrl(httpRequest) + "/HomeServlet");
      }
      return;
    }

    // Protected pages must not be served from the browser cache after sign-out
    httpResponse.setHeader("Cache-Control", "no-store, no-cache, must-revalidate, max-age=0");
    httpResponse.setHeader("Pragma", "no-cache");
    httpResponse.setDateHeader("Expires", 0);
    chain.doFilter(request, response);
  }

  static boolean isPublicPath(String path) {
    return path.equals("/login") || path.equals("/logout") || path.equals("/CatchServlet")
        || path.equals("/wsdl") || path.startsWith("/wsdl/") || path.equals("/wsdl-demo")
        || path.startsWith("/wsdl-demo/") || path.endsWith(".css") || path.endsWith(".png")
        || path.endsWith(".ico");
  }

  private static String getPath(HttpServletRequest request) {
    String path = request.getRequestURI().substring(request.getContextPath().length());
    return path.isEmpty() ? "/" : path;
  }

  private static String getExternalUrl(HttpServletRequest request) {
    return InteropHubAuthenticationProvider
        .getExternalUrl(SmmWebConfig.get(request.getServletContext()), request);
  }

  private static String getRequestedUrl(HttpServletRequest request) {
    String query = request.getQueryString();
    return getExternalUrl(request) + getPath(request)
        + (query == null || query.isEmpty() ? "" : "?" + query);
  }
}
