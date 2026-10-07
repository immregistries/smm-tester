package org.immregistries.smm.tester.connectors;
import javax.net.ssl.SSLContext;

public class ConnectorFactory {

  public static final String TYPE_SOAP = "SOAP";
  public static final String TYPE_POST = "POST";
  public static final String TYPE_MLLP = "MLLP";
  public static final String TYPE_RAW = "RAW";
  public static final String TYPE_IZ_GATEWAY = "IZ Gateway";

  public static final String[][] TYPES = {{TYPE_SOAP, "SOAP"}, {TYPE_POST, "POST"},
      {TYPE_RAW, "Raw"}, {TYPE_MLLP, "MLLP"}, {TYPE_IZ_GATEWAY, "IZ Gateway"}};

  public static Connector getConnector(String type, String label, String url) throws Exception {
      return getConnector(type, label, url, null);
  }

  public static Connector getConnector(String type, String label, String url, SSLContext sslContext) throws Exception {
    Connector connector = null;
    if (type.equals(TYPE_SOAP)) {
      connector = new SoapConnector(label, url, sslContext);
    } else if (type.equals(TYPE_MLLP)) {
      connector = new MLLPConnector(label, url);
    } else if (type.equals(TYPE_POST)) {
      connector = new HttpConnector(label, url);
    } else if (type.equals(TYPE_RAW)) {
      connector = new HttpRawConnector(label, url);
    } else if (type.equals(TYPE_IZ_GATEWAY)) {
      connector = new IZGatewayConnector(label, url);
    }
    return connector;
  }
}
