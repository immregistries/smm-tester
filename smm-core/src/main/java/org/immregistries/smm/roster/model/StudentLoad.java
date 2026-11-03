package org.immregistries.smm.roster.model;

import java.time.LocalDateTime;
import java.util.Objects;

public class StudentLoad {

  private Student student;
  private String vxu;
  private String ack;
  private String id;
  private LocalDateTime created;
  private LocalDateTime sent;
  private boolean ackPositive;
  
  @Override
  public int hashCode() {
    return Objects.hash(ack, Boolean.valueOf(ackPositive), created, id, sent, student, vxu);
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
    StudentLoad other = (StudentLoad) obj;
    return
      Objects.equals(ack, other.ack)
      && ackPositive == other.ackPositive
      && Objects.equals(created, other.created)
      && Objects.equals(id, other.id)
      && Objects.equals(sent, other.sent)
      && Objects.equals(student, other.student)
      && Objects.equals(vxu, other.vxu);
  }
  
  public Student getStudent() {
    return student;
  }
  
  public void setStudent(Student student) {
    this.student = student;
  }
  
  public String getVxu() {
    return vxu;
  }
  
  public void setVxu(String vxu) {
    this.vxu = vxu;
  }
  
  public String getAck() {
    return ack;
  }
  
  public void setAck(String ack) {
    this.ack = ack;
  }
  
  public String getId() {
    return id;
  }
  
  public void setId(String id) {
    this.id = id;
  }
  
  public LocalDateTime getCreated() {
    return created;
  }
  
  public void setCreated(LocalDateTime created) {
    this.created = created;
  }
  
  public LocalDateTime getSent() {
    return sent;
  }
  
  public void setSent(LocalDateTime sent) {
    this.sent = sent;
  }
  
  public boolean isAckPositive() {
    return ackPositive;
  }
  
  public void setAckPositive(boolean ackPositive) {
    this.ackPositive = ackPositive;
  }
}
