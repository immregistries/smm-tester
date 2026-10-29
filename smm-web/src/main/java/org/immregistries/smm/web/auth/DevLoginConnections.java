package org.immregistries.smm.web.auth;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.immregistries.smm.tester.ConnectServlet;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpSession;

/**
 * DEV LOGIN ONLY. Gives the auto-signed-in demo user a ready-made IIS connection so the Connect, Send
 * and Query pages work right after sign-in. Only called by {@link SignInServlet} when the dev login
 * flag is on. Nothing happens unless the context parameter <code>smm.dev.login.iis.url</code> is set.
 * Optional parameters: <code>.type</code> (default SOAP), <code>.label</code>, <code>.user</code>,
 * <code>.password</code>, <code>.facility</code>.
 */
final class DevLoginConnections {

  static final String PREFIX = "smm.dev.login.iis.";
  private static final Logger LOG = Logger.getLogger(DevLoginConnections.class.getName());

  private DevLoginConnections() {}

  static void seed(ServletContext context, HttpSession session) {
    String url = param(context, "url", "");
    if (url.isEmpty()) {
      return;
    }
    String label = param(context, "label", "Local IIS");
    try {
      List<Connector> connectors = ConnectServlet.getConnectors(session);
      for (Connector existing : connectors) {
        if (label.equals(existing.getLabel())) {
          return;
        }
      }
      Connector connector =
          ConnectorFactory.getConnector(param(context, "type", ConnectorFactory.TYPE_SOAP), label, url);
      connector.setUserid(param(context, "user", ""));
      connector.setPassword(param(context, "password", ""));
      connector.setFacilityid(param(context, "facility", ""));
      connectors.add(connector);
      if (session.getAttribute("id") == null) {
        session.setAttribute("id", connectors.size());
      }
      LOG.warning("Dev login: preconfigured IIS connection \"" + label + "\" -> " + url);
    } catch (Exception e) {
      LOG.log(Level.WARNING, "Dev login: unable to preconfigure IIS connection", e);
    }
  }

  private static String param(ServletContext context, String name, String fallback) {
    String value = context.getInitParameter(PREFIX + name);
    return value == null || value.trim().isEmpty() ? fallback : value.trim();
  }
}
