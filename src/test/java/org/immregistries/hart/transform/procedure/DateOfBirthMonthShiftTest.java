package org.immregistries.hart.transform.procedure;

import org.junit.Test;

public class DateOfBirthMonthShiftTest extends ProcedureCommonTest {


  @Test
  public void test() {
    testDobChange("20220105", "20211205");
    testDobChange("20220201", "20220101");
    testDobChange("20220531", "20220428");
  }
  
  
  protected void testDobChange(String startDate, String endDate) {
    assertEquals(endDate, DateOfBirthMonthShift.varyDate(startDate));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + startDate);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + endDate);
    testEquals(testStart, testEnd, ProcedureFactory.DATE_OF_BIRTH_MONTH_SHIFT);
  }

}
