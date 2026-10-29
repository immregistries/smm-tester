package org.immregistries.smm.tester.manager.query;

import org.junit.Test;
import junit.framework.TestCase;

public class QueryConverterQBPZ45Test extends TestCase {

  private static final String VXU =
      "MSH|^~\\&|||||20260701120000-0500||VXU^V04^VXU_V04|MSG1|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS|\r"
          + "PID|1||V43H49^^^AIRA-TEST^MR||Smith^Jane^Q^^^^L|Jones^^^^^^M|19900123|F||2106-3^White^CDCREC|1345 Zulst St^^Saginaw^MI^48605^USA^P||^PRN^PH^^^989^5114370|\r"
          + "ORC|RE||V43H49.3^AIRA|\r"
          + "RXA|0|1|20260701||03^MMR^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001|||||||||||CP|A|\r";

  @Test
  public void testConvertAddsProfileAndObx() {
    QueryConverterQBPZ45 converter = new QueryConverterQBPZ45("007=Y,170=20260701");
    String query = converter.convert(VXU);
    String[] segments = query.split("\r");
    assertTrue(segments[0].startsWith("MSH|"));
    assertTrue(segments[0].contains("|QBP^Q11^QBP_Q11|"));
    assertTrue(segments[0].contains("|Z45^CDCPHINVS|"));
    assertFalse(query.contains("Z44"));
    assertTrue(segments[1].startsWith(
        "QPD|Z45^Request Evaluated Immunization History and Risk-Based Forecast^CDCPHINVS|"));
    assertEquals("OBX|1|ID|007^Patient is currently pregnant^99CDS|1|Y||||||F", segments[2]);
    assertEquals("OBX|2|DT|170^Onset of pregnancy^99CDS|2|20260701||||||F", segments[3]);
    assertTrue(segments[4].startsWith("RCP|"));
  }

  @Test
  public void testNoConditionsMeansNoObx() {
    String query = new QueryConverterQBPZ45().convert(VXU);
    assertFalse(query.contains("OBX|"));
    assertTrue(query.contains("RCP|"));
  }

  @Test
  public void testUnknownCodeStillSent() {
    String query = new QueryConverterQBPZ45("999=Y").convert(VXU);
    assertTrue(query.contains("OBX|1|ID|999^Unrecognized condition^99CDS|1|Y||||||F"));
  }

  @Test
  public void testFactoryAndLabel() {
    assertTrue(QueryConverter.getQueryConverter(QueryType.QBP_Z45) instanceof QueryConverterQBPZ45);
    assertEquals(QueryType.QBP_Z45, QueryType.getValue("QBP-Z45"));
  }
}
