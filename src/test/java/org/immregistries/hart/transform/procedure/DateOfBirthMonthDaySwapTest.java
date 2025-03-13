package org.immregistries.hart.transform.procedure;

import org.junit.Test;

public class DateOfBirthMonthDaySwapTest extends ProcedureCommonTest {


  @Test
  public void test() {

    testDobChange("20220105", "20220501");
    testDobChange("20220215", "20220103");
    testDobChange("20220230", "20211206");
    testDobChange("2022023", "2022023");
    testDobChange("BAD", "BAD");
    testDobChange("20220105104530", "20220501");
    
  }
  
  protected void testDobChange(String startDate, String endDate) {
    assertEquals(endDate, DateOfBirthMonthDaySwap.varyDate(startDate));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + startDate);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + endDate);
    testEquals(testStart, testEnd, ProcedureFactory.DATE_OF_BIRTH_MONTH_DAY_SWAP);
  }


}
