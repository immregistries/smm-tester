package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.List;
import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.InstallCert;
import org.immregistries.smm.web.auth.SmmUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author nathan
 */
public class InstallCertServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * 
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
    } else {
      SmmUser user = (SmmUser) session.getAttribute("user");
      if (user.getSendData() == null) {
        response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
        return;
      }
      SendData sendData = user.getSendData();
      InstallCert installCert = (InstallCert) session.getAttribute("installCert");
      if (installCert == null) {
        installCert = new InstallCert();
        File certFile = new File(sendData.getRootDir(), "smm.jks");
        installCert.setFile(certFile);
        URL url = new URL(sendData.getConnector().getUrl());
        installCert.setHost(url.getHost());
        if (url.getPort() > 0) {
          installCert.setPort(url.getPort());
        }
        session.setAttribute("installCert", installCert);
      }
      int id = 0;
      List<Connector> connectors = ConnectServlet.getConnectors(session);
      if (connectors.size() == 1) {
        id = 1;
      } else {
        if (request.getParameter("id") != null && !request.getParameter("id").equals("")) {
          id = Integer.parseInt(request.getParameter("id"));
        } else if (session.getAttribute("id") != null) {
          id = (Integer) session.getAttribute("id");
        }
      }

      String action = request.getParameter("action");

      PrintWriter out = response.getWriter();
      try {
        printHtmlHead(out, MENU_HEADER_SETUP, request);
        String checkLog = null;
        if (action != null && action.equals("Check")) {
          StringWriter log = new StringWriter();
          try {
            installCert.findCert(new PrintWriter(log));
          } catch (Exception e) {
            log.append(stackTrace(e));
          }
          checkLog = log.toString();
        }
        String installLog = null;
        if (action != null && action.startsWith("Install ")) {
          StringWriter log = new StringWriter();
          try {
            installCert.setChainPos(Integer.parseInt(action.substring("Install ".length())) - 1);
            installCert.saveCert(new PrintWriter(log));
          } catch (Exception e) {
            log.append(stackTrace(e));
          }
          installLog = log.toString();
        }
        printPageHeader(out, "Install Certificate",
            "Check the certificate chain an IIS presents and install a certificate from it.");
        out.println("<div class=\"aira-stack\">");
        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__body\">");
        out.println("    <form class=\"aira-form\" action=\"InstallCertServlet\" method=\"POST\">");
        out.println("      <div class=\"aira-field\">");
        if (connectors.size() == 1) {
          out.println("        <span class=\"aira-label\">Connection</span>");
          out.println("        <span>" + escapeHtml(connectors.get(0).getLabelDisplay())
              + "</span>");
          out.println("        <input type=\"hidden\" name=\"id\" value=\"1\"/>");
        } else {
          out.println("        <label for=\"id\">Connection</label>");
          out.println("        <select class=\"aira-select smm-select-auto\" id=\"id\" name=\"id\">");
          out.println("          <option value=\"\">select</option>");
          int i = 0;
          for (Connector connector : connectors) {
            i++;
            out.println("          <option value=\"" + i + "\"" + (id == i ? " selected" : "")
                + ">" + escapeHtml(connector.getLabelDisplay()) + "</option>");
          }
          out.println("        </select>");
        }
        out.println("      </div>");
        out.println("      <div class=\"aira-form-actions\">");
        out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
            + " name=\"action\" value=\"Check\">Check</button>");
        if (installCert.getChain() != null) {
          int pos = 0;
          for (@SuppressWarnings("unused") X509Certificate cert : installCert.getChain()) {
            pos++;
            out.println("        <button class=\"aira-button aira-button--secondary\""
                + " type=\"submit\" name=\"action\" value=\"Install " + pos + "\">Install " + pos
                + "</button>");
          }
        }
        out.println("      </div>");
        out.println("    </form>");
        out.println("  </div>");
        out.println("</section>");
        if (checkLog != null) {
          printLog(out, "Certificate Check", checkLog);
        }
        if (installLog != null) {
          printLog(out, "Certificate Install", installLog);
        }
        out.println("</div>");
        printHtmlFoot(out);

      } finally {
        out.close();
      }
    }
  }

  // <editor-fold defaultstate="collapsed"
  // desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

  /**
   * Handles the HTTP <code>GET</code> method.
   * 
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Handles the HTTP <code>POST</code> method.
   * 
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   * 
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>
}
