package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
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

    location = "PID-6.2";
    procedure = ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_ENDINGS;

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

    location = "PID-11.1";
    procedure = ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_ENDINGS;

    testVariation("555 Crescent Ln.", "555 Crescent Lmm.", location, procedure);
    testVariation("1000 North La Sierra Dr.", "1000 North La Sierra Drrie.", location, procedure);
    testVariation("808 Beechwood Drive", "808 Beechwood Drivee", location, procedure);
    testVariation("W984 Gainsway Ave", "W984 Gainsway Avee", location, procedure);
    testVariation("N9655 Denning Road", "N9655 Denning Roadd", location, procedure);
    testVariation("12 Glen Ridge Blvd.", "12 Glen Ridge Blvdd.", location, procedure);
    testVariation("100a Bohemia Circ;", "100a Bohemia Cirk;", location, procedure);
    testVariation("988112 Albany Street", "988112 Albany Streets", location, procedure);
    testVariation("16 Abbey Rd.", "16 Abbey Rdy.", location, procedure);
    testVariation("5 Shore Ave.", "5 Shore Avee.", location, procedure);

    location = "PID-11.3";
    procedure = ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_ENDINGS;

    testVariation("Kansas City", "Kansas Citie", location, procedure);
    testVariation("Chandler", "Chandlerrie", location, procedure);
    testVariation("Las Vegas", "Las Vegasz", location, procedure);
    testVariation("Buffalo", "Buffaloo", location, procedure);
    testVariation("Lexington-Fayette", "Lexington-Fayettee", location, procedure);
    testVariation("Minneapolis", "Minneapolisz", location, procedure);
    testVariation("St. Paul", "St. Pauls", location, procedure);
    testVariation("North Hempstead", "North Hempsteadd", location, procedure);
    testVariation("Nashville-Davidson", "Nashville-Davidsomm", location, procedure);
    testVariation("Salt Lake City", "Salt Lake Citie", location, procedure);

    location = "PID-13.4";
    procedure = ProcedureFactory.EMAIL_ALTERNATIVE_ENDINGS;

    // random emails from randomlists.com
    testDifferent("dwendlan@me.com", "dwendlan@me.conn", location, procedure);
    testDifferent("lauronen@msn.com", "lauronen@msn.conn", location, procedure);
    testDifferent("wmszeliga@gmail.com", "wmszeliga@gmail.conn", location, procedure);
    testDifferent("isotopian@yahoo.com", "isotopian@yahoo.conn", location, procedure);
    testDifferent("osrin@att.net", "osrin@att.nets", location, procedure);
    testDifferent("paina@optonline.net", "paina@optonline.nets", location, procedure);
    testDifferent("hamilton@gmail.com", "hamilton@gmail.conn", location, procedure);
    testDifferent("gboss@mac.com", "gboss@mac.conn", location, procedure);
    testDifferent("peoplesr@comcast.net", "peoplesr@comcast.nets", location, procedure);
    testDifferent("lipeng@gmail.com", "lipeng@gmail.conn", location, procedure);
  }

  private void testVariation(String startValue, String endValue, String location,
      String procedure) {
    assertEquals(endValue, AlternativeEndings.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }

  private void testDifferent(String startValue, String endValue, String location,
      String procedure) {
    assertEquals(endValue, AlternativeEndings.varyName(startValue));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String after = processProcedureChangesMessage(testStart, procedure);
    assertNotEquals(testStart, after);
  }
}
