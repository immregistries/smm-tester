package org.immregistries.hart.transform.procedure;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.hart.transform.Transformer;

public class TextTypo extends AbstractTypoProcedure {

  public TextTypo(Field field) {
    super(field);
  }

  protected String varyText(String name, Transformer transformer) {
    if (StringUtils.isBlank(name)) {
      return name;
    }

    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    int pos = transformer.getRandom().nextInt(name.length() - 1) + 1;
    String letter = name.substring(pos, pos + 1).toUpperCase();
    String typoes = typoesMap.get(letter);
    if (typoes == null) {
      typoes = typoesMap.get("S");
    }
    char typo = typoes.charAt(transformer.getRandom().nextInt(typoes.length()));
    name = name.substring(0, pos) + ("" + typo).toLowerCase() + name.substring(pos + 1);

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

  static {
    typoesMap.put("A", "QWSXZ");
    typoesMap.put("B", "VFGHN");
    typoesMap.put("C", "XSDFV");
    typoesMap.put("D", "WERFVCXS");
    typoesMap.put("E", "WSDFR");
    typoesMap.put("F", "ERTGBVCD");
    typoesMap.put("G", "RFVBNHYT");
    typoesMap.put("H", "TYUJMNBG");
    typoesMap.put("I", "UJKLO");
    typoesMap.put("J", "UYHNMKI");
    typoesMap.put("K", "UJMLOI");
    typoesMap.put("L", "KIOP");
    typoesMap.put("M", "NJK");
    typoesMap.put("N", "BGHJM");
    typoesMap.put("O", "IKLP");
    typoesMap.put("P", "OL");
    typoesMap.put("Q", "ASW");
    typoesMap.put("R", "EDFGT");
    typoesMap.put("S", "QWEDCXZA");
    typoesMap.put("T", "RFGHY");
    typoesMap.put("U", "YHJKI");
    typoesMap.put("V", "CDFGB");
    typoesMap.put("W", "QASDE");
    typoesMap.put("X", "ZASDC");
    typoesMap.put("Y", "TGHJU");
    typoesMap.put("Z", "ASX");
    typoesMap.put(" ", "-.");
    typoesMap.put("0", "9");
    typoesMap.put("1", "2");
    typoesMap.put("2", "13");
    typoesMap.put("3", "24");
    typoesMap.put("4", "35");
    typoesMap.put("5", "46");
    typoesMap.put("6", "57");
    typoesMap.put("7", "68");
    typoesMap.put("8", "79");
    typoesMap.put("9", "90");
    typoesMap.put("@", "2!#");
    typoesMap.put(".", ",>");
  }
}
