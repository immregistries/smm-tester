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

    location = "PID-6.1";
    procedure = ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_BEGINNINGS;

    testVariation("Pastel", "Quastel", location, procedure);
    testVariation("Sanders", "Tsanders", location, procedure);
    testVariation("Xander", "Ekander", location, procedure);
    testVariation("Yennifer", "Wennifer", location, procedure);
    testVariation("Zozo", "Sozo", location, procedure);

    location = "PID-6.2";
    procedure = ProcedureFactory.MOTHERS_MAIDEN_FIRST_NAME_ALTERNATIVE_BEGINNINGS;

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

    location = "PID-11.1";
    procedure = ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_BEGINNINGS;

    testVariation("Crescent Ln.", "Krescent Ln.", location, procedure);
    testVariation("North La Sierra Dr.", "Morth La Sierra Dr.", location, procedure);
    testVariation("Beechwood Drive", "Veechwood Drive", location, procedure);
    testVariation("Gainsway Ave", "Kainsway Ave", location, procedure);
    testVariation("Denning Road", "Thenning Road", location, procedure);
    testVariation("Glen Ridge Blvd.", "Len Ridge Blvd.", location, procedure);
    testVariation("Bohemia Circle", "Vohemia Circle", location, procedure);
    testVariation("Albany Street", "Elbany Street", location, procedure);
    testVariation("Abbey Rd.", "Ebbey Rd.", location, procedure);
    testVariation("Shore Ave.", "Thore Ave.", location, procedure);

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

    location = "PID-13.4";
    procedure = ProcedureFactory.EMAIL_ALTERNATIVE_BEGINNINGS;

    // random emails from randomlists.com
    testVariation("dwendlan@me.com", "twendlan@me.com", location, procedure);
    testVariation("lauronen@msn.com", "jauronen@msn.com", location, procedure);
    testVariation("wmszeliga@gmail.com", "vmszeliga@gmail.com", location, procedure);
    testVariation("isotopian@yahoo.com", "esotopian@yahoo.com", location, procedure);
    testVariation("osrin@att.net", "asrin@att.net", location, procedure);
    testVariation("paina@optonline.net", "quaina@optonline.net", location, procedure);
    testVariation("hamilton@gmail.com", "vamilton@gmail.com", location, procedure);
    testVariation("gboss@mac.com", "kboss@mac.com", location, procedure);
    testVariation("peoplesr@comcast.net", "queoplesr@comcast.net", location, procedure);
    testVariation("lipeng@gmail.com", "jipeng@gmail.com", location, procedure);
  }

  private void testVariation(String startValue, String endValue, String location,
      String procedure) {
    assertEquals(endValue, AlternativeBeginnings.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }

}
