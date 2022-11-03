package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class DateOfBirthMonthDaySwap extends ProcedureCommon implements ProcedureInterface {



  public DateOfBirthMonthDaySwap() {

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
        if (day > 12) {
          // If the day is too high then we have to move the date of birth back in time 
          // to a day of the month that is 12 or lower. We will shift it back and then 
          // adjust the date so that it is still accurate.
          if (day > 24) {
            month = month - 2;
            day = day - 24;
          } else {
            month = month - 1;
            day = day - 12;
          }
          if (month < 1) {
            // month has decremented into next year
            month = month + 12;
            year = year - 1;
          }
        } else {
          // We are good to do a switch of dates
          int temp = month;
          month = day;
          day = temp;
        }
        dob = "" + year;
        if (month < 10) {
          dob += "0" + month;
        } else {
          dob += month;
        }
        if (day < 10) {
          dob += "0" + day;
        } else {
          dob += day;
        }
      }
    }

    return dob;
  }



  public void setTransformer(Transformer transformer) {}



}
