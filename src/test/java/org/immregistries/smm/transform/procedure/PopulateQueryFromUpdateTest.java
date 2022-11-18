package org.immregistries.smm.transform.procedure;

import java.util.HashMap;
import java.util.Map;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

import junit.framework.TestCase;

public class PopulateQueryFromUpdateTest extends TestCase {
  
  

  private static final String UPDATE_MESSAGE = "MSH|^~\\&|||||20221117085729-0700||VXU^V04^VXU_V04|V43H49.2pl|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS|\r" + 
      "PID|1||V43H49^^^AIRA-TEST^MR||EsceibreAIRA^CharlotteAIRA^Mare^^^^L|PerryAIRA^KrisAIRA^^^^^M|20181123|F||2106-3^White^CDCREC|1345 Zulst St^^Saginaw^MI^48605^USA^P||^PRN^PH^^^989^5114370|||||||||2186-5^not Hispanic or Latino^CDCREC|\r" + 
      "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20221117|20221117|\r" + 
      "NK1|1|EsceibreAIRA^KrisAIRA^^^^^L|MTH^Mother^HL70063|1345 Zulst St^^Saginaw^MI^48605^USA^P|^PRN^PH^^^989^5114370|\r" + 
      "ORC|RE||V43H49.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^PRN|\r" + 
      "RXA|0|1|20221117||03^MMR^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||U1747GW||MSD^Merck and Co^MVX|||CP|A|\r" + 
      "RXR|C38299^^NCIT|LA^^HL70163|\r" + 
      "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V02^VFC eligible - Medicaid/Medicaid Managed Care^HL70064||||||F|||20221117|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r" + 
      "OBX|2|CE|30956-7^Vaccine Type^LN|2|03^MMR^CVX||||||F|\r" + 
      "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120420||||||F|\r" + 
      "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20221117||||||F|\r";

  private static final String QUERY_MESSAGE_ORIGINAL = "MSH|^~\\&|||||20221117085830-0700||QBP^Q11^QBP_Q11|1668700713602.3|P|2.5.1|||ER|AL|||||Z44^CDCPHINVS|||\r" + 
      "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|1668700713602.3|M65D50^^^AIRA-TEST^MR|MonroeAIRA^ZadorAIRA^Hannu^^^^L|ClarkAIRA^AdaliaAIRA^^^^^M|20181124|M|1582 Tpe St^^Harrisville^MI^48740^USA^P|^PRN^PH^^^989^4668008|||||\r" + 
      "RCP|I|1^RD&Records&HL70126\r";

  private static final String QUERY_MESSAGE_FINAL = "MSH|^~\\&|||||20221117085830-0700||QBP^Q11^QBP_Q11|1668700713602.3|P|2.5.1|||ER|AL|||||Z44^CDCPHINVS|||\r" + 
      "QPD|Z44^Request Evaluated History and Forecast^CDCPHINVS|1668700713602.3|V43H49^^^AIRA-TEST^MR|EsceibreAIRA^CharlotteAIRA^Mare^^^^L|PerryAIRA^KrisAIRA^^^^^M|20181123|F|1345 Zulst St^^Saginaw^MI^48605^USA^P|^PRN^PH^^^989^5114370|||||\r" + 
      "RCP|I|1^RD&Records&HL70126\r";


  @Test
  public void test() {
    test(UPDATE_MESSAGE, QUERY_MESSAGE_ORIGINAL,  QUERY_MESSAGE_FINAL);
  }

  public void test(String update, String queryOriginal, String queryFinal) {
    TestCaseMessage testCaseMessage = new TestCaseMessage();
    testCaseMessage.setOriginalMessage(queryOriginal);
    testCaseMessage.appendCustomTransformation(" run procedure POPULATE_QUERY_FROM_UPDATE query");
    {
      Map<String, TestCaseMessage> testCaseMessageMap = new HashMap<String, TestCaseMessage>();
      TestCaseMessage tcm = new TestCaseMessage();
      tcm.setMessageText(update);
      testCaseMessageMap.put("query", tcm);
      testCaseMessage.registerTestCaseMap(testCaseMessageMap);
    }
    Transformer transformer = new Transformer();
    transformer.transform(testCaseMessage);
    assertEquals(queryFinal, testCaseMessage.getMessageText());
  }
}
