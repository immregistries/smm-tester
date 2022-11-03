package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AdministrativeSexVariation extends ProcedureCommon implements ProcedureInterface {



  public AdministrativeSexVariation() {

  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("PID")) {
          int fieldPos = 8;
          int subPos = 1;
          String administrativeSex = readValue(fields, fieldPos, subPos).trim();
          administrativeSex = varyCode(administrativeSex);
          updateValue(administrativeSex, fields, fieldPos, subPos);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyCode(String administrativeSex) {
    if (administrativeSex.equalsIgnoreCase("M")) {
      administrativeSex = "F";
    } else if (administrativeSex.equalsIgnoreCase("F")) {
      administrativeSex = "M";
    } else if (administrativeSex.equalsIgnoreCase("U")) {
      administrativeSex = "F";
    } else {
      administrativeSex = "U";
    }
    return administrativeSex;
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
