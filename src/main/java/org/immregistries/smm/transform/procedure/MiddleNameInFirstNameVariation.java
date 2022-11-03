package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class MiddleNameInFirstNameVariation extends ProcedureCommon implements ProcedureInterface {



  public MiddleNameInFirstNameVariation() {

  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("PID")) {
          int fieldPos = 5;
          String firstName = readValue(fields, fieldPos, 2);
          String middleName = readValue(fields, fieldPos, 3);
          String[] names = varyName(firstName, middleName);
          updateValue(names[0], fields, fieldPos, 2);
          updateValue(names[0], fields, fieldPos, 3);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String[] varyName(String firstName, String middleName) {

    String firstNameOriginal = firstName;
    String middleNameOriginal = middleName;

    boolean upperCase =
        firstName.toUpperCase().equals(firstName) && middleName.toUpperCase().equals(middleName);
    boolean lowerCase =
        firstName.toLowerCase().equals(firstName) && middleName.toLowerCase().equals(middleName);

    String[] names = new String[2];

    firstName = firstName.trim();
    middleName = middleName.trim();

    names[0] = firstName;
    names[1] = middleName;

    if (firstName.equals("")) {
      // do nothing
    } else if (middleName.equals("")) {
      int pos = firstName.indexOf(" ");
      if (pos > 0) {
        names[0] = firstName.substring(0, pos);
        names[1] = firstName.substring(pos + 1);
      } else {
        // nothing to do, no middle name to pull out
      }
    } else {
      names[0] = firstName + " " + middleName;
      names[1] = "";
    }

    if (upperCase) {
      names[0] = names[0].toUpperCase();
      names[1] = names[1].toUpperCase();
    } else if (lowerCase) {
      names[0] = names[0].toLowerCase();
      names[1] = names[1].toLowerCase();
    }

    System.out.println(
        firstNameOriginal + ", " + middleNameOriginal + " --> " + names[0] + ", " + names[1]);
    return names;
  }



  public void setTransformer(Transformer transformer) {}



}
