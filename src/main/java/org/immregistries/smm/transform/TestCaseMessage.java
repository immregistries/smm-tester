/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.immregistries.smm.transform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;
import org.immregistries.smm.tester.manager.forecast.EvaluationActual;
import org.immregistries.smm.tester.manager.forecast.ForecastActual;
import org.immregistries.smm.tester.manager.nist.ValidationReport;
import org.immregistries.smm.tester.manager.nist.ValidationResource;
import org.immregistries.smm.transform.forecast.ForecastTestCase;
import org.immregistries.smm.transform.forecast.ForecastTestPanel;

/**
 * 
 * @author nathan
 */
public class TestCaseMessage {

  public static final String TEST_CASE_SET = "Test Case Set:";
  public static final String TEST_CASE_NUMBER = "Test Case Number:";
  public static final String DESCRIPTION = "Description:";
  public static final String FIELD_NAME = "Field Name:";
  public static final String EXPECTED_RESULT = "Expected Result:";
  public static final String ASSERT_RESULT = "Assert Result:";
  public static final String ASSERT_RESULT_PARAMETER = "Assert Result Parameter:";
  public static final String MESSAGE_TYPE = "Message Type:";
  public static final String DERIVED_FROM_TEST_CASE_NUMBER = "Derived From Test Case Number:";
  public static final String ORIGINAL_TEST_CASE_NUMBER = "Original Test Case Number:";
  public static final String ORIGINAL_MESSAGE = "Original Message:";
  public static final String ACTUAL_RESPONSE_MESSAGE = "Actual Response Message:";
  public static final String DERIVED_FROM_VXU_MESSAGE = "Derived From VXU Message:";
  public static final String QUICK_TRANSFORMATIONS = "Quick Transformations:";
  public static final String CUSTOM_TRANSFORMATIONS = "Custom Transformations:";
  public static final String ADDITIONAL_TRANSFORMATIONS = "Additional Transformations:";
  public static final String EXCLUDE_TRANSFORMATIONS = "Exclude Transformations:";
  public static final String CAUSE_ISSUES = "Cause Issues:";
  public static final String COMMENT = "Comment:";
  public static final String PATIENT_TYPE = "Patient Type:";
  public static final String SCENARIO = "Scenario:";
  public static final String TEST_TYPE = "Test Type:";
  public static final String TEST_CASE_MODE = "Test Case Mode:";

  public static void main(String[] args) {
    for (int i = 0; i < args.length; i++) {
      System.out.println((i + 1) + ". Add one: " + addOne(args[1]));
    }
  }

  protected static String addOne(String s) {
    String result = "";
    if (s.equals("")) {
      return "1";
    }
    int carry = 1;
    for (int i = s.length() - 1; i >= 0; i--) {
      char c = s.charAt(i);
      if (c < '0' || c > '9') {
        result = s.substring(0, i) + carry + result;
        carry = 0;
        break;
      }
      int num = (c - '0') + carry;
      if (num >= 10) {
        carry = num / 10;
        num = num - (carry * 10);
      } else {
        carry = 0;
      }
      result = num + result;
    }
    if (carry > 0) {
      result = carry + result;
    }
    return result;
  }

  protected static void addTestMessageToList(List<TestCaseMessage> testCaseMessageList,
      TestCaseMessage testCaseMessage) {
    if (testCaseMessage.getMessageText().startsWith("MSH|TRANSFORM")) {
      Transformer transformer = new Transformer();
      transformer.transform(testCaseMessage);
    }
    if (testCaseMessage.getOriginalMessage().equals("")) {
      testCaseMessage.setOriginalMessage(testCaseMessage.getMessageText());
    }

    for (int i = 0; i < testCaseMessageList.size(); i++) {
      if (testCaseMessage.getTestCaseNumber()
          .equals(testCaseMessageList.get(i).getTestCaseNumber())) {
        testCaseMessageList.get(i).merge(testCaseMessage);
        testCaseMessage = null;
        break;
      }
    }
    if (testCaseMessage != null) {
      testCaseMessageList.add(testCaseMessage);
    }
  }

  private String testCaseSet = "";
  private String testCaseNumber = "";
  private String testCaseNumberOriginal = "";
  private String testCaseCategoryId = "";
  private String description = "";
  private String expectedResult = "";
  private String messageText = "";
  private String messageTextSent = "";
  private String assertResult = "";
  private String assertResultParameter = "";
  private String derivedFromTestCaseNumber = "";
  private String fieldName = "";
  private String originalTestCaseNumber = "";
  private String originalMessage = "";
  private String originalMessageResponse = "";
  private String preparedMessage = null;
  private String[] quickTransformations = new String[] {};
  private String quickTransformationsConverted = "";
  private String customTransformations = "";
  private String additionalTransformations = "";
  private String excludeTransformations = "";
  private String causeIssueTransforms = "";
  private String causeIssues = "";
  private List<Comment> comments = new ArrayList<Comment>();
  private String actualResultStatus = "";
  private String actualResultAckType = "";
  private String actualResultQueryType = "";
  private String actualResultAckMessage = "";
  private String actualMessageResponseType = "";
  private PatientType patientType = PatientType.ANY_CHILD;
  private boolean hasIssue = false;
  private Throwable exception = null;
  private String actualRequestMessage = "";
  private String actualResponseMessage = "";
  private List<TestError> errorList = null;
  private String derivedFromVXUMessage = "";
  private String derivedFromTestCaseCategoryId = "";
  private List<Comparison> comparisonList = null;
  private boolean passedTest = false;
  private boolean accepted = false;
  private boolean majorChangesMade = false;
  private boolean hasRun = false;
  private int testCaseId = 0;
  private boolean resultNotExpectedToConform = false;
  private ForecastTestCase forecastTestCase = null;
  private ForecastTestPanel forecastTestPanel = null;
  private String scenario = "";
  private String messageAcceptStatusDebug = "";
  private int testPosition = 0;
  private String testType = "";
  private ValidationReport validationReport = null;
  private String validationProblem = "";
  private ValidationResource validationResource = null;
  private boolean validationReportPass = false;
  private boolean originalAccepted = false;
  private String resultForecastStatus = "";
  private TestCaseMessage updateTestCaseMessage = null;
  private boolean doNotQueryFor = false;
  private boolean global = false;
  private String lineEnding = "\r";
  private String tchForecastTesterUserName = null;
  private String tchForecastTesterPassword = null;
  private String tchForecastTesterTestPanelLabel = null;
  private String tchForecastTesterTestCaseNumber = null;
  private String patientDob = null;
  private String patientSex = null;
  private TestCaseMode testCaseMode = TestCaseMode.DEFAULT;
  private Map<String, TestCaseMessage> testCaseMessageMap = null;
  private long totalRunTime = 0;
  private StringBuilder log = new StringBuilder();
  private String scenarioTransforms = null;
  private Map<String, String> variables = new HashMap<>();

  public String getOriginalTestCaseNumber() {
    return originalTestCaseNumber;
  }

  public void setOriginalTestCaseNumber(String originalTestCaseNumber) {
    this.originalTestCaseNumber = originalTestCaseNumber;
  }

  public void log(String s) {
    log.append(s);
    log.append("\n");
  }

  public String getLog() {
    return log.toString();
  }

  public String getValidationProblem() {
    return validationProblem;
  }

  public void setValidationProblem(String validationProblem) {
    this.validationProblem = validationProblem;
  }

  public long getTotalRunTime() {
    return totalRunTime;
  }

  public void setTotalRunTime(long totalRunTime) {
    this.totalRunTime = totalRunTime;
  }

  public String getTestCaseNumberOriginal() {
    return testCaseNumberOriginal;
  }

  public void setTestCaseNumberOriginal(String testCaseNumberOriginal) {
    this.testCaseNumberOriginal = testCaseNumberOriginal;
  }

  public String getDerivedFromTestCaseNumber() {
    return derivedFromTestCaseNumber;
  }

  public void setDerivedFromTestCaseNumber(String derivedFromTestCaseNumber) {
    this.derivedFromTestCaseNumber = derivedFromTestCaseNumber;
  }

  public Map<String, TestCaseMessage> getTestCaseMessageMap() {
    return testCaseMessageMap;
  }

  public Map<String, String> getVariables() {
    return variables;
  }

  public TestCaseMode getTestCaseMode() {
    return testCaseMode;
  }

  public void setTestCaseMode(TestCaseMode testCaseMode) {
    this.testCaseMode = testCaseMode;
  }

  public String getTchForecastTesterPassword() {
    return tchForecastTesterPassword;
  }

  public void setTchForecastTesterPassword(String tchForecastTesterPassword) {
    this.tchForecastTesterPassword = tchForecastTesterPassword;
  }

  public String getPatientDob() {
    return patientDob;
  }

  public void setPatientDob(String patientDob) {
    this.patientDob = patientDob;
  }

  public String getPatientSex() {
    return patientSex;
  }

  public void setPatientSex(String patientSex) {
    this.patientSex = patientSex;
  }

  public String getTchForecastTesterUserName() {
    return tchForecastTesterUserName;
  }

  public void setTchForecastTesterUserName(String tchForecastTesterUserName) {
    this.tchForecastTesterUserName = tchForecastTesterUserName;
  }

  public String getTchForecastTesterTestPanelLabel() {
    return tchForecastTesterTestPanelLabel;
  }

  public void setTchForecastTesterTestPanelLabel(String tchForecastTesterTestPanelLabel) {
    this.tchForecastTesterTestPanelLabel = tchForecastTesterTestPanelLabel;
  }

  public String getTchForecastTesterTestCaseNumber() {
    return tchForecastTesterTestCaseNumber;
  }

  public void setTchForecastTesterTestCaseNumber(String tchForecastTesterTestCaseNumber) {
    this.tchForecastTesterTestCaseNumber = tchForecastTesterTestCaseNumber;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getLineEnding() {
    return lineEnding;
  }

  public void setLineEnding(String lineEnding) {
    this.lineEnding = lineEnding;
  }

  public boolean isGlobal() {
    return global;
  }

  public void setGlobal(boolean global) {
    this.global = global;
  }

  public TestCaseMessage getUpdateTestCaseMessage() {
    return updateTestCaseMessage;
  }

  public void setUpdateTestCaseMessage(TestCaseMessage updateTestCaseMessage) {
    this.updateTestCaseMessage = updateTestCaseMessage;
  }

  public boolean isDoNotQueryFor() {
    return doNotQueryFor;
  }

  public void setDoNotQueryFor(boolean doNotQueryFor) {
    this.doNotQueryFor = doNotQueryFor;
  }

  public String getResultForecastStatus() {
    return resultForecastStatus;
  }

  public void setResultForecastStatus(String resultForecastStatus) {
    this.resultForecastStatus = resultForecastStatus;
  }

  private List<ForecastActual> forecastActualList = null;
  private List<EvaluationActual> evaluationActualList = null;

  public List<ForecastActual> getForecastActualList() {
    return forecastActualList;
  }

  public void setForecastActualList(List<ForecastActual> forecastActualList) {
    this.forecastActualList = forecastActualList;
  }

  public List<EvaluationActual> getEvaluationActualList() {
    return evaluationActualList;
  }

  public void setEvaluationActualList(List<EvaluationActual> evaluationActualList) {
    this.evaluationActualList = evaluationActualList;
  }

  public String getActualResultQueryType() {
    return actualResultQueryType;
  }

  public void setActualResultQueryType(String actualResultQueryType) {
    this.actualResultQueryType = actualResultQueryType;
  }

  public String getExcludeTransformations() {
    return excludeTransformations;
  }

  public void setExcludeTransformations(String ignoreTransformations) {
    this.excludeTransformations = ignoreTransformations;
  }

  public boolean isOriginalAccepted() {
    return originalAccepted;
  }

  public void setOriginalAccepted(boolean originalAccepted) {
    this.originalAccepted = originalAccepted;
  }

  public String getOriginalMessageResponse() {
    return originalMessageResponse;
  }

  public void setOriginalMessageResponse(String originalMessageAck) {
    this.originalMessageResponse = originalMessageAck;
  }

  public boolean isValidationReportPass() {
    return validationReportPass;
  }

  public void setValidationReportPass(boolean validationReportPass) {
    this.validationReportPass = validationReportPass;
  }

  public ValidationResource getValidationResource() {
    return validationResource;
  }

  public void setValidationResource(ValidationResource validationResource) {
    this.validationResource = validationResource;
  }

  public ValidationReport getValidationReport() {
    return validationReport;
  }

  public void setValidationReport(ValidationReport validationReport) {
    this.validationReport = validationReport;
  }

  public String getTestType() {
    return testType;
  }

  public void setTestType(String testType) {
    this.testType = testType;
  }

  public int getTestPosition() {
    return testPosition;
  }

  public void setTestPosition(int testPosition) {
    this.testPosition = testPosition;
  }

  public String getMessageAcceptStatusDebug() {
    return messageAcceptStatusDebug;
  }

  public void setMessageAcceptStatusDebug(String messageAcceptStatusDebug) {
    this.messageAcceptStatusDebug = messageAcceptStatusDebug;
  }

  public String getTestCaseCategoryId() {
    return testCaseCategoryId;
  }

  public void setTestCaseCategoryId(String testCaseCategoryId) {
    this.testCaseCategoryId = testCaseCategoryId;
  }

  public String getAdditionalTransformations() {
    return additionalTransformations;
  }

  public void setAdditionalTransformations(String additionalTransformations) {
    this.additionalTransformations = additionalTransformations;
  }

  public String getScenario() {
    return scenario;
  }

  public void setScenario(String scenario) {
    this.scenario = scenario;
  }

  public ForecastTestPanel getForecastTestPanel() {
    return forecastTestPanel;
  }

  public void setForecastTestPanel(ForecastTestPanel forecastTestPanel) {
    this.forecastTestPanel = forecastTestPanel;
  }

  public ForecastTestCase getForecastTestCase() {
    return forecastTestCase;
  }

  public void setForecastTestCase(ForecastTestCase forecastTestCase) {
    this.forecastTestCase = forecastTestCase;
  }

  public boolean isResultNotExpectedToConform() {
    return resultNotExpectedToConform;
  }

  public String getCauseIssueTransforms() {
    return causeIssueTransforms;
  }

  public void setCauseIssueTransforms(String causeIssueTransforms) {
    this.causeIssueTransforms = causeIssueTransforms;
  }

  public void setResultNotExpectedToConform(boolean resultNotExpectedToConform) {
    this.resultNotExpectedToConform = resultNotExpectedToConform;
  }

  public boolean isAccepted() {
    return accepted;
  }

  public void setAccepted(boolean accepted) {
    this.accepted = accepted;
  }

  public boolean isMajorChangesMade() {
    return majorChangesMade;
  }

  public void setMajorChangesMade(boolean majorChangesMade) {
    this.majorChangesMade = majorChangesMade;
  }

  public String getMessageTextSent() {
    return messageTextSent;
  }

  public void setMessageTextSent(String messageTextSent) {
    this.messageTextSent = messageTextSent;
  }

  public String getActualMessageResponseType() {
    return actualMessageResponseType;
  }

  public void setActualMessageResponseType(String actualMessageResponseType) {
    this.actualMessageResponseType = actualMessageResponseType;
  }

  public int getTestCaseId() {
    return testCaseId;
  }

  public void setTestCaseId(int testCaseId) {
    this.testCaseId = testCaseId;
  }

  public boolean isHasRun() {
    return hasRun;
  }

  public void setHasRun(boolean hasRun) {
    this.hasRun = hasRun;
  }

  public boolean isPassedTest() {
    return passedTest;
  }

  public void setPassedTest(boolean pass) {
    this.passedTest = pass;
  }

  public List<Comparison> getComparisonList() {
    return comparisonList;
  }

  public void setComparisonList(List<Comparison> comparisonList) {
    this.comparisonList = comparisonList;
  }

  public String getDerivedFromVXUMessage() {
    return derivedFromVXUMessage;
  }

  public void setDerivedFromVXUMessage(String derivedFromVXUText) {
    this.derivedFromVXUMessage = derivedFromVXUText;
  }

  public List<TestError> getErrorList() {
    return errorList;
  }

  public void setErrorList(List<TestError> errorList) {
    this.errorList = errorList;
  }

  public String getActualResponseMessage() {
    return actualResponseMessage;
  }

  public void setActualResponseMessage(String actualAck) {
    this.actualResponseMessage = actualAck;
  }

  public Throwable getException() {
    return exception;
  }

  public void setException(Throwable exception) {
    this.exception = exception;
  }

  public PatientType getPatientType() {
    return patientType;
  }

  public void setPatientType(PatientType patientType) {
    this.patientType = patientType;
  }

  public void setHasIssue(boolean hasIssue) {
    this.hasIssue = hasIssue;
  }

  public boolean hasIssue() {
    return hasIssue;
  }

  public TestCaseMessage() {
    // default;
  }

  public String getDerivedFromTestCaseCategoryId() {
    return derivedFromTestCaseCategoryId;
  }

  public void setDerivedFromTestCaseCategoryId(String derivedFromTestCaseCategoryId) {
    this.derivedFromTestCaseCategoryId = derivedFromTestCaseCategoryId;
  }

  public TestCaseMessage(TestCaseMessage copy) {
    this.testCaseSet = copy.testCaseSet;
    this.testCaseNumber = copy.testCaseNumber;
    this.testCaseNumberOriginal = copy.testCaseNumber;
    this.description = copy.description;
    this.fieldName = copy.fieldName;
    this.expectedResult = copy.expectedResult;
    this.messageText = copy.messageText;
    this.assertResult = copy.assertResult;
    this.assertResultParameter = copy.assertResultParameter;
    this.originalMessage = copy.originalMessage;
    this.quickTransformations = new String[copy.quickTransformations.length];
    System.arraycopy(copy.quickTransformations, 0, this.quickTransformations, 0,
        copy.quickTransformations.length);
    this.quickTransformationsConverted = copy.quickTransformationsConverted;
    this.excludeTransformations = copy.excludeTransformations;
    this.customTransformations = copy.customTransformations;
    this.additionalTransformations = copy.additionalTransformations;
    this.causeIssueTransforms = copy.causeIssueTransforms;
    this.causeIssues = copy.causeIssues;
    this.comments = new ArrayList<Comment>(copy.comments);
    this.actualResultStatus = copy.actualResultStatus;
    this.actualResultAckType = copy.actualResultAckType;
    this.actualResultAckMessage = copy.actualResultAckMessage;
    this.patientType = copy.patientType;
    this.actualResponseMessage = copy.actualResponseMessage;
    this.scenario = copy.scenario;
    this.testType = copy.testType;
    this.derivedFromTestCaseNumber = copy.derivedFromTestCaseNumber;
  }

  public void merge(TestCaseMessage updated) {
    if (!updated.getMessageText().equals("")) {
      messageText = updated.getMessageText();
    }
    if (!updated.getTestCaseSet().equals("")) {
      testCaseSet = updated.getTestCaseSet();
    }
    if (!updated.getDescription().equals("")) {
      description = updated.getDescription();
    }
    if (updated.getPatientType() != PatientType.ANY_CHILD) {
      patientType = updated.getPatientType();
    }
    if (!updated.getExpectedResult().equals("")) {
      expectedResult = updated.getExpectedResult();
    }
    if (!updated.getOriginalMessage().equals("")) {
      originalMessage = updated.getOriginalMessage();
    }
    if (updated.getQuickTransformations().length > 0
        && !updated.getQuickTransformations()[0].equals("")) {
      quickTransformations = updated.getQuickTransformations();
    }
    if (!updated.getQuickTransformationsConverted().equals("")) {
      quickTransformationsConverted = updated.getQuickTransformationsConverted();
    }
    if (!updated.getCustomTransformations().equals("")) {
      customTransformations = updated.getCustomTransformations();
    }
    if (!updated.getAdditionalTransformations().equals("")) {
      additionalTransformations = updated.getAdditionalTransformations();
    }
    if (!updated.getExcludeTransformations().equals("")) {
      excludeTransformations = updated.getExcludeTransformations();
    }
    if (!updated.getCauseIssues().equals("")) {
      causeIssues = updated.getCauseIssues();
    }
    if (updated.getComments().size() > 0) {
      comments = updated.getComments();
    }
    if (!updated.getActualResultStatus().equals("")) {
      actualResultStatus = updated.getActualResultStatus();
    }
    if (!updated.getActualResultAckMessage().equals("")) {
      actualResultAckMessage = updated.getActualResultAckMessage();
    }
    if (!updated.getActualResultAckType().equals("")) {
      actualResultAckType = updated.getActualResultAckType();
    }
    if (!updated.getScenario().equals("")) {
      scenario = updated.getScenario();
    }
    if (!updated.getTestType().equals("")) {
      testType = updated.getTestType();
    }
  }

  public String getActualResultStatus() {
    return actualResultStatus;
  }

  public void setActualResultStatus(String actualResultStatus) {
    this.actualResultStatus = actualResultStatus;
  }

  @Deprecated
  public String getActualResultAckMessage() {
    return actualResultAckMessage;
  }

  public void setActualResultAckMessage(String actualResultAckMessage) {
    this.actualResultAckMessage = actualResultAckMessage;
  }

  public String getActualResultAckType() {
    return actualResultAckType;
  }

  public void setActualResultAckType(String actualResultAckType) {
    this.actualResultAckType = actualResultAckType;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComment(String name, String text) {
    Comment comment = new Comment();
    comment.setName(name);
    comment.setText(text);
    comments.add(comment);
  }

  public class Comment {

    private String name = "";
    private String text = "";

    public String getName() {
      return name;
    }

    public String getText() {
      return text;
    }

    public void setName(String name) {
      this.name = name;
    }

    public void setText(String text) {
      this.text = text;
    }
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Deprecated
  public String getExpectedResult() {
    return expectedResult;
  }

  public void setExpectedResult(String expectedResult) {
    this.expectedResult = expectedResult;
  }

  public String getMessageText() {
    return messageText;
  }

  public void setMessageText(String messageText) {
    this.messageText = messageText;
  }

  public String getTestCaseNumber() {
    return testCaseNumber;
  }

  public void setTestCaseNumber(String testCaseNumber) {
    this.testCaseNumber = testCaseNumber;
  }

  public void setTestCaseSet(String testCaseSet) {
    this.testCaseSet = testCaseSet;
  }

  public String getTestCaseSet() {
    return testCaseSet;
  }

  public String getAssertResult() {
    return assertResult;
  }

  public void setAssertResult(String assertResult) {
    this.assertResult = assertResult;
  }

  public String getAssertResultParameter() {
    return assertResultParameter;
  }

  public void setAssertResultParameter(String assertResultParameter) {
    this.assertResultParameter = assertResultParameter;
  }

  public String getCustomTransformations() {
    return customTransformations;
  }

  public void setCustomTransformations(String customTransformations) {
    this.customTransformations = customTransformations;
  }

  public String getCauseIssues() {
    return causeIssues;
  }

  public void setCauseIssues(String causeIssues) {
    this.causeIssues = causeIssues;
  }

  public void addCauseIssues(String causeIssues) {
    this.causeIssues += causeIssues + "\n";
  }

  public void appendCustomTransformation(String customTransformation) {
    if (this.customTransformations == null) {
      this.customTransformations = "";
    }
    this.customTransformations += customTransformation + "\n";
  }

  public void appendExcludeTransformation(String t) {
    if (this.excludeTransformations == null) {
      this.excludeTransformations = "";
    }
    this.excludeTransformations += t + "\n";
  }

  public void appendAdditionalTransformation(String additionalTransformation) {
    if (this.additionalTransformations == null) {
      this.additionalTransformations = "";
    }
    this.additionalTransformations += additionalTransformation + "\n";
  }

  public void appendOriginalMessage(String append) {
    if (this.originalMessage == null) {
      this.originalMessage = "";
    }
    this.originalMessage += append;
  }

  public void appendCauseIssue(String causeIssue) {
    if (this.causeIssues == null) {
      this.causeIssues = "";
    }
    this.causeIssues += causeIssue + "\n";
  }

  public String getOriginalMessage() {
    return originalMessage;
  }

  public void setOriginalMessage(String originalMessage) {
    this.originalMessage = originalMessage;
  }

  public String[] getQuickTransformations() {
    return quickTransformations;
  }

  public void setQuickTransformations(String[] quickTransformations) {
    this.quickTransformations = quickTransformations;
  }

  public void addQuickTransformation(String quickTransformation) {
    quickTransformations = ArrayUtils.add(quickTransformations, quickTransformation);
  }

  public String createText() {
    return createText(false);
  }

  public String createText(boolean forHtml) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter stringOut = new PrintWriter(stringWriter);
    try {
      stringOut.println(
          "--------------------------------------------------------------------------------");
      stringOut.println(TEST_CASE_NUMBER + " " + testCaseNumber);
      stringOut.println(TEST_CASE_SET + " " + testCaseSet);
      stringOut.println(DESCRIPTION + " " + description);
      stringOut.println(EXPECTED_RESULT + " " + expectedResult);
      if (!assertResult.equals("")) {
        stringOut.println(ASSERT_RESULT + " " + assertResult);
      }
      if (!assertResultParameter.equals("")) {
        stringOut.println(ASSERT_RESULT_PARAMETER + " " + assertResultParameter);
      }
      if (!derivedFromTestCaseNumber.equals("")) {
        stringOut.println(DERIVED_FROM_TEST_CASE_NUMBER + " " + derivedFromTestCaseNumber);
      }
      if (!originalTestCaseNumber.equals("")) {
        stringOut.println(ORIGINAL_TEST_CASE_NUMBER + " " + originalTestCaseNumber);
      }
      for (Comment comment : comments) {
        stringOut.println(COMMENT + " " + comment.getName() + " - " + comment.getText());
      }
      if (!originalMessage.equals(messageText)) {
        printHL7(forHtml, stringOut, ORIGINAL_MESSAGE, originalMessage);
      }
      if (actualResponseMessage != null && !actualResponseMessage.equals("")) {
        printHL7(forHtml, stringOut, ACTUAL_RESPONSE_MESSAGE, actualResponseMessage);
      }
      if (derivedFromVXUMessage != null && !derivedFromVXUMessage.equals("")) {
        printHL7(forHtml, stringOut, DERIVED_FROM_VXU_MESSAGE, derivedFromVXUMessage);
      }
      if (quickTransformations != null && quickTransformations.length > 0
          && quickTransformations[0].trim().length() > 0) {
        stringOut.print(QUICK_TRANSFORMATIONS + " ");
        boolean first = true;
        for (String extra : quickTransformations) {
          if (first) {
            stringOut.print(extra + "");
            first = false;
          } else {
            stringOut.print(", " + extra);
          }
        }
        stringOut.println();
      }
      if (customTransformations != null && !customTransformations.equals("")) {
        stringOut.println(CUSTOM_TRANSFORMATIONS + " ");
        BufferedReader inTransform = new BufferedReader(new StringReader(customTransformations));
        String line;
        while ((line = inTransform.readLine()) != null) {
          line = line.trim();
          stringOut.println(" + " + line);
        }
      }
      if (additionalTransformations != null && !additionalTransformations.equals("")) {
        stringOut.println(ADDITIONAL_TRANSFORMATIONS + " ");
        BufferedReader inTransform =
            new BufferedReader(new StringReader(additionalTransformations));
        String line;
        while ((line = inTransform.readLine()) != null) {
          line = line.trim();
          stringOut.println(" + " + line);
        }
      }
      if (excludeTransformations != null && !excludeTransformations.equals("")) {
        stringOut.println(EXCLUDE_TRANSFORMATIONS + " ");
        BufferedReader inTransform = new BufferedReader(new StringReader(excludeTransformations));
        String line;
        while ((line = inTransform.readLine()) != null) {
          line = line.trim();
          stringOut.println(" + " + line);
        }
      }
      if (causeIssues != null && !causeIssues.equals("")) {
        stringOut.println(CAUSE_ISSUES + " ");
        BufferedReader inTransform = new BufferedReader(new StringReader(causeIssues));
        String line;
        while ((line = inTransform.readLine()) != null) {
          line = line.trim();
          stringOut.println(" + " + line);
        }
      }
      stringOut.println(PATIENT_TYPE + " " + patientType);
      if (!scenario.equals("")) {
        stringOut.println(SCENARIO + " " + scenario);
      }
      if (!testType.equals("")) {
        stringOut.println(TEST_TYPE + " " + testType);
      }
      if (testCaseMode != TestCaseMode.DEFAULT) {
        stringOut.println(TEST_CASE_MODE + " " + testCaseMode);
      }
      stringOut.println(
          "--------------------------------------------------------------------------------");
      stringOut.print(messageText);
    } catch (Exception ioe) {
      stringOut.println("Exception occured: " + ioe);
      ioe.printStackTrace(stringOut);
    } finally {
      stringOut.close();
    }
    return stringWriter.toString();
  }

  private void printHL7(boolean forHtml, PrintWriter stringOut, String fieldName, String message)
      throws IOException {
    stringOut.print(fieldName + " ");
    BufferedReader inBase = new BufferedReader(new StringReader(message));
    String line;
    while ((line = inBase.readLine()) != null) {
      line = line.trim();
      stringOut.print(line + (forHtml ? "&lt;CR&gt;" : "<CR>"));
    }
    stringOut.println();
  }

  public String getQuickTransformationsConverted() {
    return quickTransformationsConverted;
  }

  public void setQuickTransformationsConverted(String quickTransformationsConverted) {
    this.quickTransformationsConverted = quickTransformationsConverted;
  }

  public String getPreparedMessage() {
    if (preparedMessage == null) {
      preparedMessage = originalMessage;
    }
    return preparedMessage;
  }

  public void setPreparedMessage(String preparedMessage) {
    this.preparedMessage = preparedMessage;
  }

  public void prepareMessageAddSegment(String segmentId, String afterSegmentId) {
    StringBuilder sb = new StringBuilder();
    try {
      BufferedReader reader = new BufferedReader(new StringReader(getPreparedMessage()));
      String line = null;
      boolean inserted = false;
      while ((line = reader.readLine()) != null) {
        sb.append(line);
        sb.append("\r");
        if (!inserted && line.startsWith(afterSegmentId)) {
          sb.append(segmentId);
          sb.append("|\r");
          inserted = true;
        }
      }
    } catch (IOException ioe) {
      throw new IllegalArgumentException("Unable to read string", ioe);
    }
    preparedMessage = sb.toString();
  }

  public void prepareMessageRemoveSegment(String segmentId) {
    StringBuilder sb = new StringBuilder();
    try {
      BufferedReader reader = new BufferedReader(new StringReader(getPreparedMessage()));
      String line = null;
      boolean removed = false;
      while ((line = reader.readLine()) != null) {
        if (!removed && line.startsWith(segmentId)) {
          removed = true;
        } else {
          sb.append(line);
          sb.append("\r");
        }
      }
    } catch (IOException ioe) {
      throw new IllegalArgumentException("Unable to read string", ioe);
    }
    preparedMessage = sb.toString();
  }

  public void registerTestCaseMap(Map<String, TestCaseMessage> testCaseMessageMap) {
    this.testCaseMessageMap = testCaseMessageMap;
    if (originalMessage.startsWith("[")) {
      int posEndBracket = originalMessage.indexOf("]");
      if (posEndBracket > 0) {
        String otherTestCaseNumber = originalMessage.substring(1, posEndBracket).trim();
        TestCaseMessage otherTestCaseMessage = testCaseMessageMap.get(otherTestCaseNumber);
        if (otherTestCaseMessage != null) {
          preparedMessage = otherTestCaseMessage.getMessageText();
        }
      }
    }
  }

  public String getScenarioTransforms() {
    return scenarioTransforms;
  }

  public void setScenarioTransforms(String scenarioTransforms) {
    this.scenarioTransforms = scenarioTransforms;
  }

  public String getActualRequestMessage() {
    return actualRequestMessage;
  }

  public void setActualRequestMessage(String actualRequestMessage) {
    this.actualRequestMessage = actualRequestMessage;
  }
}
