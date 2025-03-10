package org.immregistries.smm.transform.procedure;

import java.util.HashMap;
import java.util.Map;
import org.immregistries.smm.transform.Transformer;

public class TextLetterToNumberTypo extends AbstractTypoProcedure {

  public TextLetterToNumberTypo(Field field) {
    super(field);
  }

  protected String varyText(String name, Transformer transformer) {
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
    while (!typoesMap.containsKey(letter) && counter < 50) {
      pos = transformer.getRandom().nextInt(name.length() - 1) + 1;
      letter = name.substring(pos, pos + 1).toUpperCase();
      counter++;
    }

    // try really hard to find a letter, somewhere
    if (!typoesMap.containsKey(letter)) {
      for (pos = 1; pos < name.length(); pos++) {
        letter = name.substring(pos, pos + 1).toUpperCase();
        if (typoesMap.containsKey(letter)) {
          break;
        }
      }
    }

    if (typoesMap.containsKey(letter)) {
      String typoes = typoesMap.get(letter);
      char typo = typoes.charAt(transformer.getRandom().nextInt(typoes.length()));
      name = name.substring(0, pos) + ("" + typo).toLowerCase() + name.substring(pos + 1);
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

  private static Map<String, String> typoesMap = new HashMap<>();

  // https://www.ismp.org/resources/misidentification-alphanumeric-symbols
  // also did a mix of other look-alikes, along with numbers "near" the letter on the keyboard
  static {
    typoesMap.put("A", "4");
    typoesMap.put("B", "86");
    typoesMap.put("C", "0");
    typoesMap.put("D", "0");
    typoesMap.put("E", "34");
    typoesMap.put("F", "7");
    typoesMap.put("G", "69");
    typoesMap.put("H", "4");
    typoesMap.put("I", "189");
    typoesMap.put("J", "79");
    typoesMap.put("K", "9");
    typoesMap.put("L", "1");
    typoesMap.put("M", "89");
    typoesMap.put("N", "9");
    typoesMap.put("O", "09");
    typoesMap.put("P", "0");
    typoesMap.put("Q", "2901");
    typoesMap.put("R", "45");
    typoesMap.put("S", "58");
    typoesMap.put("T", "567");
    typoesMap.put("U", "0478");
    typoesMap.put("V", "4");
    typoesMap.put("W", "23");
    typoesMap.put("X", "3");
    typoesMap.put("Y", "567");
    typoesMap.put("Z", "27");
  }
}
