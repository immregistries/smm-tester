package org.immregistries.smm.roster.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class LoadDetails {
  
  private List<Student> students = new ArrayList<>();
  private Map<Integer, LineDetails> lineDetails = new TreeMap<>();
  private List<String> overallErrors = new ArrayList<>();
  
  public LoadDetails() {}
  
  public LoadDetails(String error) {
    this.overallErrors = List.of(error);
  }
  
  @Override
  public String toString() {
    return "LoadDetails [students=" + students + ", lineDetails=" + lineDetails + ", overallErrors="
        + overallErrors + "]";
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(lineDetails, overallErrors, students);
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
    return Objects.equals(lineDetails, other.lineDetails)
        && Objects.equals(overallErrors, other.overallErrors)
        && Objects.equals(students, other.students);
  }
  
  public List<Student> getStudents() {
    return students;
  }
  
  public Map<Integer, LineDetails> getLineDetails() {
    return lineDetails;
  }
  
  public List<String> getOverallErrors() {
    return overallErrors;
  }
  
  public void setStudents(List<Student> students) {
    this.students = students;
  }
  
  public void setLineDetail(Map<Integer, LineDetails> lineDetails) {
    this.lineDetails = lineDetails;
  }
  
  public void setOverallErrors(List<String> overallErrors) {
    this.overallErrors = overallErrors;
  }
}
