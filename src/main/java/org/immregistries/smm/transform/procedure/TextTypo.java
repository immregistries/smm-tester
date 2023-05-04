package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class TextTypo extends ProcedureCommon implements ProcedureInterface {



  public enum Field {
                     FIRST_NAME(5, 2, false),
                     MIDDLE_NAME(5, 3, false),
                     LAST_NAME(5, 1, false),
                     MOTHERS_MAIDEN_NAME(6, 1, false),
                     MOTHERS_MAIDEN_FIRST_NAME(6, 2, false),
                     ADDRESS_STREET(11, 1, false),
                     ADDRESS_CITY(11, 3, false),
                     PHONE(13, 7, true),
                     EMAIL(13, 4, true);

    int fieldPos;
    int subPos;
    boolean specialCase;

    private Field(int fieldPos, int subPos, boolean specialCase) {
      this.fieldPos = fieldPos;
      this.subPos = subPos;
      this.specialCase = specialCase;
    }
  }


  private Transformer transformer;

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }

  private Field field;


  public TextTypo(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);

    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        if (!field.specialCase) {
          String value = readValue(fields, field.fieldPos, field.subPos);
          value = varyText(value, transformer);
          updateValue(value, fields, field.fieldPos, field.subPos);
        } else {
          int fieldPos = 13;
          String[] repeatFields = readRepeats(fields, fieldPos);
          int pos = 0;
          for (String value : repeatFields) {
            if (field == Field.PHONE) {
              int subPos = 7;
              String phone = readRepeatValue(value, subPos);
              if (phone.length() >= 4) {
                phone = varyText(phone, transformer);
                updateRepeat(phone, repeatFields, pos, subPos);
              }
            } else if (field == Field.EMAIL) {
              int subPos = 4;

              String email = readRepeatValue(value, subPos);
              if (email.indexOf('@') > 0) {
                email = varyText(email, transformer);
                updateRepeat(email, repeatFields, pos, subPos);
              }
            }
            pos++;
          }
          String fieldFinal = createRepeatValue(repeatFields);
          updateContent(fieldFinal, fields, fieldPos);
        }
      }
    }

    putMessageBackTogether(transformRequest, fieldsList);
  }



  protected static String varyText(String name, Transformer transformer) {
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
