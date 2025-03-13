package org.immregistries.hart.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.hart.transform.Transformer;
import org.junit.Test;

public class TextRandomTypoTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer transformer = new Transformer();

    for (int i = 0; i < 100; i++) {
      testVariationDifferent("CARPENTER", "PID-5.1", ProcedureFactory.LAST_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Washington", "PID-5.1", ProcedureFactory.LAST_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Hyphen-nated", "PID-5.1", ProcedureFactory.LAST_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Twolast Names", "PID-5.1", ProcedureFactory.LAST_NAME_RANDOM_TYPO,
          transformer);

      testVariationDifferent("Samuel", "PID-5.2", ProcedureFactory.FIRST_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Emily", "PID-5.2", ProcedureFactory.FIRST_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Mary Sue", "PID-5.2", ProcedureFactory.FIRST_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Ye Sune", "PID-5.2", ProcedureFactory.FIRST_NAME_RANDOM_TYPO,
          transformer);

      testVariationDifferent("Jennifer", "PID-5.3", ProcedureFactory.MIDDLE_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Ryan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Frederick Dempsey", "PID-5.3",
          ProcedureFactory.MIDDLE_NAME_RANDOM_TYPO, transformer);
      testVariationDifferent("Stan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_RANDOM_TYPO,
          transformer);

      testVariationDifferent("Grace", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Patrick", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Ziegfried", "PID-6.1",
          ProcedureFactory.MOTHERS_MAIDEN_NAME_RANDOM_TYPO, transformer);
      testVariationDifferent("Threepwood", "PID-6.1",
          ProcedureFactory.MOTHERS_MAIDEN_NAME_RANDOM_TYPO, transformer);

      testVariationDifferent("Sandra", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Emily", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Mary Sue", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Ye Sune", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_RANDOM_TYPO,
          transformer);

      testVariationDifferent("4356180", "PID-13.7", ProcedureFactory.PHONE_RANDOM_TYPO,
          transformer);
      testVariationDifferent("1234567", "PID-13.7", ProcedureFactory.PHONE_RANDOM_TYPO,
          transformer);
      testVariationDifferent("555-5555", "PID-13.7", ProcedureFactory.PHONE_RANDOM_TYPO,
          transformer);

      testVariationDifferent("something@gmail.com", "PID-13.4", ProcedureFactory.EMAIL_RANDOM_TYPO,
          transformer);
      testVariationDifferent("plus+sign@apple.net", "PID-13.4", ProcedureFactory.EMAIL_RANDOM_TYPO,
          transformer);

      testVariationDifferent("5678 Wooster Ln", "PID-11.1",
          ProcedureFactory.ADDRESS_STREET_RANDOM_TYPO, transformer);
      testVariationDifferent("1234 Howard Drive", "PID-11.1",
          ProcedureFactory.ADDRESS_STREET_RANDOM_TYPO, transformer);
      testVariationDifferent("600 Denter St.", "PID-11.1",
          ProcedureFactory.ADDRESS_STREET_RANDOM_TYPO, transformer);

      testVariationDifferent("Madison", "PID-11.3", ProcedureFactory.ADDRESS_CITY_RANDOM_TYPO,
          transformer);
      testVariationDifferent("New York City", "PID-11.3", ProcedureFactory.ADDRESS_CITY_RANDOM_TYPO,
          transformer);
      testVariationDifferent("Martha's Vineyard", "PID-11.3",
          ProcedureFactory.ADDRESS_CITY_RANDOM_TYPO, transformer);
    }
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer) {
    assertNotEquals(startValue, new TextRandomTypo(null).varyText(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessageAndDoesNotContain(testStart, startValue, procedure);
  }
}
