package org.immregistries.smm.transform.procedure;

import org.junit.Test;

public class AdministrativeSexVariationTest extends ProcedureCommonTest {


  @Test
  public void test() {
    assertEquals("F", AdministrativeSexVariation.varyCode("M"));
    assertEquals("M", AdministrativeSexVariation.varyCode("F"));
    assertEquals("F", AdministrativeSexVariation.varyCode("U"));
    assertEquals("U", AdministrativeSexVariation.varyCode(""));
    assertEquals("U", AdministrativeSexVariation.varyCode("X"));
    
    String hl7Female = transform(DEFAULT_TEST_MESSAGE, "PID-8=F");
    String hl7Male = transform(DEFAULT_TEST_MESSAGE, "PID-8=M");
    String hl7Unknown = transform(DEFAULT_TEST_MESSAGE, "PID-8=U");
    String hl7Blank = transform(DEFAULT_TEST_MESSAGE, "PID-8=");
    
    testEquals(hl7Female, hl7Male, ProcedureFactory.ADMINISTRATIVE_SEX_VARIATION);
    testEquals(hl7Male, hl7Female, ProcedureFactory.ADMINISTRATIVE_SEX_VARIATION);
    testEquals(hl7Unknown, hl7Female, ProcedureFactory.ADMINISTRATIVE_SEX_VARIATION);
    testEquals(hl7Blank, hl7Unknown, ProcedureFactory.ADMINISTRATIVE_SEX_VARIATION);
    
  }

}
