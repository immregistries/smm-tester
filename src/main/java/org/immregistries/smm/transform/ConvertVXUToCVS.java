package org.immregistries.smm.transform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.immregistries.smm.transform.procedure.ProcedureCommon;

public class ConvertVXUToCVS {
  private static final String FILENAME_IN =
      "C:\\dev\\immregistries\\smm-tester\\Examples.Adult.1000000.hl7.txt";
  private static final String FILENAME_OUT =
      "C:\\dev\\immregistries\\smm-tester\\Examples.Adult.1000000.csv";

  public static void main(String[] args) throws IOException {
    PrintWriter out = new PrintWriter(new FileWriter(new File(FILENAME_OUT)));
    try {
      printHeader(out);
      File myObj = new File(FILENAME_IN);
      Scanner myReader = new Scanner(myObj);
      String message = "";
      int count = 0;
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        if (data.startsWith("MSH")) {
          count += 1;
          handleMessage(message, out);
          message = data + "\r";
        } else {
          message += data + "\r";
        }
        //System.out.println(data);
      }
      handleMessage(message, out);
      System.out.println(count);
      myReader.close();
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    finally {
      out.close();
    }
  }

  private static void printHeader(PrintWriter out) {
    String line = "";
    line = add("patientId", line);
    line = add("firstName", line);
    line = add("middleName", line);
    line = add("lastName", line);
    line = add("dateOfBirth", line);
    line = add("gender", line);
    line = add("streetAddress", line);
    line = add("city", line);
    line = add("state", line);
    line = add("zip", line);
    line = add("telephoneNumber", line);
    line = add("vaccinationId", line);
    line = add("vaccineTypeCVX", line);
    line = add("administrationDate", line);
    line = add("manufacturer", line);
    line = add("lotNumber", line);
    line = add("expirationDate", line);
    line = add("doseVolume", line);
    line = add("recordCreationDate", line);
    
    out.println(line);
  }

  private static void handleMessage(String message, PrintWriter out) throws IOException {
    if (!message.equals("")) {
      
      List<String> messageList = new ArrayList<>();
      {
        String top = "";
        String bottom = "";
        boolean readingTop = true; 
        BufferedReader inResult = new BufferedReader(new StringReader(message));
        String lineResult;
        while ((lineResult = inResult.readLine()) != null) {
          if(lineResult.startsWith("ORC")) {
            readingTop = false;
            if(!bottom.equals("")) {
              messageList.add(top + bottom);
            }
            bottom = lineResult + "\r";
          }
          else if (readingTop) {
            top += lineResult + "\r";
          }
          else {
            bottom += lineResult + "\r";
          }
        }
        if(!bottom.equals("")) {
          messageList.add(top + bottom);
        }
        inResult.close();
      }
      
      for(String m:messageList) {
      List<String[]> fieldsList = readMessage(m);
      {
        
        String patientId = "";
        String firstName = "";
        String middleName = "";
        String lastName = "";
        String dateOfBirth = "";
        String gender = "";
        String streetAddress = "";
        String city = "";
        String state = "";
        String zip = "";
        String telephoneNumber = "";
        String vaccinationId = "";
        String vaccineTypeCVX = "";
        String administrationDate = "";
        String manufacturer = "";
        String lotNumber = "";
        String expirationDate = "";
        String doseVolume = "";
        String recordCreationDate = "";

        for (String[] fields : fieldsList) {
          String segmentName = fields[0];
          if (segmentName.equals("PID")) {
            patientId = ProcedureCommon.readValue(fields, 3, 1);
            firstName = ProcedureCommon.readValue(fields, 5, 2);
            middleName = ProcedureCommon.readValue(fields, 5, 3);
            lastName = ProcedureCommon.readValue(fields, 5, 1);
            dateOfBirth = ProcedureCommon.readValue(fields, 7, 1);
            gender = ProcedureCommon.readValue(fields, 8, 1);
            streetAddress = ProcedureCommon.readValue(fields, 11, 1);
            city = ProcedureCommon.readValue(fields, 11, 3);
            state = ProcedureCommon.readValue(fields, 11, 4);
            zip = ProcedureCommon.readValue(fields, 11, 5);
            telephoneNumber = ProcedureCommon.readValue(fields, 13, 6) + ProcedureCommon.readValue(fields, 13, 7);
          }
          else if (segmentName.equals("RXA")) {
            vaccineTypeCVX = ProcedureCommon.readValue(fields, 5, 1);
            administrationDate = ProcedureCommon.readValue(fields, 3, 1);
            manufacturer = ProcedureCommon.readValue(fields, 17, 1);
            lotNumber = ProcedureCommon.readValue(fields, 15, 1);
            expirationDate = ProcedureCommon.readValue(fields, 16, 1);
            doseVolume = ProcedureCommon.readValue(fields, 6, 1);
            recordCreationDate = ProcedureCommon.readValue(fields, 22, 1);
          }
          else if (segmentName.equals("ORC")) {
           vaccinationId = ProcedureCommon.readValue(fields, 3, 1);
          }
        }
        String line = "";
        line = add(patientId, line);
        line = add(firstName, line);
        line = add(middleName, line);
        line = add(lastName, line);
        line = add(dateOfBirth, line);
        line = add(gender, line);
        line = add(streetAddress, line);
        line = add(city, line);
        line = add(state, line);
        line = add(zip, line);
        line = add(telephoneNumber, line);
        line = add(vaccinationId, line);
        line = add(vaccineTypeCVX, line);
        line = add(administrationDate, line);
        line = add(manufacturer, line);
        line = add(lotNumber, line);
        line = add(expirationDate, line);
        line = add(doseVolume, line);
        line = add(recordCreationDate, line);
        
        out.println(line);
      }
      }
    }
  }

  private static String add(String field, String line) {
    line += '"' + field + '"' + ",";
    return line;
  }

  private static List<String[]> readMessage(String message) throws IOException {
    BufferedReader inResult = new BufferedReader(new StringReader(message));
    String lineResult;
    List<String[]> fieldsList = new ArrayList<String[]>();
    while ((lineResult = inResult.readLine()) != null) {
      lineResult = lineResult.trim();
      String[] fields = lineResult.split("\\|");
      if (fields.length > 0) {
        fieldsList.add(fields);
      }
    }
    inResult.close();
    return fieldsList;
  }
}
