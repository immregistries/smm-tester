package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.query.QueryType;
import org.immregistries.smm.tester.query.QueryRunner;
import org.immregistries.smm.web.auth.SmmUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class BulkQueryServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    this.doGet(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
      return;
    }
    SmmUser user = (SmmUser) session.getAttribute("user");
    if (user == null || user.getSendData() == null) {
      // Bulk queries read from and write to a folder-based mover connection
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
      return;
    }

    QueryType queryType = QueryType.NONE;
    if (request.getParameter("queryType") != null) {
      queryType = QueryType.getValue(request.getParameter("queryType"));
    } else if (user.getSendData() != null && user.getSendData().getTestParticipant() != null) {
      queryType = QueryType.getValue(user.getSendData().getTestParticipant().getQueryType());
    }

    Set<String> filenamesSelectedSet = new HashSet<String>();
    String[] filenamesSelected = request.getParameterValues(QueryRunner.FILE_NAME);
    if (filenamesSelected != null) {
      for (String filenameSelected : filenamesSelected) {
        filenamesSelectedSet.add(filenameSelected);
      }
    }
    String userName = request.getParameter(QueryRunner.USER_NAME);
    String password = request.getParameter(QueryRunner.PASSWORD);
    String transforms = request.getParameter(QueryRunner.TRANSFORMS);
    if (request.getParameter("save") == null) {
      transforms = null;
    }

    QueryRunner queryRunner = (QueryRunner) session.getAttribute("queryRunner");

    String action = request.getParameter("action");
    if (action != null) {
      if (action.equals("Start") && isReadyToStart(queryRunner)) {
        List<Connector> connectors = ConnectServlet.getConnectors(session);
        int id = Integer.parseInt(request.getParameter("id"));
        queryRunner = new QueryRunner(connectors.get(id - 1), user.getSendData(), queryType,
            filenamesSelectedSet, userName, password, transforms);
        queryRunner.start();
        session.setAttribute("queryRunner", queryRunner);
      } else if (action.equals("Stop") && queryRunner != null) {
        queryRunner.stopRunning();
      }
    }

    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_HOME, request);
      printPageHeader(out, "Bulk Query",
          "Send the queries in the mover connection's query files to an IIS.");
      out.println("<div class=\"aira-stack\">");
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__body\">");
      out.println("<form class=\"aira-form\" id=\"queryForm\" action=\"BulkQueryServlet\""
          + " method=\"POST\">");
      printServiceSelector(request, session, out);
      out.println("      <fieldset class=\"aira-fieldset\">");
      out.println("        <legend class=\"aira-legend\">Query Type</legend>");
      out.println("        <div class=\"aira-cluster\">");
      for (QueryType qt : QueryType.values()) {
        out.println("          <label class=\"aira-radio\"><input type=\"radio\" name=\"queryType\""
            + " value=\"" + qt + "\"" + (qt.equals(queryType) ? " checked" : "") + "> " + qt
            + "</label>");
      }
      out.println("        </div>");
      out.println("      </fieldset>");
      out.println("      <fieldset class=\"aira-fieldset\">");
      out.println("        <legend class=\"aira-legend\">Files</legend>");
      {
        SendData sendData = user.getSendData();
        sendData.setupQueryDir();
        String[] filenames =
            sendData.getQueryDir().exists() ? QueryRunner.getListOfFiles(sendData) : new String[0];
        if (filenames.length == 0) {
          out.println("        <p class=\"aira-muted\">No query files found.</p>");
        }
        for (String filename : filenames) {
          out.println("        <label class=\"aira-check\"><input type=\"checkbox\" name=\""
              + QueryRunner.FILE_NAME + "\" value=\"" + escapeHtml(filename) + "\""
              + (filenamesSelectedSet.size() == 0 || filenamesSelectedSet.contains(filename)
                  ? " checked" : "")
              + "/> " + escapeHtml(filename) + "</label>");
        }
      }
      out.println("      </fieldset>");
      out.println("      <label class=\"aira-check\"><input type=\"checkbox\" name=\"save\""
          + " value=\"T\"/> Save</label>");
      out.println("      <div class=\"aira-field\">");
      out.println("        <label for=\"" + QueryRunner.TRANSFORMS + "\">Transforms</label>");
      out.println("        <textarea class=\"aira-textarea smm-code\" id=\"" + QueryRunner.TRANSFORMS
          + "\" name=\"" + QueryRunner.TRANSFORMS + "\" rows=\"5\">"
          + escapeHtml(transforms) + "</textarea>");
      out.println("      </div>");
      out.println("      <div class=\"aira-field-row\">");
      printTextField(out, QueryRunner.USER_NAME, "TCH User Name", userName);
      out.println("        <div class=\"aira-field\">");
      out.println("          <label for=\"" + QueryRunner.PASSWORD + "\">TCH Password</label>");
      out.println("          <input class=\"aira-input\" type=\"password\" id=\""
          + QueryRunner.PASSWORD + "\" name=\"" + QueryRunner.PASSWORD + "\" value=\""
          + escapeHtml(password) + "\" autocomplete=\"off\"/>");
      out.println("        </div>");
      out.println("      </div>");

      out.println("      <div class=\"aira-form-actions\">");
      if (isReadyToStart(queryRunner)) {
        out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
            + " name=\"action\" value=\"Start\">Start</button>");
      } else {
        out.println("        <span class=\"aira-badge aira-badge--info\">"
            + escapeHtml(queryRunner.getStatus()) + "</span>");
        out.println("        <button class=\"aira-button aira-button--danger\" type=\"submit\""
            + " name=\"action\" value=\"Stop\">Stop</button>");
      }
      out.println("        <button class=\"aira-button aira-button--secondary\" type=\"submit\""
          + " name=\"action\" value=\"Refresh\">Refresh</button>");
      out.println("      </div>");
      out.println("</form>");
      out.println("  </div>");
      out.println("</section>");
      if (queryRunner != null) {
        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">Status</h2></div>");
        out.println("  <div class=\"aira-panel__body\">");
        out.print("    <pre class=\"smm-hl7\">");
        for (String statusMessage : queryRunner.getStatusMessageList()) {
          out.println(escapeHtml(statusMessage));
        }
        out.println("</pre>");
        out.println("  </div>");
        out.println("</section>");
      }
      out.println("</div>");
      printHtmlFoot(out);
    } finally {
      out.close();
    }

  }

  private boolean isReadyToStart(QueryRunner queryRunner) {
    return queryRunner == null || queryRunner.getStatus().equals(QueryRunner.STATUS_COMPLETED)
        || queryRunner.getStatus().equals(QueryRunner.STATUS_PROBLEM)
        || queryRunner.getStatus().equals(QueryRunner.STATUS_STOPPED);
  }
}
