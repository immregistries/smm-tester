/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.immregistries.smm.tester;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.immregistries.smm.transform.ModifyMessageRequest;
import org.immregistries.smm.transform.ModifyMessageService;
import org.immregistries.smm.transform.ScenarioManager;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;

/**
 * 
 * @author nathan
 */
@SuppressWarnings("serial")
public class ModifyMessageServlet extends ClientServlet {
  public static final String IIS_TEST_REPORT_FILENAME_PREFIX = "IIS Test Report";

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * 
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response,
      ModifyMessageRequest mmr) throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();

    String script = request.getParameter("script");
    if (script == null) {
      script = "";
    }
    String originalMessage = request.getParameter("originalMessage");
    if (originalMessage == null) {
      originalMessage = "";
    }

    if (request.getParameter("action") != null
        && request.getParameter("action").equals("Load Example")) {
      TestCaseMessage testCaseMessage =
          ScenarioManager.createTestCaseMessage(ScenarioManager.SCENARIO_1_R_ADMIN_CHILD);
      Transformer transformer = new Transformer();
      transformer.transform(testCaseMessage);
      originalMessage = testCaseMessage.getMessageText();
      script = "PID-5.1=Smith\nPID-8=[MAP 'M'=>'Male', 'F'=>'Female']\nPID-5.3=[TRUNC 1]";
      mmr = null;
    }

    try {
      printHtmlHead(out, MENU_HEADER_HOME, request);
      out.println("<h2>Message Modifier Demo</h2>");
      out.println("    <form action=\"ModifyMessageServlet\" method=\"POST\">");
      out.println("      <table>");
      out.println("        <tr>");
      out.println("          <td valign=\"top\">Original Message</td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println(
          "          <td colspan=\"2\"><textarea name=\"originalMessage\" cols=\"120\" rows=\"15\" wrap=\"off\">"
              + originalMessage + "</textarea></td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td valign=\"top\">Script</td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println(
          "          <td colspan=\"2\"><textarea name=\"script\" cols=\"120\" rows=\"15\" wrap=\"off\">"
              + script + "</textarea></td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td align=\"right\">");
      out.println("            <input type=\"submit\" name=\"action\" value=\"Load Example\"/>");
      out.println("            <input type=\"submit\" name=\"action\" value=\"Run\"/>");
      out.println("          </td>");
      out.println("        </tr>");
      out.println("      </table>");
      out.println("    </form>");
      if (mmr != null) {
        out.println("<h2>Final Message</h2>");
        out.println("<pre>");
        out.print(mmr.getMessageFinal());
        out.println("</pre>");
      }
      out.println("<h3>Explanation</h3>");
      out.println(
          "<p>This web page was created to show the original Message Modify software. It is an integrated ability "
              + "of the Simple Message Mover and was partially extracted to be used by an NIST project. The proposal linked below "
              + "is the original proposal last year to extract the functionality for this project.</p>");
      out.println(
          "<p>Link to original document: <a href=\"https://www.dropbox.com/s/4osx52uphsvh0lu/Modify%20Message%20Recommendation.docx?dl=0\">Message Modifier Recommendations</a></p>");
      out.println(
          "<p>Now in 2016, the next step is to create a more generalized solution that does what this solution does "
              + "but with improvements to the language and the ability to support other domain knowledge areas. The proposal "
              + "listed above is now out-dated and is only being listed for reference purposes. </p>");
      
      out.println("<p><a href=\"GenerateExamplesServlet\">Generate Examples</a></p>");

      ClientServlet.printHtmlFoot(out);
    } catch (Exception e) {
      out.println("<p>Exception Occurred: " + e.getMessage() + "</p><pre>");
      e.printStackTrace(out);
      out.println("</pre>");
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
    processRequest(request, response, null);

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
    String script = request.getParameter("script");
    if (script != null) {
      ModifyMessageRequest mmr = new ModifyMessageRequest();
      String originalMessage = request.getParameter("originalMessage");
      mmr.setMessageOriginal(originalMessage);
      mmr.setTransformScript(script);
      ModifyMessageService modifyMessageService = new ModifyMessageService();
      modifyMessageService.transform(mmr);

      processRequest(request, response, mmr);
      // response.setContentType("text/plain;charset=UTF-8");
      // PrintWriter out = response.getWriter();
      // out.println(finalMessage);
      // out.close();
      // in = new BufferedReader(new StringReader(finalMessage));
      // while ((line = in.readLine()) != null) {
      // out.println("-- " + line);
      // }
      // in.close();
    } else {
      processRequest(request, response, null);
    }

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
