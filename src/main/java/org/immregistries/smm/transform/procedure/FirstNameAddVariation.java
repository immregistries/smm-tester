package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class FirstNameAddVariation extends ProcedureCommon implements ProcedureInterface {

  public FirstNameAddVariation() {

  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("PID")) {
          int fieldPos = 5;
          int subPos = 2;
          String firstName = readValue(fields, fieldPos, subPos).trim();
          firstName = varyName(firstName);
          updateValue(firstName, fields, fieldPos, subPos);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyName(String firstName) {
    if (firstName.startsWith("'") || firstName.endsWith("'"))
    {
      return firstName;
    }
    boolean upperCase = firstName.toUpperCase().equals(firstName);
    boolean lowerCase = firstName.toLowerCase().equals(firstName);

    int pos = firstName.indexOf('\'');
    if (pos > 0 && (pos + 1) < firstName.length()) {
      firstName = firstName.substring(0, pos) + " " + capitalizeFirst(firstName.substring(pos + 1));
    } else {
      pos = firstName.indexOf(' ');
      if (pos > 0 && (pos + 1) < firstName.length()) {
        firstName = firstName.substring(0, pos) + capitalizeFirst(firstName.substring(pos + 1));
      } else {
        pos = findAnotherCapital(firstName);
        if (pos == -1) {
          pos = findFirstConsentAfterVowel(firstName);
        }
        if (pos > 0 && pos < firstName.length()) {
          firstName = firstName.substring(0, pos) + "'" + capitalizeFirst(firstName.substring(pos));
        }
      }
    }
    if (upperCase) {
      firstName = firstName.toUpperCase();
    } else if (lowerCase) {
      firstName = firstName.toLowerCase();
    }
    return firstName;
  }

  protected static int findFirstConsentAfterVowel(String firstName) {
    int pos = 0;
    String fn = firstName.toUpperCase();
    boolean foundVowel = false;
    while (pos < fn.length()) {
      char c = fn.charAt(pos);
      if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
        foundVowel = true;
      } else if (foundVowel) {
        return pos;
      }
      pos++;
    }
    return -1;
  }

  protected static int findAnotherCapital(String firstName) {
    if (firstName.equals(firstName.toUpperCase())) {
      return -1;
    }
    int pos = 1;
    while (pos < firstName.length()) {
      char c = firstName.charAt(pos);
      if (c < 'a') {
        return pos;
      }
      pos++;
    }
    return -1;
  }

  protected static String capitalizeFirst(String namePart) {
    if (namePart.length() <= 1) {
      return namePart.toUpperCase();
    }
    return namePart.substring(0, 1).toUpperCase() + namePart.substring(1);
  }


  public void setTransformer(Transformer transformer) {
    // not needed
  }



}
