package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AlternativeVowels extends ProcedureCommon implements ProcedureInterface {

  public static enum Field {
    FIRST_NAME,
    MIDDLE_NAME,
    LAST_NAME,
    MOTHERS_MAIDEN_NAME
  }
  
  private Field field;

  public AlternativeVowels(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("PID")) {
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

  protected static String varyName(String name, Transformer transformer) {
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    Random random = transformer.getRandom();
    String nameLower = name.toLowerCase();
    boolean foundMatch = false;
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
        
        foundMatch = true;
        break;
      }
    }

    // if no match was found with the random lookups, brute force one
    if(!foundMatch) {
      for(int i = 1; i < nameLower.length(); i++) {
        String letter = nameLower.substring(i, i + 1);
        for(String[] vowelSounds : vowelSoundsList) {
          if(letter.equals(vowelSounds[0])) {
            name = nameLower.substring(0, i) + vowelSounds[1]
                + nameLower.substring(i + 1);
          }
        }
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
