package org.immregistries.hart.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.hart.transform.TransformRequest;
import org.immregistries.hart.transform.Transformer;

public class AddVariation extends ProcedureCommon {

  public enum Field {
                     FIRST_NAME(5, 2, false),
                     MIDDLE_NAME(5, 3, false),
                     LAST_NAME(5, 1, false),
                     MOTHERS_MAIDEN_NAME(6, 1, false),
                     MOTHERS_FIRST_NAME(6, 2, false),
                     ADDRESS_STREET(11, 1, false),
                     ADDRESS_CITY(11, 3, false),
                     EMAIL(13, 4, true);

    int fieldPos;
    int subPos;
    boolean repeatedField;

    private Field(int fieldPos, int subPos, boolean repeatedField) {
      this.fieldPos = fieldPos;
      this.subPos = subPos;
      this.repeatedField = repeatedField;
    }
  }

  private Field field;

  public AddVariation(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList) throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);

    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        if (!field.repeatedField) {
          String value = readValue(fields, field.fieldPos, field.subPos);
          value = varyName(value, field);
          updateValue(value, fields, field.fieldPos, field.subPos);
        } else {
          String[] repeatFields = readRepeats(fields, field.fieldPos);
          int pos = 0;
          for (String value : repeatFields) {
            String email = readRepeatValue(value, field.subPos);
            if (email.indexOf('@') > 0) {
              email = varyName(email, field);
              updateRepeat(email, repeatFields, pos, field.subPos);
            }
            pos++;
          }
          String fieldFinal = createRepeatValue(repeatFields);
          updateContent(fieldFinal, fields, field.fieldPos);
        }
      }
    }

    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyName(String name, Field field) {
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
        if (field == Field.EMAIL || System.currentTimeMillis() % 2 == 0) {
          // do not add a space to the email
          name = name.substring(0, pos) + "'" + capitalizeFirst(name.substring(pos));
        } else {
          name = name.substring(0, pos) + " " + capitalizeFirst(name.substring(pos));
        }
      }
    }

    if (field == Field.EMAIL) {
      name = makeEmailValid(name);
    }

    if (upperCase) {
      name = name.toUpperCase();
    } else if (lowerCase) {
      name = name.toLowerCase();
    }
    return name;
  }

  //TODO (klindgren): Does this belong here?
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

  //TODO (klindgren): Does this belong here?
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

  public void setTransformer(Transformer transformer) {
    // not needed
  }
}
