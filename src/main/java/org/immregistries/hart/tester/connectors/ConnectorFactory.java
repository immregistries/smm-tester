package org.immregistries.hart.tester.connectors;
import javax.net.ssl.SSLContext;

/**
 * This factory is unnecessary. Just use the SoapConnector constructor directly:
 *  new SoapConnector(String label, String url, sslContext|null)
 *
 *  @deprecated use new {@link #SoapConnector(String label, String url, SSLContext sslContext|null)} directly instead.
 */
@Deprecated
public class ConnectorFactory {
  public static Connector getConnector(String type, String label, String url) throws Exception {
    return getConnector(type, label, url, null);
  }

  public static Connector getConnector(String type, String label, String url, SSLContext sslContext) throws Exception {
    return new SoapConnector(label, url, sslContext);
  }
}
