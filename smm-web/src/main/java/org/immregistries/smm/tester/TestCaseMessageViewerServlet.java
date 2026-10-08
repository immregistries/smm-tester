package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.immregistries.smm.transform.TestCaseMessage;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class TestCaseMessageViewerServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

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
    String problem = null;

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
      printPageHeader(out, "Test Case Message", null);

      if (problem != null) {
        out.println("<div class=\"aira-alert aira-alert--warning\" role=\"status\"><p>"
            + escapeHtml(problem) + "</p></div>");
      }
      TestCaseMessage testCaseMessage = (TestCaseMessage) session.getAttribute("testCaseMessage");
      String certifyServletBasicNum = request.getParameter("certifyServletBasicNum");

      if (testCaseMessage != null) {
        out.println("<div class=\"aira-stack\">");
        printTestCaseMessage(out, testCaseMessage);
        out.println("<p><a class=\"aira-button aira-button--primary\" href=\"testCase?"
            + "certifyServletBasicNum=" + URLEncoder.encode(String.valueOf(certifyServletBasicNum),
                StandardCharsets.UTF_8)
            + "\">Run Test Case</a></p>");
        out.println("</div>");
      } else {
        out.println("<div class=\"aira-empty-state\"><p class=\"aira-empty-state__title\">No test"
            + " case message</p><p>Open a test case from <a href=\"SetupServlet\">Manage Test"
            + " Cases</a> first.</p></div>");
      }

      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  /**
   * Writes a test case message and its results. Also used for stand-alone report files, so it
   * writes plain headings and pre blocks that read well without the AIRA stylesheet.
   */
  public static void printTestCaseMessage(PrintWriter out, TestCaseMessage testCaseMessage) {
    out.println("<h2 class=\"aira-section-title\">" + escapeHtml(testCaseMessage.getDescription())
        + "</h2>");

    out.println("<h3 class=\"aira-subsection-title\">Message Sent</h3>");
    out.println("<pre class=\"smm-hl7\">" + escapeHtml(testCaseMessage.getMessageTextSent())
        + "</pre>");

    if (!testCaseMessage.getAdditionalTransformations().equals("")) {
      out.println("<h4>Additional Transformations Applied</h4>");
      out.println("<pre class=\"smm-hl7\">"
          + escapeHtml(testCaseMessage.getAdditionalTransformations()) + "</pre>");
    }
    if (testCaseMessage.isHasRun()) {
      if (testCaseMessage.getActualMessageResponseType().equals("ACK")) {
        if (testCaseMessage.isAccepted()) {
          out.println("<h3 class=\"aira-subsection-title\">Message Accepted</h3>");
        } else {
          out.println("<h3 class=\"aira-subsection-title\">Message Rejected</h3>");
        }
      } else {
        out.println("<h3 class=\"aira-subsection-title\">Response Received</h3>");
      }
      out.println("<pre class=\"smm-hl7\">"
          + escapeHtml(testCaseMessage.getActualResponseMessage()) + "</pre>");
      if (!testCaseMessage.getMessageAcceptStatusDebug().equals("")) {
        out.println("<h4>Logic For Expectation</h4>");
        out.println("<pre class=\"smm-hl7\">"
            + escapeHtml(testCaseMessage.getMessageAcceptStatusDebug()) + "</pre>");
      }
    }

    if (testCaseMessage.getException() != null) {
      out.println("<h3 class=\"aira-subsection-title\">Unexpected Problem Occurred</h3>");
      out.println("<p>Exception occurred: "
          + escapeHtml(testCaseMessage.getException().getMessage()) + "</p>");
      out.println("<pre class=\"smm-hl7\">"
          + escapeHtml(stackTrace(testCaseMessage.getException())) + "</pre>");
    }
    if (testCaseMessage.getDerivedFromVXUMessage() != null
        && !testCaseMessage.getDerivedFromVXUMessage().equals("")) {
      out.println("<h3 class=\"aira-subsection-title\">Request Derived From This VXU Message</h3>");
      out.println("<pre class=\"smm-hl7\">"
          + escapeHtml(testCaseMessage.getDerivedFromVXUMessage()) + "</pre>");
    }
  }
}
