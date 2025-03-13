package org.immregistries.hart.transform.procedure;

import org.junit.Test;

public class DateOfBirthYearShiftTest extends ProcedureCommonTest {


  @Test
  public void test() {
    testDobChange("20220105", "20210105");
    testDobChange("20000229", "19990228");
  }

  protected void testDobChange(String startDate, String endDate) {
    assertEquals(endDate, DateOfBirthYearShift.varyDate(startDate));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + startDate);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + endDate);
    testEquals(testStart, testEnd, ProcedureFactory.DATE_OF_BIRTH_YEAR_SHIFT);
  }

}
