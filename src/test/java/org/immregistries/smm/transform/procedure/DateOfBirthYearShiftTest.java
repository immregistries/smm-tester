package org.immregistries.smm.transform.procedure;

import org.junit.Test;
import junit.framework.TestCase;

public class DateOfBirthYearShiftTest extends TestCase {


  @Test
  public void test() {

    assertEquals("20210105", DateOfBirthYearShift.varyDate("20220105"));
    assertEquals("19990228", DateOfBirthYearShift.varyDate("20000229"));

  }

}
