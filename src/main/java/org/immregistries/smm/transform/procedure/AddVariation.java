package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AddVariation extends ProcedureCommon implements ProcedureInterface {

  public enum Field {
                     FIRST_NAME(5, 2),
                     MIDDLE_NAME(5, 3),
                     LAST_NAME(5, 1),
                     MOTHERS_MAIDEN_NAME(6, 1),
                     MOTHERS_MAIDEN_FIRST_NAME(6, 2),
                     ADDRESS_STREET(11, 1),
                     ADDRESS_CITY(11, 3),
                     EMAIL(13, 4);

    int fieldPos;
    int subPos;

    private Field(int fieldPos, int subPos) {
      this.fieldPos = fieldPos;
      this.subPos = subPos;
    }
  }

  private Field field;

  public AddVariation(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);

    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        String value = readValue(fields, field.fieldPos, field.subPos);
        value = varyName(value);
        updateValue(value, fields, field.fieldPos, field.subPos);
      }
    }

    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyName(String name) {
    if (name.startsWith("'") || name.endsWith("'")) {
      return name;
    }
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    int posApostrophe = name.indexOf('\'');

    int posSpace = name.indexOf(' ');
    boolean hasApostrophe = posApostrophe > 0 && (posApostrophe + 1) < name.length();
    boolean hasSpace = posSpace > 0 && (posSpace + 1) < name.length();
    if (hasApostrophe) {
      name = name.substring(0, posApostrophe) + capitalizeFirst(name.substring(posApostrophe + 1));
    } else if (hasSpace) {
      name = name.substring(0, posSpace) + capitalizeFirst(name.substring(posSpace + 1));
    } else {
      int pos = findAnotherCapital(name);
      if (pos == -1) {
        pos = findFirstConsonantAfterVowel(name);
      }
      if (pos > 0 && pos < name.length()) {
        if (System.currentTimeMillis() % 2 == 0) {
          name = name.substring(0, pos) + "'" + capitalizeFirst(name.substring(pos));
        } else {
          name = name.substring(0, pos) + " " + capitalizeFirst(name.substring(pos));
        }
      }
    }

    if (upperCase) {
      name = name.toUpperCase();
    } else if (lowerCase) {
      name = name.toLowerCase();
    }
    return name;
  }

  protected static int findFirstConsonantAfterVowel(String name) {
    int pos = 0;
    String fn = name.toUpperCase();
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

  protected static int findAnotherCapital(String name) {
    if (name.equals(name.toUpperCase())) {
      return -1;
    }
    int pos = 1;
    while (pos < name.length()) {
      char c = name.charAt(pos);
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
