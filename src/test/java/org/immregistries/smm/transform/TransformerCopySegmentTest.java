package org.immregistries.smm.transform;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;
import junit.framework.TestCase;

public class TransformerCopySegmentTest extends TestCase {


  @Test
  public void testCopySegment() {
    testTransform("NK1=|Hello", TEST_ORIGINAL, TEST1_EXPECTED);
    testTransform("NK1=|Hello|Goodbye", TEST_ORIGINAL, TEST2_EXPECTED);
    testTransform("NK1=[PID]", TEST_ORIGINAL, TEST3_EXPECTED);
    testTransform("OBX#1=|Hello", TEST_ORIGINAL, TEST4_EXPECTED);
    testTransform("OBX#2=|Hello", TEST_ORIGINAL, TEST5_EXPECTED);
    testTransform("OBX#2=[PID]", TEST_ORIGINAL, TEST6_EXPECTED);
    testTransform("OBX#2=[OBX#1]", TEST_ORIGINAL, TEST7_EXPECTED);
    {
      Map<String, TestCaseMessage> testCaseMessageMap = new HashMap<String, TestCaseMessage>();
      TestCaseMessage tcm = new TestCaseMessage();
      tcm.setMessageText(TEST8_EXPECTED);
      testCaseMessageMap.put("COPY", tcm);
      testTransform("PID=[COPY::PID]", TEST8_ORIGINAL, TEST8_EXPECTED, testCaseMessageMap);
    }
  }
  
  private static final String TEST_ORIGINAL =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r" + "PV1|1|R|\r" + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String TEST1_EXPECTED =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "NK1|Hello\r" + "PV1|1|R|\r" + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String TEST2_EXPECTED =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "NK1|Hello|Goodbye\r" + "PV1|1|R|\r" + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String TEST3_EXPECTED =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "NK1|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "PV1|1|R|\r" + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String TEST4_EXPECTED =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r" + "PV1|1|R|\r" + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|Hello\r" + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String TEST5_EXPECTED =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r" + "PV1|1|R|\r" + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|Hello\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String TEST6_EXPECTED =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r" + "PV1|1|R|\r" + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String TEST7_EXPECTED =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r" + "PV1|1|R|\r" + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";


  private static final String TEST8_ORIGINAL =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID||||||||||||||||||||||||\r" + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r"
          + "PV1|1|R|\r" + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String TEST8_EXPECTED =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r" + "PV1|1|R|\r" + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private void testTransform(String transform, String originalMessage, String finalMessage) {
    Transformer transformer = new Transformer();
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(originalMessage);
    testCaseMessage.setCustomTransformations(transform);
    transformer.transform(testCaseMessage);
    assertEquals(finalMessage, testCaseMessage.getMessageText());
  }

  private void testTransform(String transform, String originalMessage, String finalMessage,
      Map<String, TestCaseMessage> testCaseMessageMap) {
    Transformer transformer = new Transformer();
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(originalMessage);
    testCaseMessage.setCustomTransformations(transform);
    testCaseMessage.registerTestCaseMap(testCaseMessageMap);
    transformer.transform(testCaseMessage);
    assertEquals(finalMessage, testCaseMessage.getMessageText());
  }

}
