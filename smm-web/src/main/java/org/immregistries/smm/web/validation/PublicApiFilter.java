package org.immregistries.smm.web.validation;

import java.io.IOException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Guards the public, unauthenticated endpoints (the validation API and the MCP server): limits
 * how many requests each client may make, refuses oversized bodies before they are read, and
 * allows cross-origin calls so browser-based tools can use the API.
 */
public class PublicApiFilter implements Filter {

  /** Room for JSON wrapping and escaping around the message text itself. */
  private static final int BODY_OVERHEAD_BYTES = 64 * 1024;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    // No cookies or credentials are used, so any origin may call these endpoints
    httpResponse.setHeader("Access-Control-Allow-Origin", "*");
    httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
    httpResponse.setHeader("Access-Control-Allow-Headers",
        "Content-Type, Accept, Mcp-Session-Id, Mcp-Protocol-Version");
    httpResponse.setHeader("Access-Control-Expose-Headers", "Retry-After");
    if ("OPTIONS".equals(httpRequest.getMethod())) {
      httpResponse.setStatus(HttpServletResponse.SC_NO_CONTENT);
      return;
    }

    ValidationGateway gateway = ValidationGateway.get(request.getServletContext());
    try {
      gateway.checkRequestRate(gateway.getClientAddress(httpRequest));
      if (httpRequest.getContentLengthLong() > maxBodyBytes(gateway)) {
        throw ValidationApiException
            .tooLarge("The request is too large; send at most " + gateway.getMaxBytes()
                + " characters of HL7 at a time.");
      }
    } catch (ValidationApiException e) {
      ApiResponses.writeError(httpResponse, e);
      return;
    }
    chain.doFilter(request, response);
  }

  static int maxBodyBytes(ValidationGateway gateway) {
    return gateway.getMaxBytes() * 2 + BODY_OVERHEAD_BYTES;
  }
}
