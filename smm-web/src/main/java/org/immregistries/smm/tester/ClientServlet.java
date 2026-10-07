package org.immregistries.smm.tester;

import java.io.PrintWriter;
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
      out.println("<div class=\"aira-alert aira-alert--warning\" role=\"status\"><p>" + message
          + "</p></div>");
    }
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
    out.println("        <tr>");
    out.println("          <td>Service</td>");
    out.println("          <td>");
    List<Connector> connectors = ConnectServlet.getConnectors(session);
    if (connectors.size() == 1) {
      out.println("            " + connectors.get(0).getLabelDisplay());
      out.println("            <input type=\"hidden\" name=\"id\" value=\"1\"/>");
      connectorSelected = connectors.get(0);
    } else {
      out.println("            <select name=\"id\">");
      out.println("              <option value=\"\">select</option>");
      int i = 0;
      for (Connector connector : connectors) {
        i++;
        if (id == i) {
          out.println("              <option value=\"" + i + "\" selected=\"true\">"
              + connector.getLabelDisplay() + "</option>");
          connectorSelected = connector;
        } else {
          out.println("              <option value=\"" + i + "\">" + connector.getLabelDisplay()
              + "</option>");
        }
      }
      out.println("            </select>");
    }
    out.println("          </td>");
    out.println("        </tr>");
    return connectorSelected;
  }

}
