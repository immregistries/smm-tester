package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class TextTypoTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();

    for (int i = 0; i < 100; i++) {
      testVariationDifferent("CARPENTER", "PID-5.1", ProcedureFactory.LAST_NAME_TYPO, transformer);
      testVariationDifferent("Washington", "PID-5.1", ProcedureFactory.LAST_NAME_TYPO, transformer);
      testVariationDifferent("Hyphen-nated", "PID-5.1", ProcedureFactory.LAST_NAME_TYPO,
          transformer);
      testVariationDifferent("Twolast Names", "PID-5.1", ProcedureFactory.LAST_NAME_TYPO,
          transformer);

      testVariationDifferent("Samuel", "PID-5.2", ProcedureFactory.FIRST_NAME_TYPO, transformer);
      testVariationDifferent("Emily", "PID-5.2", ProcedureFactory.FIRST_NAME_TYPO, transformer);
      testVariationDifferent("Mary Sue", "PID-5.2", ProcedureFactory.FIRST_NAME_TYPO, transformer);
      testVariationDifferent("Ye Sune", "PID-5.2", ProcedureFactory.FIRST_NAME_TYPO, transformer);

      testVariationDifferent("Jennifer", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TYPO, transformer);
      testVariationDifferent("Ryan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TYPO, transformer);
      testVariationDifferent("Frederick Dempsey", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TYPO,
          transformer);
      testVariationDifferent("Stan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TYPO, transformer);

      testVariationDifferent("Grace", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TYPO,
          transformer);
      testVariationDifferent("Patrick", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TYPO,
          transformer);
      testVariationDifferent("Ziegfried", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TYPO,
          transformer);
      testVariationDifferent("Threepwood", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TYPO,
          transformer);

      testVariationDifferent("Sandra", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_TYPO,
          transformer);
      testVariationDifferent("Emily", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_TYPO,
          transformer);
      testVariationDifferent("Mary Sue", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_TYPO,
          transformer);
      testVariationDifferent("Ye Sune", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_TYPO,
          transformer);

      testVariationDifferent("4356180", "PID-13.7", ProcedureFactory.PHONE_TYPO, transformer);
      testVariationDifferent("1234567", "PID-13.7", ProcedureFactory.PHONE_TYPO, transformer);
      testVariationDifferent("555-5555", "PID-13.7", ProcedureFactory.PHONE_TYPO, transformer);

      testVariationDifferent("something@gmail.com", "PID-13.4", ProcedureFactory.EMAIL_TYPO,
          transformer);
      testVariationDifferent("plus+sign@apple.net", "PID-13.4", ProcedureFactory.EMAIL_TYPO,
          transformer);

      testVariationDifferent("5678 Wooster Ln", "PID-11.1", ProcedureFactory.ADDRESS_STREET_TYPO,
          transformer);
      testVariationDifferent("1234 Howard Drive", "PID-11.1", ProcedureFactory.ADDRESS_STREET_TYPO,
          transformer);
      testVariationDifferent("600 Denter St.", "PID-11.1", ProcedureFactory.ADDRESS_STREET_TYPO,
          transformer);

      testVariationDifferent("Madison", "PID-11.3", ProcedureFactory.ADDRESS_CITY_TYPO,
          transformer);
      testVariationDifferent("New York City", "PID-11.3", ProcedureFactory.ADDRESS_CITY_TYPO,
          transformer);
      testVariationDifferent("Martha's Vineyard", "PID-11.3", ProcedureFactory.ADDRESS_CITY_TYPO,
          transformer);

      // assert only changes required field
      testWrongHookup("Washington", "PID-5.1", ProcedureFactory.LAST_NAME_TYPO, transformer);
      testWrongHookup("Robert", "PID-5.2", ProcedureFactory.FIRST_NAME_TYPO, transformer);
      testWrongHookup("Middleditch", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TYPO, transformer);
      testWrongHookup("Madre", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TYPO, transformer);
      testWrongHookup("Judy", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_TYPO, transformer);
      testWrongHookup("4356180", "PID-13.7", ProcedureFactory.PHONE_TYPO, transformer);
      testWrongHookup("5678 Wooster Ln", "PID-11.1", ProcedureFactory.ADDRESS_STREET_TYPO,
          transformer);
      testWrongHookup("New York City", "PID-11.3", ProcedureFactory.ADDRESS_CITY_TYPO, transformer);

      // support empty string doesn't do anything
      testVariationSame("", "PID-5.1", ProcedureFactory.LAST_NAME_TYPO, transformer);
      testVariationSame("", "PID-5.2", ProcedureFactory.FIRST_NAME_TYPO, transformer);
      testVariationSame("", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TYPO, transformer);
      testVariationSame("", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TYPO, transformer);
      testVariationSame("", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_TYPO, transformer);
      testVariationSame("", "PID-13.7", ProcedureFactory.PHONE_TYPO, transformer);
      testVariationSame("", "PID-11.1", ProcedureFactory.ADDRESS_STREET_TYPO, transformer);
      testVariationSame("", "PID-11.3", ProcedureFactory.ADDRESS_CITY_TYPO, transformer);
    }
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer) {
    assertNotEquals(startValue, new TextTypo(null).varyText(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessageAndDoesNotContain(testStart, startValue, procedure);
  }

  private void testVariationSame(String startValue, String location, String procedure,
      Transformer transformer) {
    assertEquals(startValue, new TextTypo(null).varyText(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureDoesNotChangeMessage(testStart, startValue, procedure);
  }

  // change the referenced PID
  // but then test every field but the one passed in to make sure they didn't change
  private void testWrongHookup(String startValue, String location, String procedure,
      Transformer transformer) {
    List<String> procedures = new ArrayList<>(Arrays.asList(ProcedureFactory.FIRST_NAME_TYPO,
        ProcedureFactory.MIDDLE_NAME_TYPO, ProcedureFactory.LAST_NAME_TYPO,
        ProcedureFactory.MOTHERS_MAIDEN_NAME_TYPO, ProcedureFactory.MOTHERS_FIRST_NAME_TYPO,
        ProcedureFactory.ADDRESS_STREET_TYPO, ProcedureFactory.ADDRESS_CITY_TYPO,
        ProcedureFactory.PHONE_TYPO, ProcedureFactory.EMAIL_TYPO));

    procedures.remove(procedure);

    for (String proc : procedures) {
      assertNotEquals(startValue, new TextTypo(null).varyText(startValue, transformer));
      String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
      testProcedureChangesMessageAndDoesContain(testStart, startValue, proc);
    }
  }
}
