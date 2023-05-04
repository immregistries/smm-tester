package org.immregistries.smm.transform.procedure;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.text.WordUtils;
import org.immregistries.smm.transform.TransformRequest;

public abstract class ProcedureCommon implements ProcedureInterface {

  protected String readValue(String[] fields, int fieldPos) {
    return readValue(fields, fieldPos, 1);
  }


  protected String readValue(String[] fields, int fieldPos, int subPos) {
    String value = "";
    if (fields[0].equals("MSH") || fields[0].equals("FHS") || fields[0].equals("BHS")) {
      fieldPos--;
    }
    if (fieldPos < fields.length) {
      value = fields[fieldPos];
      {
        int posTilde = value.indexOf("~");
        if (posTilde != -1) {
          value = value.substring(0, posTilde);
        }
      }
      while (subPos > 1) {
        int posCaret = value.indexOf("^");
        if (posCaret != -1) {
          value = value.substring(posCaret + 1);
        }
        subPos--;
      }
      {
        int posCaret = value.indexOf("^");
        if (posCaret != -1) {
          value = value.substring(0, posCaret);
        }
      }
      {
        int posAmpersand = value.indexOf("&");
        if (posAmpersand != -1) {
          value = value.substring(0, posAmpersand);
        }
      }
    }
    return value;
  }

  protected void updateValue(String updateValue, String[] fields, int fieldPos, int subPos) {
    if (fields[0].equals("MSH") || fields[0].equals("FHS") || fields[0].equals("BHS")) {
      fieldPos--;
    }
    if (fieldPos < fields.length) {
      String originalValue = fields[fieldPos];
      int posStart = 0;
      int posEnd = originalValue.length();
      {
        int posTilde = originalValue.indexOf("~");
        if (posTilde != -1) {
          posEnd = posTilde;
        }
      }
      while (subPos > 1) {
        int posCaret = originalValue.indexOf("^", posStart);
        if (posCaret != -1) {
          posStart = posCaret + 1;
        } else {
          originalValue += "^";
          posStart = originalValue.length();
        }
        subPos--;
      }
      {
        int posCaret = originalValue.indexOf("^", posStart);
        if (posCaret != -1 && posCaret < posEnd) {
          posEnd = posCaret;
        }
      }
      {
        int posAmpersand = originalValue.indexOf("&", posStart);
        if (posAmpersand != -1 && posAmpersand < posEnd) {
          posEnd = posAmpersand;
        }
      }
      fields[fieldPos] =
          originalValue.substring(0, posStart) + updateValue + originalValue.substring(posEnd);
    }
  }

  protected List<String[]> readMessage(TransformRequest transformRequest) throws IOException {
    BufferedReader inResult =
        new BufferedReader(new StringReader(transformRequest.getResultText()));
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


  protected void putMessageBackTogether(TransformRequest transformRequest,
      List<String[]> fieldsList) {
    String finalMessage = "";
    for (String[] fields : fieldsList) {
      if (fields[0].length() == 3) {
        finalMessage += fields[0];
        for (int i = 1; i < fields.length; i++) {
          finalMessage += "|" + fields[i];
        }
        finalMessage += transformRequest.getSegmentSeparator();
      }
    }
    transformRequest.setResultText(finalMessage);
  }

  protected void updateRepeatValue(String updateValue, String[] repeatFields, int repeatPos,
      int subPo) {
    String value = readRepeatValue(repeatFields[repeatPos], subPo);
    if (!value.equals("")) {
      updateRepeat(updateValue, repeatFields, repeatPos, subPo);
    }
  }

  protected void updateValue(String updateValue, String[] fields, int fieldPos) {
    updateValue(updateValue, fields, fieldPos, 1);
  }

  protected void updateContent(String contentValue, String[] fields, int fieldPos) {
    if (fields[0].equals("MSH") || fields[0].equals("FHS") || fields[0].equals("BHS")) {
      fieldPos--;
    }
    if (fieldPos < fields.length) {
      fields[fieldPos] = contentValue;
    }
  }

  protected void updateRepeat(String updateValue, String[] repeatFields, int repeatPos,
      int subPos) {
    String originalValue = repeatFields[repeatPos];
    int posStart = 0;
    int posEnd = originalValue.length();
    while (subPos > 1) {
      int posCaret = originalValue.indexOf("^", posStart);
      if (posCaret != -1) {
        posStart = posCaret + 1;
      } else {
        originalValue += "^";
        posStart = originalValue.length();
      }
      subPos--;
    }
    {
      int posCaret = originalValue.indexOf("^", posStart);
      if (posCaret != -1 && posCaret < posEnd) {
        posEnd = posCaret;
      }
    }
    {
      int posAmpersand = originalValue.indexOf("&", posStart);
      if (posAmpersand != -1 && posAmpersand < posEnd) {
        posEnd = posAmpersand;
      }
    }
    repeatFields[repeatPos] =
        originalValue.substring(0, posStart) + updateValue + originalValue.substring(posEnd);
  }


  protected String createRepeatValue(String[] repeatFields) {
    String value = "";
    if (repeatFields.length > 0) {
      value = repeatFields[0];
      for (int i = 1; i < repeatFields.length; i++) {
        value += "~" + repeatFields[i];
      }
    }
    return value;
  }

  protected String[] readRepeats(String[] fields, int fieldPos) {
    if (fields[0].equals("MSH") || fields[0].equals("FHS") || fields[0].equals("BHS")) {
      fieldPos--;
    }
    if (fieldPos < fields.length) {
      return fields[fieldPos].split("\\~");
    }
    return new String[] {""};
  }

  protected static String readRepeatValue(String value, int subPos) {
    while (subPos > 1) {
      int posCaret = value.indexOf("^");
      if (posCaret == -1) {
        // no more repeats, value is missing
        return "";
      }
      value = value.substring(posCaret + 1);
      subPos--;
    }
    {
      int posCaret = value.indexOf("^");
      if (posCaret != -1) {
        value = value.substring(0, posCaret);
      }
    }
    {
      int posAmpersand = value.indexOf("&");
      if (posAmpersand != -1) {
        value = value.substring(0, posAmpersand);
      }
    }
    return value;
  }


  protected static String capitalizeFirst(String namePart) {
    return WordUtils.capitalize(namePart, ' ', '-');
  }


  protected static String createDate(int year, int month, int day) {
    String dob;
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
    return dob;
  }


}
