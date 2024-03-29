/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.immregistries.smm.tester.run;

import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_FORECAST_STATUS_INCLUDED;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_FORECAST_STATUS_NOT_INCLUDED;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_ERROR;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_ERROR_Z33;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_ERROR_Z33_QUERY_REJECTED;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_LIST;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH_Z32;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MATCH_Z42;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_MULTIPLE_Z31_Z33;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND_OR_TOO_MANY;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_NOT_FOUND_Z33;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_PROFILE_ID_MISSING;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_PROFILE_ID_UNEXPECTED;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_TOO_MANY;
import static org.immregistries.smm.RecordServletInterface.VALUE_RESULT_QUERY_TYPE_UNEXPECTED_IIS_RESPONSE;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.immregistries.smm.mover.AckAnalyzer;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.manager.HL7Reader;
import org.immregistries.smm.tester.manager.forecast.ForecastTesterManager;
import org.immregistries.smm.tester.manager.nist.NISTValidator;
import org.immregistries.smm.tester.manager.nist.ValidationReport;
import org.immregistries.smm.tester.manager.nist.ValidationResource;
import org.immregistries.smm.transform.TestCaseMessage;
import org.immregistries.smm.transform.TestError;
import org.immregistries.smm.transform.Transform;
import org.immregistries.smm.transform.Transformer;

/**
 * 
 * @author nathan
 */
public class TestRunner {
  public static final String ASSERT_RESULT_ACCEPT_ACCEPT_OR_ERROR = "Accept or Error";
  public static final String ASSERT_RESULT_ACCEPT_ACCEPT_OR_REJECT = "Accept or Reject";
  public static final String ASSERT_RESULT_ACCEPT_REJECT = "Reject";
  public static final String ASSERT_RESULT_ACCEPT_ERROR = "Error";
  public static final String ASSERT_RESULT_ACCEPT_ACCEPT_AND_SKIP = "Accept and Skip";
  public static final String ASSERT_RESULT_ACCEPT_ACCEPT_AND_WARN = "Accept and Warn";
  public static final String ASSERT_RESULT_ACCEPT = "Accept";
  public static final String ASSERT_RESULT_ERROR_LOCATION_IS_ = "Error Location is ";
  public static final String ASSERT_RESULT_ERROR_INDICATED = "Error Indicated";
  public static final String ASSERT_RESULT_SEGMENT_RETURNED = "Segment Returned";
  public static final String ASSERT_RESULT_SEGMENT_NOT_RETURNED = "Segment Not Returned";

  public static final String ACTUAL_RESULT_STATUS_FAIL = "FAIL";
  public static final String ACTUAL_RESULT_STATUS_PASS = "PASS";

  protected String actualResponseMessage = null;
  private boolean passedTest = false;
  private String status = "";
  private List<TestError> errorList = null;
  private ErrorType errorType = ErrorType.UNKNOWN;
  private HL7Reader ackMessageReader;
  private long startTime = 0;
  private long endTime = 0;
  private boolean wasRun = false;
  private boolean validateResponse = false;
  private String testSectionType = "";

  public String getTestSectionType() {
    return testSectionType;
  }

  public void setTestSectionType(String testSectionType) {
    this.testSectionType = testSectionType;
  }

  public boolean isValidateResponse() {
    return validateResponse;
  }

  public void setValidateResponse(boolean validateResponse) {
    this.validateResponse = validateResponse;
  }

  public boolean isWasRun() {
    return wasRun;
  }

  public void setWasRun(boolean wasRun) {
    this.wasRun = wasRun;
  }

  public long getTotalRunTime() {
    return endTime - startTime;
  }

  public HL7Reader getAckMessageReader() {
    return ackMessageReader;
  }

  public ErrorType getErrorType() {
    return errorType;
  }

  public List<TestError> getErrorList() {
    return errorList;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getAckMessageText() {
    return actualResponseMessage;
  }

  public void setAckMessageText(String ack) {
    this.actualResponseMessage = ack;
  }

  public boolean isPassedTest() {
    return passedTest;
  }

  public void setPassedTest(boolean pass) {
    this.passedTest = pass;
  }

  public boolean runTest(Connector connector, TestCaseMessage testCaseMessage) throws Exception {
    testCaseMessage.setActualResponseMessage("");
    testCaseMessage.setPassedTest(false);
    testCaseMessage.setHasRun(false);

    String message = Transformer.transform(connector, testCaseMessage);
    wasRun = false;
    return runTest(connector, testCaseMessage, message);
  }

  public TestCaseMessage runTestIfNew(Connector connector, TestCaseMessage testCaseMessage,
      Map<String, TestCaseMessage> testCaseMessageMap) throws Exception {
    wasRun = false;
    testCaseMessage.setActualResponseMessage("");
    testCaseMessage.setPassedTest(false);
    testCaseMessage.setHasRun(false);
    String message = Transformer.transform(connector, testCaseMessage);
    if (testCaseMessageMap.containsKey(message)) {
      return testCaseMessageMap.get(message);
    }
    runTest(connector, testCaseMessage, message);
    testCaseMessageMap.put(message, testCaseMessage);
    return testCaseMessage;
  }

  public boolean runTest(Connector connector, TestCaseMessage testCaseMessage, String message)
      throws Exception {
    setupForRunTest(testCaseMessage, message);
    doRunTest(connector, testCaseMessage, message);
    testCaseMessage.setTotalRunTime(getTotalRunTime());
    evaluateRunTest(connector, testCaseMessage);
    if (validateResponse) {
      validateResponseWithNIST(testCaseMessage, actualResponseMessage);
    }
    return passedTest;
  }

  private boolean setPassFailForQuery(TestCaseMessage q) {
    String queryType = "";
    {
      HL7Reader responseReader = new HL7Reader(q.getActualResponseMessage());
      if (responseReader.advanceToSegment("MSH")) {
        String messageType = responseReader.getValue(9);
        q.log("  + MSH-9 Message Type: " + messageType);
        if (messageType.equals("VXR")) {
          queryType = VALUE_RESULT_QUERY_TYPE_MATCH;
        } else if (messageType.equals("VXX")) {
          queryType = VALUE_RESULT_QUERY_TYPE_LIST;
        } else if (messageType.equals("QCK")) {
          if (responseReader.advanceToSegment("QAK") && responseReader.getValue(2).equals("NF")) {
            q.log("  + QAK-2 Response Status: NF");
            queryType = VALUE_RESULT_QUERY_TYPE_NOT_FOUND;
          } else {
            q.log("  + No QAK segment found!");
            queryType = VALUE_RESULT_QUERY_TYPE_UNEXPECTED_IIS_RESPONSE;
          }
        } else if (messageType.equals("ACK")) {
          queryType = VALUE_RESULT_QUERY_TYPE_ERROR;
        } else if (messageType.equals("RSP")) {
          String profile = responseReader.getValue(21);
          q.log("  + MSH-21 Profile: " + profile);
          if (profile.equalsIgnoreCase("")) {
            queryType = VALUE_RESULT_QUERY_TYPE_PROFILE_ID_MISSING;
          } else if (profile.equalsIgnoreCase("Z32")) {
            queryType = VALUE_RESULT_QUERY_TYPE_MATCH_Z32;
          } else if (profile.equalsIgnoreCase("Z42")) {
            queryType = VALUE_RESULT_QUERY_TYPE_MATCH_Z42;
          } else if (profile.equalsIgnoreCase("Z31")) {
            queryType = VALUE_RESULT_QUERY_TYPE_LIST;
          } else if (profile.equalsIgnoreCase("Z33")) {
            if (responseReader.advanceToSegment("QAK")) {
              String responseStatus = responseReader.getValue(2);
              q.log("  + QAK-2 Response Status: " + responseStatus);
              if (responseStatus.equals("NF")) {
                queryType = VALUE_RESULT_QUERY_TYPE_NOT_FOUND_Z33;
              } else if (responseStatus.equals("TM")) {
                queryType = VALUE_RESULT_QUERY_TYPE_TOO_MANY;
              } else if (responseStatus.equals("AE")) {
                queryType = VALUE_RESULT_QUERY_TYPE_ERROR_Z33;
              } else if (responseStatus.equals("AR")) {
                queryType = VALUE_RESULT_QUERY_TYPE_ERROR_Z33_QUERY_REJECTED;
              } else {
                queryType = VALUE_RESULT_QUERY_TYPE_UNEXPECTED_IIS_RESPONSE;
              }
            } else {
              q.log("  + No QAK segment found!");
              queryType = VALUE_RESULT_QUERY_TYPE_UNEXPECTED_IIS_RESPONSE;
            }
          } else {
            queryType = VALUE_RESULT_QUERY_TYPE_PROFILE_ID_UNEXPECTED;
          }
        }
      } else {
        q.log("  + No MSH segment found!");
        if (actualResponseMessage.length() > 50) {
          q.log("    Actual message: '" + q.getActualResponseMessage().substring(0, 50) + "...'");
        } else {
          q.log("    Actual message: '" + q.getActualResponseMessage() + "'");
        }

        queryType = VALUE_RESULT_QUERY_TYPE_UNEXPECTED_IIS_RESPONSE;
      }
    }

    q.log("  + Query Type Detected: " + queryType);
    q.setActualResultQueryType(queryType);

    boolean passed = q.getAssertResult().equals(queryType);

    String ar = q.getAssertResult();
    if (ar.equalsIgnoreCase(VALUE_RESULT_QUERY_TYPE_MATCH)) {
      passed = queryType.equals(VALUE_RESULT_QUERY_TYPE_MATCH_Z32)
          || queryType.equals(VALUE_RESULT_QUERY_TYPE_MATCH_Z42);
    } else if (ar.equalsIgnoreCase(VALUE_RESULT_QUERY_TYPE_MATCH_Z32)) {
      passed = queryType.equals(VALUE_RESULT_QUERY_TYPE_MATCH_Z32);
    } else if (ar.equalsIgnoreCase(VALUE_RESULT_QUERY_TYPE_MATCH_Z42)) {
      passed = queryType.equals(VALUE_RESULT_QUERY_TYPE_MATCH_Z42);
    } else if (ar.equalsIgnoreCase(VALUE_RESULT_QUERY_TYPE_LIST)) {
      passed = queryType.equals(VALUE_RESULT_QUERY_TYPE_LIST);
    } else if (ar.equalsIgnoreCase(VALUE_RESULT_QUERY_TYPE_NOT_FOUND)
        || ar.equalsIgnoreCase(VALUE_RESULT_QUERY_TYPE_NOT_FOUND_Z33)) {
      passed = queryType.equals(VALUE_RESULT_QUERY_TYPE_NOT_FOUND_Z33);
    } else if (ar.equalsIgnoreCase(VALUE_RESULT_QUERY_TYPE_TOO_MANY)) {
      passed = queryType.equals(VALUE_RESULT_QUERY_TYPE_TOO_MANY);
    } else if (ar.equalsIgnoreCase(VALUE_RESULT_QUERY_TYPE_ERROR)) {
      passed = queryType.equals(VALUE_RESULT_QUERY_TYPE_ERROR);
    } else if (ar.equalsIgnoreCase(VALUE_RESULT_QUERY_TYPE_MULTIPLE_Z31_Z33)) {
      passed = queryType.equals(VALUE_RESULT_QUERY_TYPE_LIST)
          || queryType.equals(VALUE_RESULT_QUERY_TYPE_NOT_FOUND_Z33)
          || queryType.equals(VALUE_RESULT_QUERY_TYPE_TOO_MANY);
    } else if (ar.equalsIgnoreCase(VALUE_RESULT_QUERY_TYPE_NOT_FOUND_OR_TOO_MANY)) {
      passed = queryType.equals(VALUE_RESULT_QUERY_TYPE_TOO_MANY)
          || queryType.equals(VALUE_RESULT_QUERY_TYPE_NOT_FOUND_Z33);
    } else if (ar.equalsIgnoreCase(ASSERT_RESULT_SEGMENT_RETURNED)) {
      FindMatchingSegment findMatchingSegment = new FindMatchingSegment(q);
      if (findMatchingSegment.checkForMatch()) {
        passed = true;
      }
    } else if (ar.equalsIgnoreCase(ASSERT_RESULT_SEGMENT_NOT_RETURNED)) {
      FindMatchingSegment findMatchingSegment = new FindMatchingSegment(q);
      if (!findMatchingSegment.checkForMatch()) {
        passed = true;
      }
    }

    if (passed) {
      q.setActualResultStatus(TestRunner.ACTUAL_RESULT_STATUS_PASS);
      q.log("  + Test PASSED");
      return true;
    } else {
      q.setActualResultStatus(TestRunner.ACTUAL_RESULT_STATUS_FAIL);
      q.log("  + Test FAILED");
      return false;
    }
  }

  private static final int[][] ERROR_INDICATED_FIELDS_AND_COMPONENTS = new int[][] {{2, 1}, {2, 2},
      {2, 3}, {2, 4}, {2, 5}, {3, 1}, {5, 1}, {5, 3}, {5, 4}, {5, 6}, {8, 1}};

  protected void evaluateRunTest(Connector connector, TestCaseMessage testCaseMessage) {
    testCaseMessage.log("EVALUATING TEST RUN");
    errorList = new ArrayList<TestError>();
    String assertResult = testCaseMessage.getAssertResult();

    if (assertResult == null || assertResult.equals("")) {
      assertResult = ASSERT_RESULT_ACCEPT;
      testCaseMessage
          .log("  + Assert Result was empty !!!!! assuming message should have been accepted");
    }
    testCaseMessage.log("  + Assert Result = " + assertResult);
    testCaseMessage.log("  + Test Type     = " + testCaseMessage.getTestType());
    if (testCaseMessage.getTestType().equals("VXU")) {
      evaluateVXU(connector, testCaseMessage, assertResult);
    } else if (testCaseMessage.getTestType().equals("QBP")) {
      evaluateQBP(testCaseMessage);
    } else {
      testCaseMessage.log("  + Test Type '" + testCaseMessage.getTestType()
          + "' not recognized, unable to evaluate");
      passedTest = false;
    }
    testCaseMessage.setActualResponseMessage(actualResponseMessage);
    testCaseMessage.setPassedTest(passedTest);
    testCaseMessage.setHasRun(true);
    wasRun = true;

  }

  private void evaluateQBP(TestCaseMessage testCaseMessage) {
    passedTest = setPassFailForQuery(testCaseMessage);
    readForecastActual(testCaseMessage);
    if (testCaseMessage.getForecastActualList().size() > 0) {
      testCaseMessage.setResultForecastStatus(VALUE_RESULT_FORECAST_STATUS_INCLUDED);
    } else {
      testCaseMessage.setResultForecastStatus(VALUE_RESULT_FORECAST_STATUS_NOT_INCLUDED);
    }
  }

  private void evaluateVXU(Connector connector, TestCaseMessage testCaseMessage,
      String assertResult) {
    HL7Reader errorIndicatedReader = null;
    if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ERROR_INDICATED)) {
      String assertResultParameter = testCaseMessage.getAssertResultParameter();
      errorIndicatedReader = new HL7Reader(assertResultParameter);
      if (!errorIndicatedReader.advanceToSegment("ERR")) {
        errorIndicatedReader = null;
      } else {
        testCaseMessage
            .log("Will be looking for ERR segment that matches this one: " + assertResultParameter);
      }
    }
    FindMatchingSegment findMatchingSegment = null;
    if (assertResult.equalsIgnoreCase(ASSERT_RESULT_SEGMENT_RETURNED)) {
      findMatchingSegment = new FindMatchingSegment(testCaseMessage);
    } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_SEGMENT_NOT_RETURNED)) {
      findMatchingSegment = new FindMatchingSegment(testCaseMessage);
    }
    if (!assertResult.equalsIgnoreCase("")) {
      if (actualResponseMessage == null || actualResponseMessage.equals("")) {
        testCaseMessage.log("No acknowledgment was returned");
        if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT)) {
          passedTest = false;
        } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_AND_WARN)) {
          passedTest = false;
        } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_AND_SKIP)) {
          passedTest = false;
        } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ERROR)
            || assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_REJECT)) {
          passedTest = true;
        } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_OR_REJECT)
            || assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_OR_ERROR)) {
          passedTest = true;
        } else if (assertResult.toUpperCase()
            .startsWith(ASSERT_RESULT_ERROR_LOCATION_IS_.toUpperCase())) {
          passedTest = false;
        }
        testCaseMessage.log("  + Passed Test = " + passedTest);
      } else {
        ackMessageReader =
            AckAnalyzer.getMessageReader(actualResponseMessage, connector.getAckType());
        if (ackMessageReader != null) {
          testCaseMessage.setActualMessageResponseType(ackMessageReader.getValue(9));
        }
        {
          if (ackMessageReader != null || !connector.getAckType().isInHL7Format()) {
            testCaseMessage.log("Analyzing acknowledgement");
            AckAnalyzer ackAnalyzer = new AckAnalyzer(actualResponseMessage, connector.getAckType(),
                null, testCaseMessage);
            if (ackAnalyzer.isPositive()) {
              testCaseMessage.log("Positive acknowledgement detected");
            } else {
              testCaseMessage.log("Negative acknowledgement detected");
            }
            testCaseMessage.setAccepted(ackAnalyzer.isPositive());


            if (ackMessageReader != null && ackMessageReader.advanceToSegment("MSA")) {
              passedTest = false;
              testCaseMessage.setActualResultAckType(ackMessageReader.getValue(1));
              testCaseMessage.setActualResultAckMessage(ackMessageReader.getValue(2));
              boolean accepted = false;
              boolean rejected = false;
              boolean severitySet = false;
              errorType = ErrorType.UNKNOWN;

              if (testCaseMessage.getActualResultAckType().equals("AA")) {
                accepted = true;
              } else if (testCaseMessage.getActualResultAckType().equals("AR")) {
                rejected = true;
              }

              List<Transform> refList = null;
              boolean foundRef = false;
              if (assertResult.toUpperCase()
                  .startsWith(ASSERT_RESULT_ERROR_LOCATION_IS_.toUpperCase())) {
                String refString =
                    assertResult.substring(ASSERT_RESULT_ERROR_LOCATION_IS_.length()).trim();
                refList = new ArrayList<Transform>();
                String[] refs = refString.split("\\Qor\\E");
                for (String r : refs) {
                  Transform ref = null;
                  ref = Transformer.readHL7Reference(r.trim());
                  if (ref.getSegment().equals("") || ref.getField() <= 0) {
                    ref = null;
                  }
                  if (ref != null) {
                    refList.add(ref);
                  }
                }
              }

              while (ackMessageReader.advanceToSegment("ERR")) {
                String severity = ackMessageReader.getValue(4);
                String userMessage = ackMessageReader.getValue(8);
                TestError error = new TestError();
                errorList.add(error);

                if (!severity.equals("")) {
                  severitySet = true;
                }

                if (refList != null) {
                  String segmentName = ackMessageReader.getValue(2, 1);
                  String fieldPos = ackMessageReader.getValue(2, 3);
                  for (Transform ref : refList) {
                    if (ref.getSegment().equalsIgnoreCase(segmentName)
                        && fieldPos.equals("" + ref.getField())) {
                      foundRef = true;
                      break;
                    }
                  }
                }

                if (severity.equals("E")) {
                  rejected = true;
                  error.setErrorType(ErrorType.ERROR);
                } else if (severity.equals("W")) {
                  error.setErrorType(ErrorType.WARNING);
                } else if (severity.equals("I")) {
                  error.setErrorType(ErrorType.INFORMATION);
                } else {
                  error.setErrorType(ErrorType.UNKNOWN);
                }
                error.setDescription(userMessage);
                if (errorIndicatedReader != null) {
                  boolean allMatches = true;
                  for (int[] fieldAndComponent : ERROR_INDICATED_FIELDS_AND_COMPONENTS) {
                    int field = fieldAndComponent[0];
                    int component = fieldAndComponent[1];
                    boolean matches = checkMatches(errorIndicatedReader, field, component);
                    if (!matches) {
                      allMatches = false;
                      break;
                    }
                  }
                  if (allMatches) {
                    passedTest = true;
                  }
                }

              }
              if (testCaseMessage.getActualResultAckType().equals("AE") && !rejected) {
                if (severitySet) {
                  accepted = true;
                  rejected = false;
                } else {
                  accepted = false;
                  rejected = true;
                }
              }

              if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT)) {
                passedTest = ackAnalyzer.isPositive();
              } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_AND_WARN)) {
                passedTest = !rejected;
              } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_AND_SKIP)) {
                passedTest = !rejected;
              } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ERROR)
                  || assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_REJECT)) {
                passedTest = !ackAnalyzer.isPositive();
              } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_OR_REJECT)
                  || assertResult.equalsIgnoreCase(ASSERT_RESULT_ACCEPT_ACCEPT_OR_ERROR)) {
                passedTest = true;
              } else if (assertResult.toUpperCase()
                  .startsWith(ASSERT_RESULT_ERROR_LOCATION_IS_.toUpperCase())) {
                if (refList == null) {
                  passedTest = false;
                } else {
                  passedTest = foundRef;
                }
              }
              if (errorType == ErrorType.UNKNOWN) {
                if (accepted) {
                  errorType = ErrorType.ACCEPT;
                } else if (rejected) {
                  errorType = ErrorType.ERROR;
                }
              }
            }
          }
        }
        if (findMatchingSegment != null) {
          if (assertResult.equalsIgnoreCase(ASSERT_RESULT_SEGMENT_RETURNED)) {
            if (findMatchingSegment.checkForMatch()) {
              passedTest = true;
            }
          } else if (assertResult.equalsIgnoreCase(ASSERT_RESULT_SEGMENT_NOT_RETURNED)) {
            if (!findMatchingSegment.checkForMatch()) {
              passedTest = true;
            }
          }
        }

        if (passedTest) {
          status = "A";
          testCaseMessage.setActualResultStatus(ACTUAL_RESULT_STATUS_PASS);
          for (TestError error : errorList) {
            if (status.equals("A") && error.getErrorType() == ErrorType.WARNING) {
              // ignore skip warnings
              if (error.getErrorType() != ErrorType.INFORMATION) {
                status = "W";
              }
            }
          }
        } else {
          testCaseMessage.setActualResultStatus(ACTUAL_RESULT_STATUS_FAIL);
          status = "E";
        }
      }
    }
  }

  private void readForecastActual(TestCaseMessage queryTestCaseMessage) {
    ForecastTesterManager.readForecastActual(queryTestCaseMessage);
  }

  private boolean checkMatches(HL7Reader errorIndicatedReader, int field, int component) {
    boolean matches = false;
    String indicatedValue = errorIndicatedReader.getValue(field, component);
    String actualValue = ackMessageReader.getValue(field, component);
    if (indicatedValue.equals("") || indicatedValue.equalsIgnoreCase(actualValue)) {
      matches = true;
    }
    return matches;
  }

  private void doRunTest(Connector connector, TestCaseMessage testCaseMessage, String message)
      throws Exception {
    startTime = System.currentTimeMillis();
    testCaseMessage.setActualRequestMessage(message);
    actualResponseMessage = connector.submitMessage(message, false);
    actualResponseMessage = cleanMessage(actualResponseMessage);
    endTime = System.currentTimeMillis();
    testCaseMessage.setActualResponseMessage(actualResponseMessage);
  }

  private String cleanMessage(String message) {
    if (message == null) {
      return "";
    }
    while (message.length() > 0 && message.charAt(0) <= ' ') {
      message = message.substring(1);
    }
    return message;
  }

  private void setupForRunTest(TestCaseMessage testCaseMessage, String message) {
    wasRun = false;
    passedTest = false;
    actualResponseMessage = null;
    testCaseMessage.setMessageTextSent(message);
  }

  public static void validateResponseWithNIST(TestCaseMessage testCaseMessage, String messageText) {
    ascertainValidationResource(testCaseMessage, messageText);
    if (testCaseMessage.getValidationResource() != null) {
      ValidationReport validationReport =
          NISTValidator.validate(messageText, testCaseMessage.getValidationResource());
      testCaseMessage.setValidationReport(validationReport);
      if (validationReport != null) {
        testCaseMessage.setValidationReportPass(
            validationReport.getHeaderReport().getValidationStatus().equals("Complete")
                && validationReport.getHeaderReport().getErrorCount() == 0);
      }
    }
  }

  public static void ascertainValidationResource(TestCaseMessage testCaseMessage,
      String messageText) {
    ValidationResource validationResource = null;
    HL7Reader hl7Reader = new HL7Reader(messageText);
    if (hl7Reader.advanceToSegment("MSH")) {
      String messageType = hl7Reader.getValue(9);
      String profileId = hl7Reader.getValue(21);
      if (profileId.equals("Z31") && messageType.equals("RSP")) {
        validationResource = ValidationResource.IZ_RSP_Z31;
      } else if (profileId.equals("Z32") && messageType.equals("RSP")) {
        validationResource = ValidationResource.IZ_RSP_Z32;
      } else if (profileId.equals("Z42") && messageType.equals("RSP")) {
        validationResource = ValidationResource.IZ_RSP_Z42;
      } else if (profileId.equals("Z33") && messageType.equals("RSP")) {
        validationResource = ValidationResource.IZ_RSP_Z33;
      } else if (profileId.equals("Z34") && messageType.equals("QBP")) {
        validationResource = ValidationResource.IZ_QBP_Z34;
      } else if (profileId.equals("Z44") && messageType.equals("QBP")) {
        validationResource = ValidationResource.IZ_QBP_Z44;
      } else if (profileId.equals("Z22") && messageType.equals("VXU")) {
        validationResource = ValidationResource.IZ_VXU_Z22;
      } else if (profileId.equals("") && messageType.equals("VXU")) {
        validationResource = ValidationResource.IZ_VXU;
      } else if (profileId.equals("Z23") || messageType.equals("ACK")) {
        validationResource = ValidationResource.IZ_ACK_FOR_AIRA;
        // validationResource = ValidationResource.IZ_ACK_Z23;
      }
    }
    testCaseMessage.setValidationResource(validationResource);
  }

}
