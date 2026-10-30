package org.immregistries.smm.roster.model;

import java.util.Objects;

public class Student {

  private String id;
  private String firstName;
  private String middleName;
  private String lastName;
  private Grade grade;
  
  @Override
  public String toString() {
    return "Student [id=" + id + ", firstName=" + firstName + ", middleName=" + middleName
        + ", lastName=" + lastName + ", grade=" + grade + "]";
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(grade, id, firstName, middleName, lastName);
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
      && Objects.equals(firstName, other.firstName)
      && Objects.equals(middleName, other.middleName)
      && Objects.equals(lastName, other.lastName);
  }
  
  public String getId() {
    return id;
  }
  
  public String getFirstName() {
    return firstName;
  }
  
  public String getMiddleName() {
    return middleName;
  }
  
  public String getLastName() {
    return lastName;
  }
  
  public Grade getGrade() {
    return grade;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public void setMiddleName(String middleName) {
    this.middleName = middleName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public void setGrade(Grade grade) {
    this.grade = grade;
  }
}
