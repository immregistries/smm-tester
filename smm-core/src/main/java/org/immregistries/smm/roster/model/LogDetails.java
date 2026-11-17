package org.immregistries.smm.roster.model;

import java.nio.file.Path;

public class LogDetails {
  
  private Path logDirectory;
  private Path outputLog;
  private Path hl7VxuLog;
  private Path hl7AckLog;
  
  public Path getLogDirectory() {
    return logDirectory;
  }
  
  public void setLogDirectory(Path logDirectory) {
    this.logDirectory = logDirectory;
  }
  
  public Path getOutputLog() {
    return outputLog;
  }
  
  public void setOutputLog(Path outputLog) {
    this.outputLog = outputLog;
  }
  
  public Path getHl7AckLog() {
    return hl7AckLog;
  }
  
  public void setHl7AckLog(Path hl7AckLog) {
    this.hl7AckLog = hl7AckLog;
  }
  
  public Path getHl7VxuLog() {
    return hl7VxuLog;
  }
  
  public void setHl7VxuLog(Path hl7VxuLog) {
    this.hl7VxuLog = hl7VxuLog;
  }
}
