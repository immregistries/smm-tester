package org.immregistries.smm.transform.procedure;

import org.junit.Test;
import junit.framework.TestCase;

public class AdministrativeSexVariationTest extends TestCase {


  @Test
  public void test() {
    assertEquals("F", AdministrativeSexVariation.varyCode("M"));
    assertEquals("M", AdministrativeSexVariation.varyCode("F"));
    assertEquals("F", AdministrativeSexVariation.varyCode("U"));
    assertEquals("U", AdministrativeSexVariation.varyCode(""));
    assertEquals("U", AdministrativeSexVariation.varyCode("X"));
  }

}
