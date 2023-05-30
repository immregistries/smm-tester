package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class TextRandomTypo extends ProcedureCommon implements ProcedureInterface {

  public TextRandomTypo(AbstractTypoProcedure.Field field) {
    this.field = field;
  }

  AbstractTypoProcedure.Field field;
  private Transformer transformer;

  @Override
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {

    // produce a random order of typo procedures
    List<AbstractTypoProcedure> typoProcs = new ArrayList<>();
    typoProcs.add(new TextTypo(field));
    typoProcs.add(new TextTransposeTypo(field));
    typoProcs.add(new TextLetterToNumberTypo(field));
    typoProcs.add(new TextNumberToLetterTypo(field));
    Collections.shuffle(typoProcs);

    // save the original text, we want to actually try to have a typo happen
    // so that means trying all the procedures until the result changes
    String before = transformRequest.getResultText();

    for (AbstractTypoProcedure proc : typoProcs) {
      proc.setTransformer(transformer);
      proc.doProcedure(transformRequest, tokenList);
      String after = transformRequest.getResultText();
      if (!before.equals(after)) {
        // got a typo, so break out
        break;
      }
    }
  }

  @Override
  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }
}
