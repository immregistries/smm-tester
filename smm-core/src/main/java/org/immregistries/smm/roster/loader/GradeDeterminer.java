package org.immregistries.smm.roster.loader;

import org.apache.commons.lang3.StringUtils;
import org.immregistries.smm.roster.model.Grade;

public class GradeDeterminer {
  
  private GradeDeterminer() {}
  
  public static Grade determine(String input) {
    if (StringUtils.isBlank(input)) {
      return Grade.UNKNOWN;
    }
    
    String normalized = input.trim();
    
    if ("U".equalsIgnoreCase(normalized) || "ungraded".equalsIgnoreCase(normalized)) {
      return Grade.UNGRADED;
    }
    
    if ("PK".equalsIgnoreCase(normalized) || normalized.toLowerCase().startsWith("pre")) {
      return Grade.PRE_KINDERGARTEN;
    }
    
    if ("K".equalsIgnoreCase(normalized)
        || "KINDERGARTEN".equalsIgnoreCase(normalized)
        || normalized.toLowerCase().startsWith("kin")) {
      return Grade.KINDERGARTEN;
    }
    
    if ("1".equals(normalized)
        || normalized.toLowerCase().startsWith("1st")
        || normalized.toLowerCase().startsWith("first")) {
      return Grade.FIRST;
    }
    
    if ("2".equals(normalized)
        || normalized.toLowerCase().startsWith("2nd")
        || normalized.toLowerCase().startsWith("second")) {
      return Grade.SECOND;
    }
    
    if ("3".equals(normalized)
        || normalized.toLowerCase().startsWith("3rd")
        || normalized.toLowerCase().startsWith("third")) {
      return Grade.THIRD;
    }
    
    if ("4".equals(normalized)
        || normalized.toLowerCase().startsWith("4th")
        || normalized.toLowerCase().startsWith("fourth")) {
      return Grade.FOURTH;
    }
    
    if ("5".equals(normalized)
        || normalized.toLowerCase().startsWith("5th")
        || normalized.toLowerCase().startsWith("fifth")) {
      return Grade.FIFTH;
    }
    
    if ("6".equals(normalized)
        || normalized.toLowerCase().startsWith("6th")
        || normalized.toLowerCase().startsWith("sixth")) {
      return Grade.SIXTH;
    }
    
    if ("7".equals(normalized)
        || normalized.toLowerCase().startsWith("7th")
        || normalized.toLowerCase().startsWith("seventh")) {
      return Grade.SEVENTH;
    }
    
    if ("8".equals(normalized)
        || normalized.toLowerCase().startsWith("8th")
        || normalized.toLowerCase().startsWith("eighth")) {
      return Grade.EIGHTH;
    }
    
    if ("9".equals(normalized)
        || normalized.toLowerCase().startsWith("9th")
        || normalized.toLowerCase().startsWith("ninth")) {
      return Grade.NINTH;
    }
    
    if ("10".equals(normalized)
        || normalized.toLowerCase().startsWith("10th")
        || normalized.toLowerCase().startsWith("tenth")) {
      return Grade.TENTH;
    }
    
    if ("11".equals(normalized)
        || normalized.toLowerCase().startsWith("11th")
        || normalized.toLowerCase().startsWith("eleventh")) {
      return Grade.ELEVENTH;
    }
    
    if ("12".equals(normalized)
        || normalized.toLowerCase().startsWith("12th")
        || normalized.toLowerCase().startsWith("twel")) {
      return Grade.TWELFTH;
    }
    
    return Grade.UNKNOWN;
  }
}
