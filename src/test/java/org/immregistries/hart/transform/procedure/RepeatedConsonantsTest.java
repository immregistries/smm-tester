package org.immregistries.hart.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.hart.transform.Transformer;
import org.immregistries.hart.transform.procedure.RepeatedConsonants.Field;
import org.junit.Test;

public class RepeatedConsonantsTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();

    for (int i = 0; i < 100; i++) {
      String location = "PID-5.1";
      String procedure = ProcedureFactory.LAST_NAME_REPEATED_CONSONANTS;
      Field field = Field.LAST_NAME;

      testVariationDifferent("Sanchez", location, procedure, transformer, field);
      testVariationDifferent("Yates", location, procedure, transformer, field);
      testVariationDifferent("Wiggins", location, procedure, transformer, field);
      testVariationDifferent("Vasquez", location, procedure, transformer, field);
      testVariationDifferent("Lambert", location, procedure, transformer, field);
      testVariationDifferent("Daugherty", location, procedure, transformer, field);

      location = "PID-5.2";
      procedure = ProcedureFactory.FIRST_NAME_REPEATED_CONSONANTS;
      field = Field.FIRST_NAME;

      testVariationDifferent("Sammie", location, procedure, transformer, field);
      testVariationDifferent("Sam", location, procedure, transformer, field);
      testVariationDifferent("Kalyn", location, procedure, transformer, field);
      testVariationDifferent("Howard", location, procedure, transformer, field);
      testVariationDifferent("Heather", location, procedure, transformer, field);
      testVariationDifferent("Ivan", location, procedure, transformer, field);

      location = "PID-5.3";
      procedure = ProcedureFactory.MIDDLE_NAME_REPEATED_CONSONANTS;
      field = Field.MIDDLE_NAME;

      testVariationDifferent("Bradley", location, procedure, transformer, field);
      testVariationDifferent("Blankenship", location, procedure, transformer, field);
      testVariationDifferent("Mccarty", location, procedure, transformer, field);
      testVariationDifferent("Barrett", location, procedure, transformer, field);
      testVariationDifferent("Matthews", location, procedure, transformer, field);
      testVariationDifferent("Wyatt", location, procedure, transformer, field);

      location = "PID-6.1";
      procedure = ProcedureFactory.MOTHERS_MAIDEN_NAME_REPEATED_CONSONANTS;
      field = Field.MOTHERS_MAIDEN_NAME;

      testVariationDifferent("Simmons", location, procedure, transformer, field);
      testVariationDifferent("Rasmussen", location, procedure, transformer, field);
      testVariationDifferent("Blanchard", location, procedure, transformer, field);
      testVariationDifferent("Chandler", location, procedure, transformer, field);
      testVariationDifferent("Carpenter", location, procedure, transformer, field);
      testVariationDifferent("Pitts", location, procedure, transformer, field);

      location = "PID-6.2";
      procedure = ProcedureFactory.MOTHERS_FIRST_NAME_REPEATED_CONSONANTS;
      field = Field.MOTHERS_FIRST_NAME;

      testVariationDifferent("Daisy", location, procedure, transformer, field);
      testVariationDifferent("Mary", location, procedure, transformer, field);
      testVariationDifferent("Gail", location, procedure, transformer, field);
      testVariationDifferent("Debra", location, procedure, transformer, field);
      testVariationDifferent("Missy", location, procedure, transformer, field);
      testVariationDifferent("Joan", location, procedure, transformer, field);

      location = "PID-11.1";
      procedure = ProcedureFactory.ADDRESS_STREET_REPEATED_CONSONANTS;
      field = Field.ADDRESS_STREET;

      testVariationDifferent("1001 Howard Drive.", location, procedure, transformer, field);
      testVariationDifferent("1600 Pennsylvania Avenue", location, procedure, transformer, field);
      testVariationDifferent("1000 North La Sierra Dr.", location, procedure, transformer, field);
      testVariationDifferent("W984 Gainsway Ave", location, procedure, transformer, field);
      testVariationDifferent("100a Bohemia Circ.", location, procedure, transformer, field);
      testVariationDifferent("N9655 Denning Road", location, procedure, transformer, field);

      location = "PID-11.3";
      procedure = ProcedureFactory.ADDRESS_CITY_REPEATED_CONSONANTS;
      field = Field.ADDRESS_CITY;

      testVariationDifferent("Kansas City", location, procedure, transformer, field);
      testVariationDifferent("Lexington-Fayette", location, procedure, transformer, field);
      testVariationDifferent("St. Paul", location, procedure, transformer, field);
      testVariationDifferent("Salt Lake City", location, procedure, transformer, field);
      testVariationDifferent("Chandler", location, procedure, transformer, field);
      testVariationDifferent("North Hempstead", location, procedure, transformer, field);

      location = "PID-13.4";
      procedure = ProcedureFactory.EMAIL_REPEATED_CONSONANTS;
      field = Field.EMAIL;

      // random emails from randomlists.com
      testVariationDifferent("dwendlan@me.com", location, procedure, transformer, field);
      testVariationDifferent("lauronen@msn.com", location, procedure, transformer, field);
      testVariationDifferent("wmszeliga@gmail.com", location, procedure, transformer, field);
      testVariationDifferent("isotopian@yahoo.com", location, procedure, transformer, field);
      testVariationDifferent("osrin@att.net", location, procedure, transformer, field);
      testVariationDifferent("lipeng@gmail.com", location, procedure, transformer, field);
    }
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer, Field field) {
    String endValue = RepeatedConsonants.varyName(startValue, transformer, field);
    assertNotEquals("start = '" + startValue + "' to " + endValue + "'", startValue, endValue);
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessage(testStart, procedure);
  }
}
