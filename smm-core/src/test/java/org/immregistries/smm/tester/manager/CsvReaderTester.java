package org.immregistries.smm.tester.manager;

import java.util.List;
import junit.framework.TestCase;

public class CsvReaderTester extends TestCase {
  private static String TEST_LINE1 =
      "MSH-3,\"private static final String FIELD_MSH_3=\"\"MSH-3\"\";\",Sending Application,,,R_OR_X,MSH-3=R_OR_X,,,,,,,,";

  public void testmain() throws Exception {
    List<String> valueList = CsvReader.readValuesFromCsv(TEST_LINE1);
    assertEquals("MSH-3", CsvReader.readValue(0, valueList));
    assertEquals("private static final String FIELD_MSH_3=\"MSH-3\";",
        CsvReader.readValue(1, valueList));
    assertEquals("Sending Application", CsvReader.readValue(2, valueList));
    assertEquals("MSH-3=R_OR_X", CsvReader.readValue(6, valueList));
  }
}
