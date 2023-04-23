package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AlternativeEndings extends ProcedureCommon implements ProcedureInterface {

  public static enum Field {
    FIRST_NAME,
    MIDDLE_NAME,
    LAST_NAME,
    MOTHERS_MAIDEN_NAME
  }
  
  private Field field;

  public AlternativeEndings(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if ("PID".equals(segmentName)) {
          if (field == Field.LAST_NAME
            || field == Field.FIRST_NAME
            || field == Field.MIDDLE_NAME
            || field == Field.MOTHERS_MAIDEN_NAME) {
            
            int fieldPos = 5;
            int subPos = 1;
            if (field == Field.LAST_NAME) {
              subPos = 1;
            } else if (field == Field.FIRST_NAME) {
              subPos = 2;
            } else if (field == Field.MIDDLE_NAME) {
              subPos = 3;
            } else if (field == Field.MOTHERS_MAIDEN_NAME) {
              fieldPos = 6;
              subPos = 1;
            }
            
            String value = readValue(fields, fieldPos, subPos);
            value = varyName(value);
            updateValue(value, fields, fieldPos, subPos);
          }
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyName(String name) {
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    String nameLower = name.trim().toLowerCase();
    for (String[] consonantClusterMap : consonantClusterMapList) {
      String lookingFor = consonantClusterMap[0];
      String replacingWith = consonantClusterMap[1];
      if (nameLower.endsWith(lookingFor)) {
        name = nameLower.substring(0, nameLower.length() - lookingFor.length())
            + replacingWith;
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
