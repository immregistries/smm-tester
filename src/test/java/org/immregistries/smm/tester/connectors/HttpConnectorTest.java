package org.immregistries.smm.tester.connectors;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.sun.net.httpserver.HttpServer;

public class HttpConnectorTest {

  private HttpServer server;
  private String url;
  private String requestAuthorization;
  private String requestBody;

  @Before
  public void before() throws IOException {
    server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
    server.createContext("/post", exchange -> {
      requestAuthorization = exchange.getRequestHeaders().getFirst("Authorization");
      requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
      byte[] bytes = "MSH|^~\\&|IIS\rMSA|AA|1\r".getBytes(StandardCharsets.UTF_8);
      exchange.sendResponseHeaders(200, bytes.length);
      exchange.getResponseBody().write(bytes);
      exchange.close();
    });
    server.start();
    url = "http://127.0.0.1:" + server.getAddress().getPort() + "/post";
  }

  @After
  public void after() {
    server.stop(0);
  }

  @Test
  public void testBasicAuthentication() throws Exception {
    HttpConnector connector = new HttpConnector("Test", url);
    connector.setAuthenticationMethod(HttpConnector.AuthenticationMethod.BASIC);
    connector.setUserid("Aladdin");
    connector.setPassword("open sesame");
    connector.setThrowExceptions(true);

    String message = "MSH|^~\\&|SMM|José\r";
    connector.submitMessage(message, false);

    // RFC 7617 example credentials
    assertEquals("Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ==", requestAuthorization);
    assertEquals(message, requestBody);
  }
}
