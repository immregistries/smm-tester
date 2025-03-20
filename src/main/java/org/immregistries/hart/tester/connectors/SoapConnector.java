/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.immregistries.hart.tester.connectors;

import java.net.URLDecoder;
import java.util.List;
import org.immregistries.hart.tester.connectors.tlep.Client_Service;
import org.immregistries.hart.tester.connectors.tlep.Client_ServiceStub;
import org.immregistries.hart.tester.connectors.tlep.ConnectivityTest;
import org.immregistries.hart.tester.connectors.tlep.ConnectivityTestRequestType;
import org.immregistries.hart.tester.connectors.tlep.ConnectivityTestResponse;
import org.immregistries.hart.tester.connectors.tlep.SubmitSingleMessage;
import org.immregistries.hart.tester.connectors.tlep.SubmitSingleMessageRequestType;
import org.immregistries.hart.tester.connectors.tlep.SubmitSingleMessageResponse;

/**
 * @author nathan
 */
public class SoapConnector extends HttpConnector {

  private Client_Service clientService = null;

  public SoapConnector(String label, String url) throws Exception {
      this(label, url, null);
  }
  public SoapConnector(String label, String url, javax.net.ssl.SSLContext sslContext) throws Exception {
    super(label, url, "SOAP");
    clientService = new Client_ServiceStub(this.url, sslContext);
  }

  @Override
  public boolean connectivityTestSupported() {
    return true;
  }

  @Override
  public String submitMessage(String message, boolean debug) throws Exception {
    SubmitSingleMessage submitSingleMessage = new SubmitSingleMessage();
    SubmitSingleMessageRequestType request = new SubmitSingleMessageRequestType();
    request.setFacilityID(this.facilityid);
    request.setHl7Message(message);
    request.setPassword(this.password);
    request.setUsername(this.userid);
    submitSingleMessage.setSubmitSingleMessage(request);
    SubmitSingleMessageResponse response = clientService.submitSingleMessage(submitSingleMessage);
    String responseMessage = response.getSubmitSingleMessageResponse().get_return();
    if (responseMessage != null && responseMessage.startsWith("MSH%7C")) {
      responseMessage = URLDecoder.decode(responseMessage, "UTF-8");
    }
    return responseMessage;
  }

  @Override
  public String connectivityTest(String message) throws Exception {
    ConnectivityTestRequestType request = new ConnectivityTestRequestType();
    request.setEchoBack(message);
    ConnectivityTest connectivityTest = new ConnectivityTest();
    connectivityTest.setConnectivityTest(request);
    ConnectivityTestResponse response = clientService.connectivityTest(connectivityTest);
    return response.getConnectivityTestResponse().get_return();
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
