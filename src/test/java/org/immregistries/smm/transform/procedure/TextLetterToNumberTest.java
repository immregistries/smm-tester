package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class TextLetterToNumberTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer transformer = new Transformer();

    assertEquals("A", new TextLetterToNumber(null).varyText("A", transformer));
    assertEquals("a", new TextLetterToNumber(null).varyText("a", transformer));

    assertEquals("O3", new TextLetterToNumber(null).varyText("Ox", transformer));
    assertEquals("d4", new TextLetterToNumber(null).varyText("da", transformer));
    assertEquals("E0", new TextLetterToNumber(null).varyText("ED", transformer));

    testVariationDifferent("CARPENTER", "PID-5.1", ProcedureFactory.LAST_NAME_LETTER_TO_NUMBER,
        transformer);
    testVariationDifferent("Washington", "PID-5.1", ProcedureFactory.LAST_NAME_LETTER_TO_NUMBER,
        transformer);
    testVariationDifferent("Hyphen-nated", "PID-5.1", ProcedureFactory.LAST_NAME_LETTER_TO_NUMBER,
        transformer);
    testVariationDifferent("Twolast Names", "PID-5.1", ProcedureFactory.LAST_NAME_LETTER_TO_NUMBER,
        transformer);

    testVariationDifferent("Samuel", "PID-5.2", ProcedureFactory.FIRST_NAME_LETTER_TO_NUMBER,
        transformer);
    testVariationDifferent("Emily", "PID-5.2", ProcedureFactory.FIRST_NAME_LETTER_TO_NUMBER,
        transformer);
    testVariationDifferent("Mary Sue", "PID-5.2", ProcedureFactory.FIRST_NAME_LETTER_TO_NUMBER,
        transformer);
    testVariationDifferent("Ye Sune", "PID-5.2", ProcedureFactory.FIRST_NAME_LETTER_TO_NUMBER,
        transformer);

    testVariationDifferent("Jennifer", "PID-5.3", ProcedureFactory.MIDDLE_NAME_LETTER_TO_NUMBER,
        transformer);
    testVariationDifferent("Ryan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_LETTER_TO_NUMBER,
        transformer);
    testVariationDifferent("Frederick Dempsey", "PID-5.3",
        ProcedureFactory.MIDDLE_NAME_LETTER_TO_NUMBER, transformer);
    testVariationDifferent("Stan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_LETTER_TO_NUMBER,
        transformer);

    testVariationDifferent("Grace", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_LETTER_TO_NUMBER, transformer);
    testVariationDifferent("Patrick", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_LETTER_TO_NUMBER, transformer);
    testVariationDifferent("Ziegfried", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_LETTER_TO_NUMBER, transformer);
    testVariationDifferent("Threepwood", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_LETTER_TO_NUMBER, transformer);

    testVariationDifferent("Sandra", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_LETTER_TO_NUMBER, transformer);
    testVariationDifferent("Emily", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_LETTER_TO_NUMBER,
        transformer);
    testVariationDifferent("Mary Sue", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_LETTER_TO_NUMBER, transformer);
    testVariationDifferent("Ye Sune", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_LETTER_TO_NUMBER, transformer);

    testVariationSame("4356180", "PID-13.7", ProcedureFactory.PHONE_LETTER_TO_NUMBER, transformer);
    testVariationSame("1234567", "PID-13.7", ProcedureFactory.PHONE_LETTER_TO_NUMBER, transformer);
    testVariationSame("555-5555", "PID-13.7", ProcedureFactory.PHONE_LETTER_TO_NUMBER, transformer);
    testVariationDifferent("555-5555 ext. 555", "PID-13.7", ProcedureFactory.PHONE_LETTER_TO_NUMBER,
        transformer);

    testVariationDifferent("something@gmail.com", "PID-13.4",
        ProcedureFactory.EMAIL_LETTER_TO_NUMBER, transformer);
    testVariationDifferent("plus+sign@apple.net", "PID-13.4",
        ProcedureFactory.EMAIL_LETTER_TO_NUMBER, transformer);

    testVariationDifferent("5678 Wooster Ln", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_LETTER_TO_NUMBER, transformer);
    testVariationDifferent("1234 Howard Drive", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_LETTER_TO_NUMBER, transformer);
    testVariationDifferent("600 Denter St.", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_LETTER_TO_NUMBER, transformer);

    testVariationDifferent("Madison", "PID-11.3", ProcedureFactory.ADDRESS_CITY_LETTER_TO_NUMBER,
        transformer);
    testVariationDifferent("New York City", "PID-11.3",
        ProcedureFactory.ADDRESS_CITY_LETTER_TO_NUMBER, transformer);
    testVariationDifferent("Martha's Vineyard", "PID-11.3",
        ProcedureFactory.ADDRESS_CITY_LETTER_TO_NUMBER, transformer);

    // assert only changes required field
    testWrongHookup("Washington", "PID-5.1", ProcedureFactory.LAST_NAME_LETTER_TO_NUMBER,
        transformer);
    testWrongHookup("Robert", "PID-5.2", ProcedureFactory.FIRST_NAME_LETTER_TO_NUMBER, transformer);
    testWrongHookup("Middleditch", "PID-5.3", ProcedureFactory.MIDDLE_NAME_LETTER_TO_NUMBER,
        transformer);
    testWrongHookup("Madre", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_LETTER_TO_NUMBER,
        transformer);
    testWrongHookup("Judy", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_LETTER_TO_NUMBER,
        transformer);
    testWrongHookup("5678 Wooster Ln", "PID-11.1", ProcedureFactory.ADDRESS_STREET_LETTER_TO_NUMBER,
        transformer);
    testWrongHookup("New York City", "PID-11.3", ProcedureFactory.ADDRESS_CITY_LETTER_TO_NUMBER,
        transformer);
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer) {
    assertNotEquals(startValue, new TextLetterToNumber(null).varyText(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessageAndDoesNotContain(testStart, startValue, procedure);
  }

  private void testVariationSame(String startValue, String location, String procedure,
      Transformer transformer) {
    assertEquals(startValue, new TextLetterToNumber(null).varyText(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureDoesNotChangeMessage(testStart, startValue, procedure);
  }

  // change the referenced PID
  // but then test every field but the one passed in to make sure they didn't change
  private void testWrongHookup(String startValue, String location, String procedure,
      Transformer transformer) {
    List<String> procedures = new ArrayList<>(Arrays.asList(
        ProcedureFactory.FIRST_NAME_LETTER_TO_NUMBER, ProcedureFactory.MIDDLE_NAME_LETTER_TO_NUMBER,
        ProcedureFactory.LAST_NAME_LETTER_TO_NUMBER,
        ProcedureFactory.MOTHERS_MAIDEN_NAME_LETTER_TO_NUMBER,
        ProcedureFactory.MOTHERS_FIRST_NAME_LETTER_TO_NUMBER,
        ProcedureFactory.ADDRESS_STREET_LETTER_TO_NUMBER,
        ProcedureFactory.ADDRESS_CITY_LETTER_TO_NUMBER, ProcedureFactory.EMAIL_LETTER_TO_NUMBER));

    procedures.remove(procedure);

    for (String proc : procedures) {
      assertNotEquals(startValue, new TextLetterToNumber(null).varyText(startValue, transformer));
      String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
      testProcedureChangesMessageAndDoesContain(testStart, startValue, proc);
    }
  }
}
