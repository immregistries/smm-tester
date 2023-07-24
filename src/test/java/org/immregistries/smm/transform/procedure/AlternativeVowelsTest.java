package org.immregistries.smm.transform.procedure;

import java.util.Arrays;
import java.util.List;
import org.immregistries.smm.tester.manager.HL7Reader;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class AlternativeVowelsTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer transformer = new Transformer();
    // Testing multiple times to make sure random process is consistent

    List<List<String>> locationProcedurePairs =
        Arrays.asList(Arrays.asList("PID-5.1", ProcedureFactory.LAST_NAME_ALTERNATIVE_VOWELS),
            Arrays.asList("PID-5.2", ProcedureFactory.FIRST_NAME_ALTERNATIVE_VOWELS),
            Arrays.asList("PID-5.3", ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_VOWELS),
            Arrays.asList("PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_VOWELS),
            Arrays.asList("PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_VOWELS),
            Arrays.asList("PID-11.1", ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_VOWELS),
            Arrays.asList("PID-11.3", ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_VOWELS),
            Arrays.asList("PID-13.4", ProcedureFactory.EMAIL_ALTERNATIVE_VOWELS));

    for (List<String> pair : locationProcedurePairs) {
      for (int i = 0; i < 100; i++) {
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
        testVariationDifferent("100 Figaro Ave.", location, procedure, transformer);
        testVariationDifferent("Bobby Lee", location, procedure, transformer);
        testVariationDifferent("Green Bay", location, procedure, transformer);
        testVariationDifferent("test.email@gmail.com", location, procedure, transformer);
        testVariationDifferent("", location, procedure, transformer);

        // won't change first letter so "i" or "e" has to change
        assertTrue(AlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));

        // only vowel is the first and we're not changing that
        testVariation("Ann", "Ann", location, procedure, transformer);
      }
    }
  }


  private void testVariation(String startValue, String endValue, String location, String procedure,
      Transformer transformer) {
    if (ProcedureFactory.EMAIL_ALTERNATIVE_VOWELS.equals(procedure)) {
      return;
    }

    assertEquals(endValue, AlternativeVowels.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer) {
    // the documentation for this procedure states a substitution is not guaranteed
    // so guaranteeing a test that passes 100% of the time is difficult without making it complex

    String variation = AlternativeVowels.varyName(startValue, transformer);
    assertTrue(startValue.equals(variation) || variation.startsWith(startValue.substring(0, 1)));

    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String result = processProcedureChangesMessage(testStart, procedure);
    HL7Reader reader = new HL7Reader(result);
    reader.advanceToSegment("PID");

    // looking up something like PID-5.1
    String[] positions = location.split("-")[1].split("\\.");
    String procedureResult =
        reader.getValue(Integer.parseInt(positions[0]), Integer.parseInt(positions[1]));

    assertTrue(startValue.equals(procedureResult)
        || procedureResult.startsWith(startValue.substring(0, 1)));
  }

}
