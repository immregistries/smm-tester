package org.immregistries.smm.roster.model;

import java.util.List;
import java.util.Objects;

public class LineDetails {
  
  private boolean error;
  private List<String> messages;
  
  @Override
  public String toString() {
    return "LineDetails [error=" + error + ", messages=" + messages + "]";
  }
  
  @Override
  public int hashCode() {
    return Objects.hash(Boolean.valueOf(error), messages);
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
    LineDetails other = (LineDetails) obj;
    return error == other.error && Objects.equals(messages, other.messages);
  }
  
  public LineDetails(List<String> errors) {
    this.error = true;
    this.messages = errors;
  }
  
  public LineDetails(String message) {
    this.error = false;
    this.messages = List.of(message);
  }
  
  public boolean isError() {
    return error;
  }
  
  public List<String> getMessages() {
    return messages;
  }
  
  public void setError(boolean error) {
    this.error = error;
  }
  
  public void setMessages(List<String> messages) {
    this.messages = messages;
  }
}
