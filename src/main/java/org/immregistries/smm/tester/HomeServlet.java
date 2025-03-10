package org.immregistries.smm.tester;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author nathan
 */
public class HomeServlet extends ClientServlet {
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

    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_HOME, request);
      if (username == null) {
        out.println("<h1>HL7 IIS Tester &amp; Simple Message Mover</h1>");
      } else {
        out.println("<h2>Primary Test Functions</h2>");
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <th>Function</th>");
        out.println("    <th>Details</th>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td><a href=\"SetupServlet\">" + MENU_HEADER_CONNECT + "</a></td>");
        out.println("    <td>Setup connection to an IIS. </td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td><a href=\"SetupServlet\">" + MENU_HEADER_SETUP + "</a></td>");
        out.println("    <td>Load, select, and download test case scripts.</td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td><a href=\"CreateTestCaseServlet\">" + MENU_HEADER_EDIT + "</a></td>");
        out.println("    <td>Edit a selected test case or create a new test case. </td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td><a href=\"SubmitServlet\">" + MENU_HEADER_SEND + "</a></td>");
        out.println("    <td>Submit a message or a test case to an IIS for processing.</td>");
        out.println("  </tr>");
        out.println("</table>");
        out.println("<h2>Other Functions</h2>");
        out.println("<table border=\"1\" cellspacing=\"0\">");
        out.println("  <tr>");
        out.println("    <td><a href=\"StressTestServlet\">Stress Test</a></td>");
        out.println(
            "    <td>Send multiple messages to an IIS to verify it's ability to handle many different requests at the same time. </td>");
        out.println("  </tr>");
        Authenticate.User user = (Authenticate.User) session.getAttribute("user");
        if (user.hasSendData()) {
          out.println("  <tr>");
          out.println("    <td><a href=\"InstallCertServlet\">Install Cert</a></td>");
          out.println("    <td>Install certificate for use on a particular connection.</td>");
          out.println("  </tr>");
          out.println("  <tr>");
          out.println("    <td><a href=\"ManualQueryServlet\">Query IIS</a></td>");
          out.println("    <td>Query and read response from IIS.</td>");
          out.println("  </tr>");
          out.println("  <tr>");
          out.println("    <td><a href=\"BulkQueryServlet\">Bulk Query IIS</a></td>");
          out.println("    <td>Query and read response from IIS.</td>");
          out.println("  </tr>");
        }
        out.println("  <tr>");
        out.println("    <td><a href=\"MessageViewerServlet\">Message Viewer</a></td>");
        out.println("    <td>View Message Details</td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td><a href=\"GenerateDataServlet\">Generate Data</a></td>");
        out.println("    <td>Create sample messages </td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td><a href=\"testCase\">Run Tests</a></td>");
        out.println(
            "    <td>This is a deprecated function that is now covered by the Test IIS. </td>");
        out.println("  </tr>");
        out.println("  <tr>");
        out.println("    <td><a href=\"interfaceProfile\">Profile Interface</a></td>");
        out.println(
            "    <td>This is a deprecated function that is now covered by the Test IIS. </td>");
        out.println("  </tr>");        
        out.println("</table>");
      }
      printHtmlFoot(out);

    } finally {
      out.close();
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
    return "DQA Tester Home Page";
  }// </editor-fold>
}
