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
                     FIRST_NAME(5, 2),
                     MIDDLE_NAME(5, 3),
                     LAST_NAME(5, 1),
                     MOTHERS_MAIDEN_NAME(6, 1),
                     MOTHERS_MAIDEN_FIRST_NAME(6, 2),
                     ADDRESS_STREET(11, 1);

    int fieldPos;
    int subPos;

    private Field(int fieldPos, int subPos) {
      this.fieldPos = fieldPos;
      this.subPos = subPos;
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
        String value = readValue(fields, field.fieldPos, field.subPos);
        value = varyName(value, transformer);
        updateValue(value, fields, field.fieldPos, field.subPos);
      }
    }

    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyName(String name, Transformer transformer) {
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
