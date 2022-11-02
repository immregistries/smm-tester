package org.immregistries.smm.transform.procedure;

import org.junit.Test;
import junit.framework.TestCase;

public class FirstNameAddVariationTest extends TestCase {


  @Test
  public void test() {
    assertEquals("De Shawn", FirstNameAddVariation.varyName("De'Shawn"));
    assertEquals("DeShawn", FirstNameAddVariation.varyName("De Shawn"));
    assertEquals("De'Shawn", FirstNameAddVariation.varyName("DeShawn"));
    assertEquals("De'Shawn", FirstNameAddVariation.varyName("Deshawn"));
    assertEquals("Des'Hawn", FirstNameAddVariation.varyName("DesHawn"));
    assertEquals("DE'SHAWN", FirstNameAddVariation.varyName("DESHAWN"));
    assertEquals("A'Nne", FirstNameAddVariation.varyName("Anne"));
    assertEquals("Ma'Y", FirstNameAddVariation.varyName("May"));
    assertEquals("Bee", FirstNameAddVariation.varyName("Bee"));
    assertEquals("Nee'Lima", FirstNameAddVariation.varyName("Neelima"));
    assertEquals("BeE", FirstNameAddVariation.varyName("Be e"));
    assertEquals("Pe'Ter", FirstNameAddVariation.varyName("Peter"));
    assertEquals("O Henry", FirstNameAddVariation.varyName("O'Henry"));
    assertEquals("OHenry", FirstNameAddVariation.varyName("O Henry"));
    assertEquals("O'Henry", FirstNameAddVariation.varyName("OHenry"));
    assertEquals("Deu'Teaux", FirstNameAddVariation.varyName("Deuteaux"));

    assertEquals("DeShawn", FirstNameAddVariation
        .varyName(FirstNameAddVariation.varyName(FirstNameAddVariation.varyName("DeShawn"))));
    assertEquals("'DeShawn", FirstNameAddVariation.varyName("'DeShawn"));
    assertEquals("DeShawn'", FirstNameAddVariation.varyName("DeShawn'"));
    assertEquals("DeShaw N", FirstNameAddVariation.varyName("DeShaw'n"));
    assertEquals("D EShawn", FirstNameAddVariation.varyName("D'eShawn"));


  }

}
