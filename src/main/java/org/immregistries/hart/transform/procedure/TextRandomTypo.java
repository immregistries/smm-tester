package org.immregistries.hart.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.hart.transform.TransformRequest;
import org.immregistries.hart.transform.Transformer;
import org.immregistries.hart.transform.procedure.AbstractTypoProcedure.Field;

public class TextRandomTypo extends ProcedureCommon {

  public TextRandomTypo(AbstractTypoProcedure.Field field) {
    this.field = field;
  }

  AbstractTypoProcedure.Field field;
  private Transformer transformer;

  private List<AbstractTypoProcedure> getTypoProceduresInRandomOrder() {
    // produce a random order of typo procedures
    List<AbstractTypoProcedure> typoProcs = new ArrayList<>();
    typoProcs.add(new TextTypo(field));
    typoProcs.add(new TextTransposeTypo(field));
    typoProcs.add(new TextLetterToNumberTypo(field));
    typoProcs.add(new TextNumberToLetterTypo(field));
    Collections.shuffle(typoProcs);

    return typoProcs;
  }

  @Override
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {

    // save the original text, we want to actually try to have a typo happen
    // so that means trying all the procedures until the result changes
    String before = transformRequest.getResultText();

    for (AbstractTypoProcedure proc : getTypoProceduresInRandomOrder()) {
      proc.setTransformer(transformer);
      proc.doProcedure(transformRequest, tokenList);
      String after = transformRequest.getResultText();
      if (!before.equals(after)) {
        // got a typo, so break out
        break;
      }
    }
  }

  // only used for testing at the moment
  protected String varyText(String name, Transformer transformer) {
    for (AbstractTypoProcedure proc : getTypoProceduresInRandomOrder()) {
      String after = proc.varyText(name, transformer);

      if (field == Field.EMAIL) {
        name = makeEmailValid(name);
      }

      if (!name.equals(after)) {
        // got a typo, so break out
        return after;
      }
    }

    return name;
  }

  @Override
  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }
}
