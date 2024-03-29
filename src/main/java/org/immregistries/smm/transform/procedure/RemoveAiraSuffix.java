package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class RemoveAiraSuffix extends ProcedureCommon implements ProcedureInterface {

  public static enum Field {
                            FIRST_NAME,
                            LAST_NAME,
                            MOTHERS_MAIDEN_NAME
  }


  public void setTransformer(Transformer transformer) {
    // not needed
  }

  private Field field;


  public RemoveAiraSuffix(Field field) {
    this.field = field;
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if ("PID".equals(segmentName)) {
          int fieldPos = 5;
          int subPos = 1;
          if (field == Field.LAST_NAME) {
            subPos = 1;
          } else if (field == Field.FIRST_NAME) {
            subPos = 2;
          } else if (field == Field.MOTHERS_MAIDEN_NAME) {
            fieldPos = 6;
            subPos = 1;
          }
          String name = readValue(fields, fieldPos, subPos);
          if (name.endsWith("AIRA")) {
            updateValue(removeSuffix(name), fields, fieldPos, subPos);
          }
        }

      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }
  
  protected static String removeSuffix(String name) {
    return name.substring(0, name.length() - 4);
  }
}
