package org.immregistries.smm.transform.procedure;

import org.junit.Test;

public class FirstNameAlternativeBeginningsTest extends ProcedureCommonTest {


  @Test
  public void test() {
    testVariation("Sam", "Tsam");
    testVariation("Liam", "Jiam");
    testVariation("Noah", "Moah");
    testVariation("Oliver", "Aliver");
    testVariation("Elijah", "Alijah");
    testVariation("James", "Games");
    testVariation("William", "Villiam");
    testVariation("Benjamin", "Venjamin");
    testVariation("Lucas", "Jucas");
    testVariation("Henry", "Venry");
    testVariation("Olivia", "Alivia");
    testVariation("Emma", "Amma");
    testVariation("Charlotte", "Sarlotte");
    testVariation("Amelia", "Emelia");
    testVariation("Ava", "Eva");
    testVariation("Sophia", "Tsophia");
    testVariation("Isabella", "Esabella");
    testVariation("Mia", "Nia");
    testVariation("Evelyn", "Avelyn");
    testVariation("Harper", "Varper");
  }

  private void testVariation(String startValue, String endValue) {
    assertEquals(endValue, FirstNameAlternativeBeginnings.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, "PID-5.2=" + endValue);
    testEquals(testStart, testEnd, ProcedureFactory.FIRST_NAME_ALTERNATIVE_BEGINNINGS);
  }

}
