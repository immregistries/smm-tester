package org.immregistries.smm.tester.run;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.immregistries.smm.tester.connectors.SoapConnector;
import org.immregistries.smm.tester.manager.query.QueryConverter;
import org.immregistries.smm.tester.manager.query.QueryConverterQBPZ45;
import org.immregistries.smm.tester.manager.query.QueryType;

/**
 * Command line helper that sends one HL7 message to an IIS over the CDC SOAP web service and
 * prints the response. Optionally turns a VXU file into a query first.
 *
 * <pre>
 * SendHl7 url userid password facilityid messageFile [queryType [conditions]]
 *   queryType: QBP-Z34, QBP-Z44, QBP-Z45 ... (messageFile is then a VXU)
 *   conditions: Z45 contextual conditions, for example 007=Y,170=20260701
 * </pre>
 */
public class SendHl7 {

  public static void main(String[] args) throws Exception {
    if (args.length < 5) {
      System.err.println(
          "usage: SendHl7 url userid password facilityid messageFile [queryType [conditions]]");
      System.exit(2);
    }
    String message = new String(Files.readAllBytes(Paths.get(args[4])), StandardCharsets.UTF_8);
    message = message.replace("\r\n", "\r").replace("\n", "\r");
    if (args.length > 5) {
      QueryType queryType = QueryType.getValue(args[5]);
      if (queryType == null) {
        System.err.println("unknown query type " + args[5]);
        System.exit(2);
      }
      QueryConverter converter = QueryConverter.getQueryConverter(queryType);
      if (converter instanceof QueryConverterQBPZ45 && args.length > 6) {
        ((QueryConverterQBPZ45) converter).setConditions(args[6]);
      }
      message = converter.convert(message);
    }
    SoapConnector connector = new SoapConnector("cli", args[0]);
    connector.setUserid(args[1]);
    connector.setPassword(args[2]);
    connector.setFacilityid(args[3]);
    System.out.println("--- REQUEST ---");
    System.out.println(message.replace("\r", "\n"));
    String response = connector.submitMessage(message, false);
    System.out.println("--- RESPONSE ---");
    System.out.println(response == null ? "" : response.replace("\r\n", "\n").replace("\r", "\n"));
  }
}
