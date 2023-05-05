package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class TextChange extends ProcedureCommon implements ProcedureInterface {

  public enum Field {
                     FIRST_NAME(5, 2, false),
                     MIDDLE_NAME(5, 3, false),
                     LAST_NAME(5, 1, false),
                     MOTHERS_MAIDEN_NAME(6, 1, false),
                     MOTHERS_MAIDEN_FIRST_NAME(6, 2, false),
                     ADDRESS_CITY(11, 3, false),
                     PHONE(13, 7, true),
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

  private Transformer transformer;

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }

  private Field field;


  public TextChange(Field field) {
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
          value = varyText(value, field, transformer);
          updateValue(value, fields, field.fieldPos, field.subPos);
        } else {
          int fieldPos = 13;
          String[] repeatFields = readRepeats(fields, fieldPos);
          int pos = 0;
          for (String value : repeatFields) {
            if (field == Field.PHONE) {
              int subPos = 7;
              String phone = readRepeatValue(value, subPos);
              if (phone.length() >= 4) {
                phone = varyText(phone, field, transformer);
                updateRepeat(phone, repeatFields, pos, subPos);
              }
            } else if (field == Field.EMAIL) {
              int subPos = 4;

              String email = readRepeatValue(value, subPos);
              if (email.indexOf('@') > 0) {
                email = varyText(email, field, transformer);
                updateRepeat(email, repeatFields, pos, subPos);
              }
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

  protected static String varyText(String value, Field field, Transformer transformer) {
    boolean upperCase = value.toUpperCase().equals(value);
    boolean lowerCase = value.toLowerCase().equals(value);
    Random random = transformer.getRandom();

    switch (field) {
      case FIRST_NAME:
        value = transformer.getRandomValue(random.nextBoolean() ? "GIRL" : "BOY");
        break;
      case MIDDLE_NAME:
        value = transformer.getRandomValue(random.nextBoolean() ? "GIRL" : "BOY");
        break;
      case LAST_NAME:
        value = transformer.getRandomValue("LAST_NAME");
        break;
      case MOTHERS_MAIDEN_NAME:
        value = transformer.getRandomValue("LAST_NAME");
        break;
      case MOTHERS_MAIDEN_FIRST_NAME:
        value = transformer.getRandomValue("GIRL");
        break;
      case ADDRESS_CITY:
        value = transformer.getRandomValue("ADDRESS");
        break;
      case EMAIL:
        value = transformer.getRandomValue("GIRL") + "." + transformer.getRandomValue("LAST_NAME")
            + random.nextInt(2500) + "@" + transformer.getRandomValue("LAST_NAME") + ".com";
        break;
      case PHONE:
        value = "" + (random.nextInt(8) + 2) + random.nextInt(10) + random.nextInt(10)
            + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10);
        break;
    }

    if (upperCase) {
      value = value.toUpperCase();
    } else if (lowerCase) {
      value = value.toLowerCase();
    }
    return value;
  }
}
