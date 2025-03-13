package org.immregistries.hart.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.hart.transform.procedure.AlternativeBeginnings.Field;
import org.junit.Test;

public class AlternativeBeginningsTest extends ProcedureCommonTest {

  @Test
  public void test() {
    String location = "PID-5.1";
    String procedure = ProcedureFactory.LAST_NAME_ALTERNATIVE_BEGINNINGS;
    Field field = Field.LAST_NAME;

    testVariation("Christopher", "Kristopher", location, procedure, field);
    testVariation("Spletzer", "Pletzer", location, procedure, field);
    testVariation("Squib", "Quib", location, procedure, field);
    testVariation("Thraselton", "Fraselton", location, procedure, field);
    testVariation("Bland", "Land", location, procedure, field);
    testVariation("Brown", "Rown", location, procedure, field);
    testVariation("Fleckner", "Leckner", location, procedure, field);
    testVariation("Fraselton", "Thaselton", location, procedure, field);
    testVariation("Klaus", "Laus", location, procedure, field);
    testVariation("Proud", "Roud", location, procedure, field);
    testVariation("Schrute", "Chrute", location, procedure, field);
    testVariation("Skellig", "Cellig", location, procedure, field);
    testVariation("Spratt", "Pratt", location, procedure, field);
    testVariation("Stclair", "Tclair", location, procedure, field);
    testVariation("Swartzman", "Wartzman", location, procedure, field);
    testVariation("Thaselton", "Faselton", location, procedure, field);
    testVariation("Twanbly", "Wanbly", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-5.2";
    procedure = ProcedureFactory.FIRST_NAME_ALTERNATIVE_BEGINNINGS;
    field = Field.FIRST_NAME;

    testVariation("Sam", "Tsam", location, procedure, field);
    testVariation("Liam", "Jiam", location, procedure, field);
    testVariation("Noah", "Moah", location, procedure, field);
    testVariation("Oliver", "Aliver", location, procedure, field);
    testVariation("Elijah", "Alijah", location, procedure, field);
    testVariation("James", "Games", location, procedure, field);
    testVariation("William", "Villiam", location, procedure, field);
    testVariation("Benjamin", "Venjamin", location, procedure, field);
    testVariation("Lucas", "Jucas", location, procedure, field);
    testVariation("Henry", "Venry", location, procedure, field);
    testVariation("Olivia", "Alivia", location, procedure, field);
    testVariation("Emma", "Amma", location, procedure, field);
    testVariation("Charlotte", "Sarlotte", location, procedure, field);
    testVariation("Amelia", "Emelia", location, procedure, field);
    testVariation("Ava", "Eva", location, procedure, field);
    testVariation("Sophia", "Tsophia", location, procedure, field);
    testVariation("Isabella", "Esabella", location, procedure, field);
    testVariation("Mia", "Nia", location, procedure, field);
    testVariation("Evelyn", "Avelyn", location, procedure, field);
    testVariation("Harper", "Varper", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-5.3";
    procedure = ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_BEGINNINGS;
    field = Field.MIDDLE_NAME;

    testVariation("Clark", "Klark", location, procedure, field);
    testVariation("Crow", "Krow", location, procedure, field);
    testVariation("Drake", "Trake", location, procedure, field);
    testVariation("Dwight", "Twight", location, procedure, field);
    testVariation("Glass", "Lass", location, procedure, field);
    testVariation("Grass", "Gass", location, procedure, field);
    testVariation("Krow", "Chrow", location, procedure, field);
    testVariation("Plaza", "Laza", location, procedure, field);
    testVariation("Shane", "Thane", location, procedure, field);
    testVariation("Slackford", "Lackford", location, procedure, field);
    testVariation("Snore", "Nore", location, procedure, field);
    testVariation("Stratford", "Tatford", location, procedure, field);
    testVariation("Trill", "Rill", location, procedure, field);
    testVariation("Wrtrykowski", "Rtrykowski", location, procedure, field);
    testVariation("Quinn", "Kinn", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-6.1";
    procedure = ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_BEGINNINGS;
    field = Field.MOTHERS_MAIDEN_NAME;

    testVariation("Pastel", "Quastel", location, procedure, field);
    testVariation("Sanders", "Tsanders", location, procedure, field);
    testVariation("Xander", "Ekander", location, procedure, field);
    testVariation("Yennifer", "Wennifer", location, procedure, field);
    testVariation("Zozo", "Sozo", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-6.2";
    procedure = ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_BEGINNINGS;
    field = Field.MOTHERS_FIRST_NAME;

    testVariation("Olivia", "Alivia", location, procedure, field);
    testVariation("Emma", "Amma", location, procedure, field);
    testVariation("Charlotte", "Sarlotte", location, procedure, field);
    testVariation("Amelia", "Emelia", location, procedure, field);
    testVariation("Ava", "Eva", location, procedure, field);
    testVariation("Sophia", "Tsophia", location, procedure, field);
    testVariation("Isabella", "Esabella", location, procedure, field);
    testVariation("Mia", "Nia", location, procedure, field);
    testVariation("Evelyn", "Avelyn", location, procedure, field);
    testVariation("Harper", "Varper", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-11.1";
    procedure = ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_BEGINNINGS;
    field = Field.ADDRESS_STREET;

    testVariation("555 Crescent Ln.", "655 Crescent Ln.", location, procedure, field);
    testVariation("1000 North La Sierra Dr.", "2000 North La Sierra Dr.", location, procedure,
        field);
    testVariation("808 Beechwood Drive", "908 Beechwood Drive", location, procedure, field);
    testVariation("W984 Gainsway Ave", "V984 Gainsway Ave", location, procedure, field);
    testVariation("N9655 Denning Road", "M9655 Denning Road", location, procedure, field);
    testVariation("12 Glen Ridge Blvd.", "22 Glen Ridge Blvd.", location, procedure, field);
    testVariation("100a Bohemia Circle", "200a Bohemia Circle", location, procedure, field);
    testVariation("988112 Albany Street", "088112 Albany Street", location, procedure, field);
    testVariation("16 Abbey Rd.", "26 Abbey Rd.", location, procedure, field);
    testVariation("5 Shore Ave.", "6 Shore Ave.", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-11.3";
    procedure = ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_BEGINNINGS;
    field = Field.ADDRESS_CITY;

    testVariation("Kansas City", "Cansas City", location, procedure, field);
    testVariation("Chandler", "Sandler", location, procedure, field);
    testVariation("Las Vegas", "Jas Vegas", location, procedure, field);
    testVariation("Buffalo", "Vuffalo", location, procedure, field);
    testVariation("Lexington-Fayette", "Jexington-Fayette", location, procedure, field);
    testVariation("Minneapolis", "Ninneapolis", location, procedure, field);
    testVariation("St. Paul", "T. Paul", location, procedure, field);
    testVariation("North Hempstead", "Morth Hempstead", location, procedure, field);
    testVariation("Nashville-Davidson", "Mashville-Davidson", location, procedure, field);
    testVariation("Salt Lake City", "Tsalt Lake City", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-13.4";
    procedure = ProcedureFactory.EMAIL_ALTERNATIVE_BEGINNINGS;
    field = Field.EMAIL;

    // random emails from randomlists.com
    testDifferent("dwendlan@me.com", "twendlan@me.com", location, procedure, field);
    testDifferent("lauronen@msn.com", "jauronen@msn.com", location, procedure, field);
    testDifferent("wmszeliga@gmail.com", "vmszeliga@gmail.com", location, procedure, field);
    testDifferent("isotopian@yahoo.com", "esotopian@yahoo.com", location, procedure, field);
    testDifferent("osrin@att.net", "asrin@att.net", location, procedure, field);
    testDifferent("paina@optonline.net", "quaina@optonline.net", location, procedure, field);
    testDifferent("hamilton@gmail.com", "vamilton@gmail.com", location, procedure, field);
    testDifferent("gboss@mac.com", "kboss@mac.com", location, procedure, field);
    testDifferent("peoplesr@comcast.net", "queoplesr@comcast.net", location, procedure, field);
    testDifferent("lipeng@gmail.com", "jipeng@gmail.com", location, procedure, field);
  }

  private void testVariation(String startValue, String endValue, String location, String procedure,
      Field field) {
    assertEquals(endValue, AlternativeBeginnings.varyName(startValue, field));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }

  private void testDifferent(String startValue, String endValue, String location, String procedure,
      Field field) {
    assertEquals(endValue, AlternativeBeginnings.varyName(startValue, field));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String after = processProcedureChangesMessage(testStart, procedure);
    assertNotEquals(testStart, after);
  }
}
