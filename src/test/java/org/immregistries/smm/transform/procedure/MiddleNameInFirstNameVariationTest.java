package org.immregistries.smm.transform.procedure;

import org.junit.Test;
import junit.framework.TestCase;

public class MiddleNameInFirstNameVariationTest extends TestCase {


  @Test
  public void test() {
    
    assertEquals("Sam Liam", MiddleNameInFirstNameVariation.varyName("Sam", "Liam")[0]);
    assertEquals("Sam", MiddleNameInFirstNameVariation.varyName("Sam Liam", "")[0]);
    assertEquals("SamLiam", MiddleNameInFirstNameVariation.varyName("SamLiam", "")[0]);
    assertEquals("Sam", MiddleNameInFirstNameVariation.varyName("Sam", "")[0]);
    assertEquals("", MiddleNameInFirstNameVariation.varyName("", "Liam")[0]);
    assertEquals("Mary", MiddleNameInFirstNameVariation.varyName("Mary Ann Sue", "")[0]);
    
    System.out.println();
    
    assertEquals("", MiddleNameInFirstNameVariation.varyName("Sam", "Liam")[1]);
    assertEquals("Liam", MiddleNameInFirstNameVariation.varyName("Sam Liam", "")[1]);
    assertEquals("", MiddleNameInFirstNameVariation.varyName("Sam", "")[1]);
    assertEquals("Liam", MiddleNameInFirstNameVariation.varyName("", "Liam")[1]);

  }

}
