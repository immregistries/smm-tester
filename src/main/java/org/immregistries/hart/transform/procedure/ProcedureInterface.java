package org.immregistries.hart.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import org.immregistries.hart.transform.TransformRequest;
import org.immregistries.hart.transform.Transformer;

public interface ProcedureInterface {
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException;

  public void setTransformer(Transformer transformer);
}
