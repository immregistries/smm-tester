package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class FirstNameRepeatedConsonantsTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    testVariationDifferent("Sammie", transformer);
    testVariationDifferent("Sam", transformer);
    testVariationDifferent("Kalyn", transformer);
    testVariationDifferent("Howard", transformer);
    testVariationDifferent("Heather", transformer);
    testVariationDifferent("Ivan", transformer);
  }
  
  protected void testVariationDifferent(String startValue, Transformer transformer) {
    assertNotEquals(startValue, FirstNameRepeatedConsonants.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + startValue);
    testProcedureChangesMessage(testStart, ProcedureFactory.FIRST_NAME_REPEATED_CONSONANTS);
  }

}
