package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class DateOfBirthRectify extends ProcedureCommon implements ProcedureInterface {



  public DateOfBirthRectify() {

  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("PID")) {
          int fieldPos = 7;
          int subPos = 1;
          String dob = readValue(fields, fieldPos, subPos);
          dob = varyDate(dob);
          updateValue(dob, fields, fieldPos, subPos);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyDate(String dob) {
    if (dob.length() >= 8) {
      if (dob.length() > 8) {
        dob = dob.substring(0, 8);
      }
      int year = 0;
      int month = 0;
      int day = 0;
      try {
        year = Integer.parseInt(dob.substring(0, 4));
        month = Integer.parseInt(dob.substring(4, 6));
        day = Integer.parseInt(dob.substring(6, 8));
      } catch (NumberFormatException nfe) {
        day = 0;
      }

      if (day > 0 && month > 0 && year > 1900) {
        if (day < 3) {
          // don't leave day on the first two days of the month, move back to the previous month
          month = month - 1;
          if (month < 1) {
            // month has decremented into next year
            month = month + 12;
            year = year - 1;
          }
          day = 28 - (day - 1);
        } else if (day > 28) {
          // don't leave the date on the days that don't work on all months, move backwards into month
          day = 28 - (31 - day);
        }
        dob = createDate(year, month, day);
      }
    }
    return dob;
  }



  public void setTransformer(Transformer transformer) {}



}
