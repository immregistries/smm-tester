package org.immregistries.smm.tester;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.immregistries.smm.SoftwareVersion;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.web.SmmPage;
import org.immregistries.smm.web.auth.SmmUser;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * @author nathan
 */
public class ClientServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
  
  protected static final String MENU_HEADER_HOME = "Home";
  protected static final String MENU_HEADER_CONNECT = "Connect to IIS";
  protected static final String MENU_HEADER_SETUP = "Manage Test Cases";
  protected static final String MENU_HEADER_EDIT = "Edit Test Case";
  protected static final String MENU_HEADER_SEND = "Send Message";
  
  public static final String APP_DEFAULT_HOME = "HomeServlet";

  /**
   * Writes the AIRA application shell and opens the page content. Any "message" request attribute
   * is shown as an alert at the top of the content.
   */
  protected static void printHtmlHead(PrintWriter out, String title, HttpServletRequest request) {
    SmmPage.writeStart(out, request, title);
    String message = (String) request.getAttribute("message");
    if (message != null) {
      out.println("<div class=\"aira-alert aira-alert--warning\" role=\"status\"><p>"
          + SmmPage.escapeHtml(message) + "</p></div>");
    }
  }

  /**
   * Writes the page title and an optional introduction. The title and introduction are HTML.
   */
  protected static void printPageHeader(PrintWriter out, String title, String intro) {
    out.println("<div class=\"aira-page-header\"><div>");
    out.println("  <h1 class=\"aira-page-title\">" + title + "</h1>");
    if (intro != null) {
      out.println("  <p class=\"aira-page-intro\">" + intro + "</p>");
    }
    out.println("</div></div>");
  }

  /**
   * Writes a labeled text input. The value is escaped.
   */
  protected static void printTextField(PrintWriter out, String name, String label, String value) {
    printTextField(out, name, label, value, null);
  }

  /**
   * Writes a labeled text input with an optional placeholder. The value is escaped.
   */
  protected static void printTextField(PrintWriter out, String name, String label, String value,
      String placeholder) {
    out.println("        <div class=\"aira-field\">");
    out.println("          <label for=\"" + name + "\">" + label + "</label>");
    out.println("          <input class=\"aira-input\" type=\"text\" id=\"" + name + "\" name=\""
        + name + "\" value=\"" + SmmPage.escapeHtml(value) + "\""
        + (placeholder == null ? "" : " placeholder=\"" + SmmPage.escapeHtml(placeholder) + "\"")
        + ">");
    out.println("        </div>");
  }

  /**
   * Writes a notice that the page needs an IIS connection, with a link to set one up.
   */
  protected static void printNoConnection(PrintWriter out) {
    out.println("<div class=\"aira-empty-state\">");
    out.println("  <p class=\"aira-empty-state__title\">No IIS connection</p>");
    out.println("  <p>This page needs a connection to an IIS. Set one up first.</p>");
    out.println("  <p><a class=\"aira-button aira-button--primary\" href=\"ConnectServlet\">"
        + "Connect to an IIS</a></p>");
    out.println("</div>");
  }

  /**
   * Writes a panel with a title and plain text output, such as a log, in a pre block. The text is
   * escaped.
   */
  protected static void printLog(PrintWriter out, String title, String text) {
    out.println("<section class=\"aira-panel\">");
    out.println("  <div class=\"aira-panel__header\"><h2 class=\"aira-panel__title\">" + title
        + "</h2></div>");
    out.println("  <div class=\"aira-panel__body\">");
    out.println("    <pre class=\"smm-hl7\">" + SmmPage.escapeHtml(text) + "</pre>");
    out.println("  </div>");
    out.println("</section>");
  }

  /**
   * Returns a badge for a test result status such as PASS or FAIL, or nothing when there is no
   * status yet.
   */
  protected static String statusBadge(String status) {
    if (status == null || status.equals("")) {
      return "";
    } else if (status.equals("PASS")) {
      return "<span class=\"aira-badge aira-badge--success\">Pass</span>";
    } else if (status.equals("FAIL")) {
      return "<span class=\"aira-badge aira-badge--danger\">Fail</span>";
    }
    return "<span class=\"aira-badge aira-badge--outline\">" + SmmPage.escapeHtml(status)
        + "</span>";
  }

  /**
   * Writes an error alert with the exception's message and stack trace.
   */
  protected static void printException(PrintWriter out, Throwable throwable) {
    out.println("<div class=\"aira-alert aira-alert--error\" role=\"alert\">");
    out.println("  <p class=\"aira-alert__title\">Exception occurred: "
        + SmmPage.escapeHtml(throwable.getMessage()) + "</p>");
    out.println("  <pre class=\"smm-hl7\">" + SmmPage.escapeHtml(stackTrace(throwable)) + "</pre>");
    out.println("</div>");
  }

  /**
   * Returns the stack trace of a throwable as text, for showing in a pre block after escaping.
   */
  protected static String stackTrace(Throwable throwable) {
    StringWriter stringWriter = new StringWriter();
    throwable.printStackTrace(new PrintWriter(stringWriter));
    return stringWriter.toString();
  }

  public static void printFooter(PrintWriter out) {
    out.println(
        "    <p>American Immunization Registry Association - Simple Message Mover - Version "
            + SoftwareVersion.VERSION + "</p>");
  }

  /**
   * Closes the page content and writes the rest of the AIRA application shell.
   */
  public static void printHtmlFoot(PrintWriter out) {
    SmmPage.writeEnd(out);
  }

  public static void printHtmlFootForFile(PrintWriter out) {
    printFooter(out);
    out.println("  </body>");
    out.println("</html>");
  }

  public static void printHtmlHeadForFile(PrintWriter out, String title) {
    out.println("<html>");
    out.println("  <head>");
    out.println("    <title>" + title + "</title>");
    out.println("    <style>");
    out.println("       body {font-family: Tahoma, Geneva, sans-serif; background:#D5E1DD}");
    out.println("       h2 {border-top-style: solid; border-top-width: 2px;}");
    out.println("       .pass {background:#77BED2; padding-left:5px;}");
    out.println("       .fail {background:#FF9999; padding-left:5px;}");
    out.println("       .nottested { padding-left:5px;}");
    out.println("       pre {background:#F7F3E8; padding:4px; margin: 2px; }");
    out.println("       a:link {  text-decoration:none; color: #2B3E42;}");
    out.println("       a:visited { text-decoration:none; color: #2B3E42;}");
    out.println(
        "       a:hover {  background: #FFFFFF; text-decoration:none; border-style: solid; border-color: #2B3E42; border-width: 1px; color: #2B3E42;}");
    out.println("       a:active {text-decoration:none; color: #2B3E42; }");
    out.println(
        "       .boxLinks { margin-bottom: 2px; font-size: 12pt; font-weight: normal; align-content: flex-end; height: 50px; border-width: 1px; border-style: solid; background-color:#AAAAAA; color: #2B3E42; padding-left: 3px; padding-right: 3px; border-color: #222222;");
    out.println("    </style>");
    out.println("  </head>");
    out.println("  <body>");
  }

  public Connector printServiceSelector(HttpServletRequest request, HttpSession session,
      PrintWriter out) {
    Connector connectorSelected = null;
    int id = 0;
    if (request.getParameter("id") != null && !request.getParameter("id").equals("")) {
      id = Integer.parseInt(request.getParameter("id"));
    }
    if (session.getAttribute("id") != null) {
      id = (Integer) session.getAttribute("id");
    }
    out.println("      <div class=\"aira-field\">");
    List<Connector> connectors = ConnectServlet.getConnectors(session);
    if (connectors.size() == 1) {
      out.println("        <span class=\"aira-label\">Connection</span>");
      out.println("        <span>" + SmmPage.escapeHtml(connectors.get(0).getLabelDisplay())
          + "</span>");
      out.println("        <input type=\"hidden\" name=\"id\" value=\"1\"/>");
      connectorSelected = connectors.get(0);
    } else {
      out.println("        <label for=\"id\">Connection</label>");
      out.println("        <select class=\"aira-select smm-select-auto\" id=\"id\" name=\"id\">");
      out.println("          <option value=\"\">select</option>");
      int i = 0;
      for (Connector connector : connectors) {
        i++;
        if (id == i) {
          connectorSelected = connector;
        }
        out.println("          <option value=\"" + i + "\"" + (id == i ? " selected" : "") + ">"
            + SmmPage.escapeHtml(connector.getLabelDisplay()) + "</option>");
      }
      out.println("        </select>");
    }
    out.println("      </div>");
    return connectorSelected;
  }

}
