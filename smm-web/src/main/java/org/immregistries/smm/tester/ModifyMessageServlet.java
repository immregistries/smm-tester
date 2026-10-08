package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.IOException;
import java.io.PrintWriter;
import org.immregistries.smm.transform.ModifyMessageRequest;
import org.immregistries.smm.transform.ModifyMessageService;
import org.immregistries.smm.transform.ScenarioManager;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author nathan
 */
public class ModifyMessageServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;
  

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
      printPageHeader(out, "Modify Message",
          "Apply a message modification script to an HL7 message and see the result.");
      out.println("<div class=\"aira-stack\">");
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__body\">");
      out.println("    <form class=\"aira-form\" action=\"ModifyMessageServlet\" method=\"POST\">");
      out.println("      <div class=\"aira-field\">");
      out.println("        <label for=\"originalMessage\">Original Message</label>");
      out.println("        <textarea class=\"aira-textarea smm-code\" id=\"originalMessage\""
          + " name=\"originalMessage\" rows=\"12\" wrap=\"off\">" + escapeHtml(originalMessage)
          + "</textarea>");
      out.println("      </div>");
      out.println("      <div class=\"aira-field\">");
      out.println("        <label for=\"script\">Script</label>");
      out.println("        <textarea class=\"aira-textarea smm-code\" id=\"script\" name=\"script\""
          + " rows=\"8\" wrap=\"off\">" + escapeHtml(script) + "</textarea>");
      out.println("      </div>");
      out.println("      <div class=\"aira-form-actions\">");
      out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
          + " name=\"action\" value=\"Run\">Run</button>");
      out.println("        <button class=\"aira-button aira-button--secondary\" type=\"submit\""
          + " name=\"action\" value=\"Load Example\">Load Example</button>");
      out.println("      </div>");
      out.println("    </form>");
      out.println("  </div>");
      out.println("</section>");
      if (mmr != null) {
        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">Final Message</h2></div>");
        out.println("  <div class=\"aira-panel__body\">");
        out.println("    <pre class=\"smm-hl7\">" + escapeHtml(mmr.getMessageFinal()) + "</pre>");
        out.println("  </div>");
        out.println("</section>");
      }
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__header\">"
          + "<h2 class=\"aira-panel__title\">Explanation</h2></div>");
      out.println("  <div class=\"aira-panel__body aira-prose\">");
      out.println(
          "    <p>This web page was created to show the original Message Modify software. It is an integrated ability "
              + "of the Simple Message Mover and was partially extracted to be used by an NIST project. The proposal linked below "
              + "is the original proposal last year to extract the functionality for this project.</p>");
      out.println(
          "    <p>Link to original document: <a href=\"https://www.dropbox.com/s/4osx52uphsvh0lu/Modify%20Message%20Recommendation.docx?dl=0\">Message Modifier Recommendations</a></p>");
      out.println(
          "    <p>Now in 2016, the next step is to create a more generalized solution that does what this solution does "
              + "but with improvements to the language and the ability to support other domain knowledge areas. The proposal "
              + "listed above is now out-dated and is only being listed for reference purposes. </p>");
      out.println("    <p><a href=\"GenerateExamplesServlet\">Generate Examples</a></p>");
      out.println("  </div>");
      out.println("</section>");
      out.println("</div>");
      ClientServlet.printHtmlFoot(out);
    } catch (Exception e) {
      printException(out, e);
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
