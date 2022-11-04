package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class LastNameHyphenateVariationTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    testVariation("Smith-Jones", "Smith Jones", transformer);
    testVariation("Smith Jones", "Smith-Jones", transformer);
    testVariation("Smith-jones", "Smith Jones", transformer);
    testVariation("smith-jones", "smith jones", transformer);
    assertEquals("Smith-Jones", LastNameHyphenateVariation
        .varyName(LastNameHyphenateVariation.varyName("Smith-Jones", transformer), transformer));
    testVariationDifferent("Smith", transformer);
    testVariationDifferent("JONES", transformer);
    testVariationDifferent("carpenter", transformer);

  }


  protected void testVariation(String startValue, String endValue, Transformer transformer) {
    assertEquals(endValue, LastNameHyphenateVariation.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.1=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-5.1=" + endValue);
    testEquals(testStart, testEnd, ProcedureFactory.LAST_NAME_HYPHENATE_VARIATION);
  }

  protected void testVariationDifferent(String startValue, Transformer transformer) {
    assertNotEquals(startValue, LastNameHyphenateVariation.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.1=" + startValue);
    testProcedureChangesMessage(testStart, ProcedureFactory.LAST_NAME_HYPHENATE_VARIATION);
  }

}
