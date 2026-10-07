package org.immregistries.smm.tester.connectors;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Sends HL7 messages using the CDC IIS SOAP web service (urn:cdc:iisb:2011, SOAP 1.2). The
 * envelope is built and read directly, matching what the earlier Axis2-generated client sent.
 */
public class SoapConnector extends HttpConnector {

  private static final String SOAP12_NS = "http://www.w3.org/2003/05/soap-envelope";
  private static final String SOAP11_NS = "http://schemas.xmlsoap.org/soap/envelope/";
  private static final String CDC_NS = "urn:cdc:iisb:2011";
  private static final int TIMEOUT_MS = 2 * 60 * 1000;
  private static final int ERROR_SNIPPET_LENGTH = 500;

  private SSLContext sslContext = null;

  public SoapConnector(String label, String url) throws Exception {
    this(label, url, null);
  }

  public SoapConnector(String label, String url, SSLContext sslContext) throws Exception {
    super(label, url, ConnectorFactory.TYPE_SOAP);
    this.sslContext = sslContext;
  }

  @Override
  public boolean connectivityTestSupported() {
    return true;
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception {
    StringBuilder body = new StringBuilder();
    body.append("<ns1:submitSingleMessage xmlns:ns1=\"" + CDC_NS + "\">");
    appendElement(body, "username", this.userid);
    appendElement(body, "password", this.password);
    appendElement(body, "facilityID", this.facilityid);
    appendElement(body, "hl7Message", message);
    body.append("</ns1:submitSingleMessage>");
    String responseMessage = send("submitSingleMessage", body.toString());
    if (responseMessage != null && responseMessage.startsWith("MSH%7C")) {
      responseMessage = URLDecoder.decode(responseMessage, "UTF-8");
    }
    return responseMessage;
  }

  @Override
  public String connectivityTest(String message) throws Exception {
    StringBuilder body = new StringBuilder();
    body.append("<ns1:connectivityTest xmlns:ns1=\"" + CDC_NS + "\">");
    appendElement(body, "echoBack", message);
    body.append("</ns1:connectivityTest>");
    return send("connectivityTest", body.toString());
  }

  /**
   * Posts the operation's body in a SOAP 1.2 envelope and returns the text of the response's
   * <code>return</code> element. A SOAP fault is thrown as a {@link SoapFaultException}.
   */
  private String send(String operation, String bodyContent) throws Exception {
    String envelope = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "<soapenv:Envelope xmlns:soapenv=\""
        + SOAP12_NS + "\"><soapenv:Header/><soapenv:Body>" + bodyContent
        + "</soapenv:Body></soapenv:Envelope>";
    byte[] requestBytes = envelope.getBytes(StandardCharsets.UTF_8);

    HttpURLConnection urlConn = (HttpURLConnection) new URL(url).openConnection();
    if (urlConn instanceof HttpsURLConnection) {
      SSLSocketFactory factory =
          sslContext != null ? sslContext.getSocketFactory() : setupSSLSocketFactory(false, null);
      if (factory != null) {
        ((HttpsURLConnection) urlConn).setSSLSocketFactory(factory);
      }
    }
    urlConn.setRequestMethod("POST");
    urlConn.setConnectTimeout(TIMEOUT_MS);
    urlConn.setReadTimeout(TIMEOUT_MS);
    urlConn.setDoInput(true);
    urlConn.setDoOutput(true);
    urlConn.setUseCaches(false);
    urlConn.setFixedLengthStreamingMode(requestBytes.length);
    urlConn.setRequestProperty("Content-Type", "application/soap+xml; charset=UTF-8; action=\""
        + CDC_NS + ":" + operation + "\"");
    try (OutputStream out = urlConn.getOutputStream()) {
      out.write(requestBytes);
    }

    int status = urlConn.getResponseCode();
    byte[] responseBytes;
    try (InputStream in = status >= 400 ? urlConn.getErrorStream() : urlConn.getInputStream()) {
      responseBytes = in == null ? new byte[0] : in.readAllBytes();
    }

    Document document;
    try {
      document = parse(responseBytes);
    } catch (Exception e) {
      throw new IOException("Unexpected response from " + url + " (HTTP " + status + "): "
          + snippet(responseBytes), e);
    }

    Element fault = findFirst(document.getDocumentElement(), SOAP12_NS, "Fault");
    if (fault == null) {
      fault = findFirst(document.getDocumentElement(), SOAP11_NS, "Fault");
    }
    if (fault != null) {
      throw new SoapFaultException(describeFault(fault));
    }
    Element returnElement = findFirst(document.getDocumentElement(), CDC_NS, "return");
    if (returnElement == null) {
      throw new IOException("Response from " + url + " (HTTP " + status
          + ") did not contain a return element: " + snippet(responseBytes));
    }
    return returnElement.getTextContent();
  }

  private static void appendElement(StringBuilder sb, String name, String value) {
    if (value == null) {
      sb.append("<ns1:" + name + " xmlns:xsi=\"" + XMLConstants.W3C_XML_SCHEMA_INSTANCE_NS_URI
          + "\" xsi:nil=\"1\"/>");
    } else {
      sb.append("<ns1:" + name + ">" + escapeXml(value) + "</ns1:" + name + ">");
    }
  }

  /**
   * Escapes text for an XML element. Carriage returns are written as character references so the
   * HL7 segment separators are not normalized to line feeds by the receiving XML parser.
   */
  protected static String escapeXml(String value) {
    StringBuilder sb = new StringBuilder(value.length() + 16);
    for (int i = 0; i < value.length(); i++) {
      char c = value.charAt(i);
      switch (c) {
        case '&':
          sb.append("&amp;");
          break;
        case '<':
          sb.append("&lt;");
          break;
        case '>':
          sb.append("&gt;");
          break;
        case '\r':
          sb.append("&#13;");
          break;
        default:
          sb.append(c);
      }
    }
    return sb.toString();
  }

  private static Document parse(byte[] bytes) throws Exception {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setNamespaceAware(true);
    factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
    factory.setXIncludeAware(false);
    factory.setExpandEntityReferences(false);
    DocumentBuilder builder = factory.newDocumentBuilder();
    return builder.parse(new ByteArrayInputStream(bytes));
  }

  private static Element findFirst(Element parent, String namespace, String localName) {
    NodeList nodeList = parent.getElementsByTagNameNS(namespace, localName);
    return nodeList.getLength() == 0 ? null : (Element) nodeList.item(0);
  }

  private static String describeFault(Element fault) {
    String reason = "";
    Element reasonText = findFirst(fault, SOAP12_NS, "Text");
    if (reasonText != null) {
      reason = reasonText.getTextContent().trim();
    } else {
      NodeList faultString = fault.getElementsByTagName("faultstring");
      if (faultString.getLength() > 0) {
        reason = faultString.item(0).getTextContent().trim();
      }
    }
    Element detail = findFirst(fault, SOAP12_NS, "Detail");
    if (detail == null) {
      NodeList detailList = fault.getElementsByTagName("detail");
      detail = detailList.getLength() == 0 ? null : (Element) detailList.item(0);
    }
    if (detail != null) {
      for (Node child = detail.getFirstChild(); child != null; child = child.getNextSibling()) {
        if (child instanceof Element) {
          Element faultDetail = (Element) child;
          Element detailText = findFirst(faultDetail, CDC_NS, "Detail");
          String text = detailText != null ? detailText.getTextContent().trim()
              : faultDetail.getTextContent().trim();
          return reason + " (" + faultDetail.getLocalName() + (text.equals("") ? "" : ": " + text)
              + ")";
        }
      }
    }
    return reason;
  }

  private static String snippet(byte[] bytes) {
    String text = new String(bytes, StandardCharsets.UTF_8);
    return text.length() > ERROR_SNIPPET_LENGTH ? text.substring(0, ERROR_SNIPPET_LENGTH) + "..."
        : text;
  }

  public static class SoapFaultException extends IOException {
    private static final long serialVersionUID = 1L;

    public SoapFaultException(String message) {
      super(message);
    }
  }

  @Override
  protected void makeScriptAdditions(StringBuilder sb) {
    // do nothing
  }

  @Override
  protected void setupFields(List<String> fields) {
    super.setupFields(fields);
  }

}
