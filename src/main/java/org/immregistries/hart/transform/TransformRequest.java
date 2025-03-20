package org.immregistries.hart.transform;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import org.immregistries.hart.tester.connectors.Connector;
import org.immregistries.hart.tester.transform.Patient;

public class TransformRequest {
  private PatientType patientType = null;
  private Connector connector = null;
  private String transformText = null;
  private String resultText = null;
  private Patient patient = null;
  private String yesterday = null;
  private String dayBeforeYesterday = null;
  private String threeDaysAgo = null;
  private String today = null;
  private String tomorrow = null;
  private String longTimeFromNow = null;
  private String now = null;
  private String nowNoTimezone = null;
  private String line = null;
  private String segmentSeparator = "\r";
  private Map<String, TestCaseMessage> testCaseMessageMap = null;
  private boolean hasException = false;
  private Throwable exception = null;
  private TestCaseMessage currentTestCaseMessage;
  private boolean administeredCheck;
  private boolean historicalCheck;
  private boolean refusalCheck;
  private boolean nonAdminCheck;

  public Map<String, TestCaseMessage> getTestCaseMessageMap() {
    return testCaseMessageMap;
  }

  public void setTestCaseMessageMap(Map<String, TestCaseMessage> testCaseMessageMap) {
    this.testCaseMessageMap = testCaseMessageMap;
  }

  public String getThreeDaysAgo() {
    return threeDaysAgo;
  }

  public void setThreeDaysAgo(String threeDaysAgo) {
    this.threeDaysAgo = threeDaysAgo;
  }

  public String getDayBeforeYesterday() {
    return dayBeforeYesterday;
  }

  public void setDayBeforeYesterday(String dayBeforeYesterday) {
    this.dayBeforeYesterday = dayBeforeYesterday;
  }

  public String getTransformText() {
    return transformText;
  }

  public void setTransformText(String transformText) {
    this.transformText = transformText;
  }

  public String getLongTimeFromNow() {
    return longTimeFromNow;
  }

  public void setLongTimeFromNow(String longTimeFromNow) {
    this.longTimeFromNow = longTimeFromNow;
  }

  public String getYesterday() {
    return yesterday;
  }

  public void setYesterday(String yesterday) {
    this.yesterday = yesterday;
  }

  public String getSegmentSeparator() {
    return segmentSeparator;
  }

  public void setSegmentSeparator(String segmentSeparator) {
    this.segmentSeparator = segmentSeparator;
  }

  public TransformRequest(String resultTextOriginal) {
    if (!resultTextOriginal.endsWith("\r")) {
      resultTextOriginal += "\r";
    }
    resultText = resultTextOriginal;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    today = sdf.format(new Date());
    {
      Calendar tomorrowCalendar = Calendar.getInstance();
      tomorrowCalendar.add(Calendar.DAY_OF_MONTH, 1);
      tomorrow = sdf.format(tomorrowCalendar.getTime());
    }
    {
      Calendar longTimeFromNowCalendar = Calendar.getInstance();
      longTimeFromNowCalendar.add(Calendar.DAY_OF_MONTH, 3);
      longTimeFromNowCalendar.add(Calendar.MONTH, 3);
      longTimeFromNowCalendar.add(Calendar.YEAR, 3);
      longTimeFromNow = sdf.format(longTimeFromNowCalendar.getTime());
    }
    {
      Calendar yesterdayCalendar = Calendar.getInstance();
      yesterdayCalendar.add(Calendar.DAY_OF_MONTH, -1);
      yesterday = sdf.format(yesterdayCalendar.getTime());
    }
    {
      Calendar dayBeforeYesterdayCalendar = Calendar.getInstance();
      dayBeforeYesterdayCalendar.add(Calendar.DAY_OF_MONTH, -2);
      dayBeforeYesterday = sdf.format(dayBeforeYesterdayCalendar.getTime());
    }
    {
      Calendar threeDaysAgoCalendar = Calendar.getInstance();
      threeDaysAgoCalendar.add(Calendar.DAY_OF_MONTH, -3);
      threeDaysAgo = sdf.format(threeDaysAgoCalendar.getTime());
    }
    sdf = new SimpleDateFormat("yyyyMMddHHmmssZ");
    now = sdf.format(new Date());
    sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    nowNoTimezone = sdf.format(new Date());
  }


  public boolean hasException() {
    return hasException;
  }

  public void setHasException(boolean hasException) {
    this.hasException = hasException;
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

  public Connector getConnector() {
    return connector;
  }

  public void setConnector(Connector connector) {
    this.connector = connector;
    if (this.connector != null) {
      this.segmentSeparator = this.connector.getSegmentSeparator();
    }
  }

  public String getResultText() {
    return resultText;
  }

  public void setResultText(String resultText) {
    this.resultText = resultText;
  }

  public Patient getPatient() {
    return patient;
  }

  public void setPatient(Patient patient) {
    this.patient = patient;
  }

  public String getToday() {
    return today;
  }

  public void setToday(String today) {
    this.today = today;
  }

  public String getTomorrow() {
    return tomorrow;
  }

  public void setTomorrow(String tomorrow) {
    this.tomorrow = tomorrow;
  }

  public String getNow() {
    return now;
  }

  public void setNow(String now) {
    this.now = now;
  }

  public String getNowNoTimezone() {
    return nowNoTimezone;
  }

  public void setNowNoTimezone(String nowNoTimezone) {
    this.nowNoTimezone = nowNoTimezone;
  }

  public String getLine() {
    return line;
  }

  public void setLine(String line) {
    this.line = line;
  }

  public TestCaseMessage getCurrentTestCaseMessage() {
    return currentTestCaseMessage;
  }

  public void setCurrentTestCaseMessage(TestCaseMessage currentTestCaseMessage) {
    this.currentTestCaseMessage = currentTestCaseMessage;
  }

  public boolean isAdministeredCheck() {
    return administeredCheck;
  }

  public void setAdministeredCheck(boolean administeredCheck) {
    this.administeredCheck = administeredCheck;
  }

  public boolean isHistoricalCheck() {
    return historicalCheck;
  }

  public void setHistoricalCheck(boolean historicalCheck) {
    this.historicalCheck = historicalCheck;
  }

  public boolean isRefusalCheck() {
    return refusalCheck;
  }

  public void setRefusalCheck(boolean refusalCheck) {
    this.refusalCheck = refusalCheck;
  }

  public boolean isNonAdminCheck() {
    return nonAdminCheck;
  }

  public void setNonAdminCheck(boolean nonAdminCheck) {
    this.nonAdminCheck = nonAdminCheck;
  }
}
