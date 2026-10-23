package org.immregistries.smm.roster.loader;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.smm.roster.model.Student;

public class StudentValidator {
  
  private StudentValidator() {}
  
  public static List<String> validate(Student student) {
    List<String> errors = new ArrayList<>();
    
    if (student == null) {
      errors.add("Student is missing.");
      return errors;
    }
    
    if (StringUtils.isBlank(student.getId())) {
      errors.add("Student ID is missing.");
    }
    
    if (StringUtils.isBlank(student.getName())) {
      errors.add("Student Name is missing.");
    }
    
    if (StringUtils.isBlank(student.getGrade())) {
      errors.add("Student Grade is missing.");
    }
    
    return errors;
  }
}
