package org.immregistries.smm.transform.procedure;

import org.junit.Test;

public class DateOfBirthRectifyTest extends ProcedureCommonTest {


  @Test
  public void test() {
    testDobChange("20220105", "20220105");
    testDobChange("20220201", "20220128");
    testDobChange("20220202", "20220127");
    testDobChange("20220203", "20220203");
    testDobChange("20220228", "20220228");
    testDobChange("20220128", "20220128");
    testDobChange("20220129", "20220126");
    testDobChange("20220130", "20220127");
    testDobChange("20220131", "20220128");
  }

  protected void testDobChange(String startDate, String endDate) {
    assertEquals(endDate, DateOfBirthRectify.varyDate(startDate));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + startDate);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + endDate);
    testEquals(testStart, testEnd, ProcedureFactory.DATE_OF_BIRTH_RECTIFY);
  }

}
