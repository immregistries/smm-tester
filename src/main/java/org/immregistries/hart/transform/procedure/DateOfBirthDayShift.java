package org.immregistries.hart.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.hart.transform.TransformRequest;
import org.immregistries.hart.transform.Transformer;
import org.immregistries.hart.util.DateOfBirth;

public class DateOfBirthDayShift extends ProcedureCommon {

  public DateOfBirthDayShift() { }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if ("PID".equals(segmentName)) {
          int fieldPos = 7;
          int subPos = 1;
          DateOfBirth dob = new DateOfBirth(readValue(fields, fieldPos, subPos));
          updateValue(dob.decrementDay(), fields, fieldPos, subPos);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  public void setTransformer(Transformer transformer) {}

}
