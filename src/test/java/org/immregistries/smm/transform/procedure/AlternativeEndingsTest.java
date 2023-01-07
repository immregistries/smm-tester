package org.immregistries.smm.transform.procedure;

import org.junit.Test;

public class AlternativeEndingsTest extends ProcedureCommonTest {


  @Test
  public void test() {
    String location = "PID-5.1";
    String procedure = ProcedureFactory.LAST_NAME_ALTERNATIVE_ENDINGS;
    
    testVariation("Brick", "Brikee", location, procedure);
    testVariation("Benedict", "Beneditus", location, procedure);
    testVariation("Swift", "Swiftee", location, procedure);
    testVariation("Fairchild", "Fairchilder", location, procedure);
    testVariation("Gandalf", "Gandalfie", location, procedure);
    testVariation("Phelp", "Phelpy", location, procedure);
    
    location = "PID-5.2";
    procedure = ProcedureFactory.FIRST_NAME_ALTERNATIVE_ENDINGS;
  
    testVariation("Sam", "Sann", location, procedure);
    testVariation("Liam", "Liann", location, procedure);
    testVariation("Noah", "Noaph", location, procedure);
    testVariation("Oliver", "Oliverrie", location, procedure);
    testVariation("Elijah", "Elijaph", location, procedure);
    testVariation("James", "Jamesz", location, procedure);
    testVariation("William", "Williann", location, procedure);
    testVariation("Benjamin", "Benjamimm", location, procedure);
    testVariation("Lucas", "Lucasz", location, procedure);
    testVariation("Henry", "Henrie", location, procedure);
    testVariation("Olivia", "Oliviay", location, procedure);
    testVariation("Emma", "Emmay", location, procedure);
    testVariation("Charlotte", "Charlottee", location, procedure);
    testVariation("Amelia", "Ameliay", location, procedure);
    testVariation("Ava", "Avay", location, procedure);
    testVariation("Sophia", "Sophiay", location, procedure);
    testVariation("Isabella", "Isabellay", location, procedure);
    testVariation("Mia", "Miay", location, procedure);
    testVariation("Evelyn", "Evelymm", location, procedure);
    testVariation("Harper", "Harperrie", location, procedure);
    
    location = "PID-5.3";
    procedure = ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_ENDINGS;
    
    testVariation("Kemp", "Kempie", location, procedure);
    testVariation("Armond", "Armody", location, procedure);
    testVariation("Hank", "Haky", location, procedure);
    testVariation("Belmont", "Belmotie", location, procedure);
    testVariation("Egypt", "Egyptie", location, procedure);
    testVariation("Claiborn", "Claiborny", location, procedure);
    testVariation("Edelgard", "Edelgardy", location, procedure);
    
    location = "PID-6.1";
    procedure = ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_ENDINGS;
    
    testVariation("York", "Yorkie", location, procedure);
    testVariation("Fisk", "Fisky", location, procedure);
    testVariation("Chasm", "Chasmon", location, procedure);
    testVariation("Whisp", "Whisper", location, procedure);
    testVariation("Forrest", "Forreon", location, procedure);
  }
  
  private void testVariation(String startValue, String endValue, String location, String procedure) {
    assertEquals(endValue, AlternativeEndings.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }

}
