package org.immregistries.hart.transform.procedure;

import org.junit.Test;

public class DateOfBirthDayShiftTest extends ProcedureCommonTest {


  @Test
  public void test() {
    testDobChange("20220105", "20220104");
    testDobChange("20220201", "20220128");
    testDobChange("20220202", "20220201");
  }

  protected void testDobChange(String startDate, String endDate) {
    assertEquals(endDate, DateOfBirthDayShift.varyDate(startDate));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + startDate);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + endDate);
    testEquals(testStart, testEnd, ProcedureFactory.DATE_OF_BIRTH_DAY_SHIFT);
  }

}
