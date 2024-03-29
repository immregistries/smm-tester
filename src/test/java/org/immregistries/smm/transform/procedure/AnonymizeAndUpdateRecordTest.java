package org.immregistries.smm.transform.procedure;

import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class AnonymizeAndUpdateRecordTest extends TestCase {

  private static final String MSH_ORIG =
      "MSH|^~\\&|||||20160805102500-0600||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r";
  private static final String MSH_NEW =
      "MSH|^~\\&|||||20161108102500-0600||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r";

  @Test
  public void test() {
    AnonymizeAndUpdateRecord.setAsOfDate("20161109");
    // Test reading MSH, basic tests
    test(MSH_ORIG, MSH_NEW);
    test(
        "MSH|^~\\&|||||20160805||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test(
        "MSH|^~\\&|||||20160805102500||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108102500||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test(
        "MSH|^~\\&|||||20160805102500-0600~Hello!||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108102500-0600~Hello!||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test("MSH|^~\\&|||||||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test(
        "MSH|^~\\&|||||20160805102500-0600&||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108102500-0600&||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test(
        "MSH|^~\\&|||||20160805102500-0600^||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108102500-0600^||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");
    test(
        "MSH|^~\\&|||||20160805102500-0600^~&||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r",
        "MSH|^~\\&|||||20161108102500-0600^~&||VXU^V04^VXU_V04|1cuT-A.01.01.3n|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r");

    //    test(
    //        MSH_ORIG
    //            + "PID|1||1234^^^AIRA-TEST^MR||Pecos^Sawyer^K^^^^L|Marion^Valisa^^^^^M|20120725|F||2106-3^White^CDCREC|350 Greene Cir^^Little Lake^MI^49833^USA^P||^PRN^PH^^^906^3464569|||||||||2186-5^not Hispanic or Latino^CDCREC||N||||||N",
    //        MSH_NEW
    //            + "PID|1||1234^^^AIRA-TEST^MR||Pecos^Sawyer^K^^^^L|Marion^Valisa^^^^^M|20120725|F||2106-3^White^CDCREC|350 Greene Cir^^Little Lake^MI^49833^USA^P||^PRN^PH^^^906^3464569|||||||||2186-5^not Hispanic or Latino^CDCREC||N||||||N");

  }

  public void test(String om, String fm) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(om);
    testCaseMessage.appendCustomTransformation(" run procedure ANONYMIZE_AND_UPDATE_RECORD");
    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    assertEquals(fm, testCaseMessage.getMessageText());
  }

  private static final String messageWithNoEmailNoPhone =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|20120701082240-0500||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r"
          + "PID|1|||||||||||||||||||||\r";


  private static final String messageWithEmail =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|20120701082240-0500||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r"
          + "PID|1||||||||||||^NET^Internet^LEEVHADCTWOIZG@YAHOO.COM|||||||||\r";

  private static final String messageWithPhone =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|20120701082240-0500||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r"
          + "PID|1||||||||||||^PRN^PH^^^657^5558563|||||||||\r";

  private static final String messageWithPhoneAndEmail =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|20120701082240-0500||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r"
          + "PID|1||||||||||||^PRN^PH^^^657^5558563~^NET^Internet^LEEVHADCTWOIZG@YAHOO.COM|||||||||\r";

  private static final String messageWithEmailAndPhone =
      "MSH|^~\\&|Test EHR Application|X68||NIST Test Iz Reg|20120701082240-0500||VXU^V04^VXU_V04|NIST-IZ-001.00|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS\r"
          + "PID|1||||||||||||^NET^Internet^LEEVHADCTWOIZG@YAHOO.COM~^PRN^PH^^^657^5558563|||||||||\r";

  // 

  @Test
  public void testEmail() {
    AnonymizeAndUpdateRecord.setAsOfDate("20161109");
    testContains(messageWithEmail, "@madeupemailaddress.com");
    testContains(messageWithPhone, "^PRN^PH^^^");
    testNotContains(messageWithPhone, "^PRN^PH^^^657^5558563");
    testNotContains(messageWithNoEmailNoPhone, "^PRN^PH");
    testNotContains(messageWithNoEmailNoPhone, "@madeupemailaddress.com");
    testContains(messageWithPhoneAndEmail, "@madeupemailaddress.com");
    testContains(messageWithEmailAndPhone, "@madeupemailaddress.com");
    testNotContains(messageWithPhoneAndEmail, "^PRN^PH^^^657^5558563");
    testNotContains(messageWithEmailAndPhone, "^PRN^PH^^^657^5558563");
  }

  private void testContains(String om, String co) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(om);
    testCaseMessage.appendCustomTransformation(" run procedure ANONYMIZE_AND_UPDATE_RECORD");
    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    assertTrue(testCaseMessage.getMessageText().contains(co));
  }

  private void testNotContains(String om, String co) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(om);
    testCaseMessage.appendCustomTransformation(" run procedure ANONYMIZE_AND_UPDATE_RECORD");
    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    assertFalse(testCaseMessage.getMessageText().contains(co));
  }

}
