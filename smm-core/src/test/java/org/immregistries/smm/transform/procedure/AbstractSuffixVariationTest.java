package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;

public abstract class AbstractSuffixVariationTest extends ProcedureCommonTest {

  protected abstract AbstractSuffixVariation getInstance();
  
  protected abstract String getProcedureFactoryName();
  
  protected void testVariation(String startLast, String startFirst, String startSuffix,
      String endLast, String endFirst, String endSuffix) {

    String[] result = getInstance().varyName(startLast, startFirst, startSuffix);

    assertEquals(endLast, result[0]);
    assertEquals(endFirst, result[1]);

    if (endSuffix == null) {
      assertNotEquals("", result[2]);
      assertNotEquals(startSuffix, result[2]);

      boolean found = false;
      for (String validSuffix : getInstance().getSuffixes()) {
        if (validSuffix.equalsIgnoreCase(result[2])) {
          found = true;
          break;
        }
      }
      assertTrue("actual suffix '" + result[2] + "' not found in " + getClass().getSimpleName() + ".SUFFIXES", found);
    } else {
      assertEquals(endSuffix, result[2]);
      String ts = "PID-5.1=" + startLast + "\nPID-5.2=" + startFirst + "\nPID-5.4=" + startSuffix;
      String te = "PID-5.1=" + endLast + "\nPID-5.2=" + endFirst + "\nPID-5.4=" + endSuffix;
      String testStart = transform(DEFAULT_TEST_MESSAGE, ts);
      String testEnd = transform(DEFAULT_TEST_MESSAGE, te);
      testEquals(testStart, testEnd, getProcedureFactoryName());
    }
  }
}
