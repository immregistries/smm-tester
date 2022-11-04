package org.immregistries.smm.transform.procedure;

import org.junit.Test;

public class FirstNameAlternativeEndingsTest extends ProcedureCommonTest {


  @Test
  public void test() {
    testVariation("Sam", "Sann");
    testVariation("Liam", "Liann");
    testVariation("Noah", "Noaph");
    testVariation("Oliver", "Oliverrie");
    testVariation("Elijah", "Elijaph");
    testVariation("James", "Jamesz");
    testVariation("William", "Williann");
    testVariation("Benjamin", "Benjamimm");
    testVariation("Lucas", "Lucasz");
    testVariation("Henry", "Henrie");
    testVariation("Olivia", "Oliviay");
    testVariation("Emma", "Emmay");
    testVariation("Charlotte", "Charlottee");
    testVariation("Amelia", "Ameliay");
    testVariation("Ava", "Avay");
    testVariation("Sophia", "Sophiay");
    testVariation("Isabella", "Isabellay");
    testVariation("Mia", "Miay");
    testVariation("Evelyn", "Evelymm");
    testVariation("Harper", "Harperrie");
  }
  
  protected void testVariation(String startValue, String endValue) {
    assertEquals(endValue, FirstNameAlternativeEndings.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + endValue);
    testEquals(testStart, testEnd, ProcedureFactory.FIRST_NAME_ALTERNATIVE_ENDINGS);
  }

}
