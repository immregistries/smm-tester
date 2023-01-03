package org.immregistries.smm.transform.procedure;

import org.junit.Test;

public class AlternativeBeginningsTest extends ProcedureCommonTest {


  @Test
  public void test() {
    String location = "PID-5.1";
    String procedure = ProcedureFactory.LAST_NAME_ALTERNATIVE_BEGINNINGS;
    
    testVariation("Christopher", "Kristopher", location, procedure);
    testVariation("Spletzer", "Pletzer", location, procedure);
    testVariation("Squib", "Quib", location, procedure);
    testVariation("Thraselton", "Fraselton", location, procedure);
    testVariation("Bland", "Land", location, procedure);
    testVariation("Brown", "Rown", location, procedure);
    testVariation("Fleckner", "Leckner", location, procedure);
    testVariation("Fraselton", "Thaselton", location, procedure);
    testVariation("Klaus", "Laus", location, procedure);
    testVariation("Proud", "Roud", location, procedure);
    testVariation("Schrute", "Chrute", location, procedure);
    testVariation("Skellig", "Cellig", location, procedure);
    testVariation("Spratt", "Pratt", location, procedure);
    testVariation("Stclair", "Tclair", location, procedure);
    testVariation("Swartzman", "Wartzman", location, procedure);
    testVariation("Thaselton", "Faselton", location, procedure);
    testVariation("Twanbly", "Wanbly", location, procedure);
    
    location = "PID-5.2";
    procedure = ProcedureFactory.FIRST_NAME_ALTERNATIVE_BEGINNINGS;
    
    testVariation("Sam", "Tsam", location, procedure);
    testVariation("Liam", "Jiam", location, procedure);
    testVariation("Noah", "Moah", location, procedure);
    testVariation("Oliver", "Aliver", location, procedure);
    testVariation("Elijah", "Alijah", location, procedure);
    testVariation("James", "Games", location, procedure);
    testVariation("William", "Villiam", location, procedure);
    testVariation("Benjamin", "Venjamin", location, procedure);
    testVariation("Lucas", "Jucas", location, procedure);
    testVariation("Henry", "Venry", location, procedure);
    testVariation("Olivia", "Alivia", location, procedure);
    testVariation("Emma", "Amma", location, procedure);
    testVariation("Charlotte", "Sarlotte", location, procedure);
    testVariation("Amelia", "Emelia", location, procedure);
    testVariation("Ava", "Eva", location, procedure);
    testVariation("Sophia", "Tsophia", location, procedure);
    testVariation("Isabella", "Esabella", location, procedure);
    testVariation("Mia", "Nia", location, procedure);
    testVariation("Evelyn", "Avelyn", location, procedure);
    testVariation("Harper", "Varper", location, procedure);
    
    location = "PID-5.3";
    procedure = ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_BEGINNINGS;
    
    testVariation("Clark", "Klark", location, procedure);
    testVariation("Crow", "Krow", location, procedure);
    testVariation("Drake", "Trake", location, procedure);
    testVariation("Dwight", "Twight", location, procedure);
    testVariation("Glass", "Lass", location, procedure);
    testVariation("Grass", "Gass", location, procedure);
    testVariation("Krow", "Chrow", location, procedure);
    testVariation("Plaza", "Laza", location, procedure);
    testVariation("Shane", "Thane", location, procedure);
    testVariation("Slackford", "Lackford", location, procedure);
    testVariation("Snore", "Nore", location, procedure);
    testVariation("Stratford", "Tatford", location, procedure);
    testVariation("Trill", "Rill", location, procedure);
    testVariation("Wrtrykowski", "Rtrykowski", location, procedure);
    testVariation("Quinn", "Kinn", location, procedure);
  }

  private void testVariation(String startValue, String endValue, String location, String procedure) {
    assertEquals(endValue, AlternativeBeginnings.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }

}
