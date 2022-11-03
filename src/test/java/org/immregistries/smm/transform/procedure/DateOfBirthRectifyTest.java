package org.immregistries.smm.transform.procedure;

import org.junit.Test;
import junit.framework.TestCase;

public class DateOfBirthRectifyTest extends TestCase {


  @Test
  public void test() {


    assertEquals("20220105", DateOfBirthRectify.varyDate("20220105"));
    assertEquals("20220128", DateOfBirthRectify.varyDate("20220201"));
    assertEquals("20220127", DateOfBirthRectify.varyDate("20220202"));
    assertEquals("20220203", DateOfBirthRectify.varyDate("20220203"));
    assertEquals("20220228", DateOfBirthRectify.varyDate("20220228"));
    assertEquals("20220128", DateOfBirthRectify.varyDate("20220128"));
    assertEquals("20220126", DateOfBirthRectify.varyDate("20220129"));
    assertEquals("20220127", DateOfBirthRectify.varyDate("20220130"));
    assertEquals("20220128", DateOfBirthRectify.varyDate("20220131"));

  }

}
