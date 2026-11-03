package org.immregistries.smm.roster;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.roster.hl7.SchoolRosterHL7Generator;
import org.immregistries.smm.roster.hl7.SchoolRosterHL7Generator.MessageProfile;
import org.immregistries.smm.roster.loader.SchoolRosterCsvLoader;
import org.immregistries.smm.roster.loader.SchoolRosterLoader;
import org.immregistries.smm.roster.model.LineDetails;
import org.immregistries.smm.roster.model.LoadDetails;
import org.immregistries.smm.roster.model.Student;
import org.immregistries.smm.roster.model.StudentLoad;
import org.immregistries.smm.tester.ClientServlet;
import org.immregistries.smm.tester.ConnectServlet;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.HL7Reader;
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
    
    handleUpload(request);
    
    processRequest(request, response);
  }
  
  private void handleUpload(HttpServletRequest request) throws IOException, ServletException {
    HttpSession session = request.getSession(true);
    String username = (String) session.getAttribute("username");
    
    LoadDetails details = new LoadDetails(username);
    
    if (StringUtils.isBlank(request.getParameter("connectionId"))) {
      details.addOverallError("Please select a Connection.");
      request.setAttribute(ATTRIBUTE_LOAD_DETAILS, details);
      return;
    }
    
    int connectionId = Integer.parseInt(request.getParameter("connectionId"));
    
    Connector connector = getConnector(connectionId, session);
    
    if (connector == null) {
      details.addOverallError("Please select a Connection.");
      request.setAttribute(ATTRIBUTE_LOAD_DETAILS, details);
      return;
    }
    
    // Retrieve the uploaded file from the request
    Part file = request.getPart(PARAM_FILE);
    String filename = getSubmittedFileName(file);
    details.setFilename(filename);
    
    String submittedName = getSubmittedFileName(file);
    
    if (file == null || file.getSize() == 0 || submittedName == null) {
      details.addOverallError("Please upload a CSV file.");
      request.setAttribute(ATTRIBUTE_LOAD_DETAILS, details);
      return;
    }
    
    if (!submittedName.toLowerCase(Locale.ROOT).endsWith(".csv")) {
      details.addOverallError("Only CSV files are allowed.");
      request.setAttribute(ATTRIBUTE_LOAD_DETAILS, details);
      return;
    }
    
    MessageProfile profile = MessageProfile.COMPATIBLE_BASELINE;
    String selectedProfile = request.getParameter(PARAM_MESSAGE_PROFILE);
    if (selectedProfile != null && !selectedProfile.isBlank()) {
      try {
        profile = MessageProfile.valueOf(selectedProfile);
      } catch (IllegalArgumentException e) {
        details.addOverallError("Invalid message profile selected.");
        request.setAttribute(ATTRIBUTE_LOAD_DETAILS, details);
        return;
      }
    }
    
    SchoolRosterLoader loader = new SchoolRosterCsvLoader();
    loader.load(details, file.getInputStream());
    
    for (Student student : details.getStudents()) {
      StudentLoad load;
      LineDetails lineDetails = new LineDetails();
      try {
        load = SchoolRosterHL7Generator.generate(student, profile);
        load.setSent(LocalDateTime.now());
        
        String response = connector.submitMessage(load.getVxu(), false);
        load.setAck(response);
        
        AckAnalyzer ackAnalyzer = new AckAnalyzer(load.getAck(), connector.getAckType());
        load.setAckPositive(ackAnalyzer.isPositive());
        
        boolean hasWarning = messageHasWarningErrSegment(load.getAck());
        
        if (load.isAckPositive()) {
          lineDetails.addMessage(
            "HL7 sent and ACK was accepted"
            + (hasWarning ? " with warnings." : "."));
        } else {
          lineDetails.setError(true);
          lineDetails.addMessage(
            "HL7 sent but ACK was rejected"
            + (hasWarning ? " with warning. " : ". ")
            + ackAnalyzer.getSetupProblemDescription());
        }
        
        details.getStudentLoads().add(load);
      } catch (Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        lineDetails.setError(true);
        lineDetails.addMessage(sw.toString());
      }
      
      details.getHl7Details().put(student.getLoadLine(), lineDetails);
    }
    
    details.setEndTime(LocalDateTime.now());
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
  
  private static boolean messageHasWarningErrSegment(String ack) {
    boolean foundError = false;
    HL7Reader hl7Reader = new HL7Reader(ack);
    while (hl7Reader.advanceToSegment("ERR")) {
      if ("W".equals(hl7Reader.getValue(4))) {
        foundError = true;
      }
    }
    return foundError;
  }
}