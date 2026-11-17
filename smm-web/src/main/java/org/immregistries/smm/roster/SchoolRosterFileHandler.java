package org.immregistries.smm.roster;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Locale;
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
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.HL7Reader;
import org.immregistries.smm.workspace.Workspace;

public class SchoolRosterFileHandler {
  
  private String username;
  private Connector connector;
  private Workspace workspace;
  private MessageProfile profile;
  
  public SchoolRosterFileHandler(
      String username,
      Connector connector,
      Workspace workspace,
      MessageProfile profile) {
    
    this.username = username;
    this.connector = connector;
    this.workspace = workspace;
    this.profile = profile;
  }
  
  public LoadDetails handle(InputStream inputStream, String filename) throws IOException {
    LoadDetails details = new LoadDetails(username);
    
    if (connector == null) {
      details.addOverallError("Please select a Connection.");
      return details;
    }
    
    if (inputStream == null || StringUtils.isBlank(filename)) {
      details.addOverallError("Please upload a CSV file.");
      return details;
    }
    
    if (!filename.toLowerCase(Locale.ROOT).endsWith(".csv")) {
      details.addOverallError("Only CSV files are allowed.");
      return details;
    }
    
    details.setFilename(filename);
    
    byte[] rosterBytes = inputStream.readAllBytes();
    try (InputStream rosterStream = new ByteArrayInputStream(rosterBytes)) {
      SchoolRosterLoader loader = new SchoolRosterCsvLoader();
      loader.load(details, rosterStream);
    }
    
    for (Student student : details.getStudents()) {
      sendStudentMessage(details, connector, profile, student);
    }
    
    details.setEndTime(LocalDateTime.now());
    
    Path rosterDir = createUniqueRosterDirectory(rosterBytes, filename, workspace);
    
    if (rosterDir != null) {
      details.getLogDetails().setLogDirectory(rosterDir);
      details.getLogDetails().setOutputLog(SchoolRosterLogger.writeOutputLog(rosterDir, details));
      details.getLogDetails().setHl7VxuLog(SchoolRosterLogger.writeHL7Log(rosterDir, details, true));
      details.getLogDetails().setHl7AckLog(SchoolRosterLogger.writeHL7Log(rosterDir, details, false));
    }
    
    return details;
  }
  
  private void sendStudentMessage(
      LoadDetails details,
      Connector connector,
      MessageProfile profile,
      Student student) {
    
    LineDetails lineDetails = new LineDetails();
    try {
      StudentLoad load = SchoolRosterHL7Generator.generate(student, profile);
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
  
  private Path createUniqueRosterDirectory(
      byte[] rosterBytes,
      String filename,
      Workspace workspace) throws IOException {
    
    if (workspace == null) {
      return null;
    }
    
    Path schoolRosterDir = createSchoolRosterDirectory(workspace);
    
    String filenameNoExtension = filename;
    if (filenameNoExtension.contains(".")) {
      filenameNoExtension = filenameNoExtension.substring(0, filenameNoExtension.indexOf("."));
    }
    
    String rosterDirName = filenameNoExtension + "-" + System.currentTimeMillis();
    Path uniqueUploadDir = schoolRosterDir.resolve(rosterDirName);
    Path targetFile = uniqueUploadDir.resolve(filename);
    Files.createDirectories(uniqueUploadDir);
    
    // copy uploaded file to new unique directory
    try (var out = Files.newOutputStream(targetFile)) {
      out.write(rosterBytes);
    }
    
    return uniqueUploadDir;
  }
  
  public static Path createSchoolRosterDirectory(Workspace workspace) throws IOException {
    if (workspace == null || workspace.getDirectory() == null) {
      return null;
    }
    
    File rosterDirectory = new File(workspace.getDirectory(), "school-roster");
    if (!rosterDirectory.exists() && !rosterDirectory.mkdirs()) {
      throw new IOException("Unable to create workspace school-roster directory: " + rosterDirectory.getAbsolutePath());
    }
    
    return rosterDirectory.toPath();
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