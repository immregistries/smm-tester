package org.immregistries.hart.transform.procedure;

import org.junit.Test;

public class MiddleNameInFirstNameVariationTest extends ProcedureCommonTest {


  @Test
  public void test() {
    
    testVariation("Sam", "Liam", "Sam Liam", "");
    testVariation("Sam Liam", "", "Sam", "Liam");
    testVariation("SamLiam", "", "SamLiam", "");
    testVariation("Sam", "", "Sam", "");
    testVariation("", "Liam", "", "Liam");
    testVariation("Mary Ann Sue", "", "Mary", "Ann Sue");
    
  }
  
  private void testVariation(String startFirst, String startMiddle, String endFirst, String endMiddle) {
    String[] result = MiddleNameInFirstNameVariation.varyName(startFirst, startMiddle);
    assertEquals(endFirst, result[0]);
    assertEquals(endMiddle, result[1]);
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + startFirst + "\nPID-5.3=" + startMiddle);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + endFirst + "\nPID-5.3=" + endMiddle);
    testEquals(testStart, testEnd, ProcedureFactory.MIDDLE_NAME_IN_FIRST_NAME_VARIATION);
  }

}
