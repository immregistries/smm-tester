package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class FirstNameAddVariation extends ProcedureCommon implements ProcedureInterface {



  public FirstNameAddVariation() {

  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("PID")) {
          int fieldPos = 5;
          int subPos = 2;
          String firstName = readValue(fields, fieldPos, subPos);
          int pos = firstName.indexOf('\'');
          if (pos > 0)
          {
            
          }
          
          
          updateValue(firstName, fields, fieldPos, subPos);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }


  public void setTransformer(Transformer transformer) {
    // not needed
  }



}
