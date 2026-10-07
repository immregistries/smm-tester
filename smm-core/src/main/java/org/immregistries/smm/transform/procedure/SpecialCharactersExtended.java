package org.immregistries.smm.transform.procedure;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SpecialCharactersExtended extends SpecialCharacters {

  public SpecialCharactersExtended(Field field) {
    super(field);
  }

  @Override
  protected Map<String, List<Character>> getDiacritics() {
    return DIACRITICS;
  }

  private static final Map<String, List<Character>> DIACRITICS =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  static {
    // https://en.wikipedia.org/wiki/Diacritic
    // https://www.rapidtables.com/code/text/ascii-table.html

    // à, á, â, ã, ä, å, æ
    DIACRITICS.put("a", Arrays.asList((char) 0xE0, (char) 0xE1, (char) 0xE2, (char) 0xE3,
        (char) 0xE4, (char) 0xE5, (char) 0xE6));

    // ç
    DIACRITICS.put("c", Arrays.asList((char) 0xE7));

    // è, é, ê, ë
    DIACRITICS.put("e", Arrays.asList((char) 0xE8, (char) 0xE9, (char) 0xEA, (char) 0xEB));

    // ì, í, î, ï
    DIACRITICS.put("i", Arrays.asList((char) 0xEC, (char) 0xED, (char) 0xEE, (char) 0xEF));

    // ñ
    DIACRITICS.put("n", Arrays.asList((char) 0xF1));

    // ò, ó, ô, õ, ö
    DIACRITICS.put("o",
        Arrays.asList((char) 0xF2, (char) 0xF3, (char) 0xF4, (char) 0xF5, (char) 0xF6));

    // ù, ú, û, ü
    DIACRITICS.put("u", Arrays.asList((char) 0xF9, (char) 0xFA, (char) 0xFB, (char) 0xFC));

    // ý, ÿ
    DIACRITICS.put("y", Arrays.asList((char) 0xFD, (char) 0xFF));
  }
}
