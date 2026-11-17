package org.immregistries.smm.roster;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.smm.roster.hl7.SchoolRosterHL7Generator.MessageProfile;
import org.immregistries.smm.roster.model.LoadDetails;
import org.immregistries.smm.tester.ClientServlet;
import org.immregistries.smm.tester.ConnectServlet;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.web.auth.SmmSession;
import org.immregistries.smm.web.auth.SmmUser;
import org.immregistries.smm.workspace.Workspace;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@MultipartConfig
public class SchoolRosterServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;
  
  private static final String PARAM_FILE = "file";
  private static final String PARAM_MESSAGE_PROFILE = "messageProfile";
  private static final String ATTRIBUTE_LOAD_DETAILS = "loadDetails";
  
  protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    
    try {
      HttpSession session = request.getSession(true);
      
      printHtmlHead(out, MENU_HEADER_HOME, request);

      printPageHeader(
        out,
        "School Roster Upload",
        "Upload a school roster file and convert to HL7.");

      List<Connector> connectors = ConnectServlet.getConnectors(session);
      if (connectors.isEmpty()) {
        out.println("<div class='aira-stack'>");
        printNoConnection(out);
        out.println("</div>");
        return;
      }

      int connectionId = getConnectionId(connectors, request, session);

      if (connectors.size() == 1) {
        SmmUser user = SmmSession.getUser(request);
        boolean enableWorkspaceScanning = user != null && user.isSchoolRosterScanning();
        out.println(
        """
          <div class="aira-stack">
            <section class="aira-panel">
              <div class="aira-panel__body">
                <form class="aira-form" method="post">
                  <div class="aira-field">
                    <label class="aira-checkbox">
                      <input
                        type="checkbox"
                        name="enableWorkspaceScanning"
                        value="true"
                        CHECKED
                        onchange="this.form.submit()" />
                      <span>Enable Workspace Scanning</span>
                    </label>
                    SCANNING_HELP
                  </div>
                  <input type="hidden" name="connectionId" value="1" />
                  <input type="hidden" name="formType" value="workspaceScanning" />
                </form>
              </div>
            </section>
          </div>
        """
          .replace("CHECKED", enableWorkspaceScanning ? " checked" : "")
          .replace("SCANNING_HELP", "<p class=\"aira-field-help\">Place compatible roster files in your workspace's <code>school-roster</code> directory.</p>"));
      }

      out.println(
      """
        <div class="aira-stack">
          <section class="aira-panel">
            <div class="aira-panel__body">
              <form class="aira-form" method="post" enctype="multipart/form-data">
                <div class="aira-field">
      """);

      if (connectors.size() == 1) {
        out.println(
        """
          <span class="aira-label">Connection</span>
          <span>CONNECTOR_LABEL</span>
          <input type="hidden" name="connectionId" value="1" />
        """
          .replace("CONNECTOR_LABEL", escapeHtml(connectors.get(0).getLabelDisplay())));
      } else {
        out.println(
        """
          <label for="connectionId">Connection</label>
          <select class="aira-select smm-select-auto" id="connectionId" name="connectionId">
          <option value="">select</option>
        """);
        
        int i = 0;
        for (Connector connector : connectors) {
          i++;
          out.println(
          """
            <option value="COUNTER" SELECTED>
              CONNECTOR_LABEL 
            </option>
          """
            .replace("COUNTER", "" + i)
            .replace("SELECTED", connectionId == i ? " selected" : "")
            .replace("CONNECTOR_LABEL", escapeHtml(connector.getLabelDisplay())));
        }
        
        out.println("</select>");
      }
      
      out.println(
      """
                </div>
                
                <div class="aira-field">
                  <label for="PARAM_FILE">
                    Choose a file:
                  </label>
                  <input
                    id="file"
                    type="file"
                    name="PARAM_FILE"
                    accept=".csv,text/csv">
                </div>
                
                <div class="aira-field">
                  <label for="messageProfile">
                    Message profile:
                  </label>
                  <select id="messageProfile" name="PARAM_MESSAGE_PROFILE">
                    MESSAGE_PROFILE_OPTIONS
                  </select>
                </div>
                
                <div class="aira-form-actions">
                  <button
                    class="aira-button aira-button--primary"
                    type="submit">
                    Upload
                  </button>
                </div>
              </form>
            </div>
          </section>
        </div>
      """
        .replace("PARAM_FILE", PARAM_FILE)
        .replace("PARAM_MESSAGE_PROFILE", PARAM_MESSAGE_PROFILE)
        .replace(
          "MESSAGE_PROFILE_OPTIONS",
          Arrays.stream(MessageProfile.values())
            .map(profile ->
              "<option value=\"" + profile.name() + "\""
              + (profile.name().equals(request.getParameter(PARAM_MESSAGE_PROFILE)) ? " selected" : "")
              + ">"
              + profile.getFriendly()
              + "</option>")
            .collect(Collectors.joining())));
      
      LoadDetails details = (LoadDetails) request.getAttribute(ATTRIBUTE_LOAD_DETAILS);
      if (details != null) {
        out.println(
        """
           <div
             class="aira-alert aira-alert--warning"
             role="status"
             style="margin-top: 10px">
        """);
        
        if (!details.getOverallErrors().isEmpty()) {
          out.println("<p>File Errors</p><ul>");
          for (String error : details.getOverallErrors()) {
            out.println("<li>" + error + "</li>");
          }
          out.println("</ul>");
        }
        
        int largestLineNumber =
          Math.max(
            Collections.max(details.getFileLoadDetails().keySet()),
            Collections.max(details.getHl7Details().keySet()));
        
        if (largestLineNumber > 0) {
          out.println("<p>Load Information</p><ul>");
          for (int lineNumber = 0; lineNumber <= largestLineNumber; lineNumber++) {
            if (!details.getFileLoadDetails().containsKey(lineNumber)
                && !details.getHl7Details().containsKey(lineNumber)) {
              continue;
            }
            
            out.println("<li>Line " + lineNumber + ":<ul>");
            
            if (details.getFileLoadDetails().containsKey(lineNumber)) {
              for (String message : details.getFileLoadDetails().get(lineNumber).getMessages()) {
                out.println("<li>" + message + "</li>");
              }
            }
            
            if (details.getHl7Details().containsKey(lineNumber)) {
              for (String message : details.getHl7Details().get(lineNumber).getMessages()) {
                out.println("<li>" + message + "</li>");
              }
            }
            
            out.println("</ul>");
          }
          out.println("</ul>");
        }
        
        out.println("</div>");
      }
    } finally {
      printHtmlFoot(out);
      out.close();
    }
  }
  
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);
  }
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    
    String formType = request.getParameter("formType");
    if ("workspaceScanning".equals(formType)) {
      SmmUser user = SmmSession.getUser(request);
      
      if (user != null && StringUtils.isNotBlank(request.getParameter("connectionId"))) {
        try {
          int connectionId = Integer.parseInt(request.getParameter("connectionId"));
          
          boolean enabled = Boolean.parseBoolean(request.getParameter("enableWorkspaceScanning"));
          user.setSchoolRosterScanning(enabled);
          
          if (enabled) {
            SchoolRosterFileHandler.createSchoolRosterDirectory(user.getWorkspace());
            user.setSchoolRosterScanningConnector(
              getConnector(connectionId, request.getSession(true)));
          }
          
          SchoolRosterScanner scanner = SchoolRosterScanner.getOrCreate(user);
          if (scanner != null) {
            scanner.setEnabled(enabled);
          }
        } catch (NumberFormatException e) {
          e.printStackTrace();
          //TODO print something for user
        } catch (IOException e) {
          e.printStackTrace();
          //TODO print something for user
        }
      } else {
        //TODO print something for user
      }
      
      processRequest(request, response);
      return;
    }

    handleUpload(request);

    processRequest(request, response);
  }
  
  private void handleUpload(HttpServletRequest request) throws IOException, ServletException {
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    
    if (StringUtils.isBlank(request.getParameter("connectionId"))) {
      LoadDetails details = new LoadDetails(username);
      details.addOverallError("Please select a Connection.");
      request.setAttribute(ATTRIBUTE_LOAD_DETAILS, details);
      return;
    }
    
    MessageProfile profile = MessageProfile.COMPATIBLE_BASELINE;
    String selectedProfile = request.getParameter(PARAM_MESSAGE_PROFILE);
    if (selectedProfile != null && !selectedProfile.isBlank()) {
      try {
        profile = MessageProfile.valueOf(selectedProfile);
      } catch (IllegalArgumentException e) {
        LoadDetails details = new LoadDetails(username);
        details.addOverallError("Invalid message profile selected.");
        request.setAttribute(ATTRIBUTE_LOAD_DETAILS, details);
        return;
      }
    }
    
    SmmUser user = SmmSession.getUser(request);
    
    Workspace workspace = user != null ? user.getWorkspace() : null;
    
    SchoolRosterFileHandler fileHandler =
      new SchoolRosterFileHandler(
        username,
        getConnector(
          Integer.parseInt(request.getParameter("connectionId")),
          session),
        workspace,
        profile);
    
    Part file = request.getPart(PARAM_FILE);
    String filename = getSubmittedFileName(file);
    LoadDetails details = fileHandler.handle(file.getInputStream(), filename);
    
    request.setAttribute(ATTRIBUTE_LOAD_DETAILS, details);
  }

  private String getSubmittedFileName(Part part) {
    if (part == null) {
      return null;
    }
    
    String contentDisposition = part.getHeader("content-disposition");
    String[] elements = contentDisposition.split(";");
    for (String element : elements) {
      if (element.trim().startsWith("filename")) {
        return element.substring(element.indexOf('=') + 1).trim().replace("\"", "");
      }
    }
    
    return null;
  }

  private int getConnectionId(
      List<Connector> connectors,
      HttpServletRequest request,
      HttpSession session) {
    
    if (connectors.size() == 1) {
      return 1;
    } else {
      if (StringUtils.isNotBlank(request.getParameter("connectionId"))) {
        return Integer.parseInt(request.getParameter("connectionId"));
      } else if (session.getAttribute("connectionId") != null) {
        return (Integer) session.getAttribute("connectionId");
      }
    }
    
    return 0;
  }
  
  private static Connector getConnector(int id, HttpSession session) {
    List<Connector> connectors = ConnectServlet.getConnectors(session);
    id--;
    if (id < connectors.size()) {
      return connectors.get(id);
    }
    
    return null;
  }
}
