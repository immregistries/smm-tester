package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class TextTransposeTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer transformer = new Transformer();

    assertEquals("A", TextTranspose.varyText("A", transformer));
    assertEquals("a", TextTranspose.varyText("a", transformer));

    assertEquals("Bo", TextTranspose.varyText("Bo", transformer));
    assertEquals("BO", TextTranspose.varyText("BO", transformer));
    assertEquals("bo", TextTranspose.varyText("bo", transformer));

    assertEquals("Aym", TextTranspose.varyText("Amy", transformer));
    assertEquals("CLA", TextTranspose.varyText("CAL", transformer));
    assertEquals("jmi", TextTranspose.varyText("jim", transformer));

    testVariationDifferent("CARPENTER", "PID-5.1", ProcedureFactory.LAST_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Washington", "PID-5.1", ProcedureFactory.LAST_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Hyphen-nated", "PID-5.1", ProcedureFactory.LAST_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Twolast Names", "PID-5.1", ProcedureFactory.LAST_NAME_TRANSPOSE,
        transformer);

    testVariationDifferent("Samuel", "PID-5.2", ProcedureFactory.FIRST_NAME_TRANSPOSE, transformer);
    testVariationDifferent("Emily", "PID-5.2", ProcedureFactory.FIRST_NAME_TRANSPOSE, transformer);
    testVariationDifferent("Mary Sue", "PID-5.2", ProcedureFactory.FIRST_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Ye Sune", "PID-5.2", ProcedureFactory.FIRST_NAME_TRANSPOSE,
        transformer);

    testVariationDifferent("Jennifer", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Ryan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TRANSPOSE, transformer);
    testVariationDifferent("Frederick Dempsey", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Stan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TRANSPOSE, transformer);

    testVariationDifferent("Grace", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Patrick", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Ziegfried", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Threepwood", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TRANSPOSE,
        transformer);

    testVariationDifferent("Sandra", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Emily", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Mary Sue", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_TRANSPOSE,
        transformer);
    testVariationDifferent("Ye Sune", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_TRANSPOSE,
        transformer);

    testVariationDifferent("4356180", "PID-13.7", ProcedureFactory.PHONE_TRANSPOSE, transformer);
    testVariationDifferent("1234567", "PID-13.7", ProcedureFactory.PHONE_TRANSPOSE, transformer);
    testVariationDifferent("555-5555", "PID-13.7", ProcedureFactory.PHONE_TRANSPOSE, transformer);

    testVariationDifferent("something@gmail.com", "PID-13#2.7", ProcedureFactory.EMAIL_TRANSPOSE,
        transformer);
    testVariationDifferent("plus+sign@apple.net", "PID-13#2.7", ProcedureFactory.EMAIL_TRANSPOSE,
        transformer);

    testVariationDifferent("5678 Wooster Ln", "PID-11.1", ProcedureFactory.ADDRESS_STREET_TRANSPOSE,
        transformer);
    testVariationDifferent("1234 Howard Drive", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_TRANSPOSE, transformer);
    testVariationDifferent("600 Denter St.", "PID-11.1", ProcedureFactory.ADDRESS_STREET_TRANSPOSE,
        transformer);

    testVariationDifferent("Madison", "PID-11.3", ProcedureFactory.ADDRESS_CITY_TRANSPOSE,
        transformer);
    testVariationDifferent("New York City", "PID-11.3", ProcedureFactory.ADDRESS_CITY_TRANSPOSE,
        transformer);
    testVariationDifferent("Martha's Vineyard", "PID-11.3", ProcedureFactory.ADDRESS_CITY_TRANSPOSE,
        transformer);

    // assert only changes required field
    testWrongHookup("Washington", "PID-5.1", ProcedureFactory.LAST_NAME_TRANSPOSE, transformer);
    testWrongHookup("Robert", "PID-5.2", ProcedureFactory.FIRST_NAME_TRANSPOSE, transformer);
    testWrongHookup("Middleditch", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TRANSPOSE, transformer);
    testWrongHookup("Madre", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TRANSPOSE,
        transformer);
    testWrongHookup("Judy", "PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_TRANSPOSE, transformer);
    testWrongHookup("4356180", "PID-13.7", ProcedureFactory.PHONE_TRANSPOSE, transformer);
    testWrongHookup("5678 Wooster Ln", "PID-11.1", ProcedureFactory.ADDRESS_STREET_TRANSPOSE,
        transformer);
    testWrongHookup("New York City", "PID-11.3", ProcedureFactory.ADDRESS_CITY_TRANSPOSE,
        transformer);
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer) {
    assertNotEquals(startValue, TextTranspose.varyText(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessageAndDoesNotContain(testStart, startValue, procedure);
  }

  // change the referenced PID
  // but then test every field but the one passed in to make sure they didn't change
  private void testWrongHookup(String startValue, String location, String procedure,
      Transformer transformer) {
    List<String> procedures = new ArrayList<>(
        Arrays.asList(ProcedureFactory.FIRST_NAME_TRANSPOSE, ProcedureFactory.MIDDLE_NAME_TRANSPOSE,
            ProcedureFactory.LAST_NAME_TRANSPOSE, ProcedureFactory.MOTHERS_MAIDEN_NAME_TRANSPOSE,
            ProcedureFactory.MOTHERS_FIRST_NAME_TRANSPOSE,
            ProcedureFactory.ADDRESS_STREET_TRANSPOSE, ProcedureFactory.ADDRESS_CITY_TRANSPOSE,
            ProcedureFactory.PHONE_TRANSPOSE, ProcedureFactory.EMAIL_TRANSPOSE));

    procedures.remove(procedure);

    for (String proc : procedures) {
      assertNotEquals(startValue, TextTranspose.varyText(startValue, transformer));
      String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
      testProcedureChangesMessageAndDoesContain(testStart, startValue, proc);
    }
  }
}
