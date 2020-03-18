package org.immregistries.smm.tester.connectors;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class MAConnector2020 extends HttpConnector {

  private static final String HL7_REQUEST_RESULT_START_TAG = "<urn:return>"; // "<urn:submitSingleMessageResponse
                                                                             // xmlns:urn=\"urn:cdc:iisb:2011\">";
  private static final String HL7_REQUEST_RESULT_END_TAG = "</urn:return>"; // "</urn:submitSingleMessageResponse>";

  protected MAConnector2020(String label, String url, String type) {
    super(label, url, type);
    this.url = url;
  }

  public MAConnector2020(String label, String url) {
    super(label, url, ConnectorFactory.TYPE_MA_SOAP_2020);
    this.url = url;
  }

  @Override
  protected void setupFields(List<String> fields) {
    // TODO Auto-generated method stub

  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception {
    ClientConnection cc = new ClientConnection();
    cc.setUrl(url);
    String result = "";
    try {
      result = sendRequest(message, cc, debug);
    } catch (Exception e) {
      if (throwExceptions) {
        throw e;
      }
      return "Unable to relay message, received this error: " + e.getMessage();
    }

    StringBuffer sbuf = new StringBuffer(result.length());
    boolean inTag = false;
    for (char c : result.toCharArray()) {
      if (c == '<') {
        inTag = true;
      } else if (c == '>') {
        inTag = false;
      } else if (!inTag) {
        sbuf.append(c);
      }
    }
    if (sbuf.length() > 0) {
      result = sbuf.toString();
    }

    while (result != null && result.length() > 0 && result.charAt(0) < ' ') {
      result = result.substring(1);
    }
    return result;

  }

  public String sendRequest(String request, ClientConnection conn, boolean debug)
      throws IOException {
    StringBuilder debugLog = null;
    if (debug) {
      debugLog = new StringBuilder();
    }
    try {
      // SSLSocketFactory factory = setupSSLSocketFactory(debug, debugLog);
      HttpURLConnection urlConn;
      DataOutputStream printout;
      InputStreamReader input = null;
      URL url = new URL(conn.getUrl());

      urlConn = (HttpURLConnection) url.openConnection();

      urlConn.setRequestMethod("POST");

      urlConn.setRequestProperty("Content-Type",
          "application/soap+xml;charset=UTF-8;action=\"urn:cdc:iisb:2011:submitSingleMessage\"");
      urlConn.setDoInput(true);
      urlConn.setDoOutput(true);
      String content;

      String lineEnding = "\r";

      StringBuilder sb = new StringBuilder();
      // sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
      sb.append(
          "<soap:Envelope xmlns:soap=\"http://www.w3.org/2003/05/soap-envelope\" xmlns:urn=\"urn:cdc:iisb:2011\">"
              + lineEnding);
      sb.append("   <soap:Header>" + lineEnding);
      sb.append(
          "      <wsse:Security xmlns:wsse=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd\">"
              + lineEnding);
      sb.append(
          "         <wsse:UsernameToken wsu:Id=\"UsernameToken-100\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\">"
              + lineEnding);
      sb.append("            <wsse:Username>" + userid + "</wsse:Username>" + lineEnding);
      sb.append(
          "            <wsse:Password Type=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-username-token-profile-1.0#PasswordText\">"
              + password + "</wsse:Password>" + lineEnding);
      sb.append("              </wsse:UsernameToken>" + lineEnding);
      sb.append("        </wsse:Security>" + lineEnding);
      sb.append("    </soap:Header>" + lineEnding);
      sb.append("    <soap:Body>" + lineEnding);
      sb.append("      <urn:submitSingleMessage>" + lineEnding);
      sb.append("        <urn:facilityID>" + facilityid + "</urn:facilityID> " + lineEnding);
      sb.append("        <urn:hl7Message><![CDATA[" + new String(request.getBytes())
          + "]]></urn:hl7Message>" + lineEnding);
      sb.append("      </urn:submitSingleMessage>" + lineEnding);
      sb.append("    </soap:Body>" + lineEnding);
      sb.append("</soap:Envelope>" + lineEnding);

      content = sb.toString();

      System.out.println("-----------------------------------------------------------");
      System.out.println(content);

      if (debug) {
        debugLog.append("SOAP Submitted: \n" + sb);
      }

      printout = new DataOutputStream(urlConn.getOutputStream());
      printout.writeBytes(content);
      printout.flush();
      printout.close();
      input = new InputStreamReader(urlConn.getInputStream());
      StringBuilder response = new StringBuilder();
      BufferedReader in = new BufferedReader(input);
      String line;
      while ((line = in.readLine()) != null) {
        response.append(line);
        response.append('\r');
      }
      input.close();
      String responseString = response.toString();
      if (debug) {
        debugLog.append("\nResponse returned \n" + responseString);
      }


      System.out.println("-----------------------------------------------------------");
      System.out.println(responseString);
      int startPos = responseString.indexOf(HL7_REQUEST_RESULT_START_TAG);
      int endPos = responseString.indexOf(HL7_REQUEST_RESULT_END_TAG);
      if (startPos > 0 && endPos > startPos) {
        responseString =
            responseString.substring(startPos + HL7_REQUEST_RESULT_START_TAG.length(), endPos);
        response = new StringBuilder(responseString);
      }
      response = new StringBuilder(new String(response.toString().getBytes()));
      if (debug) {
        response.append("\r");
        response.append("DEBUG LOG: \r");
        response.append(debugLog);
      }
      return response.toString();
    } catch (IOException e) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter out = new PrintWriter(stringWriter);
      out.println("Unable to complete request");
      e.printStackTrace(out);
      out.println("DEBUG LOG: \r");
      out.println(debugLog);
      out.close();
      return stringWriter.toString();
    }

  }

  @Override
  public String connectivityTest(String message) throws Exception {
    return "Not supported by MA SOAP interface";
  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb) {
    // TODO Auto-generated method stub
  }

}
