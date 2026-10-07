package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.web.SmmNavigation;
import org.immregistries.smm.web.SmmNavigation.Area;
import org.immregistries.smm.web.SmmNavigation.Page;
import org.immregistries.smm.web.auth.SmmSession;
import org.immregistries.smm.web.auth.SmmUser;
import org.immregistries.smm.workspace.Workspace;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * The home page: who is signed in, their workspace and IIS connections, and the main areas of the
 * application.
 */
public class HomeServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

  private static final String[][] AREA_DESCRIPTIONS = {
      {"CONNECT", "Set up and test the connection to an IIS."},
      {"SEND", "Send a message or a query to an IIS and read the response."},
      {"TEST_CASES", "Load, edit, and run test case scripts."},
      {"TOOLS", "View, modify, and generate HL7 messages, and other utilities."}};

  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    SmmUser user = SmmSession.getUser(request);
    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, "Home", request);
      String contextPath = request.getContextPath();

      out.println("<div class=\"aira-stack\">");
      out.println("  <div class=\"aira-page-header\"><div>");
      out.println("    <h1 class=\"aira-page-title\">Welcome"
          + (user == null ? "" : ", " + escapeHtml(user.getName())) + "</h1>");
      out.println("    <p class=\"aira-page-intro\">Connect to an Immunization Information System "
          + "(IIS), send HL7 messages, and run tests.</p>");
      out.println("  </div></div>");

      out.println("  <div class=\"aira-grid\">");
      printWorkspacePanel(out, user);
      printConnectionsPanel(out, request, contextPath);
      out.println("  </div>");

      out.println("  <div class=\"aira-card-grid\">");
      for (String[] areaDescription : AREA_DESCRIPTIONS) {
        printAreaCard(out, Area.valueOf(areaDescription[0]), areaDescription[1], contextPath,
            user != null && user.hasSendData());
      }
      out.println("  </div>");
      out.println("</div>");

      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  private static void printWorkspacePanel(PrintWriter out, SmmUser user) {
    out.println("  <section class=\"aira-panel\">");
    out.println("    <div class=\"aira-panel__header\">"
        + "<h2 class=\"aira-panel__title\">Your Workspace</h2></div>");
    out.println("    <div class=\"aira-panel__body\">");
    if (user == null) {
      out.println("      <p>Not signed in.</p>");
    } else {
      Workspace workspace = user.getWorkspace();
      out.println("      <dl class=\"smm-details\">");
      printDetail(out, "Name", user.getName());
      printDetail(out, "Organization", user.getOrganization());
      printDetail(out, "Email", user.getEmail());
      if (workspace != null) {
        printDetail(out, "Workspace", workspace.getId());
      }
      out.println("      </dl>");
    }
    out.println("    </div>");
    out.println("  </section>");
  }

  private static void printConnectionsPanel(PrintWriter out, HttpServletRequest request,
      String contextPath) {
    List<Connector> connectors = ConnectServlet.getConnectors(request.getSession());
    out.println("  <section class=\"aira-panel\">");
    out.println("    <div class=\"aira-panel__header\">"
        + "<h2 class=\"aira-panel__title\">IIS Connections</h2></div>");
    out.println("    <div class=\"aira-panel__body\">");
    if (connectors.isEmpty()) {
      out.println("      <p>No IIS connection has been set up for this session.</p>");
    } else {
      out.println("      <div class=\"aira-table-wrap\">");
      out.println("        <table class=\"aira-table\">");
      out.println("          <caption class=\"aira-visually-hidden\">IIS connections</caption>");
      out.println("          <thead><tr><th scope=\"col\">Connection</th>"
          + "<th scope=\"col\">Type</th><th scope=\"col\">URL</th></tr></thead>");
      out.println("          <tbody>");
      for (Connector connector : connectors) {
        out.println("            <tr><th scope=\"row\" class=\"aira-table__cell--primary\">"
            + escapeHtml(connector.getLabelDisplay()) + "</th><td>"
            + escapeHtml(connector.getType()) + "</td><td class=\"aira-table__cell--code\">"
            + escapeHtml(connector.getUrl()) + "</td></tr>");
      }
      out.println("          </tbody>");
      out.println("        </table>");
      out.println("      </div>");
    }
    out.println("      <p><a class=\"aira-button aira-button--secondary aira-button--small\" href=\""
        + contextPath + Area.CONNECT.getHref() + "\">"
        + (connectors.isEmpty() ? "Connect to an IIS" : "Manage connections") + "</a></p>");
    out.println("    </div>");
    out.println("  </section>");
  }

  private static void printAreaCard(PrintWriter out, Area area, String description,
      String contextPath, boolean hasMoverConnection) {
    out.println("    <section class=\"aira-section-card\">");
    out.println("      <div class=\"aira-section-card__header\"><h2 class=\"aira-section-card__title\">"
        + "<a href=\"" + contextPath + area.getHref() + "\">" + area.getLabel() + "</a></h2></div>");
    out.println("      <div class=\"aira-section-card__body\">");
    out.println("        <p>" + description + "</p>");
    out.println("        <ul class=\"smm-link-list\">");
    for (Page page : SmmNavigation.getRailPages(area, hasMoverConnection)) {
      String href = contextPath + page.getPath() + (page.getPath().equals("/wsdl-demo") ? "/" : "");
      out.println("          <li><a href=\"" + href + "\">" + page.getLabel() + "</a></li>");
    }
    out.println("        </ul>");
    out.println("      </div>");
    out.println("    </section>");
  }

  private static void printDetail(PrintWriter out, String label, String value) {
    out.println("        <dt>" + label + "</dt><dd>" + escapeHtml(value) + "</dd>");
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  @Override
  public String getServletInfo() {
    return "Simple Message Mover home page";
  }
}
