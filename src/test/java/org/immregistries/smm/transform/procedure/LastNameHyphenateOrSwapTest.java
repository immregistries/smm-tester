package org.immregistries.smm.transform.procedure;

import org.immregistries.smm.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class LastNameHyphenateOrSwapTest extends TestCase {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    assertEquals("Jones-Smith", LastNameHyphenateOrSwap.varyName("Smith-Jones", transformer));
    assertEquals("Jones-Smith", LastNameHyphenateOrSwap.varyName("Smith-jones", transformer));
    assertEquals("jones-smith", LastNameHyphenateOrSwap.varyName("smith-jones", transformer));
    assertEquals("Smith-Jones", LastNameHyphenateOrSwap
        .varyName(LastNameHyphenateOrSwap.varyName("Smith-Jones", transformer), transformer));
    LastNameHyphenateOrSwap.varyName("Smith", transformer);
    LastNameHyphenateOrSwap.varyName("JONES", transformer);
    LastNameHyphenateOrSwap.varyName("carpenter", transformer);

  }

}
