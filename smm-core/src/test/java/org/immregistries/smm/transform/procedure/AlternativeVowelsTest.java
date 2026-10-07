package org.immregistries.smm.transform.procedure;

import java.util.Arrays;
import java.util.List;
import org.immregistries.smm.tester.manager.HL7Reader;
import org.immregistries.smm.transform.Transformer;
import org.immregistries.smm.transform.procedure.AlternativeVowels.Field;
import org.junit.Test;

public class AlternativeVowelsTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer transformer = new Transformer();
    // Testing multiple times to make sure random process is consistent

    List<Triplet> triplets = Arrays.asList(
        new Triplet("PID-5.1", ProcedureFactory.LAST_NAME_ALTERNATIVE_VOWELS, Field.LAST_NAME),
        new Triplet("PID-5.2", ProcedureFactory.FIRST_NAME_ALTERNATIVE_VOWELS, Field.FIRST_NAME),
        new Triplet("PID-5.3", ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_VOWELS, Field.MIDDLE_NAME),
        new Triplet("PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_VOWELS,
            Field.MOTHERS_MAIDEN_NAME),
        new Triplet("PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_VOWELS,
            Field.MOTHERS_FIRST_NAME),
        new Triplet("PID-11.1", ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_VOWELS,
            Field.ADDRESS_STREET),
        new Triplet("PID-11.3", ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_VOWELS,
            Field.ADDRESS_CITY),
        new Triplet("PID-13.4", ProcedureFactory.EMAIL_ALTERNATIVE_VOWELS, Field.EMAIL));

    for (Triplet trip : triplets) {
      for (int i = 0; i < 100; i++) {
        testVariationDifferent("Sammie", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("Herbert", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("Greyson", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("Emily", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("Earl", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("Bob", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("Doug", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("Kayla", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("Goose", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("Mouse", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("Chewie", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("100 Figaro Ave.", trip.location, trip.procedure, transformer,
            trip.field);
        testVariationDifferent("Bobby Lee", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("Green Bay", trip.location, trip.procedure, transformer, trip.field);
        testVariationDifferent("test.email@gmail.com", trip.location, trip.procedure, transformer,
            trip.field);
        testVariationDifferent("", trip.location, trip.procedure, transformer, trip.field);

        // won't change first letter so "i" or "e" has to change
        assertTrue(AlternativeVowels.varyName("Annie", transformer, trip.field).startsWith("Ann"));

        // only vowel is the first and we're not changing that
        testVariation("Ann", "Ann", trip.location, trip.procedure, transformer, trip.field);
      }
    }
  }

  private void testVariation(String startValue, String endValue, String location, String procedure,
      Transformer transformer, Field field) {
    if (ProcedureFactory.EMAIL_ALTERNATIVE_VOWELS.equals(procedure)) {
      return;
    }

    assertEquals(endValue, AlternativeVowels.varyName(startValue, transformer, field));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer, Field field) {
    // the documentation for this procedure states a substitution is not guaranteed
    // so guaranteeing a test that passes 100% of the time is difficult without making it complex

    String variation = AlternativeVowels.varyName(startValue, transformer, field);
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

  private class Triplet {
    String location;
    String procedure;
    Field field;

    Triplet(String location, String procedure, Field field) {
      this.location = location;
      this.procedure = procedure;
      this.field = field;
    }
  }
}
