package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.immregistries.smm.tester.manager.query.PatientIdType;
import org.immregistries.smm.tester.manager.query.QueryConverter;
import org.immregistries.smm.tester.manager.query.QueryRequest;
import org.immregistries.smm.tester.manager.query.QueryType;
import org.immregistries.smm.transform.ScenarioManager;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author nathan
 */
public class ManualQueryServlet extends ClientServlet {

  public static final String PARAM_ACTION = "action";
  public static final String ACTION_UPDATE = "Send Update";
  public static final String ACTION_QUERY = "Send Query";
  public static final String ACTION_REFRESH = "Refresh";

  public static final String PARAM_ID_NUMBER = "idNumber";
  public static final String PARAM_ID_AUTHORITY = "idAuthority";
  public static final String PARAM_ID_TYPE = "idType";
  public static final String PARAM_NAME_LAST = "nameLast";
  public static final String PARAM_NAME_FIRST = "nameFirst";
  public static final String PARAM_NAME_MIDDLE = "nameMiddle";
  public static final String PARAM_MOTHER_NAME_MAIDEN = "motherNameMaiden";
  public static final String PARAM_MOTHER_NAME_LAST = "motherNameLast";
  public static final String PARAM_MOTHER_NAME_FIRST = "motherNameFirst";
  public static final String PARAM_MOTHER_NAME_MIDDLE = "motherNameMiddle";
  public static final String PARAM_BIRTH_DATE = "birthDate";
  public static final String PARAM_SEX = "sex";
  public static final String PARAM_ADDRESS_STREET_1 = "addressStreet1";
  public static final String PARAM_ADDRESS_STREET_2 = "addressStreet2";
  public static final String PARAM_ADDRESS_CITY = "addressCity";
  public static final String PARAM_ADDRESS_STATE = "addressState";
  public static final String PARAM_ADDRESS_ZIP = "addressZip";
  public static final String PARAM_ADDRESS_COUNTRY = "addressCountry";
  public static final String PARAM_PHONE_AREA = "phoneArea";
  public static final String PARAM_PHONE_LOCAL = "phoneLocal";
  public static final String PARAM_MULTIPLE_BIRTH_INDICATOR = "multipleBirthIndicator";
  public static final String PARAM_MULTIPLE_BIRTH_ORDER = "multipleBirthOrder";


  public static final String PARAM_QUERY_TYPE = "queryType";



  /**
   * 
   */
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
    String action = request.getParameter(PARAM_ACTION);
    String problem = null;
    if (username == null) {
      response.sendRedirect(ClientServlet.APP_DEFAULT_HOME);
      return;
    }
    QueryRequest queryRequest = new QueryRequest();
    problem = readQueryRequest(request, problem, queryRequest);
    session.setAttribute("queryRequest", queryRequest);
    if (action != null) {
      if (action.equals(ACTION_UPDATE)) {
        TestCaseMessage testCaseMessage = createTestCaseMessage(queryRequest);
        session.setAttribute("testCaseMessage", testCaseMessage);
        session.setAttribute("message", testCaseMessage.getMessageText());
        response.sendRedirect("SubmitServlet");
        return;
      } else if (action.equals(ACTION_QUERY)) {
        TestCaseMessage testCaseMessage = createTestCaseMessage(queryRequest);

        if (queryRequest.getQueryType() != null) {
          QueryConverter queryConverter =
              QueryConverter.getQueryConverter(queryRequest.getQueryType());
          if (queryConverter != null) {
            TestCaseMessage queryTestCaseMessage = new TestCaseMessage();
            queryTestCaseMessage
                .setMessageText(queryConverter.convert(testCaseMessage.getMessageText()));
            queryTestCaseMessage.setDerivedFromVXUMessage(testCaseMessage.getMessageText());
            session.setAttribute("testCaseMessage", queryTestCaseMessage);
            session.setAttribute("message", queryTestCaseMessage.getMessageText());
            response.sendRedirect("SubmitServlet");
            return;
          }
        }

      }
    }
    doGet(request, response, session, problem, queryRequest);
  }


  public String readQueryRequest(HttpServletRequest request, String problem,
      QueryRequest queryRequest) {
    queryRequest.setIdNumber(request.getParameter(PARAM_ID_NUMBER));
    queryRequest.setIdAuthority(request.getParameter(PARAM_ID_AUTHORITY));
    queryRequest.setIdType(PatientIdType.valueOf(request.getParameter(PARAM_ID_TYPE)));
    queryRequest.setNameLast(request.getParameter(PARAM_NAME_LAST));
    queryRequest.setNameFirst(request.getParameter(PARAM_NAME_FIRST));
    queryRequest.setNameMiddle(request.getParameter(PARAM_NAME_MIDDLE));
    queryRequest.setMotherNameMaiden(request.getParameter(PARAM_MOTHER_NAME_MAIDEN));
    queryRequest.setMotherNameLast(request.getParameter(PARAM_MOTHER_NAME_LAST));
    queryRequest.setMotherNameFirst(request.getParameter(PARAM_MOTHER_NAME_FIRST));
    queryRequest.setMotherNameMiddle(request.getParameter(PARAM_MOTHER_NAME_MIDDLE));
    if (request.getParameter(PARAM_BIRTH_DATE).equals("")) {
      queryRequest.setBirthDate(null);
    } else {
      SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
      try {
        queryRequest.setBirthDate(sdf.parse(request.getParameter(PARAM_BIRTH_DATE)));
      } catch (ParseException e) {
        try {
          sdf = new SimpleDateFormat("M/d/yyyy");
          queryRequest.setBirthDate(sdf.parse(request.getParameter(PARAM_BIRTH_DATE)));
        } catch (ParseException e2) {
          problem = "Unable to parse date '" + request.getParameter(PARAM_BIRTH_DATE)
              + "', must be in MM/DD/YYYY format";
        }
      }
    }
    queryRequest.setSex(request.getParameter(PARAM_SEX));
    queryRequest.setAddressStreet1(request.getParameter(PARAM_ADDRESS_STREET_1));
    queryRequest.setAddressStreet2(request.getParameter(PARAM_ADDRESS_STREET_2));
    queryRequest.setAddressCity(request.getParameter(PARAM_ADDRESS_CITY));
    queryRequest.setAddressState(request.getParameter(PARAM_ADDRESS_STATE));
    queryRequest.setAddressZip(request.getParameter(PARAM_ADDRESS_ZIP));
    queryRequest.setAddressCountry(request.getParameter(PARAM_ADDRESS_COUNTRY));
    queryRequest.setPhoneArea(request.getParameter(PARAM_PHONE_AREA));
    queryRequest.setPhoneLocal(request.getParameter(PARAM_PHONE_LOCAL));
    queryRequest
        .setMultipleBirthIndicator(request.getParameter(PARAM_MULTIPLE_BIRTH_INDICATOR) != null);
    if (request.getParameter(PARAM_MULTIPLE_BIRTH_ORDER).equals("")) {
      queryRequest.setMultipleBirthOrder(0);
    } else {
      queryRequest.setMultipleBirthOrder(
          Integer.parseInt(request.getParameter(PARAM_MULTIPLE_BIRTH_ORDER)));
    }
    return problem;
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
      QueryRequest queryRequest = (QueryRequest) session.getAttribute("queryRequest");
      if (queryRequest == null) {
        queryRequest = new QueryRequest();
        session.setAttribute("queryRequest", queryRequest);
      }
      doGet(request, response, session, null, queryRequest);
    }
  }

  private void doGet(HttpServletRequest request, HttpServletResponse response, HttpSession session,
      String problem, QueryRequest queryRequest) throws IOException {
    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_HOME, request);
      printPageHeader(out, "Query IIS",
          "Describe a patient, then send an update (VXU) or a query for that patient.");

      if (problem != null) {
        out.println("<div class=\"aira-alert aira-alert--warning\" role=\"status\"><p>"
            + escapeHtml(problem) + "</p></div>");
      }

      out.println("<form class=\"aira-stack\" action=\"ManualQueryServlet\" method=\"POST\">");
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__header\">"
          + "<h2 class=\"aira-panel__title\">Patient</h2></div>");
      out.println("  <div class=\"aira-panel__body aira-form\">");
      out.println("      <div class=\"aira-field-row\">");
      printTextField(out, PARAM_ID_NUMBER, "Id", queryRequest.getIdNumber(), null);
      printTextField(out, PARAM_ID_AUTHORITY, "Authority", queryRequest.getIdAuthority(), null);
      out.println("        <div class=\"aira-field\">");
      out.println("          <label for=\"" + PARAM_ID_TYPE + "\">Id Type</label>");
      out.println("          <select class=\"aira-select\" id=\"" + PARAM_ID_TYPE + "\" name=\""
          + PARAM_ID_TYPE + "\">");
      for (PatientIdType idType : PatientIdType.values()) {
        out.println("            <option value=\"" + idType + "\""
            + (idType == queryRequest.getIdType() ? " selected" : "") + ">" + idType + " - "
            + escapeHtml(idType.getLabel()) + "</option>");
      }
      out.println("          </select>");
      out.println("        </div>");
      out.println("      </div>");
      out.println("      <div class=\"aira-field-row\">");
      printTextField(out, PARAM_NAME_FIRST, "First Name", queryRequest.getNameFirst(), null);
      printTextField(out, PARAM_NAME_MIDDLE, "Middle Name", queryRequest.getNameMiddle(), null);
      printTextField(out, PARAM_NAME_LAST, "Last Name", queryRequest.getNameLast(), null);
      out.println("        <div class=\"aira-field\">");
      out.println("          <label for=\"" + PARAM_SEX + "\">Sex</label>");
      out.println("          <select class=\"aira-select\" id=\"" + PARAM_SEX + "\" name=\""
          + PARAM_SEX + "\">");
      for (String sex : new String[] {"M", "F", "U"}) {
        out.println("            <option value=\"" + sex + "\""
            + (sex.equals(queryRequest.getSex()) ? " selected" : "") + ">" + sex + "</option>");
      }
      out.println("          </select>");
      out.println("        </div>");
      out.println("      </div>");
      out.println("      <div class=\"aira-field-row\">");
      printTextField(out, PARAM_MOTHER_NAME_FIRST, "Mother First Name",
          queryRequest.getMotherNameFirst(), null);
      printTextField(out, PARAM_MOTHER_NAME_MIDDLE, "Mother Middle Name",
          queryRequest.getMotherNameMiddle(), null);
      printTextField(out, PARAM_MOTHER_NAME_LAST, "Mother Last Name",
          queryRequest.getMotherNameLast(), null);
      printTextField(out, PARAM_MOTHER_NAME_MAIDEN, "Mother Maiden Name",
          queryRequest.getMotherNameMaiden(), null);
      out.println("      </div>");

      {
        String birthDateString = "";
        if (queryRequest.getBirthDate() != null) {
          SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
          birthDateString = sdf.format(queryRequest.getBirthDate());
        }
        boolean birthMultiple = queryRequest.getMultipleBirthIndicator();
        String birthOrder = "";
        if (queryRequest.getMultipleBirthOrder() > 0) {
          birthOrder = "" + queryRequest.getMultipleBirthOrder();
        }
        out.println("      <div class=\"aira-field-row\">");
        printTextField(out, PARAM_BIRTH_DATE, "Birth Date", birthDateString, "MM/DD/YYYY");
        out.println("        <div class=\"aira-field smm-field-end\">");
        out.println("          <label class=\"aira-check\"><input type=\"checkbox\" name=\""
            + PARAM_MULTIPLE_BIRTH_INDICATOR + "\" value=\"true\""
            + (birthMultiple ? " checked" : "") + "/> Multiple Birth</label>");
        out.println("        </div>");
        printTextField(out, PARAM_MULTIPLE_BIRTH_ORDER, "Birth Order", birthOrder, null);
        out.println("      </div>");
      }
      out.println("      <div class=\"aira-field-row\">");
      printTextField(out, PARAM_ADDRESS_STREET_1, "Street", queryRequest.getAddressStreet1(),
          "123 Main Street");
      printTextField(out, PARAM_ADDRESS_STREET_2, "Street Line 2",
          queryRequest.getAddressStreet2(), "Apt 101");
      out.println("      </div>");
      out.println("      <div class=\"aira-field-row\">");
      printTextField(out, PARAM_ADDRESS_CITY, "City", queryRequest.getAddressCity(), "Anytown");
      printTextField(out, PARAM_ADDRESS_STATE, "State", queryRequest.getAddressState(), "MI");
      printTextField(out, PARAM_ADDRESS_ZIP, "Zip", queryRequest.getAddressZip(), "12345");
      printTextField(out, PARAM_ADDRESS_COUNTRY, "Country", queryRequest.getAddressCountry(),
          null);
      out.println("      </div>");
      out.println("      <div class=\"aira-field-row\">");
      printTextField(out, PARAM_PHONE_AREA, "Phone Area Code", queryRequest.getPhoneArea(), "555");
      printTextField(out, PARAM_PHONE_LOCAL, "Phone Number", queryRequest.getPhoneLocal(),
          "123-4567");
      out.println("      </div>");
      out.println("      <div class=\"aira-form-actions\">");
      out.println("        <button class=\"aira-button aira-button--secondary\" type=\"submit\""
          + " name=\"" + PARAM_ACTION + "\" value=\"" + ACTION_REFRESH + "\">" + ACTION_REFRESH
          + "</button>");
      out.println("      </div>");
      out.println("  </div>");
      out.println("</section>");

      TestCaseMessage testCaseMessage = createTestCaseMessage(queryRequest);
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__header\">"
          + "<h2 class=\"aira-panel__title\">Update</h2></div>");
      out.println("  <div class=\"aira-panel__body aira-stack aira-stack--compact\">");
      out.println("    <pre class=\"smm-hl7\">" + escapeHtml(testCaseMessage.getMessageText())
          + "</pre>");
      out.println("    <div class=\"aira-form-actions\">");
      out.println("      <button class=\"aira-button aira-button--primary\" type=\"submit\" name=\""
          + PARAM_ACTION + "\" value=\"" + ACTION_UPDATE + "\">" + ACTION_UPDATE + "</button>");
      out.println("    </div>");
      out.println("  </div>");
      out.println("</section>");

      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__header\">"
          + "<h2 class=\"aira-panel__title\">Query</h2></div>");
      out.println("  <div class=\"aira-panel__body aira-stack aira-stack--compact\">");
      out.println("    <div class=\"aira-inline-form\">");
      out.println("      <label class=\"aira-label\" for=\"" + PARAM_QUERY_TYPE
          + "\">Query Type</label>");
      out.println("      <select class=\"aira-select smm-select-auto\" id=\"" + PARAM_QUERY_TYPE
          + "\" name=\"" + PARAM_QUERY_TYPE + "\">");
      for (QueryType qt : QueryType.values()) {
        out.println("        <option value=\"" + qt + "\""
            + (qt == queryRequest.getQueryType() ? " selected" : "") + ">" + qt + "</option>");
      }
      out.println("      </select>");
      out.println("      <button class=\"aira-button aira-button--primary\" type=\"submit\" name=\""
          + PARAM_ACTION + "\" value=\"" + ACTION_QUERY + "\">" + ACTION_QUERY + "</button>");
      out.println("    </div>");

      if (queryRequest.getQueryType() != null) {
        QueryConverter queryConverter =
            QueryConverter.getQueryConverter(queryRequest.getQueryType());
        if (queryConverter != null) {
          out.println("    <pre class=\"smm-hl7\">"
              + escapeHtml(queryConverter.convert(testCaseMessage.getMessageText())) + "</pre>");
        }
      }
      out.println("  </div>");
      out.println("</section>");
      out.println("</form>");
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }


  public TestCaseMessage createTestCaseMessage(QueryRequest queryRequest) {
    SimpleDateFormat sdfDateOnly = new SimpleDateFormat("yyyyMMdd");
    SimpleDateFormat sdfDateAndTime = new SimpleDateFormat("yyyyMMddHHmmssZ");
    TestCaseMessage testCaseMessage =
        ScenarioManager.createTestCaseMessage(ScenarioManager.SCENARIO_1_1_ADMIN_CHILD);
    testCaseMessage.appendCustomTransformation("PID-3.1=" + queryRequest.getIdNumber());
    testCaseMessage.appendCustomTransformation("PID-3.4=" + queryRequest.getIdAuthority());
    testCaseMessage.appendCustomTransformation("PID-3.5=" + queryRequest.getIdType());
    testCaseMessage.appendCustomTransformation("PID-5.1=" + queryRequest.getNameLast());
    testCaseMessage.appendCustomTransformation("PID-5.2=" + queryRequest.getNameFirst());
    testCaseMessage.appendCustomTransformation("PID-5.3=" + queryRequest.getNameMiddle());
    testCaseMessage.appendCustomTransformation("PID-6.1=" + queryRequest.getMotherNameMaiden());
    testCaseMessage.appendCustomTransformation("PID-6.2=" + queryRequest.getMotherNameFirst());
    testCaseMessage.appendCustomTransformation("PID-6.3=" + queryRequest.getMotherNameMiddle());
    testCaseMessage.appendCustomTransformation("NK1-2.1=" + queryRequest.getMotherNameLast());
    testCaseMessage.appendCustomTransformation("NK1-2.2=" + queryRequest.getMotherNameFirst());
    testCaseMessage.appendCustomTransformation("NK1-2.3=" + queryRequest.getMotherNameMiddle());
    if (queryRequest.getBirthDate() == null) {
      testCaseMessage.appendCustomTransformation("PID-7=");
    } else {
      testCaseMessage
          .appendCustomTransformation("PID-7=" + sdfDateOnly.format(queryRequest.getBirthDate()));
    }
    testCaseMessage.appendCustomTransformation("PID-8=" + queryRequest.getSex());
    testCaseMessage.appendCustomTransformation("PID-11.1=" + queryRequest.getAddressStreet1());
    testCaseMessage.appendCustomTransformation("PID-11.2=" + queryRequest.getAddressStreet2());
    testCaseMessage.appendCustomTransformation("PID-11.3=" + queryRequest.getAddressCity());
    testCaseMessage.appendCustomTransformation("PID-11.4=" + queryRequest.getAddressState());
    testCaseMessage.appendCustomTransformation("PID-11.5=" + queryRequest.getAddressZip());
    testCaseMessage.appendCustomTransformation("NK1-4.6=" + queryRequest.getAddressCountry());
    testCaseMessage.appendCustomTransformation("NK1-4.1=" + queryRequest.getAddressStreet1());
    testCaseMessage.appendCustomTransformation("NK1-4.2=" + queryRequest.getAddressStreet2());
    testCaseMessage.appendCustomTransformation("NK1-4.3=" + queryRequest.getAddressCity());
    testCaseMessage.appendCustomTransformation("NK1-4.4=" + queryRequest.getAddressState());
    testCaseMessage.appendCustomTransformation("NK1-4.5=" + queryRequest.getAddressZip());
    testCaseMessage.appendCustomTransformation("NK1-4.6=" + queryRequest.getAddressCountry());
    testCaseMessage.appendCustomTransformation("PID-13.6=" + queryRequest.getPhoneArea());
    testCaseMessage.appendCustomTransformation("PID-13.7=" + queryRequest.getPhoneLocal());
    testCaseMessage.appendCustomTransformation("NK1-5.6=" + queryRequest.getPhoneArea());
    testCaseMessage.appendCustomTransformation("NK1-5.7=" + queryRequest.getPhoneLocal());
    testCaseMessage.appendCustomTransformation(
        "PID-24=" + (queryRequest.getMultipleBirthIndicator() ? "Y" : "N"));
    if (queryRequest.getMultipleBirthOrder() == 0) {
      testCaseMessage.appendCustomTransformation("PID-25=");
    } else {
      testCaseMessage.appendCustomTransformation("PID-25=" + queryRequest.getMultipleBirthOrder());
    }
    Date now = new Date();
    testCaseMessage.appendCustomTransformation("RXA-3=" + sdfDateOnly.format(now));
    testCaseMessage.appendCustomTransformation("OBX#4-5=" + sdfDateOnly.format(now));
    testCaseMessage.appendCustomTransformation("MSH-7=" + sdfDateAndTime.format(now));

    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    return testCaseMessage;
  }

}

