
package org.immregistries.hart.transform.procedure;

import org.immregistries.hart.util.DateOfBirth;
import org.junit.Test;

public class DateOfBirthTest extends ProcedureCommonTest {

  @Test
  public void test() {
    testDayShift("20220105", "20220104");
    testDayShift("20220201", "20220128");
    testDayShift("20220202", "20220201");
    testMonthSwap("20220105", "20220501");
    testMonthSwap("20220215", "20220103");
    testMonthSwap("20220230", "20211206");
    testMonthSwap("2022023", "2022023");
    testMonthSwap("BAD", "BAD");
    testMonthSwap("20220105104530", "20220501");
    testMonthShift("20220105", "20211205");
    testMonthShift("20220201", "20220101");
    testMonthShift("20220531", "20220428");
    testYearShift("20220105", "20210105");
    testYearShift("20000229", "19990228");
    testRectify("20220105", "20220105");
    testRectify("20220201", "20220128");
    testRectify("20220202", "20220127");
    testRectify("20220203", "20220203");
    testRectify("20220228", "20220228");
    testRectify("20220128", "20220128");
    testRectify("20220129", "20220126");
    testRectify("20220130", "20220127");
    testRectify("20220131", "20220128");
  }

  protected void testDayShift(String startDate, String endDate) {
    DateOfBirth dob = new DateOfBirth(startDate);
    assertEquals(endDate, dob.decrementDay());
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + startDate);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + endDate);
    testEquals(testStart, testEnd, ProcedureFactory.DATE_OF_BIRTH_DAY_SHIFT);
  }

  protected void testMonthSwap(String startDate, String endDate) {
    DateOfBirth dob = new DateOfBirth(startDate);
    assertEquals(endDate, dob.swapDayAndMonth());
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + startDate);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + endDate);
    testEquals(testStart, testEnd, ProcedureFactory.DATE_OF_BIRTH_MONTH_DAY_SWAP);
  }

  protected void testMonthShift(String startDate, String endDate) {
    DateOfBirth dob = new DateOfBirth(startDate);
    assertEquals(endDate, dob.decrementMonth());
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + startDate);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + endDate);
    testEquals(testStart, testEnd, ProcedureFactory.DATE_OF_BIRTH_MONTH_SHIFT);
  }

  protected void testRectify(String startDate, String endDate) {
    assertEquals(endDate, DateOfBirth.rectify(startDate));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + startDate);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + endDate);
    testEquals(testStart, testEnd, ProcedureFactory.DATE_OF_BIRTH_RECTIFY);
  }

  protected void testYearShift(String startDate, String endDate) {
    DateOfBirth dob = new DateOfBirth(startDate);
    assertEquals(endDate, dob.decrementYear());
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + startDate);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-7=" + endDate);
    testEquals(testStart, testEnd, ProcedureFactory.DATE_OF_BIRTH_YEAR_SHIFT);
  }

}
