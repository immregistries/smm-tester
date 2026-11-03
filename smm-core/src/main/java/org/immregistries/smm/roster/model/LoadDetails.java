package org.immregistries.smm.roster.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import org.apache.commons.lang3.StringUtils;

public class LoadDetails {
  
  private String user;
  private String filename;
  private LocalDateTime startTime;
  private LocalDateTime endTime;
  
  private List<Student> students = new ArrayList<>();
  private Map<Integer, LineDetails> fileLoadDetails = new TreeMap<>();
  private Map<Integer, LineDetails> hl7Details = new TreeMap<>();
  private List<String> overallErrors = new ArrayList<>();
  private List<StudentLoad> studentLoads = new ArrayList<>();
  
  public LoadDetails(String user) {
    this.user = user;
    this.startTime = LocalDateTime.now();
  }
  
  public void addOverallError(String error) {
    if (StringUtils.isNotBlank(error)) {
      overallErrors.add(error);
    }
  }
  
  @Override
  public String toString() {
    return "LoadDetails [user=" + user + ", filename=" + filename + ", startTime=" + startTime
        + ", endTime=" + endTime + ", students=" + students + ", fileLoadDetails=" + fileLoadDetails
        + ", hl7Details=" + hl7Details + ", overallErrors=" + overallErrors + ", studentLoads="
        + studentLoads + "]";
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(endTime, fileLoadDetails, filename, hl7Details, overallErrors, startTime,
        studentLoads, students, user);
  }
  
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    LoadDetails other = (LoadDetails) obj;
    return Objects.equals(endTime, other.endTime)
        && Objects.equals(fileLoadDetails, other.fileLoadDetails)
        && Objects.equals(filename, other.filename) && Objects.equals(hl7Details, other.hl7Details)
        && Objects.equals(overallErrors, other.overallErrors)
        && Objects.equals(startTime, other.startTime)
        && Objects.equals(studentLoads, other.studentLoads)
        && Objects.equals(students, other.students) && Objects.equals(user, other.user);
  }
  
  public String getUser() {
    return user;
  }
  
  public String getFilename() {
    return filename;
  }
  
  public LocalDateTime getStartTime() {
    return startTime;
  }
  
  public LocalDateTime getEndTime() {
    return endTime;
  }
  
  public List<Student> getStudents() {
    return students;
  }
  
  public Map<Integer, LineDetails> getFileLoadDetails() {
    return fileLoadDetails;
  }
  
  public List<String> getOverallErrors() {
    return overallErrors;
  }
  
  public List<StudentLoad> getStudentLoads() {
    return studentLoads;
  }
  
  public Map<Integer, LineDetails> getHl7Details() {
    return hl7Details;
  }
  
  public void setUser(String user) {
    this.user = user;
  }
  
  public void setFilename(String filename) {
    this.filename = filename;
  }
  
  public void setStartTime(LocalDateTime startTime) {
    this.startTime = startTime;
  }
  
  public void setEndTime(LocalDateTime endTime) {
    this.endTime = endTime;
  }
  
  public void setStudents(List<Student> students) {
    this.students = students;
  }
  
  public void setFileLoadDetails(Map<Integer, LineDetails> fileLoadDetails) {
    this.fileLoadDetails = fileLoadDetails;
  }
  
  public void setOverallErrors(List<String> overallErrors) {
    this.overallErrors = overallErrors;
  }
  
  public void setStudentLoads(List<StudentLoad> studentLoads) {
    this.studentLoads = studentLoads;
  }
  
  public void setHl7Details(Map<Integer, LineDetails> hl7Details) {
    this.hl7Details = hl7Details;
  }
}
