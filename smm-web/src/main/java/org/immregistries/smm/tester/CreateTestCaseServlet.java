package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.immregistries.smm.RecordServletInterface;
import org.immregistries.smm.mover.ConnectionManager;
import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.query.QueryRunner;
import org.immregistries.smm.transform.PatientType;
import org.immregistries.smm.transform.ScenarioManager;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;
import org.immregistries.smm.web.auth.SmmUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author nathan
 */
public class CreateTestCaseServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

  private static final Object[][] PATIENT_TYPE_OPTIONS = {{PatientType.ANY_CHILD, "Any Child"},
      {PatientType.ADULT, "Adult"}, {PatientType.BABY, "Baby"}, {PatientType.TODDLER, "Toddler"},
      {PatientType.TWEEN, "Tween"}, {PatientType.TWO_MONTHS_OLD, "2 Months"},
      {PatientType.TWO_YEARS_OLD, "2 Years"}, {PatientType.FOUR_YEARS_OLD, "4 Years"},
      {PatientType.TWELVE_YEARS_OLD, "12 Years"}};

  /** Quick transform check boxes, one row each: a row label (or null), then value/label pairs. */
  private static final String[][][] QUICK_TRANSFORM_ROWS = {
      {{null}, {"2.5.1", "2.5.1"}, {"2.3.1", "2.3.1"}},
      {{null}, {"BOY", "Boy"}, {"GIRL", "Girl"}, {"BOY_OR_GIRL", "Either"}},
      {{null}, {"DOB", "Date of Birth"}, {"TWIN_POSSIBLE", "Twin Possible"}},
      {{null}, {"ADDRESS", "Address"}, {"PHONE", "Phone"}},
      {{null}, {"MOTHER", "Mother"}, {"FATHER", "Father"}},
      {{null}, {"RACE", "Race"}, {"ETHNICITY", "Ethnicity"}},
      {{"Vacc #1"}, {"VAC1_ADMIN", "Admin"}, {"VAC1_HIST", "Hist"}},
      {{"Vacc #2"}, {"VAC2_ADMIN", "Admin"}, {"VAC2_HIST", "Hist"}},
      {{"Vacc #3"}, {"VAC3_ADMIN", "Admin"}, {"VAC3_HIST", "Hist"}}};

  public static final String IIS_TEST_REPORT_FILENAME_PREFIX =
      QueryRunner.IIS_TEST_REPORT_FILENAME_PREFIX;

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
      List<Connector> connectors = ConnectServlet.getConnectors(session);
      PrintWriter out = response.getWriter();
      @SuppressWarnings("unchecked")
      List<TestCaseMessage> selectedTestCaseMessageList =
          (List<TestCaseMessage>) session.getAttribute("selectedTestCaseMessageList");
      if (selectedTestCaseMessageList != null && selectedTestCaseMessageList.isEmpty()) {
        selectedTestCaseMessageList = null;
      }
      String action = request.getParameter("action");
      int testCasePos = 0;
      if (request.getParameter("testCasePos") != null) {
        testCasePos = Integer.parseInt(request.getParameter("testCasePos"));
      }
      if (action != null && (action.equals("Prev") || action.equals("Next"))) {
        if (selectedTestCaseMessageList != null && !selectedTestCaseMessageList.isEmpty()) {
          if (action.equals("Prev")) {
            testCasePos--;
          }
          if (action.equals("Next")) {
            testCasePos++;
          }
        }
      }


      String testCaseNumber;
      String testCaseSet;
      TestCaseMessage testCaseMessage = null;
      if (selectedTestCaseMessageList != null) {
        testCaseMessage = selectedTestCaseMessageList.get(testCasePos);
        testCaseNumber = testCaseMessage.getTestCaseNumber();
        testCaseSet = testCaseMessage.getTestCaseSet();
      } else {
        testCaseNumber = request.getParameter("testCaseNumber");
        testCaseSet = request.getParameter("testCaseSet");
        if (testCaseNumber != null && testCaseNumber.length() > 0) {
          if (request.getParameter("global") != null) {
            testCaseMessage =
                getTestCaseMessageMap("Global: " + testCaseSet, getTestCaseMessageMapMap(session))
                    .get(testCaseNumber);
          } else {
            testCaseMessage = getTestCaseMessageMap(testCaseSet, getTestCaseMessageMapMap(session))
                .get(testCaseNumber);
          }
        } else {
          testCaseMessage = (TestCaseMessage) session.getAttribute("testCaseMessage");
          if (testCaseMessage != null) {
            testCaseNumber = testCaseMessage.getTestCaseNumber();
          }
        }
      }
      int runTimes = 0;
      if (session.getAttribute("runTimes") != null) {
        runTimes = (Integer) session.getAttribute("runTimes");
      } else if (request.getParameter("runTimes") != null) {
        runTimes = Integer.parseInt(request.getParameter("runTimes"));
      }
      runTimes++;
      session.setAttribute("runTimes", runTimes);
      if (testCaseNumber == null || testCaseNumber.equals("")) {
        testCaseNumber = "";
      }
      {
        String scenario = request.getParameter("scenario");
        if (scenario == null) {
          if (action == null || action.equals("Update")) {
            if (request.getParameter("testCaseNumber") != null) {
              testCaseNumber = request.getParameter("testCaseNumber");
            }
            String originalMessage = request.getParameter("base");
            String customTransformations = request.getParameter("customTransforms");
            String additionalTransforms = request.getParameter("additionalTransforms");
            String description = request.getParameter("description");
            String assertResult = request.getParameter("assertResult");
            String testType = request.getParameter("messageType");
            String derivedFromTestCaseNumber = request.getParameter("derivedFromTestCaseNumber");
            if (derivedFromTestCaseNumber == null) {
              derivedFromTestCaseNumber = "";
            }
            String[] quickTransformations = request.getParameterValues("extra");
            PatientType patientType = PatientType.ANY_CHILD;
            if (request.getParameter("patientType") != null) {
              patientType = PatientType.valueOf(request.getParameter("patientType"));
            }
            if (quickTransformations == null
                && request.getParameter("settingQuickTransformations") == null) {
              if (testCaseMessage != null) {
                quickTransformations = testCaseMessage.getQuickTransformations();
              } else {
                quickTransformations = new String[] {"2.5.1", "BOY", "DOB", "ADDRESS", "PHONE",
                    "MOTHER", "VAC1_HIST", "VAC2_HIST", "VAC3_ADMIN"};
              }
            }
            if (testCaseSet == null && testCaseMessage != null) {
              testCaseSet = testCaseMessage.getTestCaseSet();
            }
            if (testCaseSet == null) {
              testCaseSet = "";
            }
            if (description == null && testCaseMessage != null) {
              description = testCaseMessage.getDescription();
            }
            if (description == null) {
              description = "";
            }
            if (originalMessage == null && testCaseMessage != null) {
              originalMessage = testCaseMessage.getOriginalMessage();
            }
            if (originalMessage == null) {
              originalMessage =
                  "MSH|\nPID|\nNK1|\nPV1|\nORC|\nRXA|\nORC|\nRXA|\nORC|\nRXA|\nOBX|\nOBX|\nOBX|\nOBX|\n";
            }
            if (customTransformations == null && testCaseMessage != null) {
              customTransformations = testCaseMessage.getCustomTransformations();
            }
            if (customTransformations == null) {
              customTransformations = "";
            }
            if (additionalTransforms == null && testCaseMessage != null) {
              additionalTransforms = testCaseMessage.getAdditionalTransformations();
            }
            if (additionalTransforms == null) {
              additionalTransforms = "";
            }
            if (assertResult == null && testCaseMessage != null) {
              assertResult = testCaseMessage.getAssertResult();
            }
            if (testType == null && testCaseMessage != null) {
              testType = testCaseMessage.getTestType();
            }
            if (assertResult == null) {
              assertResult = "Accept";
            }
            if (testType == null) {
              testType = "VXU";
            }
            if (testCaseMessage == null) {
              testCaseMessage = new TestCaseMessage();
            }
            if (!testCaseMessage.getTestCaseNumber().equals(testCaseNumber)
                || !testCaseMessage.getTestCaseSet().equals(testCaseSet)) {
              testCaseMessage = new TestCaseMessage();
              if (selectedTestCaseMessageList != null) {
                selectedTestCaseMessageList.add(testCaseMessage);
                testCasePos = selectedTestCaseMessageList.size() - 1;
              }
            }
            testCaseMessage.setTestCaseNumber(testCaseNumber);
            testCaseMessage.setTestCaseSet(testCaseSet);
            testCaseMessage.setAssertResult(assertResult);
            testCaseMessage.setTestType(testType);
            testCaseMessage.setDerivedFromTestCaseNumber(derivedFromTestCaseNumber);
            testCaseMessage.setCustomTransformations(customTransformations);
            testCaseMessage.setAdditionalTransformations(additionalTransforms);
            testCaseMessage.setDescription(description);
            testCaseMessage.setOriginalMessage(originalMessage);
            testCaseMessage.setQuickTransformations(quickTransformations);
            testCaseMessage.setPatientType(patientType);
            if (request.getParameter("testCasePos") != null) {
              testCaseMessage.setGlobal(request.getParameter("global") != null);
            }
            if (request.getParameter("excludeTransform") != null) {
              if (connectors.size() == 1) {
                if (!connectors.get(0).getCustomTransformations().equals("")) {
                  BufferedReader customTransformsIn = new BufferedReader(
                      new StringReader(connectors.get(0).getCustomTransformations()));
                  String line;
                  int i = 0;
                  StringBuilder sb = new StringBuilder();
                  while ((line = customTransformsIn.readLine()) != null) {
                    i++;
                    boolean selected = request.getParameter("excludeTransform" + i) != null;
                    if (selected) {
                      sb.append(line);
                      sb.append("\n");
                    }
                  }
                  testCaseMessage.setExcludeTransformations(sb.toString());
                }
              }
            }
          }
        } else {
          testCaseMessage = ScenarioManager.createTestCaseMessage(scenario);
          testCaseMessage.setTestCaseNumber(testCaseNumber);
        }
        session.setAttribute("baseMessage", testCaseMessage.getOriginalMessage());

        Map<String, TestCaseMessage> testCaseMessageMap = null;
        if (testCaseMessage.getTestCaseNumber().length() > 0) {
          if (testCaseMessage.isGlobal()) {
            testCaseMessageMap =
                getTestCaseMessageMap("Global: " + testCaseSet, getTestCaseMessageMapMap(session));
            testCaseMessageMap.put(testCaseNumber, testCaseMessage);
          } else {
            testCaseMessageMap =
                getTestCaseMessageMap(testCaseSet, getTestCaseMessageMapMap(session));
            testCaseMessageMap.put(testCaseNumber, testCaseMessage);
          }
        }
        Transformer transformer = new Transformer();
        testCaseMessage.setPreparedMessage(null);
        testCaseMessage.registerTestCaseMap(testCaseMessageMap);
        transformer.transform(testCaseMessage);
        session.setAttribute("message", testCaseMessage.getMessageText());
        session.setAttribute("testCaseMessge", testCaseMessage);
        if (action != null && action.equals("Update")) {
          saveTestCase(testCaseMessage, session);
        }
        // saveTestCaseHtml(testCaseMessage, session);
      }

      try {
        printHtmlHead(out, MENU_HEADER_EDIT, request);
        String position = "";
        if (selectedTestCaseMessageList != null && selectedTestCaseMessageList.size() > 1) {
          position = " <span class=\"aira-badge aira-badge--subtle\">" + (testCasePos + 1) + " of "
              + selectedTestCaseMessageList.size() + "</span>";
        }
        printPageHeader(out, "Edit Test Case" + position,
            "Build a test message from a start message and transforms. Click Update to apply"
                + " changes and see the resulting test case.");
        out.println("<form class=\"aira-stack\" action=\"CreateTestCaseServlet\" method=\"POST\">");
        out.println("  <input type=\"hidden\" name=\"runTimes\" value=\"" + runTimes + "\">");
        out.println("  <input type=\"hidden\" name=\"testCasePos\" value=\"" + testCasePos + "\">");
        makeButtons(selectedTestCaseMessageList, out, testCasePos);

        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">Test Case</h2></div>");
        out.println("  <div class=\"aira-panel__body aira-form\">");
        out.println("      <div class=\"aira-field-row\">");
        printTextField(out, "testCaseNumber", "Test Case Number",
            testCaseMessage.getTestCaseNumber());
        printTextField(out, "testCaseSet", "Test Set", testCaseMessage.getTestCaseSet());
        out.println("        <div class=\"aira-field smm-field-end\">");
        out.println("          <label class=\"aira-check\"><input type=\"checkbox\" name=\"global\""
            + " value=\"true\"" + (testCaseMessage.isGlobal() ? " checked" : "")
            + "> Global</label>");
        out.println("        </div>");
        out.println("      </div>");
        printTextField(out, "description", "Description", testCaseMessage.getDescription());
        out.println("      <div class=\"aira-field-row\">");
        out.println("        <div class=\"aira-field\">");
        out.println("          <label for=\"assertResult\">Assert Result</label>");
        out.println("          <select class=\"aira-select\" id=\"assertResult\" name=\"assertResult\">");
        out.println("            <option value=\"\">select</option>");
        for (String assertResult : new String[] {"Accept", "Accept and Warn", "Reject"}) {
          out.println("            <option value=\"" + assertResult + "\""
              + (testCaseMessage.getAssertResult().equals(assertResult) ? " selected" : "") + ">"
              + assertResult + "</option>");
        }
        for (String queryField : new String[] {RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH,
            RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH_Z32,
            RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH_Z42,
            RecordServletInterface.VALUE_RESULT_QUERY_TYPE_LIST,
            RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND,
            RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND_Z33,
            RecordServletInterface.VALUE_RESULT_QUERY_TYPE_ERROR_Z33,
            RecordServletInterface.VALUE_RESULT_QUERY_TYPE_ERROR_Z33_QUERY_REJECTED,
            RecordServletInterface.VALUE_RESULT_QUERY_TYPE_TOO_MANY,
            RecordServletInterface.VALUE_RESULT_QUERY_TYPE_ERROR,
            RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MULTIPLE_Z31_Z33,
            RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND_OR_TOO_MANY}) {
          printQueryOption(out, testCaseMessage, queryField);
        }
        out.println("          </select>");
        out.println("        </div>");
        out.println("        <div class=\"aira-field\">");
        out.println("          <label for=\"messageType\">Message Type</label>");
        out.println("          <select class=\"aira-select\" id=\"messageType\" name=\"messageType\">");
        out.println("            <option value=\"\">select</option>");
        for (String messageType : new String[] {"VXU", "QBP"}) {
          out.println("            <option value=\"" + messageType + "\""
              + (testCaseMessage.getTestType().equals(messageType) ? " selected" : "") + ">"
              + messageType + "</option>");
        }
        out.println("          </select>");
        out.println("        </div>");
        printTextField(out, "derivedFromTestCaseNumber", "Derived From",
            testCaseMessage.getDerivedFromTestCaseNumber());
        out.println("      </div>");
        if (!testCaseMessage.getActualResultAckType().equals("")) {
          out.println("      <dl class=\"smm-details\"><dt>Actual Result</dt><dd>"
              + escapeHtml(testCaseMessage.getActualResultAckType()) + "</dd></dl>");
        }
        out.println("      <div class=\"aira-field\">");
        out.println("        <label for=\"base\">Start Message</label>");
        out.println("        <textarea class=\"aira-textarea smm-code\" id=\"base\" name=\"base\""
            + " rows=\"7\" wrap=\"off\">" + escapeHtml(testCaseMessage.getOriginalMessage())
            + "</textarea>");
        out.println("      </div>");
        out.println("  </div>");
        out.println("</section>");

        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">Transforms</h2></div>");
        out.println("  <div class=\"aira-panel__body aira-grid\">");
        out.println("    <div class=\"aira-stack aira-stack--compact\">");
        out.println("      <fieldset class=\"aira-fieldset\">");
        out.println("        <legend class=\"aira-legend\">Patient Type</legend>");
        out.println("        <div class=\"smm-choice-grid\">");
        for (Object[] patientType : PATIENT_TYPE_OPTIONS) {
          out.println("          <label class=\"aira-radio\"><input type=\"radio\""
              + " name=\"patientType\" value=\"" + patientType[0] + "\""
              + isChecked((PatientType) patientType[0], testCaseMessage.getPatientType()) + "/> "
              + patientType[1] + "</label>");
        }
        out.println("        </div>");
        out.println("      </fieldset>");
        out.println("      <fieldset class=\"aira-fieldset\">");
        out.println("        <legend class=\"aira-legend\">Quick Transforms</legend>");
        out.println(
            "        <input type=\"hidden\" name=\"settingQuickTransformations\" value=\"true\">");
        for (String[][] row : QUICK_TRANSFORM_ROWS) {
          out.println("        <div class=\"aira-cluster\">");
          if (row[0][0] != null) {
            out.println("          <span class=\"aira-label\">" + row[0][0] + "</span>");
          }
          for (int i = 1; i < row.length; i++) {
            out.println("          <label class=\"aira-check\"><input type=\"checkbox\""
                + " name=\"extra\" value=\"" + row[i][0] + "\""
                + isChecked(row[i][0], testCaseMessage.getQuickTransformations()) + "> "
                + row[i][1] + "</label>");
          }
          out.println("        </div>");
        }
        out.println("      </fieldset>");
        if (connectors.size() == 1) {
          if (!connectors.get(0).getCustomTransformations().equals("")) {
            out.println("      <fieldset class=\"aira-fieldset\">");
            out.println("        <legend class=\"aira-legend\">Exclude Transforms</legend>");
            out.println(
                "        <input type=\"hidden\" name=\"excludeTransform\" value=\"true\"/>");
            try {
              BufferedReader ctIn = new BufferedReader(
                  new StringReader(connectors.get(0).getCustomTransformations()));
              String line;
              int i = 0;
              while ((line = ctIn.readLine()) != null) {
                i++;
                boolean selected = false;
                {
                  BufferedReader etIn = new BufferedReader(
                      new StringReader(testCaseMessage.getExcludeTransformations()));
                  String l;
                  while ((l = etIn.readLine()) != null) {
                    if (l.equals(line)) {
                      selected = true;
                      break;
                    }
                  }
                }
                out.println("        <label class=\"aira-check\"><input type=\"checkbox\""
                    + " name=\"excludeTransform" + i + "\" value=\"true\""
                    + (selected ? " checked" : "") + "/> <code>" + escapeHtml(line)
                    + "</code></label>");
              }
            } catch (IOException ioe) {
              // ignore
            }
            out.println("      </fieldset>");
          }
        }
        out.println("    </div>");
        out.println("    <div class=\"aira-stack aira-stack--compact\">");
        out.println("      <div class=\"aira-field\">");
        out.println("        <span class=\"aira-label\">Quick Transforms Applied</span>");
        out.println("        <pre class=\"smm-hl7 smm-scroll-short\">"
            + escapeHtml(testCaseMessage.getQuickTransformationsConverted()) + "</pre>");
        out.println("      </div>");
        out.println("      <div class=\"aira-field\">");
        out.println("        <label for=\"customTransforms\">Custom Transforms</label>");
        out.println("        <textarea class=\"aira-textarea smm-code\" id=\"customTransforms\""
            + " name=\"customTransforms\" rows=\"4\" wrap=\"off\">"
            + escapeHtml(testCaseMessage.getCustomTransformations()) + "</textarea>");
        out.println("      </div>");
        out.println("      <div class=\"aira-field\">");
        out.println("        <label for=\"additionalTransforms\">Additional Transforms</label>");
        out.println("        <textarea class=\"aira-textarea smm-code\" id=\"additionalTransforms\""
            + " name=\"additionalTransforms\" rows=\"4\" wrap=\"off\">"
            + escapeHtml(testCaseMessage.getAdditionalTransformations()) + "</textarea>");
        out.println("      </div>");
        out.println("    </div>");
        out.println("  </div>");
        out.println("</section>");

        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">Generated Test Case</h2></div>");
        out.println("  <div class=\"aira-panel__body\">");
        out.println("    <pre class=\"smm-hl7 smm-scroll-tall\">"
            + escapeHtml(testCaseMessage.createText(true)) + "</pre>");
        out.println("  </div>");
        out.println("</section>");
        makeButtons(selectedTestCaseMessageList, out, testCasePos);
        session.setAttribute("testCaseMessage", testCaseMessage);
        if (!testCaseMessage.getTestCaseNumber().equals("")) {
          if (testCaseMessage.isGlobal()) {
            getTestCaseMessageMap("Global: " + testCaseSet, getTestCaseMessageMapMap(session))
                .put(testCaseMessage.getTestCaseNumber(), testCaseMessage);
          } else {
            getTestCaseMessageMap(testCaseSet, getTestCaseMessageMapMap(session))
                .put(testCaseMessage.getTestCaseNumber(), testCaseMessage);
          }
        }
        out.println("</form>");
        out.println("<details class=\"smm-disclosure aira-prose\">");
        out.println("  <summary>How to use this page</summary>");
        out.println("  <p>This page is built to help you quickly create a valid test message. "
            + "The result of this process is an HL7 message that can be tested plus the headers "
            + "that the Data Quality Test tool uses to make a pretty test case report. Here are "
            + "the steps to creating a test case:</p>");
        out.println("   <ol>");
        out.println(
            "     <li><b>Test Case Num and Set</b> Indicate the test case number you wish to use for this test case. If you don't supply a number then one will be automatically assigned. (This assigned number will increment by 1 every time you submit this page.) The set is for grouping test cases into more manageble sections. </li>");
        out.println(
            "     <li><b>Description</b> Write a human readable description that describes what you are trying to test. </li>");
        out.println(
            "     <li><b>Expected Result</b> Write a human readable description of describes what you expect to happen when you submit this message.</li>");
        out.println(
            "     <li><b>Actual Result</b> Select what kind of result you are expecting and the exact value of either the acknowledgement text or warning text");
        out.println("       <ul>");
        out.println(
            "         <li><b>Accept</b> The message should return with a positive ACK. In the value write the exact acknowledgement text you expect.</li>");
        out.println(
            "         <li><b>Accept and Warn</b> The message should return with a positive ACK but should have a warning listed. In the value write the exact warning text you expect.</li>");
        out.println(
            "         <li><b>Reject</b> The message should return with a negative ACK (not a positive ACK). The value should be the exact negative acknolwedgment text returned.</li>");
        out.println("       </ul>");
        out.println("     </li>");
        out.println(
            "     <li><b>Start Message</b> The base message that you wish to start with. All that is required is that every segment you wish to be in the final message is listed. This process will not create new segments or reorder segments, but it can set field values. You do not have to supply a start message, the default one will work.</li>");
        out.println(
            "     <li><b>Transform</b> The transform section indicates the changes you want to make to the base message. If you are using the default base message you will have to indicate transforms in order to make a vaid HL7 message.");
        out.println("       <ul>");
        out.println(
            "         <li><b>Quick Transforms</b> This is a list of basic transforms that you will commonly do. Select each option you wish to use. Items on the same line are normaly mutually exclusive. Checking a box from each line will result in a valid HL7 message. Please remember: if you don't have the segment defined in the original message these quick transforms do not automatically create the segment!");
        out.println("           <ul>");
        out.println(
            "             <li><b>2.5.1 or 2.3.1</b> Select what kind of message you are generating and the required fields not listed below will be filled in. </li>");
        out.println(
            "             <li><b>Boy or Girl</b> Selecting boy or girl will populate the last name, first name, middle name and birth date of the patient. The last name is a random last name out of over 1000 last names derived from US county names. The first and middle name are a random selection of the top 1000 most popular baby names for either girl or boy. The gender is M for boy and F for girl. </li>");
        out.println(
            "             <li><b>Date of Birth</b> The birth date is a random date in 2009 or 2010. </li>");
        out.println(
            "             <li><b>Address</b> The street number is randomly generated number betewen 1 and 400, the street name a randomly selected county name, the street type a randomly selected value of 5 common street types. The city, state and zip code are correct for a real city in the state of Michigan.</li>");
        out.println(
            "             <li><b>Phone</b> The area code is the correct area code for the address randomly selected and represents a real area code used in Michigan. The rest of the number is in the format 555-xxxx where the last 4 digits are randomly generated.</li>");
        out.println(
            "             <li><b>Mother or Father</b> Sets the NK1 last name as the same as the patients. Sets the first name as a randomly chosen baby girl or baby boy name. Sets the NK1 type to either mother or father.</li>");
        out.println(
            "             <li><b>Vacc #1 Admin or Hist</b> Sets the first vaccination date to a random date about 2 months after the randomly selected patient's date of birth. Sets RXA-9 to indiate the whether this is administered or historical.</li>");
        out.println(
            "             <li><b>Vacc #2 Admin or Hist</b> Sets the second vaccination date to a random date about 2 months after the randomly selected patient's date of birth. Sets RXA-9 to indiate the whether this is administered or historical.</li>");
        out.println("           </ul>");
        out.println("         </li>");
        out.println(
            "         <li><b>Quick Transforms Applied</b> These are the transformed that are currently being applied. Selecting a check box does not automatically update this area. To update this area, simply click Submit. The transforms listed here may be copied to the Custom Transforms area and changed as desired. </li>");
        out.println(
            "         <li><b>Custom Transforms</b> You can indicate your own transforms by using the same format as the quick transforms. The format is this: <code>{SEG_NAME}[#{SEG_REP_NUM}]-{FIELD_NUM}[.{SUB_FIELD{NUM}]={value}</code>. The custom transforms will be run after the quick transforms. You may set any value here and it will be placed in the message exactly as you write it. It is okay to use HL7 special characters but they will not be escaped. Putting a blank value will blank out a specific field. Values such as PID#1-1 and PID-1 are equivalant, and PID-3.1 and PID-3 are also equivalant. There are also a set of defined values that you can access by placing the defined value code with bracket around it. Note: These values are generate each time a new message is generated, but stay the same while transforming the entire message, which means that if you use [BOY] a randomly choosen baby boy's name will be choosen but it will be the same throughout a transformation of a single message. The following defind value codes are supported:");
        out.println("           <ul>");
        out.println(
            "             <li><b>[BOY]</b> A randomly choosen name for a list of 1000 most common male baby names in 2010.</li>");
        out.println(
            "             <li><b>[GIRL]</b> A randomly choosen name for a list of 1000 most common female baby names in 2010.</li>");
        out.println(
            "             <li><b>[FATHER]</b> A randomly choosen name for a list of 1000 most common male baby names in 2010.</li>");
        out.println(
            "             <li><b>[MOTHER]</b> A randomly choosen name for a list of 1000 most common female baby names in 2010.</li>");
        out.println(
            "             <li><b>[MOTHER_MAIDEN]</b> A randomly choosen name for a list of over 1000 names derived from current US county names.</li>");
        out.println("             <li><b>[DOB]</b> A randomly choosen date in 2009 or 2010.</li>");
        out.println(
            "             <li><b>[NOW]</b> An HL7 formatted date and time representing the current time now.</li>");
        out.println(
            "             <li><b>[TODAY]</b> An HL7 formatted date with no time representing the current date today.</li>");
        out.println(
            "             <li><b>[LAST]</b> A randomly choosen name for a list of over 1000 names derived from current US county names.</li>");
        out.println(
            "             <li><b>[GIRL_MIDDLE]</b> A randomly choosen name for a list of 1000 most common female baby names in 2010.</li>");
        out.println(
            "             <li><b>[BOY_MIDDLE]</b> A randomly choosen name for a list of 1000 most common male baby names in 2010.</li>");
        out.println(
            "             <li><b>[GIRL_MIDDLE_INITIAL] The first initial of [GIRL_MIDDLE]</b> </li>");
        out.println(
            "             <li><b>[BOY_MIDDLE_INITIAL]</b> The first initial of [BOY_MIDDLE]</li>");
        out.println(
            "             <li><b>[VAC1_DATE]</b> A randomly choosen date about 2 months after [DOB]</li>");
        out.println(
            "             <li><b>[VAC2_DATE]</b> A randomly choosen date about 2 months after [VAC1_DATE]</li>");
        out.println(
            "             <li><b>[VAC3_DATE]</b> A randomly choosen date about 2 months after [VAC2_DATE]</li>");
        out.println(
            "             <li><b>[CITY]</b> A randomly choosen name of a city in Michigan.</li>");
        out.println(
            "             <li><b>[STREET]</b> A randomly generated street with a street number between 1 and 400 a street name from a randomly choosen US county name, and the street type from a list of 5 commmon street types.</li>");
        out.println(
            "             <li><b>[STATE]</b> The correct state for the real city picked [CITY]</li>");
        out.println(
            "             <li><b>[ZIP]</b> The correct zip for the real city picked for [CITY]</li>");
        out.println(
            "             <li><b>[PHONE]</b> A randomly generated phone, but with a area code correct for city picked for [CITY]. </li>");
        out.println("           </ul>");
        out.println("         </li>");
        out.println("       </ul>");
        out.println("     </li>");
        out.println(
            "     <li><b>Test Case</b> After hitting the submit button a test case will be generated. This can be copied and then pasted in the data quality tester to verify the test and the immunization registry. </li>");
        out.println("   </ol>");
        out.println("</details>");
        ClientServlet.printHtmlFoot(out);
      } catch (Exception e) {
        printException(out, e);
      } finally {
        out.close();
      }
    }
  }

  private void printQueryOption(PrintWriter out, TestCaseMessage testCaseMessage,
      String queryField) {
    out.println("            <option value=\"" + escapeHtml(queryField) + "\""
        + (testCaseMessage.getAssertResult().equals(queryField) ? " selected" : "") + ">Query "
        + escapeHtml(queryField) + "</option>");
  }

  protected void makeButtons(List<TestCaseMessage> selectedTestCaseMessageList, PrintWriter out,
      int testCasePos) {
    out.println("  <div class=\"aira-form-actions\">");
    out.println("    <button class=\"aira-button aira-button--primary\" type=\"submit\""
        + " name=\"action\" value=\"Update\">Update</button>");
    if (selectedTestCaseMessageList != null && selectedTestCaseMessageList.size() > 1) {
      boolean showPrev = testCasePos > 0;
      boolean showNext = (testCasePos + 1) < selectedTestCaseMessageList.size();
      out.println("    <button class=\"aira-button aira-button--secondary\" type=\"submit\""
          + " name=\"action\" value=\"Prev\"" + (showPrev ? "" : " disabled") + ">Prev</button>");
      out.println("    <button class=\"aira-button aira-button--secondary\" type=\"submit\""
          + " name=\"action\" value=\"Next\"" + (showNext ? "" : " disabled") + ">Next</button>");
    }
    out.println("  </div>");
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

  private static String isChecked(PatientType patientType1, PatientType patientType2) {
    if (patientType1 == patientType2) {
      return " checked";
    }
    return "";

  }

  private static String isChecked(String s, String[] extras) {
    if (extras != null) {
      for (String extra : extras) {
        if (s.equals(extra)) {
          return " checked";
        }
      }
    }
    return "";

  }

  protected static Map<String, TestCaseMessage> getTestCaseMessageMap(String testCaseSet,
      Map<String, Map<String, TestCaseMessage>> testMessageMapMap) {
    if (testCaseSet == null) {
      testCaseSet = "";
    }
    Map<String, TestCaseMessage> testMessageMap = testMessageMapMap.get(testCaseSet);
    if (testMessageMap == null) {
      testMessageMap = new HashMap<String, TestCaseMessage>();
      testMessageMapMap.put(testCaseSet, testMessageMap);
    }
    return testMessageMap;
  }

  protected static String getTestSetSelected(HttpSession session) {
    return (String) session.getAttribute("testSetSelected");
  }

  protected static void setTestSetSelected(String testSetSelected, HttpSession session) {
    if (testSetSelected == null || testSetSelected.equals("")) {
      session.removeAttribute("testSetSelected");
    } else {
      session.setAttribute("testSetSelected", testSetSelected);
    }
  }

  protected static Map<String, Map<String, TestCaseMessage>> getTestCaseMessageMapMap(
      HttpSession session) {
    @SuppressWarnings("unchecked")
    Map<String, Map<String, TestCaseMessage>> testMessageMapMap =
        (Map<String, Map<String, TestCaseMessage>>) session.getAttribute("testCaseMessageMapMap");
    if (testMessageMapMap == null) {
      testMessageMapMap = new HashMap<String, Map<String, TestCaseMessage>>();
      session.setAttribute("testCaseMessageMapMap", testMessageMapMap);
    }
    return testMessageMapMap;
  }

  protected static void saveTestCase(TestCaseMessage testCaseMessage, HttpSession session) {
    if (testCaseMessage.getTestCaseNumber() != null
        && !testCaseMessage.getTestCaseNumber().equals("")) {
      SmmUser user = (SmmUser) session.getAttribute("user");
      if (testCaseMessage.isGlobal()) {
        File testCaseDir = getOrCreateTestCaseDir(testCaseMessage, user);
        File testCaseFile =
            new File(testCaseDir, "TC-" + testCaseMessage.getTestCaseNumber() + ".txt");
        try {
          PrintWriter out = new PrintWriter(new FileWriter(testCaseFile, StandardCharsets.UTF_8));
          out.print(testCaseMessage.createText());
          out.close();
        } catch (IOException ioe) {
          ioe.printStackTrace();
          // unable to save, continue as normal
        }
      } else if (user != null && user.hasSendData()) {
        File testCaseDir = getOrCreateTestCaseDir(testCaseMessage, user);
        File testCaseFile =
            new File(testCaseDir, "TC-" + testCaseMessage.getTestCaseNumber() + ".txt");
        try {
          PrintWriter out = new PrintWriter(new FileWriter(testCaseFile, StandardCharsets.UTF_8));
          out.print(testCaseMessage.createText());
          out.close();
        } catch (IOException ioe) {
          ioe.printStackTrace();
          // unable to save, continue as normal
        }
      }
    }
  }

  protected static void saveTestCaseHtml(TestCaseMessage testCaseMessage, HttpSession session) {
    SmmUser user = (SmmUser) session.getAttribute("user");
    if (user != null && user.hasSendData()) {
      File testCaseDir = getOrCreateTestCaseDir(testCaseMessage, user);
      if (testCaseMessage.getTestCaseNumber() != null
          && !testCaseMessage.getTestCaseNumber().equals("")) {
        saveTestCaseHtml(testCaseMessage, testCaseDir);
      }
    }
  }

  public static void saveTestCaseHtml(TestCaseMessage testCaseMessage, File testCaseDir) {
    File testCaseFile =
        new File(testCaseDir, "TC-" + testCaseMessage.getTestCaseNumber() + ".html");
    try {
      PrintWriter out = new PrintWriter(new FileWriter(testCaseFile, StandardCharsets.UTF_8));
      String title = "Test Case Message " + testCaseMessage.getTestCaseNumber() + ": "
          + testCaseMessage.getDescription();
      ClientServlet.printHtmlHeadForFile(out, title);
      out.println("<p>[Return to <a href=\"IIS Testing Report.html\"/>IIS Test Report</a>]</p>");

      TestCaseMessageViewerServlet.printTestCaseMessage(out, testCaseMessage);
      ClientServlet.printHtmlFootForFile(out);
      out.close();
    } catch (IOException ioe) {
      ioe.printStackTrace();
      // unable to save, continue as normal
    }
  }

  public static File getOrCreateTestCaseDir(TestCaseMessage testCaseMessage,
      SmmUser user) {
    if (testCaseMessage.isGlobal() && ConnectionManager.getGlobalFolders().size() > 0) {
      File globalDir = ConnectionManager.getGlobalFolders().get(0);
      if (!globalDir.exists()) {
        throw new IllegalArgumentException(
            "Global directory does not exsist, unable to save file!");
      }
      if (!testCaseMessage.getTestCaseSet().equals("")) {
        globalDir = new File(globalDir, testCaseMessage.getTestCaseSet());
        if (!globalDir.exists()) {
          globalDir.mkdir();
        }
      }
      return globalDir;
    } else {
      File testCaseDir = user.getSendData().getTestCaseDir(true);
      if (!testCaseMessage.getTestCaseSet().equals("")) {
        testCaseDir = new File(testCaseDir, testCaseMessage.getTestCaseSet());
        if (!testCaseDir.exists()) {
          testCaseDir.mkdir();
        }
      }
      return testCaseDir;
    }
  }

  public static File getTestDataFile(SmmUser user) {
    SendData sendData = user.getSendData();
    return getTestDataFile(sendData);
  }

  public static File getTestDataFile(SendData sendData) {
    File testCaseDir = sendData.getTestDir(false);
    if (testCaseDir != null) {
      File testDataFile = new File(testCaseDir, "test-data.txt");
      if (testDataFile.exists()) {
        return testDataFile;
      }
    }
    return null;
  }

  protected static void loadTestCases(HttpSession session) throws ServletException, IOException {
    SmmUser user = (SmmUser) session.getAttribute("user");
    if (user != null && user.hasSendData()) {
      File testCaseDir = user.getSendData().getTestCaseDir(false);
      if (testCaseDir != null) {
        readTestCases(getTestCaseMessageMapMap(session), testCaseDir, null, false);
        {
          File[] dirs = testCaseDir.listFiles(new FileFilter() {
            public boolean accept(File arg0) {
              return arg0.isDirectory()
                  && !arg0.getName().startsWith(IIS_TEST_REPORT_FILENAME_PREFIX);
            }
          });
          if (dirs != null) {
            for (File dir : dirs) {
              readTestCases(getTestCaseMessageMapMap(session), dir, dir.getName(), false);
            }
          }
        }
      }

    }
    Map<String, Map<String, TestCaseMessage>> testMessageMapMap = getTestCaseMessageMapMap(session);
    setupGlobalTestCases(testMessageMapMap);

  }

  public static void setupGlobalTestCases(
      Map<String, Map<String, TestCaseMessage>> testMessageMapMap)
      throws FileNotFoundException, IOException {
    for (File globalDir : ConnectionManager.getGlobalFolders()) {
      if (globalDir.exists() && globalDir.isDirectory()) {
        readTestCases(testMessageMapMap, globalDir, null, true);
        File[] dirs = globalDir.listFiles(new FileFilter() {
          public boolean accept(File arg0) {
            return arg0.isDirectory()
                && !arg0.getName().startsWith(IIS_TEST_REPORT_FILENAME_PREFIX);
          }
        });
        if (dirs != null) {
          for (File dir : dirs) {
            readTestCases(testMessageMapMap, dir, dir.getName(), true);
          }
        }
      }

    }
  }

  public static List<File> listIISTestReports(SendData sendData) {
    List<File> fileList = new ArrayList<File>();
    {
      File[] dirs = sendData.getRootDir().listFiles(new FileFilter() {
        public boolean accept(File arg0) {
          return arg0.isDirectory() && arg0.getName().startsWith(IIS_TEST_REPORT_FILENAME_PREFIX);
        }
      });
      for (File dir : dirs) {
        fileList.add(dir);
      }
    }
    {
      File testCaseDir = sendData.getTestCaseDir(false);
      if (testCaseDir != null) {
        File[] dirs = testCaseDir.listFiles(new FileFilter() {
          public boolean accept(File arg0) {
            return arg0.isDirectory() && arg0.getName().startsWith(IIS_TEST_REPORT_FILENAME_PREFIX);
          }
        });
        for (File dir : dirs) {
          fileList.add(dir);
        }
      }
    }

    Collections.sort(fileList);
    return fileList;
  }

  public static void readTestCases(Map<String, Map<String, TestCaseMessage>> testMessageMapMap,
      File testCaseDir, String testCaseSet, boolean global)
      throws FileNotFoundException, IOException {
    String[] filenames = testCaseDir.list(new FilenameFilter() {
      public boolean accept(File file, String arg1) {
        return arg1.startsWith("TC-") && arg1.endsWith(".txt");
      }
    });
    if (global) {
      testCaseSet = "Global: " + testCaseSet;
    }
    if (filenames != null) {
      for (String filename : filenames) {
        BufferedReader in = new BufferedReader(new FileReader(new File(testCaseDir, filename), StandardCharsets.UTF_8));
        String line;
        StringBuilder testScript = new StringBuilder();
        while ((line = in.readLine()) != null) {
          testScript.append(line);
          testScript.append("\n");
        }
        in.close();

        List<TestCaseMessage> testCaseMessageList =
            TestCaseServlet.parseAndAddTestCases(testScript.toString());
        for (TestCaseMessage testCaseMessage : testCaseMessageList) {
          if (!testCaseMessage.getTestCaseNumber().equals("")) {
            testCaseMessage.setGlobal(global);
            CreateTestCaseServlet.getTestCaseMessageMap(testCaseSet, testMessageMapMap)
                .put(testCaseMessage.getTestCaseNumber(), testCaseMessage);
          }
        }
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
    return "Short description";

  }// </editor-fold>
}
