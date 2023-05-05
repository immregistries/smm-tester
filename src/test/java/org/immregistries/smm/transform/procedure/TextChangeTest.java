package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class TextChangeTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();

    testVariationDifferent("Robert", "PID-5.2", ProcedureFactory.FIRST_NAME_CHANGE,
        TextChange.Field.FIRST_NAME, transformer);
    testVariationDifferent("Betty", "PID-5.2", ProcedureFactory.FIRST_NAME_CHANGE,
        TextChange.Field.FIRST_NAME, transformer);
    testVariationDifferent("Sandra Dee", "PID-5.2", ProcedureFactory.FIRST_NAME_CHANGE,
        TextChange.Field.FIRST_NAME, transformer);

    testVariationDifferent("Billy", "PID-5.3", ProcedureFactory.MIDDLE_NAME_CHANGE,
        TextChange.Field.MIDDLE_NAME, transformer);
    testVariationDifferent("Susan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_CHANGE,
        TextChange.Field.MIDDLE_NAME, transformer);
    testVariationDifferent("Hyphen-nated", "PID-5.3", ProcedureFactory.MIDDLE_NAME_CHANGE,
        TextChange.Field.MIDDLE_NAME, transformer);

    testVariationDifferent("Washington", "PID-5.1", ProcedureFactory.LAST_NAME_CHANGE,
        TextChange.Field.LAST_NAME, transformer);
    testVariationDifferent("Black", "PID-5.1", ProcedureFactory.LAST_NAME_CHANGE,
        TextChange.Field.LAST_NAME, transformer);
    testVariationDifferent("Hyphen-nated", "PID-5.1", ProcedureFactory.LAST_NAME_CHANGE,
        TextChange.Field.LAST_NAME, transformer);

    testVariationDifferent("Keller", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_CHANGE,
        TextChange.Field.MOTHERS_MAIDEN_NAME, transformer);
    testVariationDifferent("Scheer", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_CHANGE,
        TextChange.Field.MOTHERS_MAIDEN_NAME, transformer);
    testVariationDifferent("Raphael", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_CHANGE,
        TextChange.Field.MOTHERS_MAIDEN_NAME, transformer);

    testVariationDifferent("Zabelle", "PID-6.2", ProcedureFactory.MOTHERS_MAIDEN_FIRST_NAME_CHANGE,
        TextChange.Field.MOTHERS_MAIDEN_FIRST_NAME, transformer);
    testVariationDifferent("Lucille", "PID-6.2", ProcedureFactory.MOTHERS_MAIDEN_FIRST_NAME_CHANGE,
        TextChange.Field.MOTHERS_MAIDEN_FIRST_NAME, transformer);
    testVariationDifferent("Martha", "PID-6.2", ProcedureFactory.MOTHERS_MAIDEN_FIRST_NAME_CHANGE,
        TextChange.Field.MOTHERS_MAIDEN_FIRST_NAME, transformer);

    testVariationDifferent("New York City", "PID-11.3", ProcedureFactory.ADDRESS_CITY_CHANGE,
        TextChange.Field.ADDRESS_CITY, transformer);
    testVariationDifferent("Madison", "PID-11.3", ProcedureFactory.ADDRESS_CITY_CHANGE,
        TextChange.Field.ADDRESS_CITY, transformer);
    testVariationDifferent("Seattle", "PID-11.3", ProcedureFactory.ADDRESS_CITY_CHANGE,
        TextChange.Field.ADDRESS_CITY, transformer);

    testVariationDifferent("4356180", "PID-13.7", ProcedureFactory.PHONE_CHANGE,
        TextChange.Field.PHONE, transformer);
    testVariationDifferent("something@gmail.com", "PID-13#2.7", ProcedureFactory.EMAIL_CHANGE,
        TextChange.Field.EMAIL, transformer);

  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      TextChange.Field field, Transformer transformer) {
    assertNotEquals(startValue, TextChange.varyText(startValue, field, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessageAndDoesNotContain(testStart, startValue, procedure);
  }

}
