package org.immregistries.smm.transform.procedure;

import org.junit.Test;
import junit.framework.TestCase;

public class DateOfBirthMonthShiftTest extends TestCase {


  @Test
  public void test() {

    assertEquals("20211205", DateOfBirthMonthShift.varyDate("20220105"));
    assertEquals("20220101", DateOfBirthMonthShift.varyDate("20220201"));
    assertEquals("20220428", DateOfBirthMonthShift.varyDate("20220531"));

  }

}
