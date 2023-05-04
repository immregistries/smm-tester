package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AlternativeEndings extends ProcedureCommon implements ProcedureInterface {

  public enum Field {
                     FIRST_NAME(5, 2),
                     MIDDLE_NAME(5, 3),
                     LAST_NAME(5, 1),
                     MOTHERS_MAIDEN_NAME(6, 1),
                     MOTHERS_MAIDEN_FIRST_NAME(6, 2),
                     ADDRESS_STREET(11, 1),
                     ADDRESS_CITY(11, 3),
                     EMAIL(13, 4);

    int fieldPos;
    int subPos;

    private Field(int fieldPos, int subPos) {
      this.fieldPos = fieldPos;
      this.subPos = subPos;
    }
  }

  private Field field;

  public AlternativeEndings(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);

    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        String value = readValue(fields, field.fieldPos, field.subPos);
        value = varyName(value);
        updateValue(value, fields, field.fieldPos, field.subPos);
      }
    }

    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyName(String name) {
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    String nameLower = name.trim().toLowerCase().replaceAll("[^a-zA-Z]+$", "");
    String punctuation = name.substring(nameLower.length());

    for (String[] consonantClusterMap : consonantClusterMapList) {
      String lookingFor = consonantClusterMap[0];
      String replacingWith = consonantClusterMap[1];
      if (nameLower.endsWith(lookingFor)) {
        name = nameLower.substring(0, nameLower.length() - lookingFor.length()) + replacingWith
            + punctuation;
        break;
      }
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
    consonantClusterMapList.add(new String[] {"ck", "kee"});
    consonantClusterMapList.add(new String[] {"ct", "tus"});
    consonantClusterMapList.add(new String[] {"ft", "ftee"});
    consonantClusterMapList.add(new String[] {"ld", "lder"});
    consonantClusterMapList.add(new String[] {"lf", "lfie"});
    consonantClusterMapList.add(new String[] {"lp", "lpy"});
    consonantClusterMapList.add(new String[] {"lt", "ltie"});
    consonantClusterMapList.add(new String[] {"mp", "mpie"});
    consonantClusterMapList.add(new String[] {"nd", "dy"});
    consonantClusterMapList.add(new String[] {"nk", "ky"});
    consonantClusterMapList.add(new String[] {"nt", "tie"});
    consonantClusterMapList.add(new String[] {"pt", "ptie"});
    consonantClusterMapList.add(new String[] {"rn", "rny"});
    consonantClusterMapList.add(new String[] {"rd", "rdy"});
    consonantClusterMapList.add(new String[] {"rk", "rkie"});
    consonantClusterMapList.add(new String[] {"sk", "sky"});
    consonantClusterMapList.add(new String[] {"sm", "smon"});
    consonantClusterMapList.add(new String[] {"sp", "sper"});
    consonantClusterMapList.add(new String[] {"st", "on"});
    consonantClusterMapList.add(new String[] {"a", "ay"});
    consonantClusterMapList.add(new String[] {"b", "bbie"});
    consonantClusterMapList.add(new String[] {"c", "k"});
    consonantClusterMapList.add(new String[] {"d", "dd"});
    consonantClusterMapList.add(new String[] {"e", "ee"});
    consonantClusterMapList.add(new String[] {"f", "ffy"});
    consonantClusterMapList.add(new String[] {"g", "ggy"});
    consonantClusterMapList.add(new String[] {"h", "ph"});
    consonantClusterMapList.add(new String[] {"i", "ie"});
    consonantClusterMapList.add(new String[] {"j", "ch"});
    consonantClusterMapList.add(new String[] {"k", "ck"});
    consonantClusterMapList.add(new String[] {"l", "ls"});
    consonantClusterMapList.add(new String[] {"m", "nn"});
    consonantClusterMapList.add(new String[] {"n", "mm"});
    consonantClusterMapList.add(new String[] {"o", "oo"});
    consonantClusterMapList.add(new String[] {"p", "ppy"});
    consonantClusterMapList.add(new String[] {"q", "que"});
    consonantClusterMapList.add(new String[] {"r", "rrie"});
    consonantClusterMapList.add(new String[] {"s", "sz"});
    consonantClusterMapList.add(new String[] {"t", "ts"});
    consonantClusterMapList.add(new String[] {"u", "oo"});
    consonantClusterMapList.add(new String[] {"v", "f"});
    consonantClusterMapList.add(new String[] {"w", "o"});
    consonantClusterMapList.add(new String[] {"x", "cks"});
    consonantClusterMapList.add(new String[] {"y", "ie"});
    consonantClusterMapList.add(new String[] {"z", "ss"});
  }

  public void setTransformer(Transformer transformer) {
    // not needed
  }

}
