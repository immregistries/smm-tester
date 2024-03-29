package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AlternativeVowels extends ProcedureCommon implements ProcedureInterface {

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

  public AlternativeVowels(Field field) {
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
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    Random random = transformer.getRandom();
    String nameLower = name.toLowerCase();
    for (int i = 0; i < 100; i++) {
      String[] vowelSounds = vowelSoundsList.get(random.nextInt(vowelSoundsList.size()));
      String lookingFor = vowelSounds[random.nextInt(vowelSounds.length)];
      int pos = nameLower.indexOf(lookingFor);
      if (pos > 0) {
        String replacingWith = vowelSounds[random.nextInt(vowelSounds.length)];
        while (replacingWith.equals(lookingFor)) {
          replacingWith = vowelSounds[random.nextInt(vowelSounds.length)];
        }
        name = nameLower.substring(0, pos) + replacingWith
            + nameLower.substring(pos + lookingFor.length());

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

  private static List<String[]> vowelSoundsList = new ArrayList<>();

  static {
    vowelSoundsList.add(new String[] {"a", "ai", "ay", "ei", "ey", "ea"});
    vowelSoundsList.add(new String[] {"ar", "ai"});
    vowelSoundsList.add(new String[] {"e", "ee", "ea", "ie", "ei"});
    vowelSoundsList.add(new String[] {"e", "ea"});
    vowelSoundsList.add(new String[] {"er", "ur", "ir", "or", "ear", "ar"});
    vowelSoundsList.add(new String[] {"i", "y"});
    vowelSoundsList.add(new String[] {"i", "ie", "y", "uy"});
    vowelSoundsList.add(new String[] {"o", "oa", "ow"});
    vowelSoundsList.add(new String[] {"oi", "oy"});
    vowelSoundsList.add(new String[] {"oo", "u", "ou"});
    vowelSoundsList.add(new String[] {"ou", "ow"});
    vowelSoundsList.add(new String[] {"ow", "aw", "au"});
    vowelSoundsList.add(new String[] {"u", "ew", "eu", "ue", "ui"});
    vowelSoundsList.add(new String[] {"u", "o", "oo", "ew", "ue", "ui", "ou"});
  }

  private Transformer transformer;

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }
}
