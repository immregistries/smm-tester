package org.immregistries.hart.transform.procedure;

import org.junit.Test;

public class RemoveAiraSuffixTest extends ProcedureCommonTest {
  
  @Test
  public void test() {
    String location = "PID-5.1";
    String procedure = ProcedureFactory.LAST_NAME_REMOVE_AIRA_SUFFIX;
    
    testVariation("ClarkAIRA", "Clark", location, procedure);
    testVariation("WordenAIRA", "Worden", location, procedure);
    
    location = "PID-5.2";
    procedure = ProcedureFactory.FIRST_NAME_REMOVE_AIRA_SUFFIX;
    
    testVariation("GregAIRA", "Greg", location, procedure);
    testVariation("EricaAIRA", "Erica", location, procedure);
    
    location = "PID-6.1";
    procedure = ProcedureFactory.MOTHERS_MAIDEN_NAME_REMOVE_AIRA_SUFFIX;
    
    testVariation("MantzoukasAIRA", "Mantzoukas", location, procedure);
    testVariation("PerrinAIRA", "Perrin", location, procedure);
  }

  private void testVariation(String startValue, String endValue, String location, String procedure) {
    assertEquals(endValue, RemoveAiraSuffix.removeSuffix(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }
}
