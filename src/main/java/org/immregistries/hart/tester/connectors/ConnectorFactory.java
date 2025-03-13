package org.immregistries.hart.tester.connectors;
import javax.net.ssl.SSLContext;

public class ConnectorFactory {
  public static Connector getConnector(String type, String label, String url) throws Exception {
    return getConnector(type, label, url, null);
  }

  public static Connector getConnector(String type, String label, String url, SSLContext sslContext) throws Exception {
    return new SoapConnector(label, url, sslContext);
  }
}
