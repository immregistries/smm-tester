package org.immregistries.smm.transform.procedure;

import org.junit.Test;
import junit.framework.TestCase;

public class DateOfBirthMonthDaySwapTest extends TestCase {


  @Test
  public void test() {

    assertEquals("20220501", DateOfBirthMonthDaySwap.varyDate("20220105"));
    assertEquals("20220103", DateOfBirthMonthDaySwap.varyDate("20220215"));    
    assertEquals("20211206", DateOfBirthMonthDaySwap.varyDate("20220230"));
    assertEquals("2022023", DateOfBirthMonthDaySwap.varyDate("2022023"));
    assertEquals("BAD", DateOfBirthMonthDaySwap.varyDate("BAD"));
    assertEquals("20220501", DateOfBirthMonthDaySwap.varyDate("20220105104530"));

  }

}
