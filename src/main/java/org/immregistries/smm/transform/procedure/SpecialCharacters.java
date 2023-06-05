package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class SpecialCharacters extends ProcedureCommon implements ProcedureInterface {

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

  public SpecialCharacters(Field field) {
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
          value = varyText(value);
          updateValue(value, fields, field.fieldPos, field.subPos);
        } else {
          int fieldPos = Field.EMAIL.fieldPos;
          String[] repeatFields = readRepeats(fields, fieldPos);
          int pos = 0;
          for (String value : repeatFields) {
            String email = readRepeatValue(value, Field.EMAIL.subPos);
            if (email.indexOf('@') > 0) {
              email = varyText(email);
              updateRepeat(email, repeatFields, pos, Field.EMAIL.subPos);
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

  protected String varyText(String name) {
    // don't switch the first character ever
    if (name.length() <= 1) {
      return name;
    }

    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    int pos = -1;
    String letter = "";
    int counter = 0;

    // randomly look for a letter
    while (!getDiacritics().containsKey(letter) && counter < 50) {
      pos = transformer.getRandom().nextInt(name.length() - 1) + 1;
      letter = name.substring(pos, pos + 1).toUpperCase();
      counter++;
    }

    // try really hard to find a letter, somewhere
    if (!getDiacritics().containsKey(letter)) {
      for (pos = 1; pos < name.length(); pos++) {
        letter = name.substring(pos, pos + 1);
        if (getDiacritics().containsKey(letter)) {
          break;
        }
      }
    }

    if (getDiacritics().containsKey(letter)) {
      List<String> diacritics = getDiacritics().get(letter);
      String diacritic = diacritics.get(transformer.getRandom().nextInt(diacritics.size()));
      name = name.substring(0, pos) + (diacritic).toLowerCase() + name.substring(pos + 1);
    }

    if (upperCase) {
      name = name.toUpperCase();
    } else if (lowerCase) {
      name = name.toLowerCase();
    }
    return name;
  }

  protected Map<String, List<String>> getDiacritics() {
    return DIACRITICS;
  }

  private static final Map<String, List<String>> DIACRITICS =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  static {
    // https://en.wikipedia.org/wiki/Diacritic

    DIACRITICS.put("a", Arrays.asList("á"));
    DIACRITICS.put("e", Arrays.asList("é"));
    DIACRITICS.put("i", Arrays.asList("í"));
    DIACRITICS.put("n", Arrays.asList("ñ"));
    DIACRITICS.put("o", Arrays.asList("ó"));
    DIACRITICS.put("u", Arrays.asList("ú", "ü"));
  }

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }
}
