package org.immregistries.smm.transform;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.Test;
import junit.framework.TestCase;

public class TransformerTest extends TestCase {
  @Test
  public void testCreateTokenList() {
    assertEquals(0, Transformer.createTokenList("").size());
    assertEquals(1, Transformer.createTokenList("1").size());
    assertEquals(2, Transformer.createTokenList("hello goodbye").size());
    assertEquals(2, Transformer.createTokenList("hello  goodbye").size());
    assertEquals(3, Transformer.createTokenList("hello  good bye").size());
    assertEquals("hello", Transformer.createTokenList("hello  goodbye").removeFirst());
    assertEquals("hello", Transformer.createTokenList("'hello'  goodbye").removeFirst());
    assertEquals("hello goodbye", Transformer.createTokenList("'hello goodbye'").removeFirst());
    assertEquals("I'm", Transformer.createTokenList("I'm great").removeFirst());
    assertEquals("I'm great", Transformer.createTokenList("'I''m great'").removeFirst());

  }

  @Test
  public void testReadValue() {
    TransformRequest transformRequest = new TransformRequest(TEST1_ORIGINAL);
    assertEquals("Burt", Transformer.readField(TEST1_ORIGINAL, "PID-5.1", transformRequest));
    assertEquals("Burt", Transformer.readField(TEST1_ORIGINAL, "PID-5", transformRequest));
    assertEquals("20150303154321-0500",
        Transformer.readField(TEST1_ORIGINAL, "MSH-7", transformRequest));
    assertEquals("", Transformer.readField(TEST1_ORIGINAL, "PID-100", transformRequest));
    assertEquals("", Transformer.readField(TEST1_ORIGINAL, "ZZZ-1", transformRequest));
  }

  private static final String TEST1_ORIGINAL =
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

  private static final String TEST1_FINALA =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Le^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String TEST1_FINALB =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt&Le^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String TEST2_ORIGINAL =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt&Le^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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
  private static final String TEST2_FINAL =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt&Le^Callis&Burt^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r" + "PV1|1|R||^&Le|\r"
          + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  public void testUpdateSubField() {
    testTransform("PID-5.1=Le\n", TEST1_ORIGINAL, TEST1_FINALA);
    testTransform("PID-5.1.2=Le\n", TEST1_ORIGINAL, TEST1_FINALB);
    testTransform("PV1-4.2.2=[PID-5.1.2]\nPID-5.2.2=[PID-5.1]\n", TEST2_ORIGINAL, TEST2_FINAL);
  }

  private static final String CLEAR1_ORIGINAL =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L~Burt^Callis^G^^^^L~Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String CLEAR1_FINALA1 =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r" + "PID|\r"
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

  private static final String CLEAR1_FINALA2 =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L~Burt^Callis^G^^^^L~Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
          + "NK1|1|Copeland^Lona|MTH^Mother^HL70063|\r" + "PV1|1|R|\r" + "ORC|RE||G98P76.1^OIS|\r"
          + "RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.2^OIS|\r"
          + "RXA|0|1|20130224||62^HPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||G98P76.3^OIS|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150303||83^Hep A^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||G2789NM||MSD^Merck and Co^MVX||||A|\r"
          + "OBX|1||64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20150303|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2||30956-7^Vaccine Type^LN|2|85^Hepatitis A^CVX||||||F|\r"
          + "OBX|3||29768-9^Date vaccine information statement published^LN|2|20111025||||||F|\r"
          + "OBX|4||29769-7^Date vaccine information statement presented^LN|2|20150303||||||F|\r";

  private static final String CLEAR1_FINALB =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR|||Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String CLEAR1_FINALC1 =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||~Burt^Callis^G^^^^L~Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String CLEAR1_FINALC2 =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L~~Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String CLEAR1_FINALD =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^^G^^^^L~Burt^Callis^G^^^^L~Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  public void testClearField() {
    testTransform("clear PID\n", CLEAR1_ORIGINAL, CLEAR1_FINALA1);
    testTransform("clear OBX-2*\n", CLEAR1_ORIGINAL, CLEAR1_FINALA2);
    testTransform("clear PID-5\n", CLEAR1_ORIGINAL, CLEAR1_FINALB);
    testTransform("clear PID-5#1\n", CLEAR1_ORIGINAL, CLEAR1_FINALC1);
    testTransform("clear PID-5#2\n", CLEAR1_ORIGINAL, CLEAR1_FINALC2);
    testTransform("clear PID-5.2\n", CLEAR1_ORIGINAL, CLEAR1_FINALD);
  }

  private static final String INSERT_SEGMENT_TEST1_ORIG =
      "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
          + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
          + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
          + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
          + "PV1|1|R|\r"
          + "ORC|RE||N90X5.1^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CNS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20120726|20120726|21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001|0461325664^Banner^Kyra^Asa^^^^^CMS^L^^^NPI|||||N3783EO|20160730|MSD^Merck and Co^MVX|||CP|A|20150730072832-0600|\r"
          + "RXR|SC^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120726||||||F|\r"
          + "ORC|RE||N90X5.2^AIRA|\r"
          + "RXA|0|1|20130224|20130224|141^Influenza^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
          + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
          + "RXR|IM^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST1_FINAL =
      "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
          + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
          + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
          + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
          + "PV1|1|R|\r"
          + "ORC|RE||N90X5.1^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CNS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20120726|20120726|21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001|0461325664^Banner^Kyra^Asa^^^^^CMS^L^^^NPI|||||N3783EO|20160730|MSD^Merck and Co^MVX|||CP|A|20150730072832-0600|\r"
          + "RXR|SC^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120726||||||F|\r"
          + "ORC|RE||N90X5.2^AIRA|\r"
          + "RXA|0|1|20130224|20130224|141^Influenza^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
          + "RXR|\r"
          + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
          + "RXR|IM^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST2_ORIG =
      "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
          + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
          + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
          + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
          + "PV1|1|R|\r"
          + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
          + "RXR|IM^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST2_FINAL =
      "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
          + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
          + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
          + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
          + "PV1|1|R|\r" + "ZZZ|\r"
          + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
          + "RXR|IM^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST3_ORIG =
      "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
          + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
          + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
          + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
          + "PV1|1|R|\r"
          + "ORC|RE||N90X5.1^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CNS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20120726|20120726|21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001|0461325664^Banner^Kyra^Asa^^^^^CMS^L^^^NPI|||||N3783EO|20160730|MSD^Merck and Co^MVX|||CP|A|20150730072832-0600|\r"
          + "RXR|SC^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120726||||||F|\r"
          + "ORC|RE||N90X5.2^AIRA|\r"
          + "RXA|0|1|20130224|20130224|141^Influenza^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
          + "OBX|\r"
          + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
          + "RXR|IM^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";


  private static final String INSERT_SEGMENT_TEST3_FINAL =
      "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
          + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
          + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
          + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
          + "PV1|1|R|\r"
          + "ORC|RE||N90X5.1^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CNS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20120726|20120726|21^Varicella^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001|0461325664^Banner^Kyra^Asa^^^^^CMS^L^^^NPI|||||N3783EO|20160730|MSD^Merck and Co^MVX|||CP|A|20150730072832-0600|\r"
          + "RXR|SC^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|21^Varicella^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20080313||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20120726||||||F|\r"
          + "ORC|RE||N90X5.2^AIRA|\r"
          + "RXA|0|1|20130224|20130224|141^Influenza^CVX|999|||01^Historical^NIP001|||||||||||CP|A|\r"
          + "OBX|\r" + "NTE|\r"
          + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
          + "RXR|IM^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST4_ORIG =
      "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
          + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
          + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
          + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
          + "PV1|1|R|\r"
          + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
          + "RXR|IM^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";

  private static final String INSERT_SEGMENT_TEST4_FINAL =
      "MSH|^~\\&|||||20150730072832-0600||VXU^V04^VXU_V04|N90X5.2629|P|2.5.1|||NE|AL||||||Delaware Pediatrics|\r"
          + "PID|1||N90X5^^^AIRA-TEST^MR||Rockcastle^Trethowan^Lionel^^^^L|Rockcastle^Platona|20110724|M||2106-3^White^HL70005|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P||^PRN^PH^^^248^3300510~^NET^^platona.garfield@madeupemailaddress.com|^WPN^PH^^^248^3300510|spa^Spanish^HL70296|||||||2135-2^Hispanic or Latino^CDCREC||N|1|||||N|\r"
          + "PD1|||||||||||02^Reminder/recall - any method^HL70215|N|20150730|||A|20150730|20150730|\r"
          + "NK1|1|Dickinson^Platona^Darby^^^^L|MTH^Mother^HL70063|42 Dickinson Ln^APT #307^Farmington Hills^MI^48333^USA^P|^PRN^PH^^^248^3300510|^WPN^PH^^^248^3300510|\r"
          + "PV1|1|R|\r" + "IN1|\r" + "IN2|\r"
          + "ORC|RE||N90X5.3^AIRA|||||||5911348664^Gallatin^Payten^Tressa^^^^^CMS^L^^^NPI||6666367619^Irwin^Turner^Eoin^^^^^CMS^L^^^NPI|||||^Delaware Pediatrics - Ross^HL70362|\r"
          + "RXA|0|1|20150730||140^Influenza^CVX|0.25|mL^milliliters^UCUM||00^Administered^NIP001||||||K4394LY|20160730|NOV^Novartis^MVX|||CP|A|\r"
          + "RXR|IM^^HL70162|RA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150730|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|140^Inactivated Flu^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120702||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20150730||||||F|\r";


  public void testInsertSegment() {
    testTransform("insert segment PID if missing\n", TEST1_ORIGINAL, TEST1_ORIGINAL);
    testTransform("insert segment RXA if missing\n", TEST1_ORIGINAL, TEST1_ORIGINAL);
    testTransform("insert segment RXA#2 if missing\n", TEST1_ORIGINAL, TEST1_ORIGINAL);
    testTransform("insert segment OBX after RXA#3 if missing\n", TEST1_ORIGINAL, TEST1_ORIGINAL);
    testTransform("insert segment RXR after RXA#2\n", INSERT_SEGMENT_TEST1_ORIG,
        INSERT_SEGMENT_TEST1_FINAL);
    testTransform("insert segment RXR after RXA#2 if missing\n", INSERT_SEGMENT_TEST1_ORIG,
        INSERT_SEGMENT_TEST1_FINAL);
    testTransform("insert segment ZZZ before ORC\n", INSERT_SEGMENT_TEST2_ORIG,
        INSERT_SEGMENT_TEST2_FINAL);
    testTransform("insert segment ZZZ before ORC if missing\n", INSERT_SEGMENT_TEST2_ORIG,
        INSERT_SEGMENT_TEST2_FINAL);
    testTransform("insert segment ZZZ before ORC if missing\n", INSERT_SEGMENT_TEST2_FINAL,
        INSERT_SEGMENT_TEST2_FINAL);
    testTransform("insert segment ZZZ before ORC if missing\n", INSERT_SEGMENT_TEST2_FINAL,
        INSERT_SEGMENT_TEST2_FINAL);
    testTransform("insert segment NTE after RXA#2:OBX if missing\n", INSERT_SEGMENT_TEST3_ORIG,
        INSERT_SEGMENT_TEST3_FINAL);
    testTransform("insert segment IN1,IN2 before ORC if missing\n", INSERT_SEGMENT_TEST4_ORIG,
        INSERT_SEGMENT_TEST4_FINAL);
    testTransform("insert segment IN1,IN2 before ORC if missing\n", INSERT_SEGMENT_TEST4_FINAL,
        INSERT_SEGMENT_TEST4_FINAL);
  }

  private TestCaseMessage testTransform(String transform, String originalMessage,
      String finalMessage) {
    return testTransform(transform, originalMessage, finalMessage, null);
  }

  private TestCaseMessage testTransform(String transform, String originalMessage,
      String finalMessage, TestCaseMessage testCaseMessage) {
    Transformer transformer = new Transformer();

    if (testCaseMessage == null) {
      testCaseMessage = new TestCaseMessage();
    }

    testCaseMessage.setOriginalMessage(originalMessage);
    testCaseMessage.setCustomTransformations(transform);
    transformer.transform(testCaseMessage);
    assertEquals(finalMessage, testCaseMessage.getMessageText());

    return testCaseMessage;
  }

  @Test
  public void testMakeBase62Number() {
    assertEquals("0", Transformer.makeBase62Number(0));
    assertEquals("1", Transformer.makeBase62Number(1));
    assertEquals("2", Transformer.makeBase62Number(2));
    assertEquals("9", Transformer.makeBase62Number(9));
    assertEquals("a", Transformer.makeBase62Number(10));
    assertEquals("z", Transformer.makeBase62Number(35));
    assertEquals("A", Transformer.makeBase62Number(36));
    assertEquals("10", Transformer.makeBase62Number(62));
    assertEquals("20", Transformer.makeBase62Number(62 * 2));
    assertEquals("2a", Transformer.makeBase62Number(62 * 2 + 10));
    //System.out.println("'6449390' = '" + Transformer.makeBase62Number(6449390) + "'");
  }

  private static final String TP_01_FINAL =
      "MSH|^~\\&||AIRA|GRITS|GRITS|20150817082226-0600||VXU^V04^VXU_V04|J94H2.1D6|P|2.5.1|\r"
          + "PID|1||J94H2^^^AIRA-TEST^MR||Butte^Rahman^Jaiden^^^^L|Marion^Donla|20150220|M|||371 Franklin Cir^^Alma^MI^48802^USA^P||^PRN^PH^^^989^4479902|\r"
          + "NK1|1|Butte^Donla|MTH^Mother^HL70063|\r" + "ORC|RE||J94H2.1^AIRA|\r"
          + "RXA|0|1|20150421||10^IPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150817|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "ORC|RE||J94H2.2^AIRA|\r"
          + "RXA|0|1|20150621||10^IPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150817|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "ORC|RE||J94H2.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150817||20^DTaP^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||^^^25966||||G3592YS||PMC^sanofi pasteur^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150817|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r";

  private static final String TP_01_START =
      "MSH|^~\\&||AIRA|GRITS|GRITS|20150817082226-0600||VXU^V04^VXU_V04|J94H2.1D6|P|2.5.1|\r"
          + "PID|1||J94H2^^^AIRA-TEST^MR||Butte^Rahman^Jaiden^^^^L|Marion^Donla|20150220|M|||371 Franklin Cir^^Alma^MI^48802^USA^P||^PRN^PH^^^989^4479902|\r"
          + "NK1|1|Butte^Donla|MTH^Mother^HL70063|\r" + "ORC|RE||J94H2.1^AIRA|\r"
          + "RXA|0|1|20150421||10^IPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||J94H2.2^AIRA|\r"
          + "RXA|0|1|20150621||10^IPV^CVX|999|||01^Historical^NIP001||||||||||||A|\r"
          + "ORC|RE||J94H2.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L|\r"
          + "RXA|0|1|20150817||20^DTaP^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||^^^25966||||G3592YS||PMC^sanofi pasteur^MVX||||A|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V01^Not VFC eligible^HL70064||||||F|||20150817|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r";

  @Test
  public void testProcedureAddFundingEligibilityToAllRxa() {
    testTransform("run procedure ADD_FUNDING_ELGIBILITY_TO_ALL_RXA\n", TP_01_START, TP_01_FINAL);
  }

  @Test
  public void testDoSetField() {
    testTransform("PID-5.1=Greg", TEST1_ORIGINAL, CONCATENATE_FINAL_1);
    testTransform("PID-5.1+=Greg", TEST1_ORIGINAL, CONCATENATE_FINAL_2);

    testTransform("PID-5.1=[PID-5.2]", TEST1_ORIGINAL, CONCATENATE_FINAL_3);
    testTransform("PID-5.1+=[PID-5.2]", TEST1_ORIGINAL, CONCATENATE_FINAL_4);

    testTransform("PID-11.5=[PID-13.7]", TEST1_ORIGINAL, CONCATENATE_FINAL_5);
    testTransform("PID-11.5+=[PID-13.7]", TEST1_ORIGINAL, CONCATENATE_FINAL_6);
  }

  private static final String CONCATENATE_FINAL_1 =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Greg^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String CONCATENATE_FINAL_2 =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||BurtGreg^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String CONCATENATE_FINAL_3 =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Callis^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String CONCATENATE_FINAL_4 =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||BurtCallis^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String CONCATENATE_FINAL_5 =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^5679656^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String CONCATENATE_FINAL_6 =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^493095679656^USA^P||^PRN^PH^^^231^5679656|\r"
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

  @Test
  public void testDoSetVariable() {
    TestCaseMessage tcm = testTransform("SET FIRST_NAME=Greg", VAR1_ORIGINAL, VAR1_ORIGINAL);

    Map<String, String> variables = tcm.getVariables();
    tcm = new TestCaseMessage();
    tcm.getVariables().putAll(variables);
    tcm = testTransform("PID-5.2=[FIRST_NAME]", VAR1_ORIGINAL, VAR1_FINAL, tcm);

    variables = tcm.getVariables();
    tcm = new TestCaseMessage();
    tcm.getVariables().putAll(variables);
    tcm = testTransform("PID-5.1=[FIRST_NAME]", VAR1_FINAL, VAR2_FINAL, tcm);
  }

  @Test
  public void testGetMessageText() throws IOException {
    Transform t = new Transform();
    TransformRequest tr = new TransformRequest(VAR1_ORIGINAL);

    t.testCaseId = "GM-1.1-?";

    tr.setTestCaseMessageMap(new HashMap<String, TestCaseMessage>());

    TestCaseMessage tcm1 = new TestCaseMessage();
    tcm1.setMessageText(VAR1_FINAL);
    tcm1.setTestCaseNumber("GM-1.1-3");
    tr.getTestCaseMessageMap().put("GM-1.1-3", tcm1);

    TestCaseMessage tcm2 = new TestCaseMessage();
    tcm2.setMessageText(VAR1_ORIGINAL);
    tcm2.setTestCaseNumber("GM-1.2-3");
    tr.setCurrentTestCaseMessage(tcm2);
    tr.getTestCaseMessageMap().put("GM-1.2-3", tcm2);

    TestCaseMessage tcm3 = new TestCaseMessage();
    tcm3.setMessageText(VAR2_FINAL);
    tcm3.setTestCaseNumber("GM-1.1");
    tr.getTestCaseMessageMap().put("GM-1.1", tcm3);

    TestCaseMessage tcm4 = new TestCaseMessage();
    tcm4.setMessageText(VAR1_ORIGINAL);
    tcm4.setTestCaseNumber("GM-1.2-4");
    tr.getTestCaseMessageMap().put("GM-1.2-4", tcm4);

    TestCaseMessage tcm5 = new TestCaseMessage();
    tcm5.setMessageText(VAR1_ORIGINAL);
    tcm5.setTestCaseNumber("GM-1.2");
    tr.getTestCaseMessageMap().put("GM-1.2", tcm5);

    // happy path
    BufferedReader br = Transformer.getMessageText(null, t, tr);
    String result = br.lines().collect(Collectors.joining("\r")) + "\r";
    assertEquals(VAR1_FINAL, result);

    // no current test case
    tr.setCurrentTestCaseMessage(null);
    br = Transformer.getMessageText(null, t, tr);
    result = br.lines().collect(Collectors.joining("\r")) + "\r";
    assertEquals(VAR2_FINAL, result);

    // no corresponding -x test case
    tr.setCurrentTestCaseMessage(tcm4);
    br = Transformer.getMessageText(null, t, tr);
    result = br.lines().collect(Collectors.joining("\r")) + "\r";
    assertEquals(VAR2_FINAL, result);

    // current test case is not a repeat itself
    tr.setCurrentTestCaseMessage(tcm5);
    br = Transformer.getMessageText(null, t, tr);
    result = br.lines().collect(Collectors.joining("\r")) + "\r";
    assertEquals(VAR2_FINAL, result);
  }

  private static final String VAR1_ORIGINAL =
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


  private static final String VAR1_FINAL =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt^Greg^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String VAR2_FINAL =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Greg^Greg^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  @Test
  public void testSpace() {
    TransformRequest transformRequest = new TransformRequest(TEST1_ORIGINAL);
    assertEquals("Burt", Transformer.readField(TEST1_ORIGINAL, "PID-5.1", transformRequest));
    testTransform("PID-5.1+=[SPACE]\n", TEST1_ORIGINAL, SPACE1_FINAL);
    testTransform("PID-5.1+=[SPACE]\n", SPACE1_FINAL, SPACE2_FINAL);
    testTransform("PID-5.1+=End\n", SPACE2_FINAL, SPACE3_FINAL);
  }

  private static final String SPACE1_FINAL =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt ^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String SPACE2_FINAL =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt  ^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  private static final String SPACE3_FINAL =
      "MSH|^~\\&|||||20150303154321-0500||VXU^V04^VXU_V04|G98P76.1245|P|2.5.1|\r"
          + "PID|1||G98P76^^^OIS-TEST^MR||Burt  End^Callis^G^^^^L|Copeland^Lona|20020222|M|||374 Refugio Ln^^Woodland Park^MI^49309^USA^P||^PRN^PH^^^231^5679656|\r"
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

  @Test
  public void testIsTestCaseIdRepeat() {
    assertTrue(Transformer.isTestCaseIdRepeat("GM-1.1-1"));
    assertTrue(Transformer.isTestCaseIdRepeat("GM-1.1-2"));
    assertTrue(Transformer.isTestCaseIdRepeat("GM-1.1-3"));
    assertTrue(Transformer.isTestCaseIdRepeat("GM-1.2-1"));
    assertTrue(Transformer.isTestCaseIdRepeat("GM-1.2-2"));
    assertTrue(Transformer.isTestCaseIdRepeat("GM-1.2-3"));
    assertTrue(Transformer.isTestCaseIdRepeat("GM-2.1-1"));
    assertTrue(Transformer.isTestCaseIdRepeat("GM-2.1-2"));
    assertTrue(Transformer.isTestCaseIdRepeat("GM-2.1-3"));

    assertFalse(Transformer.isTestCaseIdRepeat("GM-1.1"));
    assertFalse(Transformer.isTestCaseIdRepeat("GM-1.2"));
    assertFalse(Transformer.isTestCaseIdRepeat("GM-1.x"));
    assertFalse(Transformer.isTestCaseIdRepeat("GM-x.1"));
    assertFalse(Transformer.isTestCaseIdRepeat("GM-"));
    assertFalse(Transformer.isTestCaseIdRepeat("GM-x"));
    assertFalse(Transformer.isTestCaseIdRepeat("GM?"));
    assertFalse(Transformer.isTestCaseIdRepeat("GM?-"));
    assertFalse(Transformer.isTestCaseIdRepeat("GM-?"));
    assertFalse(Transformer.isTestCaseIdRepeat("GM-1.1-x"));
  }

  @Test
  public void testDoVariableReplacement() throws IOException {
    Transform t = new Transform();

    TransformRequest tr = new TransformRequest(VAR1_ORIGINAL);

    // no variable to match against, print variable name
    t.value = "[FIRST_NAME]";
    new Transformer().doVariableReplacement(t, tr);
    assertEquals("[FIRST_NAME]", t.value);

    // add current test case message
    // no variable to match against, print variable name
    t.value = "[FIRST_NAME]";
    TestCaseMessage current = new TestCaseMessage();
    current.setTestCaseNumber("TM-1.1");
    tr.setCurrentTestCaseMessage(current);
    new Transformer().doVariableReplacement(t, tr);
    assertEquals("[FIRST_NAME]", t.value);

    // update current test case message with variable
    // works for checking variable name against itself
    t.value = "[FIRST_NAME]";
    current.getVariables().put("FIRST_NAME", "[PID-5.2]");
    new Transformer().doVariableReplacement(t, tr);
    assertEquals("Callis", t.value);

    // reference another test case
    // no variable to match against, print variable name
    t.value = "[GM-1.1::FIRST_NAME]";
    current.getVariables().put("FIRST_NAME", "[PID-5.2]");
    new Transformer().doVariableReplacement(t, tr);
    assertEquals("[GM-1.1::FIRST_NAME]", t.value);

    // load GM-1.1 into map but no variable
    // no variable to match against, print variable name
    t.value = "[GM-1.1::FIRST_NAME]";
    TestCaseMessage gm11 = new TestCaseMessage();
    gm11.setTestCaseNumber("GM-1.1");
    tr.setTestCaseMessageMap(new HashMap<String, TestCaseMessage>());
    tr.getTestCaseMessageMap().put("GM-1.1", gm11);
    new Transformer().doVariableReplacement(t, tr);
    assertEquals("[GM-1.1::FIRST_NAME]", t.value);

    // set variable on GM-1.1 so it works
    t.value = "[GM-1.1::FIRST_NAME]";
    gm11.getVariables().put("FIRST_NAME", "[PID-5.1]");
    new Transformer().doVariableReplacement(t, tr);
    assertEquals("Burt", t.value);

    // reference a repeatable test case
    // this will work because since we're not in a repeat (TM-1.1), it will just ref GM-1.1
    t.value = "[GM-1.1-?::FIRST_NAME]";
    gm11.getVariables().put("FIRST_NAME", "[PID-6.1]");
    new Transformer().doVariableReplacement(t, tr);
    assertEquals("Copeland", t.value);

    // this will also just work even though we're in a repeat test because there is no GM-1.1-2 ref
    t.value = "[GM-1.1-?::FIRST_NAME]";
    current.setTestCaseNumber("TM-1.1-2");
    new Transformer().doVariableReplacement(t, tr);
    assertEquals("Copeland", t.value);

    // finally referencing the corresponding repeat reference test case that exists
    t.value = "[GM-1.1-?::FIRST_NAME]";
    TestCaseMessage gm112 = new TestCaseMessage();
    gm112.setTestCaseNumber("GM-1.1-2");
    tr.getTestCaseMessageMap().put("GM-1.1-2", gm112);
    gm112.getVariables().put("FIRST_NAME", "[PID-6.2]");
    new Transformer().doVariableReplacement(t, tr);
    assertEquals("Lona", t.value);
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_noModifiers() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_admin_rxa9empty() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if administered");
    
    assertTrue(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform if administered", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_admin_rxa9not00() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if administered");
    
    assertTrue(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform if administered", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_admin_rxa9is00() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||00^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if administered");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_historical_rxa9empty() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if historical");
    
    assertTrue(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform if historical", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_historical_rxa9bad() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||00^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if historical");
    
    assertTrue(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform if historical", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_historical_rxa9is01() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if historical");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_historical_rxa9is02() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||02^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if historical");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_historical_rxa9is03() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||03^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if historical");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_historical_rxa9is04() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||04^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if historical");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_historical_rxa9is05() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||05^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if historical");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_historical_rxa9is06() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||06^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if historical");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_historical_rxa9is07() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||07^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if historical");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_historical_rxa9is08() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||08^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if historical");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_historical_rxa9is09() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||09^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if historical");
    
    assertTrue(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform if historical", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_refusal_rxa9notEmpty() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if refusal");
    
    assertTrue(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform if refusal", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_refusal_rxa9empty_rxa20empty() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||^Historical^NIP001||||||||||||A|\\r\"");
    tr.setLine("my transform if refusal");
    
    assertTrue(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform if refusal", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_refusal_rxa9empty_rxa20bad() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||^Historical^NIP001|||||||||||bad|A|\\r\"");
    tr.setLine("my transform if refusal");
    
    assertTrue(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform if refusal", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_refusal_rxa9empty_rxa20isRE() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||^Historical^NIP001|||||||||||RE|A|\\r\"");
    tr.setLine("my transform if refusal");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_nonAdmin_rxa9notEmpty() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||01^Historical^NIP001|||||||||||20|A|\\r\"");
    tr.setLine("my transform if non-admin");
    
    assertTrue(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform if non-admin", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_nonAdmin_rxa9empty_rxa20empty() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||^Historical^NIP001||||||||||||A|\\r\"");
    tr.setLine("my transform if non-admin");
    
    assertTrue(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform if non-admin", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_nonAdmin_rxa9empty_rxa20bad() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||^Historical^NIP001|||||||||||bad|A|\\r\"");
    tr.setLine("my transform if non-admin");
    
    assertTrue(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform if non-admin", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_nonAdmin_rxa9empty_rxa20isNA() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||^Historical^NIP001|||||||||||NA|A|\\r\"");
    tr.setLine("my transform if non-admin");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("my transform", tr.getLine());
  }
  
  @Test
  public void test_checkForSpecialVaccineModifierSkip_nearEmpty() throws IOException {
    TransformRequest tr = new TransformRequest("RXA|0|1|20110220||62^HPV^CVX|999|||^Historical^NIP001|||||||||||NA|A|\\r\"");
    tr.setLine(" if non-admin");
    
    assertFalse(new Transformer().checkForSpecialVaccineModifierSkip(tr));
    
    assertEquals("", tr.getLine());
  }
}
