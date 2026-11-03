package org.immregistries.smm.roster.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.smm.roster.model.LineDetails;
import org.immregistries.smm.roster.model.LoadDetails;
import org.immregistries.smm.roster.model.Student;
import org.immregistries.smm.tester.manager.CsvReader;

public class SchoolRosterCsvLoader implements SchoolRosterLoader {

  @Override
  public void load(LoadDetails details, InputStream inputStream) throws IOException {
    
    try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
      String line = br.readLine();
      
      if (line == null) {
        details.setOverallErrors(List.of("File is empty."));
        return;
      }
      
      List<String> headers = CsvReader.readValuesFromCsv(line);
      List<String> headerErrors = new ArrayList<>();
      
      int idColumn = CsvReader.findPosition(COLUMN_ID, headers);
      int firstNameColumn = CsvReader.findPosition(COLUMN_FIRST_NAME, headers);
      int middleNameColumn = CsvReader.findPosition(COLUMN_MIDDLE_NAME, headers);
      int lastNameColumn = CsvReader.findPosition(COLUMN_LAST_NAME, headers);
      int gradeColumn = CsvReader.findPosition(COLUMN_GRADE, headers);
      
      if (idColumn == -1) {
        headerErrors.add("Header line missing 'ID' column.");
      }
      if (firstNameColumn == -1) {
        headerErrors.add("Header line missing 'First' column.");
      }
      if (middleNameColumn == -1) {
        headerErrors.add("Header line missing 'Middle' column.");
      }
      if (lastNameColumn == -1) {
        headerErrors.add("Header line missing 'Last' column.");
      }
      if (gradeColumn == -1) {
        headerErrors.add("Header line missing 'Grade' column.");
      }
      if (!headerErrors.isEmpty()) {
        details.setOverallErrors(headerErrors);
        return;
      }
      
      List<Student> students = new ArrayList<>();
      int lineNumber = 1;
      
      while ((line = br.readLine()) != null) {
        lineNumber++;
        List<String> values = CsvReader.readValuesFromCsv(line);
        Student student = new Student();
        student.setId(CsvReader.readValue(idColumn, values));
        student.setFirstName(CsvReader.readValue(firstNameColumn, values));
        student.setMiddleName(CsvReader.readValue(middleNameColumn, values));
        student.setLastName(CsvReader.readValue(lastNameColumn, values));
        student.setGrade(GradeDeterminer.determine(CsvReader.readValue(gradeColumn, values)));
        student.setLoadLine(lineNumber);
        
        List<String> lineErrors = StudentValidator.validate(student);
        if (lineErrors == null || lineErrors.isEmpty()) {
          students.add(student);
          details.getFileLoadDetails().put(
            lineNumber,
            new LineDetails(
              "Student read successfully - "
              + student.getFirstName()
              + " "
              + (StringUtils.isNotBlank(student.getMiddleName()) ? (student.getMiddleName() + " ") : "")
              + student.getLastName()
              + " ["
              + student.getId()
              + ", Grade: "
              + student.getGrade()
              + "]."));
        } else {
          details.getFileLoadDetails().put(lineNumber, new LineDetails(lineErrors));
        }
      }
      
      details.setStudents(students);
    }
  }
}
