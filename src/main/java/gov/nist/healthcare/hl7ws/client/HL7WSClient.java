package gov.nist.healthcare.hl7ws.client;

import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;


public class HL7WSClient {
  private MessageValidationV2SoapClient client = null;
  private String URL;
  private String xmlResourceList;

  public HL7WSClient() {
    this("http://hit-testing.nist.gov:8080/hl7v2ws/services/soap/MessageValidationV2/");
  }

  public HL7WSClient(String url) {
    // Connect to the wb service
    this.client = new MessageValidationV2SoapClient(URL);

    // Query the resources already loaded
    xmlResourceList = client.getServiceStatus();
  }

  public String validateMessage(String baseTableOid, String userTableOid, String profileOid, String validationContext, String message) {
    // returns XML validation report
    String tableOids = baseTableOid + ":" + userTableOid;
    return this.client.validate(message, profileOid, tableOids, validationContext);
  }

  public String loadResource(String resource, String oid, String type) {
    return this.client.loadResource(resource, oid, type);
  }

  private static URI getPath(String fn) throws URISyntaxException {
    return HL7WSClient.class.getResource(fn).toURI();
  }

  private static String getValue(String tag, Element element) {
    NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
    Node node = (Node) nodes.item(0);
    return node.getNodeValue();
  }

  // parse the response to get the oid
  private static String getOid(String xmlResponse) {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = null;
    Document doc = null;
    String oid = null;

    try {
      dBuilder = dbFactory.newDocumentBuilder();
    } catch (Exception ex) {
    } ;

    try {
      doc = dBuilder.parse(new InputSource(new StringReader(xmlResponse)));
    } catch (Exception ex) {
    } ;
    doc.getDocumentElement().normalize();
    NodeList nodes = doc.getElementsByTagName("xmlLoadResource");
    for (int i = 0; i < nodes.getLength(); i++) {
      Node node = nodes.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;
        oid = getValue("oid", element);
      }
    }
    return oid;
  }
}

