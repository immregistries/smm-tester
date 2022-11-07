package org.immregistries.smm.transform.procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.tester.transform.Patient;
import org.immregistries.smm.transform.PatientType;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AnonymizeAndUpdateRecord extends ProcedureCommon implements ProcedureInterface {

  private static final int MS_IN_DAY = 24 * 60 * 60 * 1000;

  private Date messageDate;
  private int daysToAdd;
  private Date today;
  private boolean isGirl = true;
  private Transformer transformer;
  private Patient patient;
  private int orcCount = 0;

  private static String asOfDate = null;

  protected static void setAsOfDate(String asOfDate) {
    AnonymizeAndUpdateRecord.asOfDate = asOfDate;
  }

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }

  protected AnonymizeAndUpdateRecord(String asOfDate) {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    try {
      today = sdf.parse(asOfDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public AnonymizeAndUpdateRecord() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    try {
      if (asOfDate == null) {
        today = sdf.parse(sdf.format(new Date()));
      } else {
        today = sdf.parse(asOfDate);
      }
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    try {
      patient = transformer.setupPatient(PatientType.BABY);
    } catch (Throwable e) {
      e.printStackTrace();
    }
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      init();
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("MSH")) {
          determineMessageDate(fields);
          shiftDate(fields, 7);
        } else if (segmentName.equals("PID")) {
          shiftDate(fields, 7);
          update(fields, 3, 1, patient.getMedicalRecordNumber());
          isGirl = !readValue(fields, 8).equals("M");
          updateName(fields, 5, 1, patient.getLastName());
          updateName(fields, 5, 2, isGirl ? patient.getGirlName() : patient.getBoyName());
          updateName(fields, 5, 3,
              isGirl ? patient.getMiddleNameGirl() : patient.getMiddleNameBoy());
          updateName(fields, 6, 1, patient.getMotherMaidenName());
          updateName(fields, 6, 2, patient.getMotherName());
          update(fields, 11, 1, patient.getStreet());
          update(fields, 11, 2, patient.getStreet2());
          update(fields, 11, 3, patient.getCity());
          update(fields, 11, 4, patient.getState());
          update(fields, 11, 5, patient.getZip());
          {
            String[] repeatFields = readRepeats(fields, 13);
            String fieldFinal = "";
            for (int i = 0; i < repeatFields.length; i++) {
              String contactMethod = readRepeatValue(repeatFields[i], 2);
              String contactType = readRepeatValue(repeatFields[i], 3);
              if (contactType.equalsIgnoreCase("PH")) {
                updateRepeatValue(patient.getPhoneArea(), repeatFields, i, 6);
                updateRepeatValue(patient.getPhoneLocal(), repeatFields, i, 7);
              } else if (contactType.equalsIgnoreCase("Internet")
                  || contactMethod.equalsIgnoreCase("NET")) {
                updateRepeatValue(patient.getEmail(), repeatFields, i, 4);
              }
              if (i > 0) {
                fieldFinal += "~";
              }
              fieldFinal += repeatFields[i];
            }
            updateContent(fieldFinal, fields, 13);
          }

        } else if (segmentName.equals("PD1")) {
          shiftDate(fields, 13);
          shiftDate(fields, 17);
          shiftDate(fields, 18);
        } else if (segmentName.equals("NK1")) {
          String relationship = readValue(fields, 3);
          if (relationship.equals("MTH") || relationship.equals("GRD") || relationship.equals("")) {
            updateName(fields, 2, 1, patient.getLastName());
            updateName(fields, 2, 2, patient.getMotherName());
            updateName(fields, 2, 2, patient.getMotherMaidenName());
          } else if (relationship.equals("FTH")) {
            updateName(fields, 2, 1, patient.getLastName());
            updateName(fields, 2, 2, patient.getFatherName());
            updateName(fields, 2, 2, patient.getDifferentLastName());
          }
          update(fields, 4, 1, patient.getStreet());
          update(fields, 4, 2, patient.getStreet2());
          update(fields, 4, 3, patient.getCity());
          update(fields, 4, 4, patient.getState());
          update(fields, 4, 5, patient.getZip());
          update(fields, 5, 6, patient.getPhoneArea());
          update(fields, 5, 7, patient.getPhoneLocal());
        } else if (segmentName.equals("ORC")) {
          orcCount++;
          update(fields, 2, 1, "A" + patient.getMedicalRecordNumber() + "." + orcCount);
          update(fields, 3, 1, "B" + patient.getMedicalRecordNumber() + "." + orcCount);
        } else if (segmentName.equals("RXA")) {
          shiftDate(fields, 3);
          shiftDate(fields, 4);
          shiftDate(fields, 16);
        } else if (segmentName.equals("OBX")) {
          shiftDate(fields, 14);
          String obsType = readValue(fields, 3);
          if (obsType.equals("29769-7")) {
            shiftDate(fields, 5);
          }
        }

      }
    }

    putMessageBackTogether(transformRequest, fieldsList);
  }

  private void update(String[] fields, int fieldPos, int subPos, String newName) {
    String prevName = readValue(fields, fieldPos, subPos);
    if (!prevName.equals("")) {
      updateValue(newName, fields, fieldPos, subPos);
    }
  }

  private void updateName(String[] fields, int fieldPos, int subPos, String newName) {
    String prevName = readValue(fields, fieldPos, subPos);
    if (!prevName.equals("")) {
      if (prevName.length() < 3) {
        if (newName.length() >= 3) {
          newName = newName.substring(0, prevName.length());
        }
      }
      updateValue(newName, fields, fieldPos, subPos);
    }
  }

  private void shiftDate(String[] fields, int pos) {
    if (fields.length > pos) {
      updateValue(shiftDate(readValue(fields, pos)), fields, pos);
    }
  }

  private String shiftDate(String originalDate) {
    if (originalDate.length() >= 8) {
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date date = sdf.parse(originalDate.substring(0, 8));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
        return sdf.format(calendar.getTime()) + originalDate.substring(8);
      } catch (ParseException e) {
        // ignore
      }
    }
    return originalDate;
  }

  private void determineMessageDate(String[] fields) {
    String value = readValue(fields, 7);

    if (value.length() >= 8) {
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        messageDate = sdf.parse(value.substring(0, 8));

        if (today.after(messageDate)) {
          daysToAdd = (int) ((today.getTime() - messageDate.getTime()) / MS_IN_DAY);
          daysToAdd--; // Don't set date to today, set it to yesterday so time
                       // doesn't end in the future
        }
      } catch (ParseException e) {
        // ignore
      }
    }
  }

  private void init() {
    messageDate = new Date();
    daysToAdd = 0;
  }



}
