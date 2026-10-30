package org.immregistries.smm.roster.model;

public enum Grade {
  
  PRE_KINDERGARTEN("Pre-K", "Pre-Kindergarten"),
  KINDERGARTEN("K", "Kindergarten"),
  FIRST("1", "First grade"),
  SECOND("2", "Second grade"),
  THIRD("3", "Third grade"),
  FOURTH("4", "Fourth grade"),
  FIFTH("5", "Fifth grade"),
  SIXTH("6", "Sixth grade"),
  SEVENTH("7", "Seventh grade"),
  EIGHTH("8", "Eighth grade"),
  NINTH("9", "Ninth grade"),
  TENTH("10", "Tenth grade"),
  ELEVENTH("11", "Eleventh grade"),
  TWELFTH("12", "Twelfth grade"),
  UNGRADED("U", "Ungraded"),
  UNKNOWN("?", "Unknown");
  
  private String code;
  private String label;
  
  private Grade(String code, String label) {
    this.code = code;
    this.label = label;
  }
  
  public String getCode() {
    return code;
  }
  
  public String getLabel() {
    return label;
  }
}
