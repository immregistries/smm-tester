package org.immregistries.smm.roster;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.smm.roster.model.LoadDetails;
import org.immregistries.smm.roster.model.StudentLoad;

public class SchoolRosterLogger {
  
  private SchoolRosterLogger() {}
  
  public static Path writeOutputLog(Path rosterDir, LoadDetails details) {
    if (rosterDir == null || details == null) {
      return null;
    }
    
    try {
      Path logFile = rosterDir.resolve("output.log");
      
      StringBuilder sb = new StringBuilder();
      sb.append("Upload started by: ")
        .append(StringUtils.defaultIfBlank(details.getUser(), "unknown"))
        .append('\n');
      sb.append("Upload started at: ")
        .append(details.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
        .append('\n');
      sb.append("CSV lines converted to Students without error: ")
        .append(details.getStudents().size())
        .append('\n');
      sb.append("Students successfully sent to the Connection: ")
        .append((int) details.getStudentLoads().stream()
          .filter(StudentLoad::isAckPositive)
          .count())
        .append('\n');
      sb.append('\n');
      
      sb.append("overallErrors:\n");
      
      if (details.getOverallErrors().isEmpty()) {
        sb.append("  <none>\n");
      } else {
        for (String error : details.getOverallErrors()) {
          sb.append("  - ").append(error).append('\n');
        }
      }
      
      sb.append("\nfileLoadDetails:\n");
      
      if (details.getFileLoadDetails().isEmpty()) {
        sb.append("  <none>\n");
      } else {
        for (var entry : details.getFileLoadDetails().entrySet()) {
          sb.append("  Line ").append(entry.getKey()).append(':').append('\n');
          for (String message : entry.getValue().getMessages()) {
            sb.append("    - ").append(message).append('\n');
          }
        }
      }
      
      sb.append("\nhl7Details:\n");
      
      if (details.getHl7Details().isEmpty()) {
        sb.append("  <none>\n");
      } else {
        for (var entry : details.getHl7Details().entrySet()) {
          sb.append("  Line ").append(entry.getKey()).append(':').append('\n');
          for (String message : entry.getValue().getMessages()) {
            sb.append("    - ").append(message).append('\n');
          }
        }
      }
      
      sb.append("\nauditTimestamp: ")
        .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(details.getEndTime()))
        .append('\n');
      
      Files.writeString(logFile, sb.toString());
      
      return logFile;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
  
  public static Path writeHL7Log(
      Path rosterDir,
      LoadDetails details,
      boolean vxu) throws IOException {
    
    if (rosterDir == null || details == null) {
      return null;
    }
    
    try {
      Path logFile = rosterDir.resolve("hl7-" + (vxu ? "roster-update" : "acknowledgement") + ".log");
      
      StringBuilder sb = new StringBuilder();
      
      for (StudentLoad load : details.getStudentLoads()) {
        sb.append(vxu ? load.getVxu() : load.getAck())
          .append("\n\n");
      }
      
      Files.writeString(logFile, sb.toString());
      
      return logFile;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
