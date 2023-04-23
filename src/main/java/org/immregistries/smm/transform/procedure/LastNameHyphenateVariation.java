package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class LastNameHyphenateVariation extends ProcedureCommon implements ProcedureInterface {


  private Transformer transformer;

  public LastNameHyphenateVariation() {

  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if ("PID".equals(segmentName)) {
          int fieldPos = 5;
          int subPos = 1;
          String firstName = readValue(fields, fieldPos, subPos).trim();
          firstName = varyName(firstName, transformer);
          updateValue(firstName, fields, fieldPos, subPos);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyName(String lastName, Transformer transfomer) {
    String lastNameOriginal = lastName;
    if (lastName.startsWith("-") || lastName.endsWith("-")) {
      return lastName;
    }
    boolean upperCase = lastName.toUpperCase().equals(lastName);
    boolean lowerCase = lastName.toLowerCase().equals(lastName);

    int pos = lastName.indexOf('-');
    if (pos > 0 && (pos + 1) < lastName.length()) {
      lastName = capitalizeFirst(lastName.substring(0, pos)) + " "
          + capitalizeFirst(lastName.substring(pos + 1));
    } else {
      pos = lastName.indexOf(' ');
      if (pos > 0 && (pos + 1) < lastName.length()) {
        lastName = capitalizeFirst(lastName.substring(0, pos)) + "-"
            + capitalizeFirst(lastName.substring(pos + 1));
      } else {
        try {
          lastName = capitalizeFirst(lastName) + "-"
              + capitalizeFirst(transfomer.getRandomValue("LAST_NAME"));
        } catch (Throwable e) {
          e.printStackTrace();
        }
      }
    }
    if (upperCase) {
      lastName = lastName.toUpperCase();
    } else if (lowerCase) {
      lastName = lastName.toLowerCase();
    }
    System.out.println(lastNameOriginal + " --> " + lastName);
    return lastName;
  }

  protected static String capitalizeFirst(String namePart) {
    if (namePart.length() <= 1) {
      return namePart.toUpperCase();
    }
    return namePart.substring(0, 1).toUpperCase() + namePart.substring(1);
  }


  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }



}
