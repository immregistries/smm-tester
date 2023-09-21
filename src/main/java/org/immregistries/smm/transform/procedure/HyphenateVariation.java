package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class HyphenateVariation extends ProcedureCommon implements ProcedureInterface {

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
  private Transformer transformer;

  public HyphenateVariation(Field field) {
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
          String[] repeatFields = readRepeats(fields, field.fieldPos);
          int pos = 0;
          for (String value : repeatFields) {
            String email = readRepeatValue(value, field.subPos);
            if (email.indexOf('@') > 0) {
              email = varyName(email, transformer, field);
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

  protected static String varyName(String name, Transformer transformer, Field field) {
    if (name.startsWith("-") || name.endsWith("-")) {
      return name;
    }

    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    boolean isSpecialCase = field == Field.EMAIL || field == Field.ADDRESS_STREET;

    int pos = name.indexOf('-');
    if (!isSpecialCase && pos > 0 && (pos + 1) < name.length()) {
      name =
          capitalizeFirst(name.substring(0, pos)) + " " + capitalizeFirst(name.substring(pos + 1));
    } else {
      pos = name.indexOf(' ');
      if (!isSpecialCase && pos > 0 && (pos + 1) < name.length()) {
        name = capitalizeFirst(name.substring(0, pos)) + "-"
            + capitalizeFirst(name.substring(pos + 1));
      } else {
        try {
          String randomValue;
          switch (field) {
            case FIRST_NAME:
            case MIDDLE_NAME:
              randomValue = transformer
                  .getRandomValue(transformer.getRandom().nextBoolean() ? "BOY" : "GIRL");
              break;
            case MOTHERS_FIRST_NAME:
              randomValue = transformer.getRandomValue("GIRL");
              break;
            case ADDRESS_STREET:
              randomValue = transformer.getRandomValue("STREET_NAME");
              break;
            case ADDRESS_CITY:
              randomValue = transformer.getRandomValue("ADDRESS");
              break;
            case EMAIL:
              randomValue =
                  transformer.getRandomValue("LAST_NAME").replaceAll("\\s+", "").toLowerCase();
              break;
            case LAST_NAME:
            case MOTHERS_MAIDEN_NAME:
            default:
              randomValue = transformer.getRandomValue("LAST_NAME");
              break;
          }

          if (field == Field.EMAIL) {
            String[] emailParts = name.split("\\@");
            name = capitalizeFirst(emailParts[0]) + "-" + capitalizeFirst(randomValue) + "@"
                + emailParts[1];
            name = name.replaceAll("\\s+", "");
          } else if (field == Field.ADDRESS_STREET) {
            String original = getAddressStreetName(name);
            name = capitalizeFirst(replaceAddressStreet(name, original + "-" + randomValue));
          } else {
            name = capitalizeFirst(name) + "-" + capitalizeFirst(randomValue);
          }
        } catch (Throwable e) {
          e.printStackTrace();
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

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }
}
