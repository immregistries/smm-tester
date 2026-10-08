package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
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
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.TestCaseMessageManager;
import org.immregistries.smm.tester.run.TestRunner;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;
import org.immregistries.smm.web.auth.SmmUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;

/**
 * @author nathan
 */
public class StressTestServlet extends ClientServlet {
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
    String username = (String) session.getAttribute("username");
    SmmUser user = (SmmUser) session.getAttribute("user");
    String action = request.getParameter("action");
    String problem = null;
    List<StressRunner> stressRunnerList = getStressRunnerList(session);
    if (username == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
    } else if (action.equals("Start")) {

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
      }

      String testCase = request.getParameter("source");
      if (testCase == null || testCase.trim().length() == 0) {
        testCase = null;
      }

      List<TestCaseMessage> testCaseMessageList = null;
      if (testCase == null || testCase.equals("")) {
        goodToQuery = false;
        problem = "Test case message not set, unable to run test. ";
      } else {
        testCaseMessageList = parseAndAddTestCases(testCase, session);
      }

      TestRunner testRunner = new TestRunner();
      TestCaseMessage testCaseMessageBase = null;
      if (goodToQuery && testCaseMessageList.size() > 0) {
        testCaseMessageBase = testCaseMessageList.get(0);
        if (connector != null) {
          try {
            testRunner.runTest(connector, testCaseMessageBase);
          } catch (Exception e) {
            problem = "Unable to run test: " + e.getMessage();
          }
        }
      } else if (testCaseMessageList.size() == 0) {
        goodToQuery = false;
        problem = "Unable to profile interface, Test Case Message size == 0 ";
      }

      if (testCaseMessageBase != null) {
        String saveDirName = request.getParameter("saveDirName");
        boolean saveTestMessages = request.getParameter("saveTestMessages") != null;
        boolean saveLogs = request.getParameter("saveLogs") != null;

        StressRunner stressRunner = new StressRunner();
        stressRunner.setUser(user);
        stressRunner.setTestCaseMessage(testCaseMessageBase);
        stressRunner.setConnector(connector);
        stressRunner.setPos(stressRunnerList.size());
        stressRunner.setSaveDirName(saveDirName);
        stressRunner.setSaveTestMessages(saveTestMessages);
        stressRunner.setSaveLogs(saveLogs);
        stressRunnerList.add(stressRunner);
        stressRunner.start();
      }

    } else if (action.equals("Stop")) {
      int pos = Integer.parseInt(request.getParameter("pos"));
      StressRunner stressRunner = stressRunnerList.get(pos);
      stressRunner.setKeepRunning(false);
    }
    doGet(request, response, session, problem);
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
      doGet(request, response, session, null);
    }
  }

  private void doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session,
      String problem) throws IOException {
    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_HOME, request);
      printPageHeader(out, "Stress Test",
          "Send a test message to an IIS repeatedly and watch the throughput and results.");

      if (problem != null) {
        out.println("<div class=\"aira-alert aira-alert--warning\" role=\"status\"><p>"
            + escapeHtml(problem) + "</p></div>");
      }
      out.println("<div class=\"aira-stack\">");

      List<StressRunner> stressRunnerList = getStressRunnerList(session);
      if (stressRunnerList.size() > 0) {
        out.println("<section class=\"aira-table-panel\">");
        out.println("  <div class=\"aira-table-panel__header\"><div>"
            + "<h2 class=\"aira-table-panel__title\">Stress Test Status</h2></div></div>");
        out.println("  <div class=\"aira-table-wrap\">");
        out.println("    <table class=\"aira-table\">");
        out.println("      <caption class=\"aira-visually-hidden\">Stress test status</caption>");
        out.println("      <thead><tr><th scope=\"col\" class=\"aira-table__cell--numeric\">Pos</th>"
            + "<th scope=\"col\">Status</th>"
            + "<th scope=\"col\" class=\"aira-table__cell--numeric\">Sent</th>"
            + "<th scope=\"col\">Expected Result</th>"
            + "<th scope=\"col\" class=\"aira-table__cell--numeric\">Passed</th>"
            + "<th scope=\"col\" class=\"aira-table__cell--numeric\">Failed</th>"
            + "<th scope=\"col\" class=\"aira-table__cell--numeric\">Exception</th>"
            + "<th scope=\"col\" class=\"aira-table__cell--numeric\">Rate (ms)</th>"
            + "<th scope=\"col\" class=\"aira-table__cell--actions\">"
            + "<span class=\"aira-visually-hidden\">Actions</span></th></tr></thead>");
        out.println("      <tbody>");
        int pos = 0;
        for (StressRunner stressRunner : stressRunnerList) {
          out.println("        <tr>");
          out.println("          <td class=\"aira-table__cell--numeric\">" + (pos + 1) + "</td>");
          out.println("          <td>" + escapeHtml(stressRunner.getStatus()) + "</td>");
          out.println("          <td class=\"aira-table__cell--numeric\">" + stressRunner.getCount()
              + "</td>");
          out.println("          <td>"
              + escapeHtml(stressRunner.getTestCaseMessage().getAssertResult()) + "</td>");
          out.println("          <td class=\"aira-table__cell--numeric\">"
              + stressRunner.getCountAccepted() + "</td>");
          out.println("          <td class=\"aira-table__cell--numeric\">"
              + stressRunner.getCountErrored() + "</td>");
          out.println("          <td class=\"aira-table__cell--numeric\">"
              + stressRunner.getCountException() + "</td>");
          out.println("          <td class=\"aira-table__cell--numeric\">"
              + ((int) stressRunner.getRate()) + "</td>");
          out.println("          <td class=\"aira-table__cell--actions\">");
          out.println("            <form class=\"aira-inline-form\" action=\"StressTestServlet\""
              + " method=\"POST\">");
          out.println("              <input type=\"hidden\" name=\"pos\" value=\"" + pos + "\">");
          out.println("              <button class=\"aira-button aira-button--small"
              + " aira-button--danger\" type=\"submit\" name=\"action\" value=\"Stop\">Stop</button>");
          out.println("              <button class=\"aira-button aira-button--small"
              + " aira-button--secondary\" type=\"submit\" name=\"action\" value=\"Refresh\">"
              + "Refresh</button>");
          out.println("            </form>");
          out.println("          </td>");
          out.println("        </tr>");
          pos++;
        }
        out.println("      </tbody>");
        out.println("    </table>");
        out.println("  </div>");
        out.println("</section>");
      }

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
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__header\">"
          + "<h2 class=\"aira-panel__title\">Start Stress Test</h2></div>");
      out.println("  <div class=\"aira-panel__body\">");
      out.println("    <form class=\"aira-form\" action=\"StressTestServlet\" method=\"POST\">");
      out.println("      <div class=\"aira-field\">");
      List<Connector> connectors = ConnectServlet.getConnectors(session);
      if (connectors.size() == 1) {
        out.println("        <span class=\"aira-label\">Connection</span>");
        out.println("        <span>" + escapeHtml(connectors.get(0).getLabelDisplay()) + "</span>");
        out.println("        <input type=\"hidden\" name=\"id\" value=\"1\"/>");
      } else {
        out.println("        <label for=\"id\">Connection</label>");
        out.println("        <select class=\"aira-select smm-select-auto\" id=\"id\" name=\"id\">");
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
      out.println("        <label for=\"source\">Test</label>");
      out.println("        <textarea class=\"aira-textarea smm-code\" id=\"source\" name=\"source\""
          + " rows=\"10\" wrap=\"off\">" + escapeHtml(testCaseMessage.createText())
          + "</textarea>");
      out.println("      </div>");
      String saveDirName = "";
      boolean saveTestMessages = true;
      boolean saveLogs = true;

      if (stressRunnerList.size() > 0) {
        StressRunner stressRunner = stressRunnerList.get(stressRunnerList.size() - 1);
        saveDirName = stressRunner.getSaveDirName();
        saveTestMessages = stressRunner.isSaveTestMessages();
        saveLogs = stressRunner.isSaveLogs();
      }
      out.println("      <fieldset class=\"aira-fieldset\">");
      out.println("        <legend class=\"aira-legend\">Log</legend>");
      printTextField(out, "saveDirName", "Directory", saveDirName);
      out.println("        <label class=\"aira-check\"><input type=\"checkbox\""
          + " name=\"saveTestMessages\" value=\"true\"" + (saveTestMessages ? " checked" : "")
          + "/> Save Messages</label>");
      out.println("        <label class=\"aira-check\"><input type=\"checkbox\" name=\"saveLogs\""
          + " value=\"true\"" + (saveLogs ? " checked" : "") + "/> Save Logs</label>");
      out.println("      </fieldset>");
      out.println("      <div class=\"aira-form-actions\">");
      out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
          + " name=\"action\" value=\"Start\">Start</button>");
      out.println("      </div>");
      out.println("    </form>");
      out.println("  </div>");
      out.println("</section>");
      if (stressRunnerList.size() > 0) {
        Map<String, StressTestServlet.MinuteStat> msm;
        synchronized (minuteStatMap) {
          msm = new HashMap<>(minuteStatMap);
        }
        List<String> minuteList = new ArrayList<>();
        minuteList = new ArrayList<>(msm.keySet());
        Collections.sort(minuteList);
        out.println("<section class=\"aira-table-panel\">");
        out.println("  <div class=\"aira-table-panel__header\"><div>"
            + "<h2 class=\"aira-table-panel__title\">Stress Test Details</h2></div></div>");
        out.println("  <div class=\"aira-table-wrap\">");
        out.println("    <table class=\"aira-table\">");
        out.println("      <caption class=\"aira-visually-hidden\">Messages by minute</caption>");
        out.println("      <thead><tr><th scope=\"col\">Minute</th>"
            + "<th scope=\"col\" class=\"aira-table__cell--numeric\">Sent</th>"
            + "<th scope=\"col\" class=\"aira-table__cell--numeric\">Passed</th>"
            + "<th scope=\"col\" class=\"aira-table__cell--numeric\">Failed</th>"
            + "<th scope=\"col\" class=\"aira-table__cell--numeric\">Exception</th>"
            + "<th scope=\"col\" class=\"aira-table__cell--numeric\">Running Average (ms)</th>"
            + "</tr></thead>");
        out.println("      <tbody>");
        for (String key : minuteList) {
          MinuteStat minuteStat = msm.get(key);
          out.println("        <tr>");
          out.println("          <td class=\"aira-table__cell--date\">" + escapeHtml(key) + "</td>");
          out.println("          <td class=\"aira-table__cell--numeric\">" + minuteStat.messageCount
              + "</td>");
          out.println("          <td class=\"aira-table__cell--numeric\">"
              + minuteStat.messagePassed + "</td>");
          out.println("          <td class=\"aira-table__cell--numeric\">"
              + minuteStat.messageNotPassed + "</td>");
          out.println("          <td class=\"aira-table__cell--numeric\">"
              + minuteStat.messageException + "</td>");
          if ((minuteStat.messagePassed + minuteStat.messageNotPassed) == 0) {
            out.println("          <td class=\"aira-table__cell--numeric\">-</td>");
          } else {
            out.println("          <td class=\"aira-table__cell--numeric\">" + (minuteStat.elapsedTime
                / (minuteStat.messagePassed + minuteStat.messageNotPassed)) + "</td>");
          }
          out.println("        </tr>");
        }
        out.println("      </tbody>");
        out.println("    </table>");
        out.println("  </div>");
        out.println("</section>");
        int pos = 0;
        for (StressRunner stressRunner : stressRunnerList) {
          out.println("<details class=\"smm-disclosure\">");
          out.println("  <summary>Tester " + (pos + 1) + "</summary>");
          out.println("  <span class=\"aira-label\">Last returned acknowledgement</span>");
          out.println("  <pre class=\"smm-hl7\">" + escapeHtml(stressRunner.getLastResult())
              + "</pre>");
          out.println("  <span class=\"aira-label\">Log</span>");
          out.println("  <pre class=\"smm-hl7 smm-scroll-tall\">"
              + escapeHtml(String.valueOf(stressRunner.getLog())) + "</pre>");
          out.println("</details>");
          pos++;
        }
      }
      out.println("</div>");
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  private List<StressRunner> getStressRunnerList(HttpSession session) {
    @SuppressWarnings("unchecked")
    List<StressRunner> stressRunnerList =
        (List<StressRunner>) session.getAttribute("stressRunnerList");
    if (stressRunnerList == null) {
      stressRunnerList = new ArrayList<StressTestServlet.StressRunner>();
      session.setAttribute("stressRunnerList", stressRunnerList);
    }
    return stressRunnerList;
  }

  @SuppressWarnings("serial")
  public class StressRunnerList extends ArrayList<StressRunner>
      implements HttpSessionBindingListener {

    public void valueBound(HttpSessionBindingEvent event) {
      // TODO Auto-generated method stub

    }

    public void valueUnbound(HttpSessionBindingEvent event) {
      for (StressRunner stressRunner : this) {
        stressRunner.setKeepRunning(false);
      }
    }

  }

  public static class MinuteStat {
    private String minute = "";
    private int messageCount = 0;
    private int messagePassed = 0;
    private int messageNotPassed = 0;
    private int messageException = 0;
    private long elapsedTime = 0;

    public MinuteStat(String minute) {
      this.minute = minute;
    }
  }

  private static Map<String, StressTestServlet.MinuteStat> minuteStatMap =
      new HashMap<String, StressTestServlet.MinuteStat>();

  private static MinuteStat getMinuteStat() {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    Date date = new Date();
    String key = sdf.format(date);
    MinuteStat minuteStat;
    synchronized (minuteStatMap) {
      minuteStat = minuteStatMap.get(key);
      if (minuteStat == null) {
        minuteStat = new MinuteStat(key);
        minuteStatMap.put(key, minuteStat);
      }
    }
    return minuteStat;
  }

  public class StressRunner extends Thread {
    private Transformer transformer = null;
    private TestRunner testRunner = null;
    private long pause = 0;
    private TestCaseMessage testCaseMessageBase = null;
    private Connector connector = null;
    private String status = "";
    private double rate = 0.0;
    private long startTime = 0;
    private long markTime = 0;
    private int count = 0;
    private int countAccepted = 0;
    private String lastResult = "";
    private StringBuilder log = new StringBuilder();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z");
    private SmmUser user = null;
    private int pos = 0;
    private String saveDirName = null;
    private boolean saveTestMessages = false;
    private boolean saveLogs = false;

    public String getSaveDirName() {
      return saveDirName;
    }

    public void setSaveDirName(String saveDirName) {
      this.saveDirName = saveDirName;
    }

    public boolean isSaveTestMessages() {
      return saveTestMessages;
    }

    public void setSaveTestMessages(boolean saveTestMessages) {
      this.saveTestMessages = saveTestMessages;
    }

    public boolean isSaveLogs() {
      return saveLogs;
    }

    public void setSaveLogs(boolean saveLogs) {
      this.saveLogs = saveLogs;
    }

    public void setPos(int pos) {
      this.pos = pos;
    }

    public void setUser(SmmUser user) {
      this.user = user;
    }

    private void log(String message) {
      String zeroPad = "00000" + count;
      zeroPad = zeroPad.substring(zeroPad.length() - 6);
      if (fileOutLog == null) {
        log.append(sdf.format(new Date()));
        log.append(" ");
        log.append(zeroPad);
        log.append(" ");
        log.append(message);
        log.append("\n");
      } else {
        fileOutLog.println(sdf.format(new Date()) + " " + zeroPad + " " + message);
        log.append("\n");
      }
    }

    public String getLog() {
      return log.toString();
    }

    public String getLastResult() {
      return lastResult;
    }

    public int getCountAccepted() {
      return countAccepted;
    }

    public int getCountException() {
      return countException;
    }

    public int getCountErrored() {
      return countErrored;
    }

    private int countException = 0;
    private int countErrored = 0;

    public long getMarkTime() {
      return markTime;
    }

    public void setMarkTime(long markTime) {
      this.markTime = markTime;
    }

    public int getCount() {
      return count;
    }

    public void setCount(int count) {
      this.count = count;
    }

    public double getRate() {
      return rate;
    }

    public void setRate(double rate) {
      this.rate = rate;
    }

    public long getStartTime() {
      return startTime;
    }

    public void setStartTime(long startTime) {
      this.startTime = startTime;
    }

    public boolean isKeepRunning() {
      return keepRunning;
    }

    public void setKeepRunning(boolean keepRunning) {
      this.keepRunning = keepRunning;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    private boolean keepRunning = true;

    public long getPause() {
      return pause;
    }

    public void setPause(long pause) {
      this.pause = pause;
    }

    public TestCaseMessage getTestCaseMessage() {
      return testCaseMessageBase;
    }

    public void setTestCaseMessage(TestCaseMessage testCaseMessage) {
      this.testCaseMessageBase = testCaseMessage;
    }

    public Connector getConnector() {
      return connector;
    }

    public void setConnector(Connector connector) {
      this.connector = connector;
    }

    public StressRunner() {
      transformer = new Transformer();
      testRunner = new TestRunner();
    }

    @Override
    public void run() {
      log("Starting test runner");
      try {
        try {
          File generatedDir = null;
          if (saveDirName != null && !saveDirName.equals("")) {
            generatedDir = new File(saveDirName);
            if (!generatedDir.exists()) {
              log("Unrecognized dir: " + saveDirName);
              generatedDir = null;
            }
          }

          if (generatedDir != null) {
            String filenameBase =
                (testCaseMessageBase == null ? "TCM" : testCaseMessageBase.getTestCaseNumber());
            {
              File file =
                  new File(generatedDir, filenameBase + "-" + (pos + 1) + " Stress Messages.txt");
              log("Saving example message to: " + file.getCanonicalPath());
              fileOutExampleMessage = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8));
            }
            {
              File file = new File(generatedDir, filenameBase + "-" + (pos + 1) + " Log.txt");
              log("Saving logs to: " + file.getCanonicalPath());
              fileOutLog = new PrintWriter(new FileWriter(file, StandardCharsets.UTF_8));
            }
          }
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }

        startTime = System.currentTimeMillis();
        status = "RUNNING";
        while (keepRunning) {
          count++;
          MinuteStat minuteStat = getMinuteStat();
          minuteStat.messageCount++;
          log("Running test count " + count);
          try {
            markTime = System.currentTimeMillis();
            TestCaseMessage testCaseMessage = new TestCaseMessage(testCaseMessageBase);
            transformer.transform(testCaseMessage);
            log("Submiting test message");
            testRunner.runTest(connector, testCaseMessage);
            printToFile(testCaseMessage);
            lastResult = testRunner.getAckMessageText();
            if (testRunner.isPassedTest()) {
              log("Test passed");
              countAccepted++;
              minuteStat.messagePassed++;
            } else {
              log("Test NOT passed");
              countErrored++;
              minuteStat.messageNotPassed++;
            }
            long timeToQuery = System.currentTimeMillis() - markTime;
            minuteStat.elapsedTime += timeToQuery;
            log("Time to query = " + timeToQuery + "ms");
            rate = rate * 0.75 + timeToQuery * 0.25;
          } catch (Exception e) {
            log("Exception ocurred: " + e.getMessage());
            countException++;
            minuteStat.messageException++;
            e.printStackTrace();
            if (countException > 10) {
              keepRunning = false;
            }
          }
          startTime = System.currentTimeMillis();
        }
        if (fileOutExampleMessage != null) {
          fileOutExampleMessage.close();
        }
        if (fileOutLog != null) {
          fileOutLog.close();
        }
      } finally {
        status = "STOPPED";
        log("Test runner stopped");
      }
    }

    private PrintWriter fileOutExampleMessage = null;
    private PrintWriter fileOutLog = null;

    private void printToFile(TestCaseMessage testCaseMessage) {
      if (fileOutExampleMessage != null) {
        try {
          fileOutExampleMessage.print(testCaseMessage.getMessageText());
        } catch (Exception e) {
          e.printStackTrace();
          fileOutExampleMessage = null;
        }

      }
    }
  }
}
