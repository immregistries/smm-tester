package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class RepeatedConsonantsTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    String location = "PID-5.1";
    String procedure = ProcedureFactory.LAST_NAME_REPEATED_CONSONANTS;

    testVariationDifferent("Sanchez", location, procedure, transformer);
    testVariationDifferent("Yates", location, procedure, transformer);
    testVariationDifferent("Wiggins", location, procedure, transformer);
    testVariationDifferent("Vasquez", location, procedure, transformer);
    testVariationDifferent("Lambert", location, procedure, transformer);
    testVariationDifferent("Daugherty", location, procedure, transformer);

    location = "PID-5.2";
    procedure = ProcedureFactory.FIRST_NAME_REPEATED_CONSONANTS;

    testVariationDifferent("Sammie", location, procedure, transformer);
    testVariationDifferent("Sam", location, procedure, transformer);
    testVariationDifferent("Kalyn", location, procedure, transformer);
    testVariationDifferent("Howard", location, procedure, transformer);
    testVariationDifferent("Heather", location, procedure, transformer);
    testVariationDifferent("Ivan", location, procedure, transformer);

    location = "PID-5.3";
    procedure = ProcedureFactory.MIDDLE_NAME_REPEATED_CONSONANTS;

    testVariationDifferent("Bradley", location, procedure, transformer);
    testVariationDifferent("Blankenship", location, procedure, transformer);
    testVariationDifferent("Mccarty", location, procedure, transformer);
    testVariationDifferent("Barrett", location, procedure, transformer);
    testVariationDifferent("Matthews", location, procedure, transformer);
    testVariationDifferent("Wyatt", location, procedure, transformer);

    location = "PID-6.1";
    procedure = ProcedureFactory.MOTHERS_MAIDEN_NAME_REPEATED_CONSONANTS;

    testVariationDifferent("Simmons", location, procedure, transformer);
    testVariationDifferent("Rasmussen", location, procedure, transformer);
    testVariationDifferent("Blanchard", location, procedure, transformer);
    testVariationDifferent("Chandler", location, procedure, transformer);
    testVariationDifferent("Carpenter", location, procedure, transformer);
    testVariationDifferent("Pitts", location, procedure, transformer);

    location = "PID-6.2";
    procedure = ProcedureFactory.MOTHERS_MAIDEN_FIRST_NAME_REPEATED_CONSONANTS;

    testVariationDifferent("Daisy", location, procedure, transformer);
    testVariationDifferent("Mary", location, procedure, transformer);
    testVariationDifferent("Gail", location, procedure, transformer);
    testVariationDifferent("Debra", location, procedure, transformer);
    testVariationDifferent("Missy", location, procedure, transformer);
    testVariationDifferent("Joan", location, procedure, transformer);

    location = "PID-11.1";
    procedure = ProcedureFactory.ADDRESS_STREET_REPEATED_CONSONANTS;

    testVariationDifferent("1001 Howard Drive.", location, procedure, transformer);
    testVariationDifferent("1600 Pennsylvania Avenue", location, procedure, transformer);
    testVariationDifferent("1000 North La Sierra Dr.", location, procedure, transformer);
    testVariationDifferent("W984 Gainsway Ave", location, procedure, transformer);
    testVariationDifferent("100a Bohemia Circ.", location, procedure, transformer);
    testVariationDifferent("N9655 Denning Road", location, procedure, transformer);

    location = "PID-11.3";
    procedure = ProcedureFactory.ADDRESS_CITY_REPEATED_CONSONANTS;

    testVariationDifferent("Kansas City", location, procedure, transformer);
    testVariationDifferent("Lexington-Fayette", location, procedure, transformer);
    testVariationDifferent("St. Paul", location, procedure, transformer);
    testVariationDifferent("Salt Lake City", location, procedure, transformer);
    testVariationDifferent("Chandler", location, procedure, transformer);
    testVariationDifferent("North Hempstead", location, procedure, transformer);

    location = "PID-13.4";
    procedure = ProcedureFactory.EMAIL_REPEATED_CONSONANTS;

    // random emails from randomlists.com
    testVariationDifferent("dwendlan@me.com", location, procedure, transformer);
    testVariationDifferent("lauronen@msn.com", location, procedure, transformer);
    testVariationDifferent("wmszeliga@gmail.com", location, procedure, transformer);
    testVariationDifferent("isotopian@yahoo.com", location, procedure, transformer);
    testVariationDifferent("osrin@att.net", location, procedure, transformer);
    testVariationDifferent("lipeng@gmail.com", location, procedure, transformer);
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer) {
    assertNotEquals(startValue, RepeatedConsonants.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);

    System.out.println("testStart = " + testStart);

    testProcedureChangesMessage(testStart, procedure);
  }
}
