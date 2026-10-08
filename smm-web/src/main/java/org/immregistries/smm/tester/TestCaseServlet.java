package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.TestCaseMessageManager;
import org.immregistries.smm.tester.run.TestRunner;
import org.immregistries.smm.transform.TestCaseMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author nathan
 */
public class TestCaseServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

  @Override
  public void init() throws ServletException {
    super.init();
  }

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
    if (username == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
    } else {
      PrintWriter out = response.getWriter();
      try {
        printHtmlHead(out, MENU_HEADER_HOME, request);
        printPageHeader(out, "Test Results", null);
        out.println("<div class=\"aira-stack\">");
        int id = 0;
        List<Connector> connectors = ConnectServlet.getConnectors(session);
        if (connectors.size() == 1) {
          id = 1;
        } else {
          if (request.getParameter("id") != null && request.getParameter("id").length() > 0) {
            id = Integer.parseInt(request.getParameter("id"));
          }
        }
        session.setAttribute("id", id);
        boolean goodToQuery = id > 0;
        Connector connector = null;
        if (goodToQuery) {
          connector = SubmitServlet.getConnector(id, session);
        } else {
          out.println("<div class=\"aira-alert aira-alert--info\" role=\"status\"><p>No connection"
              + " specified, displaying but not running tests.</p></div>");
        }
        Set<String> testCaseNumberSelectedSet = setTestCaseNumberSelectedSet(request, session);

        String testCase = request.getParameter("source");
        List<TestCaseMessage> testCaseMessageList = parseAndAddTestCases(testCase);

        Map<String, TestCaseMessage> testCaseMessageMap = CreateTestCaseServlet
            .getTestCaseMessageMap(null, CreateTestCaseServlet.getTestCaseMessageMapMap(session));
        for (String testCaseNumber : testCaseNumberSelectedSet) {
          TestCaseMessage tcm = testCaseMessageMap.get(testCaseNumber);
          if (tcm != null) {
            boolean alreadyBeingTested = false;
            for (TestCaseMessage testing : testCaseMessageList) {
              if (testing.getTestCaseNumber().equals(tcm.getTestCaseNumber())) {
                alreadyBeingTested = true;
                break;
              }
            }
            if (!alreadyBeingTested) {
              testCaseMessageList.add(tcm);
            }
          }
        }

        Collections.sort(testCaseMessageList, new Comparator<TestCaseMessage>() {
          public int compare(TestCaseMessage o1, TestCaseMessage o2) {
            return o1.getTestCaseNumber().compareTo(o2.getTestCaseNumber());
          }
        });

        // Run every test first so the summary can be shown above the details
        TestRunner testRunner = new TestRunner();
        Map<TestCaseMessage, String> ackMap = new HashMap<TestCaseMessage, String>();
        Map<TestCaseMessage, Boolean> passMap = new HashMap<TestCaseMessage, Boolean>();
        Map<TestCaseMessage, Throwable> errorMap = new HashMap<TestCaseMessage, Throwable>();
        for (TestCaseMessage testCaseMessage : testCaseMessageList) {
          if (goodToQuery) {
            try {
              passMap.put(testCaseMessage, testRunner.runTest(connector, testCaseMessage));
              ackMap.put(testCaseMessage, testRunner.getAckMessageText());
            } catch (Throwable t) {
              errorMap.put(testCaseMessage, t);
            }
          }
          if (!testCaseMessage.getTestCaseNumber().equals("")) {
            CreateTestCaseServlet
                .getTestCaseMessageMap(testCaseMessage.getTestCaseSet(),
                    CreateTestCaseServlet.getTestCaseMessageMapMap(session))
                .put(testCaseMessage.getTestCaseNumber(), testCaseMessage);
          }
        }
        if (testCaseMessageList.size() == 1) {
          session.setAttribute("testCaseMessage", testCaseMessageList.get(0));
        }

        out.println("<section class=\"aira-table-panel\">");
        out.println("  <div class=\"aira-table-panel__header\"><div>"
            + "<h2 class=\"aira-table-panel__title\">Test Result Summary</h2></div></div>");
        out.println("  <div class=\"aira-table-wrap\">");
        out.println("    <table class=\"aira-table\">");
        out.println("      <caption class=\"aira-visually-hidden\">Test result summary</caption>");
        out.println("      <thead><tr><th scope=\"col\">Test Case</th><th scope=\"col\">Status</th>"
            + "<th scope=\"col\">Expected Result</th><th scope=\"col\">Actual Result</th>"
            + "<th scope=\"col\">Actual Message</th></tr></thead>");
        out.println("      <tbody>");
        if (testCaseMessageList.isEmpty()) {
          out.println("        <tr><td colspan=\"5\" class=\"aira-table__empty\">No test cases were"
              + " selected or entered.</td></tr>");
        }
        for (TestCaseMessage testCaseMessage : testCaseMessageList) {
          out.println("        <tr>");
          out.println("          <th scope=\"row\" class=\"aira-table__cell--primary\"><a href=\"#tc-"
              + escapeHtml(testCaseMessage.getTestCaseNumber()) + "\">"
              + escapeHtml(testCaseMessage.getTestCaseNumber()) + "</a></th>");
          out.println("          <td>" + statusBadge(testCaseMessage.getActualResultStatus())
              + "</td>");
          out.println("          <td>" + escapeHtml(testCaseMessage.getAssertResult()) + "</td>");
          out.println("          <td>" + escapeHtml(testCaseMessage.getActualResultAckType())
              + "</td>");
          out.println("          <td class=\"aira-table__cell--wrap\">"
              + escapeHtml(testCaseMessage.getActualResponseMessage()) + "</td>");
          out.println("        </tr>");
        }
        out.println("      </tbody>");
        out.println("    </table>");
        out.println("  </div>");
        out.println("</section>");

        for (TestCaseMessage testCaseMessage : testCaseMessageList) {
          String testCaseNumber = testCaseMessage.getTestCaseNumber();
          String badge;
          if (!goodToQuery) {
            badge = "<span class=\"aira-badge aira-badge--outline\">Not run</span>";
          } else if (errorMap.containsKey(testCaseMessage)) {
            badge = "<span class=\"aira-badge aira-badge--danger\">Error</span>";
          } else if (Boolean.TRUE.equals(passMap.get(testCaseMessage))) {
            badge = "<span class=\"aira-badge aira-badge--success\">Pass</span>";
          } else {
            badge = "<span class=\"aira-badge aira-badge--danger\">Fail</span>";
          }
          out.println("<details class=\"smm-disclosure\" id=\"tc-" + escapeHtml(testCaseNumber)
              + "\"" + (testCaseMessageList.size() == 1 ? " open" : "") + ">");
          out.println("  <summary>Test Case " + escapeHtml(testCaseNumber) + " " + badge
              + "</summary>");
          out.println("  <div class=\"aira-stack aira-stack--compact\">");
          out.println("    <p><a class=\"aira-button aira-button--small aira-button--secondary\""
              + " href=\"CreateTestCaseServlet?testCase="
              + URLEncoder.encode(testCaseNumber, "UTF-8") + "\">Edit</a></p>");
          if (errorMap.containsKey(testCaseMessage)) {
            printException(out, errorMap.get(testCaseMessage));
          }
          out.println("    <dl class=\"smm-details\">");
          out.println("      <dt>Description</dt><dd>"
              + escapeHtml(testCaseMessage.getDescription()) + "</dd>");
          out.println("      <dt>Assert Result</dt><dd>"
              + escapeHtml(testCaseMessage.getAssertResult()) + "</dd>");
          if (!testCaseMessage.getCustomTransformations().equals("")) {
            out.println("      <dt>Changes</dt><dd><pre class=\"smm-hl7\">"
                + escapeHtml(testCaseMessage.getCustomTransformations()) + "</pre></dd>");
          }
          if (!testCaseMessage.getAdditionalTransformations().equals("")) {
            out.println("      <dt>Additional Changes</dt><dd><pre class=\"smm-hl7\">"
                + escapeHtml(testCaseMessage.getAdditionalTransformations()) + "</pre></dd>");
          }
          if (!testCaseMessage.getCauseIssues().equals("")) {
            out.println("      <dt>Issues</dt><dd><pre class=\"smm-hl7\">"
                + escapeHtml(testCaseMessage.getCauseIssues()) + "</pre></dd>");
          }
          for (TestCaseMessage.Comment comment : testCaseMessage.getComments()) {
            out.println("      <dt>Comment: " + escapeHtml(comment.getName()) + "</dt><dd>"
                + escapeHtml(comment.getText()) + "</dd>");
          }
          out.println("    </dl>");
          out.println("    <span class=\"aira-label\">Message Sent</span>");
          out.println("    <pre class=\"smm-hl7\">" + escapeHtml(testCaseMessage.getMessageText())
              + "</pre>");
          if (ackMap.get(testCaseMessage) != null) {
            out.println("    <span class=\"aira-label\">Response</span>");
            out.println("    <pre class=\"smm-hl7\">" + escapeHtml(ackMap.get(testCaseMessage))
                + "</pre>");
          }
          out.println("  </div>");
          out.println("</details>");
        }

        out.println("<details class=\"smm-disclosure\">");
        out.println("  <summary>Test Script</summary>");
        out.print("  <pre class=\"smm-hl7\">");
        for (TestCaseMessage testCaseMessage : testCaseMessageList) {
          out.println(escapeHtml(testCaseMessage.createText()));
          out.println();
        }
        out.println("</pre>");
        out.println("</details>");
        out.println("<details class=\"smm-disclosure\">");
        out.println("  <summary>Messages Only</summary>");
        out.print("  <pre class=\"smm-hl7\">");
        for (TestCaseMessage testCaseMessage : testCaseMessageList) {
          out.println(escapeHtml(testCaseMessage.getMessageText()));
          out.println();
        }
        out.println("</pre>");
        out.println("</details>");
        out.println("</div>");
        printHtmlFoot(out);
      } finally {
        out.close();
      }
    }
  }

  protected static Set<String> setTestCaseNumberSelectedSet(HttpServletRequest request,
      HttpSession session) {
    Set<String> testCaseNumberSelectedSet = new HashSet<String>();
    String[] testCaseNumberSelected = request.getParameterValues("testCaseNumber");
    if (testCaseNumberSelected != null) {
      for (String s : testCaseNumberSelected) {
        if (s != null && s.length() > 0) {
          testCaseNumberSelectedSet.add(s);
        }
      }
    }
    session.setAttribute("testCaseNumberSelectedList", testCaseNumberSelectedSet);
    return testCaseNumberSelectedSet;
  }

  protected static List<TestCaseMessage> parseAndAddTestCases(String testCase) {
    List<TestCaseMessage> testCaseMessageList = null;
    if (testCase == null) {
      testCaseMessageList = new ArrayList<TestCaseMessage>();
    } else {
      try {
        testCaseMessageList = TestCaseMessageManager.createTestCaseMessageList(testCase);
      } catch (Exception e) {
        throw new IllegalArgumentException("Unable to read test case messages", e);
      }
    }
    return testCaseMessageList;
  }

  protected static void sortTestCaseMessageList(List<TestCaseMessage> testCaseMessageList) {
    Collections.sort(testCaseMessageList, new Comparator<TestCaseMessage>() {

      public int compare(TestCaseMessage o1, TestCaseMessage o2) {
        return o1.getTestCaseNumber().compareTo(o2.getTestCaseNumber());
      }
    });
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
    response.setContentType("text/html;charset=UTF-8");
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
    } else {
      PrintWriter out = response.getWriter();
      try {
        printHtmlHead(out, MENU_HEADER_HOME, request);
        printPageHeader(out, "Run Test Cases",
            "Run one or more test cases against an IIS and compare the results with what each test"
                + " case expects.");
        out.println("<div class=\"aira-stack\">");
        int id = 0;
        if (request.getParameter("id") != null) {
          id = Integer.parseInt(request.getParameter("id"));
        }
        if (session.getAttribute("id") != null) {
          id = (Integer) session.getAttribute("id");
        }
        TestCaseMessage testCaseMessage = (TestCaseMessage) session.getAttribute("testCaseMessage");
        if (testCaseMessage == null) {
          testCaseMessage = new TestCaseMessage();
        }

        List<Connector> connectors = ConnectServlet.getConnectors(session);
        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__body\">");
        out.println("    <form class=\"aira-form\" action=\"testCase\" method=\"POST\">");
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
          if (connectors.isEmpty()) {
            out.println("        <p class=\"aira-field-help\">No IIS connection is set up, so the"
                + " tests will be shown but not run. <a href=\"ConnectServlet\">Connect to an"
                + " IIS</a></p>");
          }
        }
        out.println("      </div>");
        out.println("      <div class=\"aira-field\">");
        out.println("        <label for=\"source\">Test</label>");
        out.println("        <textarea class=\"aira-textarea smm-code\" id=\"source\" name=\"source\""
            + " rows=\"12\" wrap=\"off\">" + escapeHtml(testCaseMessage.createText())
            + "</textarea>");
        out.println("      </div>");
        out.println("      <div class=\"aira-form-actions\">");
        out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
            + " name=\"method\" value=\"Submit\">Submit</button>");
        out.println("      </div>");
        out.println("    </form>");
        out.println("  </div>");
        out.println("</section>");
        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">How to Use This Page</h2></div>");
        out.println("  <div class=\"aira-panel__body aira-prose\">");
        out.println("    <p>Select the target connection, then enter one or more test case"
            + " descriptions into the test area.</p>");
        out.println("    <p>After submitting you will see a summary of the results and the details"
            + " for each test case. If there is more than one test case, the details are collapsed"
            + " so that only the test case id and its pass or fail status are shown. Open a test"
            + " case to see its details.</p>");
        out.println("  </div>");
        out.println("</section>");
        out.println("</div>");
        printHtmlFoot(out);
      } finally {
        out.close();
      }
    }
  }

  /**
   * Returns a short description of the servlet.
   * 
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "";
  }// </editor-fold>
}
