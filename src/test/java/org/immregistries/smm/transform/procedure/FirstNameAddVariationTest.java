package org.immregistries.smm.transform.procedure;

import org.junit.Test;

public class FirstNameAddVariationTest extends ProcedureCommonTest {


  @Test
  public void test() {
    testVariation("De'Shawn", "De Shawn");
    testVariation("De Shawn", "DeShawn");
    testVariation("DeShawn", "De'Shawn");
    testVariation("Deshawn", "De'Shawn");
    testVariation("DesHawn", "Des'Hawn");
    testVariation("DESHAWN", "DE'SHAWN");
    testVariation("Anne", "A'Nne");
    testVariation("May", "Ma'Y");
    testVariation("Bee", "Bee");
    testVariation("Neelima", "Nee'Lima");
    testVariation("Be e", "BeE");
    testVariation("Peter", "Pe'Ter");
    testVariation("O'Henry", "O Henry");
    testVariation("O Henry", "OHenry");
    testVariation("OHenry", "O'Henry");
    testVariation("Deuteaux", "Deu'Teaux");
    testVariation("'DeShawn", "'DeShawn");
    testVariation("DeShawn'", "DeShawn'");
    testVariation("DeShaw'n", "DeShaw N");
    testVariation("D'eShawn", "D EShawn");
  }

  private void testVariation(String startValue, String endValue) {
    assertEquals(endValue, FirstNameAddVariation.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + endValue);
    testEquals(testStart, testEnd, ProcedureFactory.FIRST_NAME_ADD_VARIATION);
  }

}
