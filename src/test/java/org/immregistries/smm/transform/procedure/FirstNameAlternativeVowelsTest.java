package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class FirstNameAlternativeVowelsTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    // Testing multiple times to make sure random process is consistent
    testVariationDifferent("Sammie", transformer);
    testVariationDifferent("Sammie", transformer);
    testVariationDifferent("Sammie", transformer);
    testVariationDifferent("Sammie", transformer);
    testVariationDifferent("Sammie", transformer);
    testVariationDifferent("Herbert", transformer);
    testVariationDifferent("Herbert", transformer);
    testVariationDifferent("Herbert", transformer);
    testVariationDifferent("Herbert", transformer);
    testVariationDifferent("Greyson", transformer);
    testVariationDifferent("Greyson", transformer);
    testVariationDifferent("Greyson", transformer);
    testVariationDifferent("Greyson", transformer);
    testVariationDifferent("Greyson", transformer);
    testVariationDifferent("Emily", transformer);
    testVariationDifferent("Emily", transformer);
    testVariationDifferent("Emily", transformer);
    testVariationDifferent("Emily", transformer);
    testVariationDifferent("Emily", transformer);
    testVariationDifferent("Emily", transformer);
    testVariationDifferent("Earl", transformer);
    testVariationDifferent("Earl", transformer);
    testVariationDifferent("Earl", transformer);
    testVariationDifferent("Earl", transformer);
    testVariationDifferent("Earl", transformer);

    assertTrue(FirstNameAlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));
    assertTrue(FirstNameAlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));
    assertTrue(FirstNameAlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));
    assertTrue(FirstNameAlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));
    assertTrue(FirstNameAlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));
    testVariation("Ann", "Ann", transformer);
    testVariation("Ann", "Ann", transformer);
    testVariation("Ann", "Ann", transformer);
    testVariation("Ann", "Ann", transformer);
    testVariation("Ann", "Ann", transformer);
    testVariation("Ann", "Ann", transformer);
  }
  
  
  private void testVariation(String startValue, String endValue, Transformer transformer) {
    assertEquals(endValue, FirstNameAlternativeVowels.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + endValue);
    testEquals(testStart, testEnd, ProcedureFactory.FIRST_NAME_ALTERNATIVE_VOWELS);
  }
  
  private void testVariationDifferent(String startValue, Transformer transformer) {
    assertNotEquals(startValue, FirstNameAlternativeVowels.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + startValue);
    testProcedureChangesMessage(testStart, ProcedureFactory.FIRST_NAME_ALTERNATIVE_VOWELS);
  }

}
