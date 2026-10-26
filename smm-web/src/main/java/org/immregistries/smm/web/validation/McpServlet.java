package org.immregistries.smm.web.validation;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The MCP server endpoint (<code>/mcp</code>), giving AI assistants such as Claude and ChatGPT an
 * HL7 validation tool. It uses the Streamable HTTP transport in its stateless form: every request
 * is a POST answered with a JSON response. No sign-in is needed; {@link PublicApiFilter} limits
 * use.
 */
public class McpServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    ValidationGateway gateway = ValidationGateway.get(getServletContext());
    McpHandler handler = new McpHandler(gateway);
    String client = gateway.getClientAddress(request);
    JsonNode body;
    try {
      body = ValidationJson.MAPPER
          .readTree(ApiResponses.readBody(request, PublicApiFilter.maxBodyBytes(gateway)));
    } catch (ValidationApiException e) {
      ApiResponses.writeError(response, e);
      return;
    } catch (JsonProcessingException e) {
      ApiResponses.writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
          McpHandler.error(null, McpHandler.PARSE_ERROR, "Parse error"));
      return;
    }
    if (body == null) {
      ApiResponses.writeJson(response, HttpServletResponse.SC_BAD_REQUEST,
          McpHandler.error(null, McpHandler.INVALID_REQUEST, "Empty request"));
      return;
    }

    if (body.isArray()) {
      // JSON-RPC batches were allowed by MCP protocol version 2025-03-26
      ArrayNode replies = ValidationJson.MAPPER.createArrayNode();
      for (JsonNode message : body) {
        ObjectNode reply = handler.handle(message, client);
        if (reply != null) {
          replies.add(reply);
        }
      }
      writeReply(response, replies.isEmpty() ? null : replies);
    } else {
      writeReply(response, handler.handle(body, client));
    }
  }

  private static void writeReply(HttpServletResponse response, JsonNode reply)
      throws IOException {
    if (reply == null) {
      response.setStatus(HttpServletResponse.SC_ACCEPTED);
    } else {
      ApiResponses.writeJson(response, HttpServletResponse.SC_OK, reply);
    }
  }

  /**
   * This server does not open server-sent event streams.
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setHeader("Allow", "POST");
    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
  }

  /**
   * There are no sessions to end.
   */
  @Override
  protected void doDelete(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    response.setHeader("Allow", "POST");
    response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
  }
}
