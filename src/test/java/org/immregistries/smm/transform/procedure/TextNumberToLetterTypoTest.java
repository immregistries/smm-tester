package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class TextNumberToLetterTypoTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer transformer = new Transformer();

    for (int i = 0; i < 100; i++) {
      assertEquals("A", new TextNumberToLetterTypo(null).varyText("A", transformer));
      assertEquals("a", new TextNumberToLetterTypo(null).varyText("a", transformer));
      assertEquals("0", new TextNumberToLetterTypo(null).varyText("0", transformer));
      assertEquals("", new TextNumberToLetterTypo(null).varyText("", transformer));

      assertEquals("1st Son", new TextNumberToLetterTypo(null).varyText("1st Son", transformer));

      assertEquals("Bob", new TextNumberToLetterTypo(null).varyText("Bob", transformer));
      assertEquals("Robert Paulson",
          new TextNumberToLetterTypo(null).varyText("Robert Paulson", transformer));
      assertEquals("Grant-Bob",
          new TextNumberToLetterTypo(null).varyText("Grant-Bob", transformer));

      testVariationDifferent("CARPENTER5", "PID-5.1", ProcedureFactory.LAST_NAME_NUMBER_TO_LETTER,
          transformer);
      testVariationDifferent("Washington 16", "PID-5.1",
          ProcedureFactory.LAST_NAME_NUMBER_TO_LETTER, transformer);
      testVariationDifferent("Hyphen-n8d", "PID-5.1", ProcedureFactory.LAST_NAME_NUMBER_TO_LETTER,
          transformer);
      testVariationSame("Twolast Names", "PID-5.1", ProcedureFactory.LAST_NAME_NUMBER_TO_LETTER,
          transformer);

      testVariationDifferent("Samu3l", "PID-5.2", ProcedureFactory.FIRST_NAME_NUMBER_TO_LETTER,
          transformer);
      testVariationDifferent("Emily the 2nd", "PID-5.2",
          ProcedureFactory.FIRST_NAME_NUMBER_TO_LETTER, transformer);
      testVariationDifferent("Mary 9ue", "PID-5.2", ProcedureFactory.FIRST_NAME_NUMBER_TO_LETTER,
          transformer);
      testVariationSame("Ye Sune", "PID-5.2", ProcedureFactory.FIRST_NAME_NUMBER_TO_LETTER,
          transformer);

      testVariationDifferent("Jenn1fer", "PID-5.3", ProcedureFactory.MIDDLE_NAME_NUMBER_TO_LETTER,
          transformer);
      testVariationDifferent("Ryan the 7th", "PID-5.3",
          ProcedureFactory.MIDDLE_NAME_NUMBER_TO_LETTER, transformer);
      testVariationDifferent("F5rederick Dempsey", "PID-5.3",
          ProcedureFactory.MIDDLE_NAME_NUMBER_TO_LETTER, transformer);
      testVariationSame("Stan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_NUMBER_TO_LETTER,
          transformer);

      testVariationDifferent("Grace 3", "PID-6.1",
          ProcedureFactory.MOTHERS_MAIDEN_NAME_NUMBER_TO_LETTER, transformer);
      testVariationDifferent("Patrick 4", "PID-6.1",
          ProcedureFactory.MOTHERS_MAIDEN_NAME_NUMBER_TO_LETTER, transformer);
      testVariationDifferent("Ziegfried 5", "PID-6.1",
          ProcedureFactory.MOTHERS_MAIDEN_NAME_NUMBER_TO_LETTER, transformer);
      testVariationSame("Threepwood", "PID-6.1",
          ProcedureFactory.MOTHERS_MAIDEN_NAME_NUMBER_TO_LETTER, transformer);

      testVariationDifferent("Sandra 7", "PID-6.2",
          ProcedureFactory.MOTHERS_FIRST_NAME_NUMBER_TO_LETTER, transformer);
      testVariationDifferent("Emily 8", "PID-6.2",
          ProcedureFactory.MOTHERS_FIRST_NAME_NUMBER_TO_LETTER, transformer);
      testVariationDifferent("Mary Sue 9", "PID-6.2",
          ProcedureFactory.MOTHERS_FIRST_NAME_NUMBER_TO_LETTER, transformer);
      testVariationSame("Ye Sune", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_NUMBER_TO_LETTER,
          transformer);

      testVariationDifferent("4356180", "PID-13.7", ProcedureFactory.PHONE_NUMBER_TO_LETTER,
          transformer);
      testVariationDifferent("1234567", "PID-13.7", ProcedureFactory.PHONE_NUMBER_TO_LETTER,
          transformer);
      testVariationDifferent("555-5555", "PID-13.7", ProcedureFactory.PHONE_NUMBER_TO_LETTER,
          transformer);
      testVariationDifferent("555-5555 ext. 555", "PID-13.7",
          ProcedureFactory.PHONE_NUMBER_TO_LETTER, transformer);

      testVariationDifferent("something64@gmail.com", "PID-13.4",
          ProcedureFactory.EMAIL_NUMBER_TO_LETTER, transformer);
      testVariationDifferent("plus1+1sign@apple.net", "PID-13.4",
          ProcedureFactory.EMAIL_NUMBER_TO_LETTER, transformer);

      testVariationDifferent("5678 Wooster Ln", "PID-11.1",
          ProcedureFactory.ADDRESS_STREET_NUMBER_TO_LETTER, transformer);
      testVariationDifferent("1234 Howard Drive", "PID-11.1",
          ProcedureFactory.ADDRESS_STREET_NUMBER_TO_LETTER, transformer);
      testVariationDifferent("600 Denter St.", "PID-11.1",
          ProcedureFactory.ADDRESS_STREET_NUMBER_TO_LETTER, transformer);

      testVariationDifferent("Madis0n", "PID-11.3", ProcedureFactory.ADDRESS_CITY_NUMBER_TO_LETTER,
          transformer);
      testVariationDifferent("New York C1ty", "PID-11.3",
          ProcedureFactory.ADDRESS_CITY_NUMBER_TO_LETTER, transformer);
      testVariationSame("Martha's Vineyard", "PID-11.3",
          ProcedureFactory.ADDRESS_CITY_NUMBER_TO_LETTER, transformer);

      // assert only changes required field
      testWrongHookup("Washington 1", "PID-5.1", ProcedureFactory.LAST_NAME_NUMBER_TO_LETTER,
          transformer);
      testWrongHookup("Robert 2", "PID-5.2", ProcedureFactory.FIRST_NAME_NUMBER_TO_LETTER,
          transformer);
      testWrongHookup("Middleditch 3", "PID-5.3", ProcedureFactory.MIDDLE_NAME_NUMBER_TO_LETTER,
          transformer);
      testWrongHookup("Madre 4", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_NUMBER_TO_LETTER,
          transformer);
      testWrongHookup("Judy 5", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_NUMBER_TO_LETTER,
          transformer);
      testWrongHookup("5678 Wooster Ln", "PID-11.1",
          ProcedureFactory.ADDRESS_STREET_NUMBER_TO_LETTER, transformer);
      testWrongHookup("New York City 6", "PID-11.3", ProcedureFactory.ADDRESS_CITY_NUMBER_TO_LETTER,
          transformer);
    }
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer) {
    assertNotEquals(startValue, new TextNumberToLetterTypo(null).varyText(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessageAndDoesNotContain(testStart, startValue, procedure);
  }

  private void testVariationSame(String startValue, String location, String procedure,
      Transformer transformer) {
    assertEquals(startValue, new TextNumberToLetterTypo(null).varyText(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureDoesNotChangeMessage(testStart, startValue, procedure);
  }

  // change the referenced PID
  // but then test every field but the one passed in to make sure they didn't change
  private void testWrongHookup(String startValue, String location, String procedure,
      Transformer transformer) {
    List<String> procedures =
        new ArrayList<>(Arrays.asList(ProcedureFactory.ADDRESS_STREET_NUMBER_TO_LETTER,
            ProcedureFactory.PHONE_NUMBER_TO_LETTER));

    procedures.remove(procedure);

    for (String proc : procedures) {
      assertNotEquals(startValue,
          new TextNumberToLetterTypo(null).varyText(startValue, transformer));
      String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
      testProcedureChangesMessageAndDoesContain(testStart, startValue, proc);
    }
  }
}
