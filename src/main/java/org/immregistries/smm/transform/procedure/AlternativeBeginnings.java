package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AlternativeBeginnings extends ProcedureCommon implements ProcedureInterface {
  
  public static enum Field {
    FIRST_NAME,
    MIDDLE_NAME,
    LAST_NAME,
    MOTHERS_MAIDEN_NAME
  }
	
  private Field field;
  
  public AlternativeBeginnings(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("PID")) {
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
      if (nameLower.startsWith(lookingFor)) {
        name = replacingWith + nameLower.substring(lookingFor.length());
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

  }



  public void setTransformer(Transformer transformer) {
    // not needed
  }



}