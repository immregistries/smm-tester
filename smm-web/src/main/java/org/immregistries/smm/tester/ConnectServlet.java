package org.immregistries.smm.tester;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.immregistries.smm.mover.ConnectionManager;
import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.web.auth.SmmUser;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * @author nathan
 */
public class ConnectServlet extends ClientServlet {
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
      SmmUser user = (SmmUser) session.getAttribute("user");
      String testScript = request.getParameter("testScript");
      List<TestCaseMessage> selectedTestCaseMessageList = null;
      if (testScript == null) {
        selectedTestCaseMessageList = getSelectedTestCaseMessageList(request, session);
        if (selectedTestCaseMessageList != null) {
          TestCaseServlet.sortTestCaseMessageList(selectedTestCaseMessageList);
          session.setAttribute("selectedTestCaseMessageList", selectedTestCaseMessageList);
        }
      }

      String action = request.getParameter("action");
      String message = null;
      if (action != null) {
        if (action.equals("Unload")) {
          deleteConnection(request, session);
        } else if (action.equals("Load Connections")) {
          runConnectionScript(request, session);
        } else if (action.equals("Add")) {
          addConnection(request, session);
        } else if (action.equals("Switch")) {
          int internalId = Integer.parseInt(request.getParameter("sendDataInternalId"));
          try {
            user.setSendData(addNewConnection(session, internalId, true));
          } catch (Exception e) {
            e.printStackTrace(System.out);
            message = "Unable to load test cases: " + e.getMessage();
          }
        }
      }
      if (message != null) {
        request.setAttribute("message", message);
      }

      PrintWriter out = response.getWriter();
      try {
        printHtmlHead(out, MENU_HEADER_CONNECT, request);
        printPageHeader(out, "Connect to an IIS",
            "Set up the connections this session uses to send messages to an IIS.");
        out.println("<div class=\"aira-stack\">");
        if (user.isAdmin()) {
          out.println("<section class=\"aira-panel\">");
          out.println("  <div class=\"aira-panel__header\">"
              + "<h2 class=\"aira-panel__title\">Mover Connection</h2></div>");
          out.println("  <div class=\"aira-panel__body\">");
          out.println("    <form class=\"aira-inline-form\" action=\"ConnectServlet\" method=\"POST\">");
          out.println("      <label class=\"aira-label\" for=\"sendDataInternalId\">Account</label>");
          out.println("      <select class=\"aira-select smm-select-auto\" id=\"sendDataInternalId\""
              + " name=\"sendDataInternalId\">");
          boolean selected = !user.hasSendData();
          out.println("        <option value=\"0\"" + (selected ? " selected" : "")
              + ">-- none selected --</option>");
          for (SendData sendData : ConnectionManager.getSendDataList()) {
            if (sendData.getConnector() != null) {
              selected = user.hasSendData()
                  && user.getSendData().getInternalId() == sendData.getInternalId();
              out.println("        <option value=\"" + sendData.getInternalId() + "\""
                  + (selected ? " selected" : "") + ">"
                  + escapeHtml(sendData.getConnector().getLabel()) + "</option>");
            }
          }
          out.println("      </select>");
          out.println("      <button class=\"aira-button aira-button--secondary\" type=\"submit\""
              + " name=\"action\" value=\"Switch\">Switch</button>");
          out.println("    </form>");
          out.println("  </div>");
          out.println("</section>");
        }

        List<Connector> connectors = getConnectors(session);
        out.println("<section class=\"aira-table-panel\">");
        out.println("  <div class=\"aira-table-panel__header\"><div>"
            + "<h2 class=\"aira-table-panel__title\">Connections</h2></div></div>");
        out.println("  <div class=\"aira-table-wrap\">");
        out.println("    <table class=\"aira-table\">");
        out.println("      <caption class=\"aira-visually-hidden\">Connections</caption>");
        out.println("      <thead><tr><th scope=\"col\">Label</th><th scope=\"col\">Type</th>"
            + "<th scope=\"col\">URL</th><th scope=\"col\">User Id</th>"
            + "<th scope=\"col\">Facility Id</th><th scope=\"col\" class=\"aira-table__cell--actions\">"
            + "<span class=\"aira-visually-hidden\">Actions</span></th></tr></thead>");
        out.println("      <tbody>");
        if (connectors.isEmpty()) {
          out.println("        <tr><td colspan=\"6\" class=\"aira-table__empty\">"
              + "No connections yet. Add one below or load a connection script.</td></tr>");
        }
        int id = 0;
        for (Connector connector : connectors) {
          id++;
          out.println("        <tr>");
          out.println("          <th scope=\"row\" class=\"aira-table__cell--primary\">"
              + escapeHtml(connector.getLabel()) + "</th>");
          out.println("          <td>" + escapeHtml(connector.getType()) + "</td>");
          out.println("          <td class=\"aira-table__cell--code\">"
              + escapeHtml(connector.getUrl()) + "</td>");
          out.println("          <td>" + escapeHtml(connector.getUserid()) + "</td>");
          out.println("          <td>" + escapeHtml(connector.getFacilityid()) + "</td>");
          out.println("          <td class=\"aira-table__cell--actions\">");
          out.println("            <form action=\"ConnectServlet\" method=\"POST\">");
          out.println("              <input type=\"hidden\" name=\"id\" value=\"" + id + "\">");
          out.println("              <button class=\"aira-button aira-button--small aira-button--ghost\""
              + " type=\"submit\" name=\"action\" value=\"Unload\">Unload</button>");
          out.println("            </form>");
          out.println("          </td>");
          out.println("        </tr>");
        }
        out.println("      </tbody>");
        out.println("    </table>");
        out.println("  </div>");
        out.println("</section>");

        if (user.getSendData() != null && user.getSendData().getTestParticipant() != null
            && connectors.contains(user.getSendData().getConnector())) {
          out.println("<section class=\"aira-panel\">");
          out.println("  <div class=\"aira-panel__header\">"
              + "<h2 class=\"aira-panel__title\">AART Participant</h2></div>");
          out.println("  <div class=\"aira-panel__body\">");
          out.println("    <dl class=\"smm-details\">");
          out.println("      <dt>Label</dt><dd>"
              + escapeHtml(user.getSendData().getTestParticipant().getFolderName()) + "</dd>");
          out.println("      <dt>Organization</dt><dd>"
              + escapeHtml(user.getSendData().getTestParticipant().getOrganizationName())
              + "</dd>");
          out.println("      <dt>Public Id Code</dt><dd>"
              + escapeHtml(user.getSendData().getTestParticipant().getPublicIdCode()) + "</dd>");
          out.println("    </dl>");
          out.println("  </div>");
          out.println("</section>");
        }

        String label = request.getParameter("label");
        if (label == null) {
          label = "";
        }
        String type = request.getParameter("type");
        if (type == null) {
          type = "";
        }
        String url = request.getParameter("url");
        if (url == null) {
          url = "";
        }
        String userid = request.getParameter("userid");
        if (userid == null) {
          userid = "";
        }
        String facilityid = request.getParameter("facilityid");
        if (facilityid == null) {
          facilityid = "";
        }
        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">Add a Connection</h2></div>");
        out.println("  <div class=\"aira-panel__body\">");
        out.println("    <form class=\"aira-form\" action=\"ConnectServlet\" method=\"POST\">");
        out.println("      <div class=\"aira-field-row\">");
        printTextField(out, "label", "Label", label);
        out.println("        <div class=\"aira-field\">");
        out.println("          <label for=\"type\">Type</label>");
        out.println("          <select class=\"aira-select\" id=\"type\" name=\"type\">");
        out.println("            <option value=\"\">select</option>");
        for (String[] option : ConnectorFactory.TYPES) {
          out.println("            <option value=\"" + escapeHtml(option[0]) + "\""
              + (type.equals(option[0]) ? " selected" : "") + ">" + escapeHtml(option[1])
              + "</option>");
        }
        out.println("          </select>");
        out.println("        </div>");
        out.println("      </div>");
        printTextField(out, "url", "URL", url);
        out.println("      <div class=\"aira-field-row\">");
        printTextField(out, "userid", "User Id", userid);
        out.println("        <div class=\"aira-field\">");
        out.println("          <label for=\"password\">Password</label>");
        out.println("          <input class=\"aira-input\" type=\"password\" id=\"password\""
            + " name=\"password\" autocomplete=\"off\">");
        out.println("        </div>");
        printTextField(out, "facilityid", "Facility Id", facilityid);
        out.println("      </div>");
        out.println("      <div class=\"aira-form-actions\">");
        out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
            + " name=\"action\" value=\"Add\">Add Connection</button>");
        out.println("      </div>");
        out.println("    </form>");
        out.println("  </div>");
        out.println("</section>");

        String connectorScript = request.getParameter("connectorScript");
        if (connectorScript == null) {
          connectorScript = (String) request.getAttribute("connectorScript");
          if (connectorScript == null) {
            connectorScript = "";
          }
        }
        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">Connection Script</h2></div>");
        out.println("  <div class=\"aira-panel__body\">");
        out.println("    <form class=\"aira-form\" action=\"ConnectServlet\" method=\"POST\">");
        out.println("      <div class=\"aira-field\">");
        out.println("        <label for=\"connectorScript\">Script</label>");
        out.println("        <textarea class=\"aira-textarea smm-code\" id=\"connectorScript\""
            + " name=\"connectorScript\" rows=\"8\" wrap=\"off\">" + escapeHtml(connectorScript)
            + "</textarea>");
        out.println("        <p class=\"aira-field-help\">Adding or unloading a connection shows its"
            + " script here, so it can be saved and loaded again later.</p>");
        out.println("      </div>");
        out.println("      <div class=\"aira-form-actions\">");
        out.println("        <button class=\"aira-button aira-button--secondary\" type=\"submit\""
            + " name=\"action\" value=\"Load Connections\">Load Connections</button>");
        out.println("      </div>");
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

  public static SendData addNewConnection(HttpSession session, int internalId,
      boolean removeOtherConnections) throws ServletException, IOException {
    SendData sendData = null;
    if (session != null) {
      CreateTestCaseServlet.getTestCaseMessageMapMap(session).clear();
    }
    if (internalId != 0) {
      sendData = ConnectionManager.getSendData(internalId);
      if (removeOtherConnections && session != null) {
        getConnectors(session).clear();
      }
      if (session != null) {
        ConnectServlet.addConnector(sendData.getConnector(), session);
      }
      if (sendData.getConnector().isSetupGlobalKeyStore()) {
        try {
          setupKeystore(sendData);
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
      } else {
        sendData.readKeyStore();
      }
      if (session != null) {
        if (sendData.getConnector().getOtherConnectorMap().size() > 0) {
          for (Connector connector : sendData.getConnector().getOtherConnectorMap().values()) {
            ConnectServlet.addConnector(connector, session);
          }
        }
        CreateTestCaseServlet.loadTestCases(session);
      }
    }
    return sendData;
  }

  public static void setupKeystore(SendData sendData) throws IOException {
    if (sendData.getConnector().getKeyStorePassword() != null
        && !sendData.getConnector().getKeyStorePassword().equals("")) {
      File keyStoreFile = new File(sendData.getRootDir(), SendData.KEYSTORE_FILE_NAME);
      if (keyStoreFile.exists()) {
        System.setProperty("javax.net.ssl.keyStore", keyStoreFile.getCanonicalPath());
        System.setProperty("javax.net.ssl.keyStorePassword",
            sendData.getConnector().getKeyStorePassword());
        System.out.println("Set keystore to be: " + keyStoreFile.getCanonicalPath());
      } else {
        System.err
            .println("Unable to find key store file here: " + keyStoreFile.getCanonicalPath());
      }
    }
  }

  protected static List<TestCaseMessage> getSelectedTestCaseMessageList(HttpServletRequest request,
      HttpSession session) {
    List<TestCaseMessage> testCaseMessageList = new ArrayList<TestCaseMessage>();
    Set<String> testCaseNumberSelectedSet =
        TestCaseServlet.setTestCaseNumberSelectedSet(request, session);
    Map<String, TestCaseMessage> testCaseMessageMap = CreateTestCaseServlet
        .getTestCaseMessageMap(null, CreateTestCaseServlet.getTestCaseMessageMapMap(session));
    for (String testCaseNumber : testCaseNumberSelectedSet) {
      TestCaseMessage tcm = testCaseMessageMap.get(testCaseNumber);
      if (testCaseNumberSelectedSet.contains(tcm.getTestCaseNumber())) {
        testCaseMessageList.add(tcm);
      }
    }
    return testCaseMessageList;
  }

  protected void addConnection(HttpServletRequest request, HttpSession session) {
    String label = request.getParameter("label");
    if (label == null) {
      label = "";
    }
    String type = request.getParameter("type");
    if (type == null) {
      type = "";
    }
    String url = request.getParameter("url");
    if (url == null) {
      url = "";
    }
    String userid = request.getParameter("userid");
    if (userid == null) {
      userid = "";
    }
    String password = request.getParameter("password");
    if (password == null) {
      password = null;
    }
    String facilityid = request.getParameter("facilityid");
    if (facilityid == null) {
      facilityid = null;
    }
    String message = "Creating connection";
    if (type.equals("")) {
      message = "Type must be specified to add new connection";
    } else if (label.equals("")) {
      message = "Label must be specified to add new connection";
    }
    if (message != null) {
      Connector connector = null;
      try {
        connector = ConnectorFactory.getConnector(type, label, url);
        if (connector == null) {
          message = "Unable to find connection type";
        } else {
          connector.setUserid(userid);
          connector.setPassword(password);
          connector.setFacilityid(facilityid);
          request.setAttribute("connectorScript", connector.getScript());
          List<Connector> connectors = getConnectors(session);
          connectors.add(connector);
          message = "Added new connection";
        }
      } catch (Exception e) {
        message = "Unable to create connector: " + e.getMessage();
        e.printStackTrace();
      }
    }
    if (message != null) {
      request.setAttribute("message", message);
    }
  }

  protected void runConnectionScript(HttpServletRequest request, HttpSession session) {
    try {
      String connectorScript = request.getParameter("connectorScript");
      addConnectors(connectorScript, session);
    } catch (Exception e) {
      String message = "Unable to run script, exception ocurred: " + e.getMessage();
      request.setAttribute("message", message);
    }
  }

  protected void deleteConnection(HttpServletRequest request, HttpSession session)
      throws NumberFormatException {
    int id = Integer.parseInt(request.getParameter("id"));
    List<Connector> connectors = getConnectors(session);
    Connector removedConnector = connectors.remove(id - 1);
    request.setAttribute("connectorScript", removedConnector.getScript());
  }

  protected static void addConnectors(String connectorScript, HttpSession session)
      throws Exception {
    List<Connector> newConnectors = Connector.makeConnectors(connectorScript);
    List<Connector> oldConnectors = getConnectors(session);
    for (Connector newConnector : newConnectors) {
      for (Iterator<Connector> it = oldConnectors.iterator(); it.hasNext();) {
        Connector oldConnector = it.next();
        if (oldConnector.getLabel().equals(newConnector.getLabel())
            && oldConnector.getType().equals(newConnector.getType())
            && oldConnector.getPurpose().equals(newConnector.getPurpose())) {
          it.remove();
        }
      }
    }
    oldConnectors.addAll(newConnectors);
  }

  protected static void addConnector(Connector nc, HttpSession session) {
    List<Connector> oldConnectors = getConnectors(session);
    for (Iterator<Connector> it = oldConnectors.iterator(); it.hasNext();) {
      Connector oc = it.next();
      if (oc.getLabel().equals(nc.getLabel()) && oc.getType().equals(nc.getType())
          && oc.getPurpose().equals(nc.getPurpose())) {
        it.remove();
      }
    }
    oldConnectors.add(nc);
    if (session.getAttribute("id") == null) {
      session.setAttribute("id", oldConnectors.size());
    }
  }

  public static List<Connector> getConnectors(HttpSession session) {
    @SuppressWarnings("unchecked")
    List<Connector> connectors = (List<Connector>) session.getAttribute("connectors");
    if (connectors == null) {
      connectors = new ArrayList<Connector>();
      session.setAttribute("connectors", connectors);
    }
    return connectors;
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
