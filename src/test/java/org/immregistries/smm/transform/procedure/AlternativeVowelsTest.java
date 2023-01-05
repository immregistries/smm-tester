package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;

import java.util.Arrays;
import java.util.List;

import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class AlternativeVowelsTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    // Testing multiple times to make sure random process is consistent
    
    List<List<String>> locationProcedurePairs = 
      Arrays.asList(
        Arrays.asList("PID-5.1", ProcedureFactory.LAST_NAME_ALTERNATIVE_VOWELS),
        Arrays.asList("PID-5.2", ProcedureFactory.FIRST_NAME_ALTERNATIVE_VOWELS),
        Arrays.asList("PID-5.3", ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_VOWELS),
        Arrays.asList("PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_VOWELS)
      );
    
    for(List<String> pair : locationProcedurePairs) {
      for(int i = 0; i < 5; i++) {
        String location = pair.get(0);
        String procedure = pair.get(1);
        
        testVariationDifferent("Sammie", location, procedure, transformer);
        testVariationDifferent("Herbert", location, procedure, transformer);
        testVariationDifferent("Greyson", location, procedure, transformer);
        testVariationDifferent("Emily", location, procedure, transformer);
        testVariationDifferent("Earl", location, procedure, transformer);
        testVariationDifferent("Bob", location, procedure, transformer);
        testVariationDifferent("Doug", location, procedure, transformer);
        testVariationDifferent("Kayla", location, procedure, transformer);
        testVariationDifferent("Goose", location, procedure, transformer);
        testVariationDifferent("Mouse", location, procedure, transformer);
        testVariationDifferent("Chewie", location, procedure, transformer);
        
        //won't change first letter so "i" or "e" has to change
        assertTrue(AlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));
        
        //only vowel is the first and we're not changing that
        testVariation("Ann", "Ann", location, procedure, transformer);
      }
    }
  }
  
  
  private void testVariation(String startValue, String endValue, String location, String procedure, Transformer transformer) {
    assertEquals(endValue, AlternativeVowels.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }
  
  private void testVariationDifferent(String startValue, String location, String procedure, Transformer transformer) {
    String variation = AlternativeVowels.varyName(startValue, transformer);
    assertNotEquals(startValue, variation);
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessage(testStart, procedure);
  }

}
