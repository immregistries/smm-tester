package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.immregistries.smm.transform.procedure.AlternativeEndingVowels.Field;
import org.junit.Test;

public class AlternativeEndingVowelsTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer t = new Transformer();

    Field field = AlternativeEndingVowels.Field.FIRST_NAME;
    testVariationDifferent("Amy", "PID-5.2", ProcedureFactory.FIRST_NAME_ALTERNATIVE_ENDING_VOWELS,
        t, field);
    testVariationDifferent("Poe", "PID-5.2", ProcedureFactory.FIRST_NAME_ALTERNATIVE_ENDING_VOWELS,
        t, field);
    testVariationDifferent("Beau", "PID-5.2", ProcedureFactory.FIRST_NAME_ALTERNATIVE_ENDING_VOWELS,
        t, field);
    testVariationDifferent("Bob", "PID-5.2", ProcedureFactory.FIRST_NAME_ALTERNATIVE_ENDING_VOWELS,
        t, field);
    testVariationDifferent("Richard", "PID-5.2",
        ProcedureFactory.FIRST_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("", "PID-5.2", ProcedureFactory.FIRST_NAME_ALTERNATIVE_ENDING_VOWELS, t,
        field);

    field = AlternativeEndingVowels.Field.MIDDLE_NAME;
    testVariationDifferent("Cathy", "PID-5.3",
        ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Bill", "PID-5.3",
        ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Suzy", "PID-5.3",
        ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Joe", "PID-5.3", ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_ENDING_VOWELS,
        t, field);
    testVariationDifferent("Drew", "PID-5.3",
        ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("", "PID-5.3", ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_ENDING_VOWELS, t,
        field);

    field = AlternativeEndingVowels.Field.LAST_NAME;
    testVariationDifferent("Washington", "PID-5.1",
        ProcedureFactory.LAST_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Terpi", "PID-5.1", ProcedureFactory.LAST_NAME_ALTERNATIVE_ENDING_VOWELS,
        t, field);
    testVariationDifferent("Fow", "PID-5.1", ProcedureFactory.LAST_NAME_ALTERNATIVE_ENDING_VOWELS,
        t, field);
    testVariationDifferent("Chiew", "PID-5.1", ProcedureFactory.LAST_NAME_ALTERNATIVE_ENDING_VOWELS,
        t, field);
    testVariationDifferent("Zruh", "PID-5.1", ProcedureFactory.LAST_NAME_ALTERNATIVE_ENDING_VOWELS,
        t, field);
    testVariationDifferent("", "PID-5.1", ProcedureFactory.LAST_NAME_ALTERNATIVE_ENDING_VOWELS, t,
        field);

    field = AlternativeEndingVowels.Field.MOTHERS_FIRST_NAME;
    testVariationDifferent("Jess", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Sosoh", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Yoyieu", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Diedrah", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);

    field = AlternativeEndingVowels.Field.MOTHERS_MAIDEN_NAME;
    testVariationDifferent("Rogers", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("They", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Goe", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Shou", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Stella", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_ENDING_VOWELS, t, field);

    field = AlternativeEndingVowels.Field.ADDRESS_STREET;
    testVariationDifferent("100 MLK Dr.", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("1010 Bry St.", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("1600 Soho Ave.", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("N5144 Neau Parkway", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("1234 Teluh Blvd.", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_ENDING_VOWELS, t, field);

    field = AlternativeEndingVowels.Field.ADDRESS_CITY;
    testVariationDifferent("Madison", "PID-11.3",
        ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Derrie", "PID-11.3",
        ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Dustow", "PID-11.3",
        ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Bellvue", "PID-11.3",
        ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("Tacomah", "PID-11.3",
        ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("", "PID-11.3", ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_ENDING_VOWELS,
        t, field);

    field = AlternativeEndingVowels.Field.EMAIL;
    testVariationDifferent("bob@gmail.com", "PID-13.4",
        ProcedureFactory.EMAIL_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("evans.julie@gmail.com", "PID-13.4",
        ProcedureFactory.EMAIL_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("stoh@aol.com", "PID-13.4",
        ProcedureFactory.EMAIL_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("kidsoffue@msn.net", "PID-13.4",
        ProcedureFactory.EMAIL_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("alameda@city.net", "PID-13.4",
        ProcedureFactory.EMAIL_ALTERNATIVE_ENDING_VOWELS, t, field);
    testVariationDifferent("", "PID-13.4", ProcedureFactory.EMAIL_ALTERNATIVE_ENDING_VOWELS, t,
        field);
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer, AlternativeEndingVowels.Field field) {
    assertNotEquals(startValue, AlternativeEndingVowels.varyText(startValue, transformer, field));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessage(testStart, procedure);
  }
}
