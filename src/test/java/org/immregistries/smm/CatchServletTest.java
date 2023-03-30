package org.immregistries.smm;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.connectors.ConnectorFactory;

public class CatchServletTest {


  private static final String END_POINT = "http://localhost/tester/CatchServlet";
  // private static final String END_POINT = "https://florence.immregistries.org/iis-sandbox/soap";

  private static final String MESSAGE =
      "MSH|^~\\&|||||20230330070559-0600||VXU^V04^VXU_V04|O64U48.2mK|P|2.5.1|||ER|AL|||||Z22^CDCPHINVS|\r"
          + "PID|1||O64U48^^^AIRA-TEST^MR||CowleyAIRA^RosanneAIRA^Zora^^^^L|RoachAIRA^HuonAIRA^^^^^M|20190408|F||2076-8^Native Hawaiian or Other Pacific Islander^CDCREC|1979 Afdrukken Ave^^Battle Creek^MI^49037^USA^P||^PRN^PH^^^269^7407010|||||||||2135-2^Hispanic or Latino^CDCREC|\r"
          + "PD1|||||||||||02^Reminder/Recall - any method^HL70215|||||A|20230330|20230330|\r"
          + "NK1|1|HuntingdonAIRA^HuonAIRA^^^^^L|MTH^Mother^HL70063|1979 Afdrukken Ave^^Battle Creek^MI^49037^USA^P|^PRN^PH^^^269^7407010|\r"
          + "ORC|RE||O64U48.3^AIRA|||||||I-23432^Burden^Donna^A^^^^^NIST-AA-1^^^^PRN||57422^RADON^NICHOLAS^^^^^^NIST-AA-1^L^^^PRN|\r"
          + "RXA|0|1|20230330||03^MMR<^CVX|0.5|mL^milliliters^UCUM||00^Administered^NIP001||||||U1747GW||MSD^Merck and Co^MVX|||CP|A|\r"
          + "RXR|C38299^^NCIT|LA^^HL70163|\r"
          + "OBX|1|CE|64994-7^Vaccine funding program eligibility category^LN|1|V03^VFC eligible - Uninsured^HL70064||||||F|||20230330|||VXC40^Eligibility captured at the immunization level^CDCPHINVS|\r"
          + "OBX|2|CE|30956-7^Vaccine Type^LN|2|03^MMR^CVX||||||F|\r"
          + "OBX|3|TS|29768-9^Date vaccine information statement published^LN|2|20120420||||||F|\r"
          + "OBX|4|TS|29769-7^Date vaccine information statement presented^LN|2|20230330||||||F|\r";

  public static void main(String[] args) throws Exception {

    String type = "SOAP";
    String label = "SOAP";
    String url = END_POINT;
    String message = MESSAGE;
    String userid = "SMMTester";
    String password = "password1234";
    String facilityid = "SMMTester-" + (new SimpleDateFormat("yyyyMMdd")).format(new Date());
    boolean debug = false;
    Connector connector = ConnectorFactory.getConnector(type, label, url);
    connector.setUserid(userid);
    connector.setPassword(password);
    connector.setFacilityid(facilityid);
    String response = connector.submitMessage(message, debug);
    System.out.println(response);
  }
}
