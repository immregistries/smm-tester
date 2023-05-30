package org.immregistries.smm.transform.procedure;

import org.immregistries.smm.transform.Transformer;

public class TextTransposeTypo extends AbstractTypoProcedure {

  public TextTransposeTypo(Field field) {
    super(field);
  }

  protected String varyText(String name, Transformer transformer) {
    // don't transpose the first character ever
    // don't do anything for names just two characters
    if (name.length() <= 2) {
      return name;
    }

    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    int pos = -1;
    String firstLetter = "";
    String secondLetter = "";
    int counter = 0;

    // don't swap letters that are the same
    while (firstLetter.equals(secondLetter) && counter < 50) {
      pos = transformer.getRandom().nextInt(name.length() - 2) + 1;
      firstLetter = name.substring(pos, pos + 1);
      secondLetter = name.substring(pos + 1, pos + 2);
      counter++;
    }

    if (!firstLetter.equals(secondLetter)) {
      name = name.substring(0, pos) + secondLetter + firstLetter + name.substring(pos + 2);
    }

    if (upperCase) {
      name = name.toUpperCase();
    } else if (lowerCase) {
      name = name.toLowerCase();
    }
    return name;
  }
}
