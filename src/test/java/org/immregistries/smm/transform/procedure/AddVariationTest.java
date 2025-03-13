package org.immregistries.hart.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import java.util.Arrays;
import java.util.List;
import org.immregistries.hart.transform.procedure.AddVariation.Field;
import org.junit.Test;

public class AddVariationTest extends ProcedureCommonTest {

  @Test
  public void test() {
    List<Triplet> triplets = Arrays.asList(
        new Triplet("PID-5.1", ProcedureFactory.LAST_NAME_ADD_VARIATION, Field.LAST_NAME),
        new Triplet("PID-5.2", ProcedureFactory.FIRST_NAME_ADD_VARIATION, Field.FIRST_NAME),
        new Triplet("PID-5.3", ProcedureFactory.MIDDLE_NAME_ADD_VARIATION, Field.MIDDLE_NAME),
        new Triplet("PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_ADD_VARIATION,
            Field.MOTHERS_MAIDEN_NAME),
        new Triplet("PID-6.2", ProcedureFactory.MOTHERS_FIRST_NAME_ADD_VARIATION,
            Field.MOTHERS_FIRST_NAME),
        new Triplet("PID-11.1", ProcedureFactory.ADDRESS_STREET_ADD_VARIATION,
            Field.ADDRESS_STREET),
        new Triplet("PID-11.3", ProcedureFactory.ADDRESS_CITY_ADD_VARIATION, Field.ADDRESS_CITY));

    for (int i = 0; i < 100; i++) {
      for (Triplet trip : triplets) {
        testVariation("De'Shawn", "DeShawn", trip.location, trip.procedure, trip.field);
        testVariation("De Shawn", "DeShawn", trip.location, trip.procedure, trip.field);
        testVariation("DeShawn", "De'Shawn", "De Shawn", trip.location, trip.procedure, trip.field);
        testVariation("Deshawn", "De'Shawn", "De Shawn", trip.location, trip.procedure, trip.field);
        testVariation("DesHawn", "Des'Hawn", "Des Hawn", trip.location, trip.procedure, trip.field);
        testVariation("DESHAWN", "DE'SHAWN", "DE SHAWN", trip.location, trip.procedure, trip.field);
        testVariation("Anne", "A'Nne", "A Nne", trip.location, trip.procedure, trip.field);
        testVariation("May", "Ma'Y", "Ma Y", trip.location, trip.procedure, trip.field);
        testVariation("Bee", "Bee", trip.location, trip.procedure, trip.field);
        testVariation("Neelima", "Nee'Lima", "Nee Lima", trip.location, trip.procedure, trip.field);
        testVariation("Be e", "BeE", trip.location, trip.procedure, trip.field);
        testVariation("Peter", "Pe'Ter", "Pe Ter", trip.location, trip.procedure, trip.field);
        testVariation("O'Henry", "OHenry", trip.location, trip.procedure, trip.field);
        testVariation("O Henry", "OHenry", trip.location, trip.procedure, trip.field);
        testVariation("OHenry", "O'Henry", "O Henry", trip.location, trip.procedure, trip.field);
        testVariation("Deuteaux", "Deu'Teaux", "Deu Teaux", trip.location, trip.procedure,
            trip.field);
        testVariation("'DeShawn", "'DeShawn", trip.location, trip.procedure, trip.field);
        testVariation("DeShawn'", "DeShawn'", trip.location, trip.procedure, trip.field);
        testVariation("DeShaw'n", "DeShawN", trip.location, trip.procedure, trip.field);
        testVariation("D'eShawn", "DEShawn", trip.location, trip.procedure, trip.field);

        testVariation("New York City", "NewYork City", trip.location, trip.procedure, trip.field);
        testVariation("505 Radio Drive", "505Radio Drive", trip.location, trip.procedure,
            trip.field);
        testVariation("1234 East Circle Dr.", "1234East Circle Dr.", trip.location, trip.procedure,
            trip.field);
        testVariation("Martha's Vineyard", "MarthaS Vineyard", trip.location, trip.procedure,
            trip.field);
        testVariation("Po'ipu", "PoIpu", trip.location, trip.procedure, trip.field);
        testVariation("", "", trip.location, trip.procedure, trip.field);
      }

      testDifferent("bob@comcast.net", "bob'@comcast.net", "PID-13.4",
          ProcedureFactory.EMAIL_ADD_VARIATION, Field.EMAIL);
    }
  }

  private void testVariation(String startValue, String endValueExpected, String location,
      String procedure, Field field) {
    String endValueActual = AddVariation.varyName(startValue, field);
    assertEquals(endValueExpected, endValueActual);
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValueExpected);
    testEquals(testStart, testEnd, procedure);
  }

  private void testDifferent(String startValue, String endValue1, String location, String procedure,
      Field field) {
    String endValue = AddVariation.varyName(startValue, field);
    assertEquals(endValue1, endValue);
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String after = processProcedureChangesMessage(testStart, procedure);
    assertNotEquals(testStart, after);
  }

  private void testVariation(String startValue, String endValue1, String endValue2, String location,
      String procedure, Field field) {
    String endValue = AddVariation.varyName(startValue, field);
    assertTrue(endValue.equals(endValue1) || endValue.equals(endValue2));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd1 = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue1);
    String testEnd2 = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue2);
    testEquals(testStart, testEnd1, testEnd2, procedure);
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
