package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class RemoveLastNamePrefixTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer transformer = new Transformer();

    assertEquals("Smith", RemoveLastNamePrefix.varyName("Smith", transformer));
    assertEquals("Washington", RemoveLastNamePrefix.varyName("Washington", transformer));
    assertEquals("Smith", RemoveLastNamePrefix.varyName("Von Smith", transformer));
    assertEquals("Smith", RemoveLastNamePrefix.varyName("VonSmith", transformer));
    assertEquals("Donald", RemoveLastNamePrefix.varyName("McDonald", transformer));
    assertEquals("Donald", RemoveLastNamePrefix.varyName("Mc Donald", transformer));
    assertEquals("Hoya", RemoveLastNamePrefix.varyName("De La Hoya", transformer));
    assertEquals("Hoya", RemoveLastNamePrefix.varyName("DeLaHoya", transformer));

    assertEquals("smith", RemoveLastNamePrefix.varyName("smith", transformer));
    assertEquals("washington", RemoveLastNamePrefix.varyName("washington", transformer));
    assertEquals("smith", RemoveLastNamePrefix.varyName("von smith", transformer));
    assertEquals("smith", RemoveLastNamePrefix.varyName("vonsmith", transformer));
    assertEquals("donald", RemoveLastNamePrefix.varyName("mcdonald", transformer));
    assertEquals("donald", RemoveLastNamePrefix.varyName("mc donald", transformer));
    assertEquals("hoya", RemoveLastNamePrefix.varyName("de la hoya", transformer));
    assertEquals("hoya", RemoveLastNamePrefix.varyName("delahoya", transformer));

    assertEquals("SMITH", RemoveLastNamePrefix.varyName("SMITH", transformer));
    assertEquals("WASHINGTON", RemoveLastNamePrefix.varyName("WASHINGTON", transformer));
    assertEquals("SMITH", RemoveLastNamePrefix.varyName("VON SMITH", transformer));
    assertEquals("SMITH", RemoveLastNamePrefix.varyName("VONSMITH", transformer));
    assertEquals("DONALD", RemoveLastNamePrefix.varyName("MCDONALD", transformer));
    assertEquals("DONALD", RemoveLastNamePrefix.varyName("MC DONALD", transformer));
    assertEquals("HOYA", RemoveLastNamePrefix.varyName("DE LA HOYA", transformer));
    assertEquals("HOYA", RemoveLastNamePrefix.varyName("DELAHOYA", transformer));

    assertNotEquals("Von", RemoveLastNamePrefix.varyName("Von", transformer));
    assertNotEquals("De La", RemoveLastNamePrefix.varyName("De La", transformer));
    assertNotEquals("De", RemoveLastNamePrefix.varyName("De La", transformer));
    assertNotEquals("La", RemoveLastNamePrefix.varyName("De La", transformer));
    assertNotEquals("DeLa", RemoveLastNamePrefix.varyName("DeLa", transformer));
    assertNotEquals("De", RemoveLastNamePrefix.varyName("DeLa", transformer));
    assertNotEquals("La", RemoveLastNamePrefix.varyName("DeLa", transformer));

    assertNotEquals("von", RemoveLastNamePrefix.varyName("von", transformer));
    assertNotEquals("de la", RemoveLastNamePrefix.varyName("de la", transformer));

    assertNotEquals("VON", RemoveLastNamePrefix.varyName("VON", transformer));
    assertNotEquals("DE LA", RemoveLastNamePrefix.varyName("DE LA", transformer));

    assertEquals("Test", RemoveLastNamePrefix.varyName("VonTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("Ven HetTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("Van DerTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("Van DenTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("Van DeTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("VanTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("TerTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("MicTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("MhicTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("MckTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("McTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("MacTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("LeTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("LaTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("ElTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("DuTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("DuTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("DosTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("DiTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("DerTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("De LaTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("DellaTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("DelTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("DeTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("DaTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("BetTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("BenTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("BarTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("ApTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("AlTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("AfTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("AbuTest", transformer));
    assertEquals("Test", RemoveLastNamePrefix.varyName("AbTest", transformer));

    List<String> locations = new ArrayList<>(Arrays.asList("PID-5.1=", "PID-6.1="));
    List<String> procedures =
        new ArrayList<>(Arrays.asList(ProcedureFactory.REMOVE_LAST_NAME_PREFIX,
            ProcedureFactory.REMOVE_MOTHERS_MAIDEN_NAME_PREFIX));

    for (int i = 0; i < locations.size(); i++) {
      String location = locations.get(i);
      String procedure = procedures.get(i);
      testVariation("VonWashington", "Washington", location, procedure);
      testVariation("Von Washington", "Washington", location, procedure);
      testVariation("DeLaWashington", "Washington", location, procedure);
      testVariation("De La Washington", "Washington", location, procedure);
    }
  }

  private void testVariation(String startValue, String endValue, String location,
      String procedure) {
    Transformer transformer = new Transformer();
    assertEquals(endValue, RemoveLastNamePrefix.varyName(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + endValue);
    testEquals(testStart, testEnd, procedure);
  }
}
