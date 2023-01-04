package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class RepeatedConsonantsTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    String location = "PID-5.1";
    String procedure = ProcedureFactory.LAST_NAME_REPEATED_CONSONANTS;
    
    testVariationDifferent("Sanchez", location, procedure, transformer);
    testVariationDifferent("Yates", location, procedure, transformer);
    testVariationDifferent("Wiggins", location, procedure, transformer);
    testVariationDifferent("Vasquez", location, procedure, transformer);
    testVariationDifferent("Lambert", location, procedure, transformer);
    testVariationDifferent("Daugherty", location, procedure, transformer);
    
    location = "PID-5.2";
    procedure = ProcedureFactory.FIRST_NAME_REPEATED_CONSONANTS;
    
    testVariationDifferent("Sammie", location, procedure, transformer);
    testVariationDifferent("Sam", location, procedure, transformer);
    testVariationDifferent("Kalyn", location, procedure, transformer);
    testVariationDifferent("Howard", location, procedure, transformer);
    testVariationDifferent("Heather", location, procedure, transformer);
    testVariationDifferent("Ivan", location, procedure, transformer);
    
    location = "PID-5.3";
    procedure = ProcedureFactory.MIDDLE_NAME_REPEATED_CONSONANTS;
    
    testVariationDifferent("Brady", location, procedure, transformer);
    testVariationDifferent("Blankenship", location, procedure, transformer);
    testVariationDifferent("Mccarty", location, procedure, transformer);
    testVariationDifferent("Barrett", location, procedure, transformer);
    testVariationDifferent("Matthews", location, procedure, transformer);
    testVariationDifferent("Wyatt", location, procedure, transformer);
    
    location = "PID-6.1";
    procedure = ProcedureFactory.MOTHERS_MAIDEN_NAME_REPEATED_CONSONANTS;
    
    testVariationDifferent("Simmons", location, procedure, transformer);
    testVariationDifferent("Rasmussen", location, procedure, transformer);
    testVariationDifferent("Blanchard", location, procedure, transformer);
    testVariationDifferent("Chandler", location, procedure, transformer);
    testVariationDifferent("Carpenter", location, procedure, transformer);
    testVariationDifferent("Pitts", location, procedure, transformer);
  }
  
  private void testVariationDifferent(String startValue, String location, String procedure, Transformer transformer) {
    assertNotEquals(startValue, RepeatedConsonants.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessage(testStart, procedure);
  }

}
