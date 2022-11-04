package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;

public class SuffixVariationTest extends ProcedureCommonTest {


  @Test
  public void test() {
    testVariation("Smith", "Sam", "Jr", "Smith Jr", "Sam", "");
    testVariation("Smith Jr", "Sam", "", "Smith", "Sam Jr", "");
    testVariation("Smith", "Sam Jr", "", "Smith", "Sam", "Jr");
    testVariation("Smith", "Sam", "", "Smith", "Sam", null);
    testVariation("Smith", "Sam", "UNKNOWN", "Smith", "Sam", null);
  }


  private void testVariation(String startLast, String startFirst, String startSuffix,
      String endLast, String endFirst, String endSuffix) {
    String[] result = SuffixVariation.varyName(startLast, startFirst, startSuffix);
    assertEquals(endLast, result[0]);
    assertEquals(endFirst, result[1]);
    if (endSuffix == null) {
      assertNotEquals("", result[2]);
      assertNotEquals(startSuffix, result[2]);
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
