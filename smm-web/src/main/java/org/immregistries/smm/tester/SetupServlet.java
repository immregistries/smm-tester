package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.immregistries.smm.transform.ScenarioManager;
import org.immregistries.smm.transform.TestCaseMessage;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author nathan
 */
public class SetupServlet extends ClientServlet {
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

      String testSetSelected = request.getParameter("testSet");
      if (testSetSelected != null) {
        CreateTestCaseServlet.setTestSetSelected(testSetSelected, session);
      } else {
        testSetSelected = CreateTestCaseServlet.getTestSetSelected(session);
      }

      String testScript = request.getParameter("testScript");
      List<TestCaseMessage> selectedTestCaseMessageList = null;
      if (testScript == null) {
        selectedTestCaseMessageList =
            getSelectedTestCaseMessageList(testSetSelected, request, session);
        if (selectedTestCaseMessageList != null) {
          TestCaseServlet.sortTestCaseMessageList(selectedTestCaseMessageList);
          session.setAttribute("selectedTestCaseMessageList", selectedTestCaseMessageList);
        }
      }

      String action = request.getParameter("action");
      if (action != null) {
        if (action.equals("Load Test Cases")) {
          loadTestCases(request, session);
        } else if (action.equals("Download Script") || action.equals("Download HL7 Only")) {
          RequestDispatcher dispatcher = request.getRequestDispatcher("DownloadServlet");
          dispatcher.forward(request, response);
          return;
        } else if (action.equals("Edit")) {
          RequestDispatcher dispatcher = request.getRequestDispatcher("CreateTestCaseServlet");
          dispatcher.forward(request, response);
          return;
        } else if (action.equals("Test")) {
          RequestDispatcher dispatcher = request.getRequestDispatcher("testCase");
          dispatcher.forward(request, response);
          return;
        }
      }

      PrintWriter out = response.getWriter();
      try {
        printHtmlHead(out, MENU_HEADER_SETUP, request);
        printPageHeader(out, "Manage Test Cases",
            "Choose saved test cases to edit, run, or download, load a test case script, or create"
                + " a new test case.");
        out.println("<div class=\"aira-stack\">");

        Map<String, TestCaseMessage> testCaseMessageMap =
            CreateTestCaseServlet.getTestCaseMessageMap(testSetSelected,
                CreateTestCaseServlet.getTestCaseMessageMapMap(session));
        List<String> testCaseNumberList = new ArrayList<String>(testCaseMessageMap.keySet());
        List<String> testCaseSetList =
            new ArrayList<String>(CreateTestCaseServlet.getTestCaseMessageMapMap(session).keySet());
        if (testCaseNumberList.size() > 0 || testCaseSetList.size() > 1) {
          out.println("<section class=\"aira-panel\">");
          out.println("  <div class=\"aira-panel__header\">"
              + "<h2 class=\"aira-panel__title\">Test Cases Saved</h2></div>");
          out.println("  <div class=\"aira-panel__body\">");
          out.println("    <form class=\"aira-form\" action=\"SetupServlet\" method=\"POST\">");
          out.println("      <div class=\"aira-field\">");
          out.println("        <label for=\"testSet\">Test Set</label>");
          out.println("        <select class=\"aira-select smm-select-auto\" id=\"testSet\""
              + " name=\"testSet\" onChange=\"this.form.submit()\">");
          Collections.sort(testCaseSetList);
          out.println("          <option value=\"\"" + (testSetSelected == null ? " selected" : "")
              + ">-- Not Specified --</option>");
          for (String testCaseSet : testCaseSetList) {
            if (testCaseSet.equals("")) {
              continue;
            }
            boolean selected = testSetSelected != null && testSetSelected.equals(testCaseSet);
            out.println("          <option value=\"" + escapeHtml(testCaseSet) + "\""
                + (selected ? " selected" : "") + ">" + escapeHtml(testCaseSet) + "</option>");
          }
          out.println("        </select>");
          out.println("      </div>");
          out.println("      <div class=\"aira-field\">");
          out.println("        <label for=\"testCaseNumber\">Test Cases</label>");
          if (testCaseNumberList.size() > 0) {
            out.println("        <select class=\"aira-select\" id=\"testCaseNumber\""
                + " name=\"testCaseNumber\" multiple size=\"8\">");
            Collections.sort(testCaseNumberList);
            @SuppressWarnings("unchecked")
            Set<String> testCaseNumberSelectedSet =
                (Set<String>) session.getAttribute("testCaseNumberSelectedList");
            if (testCaseNumberSelectedSet == null) {
              testCaseNumberSelectedSet = new HashSet<String>();
            }
            for (String testCaseNumber : testCaseNumberList) {
              TestCaseMessage tcm = testCaseMessageMap.get(testCaseNumber);
              String text = tcm.getTestCaseNumber() + ": " + (tcm.getDescription().length() > 80
                  ? tcm.getDescription().substring(0, 80) + "..." : tcm.getDescription());
              boolean selected = testCaseNumberSelectedSet.contains(testCaseNumber);
              out.println("          <option value=\"" + escapeHtml(tcm.getTestCaseNumber()) + "\""
                  + (selected ? " selected" : "") + ">" + escapeHtml(text) + "</option>");
            }
            out.println("        </select>");
            out.println("        <p class=\"aira-field-help\">Hold Ctrl or Shift to select more"
                + " than one.</p>");
          } else {
            out.println("        <p class=\"aira-muted\">No test cases saved in this test set.</p>");
          }
          out.println("      </div>");
          if (testCaseNumberList.size() > 0) {
            out.println("      <div class=\"aira-form-actions\">");
            out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
                + " name=\"action\" value=\"Test\">Test</button>");
            out.println("        <button class=\"aira-button aira-button--secondary\" type=\"submit\""
                + " name=\"action\" value=\"Edit\">Edit</button>");
            out.println("        <button class=\"aira-button aira-button--tertiary\" type=\"submit\""
                + " name=\"action\" value=\"Download Script\">Download Script</button>");
            out.println("        <button class=\"aira-button aira-button--tertiary\" type=\"submit\""
                + " name=\"action\" value=\"Download HL7 Only\">Download HL7 Only</button>");
            out.println("      </div>");
          }
          out.println("    </form>");
          out.println("  </div>");
          out.println("</section>");
        }

        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">Load Test Cases</h2></div>");
        out.println("  <div class=\"aira-panel__body\">");
        out.println("    <form class=\"aira-form\" action=\"SetupServlet\" method=\"POST\">");
        out.println("      <div class=\"aira-field\">");
        out.println("        <label for=\"testScript\">Script</label>");
        out.println("        <textarea class=\"aira-textarea smm-code\" id=\"testScript\""
            + " name=\"testScript\" rows=\"8\" wrap=\"off\"></textarea>");
        out.println("      </div>");
        out.println("      <div class=\"aira-form-actions\">");
        out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
            + " name=\"action\" value=\"Load Test Cases\">Load Test Cases</button>");
        out.println("        <button class=\"aira-button aira-button--tertiary\" type=\"submit\""
            + " name=\"action\" value=\"Download Script\">Download Script</button>");
        out.println("        <button class=\"aira-button aira-button--tertiary\" type=\"submit\""
            + " name=\"action\" value=\"Download HL7 Only\">Download HL7 Only</button>");
        out.println("      </div>");
        out.println("    </form>");
        out.println("  </div>");
        out.println("</section>");

        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">Create a New Test Case</h2></div>");
        out.println("  <div class=\"aira-panel__body\">");
        out.println("    <p>Create a sample test case based on a specific NIST certification test"
            + " story.</p>");
        out.println("    <form class=\"aira-inline-form\" action=\"CreateTestCaseServlet\">");
        out.println("      <label class=\"aira-label\" for=\"scenario\">Scenario</label>");
        out.println("      <select class=\"aira-select smm-select-auto\" id=\"scenario\""
            + " name=\"scenario\">");
        for (String scenario : ScenarioManager.SCENARIOS) {
          out.println("        <option value=\"" + escapeHtml(scenario) + "\">"
              + escapeHtml(scenario) + "</option>");
        }
        out.println("      </select>");
        out.println("      <button class=\"aira-button aira-button--secondary\" type=\"submit\""
            + " name=\"Start\" value=\"Create\">Create</button>");
        out.println("    </form>");
        out.println("  </div>");
        out.println("</section>");
        out.println("</div>");
        printHtmlFoot(out);

      } finally {
        out.close();
      }
    }
  }

  protected static List<TestCaseMessage> getSelectedTestCaseMessageList(String testCaseSet,
      HttpServletRequest request, HttpSession session) {
    List<TestCaseMessage> testCaseMessageList = new ArrayList<TestCaseMessage>();
    Set<String> testCaseNumberSelectedSet =
        TestCaseServlet.setTestCaseNumberSelectedSet(request, session);
    Map<String, TestCaseMessage> testCaseMessageMap = CreateTestCaseServlet.getTestCaseMessageMap(
        testCaseSet, CreateTestCaseServlet.getTestCaseMessageMapMap(session));
    for (String testCaseNumber : testCaseNumberSelectedSet) {
      TestCaseMessage tcm = testCaseMessageMap.get(testCaseNumber);
      if (testCaseNumberSelectedSet.contains(tcm.getTestCaseNumber())) {
        testCaseMessageList.add(tcm);
      }
    }
    return testCaseMessageList;
  }

  protected void loadTestCases(HttpServletRequest request, HttpSession session) {
    String testScript = request.getParameter("testScript");
    try {
      List<TestCaseMessage> testCaseMessageList = TestCaseServlet.parseAndAddTestCases(testScript);
      for (TestCaseMessage testCaseMessage : testCaseMessageList) {
        if (!testCaseMessage.getTestCaseNumber().equals("")) {
          CreateTestCaseServlet
              .getTestCaseMessageMap(testCaseMessage.getTestCaseSet(),
                  CreateTestCaseServlet.getTestCaseMessageMapMap(session))
              .put(testCaseMessage.getTestCaseNumber(), testCaseMessage);
        }
      }
    } catch (Throwable e) {
      String message = "Unable to load test script, exception ocurred: " + e.getMessage();
      request.setAttribute("message", message);
    }
  }

  // <editor-fold defaultstate="collapsed"
  // desc="HttpServlet methods. Click on the + sign on the left to edit the
  // code.">

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
