package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.HL7Reader;
import org.immregistries.smm.tester.manager.TestCaseMessageManager;
import org.immregistries.smm.tester.manager.query.QueryConverter;
import org.immregistries.smm.tester.manager.query.QueryType;
import org.immregistries.smm.tester.run.TestRunner;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author nathan
 */
public class SubmitServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

  protected static Connector getConnector(int id, HttpSession session) throws ServletException {
    List<Connector> connectors = ConnectServlet.getConnectors(session);
    id--;
    if (id < connectors.size()) {
      return connectors.get(id);
    }
    throw new IllegalArgumentException("Unable to find connection " + id);
  }

  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * 
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws Exception {

    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
    } else {
      // For example purposes, determine what method to perform based on
      // a "method" request parameter in the URL.
      int id = 0;
      if (request.getParameter("id") != null) {
        id = Integer.parseInt(request.getParameter("id"));
      }
      String str = request.getParameter("method");
      if (str == null || !str.equalsIgnoreCase("Submit") || id == 0) {
        return;
      }
      boolean debug = request.getParameter("debug") != null;
      boolean transform = request.getParameter("transform") != null
          || request.getParameter("transformSelection") != null;
      Connector connector = getConnector(id, session);
      String message = request.getParameter("message");

      if (transform) {
        if (request.getParameter("transform") != null) {
          TestCaseMessage testCaseMessage = (TestCaseMessage) session.getAttribute("testCaseMessage");
          List<String> scenarioTransforms = new ArrayList<>();
          String additionalTransformations = "";
          
          if (testCaseMessage != null) {
            // exact match lookup for scenario name
            if (connector.getScenarioTransformationsMap().containsKey(testCaseMessage.getScenario())) {
              scenarioTransforms.add(connector.getScenarioTransformationsMap().get(testCaseMessage.getScenario()));
            }
            
            // wildcard lookups for test code
            // possibly don't include the exact match result here to replicate the old functionality
            // of not including the testCaseNumber lookup if the scenario lookup returned a result
            scenarioTransforms.addAll(
              connector.getTransformsFromScenarioMap(
                testCaseMessage.getTestCaseNumber(),
                scenarioTransforms.isEmpty()));
            
            additionalTransformations = testCaseMessage.getAdditionalTransformations();
            if ("".equals(additionalTransformations)) {
              additionalTransformations = null;
            }
          }
          if (!"".equals(connector.getCustomTransformations()) || !scenarioTransforms.isEmpty()
              || additionalTransformations != null) {
            Transformer transformer = new Transformer();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            connector.setCurrentFilename("dqa-tester-request" + sdf.format(new Date()) + ".hl7");
            message = transformer.transformForTesting(connector, message,
                connector.getCustomTransformations(), scenarioTransforms, null,
                additionalTransformations, null);
          }
        } else {
          String customTranformations = "";
          try {
            BufferedReader customTransformsIn =
                new BufferedReader(new StringReader(connector.getCustomTransformations()));
            String line;
            int i = 0;
            while ((line = customTransformsIn.readLine()) != null) {
              i++;
              if (request.getParameter("transform" + i) != null) {
                customTranformations = customTranformations + line + "\n";
              }
            }
          } catch (IOException ioe) {
            // ignore
          }
          TestCaseMessage testCaseMessage =
              (TestCaseMessage) session.getAttribute("testCaseMessage");
          String scenarioTransforms = null;
          String additionalTransformations = "";
          if (testCaseMessage != null) {
            scenarioTransforms =
                connector.getScenarioTransformationsMap().get(testCaseMessage.getScenario());
            if (scenarioTransforms == null) {
              scenarioTransforms = connector.getScenarioTransformationsMap()
                  .get(testCaseMessage.getTestCaseNumber());
            }
            additionalTransformations = testCaseMessage.getAdditionalTransformations();
            if (additionalTransformations.equals("")) {
              additionalTransformations = null;
            }
          }
          if (!connector.getCustomTransformations().equals("") || scenarioTransforms != null
              || additionalTransformations != null) {
            Transformer transformer = new Transformer();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            connector.setCurrentFilename("dqa-tester-request" + sdf.format(new Date()) + ".hl7");
            message = transformer.transformForSubmitServlet(connector, message,
                customTranformations, scenarioTransforms, null, additionalTransformations);
          }
        }
      }

      message = cleanMessage(message);
      request.setAttribute("requestText", message);
      String responseText = connector.submitMessage(message, debug);
      request.setAttribute("responseText", responseText);

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
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    if (username == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
    } else {
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

      String userId = request.getParameter("userid");
      String password = request.getParameter("password");
      String facilityId = request.getParameter("facilityid");
      String message = request.getParameter("message");
      if (userId == null) {
        userId = (String) session.getAttribute("userId");
        if (userId == null) {
          userId = "";
        }
      }
      if (password == null) {
        password = (String) session.getAttribute("password");
        if (password == null) {
          password = "";
        }
      }
      if (facilityId == null) {
        facilityId = (String) session.getAttribute("facilityId");
        if (facilityId == null) {
          facilityId = "";
        }
      }
      if (message == null) {
        message = (String) session.getAttribute("message");
        if (message == null) {
          message = "";
        }
      }
      TestCaseMessage testCaseMessage = (TestCaseMessage) session.getAttribute("testCaseMessage");

      session.setAttribute("userId", userId);
      session.setAttribute("facilityId", facilityId);
      session.setAttribute("password", password);
      session.setAttribute("id", id);
      session.setAttribute("message", message);
      PrintWriter out = new PrintWriter(response.getWriter());
      response.setContentType("text/html;charset=UTF-8");
      printHtmlHead(out, MENU_HEADER_SEND, request);
      printPageHeader(out, "Send Message",
          "Send an HL7 message to an IIS and see its response, or refresh to check the connection.");
      out.println("<div class=\"aira-stack\">");
      if (connectors.isEmpty()) {
        printNoConnection(out);
        out.println("</div>");
        printHtmlFoot(out);
        out.close();
        return;
      }
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__body\">");
      printForm("send", id, connectors, message, testCaseMessage, request, out);
      out.println("  </div>");
      out.println("</section>");
      String responseText = null;
      if (id != 0) {
        try {
          Connector connector = getConnector(id, session);
          responseText = (String) request.getAttribute("responseText");
          AckAnalyzer ackAnalyzer = null;
          if (responseText != null) {
            String title = "Response Received";
            String badge = "";
            HL7Reader ackMessageReader = new HL7Reader(responseText);
            if (ackMessageReader.advanceToSegment("MSH")) {
              String messageType = ackMessageReader.getValue(9);
              if (messageType.equals("RSP") || messageType.equals("VXR")
                  || messageType.equals("VXX")) {
                title = "Query Response Received";
              } else {
                ackAnalyzer = new AckAnalyzer(responseText, connector.getAckType());
                if (ackAnalyzer.isPositive()) {
                  badge = " <span class=\"aira-badge aira-badge--success\">Accepted</span>";
                } else {
                  badge = " <span class=\"aira-badge aira-badge--danger\">Rejected</span>";
                }
              }
            }
            out.println("<section class=\"aira-panel\">");
            out.println("  <div class=\"aira-panel__header\"><h2 class=\"aira-panel__title\">"
                + title + badge + "</h2></div>");
            out.println("  <div class=\"aira-panel__body\">");
            out.println("    <pre class=\"smm-hl7\">" + escapeHtml(responseText) + "</pre>");
            out.println("  </div>");
            out.println("</section>");
          }
          String requestText = (String) request.getAttribute("requestText");
          if (requestText != null) {
            out.println("<section class=\"aira-panel\">");
            out.println("  <div class=\"aira-panel__header\">"
                + "<h2 class=\"aira-panel__title\">Request Submitted</h2></div>");
            out.println("  <div class=\"aira-panel__body\">");
            out.println("    <p>What was actually sent to "
                + escapeHtml(connector.getLabelDisplay()) + ":</p>");
            out.println("    <pre class=\"smm-hl7\">" + escapeHtml(requestText) + "</pre>");
            out.println("  </div>");
            out.println("</section>");
          }

          String host = "";
          try {
            InetAddress addr = InetAddress.getLocalHost();
            host = addr.getHostName();
          } catch (UnknownHostException e) {
            host = "[unknown]";
          }
          try {
            String status = connector.connectivityTest("Sent from client '" + host + "'");
            out.println("<div class=\"aira-alert aira-alert--info\" role=\"status\">");
            out.println("  <p class=\"aira-alert__title\">Status for "
                + escapeHtml(connector.getLabelDisplay()) + "</p>");
            out.println("  <p>" + escapeHtml(status) + "</p>");
            out.println("</div>");
          } catch (Exception t) {
            out.println("<div class=\"aira-alert aira-alert--error\" role=\"alert\">");
            out.println("  <p class=\"aira-alert__title\">Unable to test against remote server</p>");
            out.println("  <p>" + escapeHtml(t.getMessage()) + "</p>");
            out.println("  <pre class=\"smm-hl7\">" + escapeHtml(stackTrace(t)) + "</pre>");
            out.println("</div>");
          }
        } catch (Throwable re) {
          out.println("<pre class=\"smm-hl7\">" + escapeHtml(stackTrace(re)) + "</pre>");
        }
      }

      if (message != null && message.indexOf("|VXU^") > 0) {
        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">Query for This Patient</h2></div>");
        out.println("  <div class=\"aira-panel__body aira-stack aira-stack--compact\">");
        out.println("    <p class=\"aira-muted\">Each query below is built from the VXU message"
            + " above.</p>");
        Object[][] queries = {{QueryType.QBP_Z34, "QBP Z34 query"},
            {QueryType.QBP_Z34_Z44, "QBP Z34 query with Z44 request"},
            {QueryType.QBP_Z44, "QBP Z44 query"}, {QueryType.VXQ, "VXQ query"}};
        int queryCount = 0;
        for (Object[] query : queries) {
          queryCount++;
          QueryConverter queryConverter = QueryConverter.getQueryConverter((QueryType) query[0]);
          String queryMessage = queryConverter.convert(message);
          out.println("    <details class=\"smm-disclosure\">");
          out.println("      <summary>" + query[1] + "</summary>");
          printForm("query" + queryCount, id, connectors, queryMessage, testCaseMessage, request,
              out);
          out.println("    </details>");
        }
        out.println("  </div>");
        out.println("</section>");
      }

      boolean showWSDL = request.getParameter("showWSDL") != null;
      if (showWSDL && id != 0) {
        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">WSDL</h2></div>");
        out.println("  <div class=\"aira-panel__body\">");
        out.print("    <pre class=\"smm-hl7\">");
        Connector connector = getConnector(id, session);
        try {
          HttpURLConnection urlConn;
          InputStreamReader input = null;
          URL url = new URL(connector.getUrl());

          urlConn = (HttpURLConnection) url.openConnection();
          urlConn.setRequestMethod("GET");
          urlConn.setDoInput(true);
          urlConn.setUseCaches(false);

          input = new InputStreamReader(urlConn.getInputStream(), StandardCharsets.UTF_8);
          BufferedReader in = new BufferedReader(input);
          String line;
          while ((line = in.readLine()) != null) {
            out.println(escapeHtml(line));
          }
          input.close();
        } catch (IOException e) {
          out.print(escapeHtml(stackTrace(e)));
        }
        out.println("</pre>");
        out.println("  </div>");
        out.println("</section>");
      }

      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__header\">"
          + "<h2 class=\"aira-panel__title\">How to Use This Page</h2></div>");
      out.println("  <div class=\"aira-panel__body aira-prose\">");
      out.println("    <p>This page supports a simple test of the connectivity to another system. "
          + "The login parameters (username, password, and facility id) must be obtained "
          + "from the system you wish to test against. Once you have the login parameters "
          + "you should select the appropriate connection and then paste a test message. "
          + "After clicking Submit you will see the results of your query.</p>");
      out.println("    <p>If you wish to only ping the server, then you only need to select the "
          + "connection and then click Refresh. This will give the results of the ping above.</p>");
      out.println("  </div>");
      out.println("</section>");
      out.println("</div>");
      printHtmlFoot(out);
      out.close();
    }
  }

  private void printForm(String formId, int id, List<Connector> connectors, String message,
      TestCaseMessage testCaseMessage, HttpServletRequest request, PrintWriter out) {
    out.println("    <form class=\"aira-form\" action=\"SubmitServlet\" method=\"POST\">");
    out.println("      <div class=\"aira-field\">");
    if (connectors.size() == 1) {
      out.println("        <span class=\"aira-label\">Connection</span>");
      out.println("        <span>" + escapeHtml(connectors.get(0).getLabelDisplay()) + "</span>");
      out.println("        <input type=\"hidden\" name=\"id\" value=\"1\"/>");
    } else {
      out.println("        <label for=\"" + formId + "-id\">Connection</label>");
      out.println("        <select class=\"aira-select smm-select-auto\" id=\"" + formId
          + "-id\" name=\"id\">");
      out.println("          <option value=\"\">select</option>");
      int i = 0;
      for (Connector connector : connectors) {
        i++;
        out.println("          <option value=\"" + i + "\"" + (id == i ? " selected" : "") + ">"
            + escapeHtml(connector.getLabelDisplay()) + "</option>");
      }
      out.println("        </select>");
    }
    out.println("      </div>");
    out.println("      <div class=\"aira-field\">");
    out.println("        <label for=\"" + formId + "-message\">Message</label>");
    out.println("        <textarea class=\"aira-textarea smm-code\" id=\"" + formId
        + "-message\" name=\"message\" rows=\"10\" wrap=\"off\">" + escapeHtml(message)
        + "</textarea>");
    out.println("      </div>");

    out.println("      <fieldset class=\"aira-fieldset\">");
    out.println("        <legend class=\"aira-legend\">Options</legend>");
    if (connectors.size() == 1) {
      if (!connectors.get(0).getCustomTransformations().equals("")) {
        out.println("        <p class=\"aira-label\">Transforms to apply before sending</p>");
        out.println(
            "        <input type=\"hidden\" name=\"transformSelection\" value=\"yes\"/>");
        boolean shouldSelectAll = request.getParameter("transformSelection") == null;
        try {
          BufferedReader customTransformsIn =
              new BufferedReader(new StringReader(connectors.get(0).getCustomTransformations()));
          String line;

          int i = 0;
          while ((line = customTransformsIn.readLine()) != null) {
            i++;
            boolean confirmed = true;
            if (testCaseMessage != null) {
              try {
                BufferedReader etIn = new BufferedReader(
                    new StringReader(testCaseMessage.getExcludeTransformations()));
                String l;
                while ((l = etIn.readLine()) != null) {
                  if (l.equals(line)) {
                    confirmed = false;
                    break;
                  }
                }
              } catch (IOException ioe) {
                // ignore
              }
            }
            boolean selected =
                (shouldSelectAll && confirmed) || request.getParameter("transform" + i) != null;
            out.println("        <label class=\"aira-check\"><input type=\"checkbox\" name=\"transform"
                + i + "\" value=\"true\"" + (selected ? " checked" : "") + "/> <code>"
                + escapeHtml(line) + "</code></label>");
          }
        } catch (IOException ioe) {
          // ignore
        }
      }
    } else {
      out.println("        <label class=\"aira-check\"><input type=\"checkbox\" name=\"transform\""
          + " value=\"true\" checked/> Apply connection specific transforms to message before"
          + " sending</label>");
    }
    out.println("        <label class=\"aira-check\"><input type=\"checkbox\" name=\"debug\""
        + " value=\"true\"/> Debug</label>");
    out.println("        <label class=\"aira-check\"><input type=\"checkbox\" name=\"showWSDL\""
        + " value=\"true\"/> Show WSDL</label>");
    out.println("      </fieldset>");
    out.println("      <div class=\"aira-form-actions\">");
    out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
        + " name=\"method\" value=\"Submit\">Submit</button>");
    out.println("        <button class=\"aira-button aira-button--secondary\" type=\"submit\""
        + " name=\"method\" value=\"Refresh\">Refresh</button>");
    out.println("      </div>");
    out.println("    </form>");
  }

  protected void testTestCaseMessage(PrintWriter out) {
    TestCaseMessage tcm = new TestCaseMessage();
    tcm.setAssertResult("Accept");
    tcm.setComment("NAB", "Okay");
    tcm.setCustomTransformations("PID-4=HAPPY\nPID-5=SAD\n");
    tcm.setDescription("This is a description");
    tcm.setExpectedResult("This is an expected result text");
    tcm.setMessageText("MSH|\rPID|1|\rRXA|1|\rRXA|2\r");
    tcm.setOriginalMessage("MSH\rPID\rRXA\rRXR\r");
    tcm.setQuickTransformations(new String[] {"2.5.1", "BOY"});
    tcm.setTestCaseNumber("1.1");
    tcm.setTestCaseSet("CDC");
    out.print("<pre>");
    String text = tcm.createText();
    out.print(text);
    try {
      List<TestCaseMessage> tcmList = TestCaseMessageManager.createTestCaseMessageList(text);
      for (TestCaseMessage tcmIt : tcmList) {
        out.print(tcmIt.createText());
      }
    } catch (Exception e) {
      e.printStackTrace(out);
    }
    out.println("</pre>");
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
    try {
      processRequest(request, response);
    } catch (Exception e) {
      request.setAttribute("responseText", e.getMessage());
    }
    doGet(request, response);
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

  private static String cleanMessage(String message) {
    if (message != null) {
      StringBuilder sb = new StringBuilder();
      BufferedReader reader = new BufferedReader(new StringReader(message));
      String line;
      try {
        while ((line = reader.readLine()) != null) {
          sb.append(line);
          sb.append("\r");
        }
      } catch (IOException ioe) {
        sb.append(ioe.getMessage());
      }
      return sb.toString();
    }

    return message;

  }

}
