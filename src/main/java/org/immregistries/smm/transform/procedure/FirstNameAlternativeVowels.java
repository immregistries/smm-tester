package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class FirstNameAlternativeVowels extends ProcedureCommon implements ProcedureInterface {



  public FirstNameAlternativeVowels() {

  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("PID")) {
          int fieldPos = 5;
          int subPos = 2;
          String firstName = readValue(fields, fieldPos, subPos);
          firstName = varyName(firstName, transformer);
          updateValue(firstName, fields, fieldPos, subPos);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyName(String firstName, Transformer transformer) {
    boolean upperCase = firstName.toUpperCase().equals(firstName);
    boolean lowerCase = firstName.toLowerCase().equals(firstName);

    Random random = transformer.getRandom();

    String firstNameLower = firstName.toLowerCase();
    for (int i = 0; i < 100; i++) {
      String[] vowelSounds = vowelSoundsList.get(random.nextInt(vowelSoundsList.size()));
      String lookingFor = vowelSounds[random.nextInt(vowelSounds.length)];
      int pos = firstNameLower.indexOf(lookingFor);
      if (pos > 0) {
        String replacingWith = vowelSounds[random.nextInt(vowelSounds.length)];
        while (replacingWith.equals(lookingFor)) {
          replacingWith = vowelSounds[random.nextInt(vowelSounds.length)];
        }
        firstName = firstNameLower.substring(0, pos) + replacingWith
            + firstNameLower.substring(pos + lookingFor.length());
        break;
      }
    }



    if (upperCase) {
      firstName = firstName.toUpperCase();
    } else if (lowerCase) {
      firstName = firstName.toLowerCase();
    } else {
      firstName = capitalizeFirst(firstName);
    }
    return firstName;
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
