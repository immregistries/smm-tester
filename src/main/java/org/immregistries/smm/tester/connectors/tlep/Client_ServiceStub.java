/**
 * Client_ServiceStub.java
 *
 * This file was auto-generated from WSDL by the Apache Axis2 version: 1.6.2 Built on : Apr 17, 2012
 * (05:33:49 IST)
 */
package org.immregistries.smm.tester.connectors.tlep;

/*
 * Client_ServiceStub java implementation
 */
@SuppressWarnings({"all"})
public class Client_ServiceStub extends org.apache.axis2.client.Stub implements Client_Service {
  protected org.apache.axis2.description.AxisOperation[] _operations;

  // hashmaps to keep the fault mapping
  private java.util.HashMap faultExceptionNameMap = new java.util.HashMap();
  private java.util.HashMap faultExceptionClassNameMap = new java.util.HashMap();
  private java.util.HashMap faultMessageMap = new java.util.HashMap();

  private static int counter = 0;

  private static synchronized java.lang.String getUniqueSuffix() {
    // reset the counter if it is greater than 99999
    if (counter > 99999) {
      counter = 0;
    }
    counter = counter + 1;
    return java.lang.Long.toString(java.lang.System.currentTimeMillis()) + "_" + counter;
  }

  private void populateAxisService() throws org.apache.axis2.AxisFault {

    // creating the Service with a unique name
    _service = new org.apache.axis2.description.AxisService("Client_Service" + getUniqueSuffix());
    addAnonymousOperations();

    // creating the operations
    org.apache.axis2.description.AxisOperation __operation;

    _operations = new org.apache.axis2.description.AxisOperation[2];

    __operation = new org.apache.axis2.description.OutInAxisOperation();

    __operation.setName(new javax.xml.namespace.QName("urn:cdc:iisb:2011", "submitSingleMessage"));
    _service.addOperation(__operation);

    (__operation).getMessage(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_OUT_VALUE)
        .getPolicySubject().attachPolicy(getPolicy(
            "<wsp:Policy wsu:Id=\"client_Binding_Soap12Policy\" xmlns:wsp=\"http://www.w3.org/ns/ws-policy\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><wsp:ExactlyOne><wsp:All><wsoma:OptimizedMimeSerialization xmlns:wsoma=\"http://schemas.xmlsoap.org/ws/2004/09/policy/optimizedmimeserialization\"></wsoma:OptimizedMimeSerialization></wsp:All></wsp:ExactlyOne></wsp:Policy>"));

    (__operation).getMessage(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE)
        .getPolicySubject().attachPolicy(getPolicy(
            "<wsp:Policy wsu:Id=\"client_Binding_Soap12Policy\" xmlns:wsp=\"http://www.w3.org/ns/ws-policy\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><wsp:ExactlyOne><wsp:All><wsoma:OptimizedMimeSerialization xmlns:wsoma=\"http://schemas.xmlsoap.org/ws/2004/09/policy/optimizedmimeserialization\"></wsoma:OptimizedMimeSerialization></wsp:All></wsp:ExactlyOne></wsp:Policy>"));

    _operations[0] = __operation;

    __operation = new org.apache.axis2.description.OutInAxisOperation();

    __operation.setName(new javax.xml.namespace.QName("urn:cdc:iisb:2011", "connectivityTest"));
    _service.addOperation(__operation);

    (__operation).getMessage(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_OUT_VALUE)
        .getPolicySubject().attachPolicy(getPolicy(
            "<wsp:Policy wsu:Id=\"client_Binding_Soap12Policy\" xmlns:wsp=\"http://www.w3.org/ns/ws-policy\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><wsp:ExactlyOne><wsp:All><wsoma:OptimizedMimeSerialization xmlns:wsoma=\"http://schemas.xmlsoap.org/ws/2004/09/policy/optimizedmimeserialization\"></wsoma:OptimizedMimeSerialization></wsp:All></wsp:ExactlyOne></wsp:Policy>"));

    (__operation).getMessage(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE)
        .getPolicySubject().attachPolicy(getPolicy(
            "<wsp:Policy wsu:Id=\"client_Binding_Soap12Policy\" xmlns:wsp=\"http://www.w3.org/ns/ws-policy\" xmlns:wsu=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd\"><wsp:ExactlyOne><wsp:All><wsoma:OptimizedMimeSerialization xmlns:wsoma=\"http://schemas.xmlsoap.org/ws/2004/09/policy/optimizedmimeserialization\"></wsoma:OptimizedMimeSerialization></wsp:All></wsp:ExactlyOne></wsp:Policy>"));

    _operations[1] = __operation;

  }

  // populates the faults
  private void populateFaults() {

    faultExceptionNameMap.put(
        new org.apache.axis2.client.FaultMapKey(
            new javax.xml.namespace.QName("urn:cdc:iisb:2011", "fault"), "submitSingleMessage"),
        "_2011.iisb.cdc.UnknownFault_Message");
    faultExceptionClassNameMap.put(
        new org.apache.axis2.client.FaultMapKey(
            new javax.xml.namespace.QName("urn:cdc:iisb:2011", "fault"), "submitSingleMessage"),
        "_2011.iisb.cdc.UnknownFault_Message");
    faultMessageMap.put(
        new org.apache.axis2.client.FaultMapKey(
            new javax.xml.namespace.QName("urn:cdc:iisb:2011", "fault"), "submitSingleMessage"),
        "_2011.iisb.cdc.Fault");

    faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(
        new javax.xml.namespace.QName("urn:cdc:iisb:2011", "SecurityFault"), "submitSingleMessage"),
        "_2011.iisb.cdc.SecurityFault_Message");
    faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(
        new javax.xml.namespace.QName("urn:cdc:iisb:2011", "SecurityFault"), "submitSingleMessage"),
        "_2011.iisb.cdc.SecurityFault_Message");
    faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(
        new javax.xml.namespace.QName("urn:cdc:iisb:2011", "SecurityFault"), "submitSingleMessage"),
        "_2011.iisb.cdc.SecurityFault");

    faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(
        new javax.xml.namespace.QName("urn:cdc:iisb:2011", "MessageTooLargeFault"),
        "submitSingleMessage"), "_2011.iisb.cdc.MessageTooLargeFault_Message");
    faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(
        new javax.xml.namespace.QName("urn:cdc:iisb:2011", "MessageTooLargeFault"),
        "submitSingleMessage"), "_2011.iisb.cdc.MessageTooLargeFault_Message");
    faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(
        new javax.xml.namespace.QName("urn:cdc:iisb:2011", "MessageTooLargeFault"),
        "submitSingleMessage"), "_2011.iisb.cdc.MessageTooLargeFault");

    faultExceptionNameMap.put(new org.apache.axis2.client.FaultMapKey(
        new javax.xml.namespace.QName("urn:cdc:iisb:2011", "UnsupportedOperationFault"),
        "connectivityTest"), "_2011.iisb.cdc.UnsupportedOperationFault_Message");
    faultExceptionClassNameMap.put(new org.apache.axis2.client.FaultMapKey(
        new javax.xml.namespace.QName("urn:cdc:iisb:2011", "UnsupportedOperationFault"),
        "connectivityTest"), "_2011.iisb.cdc.UnsupportedOperationFault_Message");
    faultMessageMap.put(new org.apache.axis2.client.FaultMapKey(
        new javax.xml.namespace.QName("urn:cdc:iisb:2011", "UnsupportedOperationFault"),
        "connectivityTest"), "_2011.iisb.cdc.UnsupportedOperationFault");

    faultExceptionNameMap.put(
        new org.apache.axis2.client.FaultMapKey(
            new javax.xml.namespace.QName("urn:cdc:iisb:2011", "fault"), "connectivityTest"),
        "_2011.iisb.cdc.UnknownFault_Message");
    faultExceptionClassNameMap.put(
        new org.apache.axis2.client.FaultMapKey(
            new javax.xml.namespace.QName("urn:cdc:iisb:2011", "fault"), "connectivityTest"),
        "_2011.iisb.cdc.UnknownFault_Message");
    faultMessageMap.put(
        new org.apache.axis2.client.FaultMapKey(
            new javax.xml.namespace.QName("urn:cdc:iisb:2011", "fault"), "connectivityTest"),
        "_2011.iisb.cdc.Fault");

  }

  /**
   * Constructor that takes in a configContext
   */

  public Client_ServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
      java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
    this(configurationContext, targetEndpoint, false);
  }

  public Client_ServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
      java.lang.String targetEndpoint, boolean useSeparateListener)
      throws org.apache.axis2.AxisFault {
    this(configurationContext, targetEndpoint, useSeparateListener, null);
    }
  /**
   * Constructor that takes in a configContext and useseperate listner
   */
  public Client_ServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext,
      java.lang.String targetEndpoint, boolean useSeparateListener, javax.net.ssl.SSLContext sslContext)
      throws org.apache.axis2.AxisFault {
    // To populate AxisService
    populateAxisService();
    populateFaults();

    _serviceClient = new org.apache.axis2.client.ServiceClient(configurationContext, _service);

    _service.applyPolicy();

    _serviceClient.getOptions()
        .setTo(new org.apache.axis2.addressing.EndpointReference(targetEndpoint));
    _serviceClient.getOptions().setUseSeparateListener(useSeparateListener);

    // Set the soap version
    _serviceClient.getOptions()
        .setSoapVersionURI(org.apache.axiom.soap.SOAP12Constants.SOAP_ENVELOPE_NAMESPACE_URI);

    _serviceClient.getOptions().setTimeOutInMilliSeconds(2 * 60 * 1000);

    if(sslContext != null){
      //set SSLContext for SOAP calls
      try {
        org.apache.http.conn.ssl.SSLSocketFactory sf = new org.apache.http.conn.ssl.SSLSocketFactory(sslContext);
        org.apache.http.conn.scheme.Scheme httpsScheme = new org.apache.http.conn.scheme.Scheme("https", 443, sf);
        org.apache.http.conn.scheme.SchemeRegistry schemeRegistry = new org.apache.http.conn.scheme.SchemeRegistry();
        schemeRegistry.register(httpsScheme);
        org.apache.http.conn.ClientConnectionManager cm =  new org.apache.http.impl.conn.SingleClientConnManager(schemeRegistry);
        org.apache.http.client.HttpClient httpClient = new org.apache.http.impl.client.DefaultHttpClient(cm);

        _serviceClient.getOptions().setProperty(org.apache.axis2.kernel.http.HTTPConstants.CACHED_HTTP_CLIENT, httpClient);
      }catch(Exception e){
        //womp womp, couldn't set the offered context
      }
    }

  }

  /**
   * Default Constructor
   */
  public Client_ServiceStub(org.apache.axis2.context.ConfigurationContext configurationContext)
      throws org.apache.axis2.AxisFault {

    this(configurationContext, "http://192.168.1.2:8282/ImmMessagingServer/client_Service");

  }

  /**
   * Default Constructor
   */
  public Client_ServiceStub() throws org.apache.axis2.AxisFault {

    this("http://192.168.1.2:8282/ImmMessagingServer/client_Service");

  }

  /**
   * Constructor taking the target endpoint
   */
  public Client_ServiceStub(java.lang.String targetEndpoint) throws org.apache.axis2.AxisFault {
    this(null, targetEndpoint);
  }

  public Client_ServiceStub(java.lang.String targetEndpoint, javax.net.ssl.SSLContext sslContext) throws org.apache.axis2.AxisFault {
    this(null, targetEndpoint, false, sslContext);
  }

  /**
   * Auto generated method signature submit single message
   * 
   * @see org.immregistries.smm.tester.connectors.tlep.Client_Service#submitSingleMessage
   * @param submitSingleMessage4
   * 
   * @throws org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message :
   * @throws org.immregistries.smm.tester.connectors.tlep.SecurityFault_Message :
   * @throws org.immregistries.smm.tester.connectors.tlep.MessageTooLargeFault_Message :
   */

  public org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessageResponse submitSingleMessage(
      org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessage submitSingleMessage4)
      throws java.rmi.RemoteException,
      org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message,
      org.immregistries.smm.tester.connectors.tlep.SecurityFault_Message,
      org.immregistries.smm.tester.connectors.tlep.MessageTooLargeFault_Message {
    org.apache.axis2.context.MessageContext _messageContext = null;
    try {
      org.apache.axis2.client.OperationClient _operationClient =
          _serviceClient.createClient(_operations[0].getName());
      _operationClient.getOptions().setAction("urn:cdc:iisb:2011:submitSingleMessage");
      _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

      addPropertyToOperationClient(_operationClient,
          org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

      // create a message context
      _messageContext = new org.apache.axis2.context.MessageContext();

      // create SOAP envelope with that payload
      org.apache.axiom.soap.SOAPEnvelope env = null;

      env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
          submitSingleMessage4,
          optimizeContent(
              new javax.xml.namespace.QName("urn:cdc:iisb:2011", "submitSingleMessage")),
          new javax.xml.namespace.QName("urn:cdc:iisb:2011", "submitSingleMessage"));

      // adding SOAP soap_headers
      _serviceClient.addHeadersToEnvelope(env);
      // set the message context with that soap envelope
      _messageContext.setEnvelope(env);

      // add the message contxt to the operation client
      _operationClient.addMessageContext(_messageContext);

      // execute the operation client
      _operationClient.execute(true);

      org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
          .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
      org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

      java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(),
          org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessageResponse.class,
          getEnvelopeNamespaces(_returnEnv));

      return (org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessageResponse) object;

    } catch (org.apache.axis2.AxisFault f) {

      org.apache.axiom.om.OMElement faultElt = f.getDetail();
      if (faultElt != null) {
        if (faultExceptionNameMap.containsKey(
            new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "submitSingleMessage"))) {
          // make the fault by reflection
          try {
            java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
                .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                    "submitSingleMessage"));
            java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
            java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
            java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
            // message class
            java.lang.String messageClassName =
                (java.lang.String) faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(
                    faultElt.getQName(), "submitSingleMessage"));
            java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
            java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
            java.lang.reflect.Method m =
                exceptionClass.getMethod("setFaultMessage", new java.lang.Class[] {messageClass});
            m.invoke(ex, new java.lang.Object[] {messageObject});

            if (ex instanceof org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message) {
              throw (org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message) ex;
            }

            if (ex instanceof org.immregistries.smm.tester.connectors.tlep.SecurityFault_Message) {
              throw (org.immregistries.smm.tester.connectors.tlep.SecurityFault_Message) ex;
            }

            if (ex instanceof org.immregistries.smm.tester.connectors.tlep.MessageTooLargeFault_Message) {
              throw (org.immregistries.smm.tester.connectors.tlep.MessageTooLargeFault_Message) ex;
            }

            throw new java.rmi.RemoteException(ex.getMessage(), ex);
          } catch (java.lang.ClassCastException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          } catch (java.lang.ClassNotFoundException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          } catch (java.lang.NoSuchMethodException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          } catch (java.lang.reflect.InvocationTargetException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          } catch (java.lang.IllegalAccessException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          } catch (java.lang.InstantiationException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          }
        } else {
          throw f;
        }
      } else {
        throw f;
      }
    } finally {
      if (_messageContext.getTransportOut() != null) {
        _messageContext.getTransportOut().getSender().cleanup(_messageContext);
      }
    }
  }

  /**
   * Auto generated method signature for Asynchronous Invocations submit single message
   * 
   * @see org.immregistries.smm.tester.connectors.tlep.Client_Service#startsubmitSingleMessage
   * @param submitSingleMessage4
   */
  public void startsubmitSingleMessage(

      org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessage submitSingleMessage4,

      final org.immregistries.smm.tester.connectors.tlep.Client_ServiceCallbackHandler callback)

      throws java.rmi.RemoteException {

    org.apache.axis2.client.OperationClient _operationClient =
        _serviceClient.createClient(_operations[0].getName());
    _operationClient.getOptions().setAction("urn:cdc:iisb:2011:submitSingleMessage");
    _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

    addPropertyToOperationClient(_operationClient,
        org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

    // create SOAP envelope with that payload
    org.apache.axiom.soap.SOAPEnvelope env = null;
    final org.apache.axis2.context.MessageContext _messageContext =
        new org.apache.axis2.context.MessageContext();

    // Style is Doc.

    env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
        submitSingleMessage4,
        optimizeContent(new javax.xml.namespace.QName("urn:cdc:iisb:2011", "submitSingleMessage")),
        new javax.xml.namespace.QName("urn:cdc:iisb:2011", "submitSingleMessage"));

    // adding SOAP soap_headers
    _serviceClient.addHeadersToEnvelope(env);
    // create message context with that soap envelope
    _messageContext.setEnvelope(env);

    // add the message context to the operation client
    _operationClient.addMessageContext(_messageContext);

    _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
      public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
        try {
          org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();

          java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
              org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessageResponse.class,
              getEnvelopeNamespaces(resultEnv));
          callback.receiveResultsubmitSingleMessage(
              (org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessageResponse) object);

        } catch (org.apache.axis2.AxisFault e) {
          callback.receiveErrorsubmitSingleMessage(e);
        }
      }

      public void onError(java.lang.Exception error) {
        if (error instanceof org.apache.axis2.AxisFault) {
          org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
          org.apache.axiom.om.OMElement faultElt = f.getDetail();
          if (faultElt != null) {
            if (faultExceptionNameMap.containsKey(new org.apache.axis2.client.FaultMapKey(
                faultElt.getQName(), "submitSingleMessage"))) {
              // make the fault by reflection
              try {
                java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
                    .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                        "submitSingleMessage"));
                java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                java.lang.reflect.Constructor constructor =
                    exceptionClass.getConstructor(String.class);
                java.lang.Exception ex =
                    (java.lang.Exception) constructor.newInstance(f.getMessage());
                // message class
                java.lang.String messageClassName =
                    (java.lang.String) faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(
                        faultElt.getQName(), "submitSingleMessage"));
                java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
                java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                    new java.lang.Class[] {messageClass});
                m.invoke(ex, new java.lang.Object[] {messageObject});

                if (ex instanceof org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message) {
                  callback.receiveErrorsubmitSingleMessage(
                      (org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message) ex);
                  return;
                }

                if (ex instanceof org.immregistries.smm.tester.connectors.tlep.SecurityFault_Message) {
                  callback.receiveErrorsubmitSingleMessage(
                      (org.immregistries.smm.tester.connectors.tlep.SecurityFault_Message) ex);
                  return;
                }

                if (ex instanceof org.immregistries.smm.tester.connectors.tlep.MessageTooLargeFault_Message) {
                  callback.receiveErrorsubmitSingleMessage(
                      (org.immregistries.smm.tester.connectors.tlep.MessageTooLargeFault_Message) ex);
                  return;
                }

                callback.receiveErrorsubmitSingleMessage(
                    new java.rmi.RemoteException(ex.getMessage(), ex));
              } catch (java.lang.ClassCastException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorsubmitSingleMessage(f);
              } catch (java.lang.ClassNotFoundException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorsubmitSingleMessage(f);
              } catch (java.lang.NoSuchMethodException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorsubmitSingleMessage(f);
              } catch (java.lang.reflect.InvocationTargetException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorsubmitSingleMessage(f);
              } catch (java.lang.IllegalAccessException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorsubmitSingleMessage(f);
              } catch (java.lang.InstantiationException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorsubmitSingleMessage(f);
              } catch (org.apache.axis2.AxisFault e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorsubmitSingleMessage(f);
              }
            } else {
              callback.receiveErrorsubmitSingleMessage(f);
            }
          } else {
            callback.receiveErrorsubmitSingleMessage(f);
          }
        } else {
          callback.receiveErrorsubmitSingleMessage(error);
        }
      }

      public void onFault(org.apache.axis2.context.MessageContext faultContext) {
        org.apache.axis2.AxisFault fault =
            org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
        onError(fault);
      }

      public void onComplete() {
        try {
          _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        } catch (org.apache.axis2.AxisFault axisFault) {
          callback.receiveErrorsubmitSingleMessage(axisFault);
        }
      }
    });

    org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
    if (_operations[0].getMessageReceiver() == null
        && _operationClient.getOptions().isUseSeparateListener()) {
      _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
      _operations[0].setMessageReceiver(_callbackReceiver);
    }

    // execute the operation client
    _operationClient.execute(false);

  }

  /**
   * Auto generated method signature the connectivity test
   * 
   * @see org.immregistries.smm.tester.connectors.tlep.Client_Service#connectivityTest
   * @param connectivityTest6
   * 
   * @throws org.immregistries.smm.tester.connectors.tlep.UnsupportedOperationFault_Message :
   * @throws org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message :
   */

  public org.immregistries.smm.tester.connectors.tlep.ConnectivityTestResponse connectivityTest(

      org.immregistries.smm.tester.connectors.tlep.ConnectivityTest connectivityTest6)

      throws java.rmi.RemoteException

      , org.immregistries.smm.tester.connectors.tlep.UnsupportedOperationFault_Message,
      org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message {
    org.apache.axis2.context.MessageContext _messageContext = null;
    try {
      org.apache.axis2.client.OperationClient _operationClient =
          _serviceClient.createClient(_operations[1].getName());
      _operationClient.getOptions().setAction("urn:cdc:iisb:2011:connectivityTest");
      _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

      addPropertyToOperationClient(_operationClient,
          org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

      // create a message context
      _messageContext = new org.apache.axis2.context.MessageContext();

      // create SOAP envelope with that payload
      org.apache.axiom.soap.SOAPEnvelope env = null;

      env = toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()),
          connectivityTest6,
          optimizeContent(new javax.xml.namespace.QName("urn:cdc:iisb:2011", "connectivityTest")),
          new javax.xml.namespace.QName("urn:cdc:iisb:2011", "connectivityTest"));

      // adding SOAP soap_headers
      _serviceClient.addHeadersToEnvelope(env);
      // set the message context with that soap envelope
      _messageContext.setEnvelope(env);

      // add the message contxt to the operation client
      _operationClient.addMessageContext(_messageContext);

      // execute the operation client
      _operationClient.execute(true);

      org.apache.axis2.context.MessageContext _returnMessageContext = _operationClient
          .getMessageContext(org.apache.axis2.wsdl.WSDLConstants.MESSAGE_LABEL_IN_VALUE);
      org.apache.axiom.soap.SOAPEnvelope _returnEnv = _returnMessageContext.getEnvelope();

      java.lang.Object object = fromOM(_returnEnv.getBody().getFirstElement(),
          org.immregistries.smm.tester.connectors.tlep.ConnectivityTestResponse.class,
          getEnvelopeNamespaces(_returnEnv));

      return (org.immregistries.smm.tester.connectors.tlep.ConnectivityTestResponse) object;

    } catch (org.apache.axis2.AxisFault f) {

      org.apache.axiom.om.OMElement faultElt = f.getDetail();
      if (faultElt != null) {
        if (faultExceptionNameMap.containsKey(
            new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "connectivityTest"))) {
          // make the fault by reflection
          try {
            java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap.get(
                new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "connectivityTest"));
            java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
            java.lang.reflect.Constructor constructor = exceptionClass.getConstructor(String.class);
            java.lang.Exception ex = (java.lang.Exception) constructor.newInstance(f.getMessage());
            // message class
            java.lang.String messageClassName = (java.lang.String) faultMessageMap.get(
                new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "connectivityTest"));
            java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
            java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
            java.lang.reflect.Method m =
                exceptionClass.getMethod("setFaultMessage", new java.lang.Class[] {messageClass});
            m.invoke(ex, new java.lang.Object[] {messageObject});

            if (ex instanceof org.immregistries.smm.tester.connectors.tlep.UnsupportedOperationFault_Message) {
              throw (org.immregistries.smm.tester.connectors.tlep.UnsupportedOperationFault_Message) ex;
            }

            if (ex instanceof org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message) {
              throw (org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message) ex;
            }

            throw new java.rmi.RemoteException(ex.getMessage(), ex);
          } catch (java.lang.ClassCastException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          } catch (java.lang.ClassNotFoundException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          } catch (java.lang.NoSuchMethodException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          } catch (java.lang.reflect.InvocationTargetException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          } catch (java.lang.IllegalAccessException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          } catch (java.lang.InstantiationException e) {
            // we cannot intantiate the class - throw the original Axis fault
            throw f;
          }
        } else {
          throw f;
        }
      } else {
        throw f;
      }
    } finally {
      if (_messageContext.getTransportOut() != null) {
        _messageContext.getTransportOut().getSender().cleanup(_messageContext);
      }
    }
  }

  /**
   * Auto generated method signature for Asynchronous Invocations the connectivity test
   * 
   * @see org.immregistries.smm.tester.connectors.tlep.Client_Service#startconnectivityTest
   * @param connectivityTest6
   */
  public void startconnectivityTest(

      org.immregistries.smm.tester.connectors.tlep.ConnectivityTest connectivityTest6,

      final org.immregistries.smm.tester.connectors.tlep.Client_ServiceCallbackHandler callback)

      throws java.rmi.RemoteException {

    org.apache.axis2.client.OperationClient _operationClient =
        _serviceClient.createClient(_operations[1].getName());
    _operationClient.getOptions().setAction("urn:cdc:iisb:2011:connectivityTest");
    _operationClient.getOptions().setExceptionToBeThrownOnSOAPFault(true);

    addPropertyToOperationClient(_operationClient,
        org.apache.axis2.description.WSDL2Constants.ATTR_WHTTP_QUERY_PARAMETER_SEPARATOR, "&");

    // create SOAP envelope with that payload
    org.apache.axiom.soap.SOAPEnvelope env = null;
    final org.apache.axis2.context.MessageContext _messageContext =
        new org.apache.axis2.context.MessageContext();

    // Style is Doc.

    env =
        toEnvelope(getFactory(_operationClient.getOptions().getSoapVersionURI()), connectivityTest6,
            optimizeContent(new javax.xml.namespace.QName("urn:cdc:iisb:2011", "connectivityTest")),
            new javax.xml.namespace.QName("urn:cdc:iisb:2011", "connectivityTest"));

    // adding SOAP soap_headers
    _serviceClient.addHeadersToEnvelope(env);
    // create message context with that soap envelope
    _messageContext.setEnvelope(env);

    // add the message context to the operation client
    _operationClient.addMessageContext(_messageContext);

    _operationClient.setCallback(new org.apache.axis2.client.async.AxisCallback() {
      public void onMessage(org.apache.axis2.context.MessageContext resultContext) {
        try {
          org.apache.axiom.soap.SOAPEnvelope resultEnv = resultContext.getEnvelope();

          java.lang.Object object = fromOM(resultEnv.getBody().getFirstElement(),
              org.immregistries.smm.tester.connectors.tlep.ConnectivityTestResponse.class,
              getEnvelopeNamespaces(resultEnv));
          callback.receiveResultconnectivityTest(
              (org.immregistries.smm.tester.connectors.tlep.ConnectivityTestResponse) object);

        } catch (org.apache.axis2.AxisFault e) {
          callback.receiveErrorconnectivityTest(e);
        }
      }

      public void onError(java.lang.Exception error) {
        if (error instanceof org.apache.axis2.AxisFault) {
          org.apache.axis2.AxisFault f = (org.apache.axis2.AxisFault) error;
          org.apache.axiom.om.OMElement faultElt = f.getDetail();
          if (faultElt != null) {
            if (faultExceptionNameMap.containsKey(
                new org.apache.axis2.client.FaultMapKey(faultElt.getQName(), "connectivityTest"))) {
              // make the fault by reflection
              try {
                java.lang.String exceptionClassName = (java.lang.String) faultExceptionClassNameMap
                    .get(new org.apache.axis2.client.FaultMapKey(faultElt.getQName(),
                        "connectivityTest"));
                java.lang.Class exceptionClass = java.lang.Class.forName(exceptionClassName);
                java.lang.reflect.Constructor constructor =
                    exceptionClass.getConstructor(String.class);
                java.lang.Exception ex =
                    (java.lang.Exception) constructor.newInstance(f.getMessage());
                // message class
                java.lang.String messageClassName =
                    (java.lang.String) faultMessageMap.get(new org.apache.axis2.client.FaultMapKey(
                        faultElt.getQName(), "connectivityTest"));
                java.lang.Class messageClass = java.lang.Class.forName(messageClassName);
                java.lang.Object messageObject = fromOM(faultElt, messageClass, null);
                java.lang.reflect.Method m = exceptionClass.getMethod("setFaultMessage",
                    new java.lang.Class[] {messageClass});
                m.invoke(ex, new java.lang.Object[] {messageObject});

                if (ex instanceof org.immregistries.smm.tester.connectors.tlep.UnsupportedOperationFault_Message) {
                  callback.receiveErrorconnectivityTest(
                      (org.immregistries.smm.tester.connectors.tlep.UnsupportedOperationFault_Message) ex);
                  return;
                }

                if (ex instanceof org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message) {
                  callback.receiveErrorconnectivityTest(
                      (org.immregistries.smm.tester.connectors.tlep.UnknownFault_Message) ex);
                  return;
                }

                callback.receiveErrorconnectivityTest(
                    new java.rmi.RemoteException(ex.getMessage(), ex));
              } catch (java.lang.ClassCastException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorconnectivityTest(f);
              } catch (java.lang.ClassNotFoundException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorconnectivityTest(f);
              } catch (java.lang.NoSuchMethodException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorconnectivityTest(f);
              } catch (java.lang.reflect.InvocationTargetException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorconnectivityTest(f);
              } catch (java.lang.IllegalAccessException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorconnectivityTest(f);
              } catch (java.lang.InstantiationException e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorconnectivityTest(f);
              } catch (org.apache.axis2.AxisFault e) {
                // we cannot intantiate the class - throw the original Axis
                // fault
                callback.receiveErrorconnectivityTest(f);
              }
            } else {
              callback.receiveErrorconnectivityTest(f);
            }
          } else {
            callback.receiveErrorconnectivityTest(f);
          }
        } else {
          callback.receiveErrorconnectivityTest(error);
        }
      }

      public void onFault(org.apache.axis2.context.MessageContext faultContext) {
        org.apache.axis2.AxisFault fault =
            org.apache.axis2.util.Utils.getInboundFaultFromMessageContext(faultContext);
        onError(fault);
      }

      public void onComplete() {
        try {
          _messageContext.getTransportOut().getSender().cleanup(_messageContext);
        } catch (org.apache.axis2.AxisFault axisFault) {
          callback.receiveErrorconnectivityTest(axisFault);
        }
      }
    });

    org.apache.axis2.util.CallbackReceiver _callbackReceiver = null;
    if (_operations[1].getMessageReceiver() == null
        && _operationClient.getOptions().isUseSeparateListener()) {
      _callbackReceiver = new org.apache.axis2.util.CallbackReceiver();
      _operations[1].setMessageReceiver(_callbackReceiver);
    }

    // execute the operation client
    _operationClient.execute(false);

  }

  /**
   * A utility method that copies the namepaces from the SOAPEnvelope
   */
  private java.util.Map getEnvelopeNamespaces(org.apache.axiom.soap.SOAPEnvelope env) {
    java.util.Map returnMap = new java.util.HashMap();
    java.util.Iterator namespaceIterator = env.getAllDeclaredNamespaces();
    while (namespaceIterator.hasNext()) {
      org.apache.axiom.om.OMNamespace ns =
          (org.apache.axiom.om.OMNamespace) namespaceIterator.next();
      returnMap.put(ns.getPrefix(), ns.getNamespaceURI());
    }
    return returnMap;
  }

  // //////////////////////////////////////////////////////////////////////

  private static org.apache.neethi.Policy getPolicy(java.lang.String policyString) {
    return org.apache.neethi.PolicyEngine.getPolicy(org.apache.axiom.om.OMXMLBuilderFactory
        .createOMBuilder(new java.io.StringReader(policyString)).getDocument()
        .getXMLStreamReader(false));
  }

  // ///////////////////////////////////////////////////////////////////////

  private javax.xml.namespace.QName[] opNameArray = null;

  private boolean optimizeContent(javax.xml.namespace.QName opName) {

    if (opNameArray == null) {
      return false;
    }
    for (int i = 0; i < opNameArray.length; i++) {
      if (opName.equals(opNameArray[i])) {
        return true;
      }
    }
    return false;
  }

  // http://192.168.1.2:8282/ImmMessagingServer/client_Service
  private org.apache.axiom.om.OMElement toOM(
      org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessage param,
      boolean optimizeContent) throws org.apache.axis2.AxisFault {

    try {
      return param.getOMElement(
          org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessage.MY_QNAME,
          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
    } catch (org.apache.axis2.databinding.ADBException e) {
      throw org.apache.axis2.AxisFault.makeFault(e);
    }

  }

  private org.apache.axiom.om.OMElement toOM(
      org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessageResponse param,
      boolean optimizeContent) throws org.apache.axis2.AxisFault {

    try {
      return param.getOMElement(
          org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessageResponse.MY_QNAME,
          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
    } catch (org.apache.axis2.databinding.ADBException e) {
      throw org.apache.axis2.AxisFault.makeFault(e);
    }

  }

  private org.apache.axiom.om.OMElement toOM(
      org.immregistries.smm.tester.connectors.tlep.Fault param, boolean optimizeContent)
      throws org.apache.axis2.AxisFault {

    try {
      return param.getOMElement(org.immregistries.smm.tester.connectors.tlep.Fault.MY_QNAME,
          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
    } catch (org.apache.axis2.databinding.ADBException e) {
      throw org.apache.axis2.AxisFault.makeFault(e);
    }

  }

  private org.apache.axiom.om.OMElement toOM(
      org.immregistries.smm.tester.connectors.tlep.SecurityFault param, boolean optimizeContent)
      throws org.apache.axis2.AxisFault {

    try {
      return param.getOMElement(org.immregistries.smm.tester.connectors.tlep.SecurityFault.MY_QNAME,
          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
    } catch (org.apache.axis2.databinding.ADBException e) {
      throw org.apache.axis2.AxisFault.makeFault(e);
    }

  }

  private org.apache.axiom.om.OMElement toOM(
      org.immregistries.smm.tester.connectors.tlep.MessageTooLargeFault param,
      boolean optimizeContent) throws org.apache.axis2.AxisFault {

    try {
      return param.getOMElement(
          org.immregistries.smm.tester.connectors.tlep.MessageTooLargeFault.MY_QNAME,
          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
    } catch (org.apache.axis2.databinding.ADBException e) {
      throw org.apache.axis2.AxisFault.makeFault(e);
    }

  }

  private org.apache.axiom.om.OMElement toOM(
      org.immregistries.smm.tester.connectors.tlep.ConnectivityTest param, boolean optimizeContent)
      throws org.apache.axis2.AxisFault {

    try {
      return param.getOMElement(
          org.immregistries.smm.tester.connectors.tlep.ConnectivityTest.MY_QNAME,
          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
    } catch (org.apache.axis2.databinding.ADBException e) {
      throw org.apache.axis2.AxisFault.makeFault(e);
    }

  }

  private org.apache.axiom.om.OMElement toOM(
      org.immregistries.smm.tester.connectors.tlep.ConnectivityTestResponse param,
      boolean optimizeContent) throws org.apache.axis2.AxisFault {

    try {
      return param.getOMElement(
          org.immregistries.smm.tester.connectors.tlep.ConnectivityTestResponse.MY_QNAME,
          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
    } catch (org.apache.axis2.databinding.ADBException e) {
      throw org.apache.axis2.AxisFault.makeFault(e);
    }

  }

  private org.apache.axiom.om.OMElement toOM(
      org.immregistries.smm.tester.connectors.tlep.UnsupportedOperationFault param,
      boolean optimizeContent) throws org.apache.axis2.AxisFault {

    try {
      return param.getOMElement(
          org.immregistries.smm.tester.connectors.tlep.UnsupportedOperationFault.MY_QNAME,
          org.apache.axiom.om.OMAbstractFactory.getOMFactory());
    } catch (org.apache.axis2.databinding.ADBException e) {
      throw org.apache.axis2.AxisFault.makeFault(e);
    }

  }

  private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
      org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessage param,
      boolean optimizeContent, javax.xml.namespace.QName methodQName)
      throws org.apache.axis2.AxisFault {

    try {

      org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
      emptyEnvelope.getBody().addChild(param.getOMElement(
          org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessage.MY_QNAME, factory));
      return emptyEnvelope;
    } catch (org.apache.axis2.databinding.ADBException e) {
      throw org.apache.axis2.AxisFault.makeFault(e);
    }

  }

  /* methods to provide back word compatibility */

  private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory,
      org.immregistries.smm.tester.connectors.tlep.ConnectivityTest param, boolean optimizeContent,
      javax.xml.namespace.QName methodQName) throws org.apache.axis2.AxisFault {

    try {

      org.apache.axiom.soap.SOAPEnvelope emptyEnvelope = factory.getDefaultEnvelope();
      emptyEnvelope.getBody().addChild(param.getOMElement(
          org.immregistries.smm.tester.connectors.tlep.ConnectivityTest.MY_QNAME, factory));
      return emptyEnvelope;
    } catch (org.apache.axis2.databinding.ADBException e) {
      throw org.apache.axis2.AxisFault.makeFault(e);
    }

  }

  /* methods to provide back word compatibility */

  /**
   * get the default envelope
   */
  private org.apache.axiom.soap.SOAPEnvelope toEnvelope(org.apache.axiom.soap.SOAPFactory factory) {
    return factory.getDefaultEnvelope();
  }

  private java.lang.Object fromOM(org.apache.axiom.om.OMElement param, java.lang.Class type,
      java.util.Map extraNamespaces) throws org.apache.axis2.AxisFault {

    try {

      if (org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessage.class.equals(type)) {

        return org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessage.Factory
            .parse(param.getXMLStreamReaderWithoutCaching());

      }

      if (org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessageResponse.class
          .equals(type)) {

        return org.immregistries.smm.tester.connectors.tlep.SubmitSingleMessageResponse.Factory
            .parse(param.getXMLStreamReaderWithoutCaching());

      }

      if (org.immregistries.smm.tester.connectors.tlep.Fault.class.equals(type)) {

        return org.immregistries.smm.tester.connectors.tlep.Fault.Factory
            .parse(param.getXMLStreamReaderWithoutCaching());

      }

      if (org.immregistries.smm.tester.connectors.tlep.SecurityFault.class.equals(type)) {

        return org.immregistries.smm.tester.connectors.tlep.SecurityFault.Factory
            .parse(param.getXMLStreamReaderWithoutCaching());

      }

      if (org.immregistries.smm.tester.connectors.tlep.MessageTooLargeFault.class.equals(type)) {

        return org.immregistries.smm.tester.connectors.tlep.MessageTooLargeFault.Factory
            .parse(param.getXMLStreamReaderWithoutCaching());

      }

      if (org.immregistries.smm.tester.connectors.tlep.ConnectivityTest.class.equals(type)) {

        return org.immregistries.smm.tester.connectors.tlep.ConnectivityTest.Factory
            .parse(param.getXMLStreamReaderWithoutCaching());

      }

      if (org.immregistries.smm.tester.connectors.tlep.ConnectivityTestResponse.class
          .equals(type)) {

        return org.immregistries.smm.tester.connectors.tlep.ConnectivityTestResponse.Factory
            .parse(param.getXMLStreamReaderWithoutCaching());

      }

      if (org.immregistries.smm.tester.connectors.tlep.UnsupportedOperationFault.class
          .equals(type)) {

        return org.immregistries.smm.tester.connectors.tlep.UnsupportedOperationFault.Factory
            .parse(param.getXMLStreamReaderWithoutCaching());

      }

      if (org.immregistries.smm.tester.connectors.tlep.Fault.class.equals(type)) {

        return org.immregistries.smm.tester.connectors.tlep.Fault.Factory
            .parse(param.getXMLStreamReaderWithoutCaching());

      }

    } catch (java.lang.Exception e) {
      throw org.apache.axis2.AxisFault.makeFault(e);
    }
    return null;
  }

}
