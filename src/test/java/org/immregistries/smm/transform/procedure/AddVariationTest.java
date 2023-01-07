package org.immregistries.smm.transform.procedure;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class AddVariationTest extends ProcedureCommonTest {


  @Test
  public void test() {
    List<List<String>> locationProcedurePairs = 
        Arrays.asList(
          Arrays.asList("PID-5.1", ProcedureFactory.LAST_NAME_ADD_VARIATION),
          Arrays.asList("PID-5.2", ProcedureFactory.FIRST_NAME_ADD_VARIATION),
          Arrays.asList("PID-5.3", ProcedureFactory.MIDDLE_NAME_ADD_VARIATION),
          Arrays.asList("PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_ADD_VARIATION)
        );
    
    for(List<String> pair : locationProcedurePairs) {
      String location = pair.get(0);
      String procedure = pair.get(1);
      
      testVariation("De'Shawn", "DeShawn", location, procedure);
      testVariation("De Shawn", "DeShawn", location, procedure);
      testVariation("DeShawn", "De'Shawn", "De Shawn", location, procedure);
      testVariation("Deshawn", "De'Shawn", "De Shawn", location, procedure);
      testVariation("DesHawn", "Des'Hawn", "Des Hawn", location, procedure);
      testVariation("DESHAWN", "DE'SHAWN", "DE SHAWN", location, procedure);
      testVariation("Anne", "A'Nne", "A Nne", location, procedure);
      testVariation("May", "Ma'Y", "Ma Y", location, procedure);
      testVariation("Bee", "Bee", location, procedure);
      testVariation("Neelima", "Nee'Lima", "Nee Lima", location, procedure);
      testVariation("Be e", "BeE", location, procedure);
      testVariation("Peter", "Pe'Ter", "Pe Ter", location, procedure);
      testVariation("O'Henry", "OHenry", location, procedure);
      testVariation("O Henry", "OHenry", location, procedure);
      testVariation("OHenry", "O'Henry", "O Henry", location, procedure);
      testVariation("Deuteaux", "Deu'Teaux", "Deu Teaux", location, procedure);
      testVariation("'DeShawn", "'DeShawn", location, procedure);
      testVariation("DeShawn'", "DeShawn'", location, procedure);
      testVariation("DeShaw'n", "DeShawN", location, procedure);
      testVariation("D'eShawn", "DEShawn", location, procedure);
    }
  }

  private void testVariation(String startValue, String endValueExpected, String location, String procedure) {
    String endValueActual = AddVariation.varyName(startValue);
    assertEquals(endValueExpected, endValueActual);
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValueExpected);
    testEquals(testStart, testEnd, procedure);
  }

  private void testVariation(String startValue, String endValue1, String endValue2, String location, String procedure) {
    String endValue = AddVariation.varyName(startValue);
    assertTrue(endValue.equals(endValue1) || endValue.equals(endValue2));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd1 = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue1);
    String testEnd2 = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue2);
    testEquals(testStart, testEnd1, testEnd2, procedure);
  }

}