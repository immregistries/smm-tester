package org.immregistries.smm.tester.connectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.sun.net.httpserver.HttpServer;

public class SoapConnectorTest {

  private static final String SOAP_RESPONSE_START =
      "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\"><soap:Body>";
  private static final String SOAP_RESPONSE_END = "</soap:Body></soap:Envelope>";

  private HttpServer server;
  private String url;
  private String requestContentType;
  private String requestBody;
  private int responseStatus;
  private String responseBody;

  @Before
  public void before() throws IOException {
    server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext("/soap", exchange -> {
      requestContentType = exchange.getRequestHeaders().getFirst("Content-Type");
      requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
      byte[] bytes = responseBody.getBytes(StandardCharsets.UTF_8);
      exchange.getResponseHeaders().add("Content-Type", "application/soap+xml; charset=utf-8");
      exchange.sendResponseHeaders(responseStatus, bytes.length);
      exchange.getResponseBody().write(bytes);
      exchange.close();
    });
    server.start();
    url = "http://127.0.0.1:" + server.getAddress().getPort() + "/soap";
    responseStatus = 200;
  }

  @After
  public void after() {
    server.stop(0);
  }

  @Test
  public void testSubmitMessage() throws Exception {
    responseBody = SOAP_RESPONSE_START
        + "<submitSingleMessageResponse xmlns=\"urn:cdc:iisb:2011\"><return>MSH|^~\\&amp;|IIS&#13;MSA|AA|1&#13;</return></submitSingleMessageResponse>"
        + SOAP_RESPONSE_END;
    SoapConnector connector = new SoapConnector("Test", url);
    connector.setUserid("user&1");
    connector.setPassword("p<w>");
    connector.setFacilityid("FAC");

    String response = connector.submitMessage("MSH|^~\\&|SMM|\u00e9\rPID|1||123\r", false);

    assertEquals("MSH|^~\\&|IIS\rMSA|AA|1\r", response);
    assertEquals(
        "application/soap+xml; charset=UTF-8; action=\"urn:cdc:iisb:2011:submitSingleMessage\"",
        requestContentType);
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
        + "<soapenv:Envelope xmlns:soapenv=\"http://www.w3.org/2003/05/soap-envelope\">"
        + "<soapenv:Header/><soapenv:Body>"
        + "<ns1:submitSingleMessage xmlns:ns1=\"urn:cdc:iisb:2011\">"
        + "<ns1:username>user&amp;1</ns1:username><ns1:password>p&lt;w&gt;</ns1:password>"
        + "<ns1:facilityID>FAC</ns1:facilityID>"
        + "<ns1:hl7Message>MSH|^~\\&amp;|SMM|\u00e9&#13;PID|1||123&#13;</ns1:hl7Message>"
        + "</ns1:submitSingleMessage></soapenv:Body></soapenv:Envelope>", requestBody);
  }

  @Test
  public void testSubmitMessageNullField() throws Exception {
    responseBody = SOAP_RESPONSE_START
        + "<submitSingleMessageResponse xmlns=\"urn:cdc:iisb:2011\"><return>OK</return></submitSingleMessageResponse>"
        + SOAP_RESPONSE_END;
    SoapConnector connector = new SoapConnector("Test", url);
    connector.setUserid(null);
    connector.submitMessage("MSH|x\r", false);
    assertTrue(requestBody.contains(
        "<ns1:username xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:nil=\"1\"/>"));
  }

  @Test
  public void testUrlEncodedResponse() throws Exception {
    responseBody = SOAP_RESPONSE_START
        + "<submitSingleMessageResponse xmlns=\"urn:cdc:iisb:2011\"><return>MSH%7C%5E~%5C%26%7CIIS</return></submitSingleMessageResponse>"
        + SOAP_RESPONSE_END;
    SoapConnector connector = new SoapConnector("Test", url);
    assertEquals("MSH|^~\\&|IIS", connector.submitMessage("MSH|x\r", false));
  }

  @Test
  public void testConnectivityTest() throws Exception {
    responseBody = SOAP_RESPONSE_START
        + "<ns2:connectivityTestResponse xmlns:ns2=\"urn:cdc:iisb:2011\"><ns2:return>hello &amp; bye</ns2:return></ns2:connectivityTestResponse>"
        + SOAP_RESPONSE_END;
    SoapConnector connector = new SoapConnector("Test", url);
    assertEquals("hello & bye", connector.connectivityTest("hello & bye"));
    assertTrue(requestContentType.endsWith("action=\"urn:cdc:iisb:2011:connectivityTest\""));
    assertTrue(requestBody.contains(
        "<ns1:connectivityTest xmlns:ns1=\"urn:cdc:iisb:2011\"><ns1:echoBack>hello &amp; bye</ns1:echoBack></ns1:connectivityTest>"));
  }

  @Test
  public void testSoapFault() throws Exception {
    responseStatus = 500;
    responseBody = SOAP_RESPONSE_START + "<soap:Fault>"
        + "<soap:Code><soap:Value>soap:Sender</soap:Value></soap:Code>"
        + "<soap:Reason><soap:Text xml:lang=\"en\">Security fault</soap:Text></soap:Reason>"
        + "<soap:Detail><SecurityFault xmlns=\"urn:cdc:iisb:2011\"><Code>10</Code>"
        + "<Reason>Security</Reason><Detail>Bad password</Detail></SecurityFault></soap:Detail>"
        + "</soap:Fault>" + SOAP_RESPONSE_END;
    SoapConnector connector = new SoapConnector("Test", url);
    try {
      connector.submitMessage("MSH|x\r", false);
      fail("Expected a SOAP fault");
    } catch (SoapConnector.SoapFaultException e) {
      assertEquals("Security fault (SecurityFault: Bad password)", e.getMessage());
    }
  }

  @Test
  public void testNonSoapResponse() throws Exception {
    responseStatus = 404;
    responseBody = "Not Found";
    SoapConnector connector = new SoapConnector("Test", url);
    try {
      connector.submitMessage("MSH|x\r", false);
      fail("Expected an exception");
    } catch (IOException e) {
      assertTrue(e.getMessage().contains("HTTP 404"));
      assertTrue(e.getMessage().contains("Not Found"));
    }
  }
}
