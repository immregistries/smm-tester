package org.immregistries.smm.roster.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.immregistries.smm.roster.model.LineDetails;
import org.immregistries.smm.roster.model.LoadDetails;
import org.immregistries.smm.roster.model.Student;
import org.immregistries.smm.tester.manager.CsvReader;

public class SchoolRosterCsvLoader implements SchoolRosterLoader {

  @Override
  public LoadDetails load(InputStream inputStream) throws IOException {
    LoadDetails details = new LoadDetails();
    
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String line = br.readLine();
      
      if (line == null) {
        details.setOverallErrors(List.of("File is empty."));
        return details;
      }
      
      List<String> headers = CsvReader.readValuesFromCsv(line);
      List<String> headerErrors = new ArrayList<>();
      
      int idColumn = CsvReader.findPosition(COLUMN_ID, headers);
      int nameColumn = CsvReader.findPosition(COLUMN_NAME, headers);
      int gradeColumn = CsvReader.findPosition(COLUMN_GRADE, headers);
      
      if (idColumn == -1) {
        headerErrors.add("Header line missing 'ID' column.");
      }
      if (nameColumn == -1) {
        headerErrors.add("Header line missing 'Name' column.");
      }
      if (gradeColumn == -1) {
        headerErrors.add("Header line missing 'Grade' column.");
      }
      if (!headerErrors.isEmpty()) {
        details.setOverallErrors(headerErrors);
        return details;
      }
      
      List<Student> students = new ArrayList<>();
      int lineNumber = 1;
      
      while ((line = br.readLine()) != null) {
        lineNumber++;
        List<String> values = CsvReader.readValuesFromCsv(line);
        Student student = new Student();
        student.setId(CsvReader.readValue(idColumn, values));
        student.setName(CsvReader.readValue(nameColumn, values));
        student.setGrade(CsvReader.readValue(gradeColumn, values));
        
        List<String> lineErrors = StudentValidator.validate(student);
        if (lineErrors == null || lineErrors.isEmpty()) {
          students.add(student);
          details.getLineDetails().put(
            lineNumber,
            new LineDetails(
              "Student read successfully - "
              + student.getName()
              + " ["
              + student.getId()
              + ", Grade: "
              + student.getGrade()
              + "]."));
        } else {
          details.getLineDetails().put(lineNumber, new LineDetails(lineErrors));
        }
      }
      
      details.setStudents(students);
    }
    
    return details;
  }
}
