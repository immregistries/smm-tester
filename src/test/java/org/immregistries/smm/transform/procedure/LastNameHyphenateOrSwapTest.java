package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class LastNameHyphenateOrSwapTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    testVariation("Smith-Jones","Jones-Smith", transformer);
    testVariation("Smith-jones","Jones-Smith", transformer);
    testVariation("smith-jones","jones-smith", transformer);
    assertEquals("Smith-Jones", LastNameHyphenateOrSwap
        .varyName(LastNameHyphenateOrSwap.varyName("Smith-Jones", transformer), transformer));
    testVariationDifferent("Smith", transformer);
    testVariationDifferent("JONES", transformer);
    testVariationDifferent("carpenter", transformer);

  }
  
  private void testVariation(String startValue, String endValue, Transformer transformer) {
    assertEquals(endValue, LastNameHyphenateOrSwap.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.1=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-5.1=" + endValue);
    testEquals(testStart, testEnd, ProcedureFactory.LAST_NAME_HYPHENATE_OR_SWAP);
  }
  
  private void testVariationDifferent(String startValue, Transformer transformer) {
    assertNotEquals(startValue, LastNameHyphenateOrSwap.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.1=" + startValue);
    testProcedureChangesMessage(testStart, ProcedureFactory.LAST_NAME_HYPHENATE_OR_SWAP);
  }

}
