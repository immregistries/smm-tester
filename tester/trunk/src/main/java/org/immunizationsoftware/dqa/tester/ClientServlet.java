/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.immunizationsoftware.dqa.tester;

import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.immunizationsoftware.dqa.SoftwareVersion;

/**
 * 
 * @author nathan
 */
public class ClientServlet extends HttpServlet
{

  protected static final String MENU_HEADER_HOME = "Home";
  protected static final String MENU_HEADER_CONNECT = "Connect to IIS";
  protected static final String MENU_HEADER_SETUP = "Manage Test Cases";
  protected static final String MENU_HEADER_EDIT = "Edit Test Case";
  protected static final String MENU_HEADER_SEND = "Send Message";
  protected static final String MENU_HEADER_TEST = "Test IIS";

  protected static void printHtmlHead(PrintWriter out, String title, HttpServletRequest request) {
    out.println("<html>");
    out.println("  <head>");
    out.println("    <title>" + title + "</title>");
    out.println("    <link rel=\"stylesheet\" type=\"text/css\" href=\"index.css\" />");
    out.println("    <script>");
    out.println("      function toggleLayer(whichLayer) ");
    out.println("      {");
    out.println("        var elem, vis;");
    out.println("        if (document.getElementById) ");
    out.println("          elem = document.getElementById(whichLayer);");
    out.println("        else if (document.all) ");
    out.println("          elem = document.all[whichLayer] ");
    out.println("        else if (document.layers) ");
    out.println("          elem = document.layers[whichLayer]");
    out.println("        vis = elem.style;");
    out.println("        if (vis.display == '' && elem.offsetWidth != undefined && elem.offsetHeight != undefined) ");
    out.println("          vis.display = (elem.offsetWidth != 0 && elem.offsetHeight != 0) ? 'block' : 'none';");
    out.println("        vis.display = (vis.display == '' || vis.display == 'block') ? 'none' : 'block';");
    out.println("      }");
    out.println("    </script>");
    out.println("  </head>");
    out.println("  <body>");
    out.println(makeMenu(request, title));
    String message = (String) request.getAttribute("message");
    if (message != null) {
      out.println("<p class=\"fail\">" + message + "</p>");
    }

  }

  public static String makeMenu(HttpServletRequest request) {
    return makeMenu(request, "&nbsp;");
  }

  private static final String[][] MENU_LOGGED_OUT = { { Authenticate.APP_DEFAULT_HOME, MENU_HEADER_HOME },
      { "LoginServlet", "Login" } };
  private static final String[][] MENU_LOGGED_IN = { { Authenticate.APP_DEFAULT_HOME, MENU_HEADER_HOME },
      { "ConnectServlet", MENU_HEADER_CONNECT }, { "SetupServlet", MENU_HEADER_SETUP }, { "CreateTestCaseServlet", MENU_HEADER_EDIT },
      { "SubmitServlet", MENU_HEADER_SEND }, { "CertifyServlet", MENU_HEADER_TEST }, { "LoginServlet?action=Logout", "Logout" } };

  public static String makeMenu(HttpServletRequest request, String title) {
    boolean loggedIn = false;
    HttpSession session = request.getSession();
    if (session != null) {
      loggedIn = session.getAttribute("username") != null;
    }
    String[][] menu = loggedIn ? MENU_LOGGED_IN : MENU_LOGGED_OUT;
    StringBuilder result = new StringBuilder();
    result.append("    <table class=\"menu\"><tr><td>");
    for (int i = 0; i < menu.length; i++) {
      if (i > 0) {
        // result.append(" &bull; ");
        result.append(" ");
      }
      String styleClass = "menuLink";
      if (menu[i][1].equals(title)) {
        styleClass = "menuLinkSelected";
      }
      result.append("<a class=\"");
      result.append(styleClass);
      result.append("\" href=\"");
      result.append(menu[i][0]);
      result.append("\">");
      result.append(menu[i][1]);
      result.append("</a>");

    }
    result.append("</td><td align=\"right\">");
    if (loggedIn) {
      Authenticate.User user = Authenticate.getUser((String) session.getAttribute("username"));
      if (user != null) {
        if (user.getName().equals("")) {
          result.append(user.getUsername());
        } else {
          result.append(user.getName());
        }
      }
    } else {
      result.append("&nbsp;");
    }
    result.append("</td></tr></table><br>");
    return result.toString();
  }

  public static void printFooter(PrintWriter out) {
    out.println("    <p>Open Immunization Software - Immunization Registry Tester &amp; Simple Message Mover - Version "
        + SoftwareVersion.VERSION
        + "<br>"
        + "For questions or support please contact <a href=\"http://openimmunizationsoftare.net\">Nathan Bunker</a>.</p>");

  }

  public static void printHtmlFoot(PrintWriter out) {
    printFooter(out);
    out.println("  </body>");
    out.println("</html>");
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
    out.println("       .pass {background:#77BED2; padding-left:5px;}");
    out.println("       .fail {background:#FF9999; padding-left:5px;}");
    out.println("       .nottested { padding-left:5px;}");
    out.println("       pre {background:#F7F3E8; padding:4px; margin: 2px; }");
    out.println("       a:link {  text-decoration:none; color: #2B3E42;}");
    out.println("       a:visited { text-decoration:none; color: #2B3E42;}");
    out.println("       a:hover {  background: #FFFFFF; text-decoration:none; border-style: solid; border-color: #2B3E42; border-width: 1px; color: #2B3E42;}");
    out.println("       a:active {text-decoration:none; color: #2B3E42; }");
    out.println("    </style>");
    out.println("  </head>");
    out.println("  <body>");
  }
}