package org.immregistries.smm.roster.model;

import java.util.Objects;

public class Student {

  private String id;
  private String name;
  private String grade;
  
  @Override
  public String toString() {
    return "Student [id=" + id + ", name=" + name + ", grade=" + grade + "]";
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(grade, id, name);
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
    Student other = (Student) obj;
    return grade == other.grade
      && Objects.equals(id, other.id)
      && Objects.equals(name, other.name);
  }
  
  public String getId() {
    return id;
  }
  
  public String getName() {
    return name;
  }
  
  public String getGrade() {
    return grade;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setGrade(String grade) {
    this.grade = grade;
  }
}
