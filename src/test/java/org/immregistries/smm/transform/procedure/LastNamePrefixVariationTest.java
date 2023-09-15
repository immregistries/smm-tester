package org.immregistries.smm.transform.procedure;

import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class LastNamePrefixVariationTest extends ProcedureCommonTest {


  @Test
  public void test() {
    
    Transformer transformer = new Transformer();
    assertTrue(LastNamePrefixVariation.varyName("Smith", transformer).endsWith(" Smith"));
    assertTrue(LastNamePrefixVariation.varyName("Von", transformer).startsWith("Von "));
    assertTrue(LastNamePrefixVariation.varyName("smith", transformer).endsWith(" smith"));
    assertTrue(LastNamePrefixVariation.varyName("SMITH", transformer).endsWith(" SMITH"));
    
    testVariation("Von Washington", "VonWashington", transformer);
    testVariation("VonWashington", "Von Washington", transformer);
    testVariation("De La Washington", "DeLaWashington", transformer);
    testVariation("DeLaWashington", "De La Washington", transformer);
    
  }
  
  private void testVariation(String startValue, String endValue, Transformer transformer) {
    assertEquals(endValue, LastNamePrefixVariation.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.1=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-5.1=" + endValue);
    testEquals(testStart, testEnd, ProcedureFactory.LAST_NAME_PREFIX_VARIATION);
  }

}
