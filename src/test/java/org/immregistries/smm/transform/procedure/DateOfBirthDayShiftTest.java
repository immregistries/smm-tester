package org.immregistries.smm.transform.procedure;

import org.junit.Test;
import junit.framework.TestCase;

public class DateOfBirthDayShiftTest extends TestCase {


  @Test
  public void test() {

    assertEquals("20220104", DateOfBirthDayShift.varyDate("20220105"));
    assertEquals("20220128", DateOfBirthDayShift.varyDate("20220201"));
    assertEquals("20220201", DateOfBirthDayShift.varyDate("20220202"));

  }

}
