package org.immregistries.hart.transform.procedure;

import java.util.HashMap;
import java.util.Map;
import org.immregistries.hart.transform.Transformer;

public class TextNumberToLetterTypo extends AbstractTypoProcedure {

  public TextNumberToLetterTypo(Field field) {
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
    String number = "";
    int counter = 0;

    // randomly look for a number
    while (!typoesMap.containsKey(number) && counter < 50) {
      pos = transformer.getRandom().nextInt(name.length() - 1) + 1;
      number = name.substring(pos, pos + 1);
      counter++;
    }

    // try really hard to find a number, somewhere
    if (!typoesMap.containsKey(number)) {
      for (pos = 1; pos < name.length(); pos++) {
        number = name.substring(pos, pos + 1);
        if (typoesMap.containsKey(number)) {
          break;
        }
      }
    }

    if (typoesMap.containsKey(number)) {
      String typoes = typoesMap.get(number);
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

  // a reverse of the TextLetterToNumber map
  static {
    typoesMap.put("0", "CDOPQU");
    typoesMap.put("1", "ILQ");
    typoesMap.put("2", "QWZ");
    typoesMap.put("3", "EWX");
    typoesMap.put("4", "AEHRUV");
    typoesMap.put("5", "RSTY");
    typoesMap.put("6", "BGTY");
    typoesMap.put("7", "FJTUYZ");
    typoesMap.put("8", "BIMSU");
    typoesMap.put("9", "GIJKMNOQ");
  }
}
