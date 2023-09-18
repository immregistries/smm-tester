package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class ProcedureCommonTest extends TestCase {

  protected static final String DEFAULT_TEST_MESSAGE =
      "MSH|^~\\&|||||20221102102500-0600||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r"
          + "PID|1||R52Z397663^^^AIRA^MR||StallardAIRA^RebeccaAIRA^Olivia^^^^L|BooneAIRA^RaginiAIRA^^^^^M|20211021|F||2106-3^White^CDCREC|1562 Weerijnen Ln^^Grant^MI^49327^USA^P||^PRN^PH^^^231^5666431~^NET^^email@email.com|||||||||2186-5^not Hispanic or Latino^CDCREC||N||||||N\r"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|N|20221102|||A|20221102|20221102\r"
          + "NK1|1|StallardAIRA^BooneAIRA^Marion^^^^L|MTH^Mother^HL70063|1562 Weerijnen Ln^^Grant^MI^49327^USA^P|^PRN^PH^^^231^5666431\r"
          + "ORC|RE|AR52Z397663.1^AIRA|BR52Z397663.1^AIRA|||||||I-23432^Burden^Donna^A^^^^^AIRA^L^^^PRN||57422^RADON^NICHOLAS^JOHN^^^^^AIRA^L^^^PRN\r"
          + "RXA|0|1|20221102||94^MMRV^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001|7824^Jackson^Lily^Suzanne^^^^^AIRA^L^^^PRN|^^^AIRA-Clinic-1||||Q3110HZ|20230728|MSD^Merck and Co^MVX|||CP|A\r"
          + "RXR|C38299^Subcutaneous^NCIT|LT^Left Thigh^HL70163\r"
          + "OBX|1|CE|30963-3^Vaccine Funding Source^LN|1|PHC70^Private^CDCPHINVS||||||F|||20221102\r"
          + "OBX|2|CE|64994-7^Vaccine Funding Program Eligibility^LN|2|V01^Not VFC Eligible^HL70064||||||F|||20221102|||VXC40^per immunization^CDCPHINVS\r"
          + "OBX|3|CE|69764-9^Document Type^LN|3|253088698300013411100521^Measles Mumps Rubella Varicella VIS^cdcgs1vis||||||F|||20221102\r"
          + "OBX|4|DT|29769-7^Date Vis Presented^LN|3|20221102||||||F|||20221102\r";

  protected void testEquals(String om, String fm, String procedure) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(om);
    testCaseMessage.appendCustomTransformation(" run procedure " + procedure);
    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    assertEquals(fm, testCaseMessage.getMessageText());
  }

  protected void testEquals(String om, String fm1, String fm2, String procedure) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(om);
    testCaseMessage.appendCustomTransformation(" run procedure " + procedure);
    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    String messageText = testCaseMessage.getMessageText();
    assertTrue(fm1.equals(messageText) || fm2.equals(messageText));
  }

  protected void testProcedureDoesNotChangeMessage(String om, String fieldText, String procedure) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(om);
    testCaseMessage.appendCustomTransformation(" run procedure " + procedure);
    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    assertEquals(om, testCaseMessage.getMessageText());
  }

  protected void testProcedureChangesMessageAndDoesNotContain(String om, String fieldText,
      String procedure) {
    String changed = testProcedureChangesMessage(om, procedure);
    assertEquals("Found '" + fieldText + "' in '" + changed + "'", -1, changed.indexOf(fieldText));
  }

  protected void testProcedureChangesMessageAndDoesContain(String om, String fieldText,
      String procedure) {
    assertNotEquals(-1, testProcedureChangesMessage(om, procedure).indexOf(fieldText));
  }

  protected String testProcedureChangesMessage(String om, String procedure) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(om);
    testCaseMessage.appendCustomTransformation(" run procedure " + procedure);
    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    assertNotEquals(om, testCaseMessage.getMessageText());
    return testCaseMessage.getMessageText();
  }

  protected String transform(String om, String transform) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(om);
    testCaseMessage.appendCustomTransformation(transform);
    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    return testCaseMessage.getMessageText();
  }

  protected String processProcedureChangesMessage(String om, String procedure) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(om);
    testCaseMessage.appendCustomTransformation(" run procedure " + procedure);
    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    return testCaseMessage.getMessageText();
  }

  @Test
  public void testReadRepeatValue() {
    assertEquals("", ProcedureCommon.readRepeatValue("^NET^^email@email.com", 1));
    assertEquals("NET", ProcedureCommon.readRepeatValue("^NET^^email@email.com", 2));
    assertEquals("", ProcedureCommon.readRepeatValue("^NET^^email@email.com", 3));
    assertEquals("email@email.com", ProcedureCommon.readRepeatValue("^NET^^email@email.com", 4));
    assertEquals("email@email.com", ProcedureCommon.readRepeatValue("^NET^^email@email.com^", 4));
    assertEquals("", ProcedureCommon.readRepeatValue("^NET^^email@email.com^", 5));
    assertEquals("", ProcedureCommon.readRepeatValue("^NET^^email@email.com", 5));
    assertEquals("", ProcedureCommon.readRepeatValue("^NET^^email@email.com", 7));
  }

  @Test
  public void testMakeEmailValid() {
    assertEquals("", ProcedureCommon.makeEmailValid(null));
    assertEquals("", ProcedureCommon.makeEmailValid(""));
    assertEquals("", ProcedureCommon.makeEmailValid(" "));
    assertEquals("", ProcedureCommon.makeEmailValid("\t"));
    assertEquals("", ProcedureCommon.makeEmailValid("\r\n"));
    assertEquals("", ProcedureCommon.makeEmailValid("\n"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bob vance@gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bob  vance@gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bob   vance@gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bob    vance@gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bob\tvance@gmail.com"));
    assertEquals("address@newberlin.com",
        ProcedureCommon.makeEmailValid("address@new\r\nberlin.com"));
    assertEquals("address@newberlin.com",
        ProcedureCommon.makeEmailValid("address@new\nberlin.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid(".bobvance@gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance.@gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid(".bobvance.@gmail.com"));
    assertEquals("bob.vance@gmail.com", ProcedureCommon.makeEmailValid(".bob.vance.@gmail.com"));
    assertEquals("bob.vance@gmail.com",
        ProcedureCommon.makeEmailValid("...bob.vance.....@gmail.com"));
    assertEquals("bob.vance@gmail.com", ProcedureCommon.makeEmailValid("bob..vance@gmail.com"));
    assertEquals("bob.vance@gmail.com", ProcedureCommon.makeEmailValid("bob...vance@gmail.com"));
    assertEquals("bob.v.ance@gmail.com",
        ProcedureCommon.makeEmailValid("bob....v......ance@gmail.com"));
    assertEquals("1234567890123456789012345678901234567890123456789012345678901234@gmail.com",
        ProcedureCommon.makeEmailValid(
            "1234567890123456789012345678901234567890123456789012345678901234567890@gmail.com"));
    assertEquals("1234567890123456789012345678901234567890123456789012345678901234@gmail.com",
        ProcedureCommon.makeEmailValid(
            "1234567890123456789012345678901234567890123456789012345678901234@gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@.gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@gmail.com."));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@.gmail.com."));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@..gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@gmail.com.."));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@..gmail.com.."));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@....gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@gmail.com...."));
    assertEquals("bobvance@gmail.com",
        ProcedureCommon.makeEmailValid("bobvance@....gmail.com...."));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@-gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@gmail.com-"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@-gmail.com-"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@--gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@gmail.com--"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@--gmail.com--"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@----gmail.com"));
    assertEquals("bobvance@gmail.com", ProcedureCommon.makeEmailValid("bobvance@gmail.com----"));
    assertEquals("bobvance@gmail.com",
        ProcedureCommon.makeEmailValid("bobvance@----gmail.com----"));
    assertEquals("bobvance@gmail.com",
        ProcedureCommon.makeEmailValid("bobvance@....gmail.....com...."));
  }
}
