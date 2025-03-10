package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class RepeatedConsonants extends ProcedureCommon {

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

  public RepeatedConsonants(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        if (!field.repeatedField) {
          String value = readValue(fields, field.fieldPos, field.subPos);
          value = varyName(value, transformer, field);
          updateValue(value, fields, field.fieldPos, field.subPos);
        } else {
          int fieldPos = 13;
          String[] repeatFields = readRepeats(fields, fieldPos);
          int pos = 0;
          for (String value : repeatFields) {
            int subPos = 4;

            String email = readRepeatValue(value, subPos);
            if (email.indexOf('@') > 0) {
              email = varyName(email, transformer, field);
              updateRepeat(email, repeatFields, pos, subPos);
            }
            pos++;
          }
          String fieldFinal = createRepeatValue(repeatFields);
          updateContent(fieldFinal, fields, fieldPos);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  // n, l, t, s, b, d, f, m, n, p, r

  private static char[] REPEATABLE_CONSONANTS = new char[] {'b', 'c', 'd', 'f', 'g', 'l', 'm', 'n',
      'p', 'r', 's', 't', 'b', 'c', 'd', 'f', 'g', 'l', 'm', 'n', 'p', 'r', 's', 't', 'b', 'c', 'd',
      'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'y', 'z'};

  protected static String varyName(String name, Transformer transformer, Field field) {
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    Random random = transformer.getRandom();

    String nameLower = name.toLowerCase();
    for (int i = 0; i < 500; i++) {
      char c = REPEATABLE_CONSONANTS[random.nextInt(REPEATABLE_CONSONANTS.length)];
      int pos = nameLower.indexOf(c);
      if (pos > 0) {
        name = nameLower.substring(0, pos) + c + nameLower.substring(pos);
        break;
      }
    }

    if (field == Field.EMAIL) {
      name = makeEmailValid(name);
    }

    if (upperCase) {
      name = name.toUpperCase();
    } else if (lowerCase) {
      name = name.toLowerCase();
    } else {
      name = capitalizeFirst(name);
    }
    return name;
  }

  private Transformer transformer;

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }
}
