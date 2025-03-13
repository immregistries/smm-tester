package org.immregistries.hart.transform.procedure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.hart.transform.Transformer;
import org.junit.Test;

public class LastNamePrefixVariationTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer transformer = new Transformer();

    assertTrue(LastNamePrefixVariation.varyName("Smith", transformer).endsWith(" Smith"));
    assertTrue(LastNamePrefixVariation.varyName("Von", transformer).startsWith("Von "));
    assertTrue(LastNamePrefixVariation.varyName("smith", transformer).endsWith(" smith"));
    assertTrue(LastNamePrefixVariation.varyName("SMITH", transformer).endsWith(" SMITH"));

    List<String> locations = new ArrayList<>(Arrays.asList("PID-5.1=", "PID-6.1="));
    List<String> procedures =
        new ArrayList<>(Arrays.asList(ProcedureFactory.LAST_NAME_PREFIX_VARIATION,
            ProcedureFactory.MOTHERS_MAIDEN_NAME_PREFIX_VARIATION));

    for (int i = 0; i < locations.size(); i++) {
      String location = locations.get(i);
      String procedure = procedures.get(i);
      testVariation("Von Washington", "VonWashington", location, procedure);
      testVariation("VonWashington", "Von Washington", location, procedure);
      testVariation("De La Washington", "DeLaWashington", location, procedure);
      testVariation("DeLaWashington", "De La Washington", location, procedure);
    }
  }

  private void testVariation(String startValue, String endValue, String location,
      String procedure) {
    Transformer transformer = new Transformer();
    assertEquals(endValue, LastNamePrefixVariation.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + endValue);
    testEquals(testStart, testEnd, procedure);
  }
}
