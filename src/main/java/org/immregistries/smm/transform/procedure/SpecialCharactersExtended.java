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
  protected Map<String, List<String>> getDiacritics() {
    return DIACRITICS;
  }

  private static final Map<String, List<String>> DIACRITICS =
      new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

  static {
    // https://en.wikipedia.org/wiki/Diacritic

    DIACRITICS.put("a", Arrays.asList("ā", "â", "ä", "á", "à", "ą", "å", "ǟ", "æ", "ă", "ạ", "ã",
        "ả", "ấ", "ắ", "ặ"));
    DIACRITICS.put("c", Arrays.asList("č", "ç", "ć", "ĉ"));
    DIACRITICS.put("d", Arrays.asList("ḑ", "đ", "ď"));
    DIACRITICS.put("e", Arrays.asList("ē", "ê", "ë", "é", "è", "ę", "ė", "ě", "ё", "ề"));
    DIACRITICS.put("g", Arrays.asList("ģ", "ğ", "ĝ"));
    DIACRITICS.put("h", Arrays.asList("ĥ"));
    DIACRITICS.put("i", Arrays.asList("ī", "î", "ï", "í", "ì", "į"));
    DIACRITICS.put("j", Arrays.asList("ĵ"));
    DIACRITICS.put("k", Arrays.asList("ķ", "ќ"));
    DIACRITICS.put("l", Arrays.asList("ļ", "ł", "ĺ"));
    DIACRITICS.put("n", Arrays.asList("ņ", "ñ", "ŉ", "ň", "ń"));
    DIACRITICS.put("o",
        Arrays.asList("ô", "ö", "ó", "ò", "õ", "ő", "ō", "ȯ", "ȱ", "ȭ", "ø", "ơ", "ỏ"));
    DIACRITICS.put("r", Arrays.asList("ŗ", "ř", "ŕ", "ґ"));
    DIACRITICS.put("s", Arrays.asList("š", "ś", "ş", "ș", "ŝ"));
    DIACRITICS.put("t", Arrays.asList("ț", "ť"));
    DIACRITICS.put("u", Arrays.asList("ū", "û", "ü", "ú", "ù", "ų", "ū", "ű", "ŭ", "ư"));
    DIACRITICS.put("w", Arrays.asList("ŵ", "ẅ", "ẃ", "ẁ"));
    DIACRITICS.put("y", Arrays.asList("ŷ", "ÿ", "ý", "ỳ", "ў"));
    DIACRITICS.put("z", Arrays.asList("ž", "ź", "ż"));
  }
}
