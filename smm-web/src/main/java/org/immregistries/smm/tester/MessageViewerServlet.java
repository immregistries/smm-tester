package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import org.immregistries.smm.tester.manager.query.QueryRequest;
import org.immregistries.smm.tester.manager.query.QueryResponse;
import org.immregistries.smm.tester.manager.query.Vaccination;
import org.immregistries.smm.tester.manager.response.ImmunizationMessage;
import org.immregistries.smm.tester.manager.response.ResponseReader;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author nathan
 */
public class MessageViewerServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

  public static final String PARAM_ACTION = "action";

  public static final String PARAM_MESSAGE = "message";

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * 
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    String action = request.getParameter(PARAM_ACTION);
    String problem = null;
    if (username == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
    }
    QueryRequest queryRequest = new QueryRequest();
    session.setAttribute("queryRequest", queryRequest);
    if (action != null) {
    }
    doGet(request, response, session, problem);
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
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
    } else {
      doGet(request, response, session, null);
    }
  }

  private void doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session,
      String problem) throws IOException {
    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_HOME, request);
      printPageHeader(out, "Message Viewer",
          "Paste an HL7 query response to see the vaccinations it reports.");

      if (problem != null) {
        out.println("<div class=\"aira-alert aira-alert--warning\" role=\"status\"><p>"
            + escapeHtml(problem) + "</p></div>");
      }

      String message = request.getParameter(PARAM_MESSAGE);
      if (message == null) {
        message = (String) session.getAttribute("message");
        if (message == null) {
          message = "";
        }
      }

      out.println("<div class=\"aira-stack\">");
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__body\">");
      out.println("    <form class=\"aira-form\" action=\"MessageViewerServlet\" method=\"POST\">");
      out.println("      <div class=\"aira-field\">");
      out.println("        <label for=\"" + PARAM_MESSAGE + "\">Message</label>");
      out.println("        <textarea class=\"aira-textarea smm-code\" id=\"" + PARAM_MESSAGE
          + "\" name=\"" + PARAM_MESSAGE + "\" rows=\"12\" wrap=\"off\">" + escapeHtml(message)
          + "</textarea>");
      out.println("      </div>");
      out.println("      <div class=\"aira-form-actions\">");
      out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
          + " name=\"" + PARAM_ACTION + "\" value=\"View\">View</button>");
      out.println("      </div>");
      out.println("    </form>");
      out.println("  </div>");
      out.println("</section>");

      if (!message.equals("")) {
        ImmunizationMessage immunizationMessage = ResponseReader.readMessage(message);
        if (immunizationMessage != null) {
          if (immunizationMessage instanceof QueryResponse) {
            QueryResponse queryResponse = (QueryResponse) immunizationMessage;
            out.println("<section class=\"aira-table-panel\">");
            out.println("  <div class=\"aira-table-panel__header\"><div>"
                + "<h2 class=\"aira-table-panel__title\">Query Response</h2>"
                + "<p class=\"aira-table-panel__description\">"
                + escapeHtml(queryResponse.getQueryResponseType().getLabel()) + "</p></div></div>");
            if (queryResponse.getVaccinationList() != null) {
              out.println("  <div class=\"aira-table-wrap\">");
              out.println("    <table class=\"aira-table\">");
              out.println("      <caption class=\"aira-visually-hidden\">Vaccinations</caption>");
              out.println("      <thead><tr><th scope=\"col\">Date</th>"
                  + "<th scope=\"col\">Vaccination (CVX)</th><th scope=\"col\">Action</th>"
                  + "<th scope=\"col\">Completion</th><th scope=\"col\">Source</th>"
                  + "<th scope=\"col\">Refusal</th><th scope=\"col\">Lot Number</th>"
                  + "<th scope=\"col\">Manufacturer (MVX)</th></tr></thead>");
              out.println("      <tbody>");
              if (queryResponse.getVaccinationList().isEmpty()) {
                out.println("        <tr><td colspan=\"8\" class=\"aira-table__empty\">No"
                    + " vaccinations in this response.</td></tr>");
              }
              SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
              for (Vaccination vaccination : queryResponse.getVaccinationList()) {
                out.println("        <tr>");
                out.println("          <td class=\"aira-table__cell--date\">"
                    + (vaccination.getAdministrationDate() == null ? ""
                        : sdf.format(vaccination.getAdministrationDate()))
                    + "</td>");
                out.println("          <td>" + escapeHtml(vaccination.getVaccineLabel()) + " ("
                    + escapeHtml(vaccination.getVaccineCvx()) + ")</td>");
                out.println("          <td>" + escapeHtml(vaccination.getActionCode()) + "</td>");
                out.println(
                    "          <td>" + escapeHtml(vaccination.getCompletionStatus()) + "</td>");
                out.println("          <td>" + escapeHtml(vaccination.getInformationSourceLabel())
                    + " (" + escapeHtml(vaccination.getInformationSource()) + ")</td>");
                out.println("          <td>" + escapeHtml(vaccination.getRefusalReason()) + "</td>");
                out.println("          <td>" + escapeHtml(vaccination.getLotNumber()) + "</td>");
                out.println("          <td>" + escapeHtml(vaccination.getManufacturerLabel()) + " ("
                    + escapeHtml(vaccination.getManufacturerMvx()) + ")</td>");
                out.println("        </tr>");
              }
              out.println("      </tbody>");
              out.println("    </table>");
              out.println("  </div>");
            }
            out.println("</section>");
          }
        }
      }
      out.println("</div>");
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }



}

