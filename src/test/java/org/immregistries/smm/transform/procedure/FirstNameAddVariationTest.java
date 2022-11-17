package org.immregistries.smm.transform.procedure;

import org.junit.Test;

public class FirstNameAddVariationTest extends ProcedureCommonTest {


  @Test
  public void test() {
    testVariation("De'Shawn", "DeShawn");
    testVariation("De Shawn", "DeShawn");
    testVariation("DeShawn", "De'Shawn", "De Shawn");
    testVariation("Deshawn", "De'Shawn", "De Shawn");
    testVariation("DesHawn", "Des'Hawn", "Des Hawn");
    testVariation("DESHAWN", "DE'SHAWN", "DE SHAWN");
    testVariation("Anne", "A'Nne", "A Nne");
    testVariation("May", "Ma'Y", "Ma Y");
    testVariation("Bee", "Bee");
    testVariation("Neelima", "Nee'Lima", "Nee Lima");
    testVariation("Be e", "BeE");
    testVariation("Peter", "Pe'Ter", "Pe Ter");
    testVariation("O'Henry", "OHenry");
    testVariation("O Henry", "OHenry");
    testVariation("OHenry", "O'Henry", "O Henry");
    testVariation("Deuteaux", "Deu'Teaux", "Deu Teaux");
    testVariation("'DeShawn", "'DeShawn");
    testVariation("DeShawn'", "DeShawn'");
    testVariation("DeShaw'n", "DeShawN");
    testVariation("D'eShawn", "DEShawn");
  }

  private void testVariation(String startValue, String endValue) {
    assertEquals(endValue, FirstNameAddVariation.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + endValue);
    testEquals(testStart, testEnd, ProcedureFactory.FIRST_NAME_ADD_VARIATION);
  }
  
  private void testVariation(String startValue, String endValue1, String endValue2) {
    String endValue = FirstNameAddVariation.varyName(startValue);
    assertTrue(endValue.equals(endValue1) || endValue.equals(endValue2));
    assertEquals(endValue, FirstNameAddVariation.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + startValue);
    String testEnd1 = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + endValue1);
    String testEnd2 = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + endValue2);
    testEquals(testStart, testEnd1, testEnd2, ProcedureFactory.FIRST_NAME_ADD_VARIATION);
  }

}
