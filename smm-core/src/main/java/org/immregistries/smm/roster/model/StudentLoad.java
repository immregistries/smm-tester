package org.immregistries.smm.roster.model;

import java.time.LocalDateTime;

public class StudentLoad {
  
  private Student student;
  private String vxu;
  private String ack;
  private String id;
  private LocalDateTime created;
  private LocalDateTime sent;
  
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
}
