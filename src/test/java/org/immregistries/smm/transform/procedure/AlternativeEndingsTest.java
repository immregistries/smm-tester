package org.immregistries.hart.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.hart.transform.procedure.AlternativeEndings.Field;
import org.junit.Test;

public class AlternativeEndingsTest extends ProcedureCommonTest {


  @Test
  public void test() {
    String location = "PID-5.1";
    String procedure = ProcedureFactory.LAST_NAME_ALTERNATIVE_ENDINGS;
    Field field = Field.LAST_NAME;

    testVariation("Brick", "Brikee", location, procedure, field);
    testVariation("Benedict", "Beneditus", location, procedure, field);
    testVariation("Swift", "Swiftee", location, procedure, field);
    testVariation("Fairchild", "Fairchilder", location, procedure, field);
    testVariation("Gandalf", "Gandalfie", location, procedure, field);
    testVariation("Phelp", "Phelpy", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-5.2";
    procedure = ProcedureFactory.FIRST_NAME_ALTERNATIVE_ENDINGS;
    field = Field.FIRST_NAME;

    testVariation("Sam", "Sann", location, procedure, field);
    testVariation("Liam", "Liann", location, procedure, field);
    testVariation("Noah", "Noaph", location, procedure, field);
    testVariation("Oliver", "Oliverrie", location, procedure, field);
    testVariation("Elijah", "Elijaph", location, procedure, field);
    testVariation("James", "Jamesz", location, procedure, field);
    testVariation("William", "Williann", location, procedure, field);
    testVariation("Benjamin", "Benjamimm", location, procedure, field);
    testVariation("Lucas", "Lucasz", location, procedure, field);
    testVariation("Henry", "Henrie", location, procedure, field);
    testVariation("Olivia", "Oliviay", location, procedure, field);
    testVariation("Emma", "Emmay", location, procedure, field);
    testVariation("Charlotte", "Charlottee", location, procedure, field);
    testVariation("Amelia", "Ameliay", location, procedure, field);
    testVariation("Ava", "Avay", location, procedure, field);
    testVariation("Sophia", "Sophiay", location, procedure, field);
    testVariation("Isabella", "Isabellay", location, procedure, field);
    testVariation("Mia", "Miay", location, procedure, field);
    testVariation("Evelyn", "Evelymm", location, procedure, field);
    testVariation("Harper", "Harperrie", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-5.3";
    procedure = ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_ENDINGS;
    field = Field.MIDDLE_NAME;

    testVariation("Kemp", "Kempie", location, procedure, field);
    testVariation("Armond", "Armody", location, procedure, field);
    testVariation("Hank", "Haky", location, procedure, field);
    testVariation("Belmont", "Belmotie", location, procedure, field);
    testVariation("Egypt", "Egyptie", location, procedure, field);
    testVariation("Claiborn", "Claiborny", location, procedure, field);
    testVariation("Edelgard", "Edelgardy", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-6.1";
    procedure = ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_ENDINGS;
    field = Field.MOTHERS_MAIDEN_NAME;

    testVariation("York", "Yorkie", location, procedure, field);
    testVariation("Fisk", "Fisky", location, procedure, field);
    testVariation("Chasm", "Chasmon", location, procedure, field);
    testVariation("Whisp", "Whisper", location, procedure, field);
    testVariation("Forrest", "Forreon", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-6.2";
    procedure = ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_ENDINGS;
    field = Field.MOTHERS_FIRST_NAME;

    testVariation("Olivia", "Oliviay", location, procedure, field);
    testVariation("Emma", "Emmay", location, procedure, field);
    testVariation("Charlotte", "Charlottee", location, procedure, field);
    testVariation("Amelia", "Ameliay", location, procedure, field);
    testVariation("Ava", "Avay", location, procedure, field);
    testVariation("Sophia", "Sophiay", location, procedure, field);
    testVariation("Isabella", "Isabellay", location, procedure, field);
    testVariation("Mia", "Miay", location, procedure, field);
    testVariation("Evelyn", "Evelymm", location, procedure, field);
    testVariation("Harper", "Harperrie", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-11.1";
    procedure = ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_ENDINGS;
    field = Field.ADDRESS_STREET;

    testVariation("555 Crescent Ln.", "555 Crescent Lmm.", location, procedure, field);
    testVariation("1000 North La Sierra Dr.", "1000 North La Sierra Drrie.", location, procedure,
        field);
    testVariation("808 Beechwood Drive", "808 Beechwood Drivee", location, procedure, field);
    testVariation("W984 Gainsway Ave", "W984 Gainsway Avee", location, procedure, field);
    testVariation("N9655 Denning Road", "N9655 Denning Roadd", location, procedure, field);
    testVariation("12 Glen Ridge Blvd.", "12 Glen Ridge Blvdd.", location, procedure, field);
    testVariation("100a Bohemia Circ;", "100a Bohemia Cirk;", location, procedure, field);
    testVariation("988112 Albany Street", "988112 Albany Streets", location, procedure, field);
    testVariation("16 Abbey Rd.", "16 Abbey Rdy.", location, procedure, field);
    testVariation("5 Shore Ave.", "5 Shore Avee.", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-11.3";
    procedure = ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_ENDINGS;
    field = Field.ADDRESS_CITY;

    testVariation("Kansas City", "Kansas Citie", location, procedure, field);
    testVariation("Chandler", "Chandlerrie", location, procedure, field);
    testVariation("Las Vegas", "Las Vegasz", location, procedure, field);
    testVariation("Buffalo", "Buffaloo", location, procedure, field);
    testVariation("Lexington-Fayette", "Lexington-Fayettee", location, procedure, field);
    testVariation("Minneapolis", "Minneapolisz", location, procedure, field);
    testVariation("St. Paul", "St. Pauls", location, procedure, field);
    testVariation("North Hempstead", "North Hempsteadd", location, procedure, field);
    testVariation("Nashville-Davidson", "Nashville-Davidsomm", location, procedure, field);
    testVariation("Salt Lake City", "Salt Lake Citie", location, procedure, field);
    testVariation("", "", location, procedure, field);

    location = "PID-13.4";
    procedure = ProcedureFactory.EMAIL_ALTERNATIVE_ENDINGS;
    field = Field.EMAIL;

    // random emails from randomlists.com
    testDifferent("dwendlan@me.com", "dwendlan@me.conn", location, procedure, field);
    testDifferent("lauronen@msn.com", "lauronen@msn.conn", location, procedure, field);
    testDifferent("wmszeliga@gmail.com", "wmszeliga@gmail.conn", location, procedure, field);
    testDifferent("isotopian@yahoo.com", "isotopian@yahoo.conn", location, procedure, field);
    testDifferent("osrin@att.net", "osrin@att.nets", location, procedure, field);
    testDifferent("paina@optonline.net", "paina@optonline.nets", location, procedure, field);
    testDifferent("hamilton@gmail.com", "hamilton@gmail.conn", location, procedure, field);
    testDifferent("gboss@mac.com", "gboss@mac.conn", location, procedure, field);
    testDifferent("peoplesr@comcast.net", "peoplesr@comcast.nets", location, procedure, field);
    testDifferent("lipeng@gmail.com", "lipeng@gmail.conn", location, procedure, field);
  }

  private void testVariation(String startValue, String endValue, String location, String procedure,
      Field field) {
    assertEquals(endValue, AlternativeEndings.varyName(startValue, field));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }

  private void testDifferent(String startValue, String endValue, String location, String procedure,
      Field field) {
    assertEquals(endValue, AlternativeEndings.varyName(startValue, field));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String after = processProcedureChangesMessage(testStart, procedure);
    assertNotEquals(testStart, after);
  }
}
