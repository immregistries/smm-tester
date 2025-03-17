package org.immregistries.hart.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.hart.transform.TransformRequest;
import org.immregistries.hart.transform.Transformer;

public class AdministrativeSexVariation extends ProcedureCommon {

  public AdministrativeSexVariation() {
  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if ("PID".equals(segmentName)) {
          int fieldPos = 8;
          int subPos = 1;
          String administrativeSex = readValue(fields, fieldPos, subPos).trim();
          administrativeSex = varyCode(administrativeSex);
          updateValue(administrativeSex, fields, fieldPos, subPos);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyCode(String administrativeSex) {
    if (administrativeSex.equalsIgnoreCase("M")) {
      administrativeSex = "F";
    } else if (administrativeSex.equalsIgnoreCase("F")) {
      administrativeSex = "M";
    } else if (administrativeSex.equalsIgnoreCase("U")) {
      administrativeSex = "F";
    } else {
      administrativeSex = "U";
    }
    return administrativeSex;
  }

  public void setTransformer(Transformer transformer) {
    // not needed
  }
}
