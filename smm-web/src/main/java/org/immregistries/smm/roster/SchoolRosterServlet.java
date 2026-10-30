package org.immregistries.smm.roster;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.immregistries.smm.roster.hl7.SchoolRosterHL7Generator;
import org.immregistries.smm.roster.hl7.SchoolRosterHL7Generator.MessageProfile;
import org.immregistries.smm.roster.loader.SchoolRosterCsvLoader;
import org.immregistries.smm.roster.loader.SchoolRosterLoader;
import org.immregistries.smm.roster.model.LineDetails;
import org.immregistries.smm.roster.model.LoadDetails;
import org.immregistries.smm.roster.model.Student;
import org.immregistries.smm.roster.model.StudentLoad;
import org.immregistries.smm.tester.ClientServlet;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
      printHtmlHead(out, MENU_HEADER_HOME, request);
      
      printPageHeader(
        out,
        "School Roster Upload",
        "Upload a school roster file and convert to HL7.");
      
      out.println(
      """
        <div class="aira-stack">
          <section class="aira-panel">
            <div class="aira-panel__body">
              <form
                  method="post"
                  enctype="multipart/form-data">
                <label for="PARAM_FILE">
                  Choose a file:
                </label>
                <br><br>
                <input
                  id="file"
                  type="file"
                  name="PARAM_FILE"
                  accept=".csv,text/csv">
                <br><br>
                <label for="messageProfile">
                  Message profile:
                </label>
                <br><br>
                <select id="messageProfile" name="PARAM_MESSAGE_PROFILE">
                  MESSAGE_PROFILE_OPTIONS
                </select>
                <br><br>
                <input
                  type="submit"
                  value="Upload">
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
        
        if (!details.getLineDetails().isEmpty()) {
          out.println("<p>Line Information</p><ul>");
          for (Entry<Integer, LineDetails> entry : details.getLineDetails().entrySet()) {
            out.println(
              "<li>Line "
              + entry.getKey()
              + ": "
              + entry.getValue().getMessages().stream().collect(Collectors.joining(" "))
              + "</li>");
          }
          out.println("</ul>");
        }
        
        out.println("</div>");
      }
    } finally {
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
    // Retrieve the uploaded file from the request
    Part file = request.getPart(PARAM_FILE);
    String filename = getSubmittedFileName(file);
    
    String submittedName = getSubmittedFileName(file);
    
    if (file == null || file.getSize() == 0 || submittedName == null) {
      request.setAttribute(
        ATTRIBUTE_LOAD_DETAILS,
        new LoadDetails("Please upload a CSV file."));
      return;
    }
    
    if (!submittedName.toLowerCase(Locale.ROOT).endsWith(".csv")) {
      request.setAttribute(
        ATTRIBUTE_LOAD_DETAILS,
        new LoadDetails("Only CSV files are allowed."));
      return;
    }
    
    MessageProfile profile = MessageProfile.COMPATIBLE_BASELINE;
    String selectedProfile = request.getParameter(PARAM_MESSAGE_PROFILE);
    if (selectedProfile != null && !selectedProfile.isBlank()) {
      try {
        profile = MessageProfile.valueOf(selectedProfile);
      } catch (IllegalArgumentException e) {
        request.setAttribute(
          ATTRIBUTE_LOAD_DETAILS,
          new LoadDetails("Invalid message profile selected."));
        return;
      }
    }
    
    System.out.println("greg filename = " + filename);
    
    SchoolRosterLoader loader = new SchoolRosterCsvLoader();
    LoadDetails details = loader.load(file.getInputStream());
    
    System.out.println("greg student count = " + details.getStudents().size());
    System.out.println("greg details = " + details);
    
    for (Student student : details.getStudents()) {
      StudentLoad load = SchoolRosterHL7Generator.generate(student, profile);
      System.out.println("Selected profile: " + profile.getFriendly() + "\n" + load.getVxu());
    }
    
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
}