package org.immregistries.smm.web.validation;

import java.io.IOException;
import java.io.Reader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Reading request bodies and writing JSON responses for the public endpoints.
 */
final class ApiResponses {

  private ApiResponses() {}

  static void writeJson(HttpServletResponse response, int status, JsonNode body)
      throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.setHeader("Cache-Control", "no-store");
    ValidationJson.MAPPER.writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), body);
  }

  static void writeError(HttpServletResponse response, ValidationApiException e)
      throws IOException {
    if (e.getRetryAfterSeconds() > 0) {
      response.setHeader("Retry-After", String.valueOf(e.getRetryAfterSeconds()));
    }
    ObjectNode body = ValidationJson.MAPPER.createObjectNode();
    body.put("error", e.getMessage());
    writeJson(response, e.getStatus(), body);
  }

  /**
   * Reads the request body as text, refusing bodies longer than the limit even when the client
   * did not send a Content-Length.
   */
  static String readBody(HttpServletRequest request, int maxChars)
      throws IOException, ValidationApiException {
    StringBuilder body = new StringBuilder();
    char[] buffer = new char[8192];
    try (Reader reader = request.getReader()) {
      int read;
      while ((read = reader.read(buffer)) != -1) {
        body.append(buffer, 0, read);
        if (body.length() > maxChars) {
          throw ValidationApiException.tooLarge("The request is too large.");
        }
      }
    }
    return body.toString();
  }
}
