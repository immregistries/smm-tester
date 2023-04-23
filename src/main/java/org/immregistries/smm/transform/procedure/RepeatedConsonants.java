package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class RepeatedConsonants extends ProcedureCommon implements ProcedureInterface {

  public static enum Field {
    FIRST_NAME,
    MIDDLE_NAME,
    LAST_NAME,
    MOTHERS_MAIDEN_NAME
  }
  
  private Field field;

  public RepeatedConsonants(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if ("PID".equals(segmentName)) {
          if (field == Field.LAST_NAME
            || field == Field.FIRST_NAME
            || field == Field.MIDDLE_NAME
            || field == Field.MOTHERS_MAIDEN_NAME) {
            
            int fieldPos = 5;
            int subPos = 1;
            if (field == Field.LAST_NAME) {
              subPos = 1;
            } else if (field == Field.FIRST_NAME) {
              subPos = 2;
            } else if (field == Field.MIDDLE_NAME) {
              subPos = 3;
            } else if (field == Field.MOTHERS_MAIDEN_NAME) {
              fieldPos = 6;
              subPos = 1;
            }
            
            String value = readValue(fields, fieldPos, subPos);
            value = varyName(value, transformer);
            updateValue(value, fields, fieldPos, subPos);
          }
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  // n, l, t, s, b, d, f, m, n, p, r

  private static char[] REPEATABLE_CONSONANTS = new char[] {'b', 'c', 'd', 'f', 'g', 'l', 'm', 'n',
      'p', 'r', 's', 't', 'b', 'c', 'd', 'f', 'g', 'l', 'm', 'n', 'p', 'r', 's', 't', 'b', 'c', 'd',
      'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'y', 'z'};

  protected static String varyName(String name, Transformer transformer) {
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    Random random = transformer.getRandom();

    String nameLower = name.toLowerCase();
    for (int i = 0; i < 100; i++) {
      char c = REPEATABLE_CONSONANTS[random.nextInt(REPEATABLE_CONSONANTS.length)];
      int pos = nameLower.indexOf(c);
      if (pos > 0)
      {
        name = nameLower.substring(0, pos) + c + nameLower.substring(pos);
        break;
      }
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
