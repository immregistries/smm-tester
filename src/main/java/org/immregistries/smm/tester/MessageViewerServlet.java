package org.immregistries.smm.tester;

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
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
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
      response.sendRedirect(Authenticate.APP_DEFAULT_HOME);
    } else {
      doGet(request, response, session, null);
    }
  }

  private void doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session,
      String problem) throws IOException {
    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_HOME, request);

      if (problem != null) {
        out.println("<p>" + problem + "</p>");
      }

      String message = request.getParameter(PARAM_MESSAGE);
      if (message == null) {
        message = (String) session.getAttribute("message");
        if (message == null) {
          message = "";
        }
      }

      out.println("<h2>Message Viewer</h2>");
      out.println("<form action=\"MessageViewerServlet\" method=\"POST\">");
      out.println("<textarea name=\"" + PARAM_MESSAGE + "\" cols=\"120\" rows=\"15\">" + message
          + "</textarea><br/>");
      out.println("<input type=\"submit\" name=\"" + PARAM_ACTION + "\" value=\"View\"/>");
      out.println("</form>");

      if (!message.equals("")) {
        ImmunizationMessage immunizationMessage = ResponseReader.readMessage(message);
        if (immunizationMessage != null) {
          if (immunizationMessage instanceof QueryResponse) {
            QueryResponse queryResponse = (QueryResponse) immunizationMessage;
            out.println("Query Response: " + queryResponse.getQueryResponseType().getLabel());
            if (queryResponse.getVaccinationList() != null) {
              out.println("<table class=\"boxed\">");
              out.println("  <tr>");
              out.println("    <th class=\"boxed\">Date</th>");
              out.println("    <th class=\"boxed\">Vaccination (CVX)</th>");
              out.println("    <th class=\"boxed\">Action</th>");
              out.println("    <th class=\"boxed\">Completion</th>");
              out.println("    <th class=\"boxed\">Source</th>");
              out.println("    <th class=\"boxed\">Refusal</th>");
              out.println("    <th class=\"boxed\">Lot Number</th>");
              out.println("    <th class=\"boxed\">Manufacturer (MVX)</th>");
              out.println("  </tr>");
              SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
              for (Vaccination vaccination : queryResponse.getVaccinationList()) {
                out.println("  <tr>");
                out.println(
                    "    <td class=\"boxed\">" + (vaccination.getAdministrationDate() == null ? ""
                        : sdf.format(vaccination.getAdministrationDate())) + "</td>");
                out.println("    <td class=\"boxed\">" + vaccination.getVaccineLabel() + " ("
                    + vaccination.getVaccineCvx() + ")</td>");
                out.println("    <td class=\"boxed\">" + vaccination.getActionCode() + "</td>");
                out.println(
                    "    <td class=\"boxed\">" + vaccination.getCompletionStatus() + "</td>");
                out.println("    <td class=\"boxed\">" + vaccination.getInformationSourceLabel()
                    + " (" + vaccination.getInformationSource() + ")</td>");
                out.println("    <td class=\"boxed\">" + vaccination.getRefusalReason() + "</td>");
                out.println("    <td class=\"boxed\">" + vaccination.getLotNumber() + "</td>");
                out.println("    <td class=\"boxed\">" + vaccination.getManufacturerLabel() + " ("
                    + vaccination.getManufacturerMvx() + ")</td>");
                out.println("  </tr>");
              }
              out.println("</table>");
            }
          }
        }
      }


      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }



}

