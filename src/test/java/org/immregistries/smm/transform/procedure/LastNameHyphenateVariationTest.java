package org.immregistries.smm.transform.procedure;

import org.immregistries.smm.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class LastNameHyphenateVariationTest extends TestCase {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    assertEquals("Smith Jones", LastNameHyphenateVariation.varyName("Smith-Jones", transformer));
    assertEquals("Smith-Jones", LastNameHyphenateVariation.varyName("Smith Jones", transformer));
    assertEquals("Smith Jones", LastNameHyphenateVariation.varyName("Smith-jones", transformer));
    assertEquals("smith jones", LastNameHyphenateVariation.varyName("smith-jones", transformer));
    assertEquals("Smith-Jones", LastNameHyphenateVariation
        .varyName(LastNameHyphenateVariation.varyName("Smith-Jones", transformer), transformer));
    LastNameHyphenateVariation.varyName("Smith", transformer);
    LastNameHyphenateVariation.varyName("JONES", transformer);
    LastNameHyphenateVariation.varyName("carpenter", transformer);

  }

}
