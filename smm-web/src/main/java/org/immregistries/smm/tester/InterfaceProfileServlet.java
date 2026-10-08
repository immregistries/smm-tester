package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.TestCaseMessageManager;
import org.immregistries.smm.tester.run.TestRunner;
import org.immregistries.smm.tester.transform.Issue;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;
import org.immregistries.smm.web.auth.SmmUser;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author nathan
 */
public class InterfaceProfileServlet extends ClientServlet {
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
    SmmUser user = (SmmUser) session.getAttribute("user");
    if (username == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
    } else {
      PrintWriter out = response.getWriter();
      try {
        printHtmlHead(out, MENU_HEADER_HOME, request);
        printPageHeader(out, "Interface Profile Results", null);
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
        boolean goodToQuery = true;
        Connector connector = null;
        if (id > 0) {
          connector = SubmitServlet.getConnector(id, session);
        } else {
          printAlert(out, "info", "No connection specified, not running interface profile.");
        }

        String testCase = request.getParameter("source");
        if (testCase == null || testCase.trim().length() == 0) {
          testCase = null;
        }
        Map<String, String> expectedStatusMap = null;
        String expectedText = request.getParameter("expected");
        if (expectedText != null && expectedText.length() > 0) {
          expectedStatusMap = new HashMap<String, String>();
          try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new ByteArrayInputStream(expectedText.getBytes(StandardCharsets.UTF_8)));
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getChildNodes();
            if (nodes.getLength() == 1) {
              String imbeddedTestCase =
                  readSubParts(expectedStatusMap, nodes.item(0).getChildNodes());
              if (testCase == null) {
                testCase = imbeddedTestCase;
              }
            }
            if (expectedStatusMap.size() == 0) {
              throw new Exception("No expected results found, unable to profile interface");
            }
            goodToQuery = true;
          } catch (Exception e) {
            goodToQuery = false;
            printAlert(out, "error", "Unable to read DQA Report Template XML.");
            printException(out, e);
          }

        }

        int batchSize = 0;
        String batchSizeString = request.getParameter("batchSize");
        if (batchSizeString != null && !batchSizeString.equals("")) {
          try {
            batchSize = Integer.parseInt(batchSizeString);
          } catch (NumberFormatException nfe) {
            batchSize = 0;
          }
        }

        List<TestCaseMessage> testCaseMessageList = null;
        if (testCase == null || testCase.equals("")) {
          goodToQuery = false;
          printAlert(out, "warning", "Test case message not set, unable to run test.");
        } else {
          testCaseMessageList = parseAndAddTestCases(testCase, session);
        }

        TestRunner testRunner = new TestRunner();
        TestCaseMessage testCaseMessageBase = null;
        if (goodToQuery && testCaseMessageList.size() > 0) {
          testCaseMessageBase = testCaseMessageList.get(0);
          if (connector != null) {
            try {
              try {
                testRunner.runTest(connector, testCaseMessageBase);
              } catch (Throwable t) {
                printException(out, t);
              }

            } catch (Exception e) {
              printException(out, e);
            }
            if (testRunner.getStatus().equals("A")) {
              goodToQuery = true;
            } else {
              goodToQuery = false;
              printAlert(out, "error", "Unable to interface profile, base message failed.");
              out.println("<pre class=\"smm-hl7\">" + escapeHtml(testRunner.getAckMessageText())
                  + "</pre>");
            }
          }

        } else if (testCaseMessageList.size() == 0) {
          goodToQuery = false;
          printAlert(out, "warning", "Unable to profile interface, no test case message found.");

        }

        Transformer transformer = new Transformer();

        SimpleDateFormat sdf = new SimpleDateFormat("msS");
        String mrnBase =
            (testCaseMessageBase == null ? "" : testCaseMessageBase.getTestCaseNumber()) + ""
                + sdf.format(new Date());
        String filenameBase =
            (testCaseMessageBase == null ? "" : testCaseMessageBase.getTestCaseNumber());

        PrintWriter sampleFileOut = null;
        if (goodToQuery) {

          if (user.hasSendData() && batchSize > 0) {
            try {
              TestCaseMessage testCaseMessage = new TestCaseMessage(testCaseMessageBase);

              File generatedDir = user.getSendData().getGeneratedDir();
              File file = new File(generatedDir, filenameBase + "-000 Base Messages.txt");
              PrintWriter fileOut = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8));
              file = new File(generatedDir, filenameBase + " Sample Messages.txt");
              sampleFileOut = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8));
              for (int i = 0; i < batchSize; i++) {
                testCaseMessage = new TestCaseMessage(testCaseMessageBase);
                testCaseMessage
                    .setTestCaseNumber(mrnBase + (i < 10 ? "00" : (i < 100 ? "0" : "")) + i);
                transformer.transform(testCaseMessage);
                fileOut.print(testCaseMessage.getMessageText());
                if (i == 0) {
                  sampleFileOut.print(testCaseMessage.getMessageText());
                }
              }
              fileOut.close();
            } catch (Exception e) {
              e.printStackTrace();
            }

          }

          out.println("<div class=\"aira-table-wrap\">");
          out.println("<table class=\"aira-table\">");
          out.println("  <caption class=\"aira-visually-hidden\">Interface profile</caption>");
          out.println("  <thead><tr>");
          out.println("    <th scope=\"col\" class=\"aira-table__cell--numeric\">#</th>");
          out.println("    <th scope=\"col\">Issue</th>");
          if (expectedStatusMap != null) {
            out.println("    <th scope=\"col\">Expect</th>");
          }
          out.println("    <th scope=\"col\">Status Is</th>");
          out.println("    <th scope=\"col\">Text</th>");
          out.println("    <th scope=\"col\">Status Not</th>");
          out.println("    <th scope=\"col\">Text</th>");
          out.println("  </tr></thead>");
          out.println("  <tbody>");

          int count = 0;
          for (Issue issue : Issue.values()) {
            count++;
            if (count % 25 == 0) {
              out.flush();
            }
            TestCaseMessage testCaseMessage = new TestCaseMessage(testCaseMessageBase);
            testCaseMessage.addCauseIssues(issue.getName());
            testCaseMessage.setTestCaseNumber(
                mrnBase + (count < 10 ? "00" : (count < 100 ? "0" : "")) + count + "0");
            TestCaseMessage testCaseMessageNot = new TestCaseMessage(testCaseMessageBase);
            testCaseMessageNot.addCauseIssues("NOT " + issue.getName());
            testCaseMessageNot.setTestCaseNumber(
                mrnBase + (count < 10 ? "00" : (count < 100 ? "0" : "")) + count + "1");
            transformer.transform(testCaseMessage);
            transformer.transform(testCaseMessageNot);
            out.println("  <tr>");
            out.println("    <td class=\"aira-table__cell--numeric\">" + count + "</td>");
            out.println("    <th scope=\"row\" class=\"aira-table__cell--primary\">"
                + escapeHtml(issue.getName()) + "</th>");
            String expectedStatus = "-";
            if (expectedStatusMap != null) {
              expectedStatus = expectedStatusMap.get(issue.getName());
              if (expectedStatus == null) {
                expectedStatus = "-";
              }
              out.println("    <td>" + escapeHtml(expectedStatus) + "</td>");
            }
            out.println("    <td>");
            String ack = null;
            if (!testCaseMessage.hasIssue()) {
              out.println("-");
            } else {
              if (connector == null) {
                out.println("<span class=\"aira-badge aira-badge--outline\">Not run</span>");
              } else {
                try {
                  try {
                    testRunner.runTest(connector, testCaseMessage);
                    ack = testRunner.getAckMessageText();
                  } catch (Throwable t) {
                    out.println("<pre class=\"smm-hl7\">" + escapeHtml(stackTrace(t)) + "</pre>");
                  }
                  if (expectedStatusMap != null && !expectedStatus.equals("-")) {
                    if (expectedStatus.equals("S")) {
                      expectedStatus = "A";
                    }
                    if (expectedStatus.equals(testRunner.getStatus())) {
                      out.println("<span class=\"aira-badge aira-badge--success\">"
                          + escapeHtml(testRunner.getStatus()) + "</span>");
                    } else {
                      out.println("<span class=\"aira-badge aira-badge--danger\">"
                          + escapeHtml(testRunner.getStatus()) + "</span>");
                    }
                  } else {
                    out.println(escapeHtml(testRunner.getStatus()));
                  }
                } catch (Exception e) {
                  printException(out, e);
                }
              }
              if (user.hasSendData()) {
                String countText = "" + count;
                if (count < 100) {
                  if (count > 9) {
                    countText = "0" + count;
                  } else {
                    countText = "00" + count;
                  }
                }
                try {
                  File file = new File(user.getSendData().getGeneratedDir(),
                      filenameBase + "-" + countText + " " + issue.getName() + ".txt");
                  PrintWriter fileOut = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8));
                  for (int i = 0; i < batchSize; i++) {
                    testCaseMessage = new TestCaseMessage(testCaseMessageBase);
                    testCaseMessage
                        .addCauseIssues(i < 20 ? issue.getName() : "NOT " + issue.getName());
                    testCaseMessage.setTestCaseNumber(
                        mrnBase + countText + (i < 10 ? "00" : (i < 100 ? "0" : "")) + i);
                    transformer.transform(testCaseMessage);
                    fileOut.print(testCaseMessage.getMessageText());
                    if (i == 0) {
                      sampleFileOut.print(testCaseMessage.getMessageText());
                    }
                  }
                  fileOut.close();
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
            }
            out.println("    </td>");
            out.println("    <td>");
            printMessageAndAck(out, testCaseMessage.getMessageText(), ack);
            out.println("    </td>");
            out.println("    <td>");
            ack = null;
            if (!testCaseMessageNot.hasIssue()) {
              out.println("-");
            } else {
              if (connector == null) {
                out.println("<span class=\"aira-badge aira-badge--outline\">Not run</span>");
              } else {
                try {
                  try {
                    testRunner.runTest(connector, testCaseMessageNot);
                    ack = testRunner.getAckMessageText();
                  } catch (Throwable t) {
                    out.println("<pre class=\"smm-hl7\">" + escapeHtml(stackTrace(t)) + "</pre>");
                  }
                  if (expectedStatusMap != null && !expectedStatus.equals("-")) {
                    if ("A".equals(testRunner.getStatus())) {
                      out.println("<span class=\"aira-badge aira-badge--success\">"
                          + escapeHtml(testRunner.getStatus()) + "</span>");
                    } else {
                      out.println("<span class=\"aira-badge aira-badge--danger\">"
                          + escapeHtml(testRunner.getStatus()) + "</span>");
                    }
                  } else {
                    out.println(escapeHtml(testRunner.getStatus()));
                  }
                } catch (Exception e) {
                  printException(out, e);
                }
              }
            }
            out.println("    </td>");
            out.println("    <td>");
            printMessageAndAck(out, testCaseMessageNot.getMessageText(), ack);
            out.println("    </td>");
            out.println("  </tr>");
          }
          out.println("  </tbody>");
          out.println("</table>");
          out.println("</div>");
          if (sampleFileOut != null) {
            sampleFileOut.close();
          }
        }
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

  protected static List<TestCaseMessage> parseAndAddTestCases(String testCase, HttpSession session)
      throws ServletException {
    List<TestCaseMessage> testCaseMessageList = null;
    if (testCase == null) {
      testCaseMessageList = new ArrayList<TestCaseMessage>();
    } else {
      try {
        testCaseMessageList = TestCaseMessageManager.createTestCaseMessageList(testCase);
      } catch (Exception e) {
        throw new ServletException("Unable to read test case messages", e);
      }
    }
    return testCaseMessageList;
  }

  private static void printAlert(PrintWriter out, String variant, String text) {
    out.println("<div class=\"aira-alert aira-alert--" + variant + "\" role=\"status\"><p>"
        + escapeHtml(text) + "</p></div>");
  }

  /** Writes a collapsed view of a test message and the acknowledgement it received. */
  private static void printMessageAndAck(PrintWriter out, String messageText, String ack) {
    out.println("      <details class=\"smm-disclosure smm-disclosure--compact\">");
    out.println("        <summary>Show</summary>");
    out.println("        <pre class=\"smm-hl7\">" + escapeHtml(messageText) + "</pre>");
    out.println("        <pre class=\"smm-hl7\">" + (ack == null ? "Not Run" : escapeHtml(ack))
        + "</pre>");
    out.println("      </details>");
  }

  private static String readSubParts(Map<String, String> expectedStatusMap, NodeList nodes) {
    String testScript = null;
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE && "report-template".equals(node.getNodeName())) {
        nodes = nodes.item(i).getChildNodes();
        for (i = 0; i < nodes.getLength(); i++) {
          node = nodes.item(i);
          if ("base-profile".equals(node.getNodeName())) {
            readExpectedIssues(expectedStatusMap, nodes.item(i).getChildNodes());
          } else if ("test-case-script".equals(node.getNodeName())) {
            testScript = node.getTextContent();
          }
        }
        break;
      }
    }
    return testScript;
  }

  private static void readExpectedIssues(Map<String, String> expectedStatusMap, NodeList nodes) {
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        NamedNodeMap map = node.getAttributes();
        if ("potential-issue-status".equals(node.getNodeName())) {
          expectedStatusMap.put(safe(map.getNamedItem("issue")),
              safe(map.getNamedItem("actionCode")));
        }
      }
    }
  }

  protected static void sortTestCaseMessageList(List<TestCaseMessage> testCaseMessageList) {
    Collections.sort(testCaseMessageList, new Comparator<TestCaseMessage>() {
      public int compare(TestCaseMessage o1, TestCaseMessage o2) {
        return o1.getTestCaseNumber().compareTo(o2.getTestCaseNumber());
      }
    });
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
      PrintWriter out = response.getWriter();
      try {
        printHtmlHead(out, MENU_HEADER_HOME, request);
        printPageHeader(out, "Interface Profile",
            "Send a base message with each known data quality issue, and with each issue removed,"
                + " to see how an IIS responds.");
        int id = 0;
        if (request.getParameter("id") != null) {
          id = Integer.parseInt(request.getParameter("id"));
        }
        if (session.getAttribute("id") != null) {
          id = (Integer) session.getAttribute("id");
        }
        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__body\">");
        out.println("    <form class=\"aira-form\" action=\"interfaceProfile\" method=\"POST\">");
        out.println("      <div class=\"aira-field\">");
        List<Connector> connectors = ConnectServlet.getConnectors(session);
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
        out.println("      <div class=\"aira-field\">");
        out.println("        <label for=\"source\">Test</label>");
        out.println("        <textarea class=\"aira-textarea smm-code\" id=\"source\" name=\"source\""
            + " rows=\"10\" wrap=\"off\"></textarea>");
        out.println("      </div>");
        out.println("      <div class=\"aira-field\">");
        out.println("        <label for=\"expected\">DQA Report Template XML</label>");
        out.println("        <textarea class=\"aira-textarea smm-code\" id=\"expected\""
            + " name=\"expected\" rows=\"10\" wrap=\"off\"></textarea>");
        out.println("      </div>");
        SmmUser user = (SmmUser) session.getAttribute("user");
        if (user.hasSendData()) {
          out.println("      <div class=\"aira-field\">");
          out.println("        <label for=\"batchSize\">Save Sample Count</label>");
          out.println("        <input class=\"aira-input smm-input-short\" id=\"batchSize\""
              + " name=\"batchSize\" type=\"text\" value=\"0\"/>");
          out.println("        <p class=\"aira-field-help\">Samples are saved to "
              + escapeHtml(String.valueOf(user.getSendData().getGeneratedDir())) + "</p>");
          out.println("      </div>");
        }
        out.println("      <div class=\"aira-form-actions\">");
        out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
            + " name=\"method\" value=\"Submit\">Submit</button>");
        out.println("      </div>");
        out.println("    </form>");
        out.println("  </div>");
        out.println("</section>");
        printHtmlFoot(out);
      } finally {
        out.close();
      }
    }
  }

  private static String safe(Node n) {
    if (n == null) {
      return "";
    }
    String s = n.getNodeValue();
    if (s == null) {
      return "";
    }
    return s;
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
