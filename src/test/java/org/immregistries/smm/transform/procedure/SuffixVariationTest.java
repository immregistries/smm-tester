package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class SuffixVariationTest extends ProcedureCommonTest {

  @Test
  public void test() {
    // first bunch will create a suffix
    // the null indicates we don't know which suffix will be randomly selected
    // but we will test that one is there
    testVariation("Smith", "Sam", "", "Smith", "Sam", null);
    testVariation("Smith", "Sam", "UNKNOWN", "Smith", "Sam", null);
    testVariation("Smith", "Sam", "the Second", "Smith", "Sam", null);
    testVariation("Smith", "Sam", "the Third", "Smith", "Sam", null);

    testVariation("Smith", "Sam", "Jr.", "Smith Jr.", "Sam", "");
    testVariation("Smith Jr.", "Sam", "", "Smith", "Sam Jr.", "");
    testVariation("Smith", "Sam Jr.", "", "Smith", "Sam", "Jr.");

    testVariation("Smith", "Sam", "Jr", "Smith Jr", "Sam", "");
    testVariation("Smith Jr", "Sam", "", "Smith", "Sam Jr", "");
    testVariation("Smith", "Sam Jr", "", "Smith", "Sam", "Jr");

    testVariation("Smith", "Sam", "Junior", "Smith Junior", "Sam", "");
    testVariation("Smith Junior", "Sam", "", "Smith", "Sam Junior", "");
    testVariation("Smith", "Sam Junior", "", "Smith", "Sam", "Junior");

    testVariation("Smith", "Sam", "Sr.", "Smith Sr.", "Sam", "");
    testVariation("Smith Sr.", "Sam", "", "Smith", "Sam Sr.", "");
    testVariation("Smith", "Sam Sr.", "", "Smith", "Sam", "Sr.");

    testVariation("Smith", "Sam", "Sr", "Smith Sr", "Sam", "");
    testVariation("Smith Sr", "Sam", "", "Smith", "Sam Sr", "");
    testVariation("Smith", "Sam Sr", "", "Smith", "Sam", "Sr");

    testVariation("Smith", "Sam", "Senior", "Smith Senior", "Sam", "");
    testVariation("Smith Senior", "Sam", "", "Smith", "Sam Senior", "");
    testVariation("Smith", "Sam Senior", "", "Smith", "Sam", "Senior");

    testVariation("Smith", "Sam", "II", "Smith II", "Sam", "");
    testVariation("Smith II", "Sam", "", "Smith", "Sam II", "");
    testVariation("Smith", "Sam II", "", "Smith", "Sam", "II");

    testVariation("Smith", "Sam", "III", "Smith III", "Sam", "");
    testVariation("Smith III", "Sam", "", "Smith", "Sam III", "");
    testVariation("Smith", "Sam III", "", "Smith", "Sam", "III");

    testVariation("Smith", "Sam", "2nd", "Smith 2nd", "Sam", "");
    testVariation("Smith 2nd", "Sam", "", "Smith", "Sam 2nd", "");
    testVariation("Smith", "Sam 2nd", "", "Smith", "Sam", "2nd");

    testVariation("Smith", "Sam", "3rd", "Smith 3rd", "Sam", "");
    testVariation("Smith 3rd", "Sam", "", "Smith", "Sam 3rd", "");
    testVariation("Smith", "Sam 3rd", "", "Smith", "Sam", "3rd");

    testVariation("SMITH", "SAM", "3rd", "SMITH 3RD", "SAM", "");
    testVariation("SMITH 3RD", "SAM", "", "SMITH", "SAM 3RD", "");
    testVariation("SMITH", "SAM 3RD", "", "SMITH", "SAM", "3rd");

    testVariation("smith", "sam", "jr", "smith jr", "sam", "");
    testVariation("smith jr", "sam", "", "smith", "sam jr", "");
    testVariation("smith", "sam jr", "", "smith", "sam", "Jr");
  }

  private void testVariation(String startLast, String startFirst, String startSuffix,
      String endLast, String endFirst, String endSuffix) {

    String[] result = SuffixVariation.varyName(startLast, startFirst, startSuffix);

    assertEquals(endLast, result[0]);
    assertEquals(endFirst, result[1]);

    if (endSuffix == null) {
      assertNotEquals("", result[2]);
      assertNotEquals(startSuffix, result[2]);

      boolean found = false;
      for (String validSuffix : SuffixVariation.SUFFIXES) {
        if (validSuffix.equalsIgnoreCase(result[2])) {
          found = true;
          break;
        }
      }
      assertTrue("actual suffix '" + result[2] + "' not found in SuffixVariation.SUFFIXES", found);
    } else {
      assertEquals(endSuffix, result[2]);
      String ts = "PID-5.1=" + startLast + "\nPID-5.2=" + startFirst + "\nPID-5.4=" + startSuffix;
      String te = "PID-5.1=" + endLast + "\nPID-5.2=" + endFirst + "\nPID-5.4=" + endSuffix;
      String testStart = transform(DEFAULT_TEST_MESSAGE, ts);
      String testEnd = transform(DEFAULT_TEST_MESSAGE, te);
      testEquals(testStart, testEnd, ProcedureFactory.SUFFIX_VARIATION);
    }
  }
}
