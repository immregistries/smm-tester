package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.HL7Reader;
import org.immregistries.smm.tester.manager.query.QueryConverterQBPZ45;
import org.immregistries.smm.transform.PatientType;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * RB demo flow: pick a scenario, a made-up patient (names, address, phone and so on come from the
 * existing test message generator) is generated with a plausible history and a prefilled VXU. Send
 * the VXU to the IIS to establish the patient, then send the risk-based query (QBP Z45) built from
 * that same VXU, with the scenario's contextual conditions, to get the Z42 forecast.
 */
public class RbDemoServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

  public static final String PARAM_SCENARIO = "scenario";
  public static final String PARAM_ACTION = "action";
  public static final String PARAM_VXU = "vxu";
  public static final String PARAM_CONDITIONS = "conditions";
  public static final String PARAM_ID = "id";
  public static final String ACTION_SEND_VXU = "Send VXU";
  public static final String ACTION_SEND_Z45 = "Send Z45";
  public static final String SESSION_STATE = "rbDemoState";

  private static final Random RANDOM = new Random();

  /** Scenario: key, button label, description. */
  private static final String[][] SCENARIOS = {
      {"pregnancy-tdap", "Pregnancy / Tdap",
          "Pregnant woman of childbearing age, about 30 weeks along: Tdap window is open."},
      {"rsv-infant", "RSV infant",
          "Newborn whose mother received an RSV vaccine in pregnancy: nirsevimab not needed."},
      {"healthcare-worker", "Healthcare worker",
          "Adult healthcare personnel: HepB (high risk) and influenza (routine)."},
      {"unknown-code", "Unknown code (ERR)",
          "Healthcare worker plus a condition code the IIS does not know: warning ERR, no reject."}};

  /** State of the demo for one browser session. */
  public static class DemoState implements Serializable {
    private static final long serialVersionUID = 1L;
    String scenario = "";
    String summary = "";
    String vxu = "";
    String conditions = "";
    String vxuRequest;
    String vxuResponse;
    String z45Request;
    String z45Response;
    int connectorId = 0;
  }

  // ------------------------------------------------------------------------------------------
  // generating the patient
  // ------------------------------------------------------------------------------------------

  private static String ymd(Calendar c) {
    return new SimpleDateFormat("yyyyMMdd").format(c.getTime());
  }

  private static Calendar daysAgo(int days) {
    Calendar c = Calendar.getInstance();
    c.add(Calendar.DAY_OF_MONTH, -days);
    return c;
  }

  private static int between(int low, int high) {
    return low + RANDOM.nextInt(high - low + 1);
  }

  /** One historical vaccination: CVX, label, date (yyyyMMdd). */
  private static String[] shot(String cvx, String label, Calendar date) {
    return new String[] {cvx, label, ymd(date)};
  }

  /**
   * Generates a made-up patient for the scenario into the state: a VXU built with the existing
   * Transformer (names, address, phone, race and ethnicity, mother) and the suggested conditions.
   */
  static void generate(String key, DemoState state) {
    Calendar dob;
    String sexQuick;
    boolean child = false;
    List<String[]> shots = new ArrayList<String[]>();
    String conditions;
    String what;
    Calendar now = Calendar.getInstance();
    if (key.equals("pregnancy-tdap")) {
      dob = daysAgo(365 * between(21, 36) + between(0, 364));
      sexQuick = "GIRL";
      Calendar mmr1 = (Calendar) dob.clone();
      mmr1.add(Calendar.DAY_OF_MONTH, 400);
      Calendar mmr2 = (Calendar) dob.clone();
      mmr2.add(Calendar.DAY_OF_MONTH, 365 * 5);
      shots.add(shot("03", "MMR", mmr1));
      shots.add(shot("03", "MMR", mmr2));
      shots.add(shot("115", "Tdap", daysAgo(365 * between(7, 11))));
      conditions = "007=Y,170=" + ymd(daysAgo(between(196, 224)));
      what = "Adult woman, pregnant (onset about 30 weeks ago)";
    } else if (key.equals("rsv-infant")) {
      int ageDays = between(14, 70);
      dob = daysAgo(ageDays);
      sexQuick = "BOY_OR_GIRL";
      child = true;
      shots.add(shot("08", "HepB, pediatric", dob));
      conditions = "278=Y";
      what = "Infant " + ageDays + " days old, mother vaccinated for RSV in pregnancy";
    } else {
      dob = daysAgo(365 * between(26, 58) + between(0, 364));
      sexQuick = "BOY_OR_GIRL";
      Calendar mmr1 = (Calendar) dob.clone();
      mmr1.add(Calendar.DAY_OF_MONTH, 400);
      Calendar mmr2 = (Calendar) dob.clone();
      mmr2.add(Calendar.DAY_OF_MONTH, 365 * 5);
      shots.add(shot("03", "MMR", mmr1));
      shots.add(shot("03", "MMR", mmr2));
      shots.add(shot("115", "Tdap", daysAgo(365 * between(3, 8))));
      shots.add(shot("88", "Influenza, unspecified formulation", daysAgo(between(380, 420))));
      conditions = key.equals("unknown-code") ? "055=Y,999=Y" : "055=Y";
      what = "Adult healthcare worker";
    }

    String mrn = "RB" + new SimpleDateFormat("yyMMdd").format(now.getTime()) + between(100, 999);
    String stamp = new SimpleDateFormat("yyyyMMddHHmmssZ").format(now.getTime());
    StringBuilder msg = new StringBuilder();
    msg.append("MSH|^~\\&|RBDEMO|RBDEMO-CLINIC|IIS|IIS|").append(stamp).append("||VXU^V04^VXU_V04|")
        .append("RBDEMO-").append(System.currentTimeMillis()).append("|P|2.5.1|||ER|AL|||||")
        .append("Z22^CDCPHINVS\n");
    msg.append("PID|1||").append(mrn).append("^^^RBDEMO^MR||||").append(ymd(dob)).append("|\n");
    if (child) {
      msg.append("NK1|1||\n");
    }
    int n = 0;
    for (String[] s : shots) {
      n++;
      msg.append("ORC|RE||").append(mrn).append(".").append(n).append("^RBDEMO\n");
      msg.append("RXA|0|1|").append(s[2]).append("||").append(s[0]).append("^").append(s[1])
          .append("^CVX|999|||01^Historical information - source unspecified^NIP001||||||||||CP|A\n");
    }
    TestCaseMessage tcm = new TestCaseMessage();
    tcm.setScenario("RB demo " + key);
    tcm.setOriginalMessage(msg.toString());
    List<String> quick = new ArrayList<String>();
    quick.add(sexQuick);
    quick.add("ADDRESS");
    quick.add("PHONE");
    quick.add("RACE");
    quick.add("ETHNICITY");
    if (child) {
      quick.add("MOTHER");
    }
    tcm.setQuickTransformations(quick.toArray(new String[0]));
    tcm.setPatientType(child ? PatientType.BABY : PatientType.ADULT);
    tcm.setCustomTransformations("");
    new Transformer().transform(tcm);
    String text = tcm.getMessageText();

    state.scenario = key;
    state.vxu = text;
    state.conditions = conditions;
    state.vxuRequest = null;
    state.vxuResponse = null;
    state.z45Request = null;
    state.z45Response = null;
    HL7Reader r = new HL7Reader(text);
    String name = "";
    String sex = "";
    if (r.advanceToSegment("PID")) {
      name = r.getValue(5, 2) + " " + r.getValue(5, 1);
      sex = r.getValue(8);
    }
    StringBuilder hist = new StringBuilder();
    for (String[] s : shots) {
      if (hist.length() > 0) {
        hist.append(", ");
      }
      hist.append(s[1]).append(" ").append(s[2]);
    }
    state.summary = what + ": " + name + " (" + sex + "), born " + ymd(dob) + ", MRN " + mrn
        + ". History: " + hist + ".";
  }

  // ------------------------------------------------------------------------------------------
  // sending
  // ------------------------------------------------------------------------------------------

  private static String cleanMessage(String message) {
    if (message == null) {
      return "";
    }
    String m = message.replace("\r\n", "\n").replace('\r', '\n').trim();
    return m.replace("\n", "\r") + "\r";
  }

  /** The Z45 for the current VXU and conditions, or null if there is no VXU. */
  static String buildZ45(DemoState state) {
    if (state.vxu == null || state.vxu.trim().length() == 0) {
      return null;
    }
    QueryConverterQBPZ45 converter = new QueryConverterQBPZ45(state.conditions);
    return converter.convert(cleanMessage(state.vxu));
  }

  private static String send(Connector connector, String message) {
    try {
      return connector.submitMessage(message, false);
    } catch (Exception e) {
      return "Unable to send: " + e.getMessage();
    }
  }

  // ------------------------------------------------------------------------------------------
  // servlet
  // ------------------------------------------------------------------------------------------

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(true);
    if (session.getAttribute("username") == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
      return;
    }
    DemoState state = getState(session);
    String scenario = request.getParameter(PARAM_SCENARIO);
    if (scenario != null) {
      generate(scenario, state);
    }
    render(request, response, session, state);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    HttpSession session = request.getSession(true);
    if (session.getAttribute("username") == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
      return;
    }
    DemoState state = getState(session);
    String vxu = request.getParameter(PARAM_VXU);
    if (vxu != null) {
      state.vxu = vxu;
    }
    String conditions = request.getParameter(PARAM_CONDITIONS);
    if (conditions != null) {
      state.conditions = conditions.trim();
    }
    String id = request.getParameter(PARAM_ID);
    if (id != null && id.length() > 0) {
      try {
        state.connectorId = Integer.parseInt(id);
      } catch (NumberFormatException nfe) {
        // ignore
      }
    }
    String scenario = request.getParameter(PARAM_SCENARIO);
    String action = request.getParameter(PARAM_ACTION);
    List<Connector> connectors = ConnectServlet.getConnectors(session);
    if (state.connectorId < 1 || state.connectorId > connectors.size()) {
      state.connectorId = connectors.isEmpty() ? 0 : 1;
    }
    if (scenario != null) {
      generate(scenario, state);
    } else if (ACTION_SEND_VXU.equals(action) && state.connectorId > 0) {
      state.vxuRequest = cleanMessage(state.vxu);
      state.vxuResponse = send(connectors.get(state.connectorId - 1), state.vxuRequest);
      state.z45Request = null;
      state.z45Response = null;
    } else if (ACTION_SEND_Z45.equals(action) && state.connectorId > 0) {
      state.z45Request = buildZ45(state);
      if (state.z45Request != null) {
        state.z45Response = send(connectors.get(state.connectorId - 1), state.z45Request);
      }
    }
    render(request, response, session, state);
  }

  private static DemoState getState(HttpSession session) {
    DemoState state = (DemoState) session.getAttribute(SESSION_STATE);
    if (state == null) {
      state = new DemoState();
      session.setAttribute(SESSION_STATE, state);
    }
    return state;
  }

  private void render(HttpServletRequest request, HttpServletResponse response, HttpSession session,
      DemoState state) throws IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    try {
      List<Connector> connectors = ConnectServlet.getConnectors(session);
      if (state.connectorId < 1 || state.connectorId > connectors.size()) {
        state.connectorId = connectors.isEmpty() ? 0 : 1;
      }
      printHtmlHead(out, "RB Demo", request);
      printPageHeader(out, "RB Demo: risk-based forecast",
          "Pick a scenario to make up a patient, send the VXU to establish the patient, then send"
              + " the risk-based query (QBP Z45) for the same patient to get the forecast (Z42).");
      if (connectors.isEmpty()) {
        printNoConnection(out);
        printHtmlFoot(out);
        return;
      }
      out.println("<form class=\"aira-stack\" action=\"RbDemoServlet\" method=\"POST\">");

      // 1. scenario
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__header\"><h2 class=\"aira-panel__title\">"
          + "1. Scenario</h2></div>");
      out.println("  <div class=\"aira-panel__body aira-stack aira-stack--compact\">");
      out.println("    <p class=\"aira-muted\">Each click makes up a new patient (random name,"
          + " address, phone, medical record number) with a plausible history.</p>");
      out.println("    <div class=\"aira-form-actions\">");
      for (String[] sc : SCENARIOS) {
        boolean selected = sc[0].equals(state.scenario);
        out.println("      <button class=\"aira-button aira-button--"
            + (selected ? "primary" : "secondary") + "\" type=\"submit\" name=\"" + PARAM_SCENARIO
            + "\" value=\"" + sc[0] + "\" title=\"" + escapeHtml(sc[2]) + "\">"
            + escapeHtml(sc[1]) + "</button>");
      }
      out.println("    </div>");
      if (connectors.size() > 1) {
        out.println("    <div class=\"aira-field\"><label for=\"" + PARAM_ID + "\">Connection"
            + "</label><select class=\"aira-select smm-select-auto\" id=\"" + PARAM_ID
            + "\" name=\"" + PARAM_ID + "\">");
        int i = 0;
        for (Connector c : connectors) {
          i++;
          out.println("      <option value=\"" + i + "\"" + (i == state.connectorId ? " selected" : "")
              + ">" + escapeHtml(c.getLabelDisplay()) + "</option>");
        }
        out.println("    </select></div>");
      } else {
        out.println("    <p class=\"aira-muted\">Connection: "
            + escapeHtml(connectors.get(0).getLabelDisplay()) + "</p>");
        out.println("    <input type=\"hidden\" name=\"" + PARAM_ID + "\" value=\"1\"/>");
      }
      if (state.summary.length() > 0) {
        out.println("    <div class=\"aira-alert aira-alert--info\" role=\"status\"><p id=\"rb-summary\">"
            + escapeHtml(state.summary) + "</p></div>");
      }
      out.println("  </div>");
      out.println("</section>");

      if (state.vxu.length() == 0) {
        out.println("</form>");
        printHtmlFoot(out);
        return;
      }

      // 2. VXU
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__header\"><h2 class=\"aira-panel__title\">"
          + "2. VXU: establish the patient</h2></div>");
      out.println("  <div class=\"aira-panel__body aira-stack aira-stack--compact\">");
      out.println("    <textarea class=\"aira-textarea smm-code\" id=\"" + PARAM_VXU + "\" name=\""
          + PARAM_VXU + "\" rows=\"10\" wrap=\"off\">" + escapeHtml(state.vxu) + "</textarea>");
      out.println("    <div class=\"aira-form-actions\">");
      out.println("      <button class=\"aira-button aira-button--primary\" type=\"submit\" name=\""
          + PARAM_ACTION + "\" value=\"" + ACTION_SEND_VXU + "\">" + ACTION_SEND_VXU
          + " to IIS</button>");
      out.println("    </div>");
      if (state.vxuResponse != null) {
        printAck(out, state, connectors);
      }
      out.println("  </div>");
      out.println("</section>");

      // 3. Z45
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__header\"><h2 class=\"aira-panel__title\">"
          + "3. Get risk-based forecast (QBP Z45)</h2></div>");
      out.println("  <div class=\"aira-panel__body aira-stack aira-stack--compact\">");
      out.println("    <div class=\"aira-field\">");
      out.println("      <label for=\"" + PARAM_CONDITIONS
          + "\">Contextual conditions (99CDS code=value)</label>");
      out.println("      <input class=\"aira-input\" type=\"text\" id=\"" + PARAM_CONDITIONS
          + "\" name=\"" + PARAM_CONDITIONS + "\" value=\"" + escapeHtml(state.conditions)
          + "\"/>");
      out.println("      <p class=\"aira-muted\">Comma separated <code>code=value</code> pairs. Y/N"
          + " or an 8 digit date (YYYYMMDD), for example <code>007=Y,170=20260401</code>."
          + " The query uses the same patient as the VXU above, edit either and the query follows."
          + " Press Refresh to update the preview.</p>");
      out.println("    </div>");
      String z45 = buildZ45(state);
      out.println("    <pre class=\"smm-hl7\" id=\"rb-z45\">" + escapeHtml(z45 == null ? "" : z45)
          + "</pre>");
      out.println("    <div class=\"aira-form-actions\">");
      out.println("      <button class=\"aira-button aira-button--primary\" type=\"submit\" name=\""
          + PARAM_ACTION + "\" value=\"" + ACTION_SEND_Z45 + "\">Get risk-based forecast (send"
          + " Z45)</button>");
      out.println("      <button class=\"aira-button aira-button--secondary\" type=\"submit\" name=\""
          + PARAM_ACTION + "\" value=\"Refresh\">Refresh</button>");
      out.println("    </div>");
      if (state.z45Response != null) {
        printRsp(out, state);
      }
      out.println("  </div>");
      out.println("</section>");
      out.println("</form>");
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  private void printAck(PrintWriter out, DemoState state, List<Connector> connectors) {
    String badge = "";
    try {
      Connector connector = connectors.get(state.connectorId - 1);
      AckAnalyzer analyzer = new AckAnalyzer(state.vxuResponse, connector.getAckType());
      badge = analyzer.isPositive() ? " <span class=\"aira-badge aira-badge--success\">Accepted</span>"
          : " <span class=\"aira-badge aira-badge--danger\">Rejected</span>";
    } catch (Exception e) {
      badge = " <span class=\"aira-badge aira-badge--danger\">Not an ACK</span>";
    }
    out.println("    <h3 class=\"aira-panel__title\">ACK received" + badge + "</h3>");
    out.println("    <pre class=\"smm-hl7\" id=\"rb-ack\">" + escapeHtml(state.vxuResponse)
        + "</pre>");
  }

  private void printRsp(PrintWriter out, DemoState state) {
    String badge = "";
    HL7Reader r = new HL7Reader(state.z45Response);
    if (r.advanceToSegment("MSH")) {
      String profile = r.getValue(21, 1);
      String type = r.getValue(9, 1);
      boolean z42 = "RSP".equals(type) && "Z42".equals(profile);
      badge = " <span class=\"aira-badge aira-badge--" + (z42 ? "success" : "danger") + "\">"
          + escapeHtml(type + (profile.length() > 0 ? " " + profile : "")) + "</span>";
    }
    out.println("    <h3 class=\"aira-panel__title\">Query response received" + badge + "</h3>");
    out.println("    <pre class=\"smm-hl7\" id=\"rb-rsp\">" + escapeHtml(state.z45Response)
        + "</pre>");
  }
}