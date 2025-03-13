package org.immregistries.hart.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.hart.transform.TransformRequest;
import org.immregistries.hart.transform.Transformer;

public class AlternativeBeginnings extends ProcedureCommon {

  public enum Field {
                     FIRST_NAME(5, 2, false),
                     MIDDLE_NAME(5, 3, false),
                     LAST_NAME(5, 1, false),
                     MOTHERS_MAIDEN_NAME(6, 1, false),
                     MOTHERS_FIRST_NAME(6, 2, false),
                     ADDRESS_STREET(11, 1, false),
                     ADDRESS_CITY(11, 3, false),
                     EMAIL(13, 4, true);

    int fieldPos;
    int subPos;
    boolean repeatedField;

    private Field(int fieldPos, int subPos, boolean repeatedField) {
      this.fieldPos = fieldPos;
      this.subPos = subPos;
      this.repeatedField = repeatedField;
    }
  }

  private Field field;

  public AlternativeBeginnings(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);

    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        if (!field.repeatedField) {
          String value = readValue(fields, field.fieldPos, field.subPos);
          value = varyName(value, field);
          updateValue(value, fields, field.fieldPos, field.subPos);
        } else {
          String[] repeatFields = readRepeats(fields, field.fieldPos);
          int pos = 0;
          for (String value : repeatFields) {
            String email = readRepeatValue(value, field.subPos);
            if (email.indexOf('@') > 0) {
              email = varyName(email, field);
              updateRepeat(email, repeatFields, pos, field.subPos);
            }
            pos++;
          }
          String fieldFinal = createRepeatValue(repeatFields);
          updateContent(fieldFinal, fields, field.fieldPos);
        }
      }
    }

    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyName(String name, Field field) {
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    String nameLower = name.trim().toLowerCase();
    for (String[] consonantClusterMap : consonantClusterMapList) {
      String lookingFor = consonantClusterMap[0];
      String replacingWith = consonantClusterMap[1];
      if (nameLower.startsWith(lookingFor)) {
        name = replacingWith + nameLower.substring(lookingFor.length());
        break;
      }
    }

    if (field == Field.EMAIL) {
      name = makeEmailValid(name);
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


  private static List<String[]> consonantClusterMapList = new ArrayList<>();

  static {
    consonantClusterMapList.add(new String[] {"chr", "kr"});
    consonantClusterMapList.add(new String[] {"spl", "pl"});
    consonantClusterMapList.add(new String[] {"squ", "qu"});
    consonantClusterMapList.add(new String[] {"thr", "fr"});
    consonantClusterMapList.add(new String[] {"str", "t"});
    consonantClusterMapList.add(new String[] {"bl", "l"});
    consonantClusterMapList.add(new String[] {"br", "r"});
    consonantClusterMapList.add(new String[] {"ch", "s"});
    consonantClusterMapList.add(new String[] {"cl", "kl"});
    consonantClusterMapList.add(new String[] {"cr", "kr"});
    consonantClusterMapList.add(new String[] {"dr", "tr"});
    consonantClusterMapList.add(new String[] {"dw", "tw"});
    consonantClusterMapList.add(new String[] {"fl", "l"});
    consonantClusterMapList.add(new String[] {"fr", "th"});
    consonantClusterMapList.add(new String[] {"gl", "l"});
    consonantClusterMapList.add(new String[] {"gr", "g"});
    consonantClusterMapList.add(new String[] {"kl", "l"});
    consonantClusterMapList.add(new String[] {"kr", "chr"});
    consonantClusterMapList.add(new String[] {"pl", "l"});
    consonantClusterMapList.add(new String[] {"pr", "r"});
    consonantClusterMapList.add(new String[] {"sc", "c"});
    consonantClusterMapList.add(new String[] {"sh", "th"});
    consonantClusterMapList.add(new String[] {"sk", "c"});
    consonantClusterMapList.add(new String[] {"sl", "l"});
    consonantClusterMapList.add(new String[] {"sn", "n"});
    consonantClusterMapList.add(new String[] {"sp", "p"});
    consonantClusterMapList.add(new String[] {"st", "t"});
    consonantClusterMapList.add(new String[] {"sw", "w"});
    consonantClusterMapList.add(new String[] {"th", "f"});
    consonantClusterMapList.add(new String[] {"tr", "r"});
    consonantClusterMapList.add(new String[] {"tw", "w"});
    consonantClusterMapList.add(new String[] {"wr", "r"});
    consonantClusterMapList.add(new String[] {"qu", "k"});
    consonantClusterMapList.add(new String[] {"a", "e"});
    consonantClusterMapList.add(new String[] {"b", "v"});
    consonantClusterMapList.add(new String[] {"c", "k"});
    consonantClusterMapList.add(new String[] {"d", "th"});
    consonantClusterMapList.add(new String[] {"e", "a"});
    consonantClusterMapList.add(new String[] {"f", "b"});
    consonantClusterMapList.add(new String[] {"g", "k"});
    consonantClusterMapList.add(new String[] {"h", "v"});
    consonantClusterMapList.add(new String[] {"i", "e"});
    consonantClusterMapList.add(new String[] {"j", "g"});
    consonantClusterMapList.add(new String[] {"k", "c"});
    consonantClusterMapList.add(new String[] {"l", "j"});
    consonantClusterMapList.add(new String[] {"m", "n"});
    consonantClusterMapList.add(new String[] {"n", "m"});
    consonantClusterMapList.add(new String[] {"o", "a"});
    consonantClusterMapList.add(new String[] {"p", "qu"});
    consonantClusterMapList.add(new String[] {"q", "p"});
    consonantClusterMapList.add(new String[] {"r", "h"});
    consonantClusterMapList.add(new String[] {"s", "ts"});
    consonantClusterMapList.add(new String[] {"t", "l"});
    consonantClusterMapList.add(new String[] {"u", "y"});
    consonantClusterMapList.add(new String[] {"v", "w"});
    consonantClusterMapList.add(new String[] {"w", "v"});
    consonantClusterMapList.add(new String[] {"x", "ek"});
    consonantClusterMapList.add(new String[] {"y", "w"});
    consonantClusterMapList.add(new String[] {"z", "s"});
    consonantClusterMapList.add(new String[] {"1", "2"});
    consonantClusterMapList.add(new String[] {"2", "3"});
    consonantClusterMapList.add(new String[] {"3", "4"});
    consonantClusterMapList.add(new String[] {"4", "5"});
    consonantClusterMapList.add(new String[] {"5", "6"});
    consonantClusterMapList.add(new String[] {"6", "7"});
    consonantClusterMapList.add(new String[] {"7", "8"});
    consonantClusterMapList.add(new String[] {"8", "9"});
    consonantClusterMapList.add(new String[] {"9", "0"});
    consonantClusterMapList.add(new String[] {"0", "1"});
  }

  public void setTransformer(Transformer transformer) {
    // not needed
  }
}
