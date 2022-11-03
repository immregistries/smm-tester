package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class NameTypo extends ProcedureCommon implements ProcedureInterface {



  public static enum Field {
                            FIRST_NAME,
                            LAST_NAME, 
                            CITY
  }


  private Transformer transformer;

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }

  private Field field;


  public NameTypo(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("PID")) {
          int fieldPos = 5;
          int subPos = 1;
          if (field == Field.LAST_NAME) {
            subPos = 1;
          } else if (field == Field.FIRST_NAME) {
            subPos = 2;
          } else if (field == Field.CITY)
          {
            fieldPos = 11;
            subPos = 3;
          }
          String name = readValue(fields, fieldPos, subPos);
          name = varyName(name, transformer);
          updateValue(name.substring(0, name.length() - 4), fields, fieldPos, subPos);
        }

      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }



  protected static String varyName(String name, Transformer transformer) {
    String originalName = name;
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


    if (upperCase) {
      name = name.toUpperCase();
    } else if (lowerCase) {
      name = name.toLowerCase();
    }

    System.out.println(originalName + " --> " + name);
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

  }



}
