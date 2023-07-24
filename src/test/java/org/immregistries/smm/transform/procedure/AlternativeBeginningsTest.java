package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
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
    testVariation("", "", location, procedure);

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
    testVariation("", "", location, procedure);

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
    testVariation("", "", location, procedure);

    location = "PID-6.1";
    procedure = ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_BEGINNINGS;

    testVariation("Pastel", "Quastel", location, procedure);
    testVariation("Sanders", "Tsanders", location, procedure);
    testVariation("Xander", "Ekander", location, procedure);
    testVariation("Yennifer", "Wennifer", location, procedure);
    testVariation("Zozo", "Sozo", location, procedure);
    testVariation("", "", location, procedure);

    location = "PID-6.2";
    procedure = ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_BEGINNINGS;

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
    testVariation("", "", location, procedure);

    location = "PID-11.1";
    procedure = ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_BEGINNINGS;

    testVariation("555 Crescent Ln.", "655 Crescent Ln.", location, procedure);
    testVariation("1000 North La Sierra Dr.", "2000 North La Sierra Dr.", location, procedure);
    testVariation("808 Beechwood Drive", "908 Beechwood Drive", location, procedure);
    testVariation("W984 Gainsway Ave", "V984 Gainsway Ave", location, procedure);
    testVariation("N9655 Denning Road", "M9655 Denning Road", location, procedure);
    testVariation("12 Glen Ridge Blvd.", "22 Glen Ridge Blvd.", location, procedure);
    testVariation("100a Bohemia Circle", "200a Bohemia Circle", location, procedure);
    testVariation("988112 Albany Street", "088112 Albany Street", location, procedure);
    testVariation("16 Abbey Rd.", "26 Abbey Rd.", location, procedure);
    testVariation("5 Shore Ave.", "6 Shore Ave.", location, procedure);
    testVariation("", "", location, procedure);

    location = "PID-11.3";
    procedure = ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_BEGINNINGS;

    testVariation("Kansas City", "Cansas City", location, procedure);
    testVariation("Chandler", "Sandler", location, procedure);
    testVariation("Las Vegas", "Jas Vegas", location, procedure);
    testVariation("Buffalo", "Vuffalo", location, procedure);
    testVariation("Lexington-Fayette", "Jexington-Fayette", location, procedure);
    testVariation("Minneapolis", "Ninneapolis", location, procedure);
    testVariation("St. Paul", "T. Paul", location, procedure);
    testVariation("North Hempstead", "Morth Hempstead", location, procedure);
    testVariation("Nashville-Davidson", "Mashville-Davidson", location, procedure);
    testVariation("Salt Lake City", "Tsalt Lake City", location, procedure);
    testVariation("", "", location, procedure);

    location = "PID-13.4";
    procedure = ProcedureFactory.EMAIL_ALTERNATIVE_BEGINNINGS;

    // random emails from randomlists.com
    testDifferent("dwendlan@me.com", "twendlan@me.com", location, procedure);
    testDifferent("lauronen@msn.com", "jauronen@msn.com", location, procedure);
    testDifferent("wmszeliga@gmail.com", "vmszeliga@gmail.com", location, procedure);
    testDifferent("isotopian@yahoo.com", "esotopian@yahoo.com", location, procedure);
    testDifferent("osrin@att.net", "asrin@att.net", location, procedure);
    testDifferent("paina@optonline.net", "quaina@optonline.net", location, procedure);
    testDifferent("hamilton@gmail.com", "vamilton@gmail.com", location, procedure);
    testDifferent("gboss@mac.com", "kboss@mac.com", location, procedure);
    testDifferent("peoplesr@comcast.net", "queoplesr@comcast.net", location, procedure);
    testDifferent("lipeng@gmail.com", "jipeng@gmail.com", location, procedure);
  }

  private void testVariation(String startValue, String endValue, String location,
      String procedure) {
    assertEquals(endValue, AlternativeBeginnings.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }

  private void testDifferent(String startValue, String endValue, String location,
      String procedure) {
    assertEquals(endValue, AlternativeBeginnings.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String after = processProcedureChangesMessage(testStart, procedure);
    assertNotEquals(testStart, after);
  }
}
