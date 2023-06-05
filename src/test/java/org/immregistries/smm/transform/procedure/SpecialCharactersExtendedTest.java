package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class SpecialCharactersExtendedTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer transformer = new Transformer();

    testVariationDifferent("CARPENTER", "PID-5.1",
        ProcedureFactory.LAST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Washington", "PID-5.1",
        ProcedureFactory.LAST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Hyphen-nated", "PID-5.1",
        ProcedureFactory.LAST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Twolast Names", "PID-5.1",
        ProcedureFactory.LAST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);

    SpecialCharactersExtended sc = new SpecialCharactersExtended(null);
    sc.setTransformer(transformer);
    assertEquals("Off", sc.varyText("Off"));
    assertEquals("Em", sc.varyText("Em"));

    testVariationDifferent("Samuel", "PID-5.2",
        ProcedureFactory.FIRST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Emily", "PID-5.2",
        ProcedureFactory.FIRST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Mary Sue", "PID-5.2",
        ProcedureFactory.FIRST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Ye Sune", "PID-5.2",
        ProcedureFactory.FIRST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);

    testVariationDifferent("Jennifer", "PID-5.3",
        ProcedureFactory.MIDDLE_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Ryan", "PID-5.3",
        ProcedureFactory.MIDDLE_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Frederick Dempsey", "PID-5.3",
        ProcedureFactory.MIDDLE_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Stan", "PID-5.3",
        ProcedureFactory.MIDDLE_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);

    testVariationDifferent("Grace", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Patrick", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Ziegfried", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Threepwood", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);

    testVariationDifferent("Sandra", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Emily", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Mary Sue", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Ye Sune", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_SPECIAL_CHARACTERS_EXTENDED, transformer);

    testVariationDifferent("something@gmail.com", "PID-13.4",
        ProcedureFactory.EMAIL_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("plus+sign@apple.net", "PID-13.4",
        ProcedureFactory.EMAIL_SPECIAL_CHARACTERS_EXTENDED, transformer);

    testVariationDifferent("5678 Wooster Ln", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("1234 Howard Drive", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("600 Denter St.", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_SPECIAL_CHARACTERS_EXTENDED, transformer);

    testVariationDifferent("Madison", "PID-11.3",
        ProcedureFactory.ADDRESS_CITY_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("New York City", "PID-11.3",
        ProcedureFactory.ADDRESS_CITY_SPECIAL_CHARACTERS_EXTENDED, transformer);
    testVariationDifferent("Martha's Vineyard", "PID-11.3",
        ProcedureFactory.ADDRESS_CITY_SPECIAL_CHARACTERS_EXTENDED, transformer);
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer) {
    SpecialCharactersExtended sc = new SpecialCharactersExtended(null);
    sc.setTransformer(transformer);

    assertNotEquals(startValue, sc.varyText(startValue));

    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessageAndDoesNotContain(testStart, startValue, procedure);
  }
}
