package org.immregistries.smm.transform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.smm.tester.connectors.Connector;
import org.immregistries.smm.tester.transform.IssueCreator;
import org.immregistries.smm.tester.transform.Patient;
import org.immregistries.smm.transform.procedure.ProcedureFactory;
import org.immregistries.smm.transform.procedure.ProcedureInterface;

/**
 * 
 * @author nathan
 */
public class Transformer {
  private static final String REP_PAT_EMAIL = "[EMAIL]";
  private static final String REP_PAT_PHONE = "[PHONE]";
  private static final String REP_PAT_PHONE_AREA = "[PHONE_AREA]";
  private static final String REP_PAT_PHONE_LOCAL = "[PHONE_LOCAL]";
  private static final String REP_PAT_PHONE_ALT = "[PHONE_ALT]";
  private static final String REP_PAT_PHONE_ALT_AREA = "[PHONE_ALT_AREA]";
  private static final String REP_PAT_PHONE_ALT_LOCAL = "[PHONE_ALT_LOCAL]";
  private static final String REP_PAT_VAC3_DATE = "[VAC3_DATE]";
  private static final String REP_PAT_LANGUAGE = "[LANGUAGE]";
  private static final String REP_PAT_ETHNICITY_LABEL = "[ETHNICITY_LABEL]";
  private static final String REP_PAT_ETHNICITY = "[ETHNICITY]";
  private static final String REP_PAT_RACE_LABEL = "[RACE_LABEL]";
  private static final String REP_PAT_RACE = "[RACE]";
  private static final String REP_PAT_SSN = "[SSN]";
  private static final String REP_PAT_MRN = "[MRN]";
  private static final String REP_PAT_WIC = "[WIC]";
  private static final String REP_PAT_MEDICAID = "[MEDICAID]";
  private static final String REP_PAT_BIRTH_ORDER = "[BIRTH_ORDER]";
  private static final String REP_PAT_BIRTH_MULTIPLE = "[BIRTH_MULTIPLE]";
  private static final String REP_PAT_DOB = "[DOB]";
  private static final String REP_PAT_MOTHER_MAIDEN = "[MOTHER_MAIDEN]";
  private static final String REP_PAT_MOTHER_DOB = "[MOTHER_DOB]";
  private static final String REP_PAT_MOTHER_SSN = "[MOTHER_SSN]";
  private static final String REP_PAT_MOTHER = "[MOTHER]";
  private static final String REP_PAT_SUFFIX = "[SUFFIX]";
  private static final String REP_PAT_FATHER = "[FATHER]";
  private static final String REP_PAT_GENDER = "[GENDER]";
  private static final String REP_PAT_BOY_OR_GIRL = "[BOY_OR_GIRL]";
  private static final String REP_PAT_GIRL = "[GIRL]";
  private static final String REP_PAT_BOY = "[BOY]";
  private static final String REP_PAT_ALIAS_BOY_OR_GIRL = "[ALIAS_BOY_OR_GIRL]";
  private static final String REP_PAT_ALIAS_GIRL = "[ALIAS_GIRL]";
  private static final String REP_PAT_ALIAS_BOY = "[ALIAS_BOY]";

  private static final String REP_ENTERED_BY_FIRST = "[ENTERED_BY_FIRST]";
  private static final String REP_ENTERED_BY_LAST = "[ENTERED_BY_LAST]";
  private static final String REP_ENTERED_BY_MIDDLE = "[ENTERED_BY_MIDDLE]";
  private static final String REP_ENTERED_BY_NPI = "[ENTERED_BY_NPI]";
  private static final String REP_ORDERED_BY_FIRST = "[ORDERED_BY_FIRST]";
  private static final String REP_ORDERED_BY_LAST = "[ORDERED_BY_LAST]";
  private static final String REP_ORDERED_BY_MIDDLE = "[ORDERED_BY_MIDDLE]";
  private static final String REP_ORDERED_BY_NPI = "[ORDERED_BY_NPI]";
  private static final String REP_ADMIN_BY_FIRST = "[ADMIN_BY_FIRST]";
  private static final String REP_ADMIN_BY_LAST = "[ADMIN_BY_LAST]";
  private static final String REP_ADMIN_BY_MIDDLE = "[ADMIN_BY_MIDDLE]";
  private static final String REP_ADMIN_BY_NPI = "[ADMIN_BY_NPI]";
  private static final String REP_RESPONSIBLE_ORG_NAME = "[RESPONSIBLE_ORG_NAME]";
  private static final String REP_RESPONSIBLE_ORG_ID = "[RESPONSIBLE_ORG_ID]";
  private static final String REP_ADMIN_ORG_1_NAME = "[ADMIN_ORG_1_NAME]";
  private static final String REP_ADMIN_ORG_1_ID = "[ADMIN  Q_ORG_1_ID]";
  private static final String REP_ADMIN_ORG_2_NAME = "[ADMIN_ORG_2_NAME]";
  private static final String REP_ADMIN_ORG_2_ID = "[ADMIN_ORG_2_ID]";

  private static final String REP_CON_USERID = "[USERID]";
  private static final String REP_CON_PASSWORD = "[PASSWORD]";
  private static final String REP_CON_FACILITYID = "[FACILITYID]";
  private static final String REP_CON_FILENAME = "[FILENAME]";
  private static final String REP_CON_OTHERID = "[OTHERID]";

  private static final String REP_SPACE = "[SPACE]";

  private static final String INSERT_SEGMENT = "insert segment ";
  private static final String INSERT_SEGMENT_FIRST = "first";
  private static final String INSERT_SEGMENT_BEFORE = "before";
  private static final String INSERT_SEGMENT_AFTER = "after";
  private static final String INSERT_SEGMENT_LAST = "last";
  private static final String INSERT_SEGMENT_IF_MISSING = "if missing";
  private static final String INSERT_SEGMENT_IF_MISSING_FROM_MESSAGE = "if missing from message";

  private static final String RUN_PROCEDURE = "run procedure";
  private static final String SET = "set";

  private static final String REMOVE_REPEAT = "remove repeat"; // remove repeat
                                                               // PID-5.5 valued
                                                               // MA
  private static final String REMOVE_SEGMENT = "remove segment ";
  private static final String REMOVE_OBSERVATION = "remove observation ";
  private static final String REMOVE_EMPTY_OBSERVATIONS = "remove empty observations";

  private static final String CLEAR = "clear";

  private static final String CLEAN = "clean";
  private static final String CLEAN_NO_LAST_SLASH = "no last slash";
  private static final String FIX = "fix";
  private static final String FIX_AMPERSAND = "ampersand";
  private static final String FIX_ESCAPE = "escape";
  private static final String FIX_MISSING_MOTHER_MAIDEN_FIRST = "missing mother maiden first";

  private static final String IF_18_PLUS = "if 18+";
  private static final String IF_19_PLUS = "if 19+";
  private static final String IF_18_MINUS = "if 18-";
  private static final String IF_19_MINUS = "if 19-";
  
  private static final String IF_ADMINISTERED = "if administered";
  private static final String IF_HISTORICAL = "if historical";
  private static final String IF_REFUSAL = "if refusal";
  private static final String IF_NON_ADMIN = "if non-admin";

  private static final int VACCINE_CVX = 0;
  private static final int VACCINE_NAME = 1;
  private static final int VACCINE_LOT = 2;
  private static final int VACCINE_MVX = 3;
  private static final int VACCINE_MANUFACTURER = 4;
  private static final int VACCINE_TRADE_NAME = 5;
  private static final int VACCINE_AMOUNT = 6;
  private static final int VACCINE_ROUTE = 7;
  private static final int VACCINE_SITE = 8;
  private static final int VACCINE_VIS_PUB = 9;
  private static final int VACCINE_VIS_PUB_CODE = 10;
  private static final int VACCINE_VIS_PUB_DATE = 11;
  private static final int VACCINE_VIS2_PUB = 12;
  private static final int VACCINE_VIS2_PUB_CODE = 13;
  private static final int VACCINE_VIS2_PUB_DATE = 14;
  private static final int VACCINE_VIS3_PUB = 15;
  private static final int VACCINE_VIS3_PUB_CODE = 16;
  private static final int VACCINE_VIS3_PUB_DATE = 17;

  private static final String REPEAT_REF = "-?";
  
  public static final String SCENARIO_WILDCARD = "*";
  public static final String SCENARIO_DELIM = ",";
  public static final String SCENARIO_NOT = "!";

  private static Map<String, List<String[]>> conceptMap = null;
  private static Map<String, List<String[]>> testDataMap = null;
  private static Random random = new Random();

  public Transformer() {
    // default
  }

  public Transformer(File testDataFile) throws IOException {
    BufferedReader in = new BufferedReader(new FileReader(testDataFile));
    testDataMap = readDataIn(in);
    in.close();
  }

  public String getRandomValue(String concept) {
    try {
      return getValue(concept, 0);
    } catch (IOException ioe) {
      return "Unable to get value: " + ioe.getMessage();
    }
  }

  public String getValue(String concept, int pos) throws IOException {
    if (conceptMap == null) {
      init();
    }
    if (testDataMap != null) {
      List<String[]> valueList = testDataMap.get(concept);
      if (valueList != null) {
        return getRandomValue(pos, valueList);
      }
    }
    List<String[]> valueList = conceptMap.get(concept);
    if (valueList != null) {
      return getRandomValue(pos, valueList);
    }
    return "";
  }

  public String getRandomValue(int pos, List<String[]> valueList) {
    String[] values = valueList.get(random.nextInt(valueList.size()));
    if (pos < values.length) {
      return values[pos];
    }
    return "";
  }

  public String[] getValueArray(String concept, int size) {
    if (conceptMap == null) {
      init();
    }
    String[] valueSourceList = null;
    List<String[]> valueList = null;
    if (testDataMap != null) {
      valueList = testDataMap.get(concept);
    }
    if (valueList == null) {
      valueList = conceptMap.get(concept);
    }
    if (valueList != null) {
      valueSourceList = valueList.get(random.nextInt(valueList.size()));
    }
    String[] values = new String[size];
    for (int i = 0; i < values.length; i++) {
      if (valueSourceList != null && i < valueSourceList.length) {
        values[i] = valueSourceList[i];
      } else {
        values[i] = "";
      }
    }
    return values;
  }

  public boolean doesValueExist(String concept, String value) {
    if (StringUtils.isBlank(value)) {
      return false;
    }

    if (conceptMap == null) {
      init();
    }
    List<String[]> valueList = null;
    if (testDataMap != null) {
      valueList = testDataMap.get(concept);
    }
    if (valueList == null) {
      valueList = conceptMap.get(concept);
    }

    for (String[] arr : valueList) {
      if (value.equalsIgnoreCase(arr[0])) {
        return true;
      }
    }

    return false;
  }

  protected boolean alsoHas(TestCaseMessage testCaseMessage, String checkString) {
    boolean alsoHas = false;
    for (String extra2 : testCaseMessage.getQuickTransformations()) {
      if (extra2.equals(checkString)) {
        alsoHas = true;
        break;
      }
    }
    return alsoHas;
  }

  protected PatientType createDates(String[] dates, PatientType type) {
    {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      if (type == PatientType.BABY || (type == PatientType.ANY_CHILD && random.nextBoolean())) {
        // Setting up baby, 6 months old today
        // 6 month appointment
        Calendar cal6Month = Calendar.getInstance();
        dates[3] = sdf.format(cal6Month.getTime());

        // Born about 6 months before
        Calendar calBorn = Calendar.getInstance();
        calBorn.add(Calendar.MONTH, -6);
        calBorn.add(Calendar.DAY_OF_MONTH, 3 - random.nextInt(17));
        dates[0] = sdf.format(calBorn.getTime());

        // 4 month appointment
        Calendar cal4Month = Calendar.getInstance();
        cal4Month.setTime(calBorn.getTime());
        cal4Month.add(Calendar.MONTH, 4);
        cal4Month.add(Calendar.DAY_OF_MONTH, random.nextInt(12) - 3);
        dates[2] = sdf.format(cal4Month.getTime());

        // 2 month appointment
        Calendar cal2Month = Calendar.getInstance();
        cal2Month.setTime(calBorn.getTime());
        cal2Month.add(Calendar.MONTH, 2);
        cal2Month.add(Calendar.DAY_OF_MONTH, random.nextInt(10) - 3);
        if (cal2Month.after(cal6Month.getTime())) {
          dates[1] = dates[3];
        } else {
          dates[1] = sdf.format(cal2Month.getTime());
        }

        return PatientType.BABY;
      } else if (type == PatientType.TWO_MONTHS_OLD || type == PatientType.TWO_YEARS_OLD
          || type == PatientType.FOUR_YEARS_OLD || type == PatientType.TWELVE_YEARS_OLD) {
        // Setting up baby, 2 months old today
        // 2 month appointment
        // This type will always be at least two and the appointment will always
        // be when the patient was at least two
        Calendar calToday = Calendar.getInstance();
        dates[3] = sdf.format(calToday.getTime());

        int months = 0;
        int years = 0;
        if (type == PatientType.TWO_MONTHS_OLD) {
          months = 2;
        } else if (type == PatientType.TWO_YEARS_OLD) {
          years = 2;
        } else if (type == PatientType.FOUR_YEARS_OLD) {
          years = 4;
        } else if (type == PatientType.TWELVE_YEARS_OLD) {
          years = 12;
        }
        // set birth date
        Calendar calBorn = Calendar.getInstance();
        calBorn.add(Calendar.MONTH, -months);
        calBorn.add(Calendar.YEAR, -years);
        calBorn.add(Calendar.DAY_OF_MONTH, -random.nextInt(17));
        dates[0] = sdf.format(calBorn.getTime());

        // 2 month appointment
        Calendar cal2Month = Calendar.getInstance();
        cal2Month.setTime(calBorn.getTime());
        cal2Month.add(Calendar.MONTH, months);
        cal2Month.add(Calendar.YEAR, years);
        cal2Month.add(Calendar.DAY_OF_MONTH, random.nextInt(10));
        if (cal2Month.getTime().after(calToday.getTime())) {
          dates[1] = dates[3];
          dates[2] = dates[3];
        } else {
          dates[1] = sdf.format(cal2Month.getTime());
          dates[2] = dates[1];
        }

        if (type == PatientType.TWO_MONTHS_OLD) {
          return PatientType.BABY;
        } else if (type == PatientType.TWO_YEARS_OLD) {
          return PatientType.TODDLER;
        } else if (type == PatientType.FOUR_YEARS_OLD) {
          return PatientType.TODDLER;
        } else if (type == PatientType.TWELVE_YEARS_OLD) {
          return PatientType.TWEEN;
        }
        return type;
      } else {
        if (type == PatientType.TODDLER
            || (type == PatientType.ANY_CHILD && random.nextBoolean())) {
          // Setting up toddler
          Calendar calendar = Calendar.getInstance();
          // 4 years (today) - 48 months
          dates[3] = sdf.format(calendar.getTime());
          // 19 months
          calendar.add(Calendar.MONTH, 19 - 48);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[2] = sdf.format(calendar.getTime());
          // 12 months
          calendar.add(Calendar.MONTH, 12 - 19);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[1] = sdf.format(calendar.getTime());
          // birth
          calendar.add(Calendar.MONTH, -12);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[0] = sdf.format(calendar.getTime());
          return PatientType.TODDLER;
        } else if (type == PatientType.ANY_CHILD || type == PatientType.TWEEN) {
          // Setting up tween
          Calendar calendar = Calendar.getInstance();
          // 13 years (today)
          dates[3] = sdf.format(calendar.getTime());
          // 11 years
          calendar.add(Calendar.YEAR, -2);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[2] = sdf.format(calendar.getTime());
          // 9 years
          calendar.add(Calendar.YEAR, -2);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[1] = sdf.format(calendar.getTime());
          // birth
          calendar.add(Calendar.YEAR, -9);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[0] = sdf.format(calendar.getTime());
          return PatientType.TWEEN;
        } else {
          // Setting up adult
          Calendar calendar = Calendar.getInstance();
          // 67 years (today)
          dates[3] = sdf.format(calendar.getTime());
          // last year
          calendar.add(Calendar.YEAR, -1);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[2] = sdf.format(calendar.getTime());
          // two years before that
          calendar.add(Calendar.YEAR, -2);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[1] = sdf.format(calendar.getTime());
          // birth
          calendar.add(Calendar.YEAR, -64);
          calendar.add(Calendar.DAY_OF_MONTH, 7 - random.nextInt(15));
          dates[0] = sdf.format(calendar.getTime());
          return PatientType.ADULT;

        }
      }
    }
  }

  protected void handleSometimes(Transform t) {
    int sometimes = 0;
    if (t.value.startsWith("~") && t.value.indexOf("%") != -1) {
      int perPos = t.value.indexOf("%");
      try {
        sometimes = Integer.parseInt(t.value.substring(1, perPos));
        t.value = t.value.substring(perPos + 1);
      } catch (NumberFormatException nfe) {
        // ignore
      }
    }
    if (sometimes > 0) {
      String part1 = t.value;
      String part2 = "";
      int colonPos = t.value.indexOf(":");
      if (colonPos != -1) {
        part1 = t.value.substring(0, colonPos);
        part2 = t.value.substring(colonPos + 1);
      }
      if (random.nextInt(100) >= sometimes) {
        t.value = part2;
        handleSometimes(t);
      } else {
        t.value = part1;
      }
    }
  }

  protected void init() {
    try {
      BufferedReader in = new BufferedReader(
          new InputStreamReader(getClass().getResourceAsStream("transform.txt")));
      conceptMap = readDataIn(in);
    } catch (IOException e) {
      e.printStackTrace();
      conceptMap = new HashMap<String, List<String[]>>();
    }
  }

  public HashMap<String, List<String[]>> readDataIn(BufferedReader in) throws IOException {
    HashMap<String, List<String[]>> map = new HashMap<String, List<String[]>>();
    String line;
    while ((line = in.readLine()) != null) {
      int equals = line.indexOf("=");
      if (equals != -1) {
        String concept = line.substring(0, equals);
        String[] values = line.substring(equals + 1).split("\\,");
        List<String[]> valueList = map.get(concept);
        if (valueList == null) {
          valueList = new ArrayList<String[]>();
          map.put(concept, valueList);
        }
        valueList.add(values);
      }
    }
    return map;
  }

  public String transform(Connector connector, String messageText) {
    String quickTransforms = "";

    if (connector.getQuickTransformations() != null) {
      for (String extra : connector.getQuickTransformations()) {
        if (extra.equals("2.5.1")) {
          quickTransforms += "MSH-12=2.5.1\n";
        } else if (extra.equals("2.3.1")) {
          quickTransforms += "MSH-12=2.3.1\n";
        }
      }
    }

    String transforms = quickTransforms + "\n";
    if (!connector.getCustomTransformations().equals("")) {
      transforms += connector.getCustomTransformations() + "\n";
    }
    TransformRequest transformRequest = new TransformRequest(messageText);
    transformRequest.setConnector(connector);
    transformRequest.setPatientType(PatientType.NONE);
    transformRequest.setTransformText(transforms);
    // transformRequest.setTestCaseMessageMap(testCaseMessage.getTestCaseMessageMap());
    transform(transformRequest);
    return transformRequest.getResultText();
  }

  public static String transform(Connector connector, TestCaseMessage testCaseMessage) {
    String message = testCaseMessage.getMessageText();
    List<String> scenarioTransforms = new ArrayList<>();
    
    // exact match lookup for scenario name
    if (connector.getScenarioTransformationsMap().containsKey(testCaseMessage.getScenario())) {
      scenarioTransforms.add(connector.getScenarioTransformationsMap().get(testCaseMessage.getScenario()));
    }
    
    // wildcard lookups for test code
    // possibly don't include the exact match result here to replicate the old functionality
    // of not including the testCaseNumber lookup if the scenario lookup returned a result
    scenarioTransforms.addAll(
      connector.getTransformsFromScenarioMap(
        testCaseMessage.getTestCaseNumber(),
        scenarioTransforms.isEmpty()));
    
    // I can't tell where this field is being used so I'm not sure if separating by a newline is appropriate or not
    testCaseMessage.setScenarioTransforms(String.join("\n", scenarioTransforms));
    
    String additionalTransformations = testCaseMessage.getAdditionalTransformations();
    if ("".equals(additionalTransformations)) {
      additionalTransformations = null;
    }
    String transforms = connector.getCustomTransformations();
    if (testCaseMessage.getTestCaseMode() == TestCaseMode.ASSESSMENT) {
      transforms = connector.getAssessmentTransformations();
    }
    if (!"".equals(transforms) || !scenarioTransforms.isEmpty() || additionalTransformations != null) {
      Transformer transformer = new Transformer();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
      connector.setCurrentFilename("dqa-tester-request" + sdf.format(new Date()) + ".hl7");
      message = transformer.transformForTesting(connector, message, transforms, scenarioTransforms,
          testCaseMessage.getExcludeTransformations(), additionalTransformations,
          testCaseMessage.getTestCaseMessageMap());
    }
    return message;
  }

  public String transformForTesting(Connector connector, String messageText,
      String customTransformations, List<String> scenarioTransformations, String excludeTransformations,
      String additionalTransformations, Map<String, TestCaseMessage> testCaseMessageMap) {
    String quickTransforms = "";

    if (connector.getQuickTransformations() != null) {
      for (String extra : connector.getQuickTransformations()) {
        if (extra.equals("2.5.1")) {
          quickTransforms += "MSH-12=2.5.1\n";
        } else if (extra.equals("2.3.1")) {
          quickTransforms += "MSH-12=2.3.1\n";
        }
      }
    }
    String transforms = quickTransforms + "\n";
    if (!customTransformations.equals("")) {
      if (excludeTransformations != null && !excludeTransformations.equals("")) {
        try {
          BufferedReader customTransformsIn =
              new BufferedReader(new StringReader(customTransformations));
          String line;
          while ((line = customTransformsIn.readLine()) != null) {
            BufferedReader etIn = new BufferedReader(new StringReader(excludeTransformations));
            String l;
            boolean exclude = false;
            while ((l = etIn.readLine()) != null) {
              if (l.equals(line)) {
                exclude = true;
                break;
              }
            }
            if (!exclude) {
              transforms += line + "\n";
            }
          }
        } catch (IOException ioe) {
          // shouldn't happen, but if it does then just keep going
        }
      } else {
        transforms += customTransformations + "\n";
      }
    }

    if (scenarioTransformations != null && !scenarioTransformations.isEmpty()) {
      for (String scenarioTransform : scenarioTransformations) {
        transforms += scenarioTransform + "\n";
      }
    }

    if (additionalTransformations != null) {
      transforms += additionalTransformations;
    }

    TransformRequest transformRequest = new TransformRequest(messageText);
    transformRequest.setConnector(connector);
    transformRequest.setPatientType(PatientType.NONE);
    transformRequest.setTransformText(transforms);
    transformRequest.setTestCaseMessageMap(testCaseMessageMap);
    transform(transformRequest);
    return transformRequest.getResultText();
  }

  public String transformForSubmitServlet(Connector connector, String messageText,
      String customTransformations, String scenarioTransformations, String excludeTransformations,
      String additionalTransformations) {
    String quickTransforms = "";

    if (connector.getQuickTransformations() != null) {
      for (String extra : connector.getQuickTransformations()) {
        if (extra.equals("2.5.1")) {
          quickTransforms += "MSH-12=2.5.1\n";
        } else if (extra.equals("2.3.1")) {
          quickTransforms += "MSH-12=2.3.1\n";
        }
      }
    }
    String transforms = quickTransforms + "\n";
    if (!customTransformations.equals("")) {
      transforms += customTransformations + "\n";
    }
    if (scenarioTransformations != null) {
      transforms += scenarioTransformations;
    }
    if (additionalTransformations != null) {
      transforms += additionalTransformations;
    }

    TransformRequest transformRequest = new TransformRequest(messageText);
    transformRequest.setConnector(connector);
    transformRequest.setPatientType(PatientType.NONE);
    transformRequest.setTransformText(transforms);
    transform(transformRequest);
    return transformRequest.getResultText();
  }

  public static String readField(String message, String reference,
      TransformRequest transformRequest) {
    Transform t = readHL7Reference(reference, reference.length());
    if (t == null) {
      return "";
    }
    try {
      return getValueFromHL7(message, t, transformRequest);
    } catch (IOException ioe) {
      throw new IllegalArgumentException(ioe);
    }
  }

  public String transform(TestCaseMessage testCaseMessage) {
    return transform(testCaseMessage, null);
  }

  public String transform(TestCaseMessage testCaseMessage, Connector connector) {
    String actualTestCase = testCaseMessage.getTestCaseNumber();
    if ("".equals(actualTestCase)) {
      actualTestCase = REP_PAT_MRN;
    }
    String quickTransforms = convertQuickTransforms(testCaseMessage, actualTestCase);

    String causeIssueTransforms = IssueCreator.createTransforms(testCaseMessage);
    testCaseMessage.setCauseIssueTransforms(causeIssueTransforms);
    String transforms = quickTransforms + "\n" + testCaseMessage.getCustomTransformations() + "\n"
        + causeIssueTransforms + testCaseMessage.getAdditionalTransformations();
    TransformRequest transformRequest = new TransformRequest(testCaseMessage.getPreparedMessage());
    transformRequest.setConnector(connector);
    transformRequest.setPatientType(testCaseMessage.getPatientType());
    transformRequest.setTransformText(transforms);
    transformRequest.setSegmentSeparator(testCaseMessage.getLineEnding());
    transformRequest.setTestCaseMessageMap(testCaseMessage.getTestCaseMessageMap());
    transformRequest.setCurrentTestCaseMessage(testCaseMessage);
    transform(transformRequest);
    String result = transformRequest.getResultText();
    testCaseMessage.setMessageText(result);
    testCaseMessage.setQuickTransformationsConverted(quickTransforms);
    testCaseMessage.setHasIssue(transformRequest.hasException());
    testCaseMessage.setException(transformRequest.getException());
    return transforms;
  }

  public String transformAddition(TestCaseMessage testCaseMessage, String additionTransformations) {
    TransformRequest transformRequest = new TransformRequest(testCaseMessage.getMessageText());
    transformRequest.setConnector(null);
    transformRequest.setPatientType(testCaseMessage.getPatientType());
    transformRequest.setTransformText(additionTransformations);
    transformRequest.setSegmentSeparator(testCaseMessage.getLineEnding());
    transformRequest.setTestCaseMessageMap(testCaseMessage.getTestCaseMessageMap());
    transformRequest.setCurrentTestCaseMessage(testCaseMessage);
    transform(transformRequest);
    testCaseMessage.setHasIssue(transformRequest.hasException());
    testCaseMessage.setException(transformRequest.getException());
    return transformRequest.getResultText();
  }

  public String convertQuickTransforms(TestCaseMessage testCaseMessage, String actualTestCase) {
    String quickTransforms = "";
    if (testCaseMessage.getQuickTransformations() != null) {
      for (String extra : testCaseMessage.getQuickTransformations()) {
        if (extra.equals("BOY")) {
          quickTransforms += "PID-5=~90%[LAST]:[MOTHER_MAIDEN]\n";
          quickTransforms += "PID-5.2=[BOY]\n";
          quickTransforms += "PID-5.3=~60%[BOY_MIDDLE]:~70%[BOY_MIDDLE_INITIAL]\n";
          quickTransforms += "PID-5.4=~10%[SUFFIX]\n";
          quickTransforms += "PID-5.7=L\n";
          if (alsoHas(testCaseMessage, "GIRL")) {
            quickTransforms += "PID-8=~50%M:F\n";

          } else {
            quickTransforms += "PID-8=M\n";
          }
        } else if (extra.equals("GIRL")) {
          quickTransforms += "PID-5=~90%[LAST]:[MOTHER_MAIDEN]\n";
          quickTransforms += "PID-5.2=[GIRL]\n";
          quickTransforms += "PID-5.3=~60%[GIRL_MIDDLE]:~70%[GIRL_MIDDLE_INITIAL]\n";
          quickTransforms += "PID-5.7=L\n";
          quickTransforms += "PID-8=F\n";
        } else if (extra.equals("BOY_OR_GIRL")) {
          quickTransforms += "PID-5=~90%[LAST]:[MOTHER_MAIDEN]\n";
          quickTransforms += "PID-5.2=[BOY_OR_GIRL]\n";
          quickTransforms += "PID-5.3=~60%[BOY_OR_GIRL_MIDDLE]:~70%[GIRL_MIDDLE_INITIAL]\n";
          quickTransforms += "PID-5.7=L\n";
          quickTransforms += "PID-8=[GENDER]\n";
        } else if (extra.equals("FATHER")) {
          if (alsoHas(testCaseMessage, "MOTHER")) {
            quickTransforms += "NK1#2-2.1=~60%[LAST]:[LAST_DIFFERENT]\n";
            quickTransforms += "NK1#2-2.2=[FATHER]\n";
            quickTransforms += "NK1#2-2.7=L\n";
            quickTransforms += "NK1#2-3=FTH\n";
            quickTransforms += "NK1#2-3.2=Father\n";
            quickTransforms += "NK1#2-3.3=HL70063\n";
          } else {
            quickTransforms += "NK1-2.1=~60%[LAST]:[LAST_DIFFERENT]\n";
            quickTransforms += "NK1-2.2=[FATHER]\n";
            quickTransforms += "NK1-2.7=L\n";
            quickTransforms += "NK1-3=FTH\n";
            quickTransforms += "NK1-3.2=Father\n";
            quickTransforms += "NK1-3.3=HL70063\n";
          }
        } else if (extra.equals("MOTHER")) {
          quickTransforms += "PID-6=~50%[MOTHER_MAIDEN]:[LAST_DIFFERENT]\n";
          quickTransforms += "PID-6.2=[MOTHER]\n";
          quickTransforms += "PID-6.7=M\n";
          quickTransforms += "NK1-2.1=~60%[LAST]:[LAST_DIFFERENT]\n";
          quickTransforms += "NK1-2.2=[MOTHER]\n";
          quickTransforms += "NK1-2.7=L\n";
          quickTransforms += "NK1-3=MTH\n";
          quickTransforms += "NK1-3.2=Mother\n";
          quickTransforms += "NK1-3.3=HL70063\n";
        } else if (extra.equals("DOB")) {
          quickTransforms += "PID-7=[DOB]\n";
        } else if (extra.equals("TWIN_POSSIBLE")) {
          quickTransforms += "PID-24=[BIRTH_MULTIPLE]\n";
          quickTransforms += "PID-25=[BIRTH_ORDER]\n";
        } else if (extra.equals("VAC1_ADMIN")) {
          int count = 3;
          count = count
              - (alsoHas(testCaseMessage, "VAC2_ADMIN") || alsoHas(testCaseMessage, "VAC2_HIST") ? 1
                  : 0);
          count = count
              - (alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1
                  : 0);
          quickTransforms = addAdmin(quickTransforms, "1", count);
        } else if (extra.equals("VAC1_DELETE")) {
          int count = 3;
          count = count
              - (alsoHas(testCaseMessage, "VAC2_ADMIN") || alsoHas(testCaseMessage, "VAC2_HIST") ? 1
                  : 0);
          count = count
              - (alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1
                  : 0);
          quickTransforms = addDelete(quickTransforms, "2", count);
        } else if (extra.equals("VAC2_ADMIN")) {
          int count = 3;
          count -=
              alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1
                  : 0;
          quickTransforms = addAdmin(quickTransforms, "2", count);
        } else if (extra.equals("VAC3_ADMIN")) {
          quickTransforms = addAdmin(quickTransforms, "3", 3);
        } else if (extra.equals("VAC1_NA")) {
          int count = 3;
          count -=
              alsoHas(testCaseMessage, "VAC2_ADMIN") || alsoHas(testCaseMessage, "VAC2_HIST") ? 1
                  : 0;
          count -=
              alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1
                  : 0;
          quickTransforms += "ORC-3=[VAC" + count + "_ID]\n";
          quickTransforms += "RXA-3=[VAC" + count + "_DATE]\n";
          quickTransforms += "RXA-5.1=[VAC" + count + "_CVX]\n";
          quickTransforms += "RXA-5.2=[VAC" + count + "_CVX_LABEL]\n";
          quickTransforms += "RXA-5.3=CVX\n";
          quickTransforms += "RXA-6=999\n";
          quickTransforms += "RXA-21=A\n";
        } else if (extra.equals("VAC1_HIST")) {
          int count = 3;
          count -=
              alsoHas(testCaseMessage, "VAC2_ADMIN") || alsoHas(testCaseMessage, "VAC2_HIST") ? 1
                  : 0;
          count -=
              alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1
                  : 0;
          quickTransforms += "ORC-3=[VAC" + count + "_ID]\n";
          quickTransforms += "RXA-3=[VAC2_DATE]\n";
          quickTransforms += "RXA-5.1=[VAC" + count + "_CVX]\n";
          quickTransforms += "RXA-5.2=[VAC" + count + "_CVX_LABEL]\n";
          quickTransforms += "RXA-5.3=CVX\n";
          quickTransforms += "RXA-6=999\n";
          quickTransforms += "RXA-9.1=01\n";
          quickTransforms += "RXA-9.2=Historical\n";
          quickTransforms += "RXA-9.3=NIP001\n";
          quickTransforms += "RXA-20=CP\n";
          quickTransforms += "RXA-21=A\n";
        } else if (extra.equals("VAC2_HIST")) {
          int count = 3;
          count -=
              alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1
                  : 0;
          quickTransforms += "ORC#2-3=[VAC" + count + "_ID]\n";
          quickTransforms += "RXA#2-3=[VAC" + count + "_DATE]\n";
          quickTransforms += "RXA#2-5.1=[VAC" + count + "_CVX]\n";
          quickTransforms += "RXA#2-5.2=[VAC" + count + "_CVX_LABEL]\n";
          quickTransforms += "RXA#2-5.3=CVX\n";
          quickTransforms += "RXA#2-9.1=01\n";
          quickTransforms += "RXA#2-6=999\n";
          quickTransforms += "RXA#2-9.2=Historical\n";
          quickTransforms += "RXA#2-9.3=NIP001\n";
          quickTransforms += "RXA#2-20=CP\n";
          quickTransforms += "RXA#2-21=A\n";
        } else if (extra.equals("VAC3_HIST")) {
          quickTransforms += "ORC#2-3=[VAC3_ID]\n";
          quickTransforms += "RXA#3-3=[VAC3_DATE]\n";
          quickTransforms += "RXA#3-5.1=[VAC3_CVX]\n";
          quickTransforms += "RXA#3-5.2=[VAC3_CVX_LABEL]\n";
          quickTransforms += "RXA#3-5.3=CVX\n";
          quickTransforms += "RXA#3-9.1=01\n";
          quickTransforms += "RXA#3-6=999\n";
          quickTransforms += "RXA#3-9.2=Historical\n";
          quickTransforms += "RXA#3-9.3=NIP001\n";
          quickTransforms += "RXA#3-20=CP\n";
          quickTransforms += "RXA#3-21=A\n";
        } else if (extra.equals("VAC2_NA")) {
          int count = 3;
          count -=
              alsoHas(testCaseMessage, "VAC3_ADMIN") || alsoHas(testCaseMessage, "VAC3_HIST") ? 1
                  : 0;
          quickTransforms += "ORC#2-3=[VAC" + count + "_ID]\n";
          quickTransforms += "RXA#2-3=[VAC" + count + "_DATE]\n";
          quickTransforms += "RXA#2-5.1=[VAC" + count + "_CVX]\n";
          quickTransforms += "RXA#2-5.2=[VAC" + count + "_CVX_LABEL]\n";
          quickTransforms += "RXA#2-5.3=CVX\n";
          quickTransforms += "RXA#2-9.1=01\n";
          quickTransforms += "RXA#2-6=999\n";
          quickTransforms += "RXA#2-21=A\n";
        } else if (extra.equals("VAC3_NA")) {
          quickTransforms += "ORC#3-3=[VAC3_ID]\n";
          quickTransforms += "RXA#3-3=[VAC3_DATE]\n";
          quickTransforms += "RXA#3-5.1=[VAC3_CVX]\n";
          quickTransforms += "RXA#3-5.2=[VAC3_CVX_LABEL]\n";
          quickTransforms += "RXA#3-5.3=CVX\n";
          quickTransforms += "RXA#3-9.1=01\n";
          quickTransforms += "RXA#3-6=999\n";
          quickTransforms += "RXA#3-21=A\n";
        } else if (extra.equals("VAC1_CHANGE")) {
          quickTransforms = changeVac(quickTransforms, "1");
        } else if (extra.equals("VAC2_CHANGE")) {
          quickTransforms = changeVac(quickTransforms, "2");
        } else if (extra.equals("VAC3_CHANGE")) {
          quickTransforms = changeVac(quickTransforms, "3");
        } else if (extra.equals("VAC4_CHANGE")) {
          quickTransforms = changeVac(quickTransforms, "4");
        } else if (extra.equals("VAC5_CHANGE")) {
          quickTransforms = changeVac(quickTransforms, "5");
        } else if (extra.equals("ADDRESS")) {
          quickTransforms += "PID-11.1=[STREET]\n";
          quickTransforms += "PID-11.3=[CITY]\n";
          quickTransforms += "PID-11.4=[STATE]\n";
          quickTransforms += "PID-11.5=[ZIP]\n";
          quickTransforms += "PID-11.6=USA\n";
          quickTransforms += "PID-11.7=P\n";
          quickTransforms += "NK1-4.1*=[STREET]\n";
          quickTransforms += "NK1-4.3*=[CITY]\n";
          quickTransforms += "NK1-4.4*=[STATE]\n";
          quickTransforms += "NK1-4.5*=[ZIP]\n";
          quickTransforms += "NK1-4.6*=USA\n";
          quickTransforms += "NK1-4.7*=P\n";
        } else if (extra.equals("PHONE")) {
          quickTransforms += "PID-13.1=\n";
          quickTransforms += "PID-13.2=PRN\n";
          quickTransforms += "PID-13.3=PH\n";
          quickTransforms += "PID-13.6=[PHONE_AREA]\n";
          quickTransforms += "PID-13.7=[PHONE_LOCAL]\n";
          quickTransforms += "NK1-5.1*=\n";
          quickTransforms += "NK1-5.2*=PRN\n";
          quickTransforms += "NK1-5.3*=PH\n";
          quickTransforms += "NK1-5.6*=[PHONE_AREA]\n";
          quickTransforms += "NK1-5.7*=[PHONE_LOCAL]\n";
        } else if (extra.equals("RACE")) {
          quickTransforms += "PID-10.1=[RACE]\n";
          quickTransforms += "PID-10.2=[RACE_LABEL]\n";
          quickTransforms += "PID-10.3=CDCREC\n";
        } else if (extra.equals("ETHNICITY")) {
          quickTransforms += "PID-22.1=[ETHNICITY]\n";
          quickTransforms += "PID-22.2=[ETHNICITY_LABEL]\n";
          quickTransforms += "PID-22.3=CDCREC\n";
        } else if (extra.equals("2.5.1")) {
          quickTransforms += "MSH-2=^~\\&\n";
          quickTransforms += "MSH-7=[NOW]\n";
          quickTransforms += "MSH-9.1=VXU\n";
          quickTransforms += "MSH-9.2=V04\n";
          quickTransforms += "MSH-9.3=VXU_V04\n";
          quickTransforms += "MSH-10=" + actualTestCase + "."
              + makeBase62Number(System.currentTimeMillis() % 10000) + "\n";
          quickTransforms += "MSH-11=P\n";
          quickTransforms += "MSH-12=2.5.1\n";
          quickTransforms += "MSH-15=ER\n";
          quickTransforms += "MSH-16=AL\n";
          quickTransforms += "MSH-21.1=Z22\n";
          quickTransforms += "MSH-21.2=CDCPHINVS\n";
          quickTransforms += "PID-1=1\n";
          quickTransforms += "MSH-23\n";
          if (actualTestCase.length() > 15) {
            quickTransforms +=
                "PID-3.1=" + actualTestCase.substring(actualTestCase.length() - 15) + "\n";
          } else {
            quickTransforms += "PID-3.1=" + actualTestCase + "\n";
          }
          quickTransforms += "PID-3.4=AIRA-TEST\n";
          quickTransforms += "PID-3.5=MR\n";
          quickTransforms += "PD1-11.1=02\n\n";
          quickTransforms += "PD1-11.2=Reminder/Recall - any method\n\n";
          quickTransforms += "PD1-11.3=HL70215\n\n";
          quickTransforms += "PD1-11.1=02\n\n";
          quickTransforms += "PD1-16=A\n\n";
          quickTransforms += "PD1-17=[TODAY]\n\n";
          quickTransforms += "PD1-18=[TODAY]\n";
          quickTransforms += "NK1-1=1\n";
          quickTransforms += "NK1#2-1=2\n";
          quickTransforms += "PV1-1=1\n";
          quickTransforms += "PV1-2=R\n";
          quickTransforms += "ORC-1=RE\n";
          quickTransforms += "ORC-3.2=AIRA\n";
          quickTransforms += "RXA-1=0\n";
          quickTransforms += "RXA-2=1\n";
          quickTransforms += "RXA-6=999\n";
          quickTransforms += "ORC#2-1=RE\n";
          quickTransforms += "ORC#2-3.2=AIRA\n";
          quickTransforms += "RXA#2-1=0\n";
          quickTransforms += "RXA#2-2=1\n";
          quickTransforms += "RXA#2-6=999\n";
          quickTransforms += "ORC#3-1=RE\n";
          quickTransforms += "ORC#3-3.2=AIRA\n";
          quickTransforms += "RXA#3-1=0\n";
          quickTransforms += "RXA#3-2=1\n";
          quickTransforms += "RXA#3-6=999\n";
          quickTransforms += "RXR-1=OTH\n";
          quickTransforms += "RXR#2-1=OTH\n";
          quickTransforms += "RXR#3-1=OTH\n";
        } else if (extra.equals("2.3.1")) {
          quickTransforms += "MSH-2=^~\\&\n";
          quickTransforms += "MSH-7=[NOW]\n";
          quickTransforms += "MSH-9.1=VXU\n";
          quickTransforms += "MSH-9.2=V04\n";
          quickTransforms += "MSH-10=" + actualTestCase + "\n";
          quickTransforms += "MSH-11=P\n";
          quickTransforms += "MSH-12=2.3.1\n";
          quickTransforms += "PID-1=1\n";
          if (actualTestCase.length() > 15) {
            quickTransforms +=
                "PID-3.1=" + actualTestCase.substring(actualTestCase.length() - 15) + "\n";
          } else {
            quickTransforms += "PID-3.1=" + actualTestCase + "\n";
          }
          quickTransforms += "PID-3.5=MR\n";
          quickTransforms += "NK1-1=1\n";
          quickTransforms += "NK1#2-1=2\n";
          quickTransforms += "ORC-1=RE\n";
          quickTransforms += "RXA-1=0\n";
          quickTransforms += "RXA-2=1\n";
          quickTransforms += "RXA-6=999\n";
          quickTransforms += "RXA#2-1=0\n";
          quickTransforms += "RXA#2-2=1\n";
          quickTransforms += "RXA#2-6=999\n";
          quickTransforms += "RXA#3-1=0\n";
          quickTransforms += "RXA#3-2=1\n";
          quickTransforms += "RXA#3-6=999\n";
          quickTransforms += "RXR-1=OTH\n";
          quickTransforms += "RXR#2-1=OTH\n";
          quickTransforms += "RXR#3-1=OTH\n";
        } else if (extra.equals("UPDATE_MESSAGE_CONTROL_ID")) {
          quickTransforms += "MSH-10=" + actualTestCase + "\n";
        }
      }
    }
    return quickTransforms;
  }

  public String changeVac(String quickTransforms, String vaccineNumber) {
    quickTransforms += "ORC#" + vaccineNumber + "-2=[VAC1_ID]\n";
    quickTransforms += "ORC#" + vaccineNumber + "-3=[VAC1_ID]\n";
    quickTransforms += "RXA#" + vaccineNumber + "-3=[VAC3_DATE]\n";
    quickTransforms += "RXA#" + vaccineNumber + ":OBX#1-14=[VAC3_DATE]\n";
    return quickTransforms;
  }

  private String addDelete(String quickTransforms, String vaccineNumber, int count) {
    quickTransforms += "ORC#" + vaccineNumber + "-3=[VAC" + count + "_ID]\n";
    String front = "RXA#" + vaccineNumber;
    quickTransforms += front + "-3=[VAC" + count + "_DATE]\n";
    quickTransforms += front + "-9.1=00\n";
    quickTransforms += front + "-9.2=Administered\n";
    quickTransforms += front + "-9.3=NIP001\n";
    quickTransforms += front + "-5.1=[VAC" + count + "_CVX]\n";
    quickTransforms += front + "-5.2=[VAC" + count + "_CVX_LABEL]\n";
    quickTransforms += front + "-5.3=CVX\n";
    quickTransforms += front + "-6=[VAC2_AMOUNT]\n";
    quickTransforms += front + "-7.1=mL\n";
    quickTransforms += front + "-7.2=milliliters\n";
    quickTransforms += front + "-7.3=UCUM\n";
    quickTransforms += front + "-15=[VAC" + count + "_LOT]\n";
    quickTransforms += front + "-17.1=[VAC" + count + "_MVX]\n";
    quickTransforms += front + "-17.2=[VAC" + count + "_MVX_LABEL]\n";
    quickTransforms += front + "-17.3=MVX\n";
    quickTransforms += front + "-21=D\n";
    return quickTransforms;
  }

  private String addAdmin(String quickTransforms, String vaccineNumber, int count) {
    String front = "ORC#" + vaccineNumber;
    quickTransforms += front + "-3=[VAC" + count + "_ID]\n";
    quickTransforms += front + "-10.1=I-23432\n";
    quickTransforms += front + "-10.2=Burden\n";
    quickTransforms += front + "-10.3=Donna\n";
    quickTransforms += front + "-10.4=A\n";
    quickTransforms += front + "-10.9=NIST-AA-1\n";
    quickTransforms += front + "-10.13=PRN\n";
    quickTransforms += front + "-12.1=57422\n";
    quickTransforms += front + "-12.2=RADON\n";
    quickTransforms += front + "-12.3=NICHOLAS\n";
    quickTransforms += front + "-12.9=NIST-AA-1\n";
    quickTransforms += front + "-12.10=L\n";
    quickTransforms += front + "-12.13=PRN\n";
    front = "RXA#" + vaccineNumber;
    quickTransforms += front + "-3=[VAC" + count + "_DATE]\n";
    quickTransforms += front + "-9.1=00\n";
    quickTransforms += front + "-9.2=Administered\n";
    quickTransforms += front + "-9.3=NIP001\n";
    quickTransforms += front + "-5.1=[VAC" + count + "_CVX]\n";
    quickTransforms += front + "-5.2=[VAC" + count + "_CVX_LABEL]\n";
    quickTransforms += front + "-5.3=CVX\n";
    quickTransforms += front + "-6=[VAC" + count + "_AMOUNT]\n";
    quickTransforms += front + "-7.1=mL\n";
    quickTransforms += front + "-7.2=milliliters\n";
    quickTransforms += front + "-7.3=UCUM\n";
    quickTransforms += front + "-15=[VAC" + count + "_LOT]\n";
    quickTransforms += front + "-17.1=[VAC" + count + "_MVX]\n";
    quickTransforms += front + "-17.2=[VAC" + count + "_MVX_LABEL]\n";
    quickTransforms += front + "-17.3=MVX\n";
    quickTransforms += front + "-20=CP\n";
    quickTransforms += front + "-21=A\n";
    quickTransforms += front + ":RXR-1.1=[VAC" + count + "_ROUTE]\n";
    quickTransforms += front + ":RXR-1.2=\n";
    quickTransforms += front + ":RXR-1.3=NCIT\n";
    quickTransforms += front + ":RXR-2.1=[VAC" + count + "_SITE]\n";
    quickTransforms += front + ":RXR-2.2=\n";
    quickTransforms += front + ":RXR-2.3=HL70163\n";
    quickTransforms = addObx(quickTransforms, vaccineNumber, count);
    return quickTransforms;
  }

  private String addObx(String quickTransforms, String vaccineNumber, int count) {
    String start = "RXA#" + vaccineNumber + ":OBX#";
    quickTransforms += start + "1-1=1\n";
    quickTransforms += start + "1-2=CE\n";
    quickTransforms += start + "1-3.1=64994-7\n";
    quickTransforms += start + "1-3.2=Vaccine funding program eligibility category\n";
    quickTransforms += start + "1-3.3=LN\n";
    quickTransforms += start + "1-4=1\n";
    quickTransforms += start + "1-5.1=[VFC]\n";
    quickTransforms += start + "1-5.2=[VFC_LABEL]\n";
    quickTransforms += start + "1-5.3=HL70064\n";
    quickTransforms += start + "1-11=F\n";
    quickTransforms += start + "1-14=[TODAY]\n";
    quickTransforms += start + "1-17.1=VXC40\n";
    quickTransforms += start + "1-17.2=Eligibility captured at the immunization level\n";
    quickTransforms += start + "1-17.3=CDCPHINVS\n";
    quickTransforms += start + "2-1=2\n";
    quickTransforms += start + "2-2=CE\n";
    quickTransforms += start + "2-3.1=30956-7\n";
    quickTransforms += start + "2-3.2=Vaccine Type\n";
    quickTransforms += start + "2-3.3=LN\n";
    quickTransforms += start + "2-4=2\n";
    quickTransforms += start + "2-5.1=[VAC" + count + "_VIS_PUB_CODE]\n";
    quickTransforms += start + "2-5.2=[VAC" + count + "_VIS_PUB_NAME]\n";
    quickTransforms += start + "2-5.3=CVX\n";
    quickTransforms += start + "2-11=F\n";
    quickTransforms += start + "3-1=3\n";
    quickTransforms += start + "3-2=TS\n";
    quickTransforms += start + "3-3.1=29768-9\n";
    quickTransforms += start + "3-3.2=Date vaccine information statement published\n";
    quickTransforms += start + "3-3.3=LN\n";
    quickTransforms += start + "3-4=2\n";
    quickTransforms += start + "3-5.1=[VAC" + count + "_VIS_PUB_DATE]\n";
    quickTransforms += start + "3-11=F\n";
    quickTransforms += start + "4-1=4\n";
    quickTransforms += start + "4-2=TS\n";
    quickTransforms += start + "4-3.1=29769-7\n";
    quickTransforms += start + "4-3.2=Date vaccine information statement presented\n";
    quickTransforms += start + "4-3.3=LN\n";
    quickTransforms += start + "4-4=2\n";
    quickTransforms += start + "4-5.1=[VAC" + count + "_DATE]\n";
    quickTransforms += start + "4-11=F\n";
    return quickTransforms;
  }

  public void transform(TransformRequest transformRequest) {
    String resultTextOriginal = transformRequest.getResultText();
    try {
      Patient patient = setupPatient(transformRequest.getPatientType());
      transformRequest.setPatient(patient);

      BufferedReader inTransform =
          new BufferedReader(new StringReader(transformRequest.getTransformText()));
      String transformCommand;
      while ((transformCommand = inTransform.readLine()) != null) {
        transformCommand = transformCommand.trim();
        transformRequest.setLine(transformCommand);

        if (transformCommand.length() > 0) {
          boolean shouldSkipTransform = checkForAgeSkip(transformRequest);
          if (shouldSkipTransform) {
            continue;
          }

          if (transformCommand.toLowerCase().startsWith(INSERT_SEGMENT)) {
            doInsertSegment(transformRequest);
          } else if (transformCommand.toLowerCase().startsWith(REMOVE_SEGMENT)) {
            doRemoveSegment(transformRequest);
          } else if (transformCommand.toLowerCase().startsWith(REMOVE_REPEAT)) {
            doRemoveRepeat(transformRequest);
          } else if (transformCommand.toLowerCase().startsWith(REMOVE_OBSERVATION)) {
            doRemoveObservation(transformRequest);
          } else if (transformCommand.toLowerCase().startsWith(REMOVE_EMPTY_OBSERVATIONS)) {
            doRemoveEmptyObservations(transformRequest);
          } else if (transformCommand.toLowerCase().trim().startsWith(FIX)) {
            doFix(transformRequest);
          } else if (transformCommand.toLowerCase().trim().startsWith(CLEAN)) {
            doClean(transformRequest);
          } else if (transformCommand.toLowerCase().trim().startsWith(CLEAR)) {
            doClear(transformRequest);
          } else if (transformCommand.toLowerCase().trim().startsWith(RUN_PROCEDURE)) {
            doRunProcedure(transformRequest);
          } else if (transformCommand.toLowerCase().trim().startsWith(SET)) {
            doSetVariable(transformRequest);
          } else {
            doSetField(transformRequest);
          }
        }
      }
    } catch (Exception e) {
      StringWriter stringWriter = new StringWriter();
      PrintWriter out = new PrintWriter(stringWriter);
      e.printStackTrace(out);
      transformRequest.setResultText(resultTextOriginal);
      transformRequest.setHasException(true);
      transformRequest.setException(e);
    }
  }

  public boolean checkForAgeSkip(TransformRequest transformRequest) throws IOException {
    String line = transformRequest.getLine();
    boolean shouldSkipTransform = false;
    int if18plus = line.indexOf(IF_18_PLUS);
    int if19plus = line.indexOf(IF_19_PLUS);
    int if18minus = line.indexOf(IF_18_MINUS);
    int if19minus = line.indexOf(IF_19_MINUS);
    if (if18plus > 0 || if19plus > 0 || if18minus > 0 || if19minus > 0) {
      String dobString =
          readValueFromHL7Text("PID-7", transformRequest.getResultText(), transformRequest);
      if (dobString.length() > 8) {
        dobString = dobString.substring(0, 8);
      }
      if (dobString.length() == 8) {
        int dobInt = Integer.parseInt(dobString);
        int ageInt = 180000;
        if (if19plus > 0 || if19minus > 0) {
          ageInt = 190000;
        }
        ageInt += dobInt;
        int todayInt = Integer.parseInt(transformRequest.getToday());
        if (if18plus > 0 || if19plus > 0) {
          if (todayInt < ageInt) {
            shouldSkipTransform = true;
          }
        }
        if (if18minus > 0 || if19minus > 0) {
          if (todayInt >= ageInt) {
            shouldSkipTransform = true;
          }
        }
      }
      if (!shouldSkipTransform) {
        if (if18plus > 0) {
          line = line.substring(0, if18plus - 1);
        } else if (if19plus > 0) {
          line = line.substring(0, if19plus - 1);
        } else if (if18minus > 0) {
          line = line.substring(0, if18minus - 1);
        } else if (if19minus > 0) {
          line = line.substring(0, if19minus - 1);
        }
        transformRequest.setLine(line);
      }
    }
    return shouldSkipTransform;
  }
  
  protected boolean checkForSpecialVaccineModifierSkip(
      TransformRequest transformRequest,
      int transformRepeat) throws IOException {
    
    if (!transformRequest.isAdministeredCheck()
        && !transformRequest.isHistoricalCheck()
        && !transformRequest.isRefusalCheck()
        && !transformRequest.isNonAdminCheck()) {
      // don't skip because there was no special vaccine modifier, just a normal transform
      return false;
    }
    
    Transform rxa9transform = readHL7Reference("RXA-9", "RXA-9".length());
    rxa9transform.setSegmentRepeat(transformRepeat);
    
    String rxa9 = getValueFromHL7(transformRequest.getResultText(), rxa9transform, transformRequest);
    
    if (transformRequest.isAdministeredCheck() && "00".equals(rxa9)) {
      return false;
    }
    
    if (transformRequest.isHistoricalCheck() && ("01".equals(rxa9) || "02".equals(rxa9) ||"03".equals(rxa9) || "04".equals(rxa9) ||"05".equals(rxa9) || "06".equals(rxa9) ||"07".equals(rxa9) || "08".equals(rxa9))) {
      return false;
    }
    
    Transform rxa20transform = readHL7Reference("RXA-20", "RXA-20".length());
    rxa20transform.setSegmentRepeat(transformRepeat);
    
    String rxa20 = getValueFromHL7(transformRequest.getResultText(), rxa20transform, transformRequest);
    
    if (transformRequest.isRefusalCheck() && StringUtils.isBlank(rxa9) && "RE".equals(rxa20)) {
      return false;
    }
    
    if (transformRequest.isNonAdminCheck() && StringUtils.isBlank(rxa9) && "NA".equals(rxa20)) {
      return false;
    }
    
    // skip this transform because there was a special vaccine modifier but the secondary criteria didn't match
    return true;
  }
  
  protected String cleanTransformLineOfIfChecks(TransformRequest transformRequest) {
    String line = transformRequest.getLine();
    
    if (transformRequest.isAdministeredCheck()) {
      line = line.substring(0, line.indexOf(IF_ADMINISTERED) - 1);
    }
    
    if (transformRequest.isHistoricalCheck()) {
      line = line.substring(0, line.indexOf(IF_HISTORICAL) - 1);
    }
    
    if (transformRequest.isRefusalCheck()) {
      line = line.substring(0, line.indexOf(IF_REFUSAL) - 1);
    }
    
    if (transformRequest.isNonAdminCheck()) {
      line = line.substring(0, line.indexOf(IF_NON_ADMIN) - 1);
    }
    
    return line;
  }

  public void doSetField(TransformRequest transformRequest) throws IOException {
    String resultText = transformRequest.getResultText();
    
    // update with any if checks
    transformRequest.setAdministeredCheck(transformRequest.getLine().indexOf(IF_ADMINISTERED) > 0);
    transformRequest.setHistoricalCheck(transformRequest.getLine().indexOf(IF_HISTORICAL) > 0);
    transformRequest.setRefusalCheck(transformRequest.getLine().indexOf(IF_REFUSAL) > 0);
    transformRequest.setNonAdminCheck(transformRequest.getLine().indexOf(IF_NON_ADMIN) > 0);
    
    // remove if checks from the transform request line
    transformRequest.setLine(cleanTransformLineOfIfChecks(transformRequest));
    
    String line = transformRequest.getLine();
    
    int posEqual = line.indexOf("=");
    Transform transform = readHL7Reference(line, posEqual);
    if (transform != null) {
      transform.value = line.substring(posEqual + 1).trim();
      
      int count = 1;
      if (transform.all) {
        count = countSegments(resultText, transform);
      }
      
      for (int i = 1; i <= count; i++) {
        if (transform.all) {
          transform.segmentRepeat = i;
        }
        
        if (checkForSpecialVaccineModifierSkip(transformRequest, transform.getSegmentRepeat())) {
          continue;
        }
        
        handleSometimes(transform);
        doReplacements(transform, transformRequest);
        resultText = setValueInHL7(resultText, transform, transformRequest);
      }
    }
    transformRequest.setResultText(resultText);
  }

  public void doSetVariable(TransformRequest transformRequest) throws IOException {
    if (transformRequest.getCurrentTestCaseMessage() == null) {
      // do nothing if test case message is null
      return;
    }

    String line = transformRequest.getLine();

    int posEqual = line.indexOf("=");
    if (posEqual < 1) {
      return;
    }

    String command = line.substring(SET.length()).trim();
    String[] parts = command.split("\\=");
    String name = parts[0];

    transformRequest.setLine(parts[1]);
    Transform transform = readHL7Reference(line, 0);
    transform.value = parts[1];
    doReplacements(transform, transformRequest);
    String value = transform.value;

    Map<String, String> variables = transformRequest.getCurrentTestCaseMessage().getVariables();
    variables.put(name, value);
  }

  public void doClear(TransformRequest transformRequest) throws IOException {
    String resultText = transformRequest.getResultText();
    String line = transformRequest.getLine();
    if (line.toLowerCase().startsWith(CLEAR)) {
      line = line.substring(CLEAR.length()).trim();
      if (line.length() > 0) {
        Transform t = readHL7Reference(line);
        if (t != null) {
          int count = 1;
          if (t.all) {
            count = countSegments(resultText, t);
          }
          for (int i = 1; i <= count; i++) {
            if (t.all) {
              t.segmentRepeat = i;
            }
            resultText = clearValueInHL7(resultText, t);
          }
        }
        transformRequest.setResultText(resultText);
      }
    }
  }

  public void doClean(TransformRequest transformRequest) throws IOException {
    String line = transformRequest.getLine();
    String resultText = transformRequest.getResultText();
    boolean noLastSlash = line.toLowerCase().indexOf(CLEAN_NO_LAST_SLASH) != -1;
    BufferedReader inResult = new BufferedReader(new StringReader(resultText));
    resultText = "";
    String lineResult;
    while ((lineResult = inResult.readLine()) != null) {
      lineResult = lineResult.trim();
      if (lineResult.length() > 0) {
        String finalLine = "";

        String headerStart = null;
        if (lineResult.startsWith("MSH|^~\\&|") || lineResult.startsWith("BHS|^~\\&|")
            || lineResult.startsWith("FHS|^~\\&|")) {
          headerStart = lineResult.substring(0, 9);
          lineResult = lineResult.substring(9);
        }

        boolean foundFieldData = false;
        boolean foundCompData = false;
        boolean foundRepData = false;

        for (int i = lineResult.length() - 1; i >= 0; i--) {
          char c = lineResult.charAt(i);

          if (!foundFieldData) {
            if (c != '|' && c != '^' && c != '~') {
              foundFieldData = true;
              foundRepData = true;
              foundCompData = true;
            }
          } else if (!foundRepData) {
            if (c != '^' && c != '~') {
              foundRepData = true;
              foundCompData = true;
            }
          } else if (!foundCompData) {
            if (c != '^') {
              foundCompData = true;
            }
          }
          if (foundFieldData) {
            if (c == '|') {
              foundRepData = false;
              foundCompData = false;
              finalLine = c + finalLine;
            } else if (c == '~') {
              if (foundRepData) {
                finalLine = c + finalLine;
              }
              foundCompData = false;
            } else if (c == '^') {
              if (foundCompData) {
                finalLine = c + finalLine;
              }
            } else {
              finalLine = c + finalLine;
            }
          }
        }
        if (noLastSlash) {
          resultText += finalLine + transformRequest.getSegmentSeparator();
        } else {
          resultText += finalLine + "|" + transformRequest.getSegmentSeparator();
        }

        if (headerStart != null) {
          resultText = headerStart + resultText;
        }
      }
    }
    transformRequest.setResultText(resultText);
  }

  public void doFix(TransformRequest transformRequest) throws IOException {
    String line = transformRequest.getLine();
    String resultText = transformRequest.getResultText();
    if (resultText.length() > 9) {
      if (line.toLowerCase().indexOf(FIX_ESCAPE) > 0) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < resultText.length(); i++) {
          char c = resultText.charAt(i);
          if (i < 8 || c != '\\') {
            sb.append(c);
          } else {
            if ((i + 2) >= resultText.length() || resultText.charAt(i + 2) != '\\') {
              sb.append("\\E\\");
            } else {
              sb.append(c);
            }
          }
        }
        resultText = sb.toString();
      }
      if (line.toLowerCase().indexOf(FIX_AMPERSAND) > 0) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < resultText.length(); i++) {
          char c = resultText.charAt(i);
          if (i < 8 || c != '&') {
            sb.append(c);
          } else {
            sb.append("\\T\\");
          }
        }
        resultText = sb.toString();
      }
      if (line.toLowerCase().indexOf(FIX_MISSING_MOTHER_MAIDEN_FIRST) > 0) {
        String motherMaidenFirst = readValueFromHL7Text("PID-6.2", resultText, transformRequest);
        String motherMaidenLast = readValueFromHL7Text("PID-6.1", resultText, transformRequest);
        boolean needToClear = true;
        if (!motherMaidenLast.equals("")) {
          if (!motherMaidenFirst.equals("")) {
            needToClear = false;
          } else {
            String guardianType = readValueFromHL7Text("NK1-3.1", resultText, transformRequest);
            motherMaidenFirst = readValueFromHL7Text("NK1-2.2", resultText, transformRequest);
            int pos = 1;
            while (!guardianType.equals("") && !guardianType.equals("MTH")) {
              pos++;
              guardianType =
                  readValueFromHL7Text("NK1#" + pos + "-3.1", resultText, transformRequest);
              motherMaidenFirst =
                  readValueFromHL7Text("NK1#" + pos + "-2.2", resultText, transformRequest);
            }
            if (guardianType.equals("MTH")) {
              resultText =
                  setValueInHL7("PID-6.2", motherMaidenFirst, resultText, transformRequest);
              needToClear = false;
            }
          }
        }
        if (needToClear) {
          resultText = setValueInHL7("PID-6.1", "", resultText, transformRequest);
          resultText = setValueInHL7("PID-6.2", "", resultText, transformRequest);
        }
      }
    }
    transformRequest.setResultText(resultText);
  }

  public void doRemoveEmptyObservations(TransformRequest transformRequest) throws IOException {
    String resultText = transformRequest.getResultText();
    BufferedReader inResult = new BufferedReader(new StringReader(resultText));
    resultText = "";
    String lineResult;
    while ((lineResult = inResult.readLine()) != null) {
      lineResult = lineResult.trim();
      if (lineResult.length() > 0) {
        if (lineResult.startsWith("OBX")) {
          String[] fields = lineResult.split("\\|");
          if (fields.length > 5 && fields[5] != null && !fields[5].startsWith("^")
              && !fields[5].startsWith("~") && !fields[5].equals("")) {
            resultText += lineResult + transformRequest.getSegmentSeparator();
          }
        } else {
          resultText += lineResult + transformRequest.getSegmentSeparator();
        }
      }
    }
    transformRequest.setResultText(resultText);
  }

  public void doRemoveObservation(TransformRequest transformRequest) throws IOException {
    String line = transformRequest.getLine();
    String resultText = transformRequest.getResultText();
    line = line.substring(REMOVE_OBSERVATION.length()).trim();
    if (line.length() >= 3) {
      int nextSpace = line.indexOf(" ");
      if (nextSpace == -1) {
        nextSpace = line.length();
      }
      String obsCode = line.substring(0, nextSpace);
      line = line.substring(obsCode.length()).trim();

      BufferedReader inResult = new BufferedReader(new StringReader(resultText));
      resultText = "";
      String lineResult;
      while ((lineResult = inResult.readLine()) != null) {
        lineResult = lineResult.trim();
        if (lineResult.length() > 0) {
          if (lineResult.startsWith("OBX")) {
            String[] fields = lineResult.split("\\|");
            if (fields.length <= 3 || fields[3] == null || (!fields[3].equalsIgnoreCase(obsCode)
                && !fields[3].toLowerCase().startsWith(obsCode.toLowerCase() + "^"))) {
              resultText += lineResult + transformRequest.getSegmentSeparator();
            }
          } else {
            resultText += lineResult + transformRequest.getSegmentSeparator();
          }
        }
      }
    }
    transformRequest.setResultText(resultText);
  }

  public void doRunProcedure(TransformRequest transformRequest) throws IOException {
    String line = transformRequest.getLine();
    line = line.substring(RUN_PROCEDURE.length()).trim();
    if (line.length() >= 3) {
      int nextSpace = line.indexOf(" ");
      if (nextSpace == -1) {
        nextSpace = line.length();
      }
      String procedureName = line.substring(0, nextSpace);
      ProcedureInterface procedure = ProcedureFactory.getProcedure(procedureName, this);
      if (procedure != null) {
        line = line.substring(nextSpace).trim();
        transformRequest.setLine(line);
        LinkedList<String> linkedList = createTokenList(line);
        procedure.doProcedure(transformRequest, linkedList);
      }
    }
  }

  protected static LinkedList<String> createTokenList(String line) {
    String tokenString = "";
    LinkedList<String> linkedList = new LinkedList<String>();
    boolean singleQuoted = false;
    for (int pos = 0; pos < line.length(); pos++) {
      char c = line.charAt(pos);
      char peak = (pos + 1) < line.length() ? line.charAt(pos + 1) : 0;
      if (!singleQuoted && c == ' ') {
        if (!tokenString.equals("")) {
          linkedList.add(tokenString);
        }
        tokenString = "";
      } else if (c == '\'') {
        if (!singleQuoted) {
          if (tokenString.equals("")) {
            singleQuoted = true;
          } else {
            tokenString = tokenString + c;
          }
        } else {
          if (peak == '\'') {
            tokenString = tokenString + c;
            pos++;
          } else if (peak == 0 || peak == ' ') {
            singleQuoted = false;
          }
        }
      } else {
        tokenString = tokenString + c;
      }
    }
    if (!tokenString.equals("")) {
      linkedList.add(tokenString);
    }
    return linkedList;
  }

  public void doRemoveSegment(TransformRequest transformRequest) throws IOException {
    String line = transformRequest.getLine();
    String resultText = transformRequest.getResultText();
    line = line.substring(REMOVE_SEGMENT.length()).trim();
    if (line.length() >= 3) {
      int nextSpace = line.indexOf(" ");
      if (nextSpace == -1) {
        nextSpace = line.length();
      }
      String removeSegmentName = line.substring(0, nextSpace);
      line = line.substring(removeSegmentName.length()).trim();
      int repeatPos = 0;
      int poundPos = removeSegmentName.indexOf("#");
      if (poundPos == -1) {
        repeatPos = 1;
      } else {
        try {
          repeatPos = Integer.parseInt(removeSegmentName.substring(poundPos + 1).trim());
        } catch (NumberFormatException nfe) {
          repeatPos = 1;
        }
        removeSegmentName = removeSegmentName.substring(0, poundPos);
      }

      String removeAction = line.trim();
      boolean all = false;
      if (removeAction.equalsIgnoreCase("all")) {
        all = true;
      }

      BufferedReader inResult = new BufferedReader(new StringReader(resultText));
      resultText = "";
      String lineResult;
      int repeatCount = 0;
      while ((lineResult = inResult.readLine()) != null) {
        lineResult = lineResult.trim();
        if (lineResult.length() > 0) {
          if (lineResult.startsWith(removeSegmentName)) {
            repeatCount++;
            if (!all && repeatCount != repeatPos) {
              resultText += lineResult + transformRequest.getSegmentSeparator();
            }
          } else {
            resultText += lineResult + transformRequest.getSegmentSeparator();
          }
        }
      }
    }
    transformRequest.setResultText(resultText);
  }

  public void doRemoveRepeat(TransformRequest transformRequest) throws IOException {
    // remove repeat PID-5.5 valued MA
    // remove repeat PID-5#1
    // remove repeat RXA-9.1 all valued 01
    String line = transformRequest.getLine();
    String resultText = transformRequest.getResultText();
    line = line.substring(REMOVE_REPEAT.length()).trim();
    if (line.length() >= 3) {
      String hl7Ref = "";
      String valued = null;
      int valuedPos = line.toLowerCase().indexOf("valued");
      if (valuedPos == -1) {
        hl7Ref = line;
      } else {
        valued = line.substring(valuedPos + "valued".length()).trim();
        hl7Ref = line.substring(0, valuedPos).trim();
      }
      boolean all = false;
      if (hl7Ref.endsWith("all")) {
        all = true;
        hl7Ref = hl7Ref.substring(0, hl7Ref.length() - 3).trim();
      }
      Transform t = readHL7Reference(hl7Ref);
      BufferedReader inResult = new BufferedReader(new StringReader(resultText));
      resultText = "";
      String lineResult;
      int repeatCount = 0;
      while ((lineResult = inResult.readLine()) != null) {
        lineResult = lineResult.trim();
        if (lineResult.length() > 0) {
          if (lineResult.startsWith(t.segment)) {
            repeatCount++;
            if (all || repeatCount == t.segmentRepeat) {
              int fieldStartPos = 0;
              int fieldEndPos = 0;
              int currentField = 0;
              int nextBar = lineResult.indexOf("|");
              while (nextBar > 0 && currentField < t.field) {
                fieldStartPos = nextBar + 1;
                nextBar = lineResult.indexOf("|", nextBar + 1);
                fieldEndPos = nextBar;
                currentField++;
              }
              if (currentField == t.field) {
                if (fieldEndPos == -1) {
                  fieldEndPos = lineResult.length();
                }
                if (fieldStartPos < fieldEndPos) {
                  String fieldOriginal = lineResult.substring(fieldStartPos, fieldEndPos);
                  String fieldFinal = fieldOriginal;
                  if (valued == null) {
                    int tildeStartPos = 0;
                    int tildeEndPos = 0;
                    int nextTilde = fieldOriginal.indexOf("~");
                    int currentRepeat = 1;
                    while (nextTilde > 0 && currentRepeat < t.fieldRepeat) {
                      tildeStartPos = nextTilde;
                      nextTilde = fieldOriginal.indexOf("~", nextTilde + 1);
                      currentRepeat++;
                    }
                    if (currentRepeat == t.fieldRepeat) {
                      if (nextTilde == -1) {
                        nextTilde = fieldOriginal.length();
                      }
                      tildeEndPos = nextTilde;
                      if (tildeStartPos == 0) {
                        if (tildeEndPos < fieldOriginal.length()) {
                          tildeEndPos++;
                        }
                        fieldFinal = fieldOriginal.substring(tildeEndPos);
                      } else {
                        fieldFinal = fieldOriginal.substring(0, tildeStartPos)
                            + fieldOriginal.substring(tildeEndPos);
                      }
                    }
                  } else {
                    int tildeStartPos = 0;
                    int tildeEndPos = 0;
                    int nextTilde = fieldOriginal.indexOf("~");
                    if (nextTilde == -1) {
                      nextTilde = fieldOriginal.length();
                    }
                    boolean foundIt = false;
                    while (tildeStartPos < nextTilde) {
                      String repeatValue = fieldOriginal.substring(tildeStartPos + 1, nextTilde);
                      int subStartPos = 0;
                      int subEndPos = 0;
                      int nextCaret = repeatValue.indexOf("^");
                      int currentSub = 1;
                      while (nextCaret != -1 && currentSub < t.subfield) {
                        subStartPos = nextCaret;
                        nextCaret = repeatValue.indexOf("^", nextCaret + 1);
                        currentSub++;
                      }
                      if (currentSub == t.subfield && subStartPos != -1) {
                        if (nextCaret == -1) {
                          nextCaret = repeatValue.length();
                        }
                        subEndPos = nextCaret;
                        String subValue = repeatValue.substring(subStartPos + 1, subEndPos);
                        if (subValue.equalsIgnoreCase(valued)) {
                          foundIt = true;
                          break;
                        }
                      }
                      tildeStartPos = nextTilde;
                      nextTilde = fieldOriginal.indexOf("~", nextTilde + 1);
                      if (nextTilde == -1) {
                        nextTilde = fieldOriginal.length();
                      }
                    }
                    if (foundIt) {
                      tildeEndPos = nextTilde;
                      if (tildeStartPos == 0) {
                        if (tildeEndPos < fieldOriginal.length()) {
                          tildeEndPos++;
                        }
                        fieldFinal = fieldOriginal.substring(tildeEndPos);
                      } else {
                        fieldFinal = fieldOriginal.substring(0, tildeStartPos)
                            + fieldOriginal.substring(tildeEndPos);
                      }
                    }
                  }
                  lineResult = lineResult.substring(0, fieldStartPos) + fieldFinal
                      + lineResult.substring(fieldEndPos);
                }
              }
            }
          }
          resultText += lineResult + transformRequest.getSegmentSeparator();
        }
      }
    }
    transformRequest.setResultText(resultText);
  }

  public void doInsertSegment(TransformRequest transformRequest) throws IOException {
    String resultText = transformRequest.getResultText();
    String line = transformRequest.getLine();
    line = line.substring(INSERT_SEGMENT.length()).trim();

    if (line.length() > 3) {
      int endOfSegmentNamePos = line.indexOf(" ");
      if (endOfSegmentNamePos == -1) {
        endOfSegmentNamePos = line.length();
      }
      String newSegmentNameString = line.substring(0, endOfSegmentNamePos);
      line = line.substring(newSegmentNameString.length()).trim();

      String[] newSegmentNames = newSegmentNameString.split("\\,");
      if (newSegmentNames.length > 0 && newSegmentNames[0].length() == 3) {
        boolean insertIfMissing = false;
        boolean insertIfMissingInMessage = false;
        int segmentIfMissingInMesagePos = line.indexOf(INSERT_SEGMENT_IF_MISSING_FROM_MESSAGE);
        if (segmentIfMissingInMesagePos > 0) {
          insertIfMissingInMessage = true;
          line = line.substring(0, segmentIfMissingInMesagePos - 1);
        }
        int segmentIfMissingPos = line.indexOf(INSERT_SEGMENT_IF_MISSING);
        if (segmentIfMissingPos > 0) {
          insertIfMissing = true;
          line = line.substring(0, segmentIfMissingPos - 1);
        }

        boolean okayToInsert = true;
        if (insertIfMissingInMessage) {
          BufferedReader inResult = new BufferedReader(new StringReader(resultText));
          String lineResult;
          boolean foundIt = false;
          while (!foundIt && (lineResult = inResult.readLine()) != null) {
            lineResult = lineResult.trim();
            if (lineResult.length() > 0) {
              for (String newSegmentName : newSegmentNames) {
                if (lineResult.startsWith(newSegmentName + "|")) {
                  foundIt = true;
                  break;
                }
              }
            }
          }
          inResult.close();
          if (foundIt) {
            okayToInsert = false;
          }
        }
        if (okayToInsert) {
          String segmentToAdd = "";
          for (String newSegmentName : newSegmentNames) {
            if (newSegmentName.equals("FHS") || newSegmentName.equals("BHS")) {
              BufferedReader inResult = new BufferedReader(new StringReader(resultText));
              String lineResult;
              String msh = null;
              while ((lineResult = inResult.readLine()) != null) {
                lineResult = lineResult.trim();
                if (lineResult.length() > 0) {
                  if (lineResult.startsWith("MSH|")) {
                    msh = lineResult;
                  }
                }
              }
              if (msh != null && msh.startsWith("MSH|")) {
                String[] mshFields = msh.split("\\|");
                if (mshFields.length > 1 && mshFields[1] != null) {
                  segmentToAdd += newSegmentName + "|" + mshFields[1];
                  // MSH-3
                  if (mshFields.length > 2 && mshFields[2] != null) {
                    segmentToAdd += "|" + mshFields[2];
                    // MSH-4
                    if (mshFields.length > 3 && mshFields[3] != null) {
                      segmentToAdd += "|" + mshFields[3];
                      // MSH-5
                      if (mshFields.length > 4 && mshFields[4] != null) {
                        segmentToAdd += "|" + mshFields[4];
                        // MSH-6
                        if (mshFields.length > 5 && mshFields[5] != null) {
                          segmentToAdd += "|" + mshFields[5];
                          // MSH-7
                          if (mshFields.length > 6 && mshFields[6] != null) {
                            segmentToAdd += "|" + mshFields[6];
                            // MSH-10
                            if (mshFields.length > 9 && mshFields[9] != null) {
                              segmentToAdd += "||||" + mshFields[9];
                            }
                          }
                        }
                      }
                    }
                  }
                  segmentToAdd += "|" + transformRequest.getSegmentSeparator();
                } else {
                  segmentToAdd +=
                      newSegmentName + "|^~\\&|" + transformRequest.getSegmentSeparator();
                }
              } else {
                segmentToAdd += newSegmentName + "|^~\\&|" + transformRequest.getSegmentSeparator();
              }
              inResult.close();
            } else {
              segmentToAdd += newSegmentName + "|" + transformRequest.getSegmentSeparator();
            }
          }

          int nextSpace = line.indexOf(" ");
          if (nextSpace == -1) {
            nextSpace = line.length();
          }
          String insertAction = line.substring(0, nextSpace);
          line = line.substring(nextSpace).trim();
          if (insertAction.equalsIgnoreCase(INSERT_SEGMENT_FIRST)) {
            resultText = segmentToAdd + resultText;
          } else if (insertAction.equalsIgnoreCase(INSERT_SEGMENT_LAST)) {
            resultText = resultText + segmentToAdd;
          } else if (insertAction.equalsIgnoreCase(INSERT_SEGMENT_AFTER)
              || insertAction.equalsIgnoreCase(INSERT_SEGMENT_BEFORE)) {
            String boundSegment = null;
            {
              int boundPos = line.indexOf(":");
              if (boundPos != -1) {
                boundSegment = line.substring(boundPos + 1).trim();
                line = line.substring(0, boundPos);
              }
            }
            int repeatPos = 0;
            int poundPos = line.indexOf("#");
            if (poundPos == -1) {
              repeatPos = 1;
            } else {
              try {
                repeatPos = Integer.parseInt(line.substring(poundPos + 1).trim());
              } catch (NumberFormatException nfe) {
                repeatPos = 1;
              }
              line = line.substring(0, poundPos);
            }
            BufferedReader inResult = new BufferedReader(new StringReader(resultText));
            resultText = "";
            String lineResultPrevious = "";
            String lineResult;
            String lineResultPeak = inResult.readLine();
            int repeatCount = 0;
            boolean triedToInsert = false;
            while ((lineResult = lineResultPeak) != null) {
              lineResultPeak = inResult.readLine();
              lineResult = lineResult.trim();
              if (lineResult.length() > 0) {
                if (insertAction.equalsIgnoreCase(INSERT_SEGMENT_AFTER)) {
                  resultText += lineResult + transformRequest.getSegmentSeparator();
                }
                if (boundSegment == null) {
                  if (lineResult.startsWith(line)) {
                    repeatCount++;
                    if (repeatCount == repeatPos) {
                      okayToInsert = true;
                      if (insertIfMissing) {
                        if (insertAction.equalsIgnoreCase(INSERT_SEGMENT_AFTER)) {
                          okayToInsert = lineResultPeak == null
                              || !lineResultPeak.startsWith(newSegmentNames[0]);
                        } else if (insertAction.equalsIgnoreCase(INSERT_SEGMENT_BEFORE)) {
                          okayToInsert = !lineResultPrevious.startsWith(newSegmentNames[0])
                              && !lineResultPrevious
                                  .startsWith(newSegmentNames[newSegmentNames.length - 1]);
                        }
                      }
                      if (okayToInsert) {
                        resultText += segmentToAdd;
                      }
                    }
                  }
                } else if (!triedToInsert) {
                  if (lineResult.startsWith(line)) {
                    repeatCount++;
                  } else if (repeatCount == repeatPos && lineResult.startsWith(boundSegment)) {
                    triedToInsert = true;
                    okayToInsert = true;
                    if (insertIfMissing) {
                      if (insertAction.equalsIgnoreCase(INSERT_SEGMENT_AFTER)) {
                        okayToInsert = lineResultPeak == null
                            || !lineResultPeak.startsWith(newSegmentNames[0]);
                      } else if (insertAction.equalsIgnoreCase(INSERT_SEGMENT_BEFORE)) {
                        okayToInsert = !lineResultPrevious.startsWith(newSegmentNames[0])
                            && !lineResultPrevious
                                .startsWith(newSegmentNames[newSegmentNames.length - 1]);
                      }
                    }
                    if (okayToInsert) {
                      resultText += segmentToAdd;
                    }
                  }
                }
                if (insertAction.equalsIgnoreCase(INSERT_SEGMENT_BEFORE)) {
                  resultText += lineResult + transformRequest.getSegmentSeparator();
                }
              }
              lineResultPrevious = lineResult;
            }
            inResult.close();
          }
        }
      }
    }
    transformRequest.setResultText(resultText);
  }

  public String setValueInHL7(String ref, String value, String resultText,
      TransformRequest transformRequest) throws IOException {
    Transform transform = readHL7Reference(ref, ref.length());
    transform.value = value;
    resultText = setValueInHL7(resultText, transform, transformRequest);
    return resultText;
  }

  public String readValueFromHL7Text(String ref, String resultText,
      TransformRequest transformRequest) throws IOException {
    Transform transform = readHL7Reference(ref, ref.length());
    String value = getValueFromHL7(resultText, transform, transformRequest);
    return value;
  }

  /**
   * This modifies the date to meet the non-standard format expected by WebIZ for MSH-7. Their only
   * deviation is they do not expect a period between the seconds and the milliseconds
   * 
   * @param resultText
   * @return
   */
  public String modifyDtmForWebiz(String resultText) {
    String dateTimeString = resultText;
    String secondsString = "";
    String timeZoneString = "";
    // 20141024071537.0001-0400
    int dashPos = dateTimeString.indexOf("-");
    if (dashPos != -1) {
      timeZoneString = dateTimeString.substring(dashPos + 1);
      dateTimeString = dateTimeString.substring(0, dashPos);
    }
    int periodPos = dateTimeString.indexOf(".");
    if (periodPos != -1) {
      secondsString = dateTimeString.substring(periodPos + 1);
      dateTimeString = dateTimeString.substring(0, periodPos);
    }
    dateTimeString = (dateTimeString + "00000000000000").substring(0, 14);
    secondsString = (secondsString + "0000").substring(0, 4);
    timeZoneString = (timeZoneString + "0000").substring(0, 4);
    resultText = dateTimeString + secondsString + "-" + timeZoneString;
    return resultText;
  }

  public int countSegments(String resultText, Transform t) throws IOException {
    BufferedReader inResult = new BufferedReader(new StringReader(resultText));
    String lineResult;
    int count = 0;
    while ((lineResult = inResult.readLine()) != null) {
      lineResult = lineResult.trim();
      if (lineResult.startsWith(t.segment + "|")) {
        count++;
      }
    }
    return count;
  }

  private String setValueInHL7(String resultText, Transform transform, TransformRequest transformRequest)
      throws IOException {

    BufferedReader inResult = new BufferedReader(new StringReader(resultText));
    boolean foundBoundStart = false;
    boolean foundBoundEnd = false;
    int boundCount = 0;
    resultText = "";
    String lineResult;
    int repeatCount = 0;
    String newValue = transform.value;
    String prepend = "";
    while ((lineResult = inResult.readLine()) != null) {
      lineResult = lineResult.trim();
      if (lineResult.length() > 0) {
        if (transform.boundSegment != null && !foundBoundEnd) {
          boolean skip = false;
          if (lineResult.startsWith(transform.boundSegment + "|")) {
            boundCount++;
            if (!foundBoundStart) {
              if (boundCount == transform.boundRepeat) {
                foundBoundStart = true;
              }
            } else if (foundBoundStart) {
              foundBoundEnd = true;
            }
            skip = true;
          } else if (foundBoundStart) {
            if (!lineResult.startsWith(transform.segment + "|")) {
              skip = true;
            }
          } else {
            skip = true;
          }
          if (skip) {
            resultText += lineResult + transformRequest.getSegmentSeparator();
            continue;
          }
        }
        if (lineResult.startsWith(transform.segment + "|")) {
          repeatCount++;
          if (transform.segmentRepeat == repeatCount) {

            if (!transform.isFieldSet()) {
              // This is a segment only reference
              lineResult = transform.segment + newValue;
            } else {

              int pos = lineResult.indexOf("|");
              int count = (lineResult.startsWith("MSH|") || lineResult.startsWith("FHS|")
                  || lineResult.startsWith("BHS|")) ? 2 : 1;
              while (pos != -1 && count < transform.field) {
                pos = lineResult.indexOf("|", pos + 1);
                count++;
              }
              if (pos == -1) {
                while (count < transform.field) {
                  lineResult += "|";
                  count++;
                }
                pos = lineResult.length();
                lineResult += "||";
              }

              boolean isMSH2 = lineResult.startsWith("MSH|") && transform.field == 2;
              count = 1;
              pos++;
              int tildePos = pos;
              while (tildePos != -1 && count < transform.fieldRepeat) {
                int endPosTilde = isMSH2 ? -1 : lineResult.indexOf("~", tildePos);
                int endPosBar = lineResult.indexOf("|", tildePos);
                if (endPosBar == -1) {
                  endPosBar = lineResult.length();
                }
                if (endPosTilde == -1 || endPosTilde >= endPosBar) {
                  tildePos = -1;
                  pos = endPosBar;
                } else {
                  tildePos = endPosTilde + 1;
                  pos = tildePos;
                  count++;
                }
              }
              if (tildePos == -1) {
                while (count < transform.fieldRepeat) {
                  prepend = "~" + prepend;
                  count++;
                }
              }

              count = 1;
              while (pos != -1 && count < transform.subfield) {
                int posCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
                int endPosBar = lineResult.indexOf("|", pos);
                if (endPosBar == -1) {
                  endPosBar = lineResult.length();
                }
                int endPosTilde = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                if (endPosTilde == -1) {
                  endPosTilde = lineResult.length();
                }
                if (posCaret == -1 || (posCaret > endPosBar || posCaret > endPosTilde)) {
                  // there's no caret, so add it to value, keep same
                  // position
                  while (count < transform.subfield) {
                    prepend = prepend + "^";
                    count++;
                  }
                  if (endPosTilde < endPosBar) {
                    pos = endPosTilde;
                  } else {
                    pos = endPosBar;
                  }
                  break;
                } else {
                  pos = posCaret + 1;
                }
                count++;
              }
              if (pos != -1) {
                if (transform.subsubfield > 0) {
                  count = 1;
                  while (pos != -1 && count < transform.subsubfield) {
                    int posAmper = isMSH2 ? -1 : lineResult.indexOf("&", pos);
                    int endPosBar = lineResult.indexOf("|", pos);
                    if (endPosBar == -1) {
                      endPosBar = lineResult.length();
                    }
                    int endPosTilde = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                    if (endPosTilde == -1) {
                      endPosTilde = lineResult.length();
                    }
                    int endPosCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
                    if (endPosCaret == -1) {
                      endPosCaret = lineResult.length();
                    }
                    int endPos = endPosCaret;
                    if (endPosTilde < endPos) {
                      endPos = endPosTilde;
                    }
                    if (endPosBar < endPos) {
                      endPos = endPosBar;
                    }

                    if (posAmper == -1 || (posAmper > endPos)) {
                      // there's no ampersand, so add it to the value, keep the
                      // same position
                      while (count < transform.subsubfield) {
                        prepend = prepend + "&";
                        count++;
                      }
                      pos = endPos;
                    } else {
                      pos = posAmper + 1;
                    }
                    count++;
                  }
                }

                int endPosBar = lineResult.indexOf("|", pos);
                if (endPosBar == -1) {
                  endPosBar = lineResult.length();
                  lineResult += "|";
                }
                int endPosCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
                int endPosRepeat = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                int endPos = endPosBar;
                if (endPosRepeat != -1 && endPosRepeat < endPos) {
                  endPos = endPosRepeat;
                }
                if (endPosCaret != -1 && endPosCaret < endPos) {
                  endPos = endPosCaret;
                }
                if (transform.subsubfield > 0) {
                  int endPosAmper = isMSH2 ? -1 : lineResult.indexOf("&", pos);
                  if (endPosAmper != -1 && endPosAmper < endPos) {
                    endPos = endPosAmper;
                  }
                }

                // if we're concatenating ("PID-5.1+=MORE" for example
                // we want to include the existing 5.1 text in the substring
                String lineNew = lineResult.substring(0, transform.concatenate ? endPos : pos);

                if (newValue.toUpperCase().startsWith("[MAP ")) {
                  String oldValue = lineResult.substring(pos, endPos);
                  newValue = mapValue(transform, oldValue, transformRequest);
                } else if (newValue.toUpperCase().startsWith("[TRUNC")) {
                  String oldValue = lineResult.substring(pos, endPos);
                  newValue = truncate(lineResult, newValue, oldValue);
                } else if (newValue.toUpperCase().startsWith("[MODIFY ")) {
                  if (newValue.indexOf("dtm for webiz") != -1) {
                    String oldValue = lineResult.substring(pos, endPos);
                    newValue = modifyDtmForWebiz(oldValue);
                  }
                }
                if (!newValue.equals("")) {
                  lineNew += prepend + newValue;
                }
                lineNew += lineResult.substring(endPos);
                lineResult = lineNew;
              }
            }
          }
        }
        resultText += lineResult + transformRequest.getSegmentSeparator();
      }

    }
    return resultText;
  }

  private String clearValueInHL7(String resultText, Transform t) throws IOException {

    BufferedReader inResult = new BufferedReader(new StringReader(resultText));
    boolean foundBoundStart = false;
    boolean foundBoundEnd = false;
    int boundCount = 0;
    resultText = "";
    String lineResult;
    int repeatCount = 0;
    while ((lineResult = inResult.readLine()) != null) {
      lineResult = lineResult.trim();
      if (lineResult.length() > 0) {
        if (t.boundSegment != null && !foundBoundEnd) {
          boolean skip = false;
          if (lineResult.startsWith(t.boundSegment + "|")) {
            boundCount++;
            if (!foundBoundStart) {
              if (boundCount == t.boundRepeat) {
                foundBoundStart = true;
              }
            } else if (foundBoundStart) {
              foundBoundEnd = true;
            }
            skip = true;
          } else if (foundBoundStart) {
            if (!lineResult.startsWith(t.segment + "|")) {
              skip = true;
            }
          } else {
            skip = true;
          }
          if (skip) {
            resultText += lineResult + "\r";
            continue;
          }
        }
        if (lineResult.startsWith(t.segment + "|")) {
          repeatCount++;
          if (t.segmentRepeat == repeatCount) {
            if (t.field == 0) {
              lineResult = t.segment + "|";
            } else {
              int pos = lineResult.indexOf("|");
              int count = (lineResult.startsWith("MSH|") || lineResult.startsWith("FHS|")
                  || lineResult.startsWith("BHS|")) ? 2 : 1;
              while (pos != -1 && count < t.field) {
                pos = lineResult.indexOf("|", pos + 1);
                count++;
              }
              if (pos != -1) {
                if (!t.fieldRepeatSet && !t.subfieldSet) {
                  int endPosBar = lineResult.indexOf("|", pos + 1);
                  if (endPosBar == -1) {
                    lineResult = lineResult.substring(0, pos) + "|";
                  } else {
                    lineResult = lineResult.substring(0, pos + 1) + lineResult.substring(endPosBar);
                  }
                } else {
                  boolean isMSH2 = ((lineResult.startsWith("MSH|") || lineResult.startsWith("FHS|")
                      || lineResult.startsWith("BHS|"))) && t.field == 2;
                  count = 1;
                  pos++;
                  int tildePos = pos;
                  while (tildePos != -1 && count < t.fieldRepeat) {
                    int endPosTilde = isMSH2 ? -1 : lineResult.indexOf("~", tildePos);
                    int endPosBar = lineResult.indexOf("|", tildePos);
                    if (endPosBar == -1) {
                      endPosBar = lineResult.length();
                    }
                    if (endPosTilde == -1 || endPosTilde >= endPosBar) {
                      tildePos = -1;
                      pos = endPosBar;
                    } else {
                      tildePos = endPosTilde + 1;
                      pos = tildePos;
                      count++;
                    }
                  }
                  if (tildePos != -1) {
                    if (!t.subfieldSet) {
                      int endPosBar = lineResult.indexOf("|", pos);
                      if (endPosBar == -1) {
                        endPosBar = lineResult.length();
                      }
                      int endPosTilde = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                      if (endPosTilde == -1) {
                        endPosTilde = lineResult.length();
                      }
                      if (endPosTilde < endPosBar) {
                        lineResult =
                            lineResult.substring(0, pos) + lineResult.substring(endPosTilde);
                      } else {
                        lineResult = lineResult.substring(0, pos) + lineResult.substring(endPosBar);
                      }
                    } else if (t.subfield == 0) {
                      int endPosBar = lineResult.indexOf("|", pos);
                      if (endPosBar == -1) {
                        endPosBar = lineResult.length();
                      }
                      int endPosTilde = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                      if (endPosTilde == -1) {
                        endPosTilde = lineResult.length();
                      }
                      if (endPosTilde < endPosBar) {
                        lineResult =
                            lineResult.substring(0, pos) + lineResult.substring(endPosTilde);
                      } else {
                        lineResult = lineResult.substring(0, pos) + lineResult.substring(endPosBar);
                      }
                    } else {
                      count = 1;
                      while (pos != -1 && count < t.subfield) {
                        int posCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
                        int endPosBar = lineResult.indexOf("|", pos);
                        if (endPosBar == -1) {
                          endPosBar = lineResult.length();
                        }
                        int endPosTilde = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                        if (endPosTilde == -1) {
                          endPosTilde = lineResult.length();
                        }
                        if (posCaret == -1 || (posCaret > endPosBar || posCaret > endPosTilde)) {
                          pos = -1;
                          break;
                        } else {
                          pos = posCaret + 1;
                        }
                        count++;
                      }
                      if (pos != -1) {
                        int endPosBar = lineResult.indexOf("|", pos);
                        if (endPosBar == -1) {
                          endPosBar = lineResult.length();
                        }
                        int endPosCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
                        int endPosRepeat = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                        int endPos = endPosBar;
                        if (endPosRepeat != -1 && endPosRepeat < endPos) {
                          endPos = endPosRepeat;
                        }
                        if (endPosCaret != -1 && endPosCaret < endPos) {
                          endPos = endPosCaret;
                        }
                        lineResult = lineResult.substring(0, pos) + lineResult.substring(endPos);
                      }
                    }
                  }
                }
              }
            }
          }
        }
        resultText += lineResult + "\r";
      }
    }
    return resultText;
  }

  public String truncate(String lineResult, String newValue, String oldValue) {
    int size = 0;
    String s = newValue.substring("[TRUNC".length());
    int endBracket = s.lastIndexOf("]");
    if (endBracket != -1) {
      s = s.substring(0, endBracket).trim();
      if (s.length() > 0) {
        try {
          size = Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
          // ignore
          size = 0;
        }
      }
    }
    if (oldValue.length() < size) {
      return oldValue;
    } else {
      return oldValue.substring(0, size);
    }
  }

  public static String getValueFromHL7(final String ref, final String messageText,
      TransformRequest transformRequest) throws IOException {
    Transform t = readHL7Reference(ref, ref.length());
    return getValueFromHL7(messageText, t, transformRequest);
  }

  protected static String getValueFromHL7(final String resultText, Transform transform,
      TransformRequest transformRequest) throws IOException {

    BufferedReader inResult = getMessageText(resultText, transform, transformRequest);

    if (inResult == null) {
      return "";
    }

    boolean foundBoundStart = false;
    boolean foundBoundEnd = false;
    int boundCount = 0;
    String lineResult;
    int repeatCount = 0;
    while ((lineResult = inResult.readLine()) != null) {
      lineResult = lineResult.trim();
      if (lineResult.length() > 0) {
        if (transform.boundSegment != null && !foundBoundEnd) {
          boolean skip = false;
          if (lineResult.startsWith(transform.boundSegment + "|")) {
            boundCount++;
            if (!foundBoundStart) {
              if (boundCount == transform.boundRepeat) {
                foundBoundStart = true;
              }
            } else if (foundBoundStart) {
              foundBoundEnd = true;
            }
            skip = true;
          } else if (foundBoundStart) {
            if (!lineResult.startsWith(transform.segment + "|")) {
              skip = true;
            }
          } else {
            skip = true;
          }
          if (skip) {
            continue;
          }
        }
        if (lineResult.startsWith(transform.segment + "|")) {
          repeatCount++;
          if (transform.segmentRepeat == repeatCount) {
            if (!transform.fieldSet) {
              // The field was not set, this a reference to the whole segment
              return lineResult.substring(3);
            }
            int pos = lineResult.indexOf("|");
            int count = (lineResult.startsWith("MSH|") || lineResult.startsWith("FHS|")
                || lineResult.startsWith("BHS|")) ? 2 : 1;
            while (pos != -1 && count < transform.field) {
              pos = lineResult.indexOf("|", pos + 1);
              count++;
            }
            if (pos == -1) {
              return "";
            }

            pos++;
            count = 1;
            boolean isMSH2 = lineResult.startsWith("MSH|") && transform.field == 2;
            while (pos != -1 && count < transform.subfield) {
              int posCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
              int endPosBar = lineResult.indexOf("|", pos);
              if (endPosBar == -1) {
                endPosBar = lineResult.length();
              }
              int endPosTilde = isMSH2 ? -1 : lineResult.indexOf("~", pos);
              if (endPosTilde == -1) {
                endPosTilde = lineResult.length();
              }
              if (posCaret == -1 || (posCaret > endPosBar || posCaret > endPosTilde)) {
                if (count < transform.subfield) {
                  return "";
                }
                if (endPosTilde < endPosBar) {
                  pos = endPosTilde;
                } else {
                  pos = endPosBar;
                }
                break;
              } else {
                pos = posCaret + 1;
              }
              count++;
            }
            if (transform.subsubfield > 0) {
              count = 1;
              while (pos != -1 && count < transform.subsubfield) {
                int posAmper = isMSH2 ? -1 : lineResult.indexOf("&", pos);
                int endPosCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
                if (endPosCaret == -1) {
                  endPosCaret = lineResult.length();
                }
                int endPosBar = lineResult.indexOf("|", pos);
                if (endPosBar == -1) {
                  endPosBar = lineResult.length();
                }
                int endPosTilde = isMSH2 ? -1 : lineResult.indexOf("~", pos);
                if (endPosTilde == -1) {
                  endPosTilde = lineResult.length();
                }
                if (posAmper == -1
                    || (posAmper > endPosBar || posAmper > endPosTilde || posAmper > endPosCaret)) {
                  if (count < transform.subsubfield) {
                    return "";
                  }
                  if (endPosCaret < endPosTilde) {
                    pos = endPosCaret;
                  } else if (endPosTilde < endPosBar) {
                    pos = endPosTilde;
                  } else {
                    pos = endPosBar;
                  }
                  break;
                } else {
                  pos = posAmper + 1;
                }
                count++;
              }
            }
            if (pos != -1) {
              int endPosBar = lineResult.indexOf("|", pos);
              if (endPosBar == -1) {
                endPosBar = lineResult.length();
              }
              int endPosAmper = isMSH2 ? -1 : lineResult.indexOf("&", pos);
              int endPosCaret = isMSH2 ? -1 : lineResult.indexOf("^", pos);
              int endPosRepeat = isMSH2 ? -1 : lineResult.indexOf("~", pos);
              int endPos = endPosBar;
              if (endPosRepeat != -1 && endPosRepeat < endPos) {
                endPos = endPosRepeat;
              }
              if (endPosCaret != -1 && endPosCaret < endPos) {
                endPos = endPosCaret;
              }
              if (endPosAmper != -1 && endPosAmper < endPos) {
                endPos = endPosAmper;
              }
              return lineResult.substring(pos, endPos);
            }
          }
        }
      }
    }
    return "";
  }

  protected static BufferedReader getMessageText(final String resultText, Transform t,
      TransformRequest transformRequest) {

    if (t.testCaseId == null) {
      return new BufferedReader(new StringReader(resultText));
    }

    if (transformRequest == null || transformRequest.getTestCaseMessageMap() == null) {
      return null;
    }

    String testCaseId = t.testCaseId;

    Map<String, TestCaseMessage> testCaseMessageMap = transformRequest.getTestCaseMessageMap();

    TestCaseMessage matchingMessage = null;

    matchingMessage =
        getRepeatReferenceTestCaseMessage(transformRequest, testCaseId, testCaseMessageMap);

    if (matchingMessage == null) {
      return null;
    }

    return new BufferedReader(new StringReader(matchingMessage.getMessageText()));
  }

  public static TestCaseMessage getRepeatReferenceTestCaseMessage(TransformRequest transformRequest,
      String testCaseId, Map<String, TestCaseMessage> testCaseMessageMap) {

    if (testCaseMessageMap == null) {
      return null;
    }

    TestCaseMessage matchingMessage;
    // REPEAT_REF is the repeat set of characters
    // indicating we want to try and match up the current repeat number against the same number of the reference
    // so if test case ID is 1.1REPEAT_REF (right now 1.1-?), and the current test is 1.2-1, then we will try and match against 1.1-1
    if (testCaseId.endsWith(REPEAT_REF)) {
      // drop the REPEAT_REF for now, we know it's there
      testCaseId = testCaseId.substring(0, testCaseId.length() - REPEAT_REF.length());

      if (transformRequest.getCurrentTestCaseMessage() == null) {
        // we don't know what the current test is, so set the test case ID without the REPEAT_REF
        matchingMessage = testCaseMessageMap.get(testCaseId);
      } else {
        // we do know what the current test case is, see if it's ends in -1, -2, etc.
        String currentTestCaseId = transformRequest.getCurrentTestCaseMessage().getTestCaseNumber();

        if (isTestCaseIdRepeat(currentTestCaseId)) {
          // the current test case ID ends in -1, -2, etc.
          int repeatIndex = currentTestCaseId.lastIndexOf("-");
          String repeat = currentTestCaseId.substring(repeatIndex);

          // look up the message with the -1, -2, etc.
          matchingMessage = testCaseMessageMap.get(testCaseId + repeat);

          // if it's not there for whatever reason, just use the original test case ID
          if (matchingMessage == null) {
            matchingMessage = testCaseMessageMap.get(testCaseId);
          }
        } else {
          // the current test case is not a repeat, just look up the non-repeat version
          matchingMessage = testCaseMessageMap.get(testCaseId);
        }
      }
    } else {
      matchingMessage = testCaseMessageMap.get(testCaseId);
    }

    return matchingMessage;
  }

  protected static boolean isTestCaseIdRepeat(String currentTestCaseId) {
    return currentTestCaseId.matches(".*-\\d+$");
  }


  public static Transform readHL7Reference(String ref) {
    return readHL7Reference(ref, ref.length());
  }

  public static Transform readHL7Reference(String line, int endOfInput) {
    Transform t = null;

    int posPlusEquals = line.indexOf("+=");
    boolean concatenate = false;
    if (posPlusEquals != -1 && posPlusEquals + 1 == endOfInput) {
      endOfInput--;
      concatenate = true;
    }

    String testCaseId = null;
    {
      int posColons = line.indexOf("::");
      if (posColons > 0 && posColons < endOfInput) {
        testCaseId = line.substring(0, posColons).trim();
        line = line.substring(posColons + 2);
        endOfInput -= (posColons + 2);
      }
    }
    boolean all = false;
    {
      int posStar = line.indexOf("-*");
      if (posStar >= 0 && posStar < endOfInput) {
        line = line.substring(0, posStar) + line.substring(posStar + 2);
        endOfInput--;
        endOfInput--;
        all = true;
      }
    }
    if (!all) {
      int posStar = line.indexOf("*");
      if (posStar >= 0 && posStar < endOfInput) {
        line = line.substring(0, posStar) + line.substring(posStar + 1);
        endOfInput--;
        all = true;
      }
    }
    int posDash = line.indexOf("-");
    int posDot = line.indexOf(".");
    if (posDash > endOfInput) {
      posDash = -1;
    }
    if (posDot > endOfInput) {
      posDot = -1;
    }
    if (endOfInput != -1 && (posDot == -1 || (posDot > posDash && posDot < endOfInput))) {
      t = new Transform();
      t.testCaseId = testCaseId;
      t.all = all;
      t.concatenate = concatenate;
      if (posDash == -1 || posDash >= endOfInput) {
        posDash = endOfInput;
      }
      t.segment = line.substring(0, posDash).trim();
      int posBound = t.segment.indexOf(":");
      if (posBound != -1) {
        t.boundSegment = t.segment.substring(0, posBound);
        t.segment = t.segment.substring(posBound + 1);
        int posHash = t.boundSegment.indexOf("#");
        if (posHash != -1) {
          t.boundRepeat = Integer.parseInt(t.boundSegment.substring(posHash + 1));
          t.boundSegment = t.boundSegment.substring(0, posHash);
        }
      }
      int posHash = t.segment.indexOf("#");
      if (posHash != -1) {
        t.segmentRepeat = Integer.parseInt(t.segment.substring(posHash + 1));
        t.segmentRepeatSet = true;
        t.segment = t.segment.substring(0, posHash);
      }
      if (posDash < endOfInput) {
        String fieldRef = line.substring(posDash + 1, endOfInput);
        posHash = fieldRef.indexOf("#");
        if (posHash != -1) {
          t.fieldRepeat = Integer.parseInt(fieldRef.substring(posHash + 1).trim());
          t.fieldRepeatSet = true;
          fieldRef = fieldRef.substring(0, posHash);
        }
        posDot = fieldRef.indexOf(".");
        if (posDot == -1) {
          t.field = Integer.parseInt(fieldRef.trim());
          t.fieldSet = true;
        } else {
          t.field = Integer.parseInt(fieldRef.substring(0, posDot).trim());
          t.fieldSet = true;
          int posSubDot = fieldRef.indexOf(".", posDot + 1);
          if (posSubDot == -1) {
            t.subfield = Integer.parseInt(fieldRef.substring(posDot + 1).trim());
            t.subfieldSet = true;
          } else {
            t.subfield = Integer.parseInt(fieldRef.substring(posDot + 1, posSubDot).trim());
            t.subfieldSet = true;
            t.subsubfield = Integer.parseInt(fieldRef.substring(posSubDot + 1).trim());
          }
        }
      }
    }
    return t;
  }

  private String mapValue(Transform t, String oldValue, TransformRequest transformRequest)
      throws IOException {
    int mapPos = t.value.toUpperCase().indexOf("'" + oldValue.toUpperCase() + "'=>");
    if (mapPos == -1) {
      mapPos = t.value.toUpperCase().indexOf("DEFAULT=>");
    }
    if (mapPos != -1) {
      mapPos = t.value.indexOf("=>", mapPos) + 2;
      int mapPosLast = t.value.indexOf(",", mapPos);
      if (mapPosLast == -1) {
        mapPosLast = t.value.lastIndexOf("]");
        if (mapPosLast == -1) {
          mapPosLast = t.value.length();
        }
      }
      String newValue = t.value.substring(mapPos, mapPosLast).trim();
      if (newValue.startsWith("'")) {
        newValue = newValue.substring(1);
      }
      if (newValue.endsWith("'")) {
        newValue = newValue.substring(0, newValue.length() - 1);
      }

      if (newValue.startsWith("[") && newValue.endsWith("]")) {
        Transform tNew = new Transform();
        tNew.value = newValue;
        doReplacements(tNew, transformRequest);
        newValue = tNew.value;
      }
      return newValue;
    } else {
      return oldValue;
    }
  }

  private void doReplacements(Transform transform, TransformRequest transformRequest) throws IOException {
    Connector connector = transformRequest.getConnector();
    String resultText = transformRequest.getResultText();
    doPatientReplacements(transformRequest.getPatient(), transform);
    if (connector != null) {
      doConnectionReplacements(connector, transform);
    }
    if (transform.value.equalsIgnoreCase("[NOW]")) {
      transform.value = transformRequest.getNow();
    } else if (transform.value.equalsIgnoreCase("[NOW_NO_TIMEZONE]")) {
      transform.value = transformRequest.getNowNoTimezone();
    } else if (transform.value.equalsIgnoreCase("[TODAY]")) {
      transform.value = transformRequest.getToday();
    } else if (transform.value.equalsIgnoreCase("[TOMORROW]")) {
      transform.value = transformRequest.getTomorrow();
    } else if (transform.value.equalsIgnoreCase("[LONG_TIME_FROM_NOW]")) {
      transform.value = transformRequest.getLongTimeFromNow();
    } else if (transform.value.equalsIgnoreCase("[YESTERDAY]")) {
      transform.value = transformRequest.getYesterday();
    } else if (transform.value.equalsIgnoreCase("[DAY_BEFORE_YESTERDAY]")) {
      transform.value = transformRequest.getDayBeforeYesterday();
    } else if (transform.value.equalsIgnoreCase("[THREE_DAYS_AGO]")) {
      transform.value = transformRequest.getThreeDaysAgo();
    } else if (transform.value.equalsIgnoreCase("[CONTROL_ID]")) {
      transform.value = connector.getCurrentControlId();
    } else if (transform.value.equalsIgnoreCase(REP_SPACE)) {
      transform.value = " ";
    } else if (transform.value.toLowerCase().startsWith("[map") && transform.value.endsWith("]")) {
      // do nothing
    } else if (transform.value.toLowerCase().startsWith("[trunc") && transform.value.endsWith("]")) {
      // do nothing
    } else if (transform.value.toLowerCase().startsWith("[modify") && transform.value.endsWith("]")) {
      // do nothing
    } else if (transform.value.startsWith("[") && transform.value.endsWith("]")) {
      doVariableReplacement(transform, transformRequest);
    }

    if (transform.valueTransform != null) {
      if (transform.valueTransform.getSegment().equals(transform.segment)
          && !transform.valueTransform.isSegmentRepeatSet()) {
        // Referencing same segment and specific repeat not set, so assume to be referencing the same segment
        transform.valueTransform.segmentRepeat = transform.segmentRepeat;
      }
      transform.value = getValueFromHL7(resultText, transform.valueTransform, transformRequest);
    }
  }

  private void doConnectionReplacements(Connector connector, Transform t) {
    int i = t.value.indexOf('[');
    int j = i;
    if (i >= 0) {
      j = t.value.indexOf(']', i);
    }
    while (i >= 0 && j > i) {
      j++;
      String p1 = "";
      if (i > 0) {
        t.value.substring(0, i);
      }
      String p2 = t.value.substring(i, j);
      String p3 = "";
      if (j < t.value.length()) {
        p3 = t.value.substring(j);
      }
      if (p2.equals(REP_CON_USERID)) {
        p2 = connector.getUserid();
      } else if (p2.equals(REP_CON_FACILITYID)) {
        p2 = connector.getFacilityid();
      } else if (p2.equals(REP_CON_PASSWORD)) {
        p2 = connector.getPassword();
      } else if (p2.equals(REP_CON_FILENAME)) {
        p2 = connector.getCurrentFilename();
      } else if (p2.equals(REP_CON_OTHERID)) {
        p2 = connector.getOtherid();
      }
      j = p1.length() + p2.length();
      t.value = p1 + p2 + p3;
      i = t.value.indexOf('[', j);
      j = i;
      if (i >= 0) {
        j = t.value.indexOf(']', i);
      }
    }
  }

  private void doPatientReplacements(Patient patient, Transform t) {
    int i = t.value.indexOf('[');
    int j = i;
    if (i >= 0) {
      j = t.value.indexOf(']', i);
    }
    while (i >= 0 && j > i) {
      j++;
      String p1 = "";
      if (i > 0) {
        t.value.substring(0, i);
      }
      String p2 = t.value.substring(i, j);
      String p3 = "";
      if (j < t.value.length()) {
        p3 = t.value.substring(j);
      }
      if (p2.equals(REP_PAT_ALIAS_BOY)) {
        p2 = patient.getAliasBoy();
      } else if (p2.equals(REP_PAT_ALIAS_GIRL)) {
        p2 = patient.getAliasGirl();
      } else if (p2.equals(REP_PAT_ALIAS_BOY_OR_GIRL)) {
        p2 = patient.getGender().equals("F") ? patient.getAliasGirl() : patient.getAliasBoy();
      } else if (p2.equals(REP_PAT_BOY)) {
        p2 = patient.getBoyName();
      } else if (p2.equals(REP_PAT_GIRL)) {
        p2 = patient.getGirlName();
      } else if (p2.equals(REP_PAT_BOY_OR_GIRL)) {
        p2 = patient.getGender().equals("F") ? patient.getGirlName() : patient.getBoyName();
      } else if (p2.equals(REP_PAT_GENDER)) {
        p2 = patient.getGender();
      } else if (p2.equals(REP_PAT_FATHER)) {
        p2 = patient.getFatherName();
      } else if (p2.equals(REP_PAT_SUFFIX)) {
        p2 = patient.getSuffix();
      } else if (p2.equals(REP_PAT_MOTHER)) {
        p2 = patient.getMotherName();
      } else if (p2.equals(REP_PAT_MOTHER_MAIDEN)) {
        p2 = patient.getMotherMaidenName();
      } else if (p2.equals(REP_PAT_DOB)) {
        p2 = patient.getDates()[0];
      } else if (p2.equals(REP_PAT_MOTHER_DOB)) {
        p2 = patient.getMotherDob();
      } else if (p2.equals(REP_PAT_MOTHER_SSN)) {
        p2 = patient.getMotherSsn();
      } else if (p2.equals(REP_PAT_BIRTH_MULTIPLE)) {
        p2 = patient.getBirthCount() > 1 ? "Y" : "N";
      } else if (p2.equals(REP_PAT_BIRTH_ORDER)) {
        p2 = "" + patient.getBirthCount();
      } else if (p2.equals(REP_PAT_MRN)) {
        p2 = patient.getMedicalRecordNumber();
      } else if (p2.equals(REP_PAT_MEDICAID)) {
        p2 = patient.getMedicaidNumber();
      } else if (p2.equals(REP_PAT_WIC)) {
        p2 = patient.getWic();
      } else if (p2.equals(REP_PAT_SSN)) {
        p2 = patient.getSsn();
      } else if (p2.equals(REP_PAT_RACE)) {
        p2 = patient.getRace()[0];
      } else if (p2.equals(REP_PAT_RACE_LABEL)) {
        p2 = patient.getRace()[1];
      } else if (p2.equals(REP_PAT_ETHNICITY)) {
        p2 = patient.getEthnicity()[0];
      } else if (p2.equals(REP_PAT_ETHNICITY_LABEL)) {
        p2 = patient.getEthnicity()[1];
      } else if (p2.equals(REP_PAT_LANGUAGE)) {
        p2 = patient.getLanguage()[0];
      } else if (p2.equals("[LANGUAGE_LABEL]")) {
        p2 = patient.getLanguage()[1];
      } else if (p2.equals("[VFC]")) {
        p2 = patient.getVfc()[0];
      } else if (p2.equals("[VFC_LABEL]")) {
        p2 = patient.getVfc()[1];
      } else if (p2.equals("[LAST]")) {
        p2 = patient.getLastName();
      } else if (p2.equals("[FUTURE]")) {
        p2 = patient.getFuture();
      } else if (p2.equals("[LAST_DIFFERENT]")) {
        p2 = patient.getDifferentLastName();
      } else if (p2.equals("[GIRL_MIDDLE]")) {
        p2 = patient.getMiddleNameGirl();
      } else if (p2.equals("[BOY_MIDDLE]")) {
        p2 = patient.getMiddleNameBoy();
      } else if (p2.equals("[BOY_OR_GIRL_MIDDLE]")) {
        p2 = patient.getGender().equals("F") ? patient.getMiddleNameGirl()
            : patient.getMiddleNameBoy();
      } else if (p2.equals("[GIRL_MIDDLE_INITIAL]")) {
        p2 = patient.getMiddleNameGirl().substring(0, 1);
      } else if (p2.equals("[BOY_MIDDLE_INITIAL]")) {
        p2 = patient.getMiddleNameBoy().substring(0, 1);
      } else if (p2.equals("[VAC1_ID]")) {
        p2 = patient.getMedicalRecordNumber() + ".1";
      } else if (p2.equals("[VAC2_ID]")) {
        p2 = patient.getMedicalRecordNumber() + ".2";
      } else if (p2.equals("[VAC3_ID]")) {
        p2 = patient.getMedicalRecordNumber() + ".3";
      } else if (p2.equals("[VAC4_ID]")) {
        p2 = patient.getMedicalRecordNumber() + ".4";
      } else if (p2.equals("[VAC5_ID]")) {
        p2 = patient.getMedicalRecordNumber() + ".5";
      } else if (p2.equals("[VAC1_DATE]")) {
        p2 = patient.getDates()[1];
      } else if (p2.equals("[VAC2_DATE]")) {
        p2 = patient.getDates()[2];
      } else if (p2.equals(REP_PAT_VAC3_DATE)) {
        p2 = patient.getDates()[3];
      } else if (p2.equals("[VAC1_CVX]")) {
        p2 = patient.getVaccine1()[VACCINE_CVX];
      } else if (p2.equals("[VAC1_CVX_LABEL]")) {
        p2 = patient.getVaccine1()[VACCINE_NAME];
      } else if (p2.equals("[VAC1_LOT]")) {
        p2 = patient.getVaccine1()[VACCINE_LOT];
      } else if (p2.equals("[VAC1_MVX]")) {
        p2 = patient.getVaccine1()[VACCINE_MVX];
      } else if (p2.equals("[VAC1_MVX_LABEL]")) {
        p2 = patient.getVaccine1()[VACCINE_MANUFACTURER];
      } else if (p2.equals("[VAC1_TRADE_NAME]")) {
        p2 = patient.getVaccine1()[VACCINE_TRADE_NAME];
      } else if (p2.equals("[VAC1_AMOUNT]")) {
        p2 = patient.getVaccine1()[VACCINE_AMOUNT];
      } else if (p2.equals("[VAC1_ROUTE]")) {
        p2 = patient.getVaccine1()[VACCINE_ROUTE];
      } else if (p2.equals("[VAC1_SITE]")) {
        p2 = patient.getVaccine1()[VACCINE_SITE];
      } else if (p2.equals("[VAC1_VIS_PUB_NAME]")) {
        p2 = patient.getVaccine1()[VACCINE_VIS_PUB];
      } else if (p2.equals("[VAC1_VIS_PUB_CODE]")) {
        p2 = patient.getVaccine1()[VACCINE_VIS_PUB_CODE];
      } else if (p2.equals("[VAC1_VIS_PUB_DATE]")) {
        p2 = patient.getVaccine1()[VACCINE_VIS_PUB_DATE];
      } else if (p2.equals("[COMBO_VIS1_PUB_NAME]")) {
        p2 = patient.getCombo()[VACCINE_VIS_PUB];
      } else if (p2.equals("[COMBO_VIS1_PUB_CODE]")) {
        p2 = patient.getCombo()[VACCINE_VIS_PUB_CODE];
      } else if (p2.equals("[COMBO_VIS1_PUB_DATE]")) {
        p2 = patient.getCombo()[VACCINE_VIS_PUB_DATE];
      } else if (p2.equals("[COMBO_VIS2_PUB_NAME]")) {
        p2 = patient.getCombo()[VACCINE_VIS2_PUB];
      } else if (p2.equals("[COMBO_VIS2_PUB_CODE]")) {
        p2 = patient.getCombo()[VACCINE_VIS2_PUB_CODE];
      } else if (p2.equals("[COMBO_VIS2_PUB_DATE]")) {
        p2 = patient.getCombo()[VACCINE_VIS2_PUB_DATE];
      } else if (p2.equals("[COMBO_VIS3_PUB_NAME]")) {
        p2 = patient.getCombo()[VACCINE_VIS3_PUB];
      } else if (p2.equals("[COMBO_VIS3_PUB_CODE]")) {
        p2 = patient.getCombo()[VACCINE_VIS3_PUB_CODE];
      } else if (p2.equals("[COMBO_VIS3_PUB_DATE]")) {
        p2 = patient.getCombo()[VACCINE_VIS3_PUB_DATE];
      } else if (p2.equals("[VAC2_CVX]")) {
        p2 = patient.getVaccine2()[VACCINE_CVX];
      } else if (p2.equals("[VAC2_CVX_LABEL]")) {
        p2 = patient.getVaccine2()[VACCINE_NAME];
      } else if (p2.equals("[VAC2_LOT]")) {
        p2 = patient.getVaccine2()[VACCINE_LOT];
      } else if (p2.equals("[VAC2_MVX]")) {
        p2 = patient.getVaccine2()[VACCINE_MVX];
      } else if (p2.equals("[VAC2_MVX_LABEL]")) {
        p2 = patient.getVaccine2()[VACCINE_MANUFACTURER];
      } else if (p2.equals("[VAC2_TRADE_NAME]")) {
        p2 = patient.getVaccine2()[VACCINE_TRADE_NAME];
      } else if (p2.equals("[VAC2_AMOUNT]")) {
        p2 = patient.getVaccine2()[VACCINE_AMOUNT];
      } else if (p2.equals("[VAC2_ROUTE]")) {
        p2 = patient.getVaccine2()[VACCINE_ROUTE];
      } else if (p2.equals("[VAC2_SITE]")) {
        p2 = patient.getVaccine2()[VACCINE_SITE];
      } else if (p2.equals("[VAC2_VIS_PUB_NAME]")) {
        p2 = patient.getVaccine2()[VACCINE_VIS_PUB];
      } else if (p2.equals("[VAC2_VIS_PUB_CODE]")) {
        p2 = patient.getVaccine2()[VACCINE_VIS_PUB_CODE];
      } else if (p2.equals("[VAC2_VIS_PUB_DATE]")) {
        p2 = patient.getVaccine2()[VACCINE_VIS_PUB_DATE];
      } else if (p2.equals("[VAC3_CVX]")) {
        p2 = patient.getVaccine3()[0];
      } else if (p2.equals("[VAC3_CVX_LABEL]")) {
        p2 = patient.getVaccine3()[1];
      } else if (p2.equals("[VAC3_LOT]")) {
        p2 = patient.getVaccine3()[2];
      } else if (p2.equals("[VAC3_MVX]")) {
        p2 = patient.getVaccine3()[3];
      } else if (p2.equals("[VAC3_MVX_LABEL]")) {
        p2 = patient.getVaccine3()[4];
      } else if (p2.equals("[VAC3_TRADE_NAME]")) {
        p2 = patient.getVaccine3()[VACCINE_TRADE_NAME];
      } else if (p2.equals("[VAC3_AMOUNT]")) {
        p2 = patient.getVaccine3()[VACCINE_AMOUNT];
      } else if (p2.equals("[VAC3_ROUTE]")) {
        p2 = patient.getVaccine3()[VACCINE_ROUTE];
      } else if (p2.equals("[VAC3_SITE]")) {
        p2 = patient.getVaccine3()[VACCINE_SITE];
      } else if (p2.equals("[VAC3_VIS_PUB_NAME]")) {
        p2 = patient.getVaccine3()[VACCINE_VIS_PUB];
      } else if (p2.equals("[VAC3_VIS_PUB_CODE]")) {
        p2 = patient.getVaccine3()[VACCINE_VIS_PUB_CODE];
      } else if (p2.equals("[VAC3_VIS_PUB_DATE]")) {
        p2 = patient.getVaccine3()[VACCINE_VIS_PUB_DATE];
      } else if (p2.equals("[CITY]")) {
        p2 = patient.getCity();
      } else if (p2.equals("[STREET]")) {
        p2 = patient.getStreet();
      } else if (p2.equals("[STREET2]")) {
        p2 = patient.getStreet2();
      } else if (p2.equals("[STATE]")) {
        p2 = patient.getState();
      } else if (p2.equals("[ZIP]")) {
        p2 = patient.getZip();
      } else if (p2.equals(REP_ENTERED_BY_FIRST)) {
        p2 = patient.getEnteredByFirstName();
      } else if (p2.equals(REP_ENTERED_BY_MIDDLE)) {
        p2 = patient.getEnteredByMiddleName();
      } else if (p2.equals(REP_ENTERED_BY_LAST)) {
        p2 = patient.getEnteredByLastName();
      } else if (p2.equals(REP_ENTERED_BY_NPI)) {
        p2 = patient.getEnteredByNPI();
      } else if (p2.equals(REP_ORDERED_BY_FIRST)) {
        p2 = patient.getOrderedByFirstName();
      } else if (p2.equals(REP_ORDERED_BY_MIDDLE)) {
        p2 = patient.getOrderedByMiddleName();
      } else if (p2.equals(REP_ORDERED_BY_LAST)) {
        p2 = patient.getOrderedByLastName();
      } else if (p2.equals(REP_ORDERED_BY_NPI)) {
        p2 = patient.getOrderedByNPI();
      } else if (p2.equals(REP_ADMIN_BY_FIRST)) {
        p2 = patient.getAdminByFirstName();
      } else if (p2.equals(REP_ADMIN_BY_MIDDLE)) {
        p2 = patient.getAdminByMiddleName();
      } else if (p2.equals(REP_ADMIN_BY_LAST)) {
        p2 = patient.getAdminByLastName();
      } else if (p2.equals(REP_ADMIN_BY_NPI)) {
        p2 = patient.getAdminByNPI();
      } else if (p2.equals(REP_RESPONSIBLE_ORG_ID)) {
        p2 = patient.getResponsibleOrg()[0];
      } else if (p2.equals(REP_RESPONSIBLE_ORG_NAME)) {
        p2 = patient.getResponsibleOrg()[1];
      } else if (p2.equals(REP_ADMIN_ORG_1_ID)) {
        p2 = patient.getAdminOrg1()[0];
      } else if (p2.equals(REP_ADMIN_ORG_1_NAME)) {
        p2 = patient.getAdminOrg1()[1];
      } else if (p2.equals(REP_ADMIN_ORG_2_ID)) {
        p2 = patient.getAdminOrg2()[0];
      } else if (p2.equals(REP_ADMIN_ORG_2_NAME)) {
        p2 = patient.getAdminOrg2()[1];
      } else if (p2.equals(REP_PAT_EMAIL)) {
        p2 = patient.getEmail();
      } else if (p2.equals(REP_PAT_PHONE)) {
        p2 = patient.getPhone();
      } else if (p2.equals(REP_PAT_PHONE_AREA)) {
        p2 = patient.getPhoneArea();
      } else if (p2.equals(REP_PAT_PHONE_LOCAL)) {
        p2 = patient.getPhoneLocal();
      } else if (p2.equals(REP_PAT_PHONE_ALT)) {
        p2 = patient.getPhone();
      } else if (p2.equals(REP_PAT_PHONE_ALT_AREA)) {
        p2 = patient.getPhoneArea();
      } else if (p2.equals(REP_PAT_PHONE_ALT_LOCAL)) {
        p2 = patient.getPhoneLocal();
      } else if (p2.equals(REP_PAT_VAC3_DATE)) {
        p2 = patient.getDates()[3];
      }
      j = p1.length() + p2.length();
      t.value = p1 + p2 + p3;
      i = t.value.indexOf('[', j);
      j = i;
      if (i >= 0) {
        j = t.value.indexOf(']', i);
      }
    }
  }

  protected void doVariableReplacement(Transform t, TransformRequest transformRequest)
      throws IOException {
    String v = t.value.substring(1, t.value.length() - 1);
    String variableName = v;

    TestCaseMessage tcm = null;
    if (variableName.contains("::")) {
      String[] split = variableName.split("\\:\\:");

      // look up referenced test message keeping in mind it could be a repeat
      // Y-1 should map to X-1, Y-2 to X-2, etc.
      TestCaseMessage matchingTestCaseMessage = getRepeatReferenceTestCaseMessage(transformRequest,
          split[0], transformRequest.getTestCaseMessageMap());

      if (matchingTestCaseMessage != null) {
        tcm = transformRequest.getTestCaseMessageMap()
            .get(matchingTestCaseMessage.getTestCaseNumber());
        variableName = split[1];
      }
    } else {
      tcm = transformRequest.getCurrentTestCaseMessage();
    }

    if (tcm != null) {
      Map<String, String> variables = tcm.getVariables();

      if (variables.containsKey(variableName)) {
        t.value = variables.get(variableName);
        doReplacements(t, transformRequest);
      } else {
        t.valueTransform = readHL7Reference(v, v.length());
      }
    } else {
      t.valueTransform = readHL7Reference(v, v.length());
    }
  }

  private static int medicalRecordNumberInc = 0;

  public Patient setupPatient(PatientType patientType) {
    Patient patient = new Patient();

    medicalRecordNumberInc++;
    patient.setMedicalRecordNumber("" + (char) (random.nextInt(26) + 'A') + random.nextInt(10)
        + random.nextInt(10) + (char) (random.nextInt(26) + 'A') + medicalRecordNumberInc);
    patient.setSsn("" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10));
    patient.setMotherSsn("" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10));
    patient.setMedicaidNumber("" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10));
    patient.setWic("" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10));
    patient.setBoyName(getRandomValue("BOY") + "AIRA");
    patient.setGirlName(getRandomValue("GIRL") + "AIRA");
    patient.setAliasBoy(getRandomValue("BOY") + "AIRA");
    patient.setAliasGirl(getRandomValue("GIRL") + "AIRA");
    patient.setMotherName(getRandomValue("GIRL") + "AIRA");
    patient.setMotherMaidenName(getRandomValue("LAST_NAME") + "AIRA");
    patient.setFatherName(getRandomValue("BOY") + "AIRA");
    patient.setLastName(getRandomValue("LAST_NAME") + "AIRA");
    patient.setDifferentLastName(getRandomValue("LAST_NAME") + "AIRA");
    patient.setMiddleNameBoy(getRandomValue("BOY"));
    patient.setMiddleNameGirl(getRandomValue("GIRL"));
    patient.setRace(getValueArray("RACE", 2));
    patient.setEthnicity(getValueArray("ETHNICITY", 2));
    patient.setLanguage(getValueArray("LANGUAGE", 2));
    patient.setAddress(getValueArray("ADDRESS", 4));
    patient.setSuffix(getRandomValue("SUFFIX"));
    patient.setStreet((random.nextInt(1000) + 1000) + " " + getRandomValue("STREET_NAME") + " "
        + getRandomValue("STREET_ABBREVIATION"));
    patient.setStreet2("APT #" + (random.nextInt(400) + 1));
    patient.setCity(patient.getAddress()[0]);
    patient.setState(patient.getAddress()[1]);
    patient.setZip(patient.getAddress()[2]);
    patient.setPhoneArea(patient.getAddress()[3]);
    patient.setPhoneLocal("" + (random.nextInt(8) + 2) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10));
    patient.setPhone("(" + patient.getPhoneArea() + ")" + patient.getPhoneLocal());
    patient.setPhoneAltArea(patient.getAddress()[3]);
    patient.setPhoneAltLocal("" + (random.nextInt(8) + 2) + random.nextInt(10) + random.nextInt(10)
        + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10));
    patient.setPhoneAlt("(" + patient.getPhoneAltArea() + ")" + patient.getPhoneAltLocal());
    if (PatientType.ADULT == patientType) {
      if (patient.getGender().equals("M")) {
        patient
            .setEmail(patient.getBoyName().toLowerCase() + "." + patient.getLastName().toLowerCase()
                + random.nextInt(2500) + "@madeupemailaddress.com");
      } else {
        patient.setEmail(
            patient.getGirlName().toLowerCase() + "." + patient.getLastName().toLowerCase()
                + random.nextInt(2500) + "@madeupemailaddress.com");
      }
    } else {
      patient.setEmail(patient.getMotherName().toLowerCase() + "."
          + patient.getLastName().toLowerCase() + random.nextInt(2500) + "@madeupemailaddress.com");
    }
    patient.setBirthCount(makeBirthCount());

    if (patientType == PatientType.NONE) {
      return patient;
    }

    String[] dates = new String[4];
    patient.setDates(dates);
    patient.setVaccineType(createDates(dates, patientType));
    patient.setMotherDob(makeMotherDob(dates[0]));
    patient.setGender(random.nextBoolean() ? "F" : "M");
    patient.setVaccine1(
        getValueArray("VACCINE_" + patient.getVaccineType(), VACCINE_VIS_PUB_DATE + 1));
    patient.setVaccine2(
        getValueArray("VACCINE_" + patient.getVaccineType(), VACCINE_VIS_PUB_DATE + 1));
    patient.setVaccine3(
        getValueArray("VACCINE_" + patient.getVaccineType(), VACCINE_VIS_PUB_DATE + 1));
    patient.setCombo(getValueArray("VACCINE_COMBO", VACCINE_VIS3_PUB_DATE + 1));
    if (PatientType.ADULT == patientType) {
      patient.setVfc(new String[] {"V01", "Not VFC eligible"});
    } else {
      patient.setVfc(getValueArray("VFC", 2));
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    Calendar calendar = Calendar.getInstance();
    calendar.add(Calendar.YEAR, 1);
    patient.setFuture(sdf.format(calendar.getTime()));
    {
      boolean enteredByBoy = random.nextBoolean();
      patient.setEnteredByFirstName(getRandomValue(enteredByBoy ? "BOY" : "GIRL"));
      patient.setEnteredByMiddleName(getRandomValue(enteredByBoy ? "BOY" : "GIRL"));
      patient.setEnteredByLastName(getRandomValue("LAST_NAME"));
      patient.setEnteredByNPI("" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
          + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
          + random.nextInt(10) + random.nextInt(10) + random.nextInt(10));
    }
    {
      boolean orderedByBoy = random.nextBoolean();
      patient.setOrderedByFirstName(getRandomValue(orderedByBoy ? "BOY" : "GIRL"));
      patient.setOrderedByMiddleName(getRandomValue(orderedByBoy ? "BOY" : "GIRL"));
      patient.setOrderedByLastName(getRandomValue("LAST_NAME"));
      patient.setOrderedByNPI("" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
          + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
          + random.nextInt(10) + random.nextInt(10) + random.nextInt(10));
    }
    {
      boolean adminByBoy = random.nextBoolean();
      patient.setAdminByFirstName(getRandomValue(adminByBoy ? "BOY" : "GIRL"));
      patient.setAdminByMiddleName(getRandomValue(adminByBoy ? "BOY" : "GIRL"));
      patient.setAdminByLastName(getRandomValue("LAST_NAME"));
      patient.setAdminByNPI("" + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
          + random.nextInt(10) + random.nextInt(10) + random.nextInt(10) + random.nextInt(10)
          + random.nextInt(10) + random.nextInt(10) + random.nextInt(10));
    }
    patient.setResponsibleOrg(getValueArray("RESPONSIBLE ORG", 2));
    if (patient.getResponsibleOrg()[0].equals("") && patient.getResponsibleOrg()[1].equals("")) {
      patient.getResponsibleOrg()[0] = "101";
      patient.getResponsibleOrg()[1] =
          getRandomValue("LAST_NAME") + (random.nextBoolean() ? " Family Clinic" : " Pediatrics");
    }
    patient.setAdminOrg1(getValueArray("ADMIN ORG 1", 2));
    if (patient.getAdminOrg1()[0].equals("") && patient.getAdminOrg1()[1].equals("")) {
      patient.getAdminOrg1()[0] = patient.getResponsibleOrg()[0] + "-" + "01";
      patient.getAdminOrg1()[1] =
          patient.getResponsibleOrg()[1] + " - " + getRandomValue("LAST_NAME");
    }
    patient.setAdminOrg2(getValueArray("ADMIN ORG 2", 2));
    if (patient.getAdminOrg2()[0].equals("") && patient.getAdminOrg2()[1].equals("")) {
      patient.getAdminOrg2()[0] = patient.getResponsibleOrg()[0] + "-" + "02";
      patient.getAdminOrg2()[1] =
          patient.getResponsibleOrg()[1] + " - " + getRandomValue("LAST_NAME");
    }
    return patient;
  }

  private static String makeMotherDob(String dob) {
    try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      Calendar calendar = Calendar.getInstance();
      calendar.setTime(sdf.parse(dob));
      calendar.add(Calendar.YEAR, -20 - random.nextInt(15));
      calendar.add(Calendar.MONTH, -random.nextInt(12));
      calendar.add(Calendar.DATE, -random.nextInt(30));
      return sdf.format(calendar.getTime());
    } catch (ParseException pe) {
      return dob;
    }
  }

  protected int makeBirthCount() {
    int birthCount = 1;
    int hat = random.nextInt(100000);
    if (hat < 3220 + 149) {
      // chances for twin are 32.2 in 1,000 or 3220 in 100,000
      birthCount = 2;
      if (hat < 149) {
        // chances for triplet or higher is is 148.9 in 100,000
        birthCount = 3;
        if (hat < 10) {
          birthCount = 4;
          if (hat < 2) {
            birthCount = 5;
          }
        }
      }
    }
    return birthCount;
  }

  public static String makeBase62Number(long number) {
    String s = "";
    if (number == 0) {
      return "0";
    }
    while (number != 0) {
      long a = number % 62;
      if (a < 10) {
        s = a + s;
      } else if (a < 36) {
        s = (char) (a - 10 + 97) + s;
      } else {
        s = (char) (a - 10 - 26 + 65) + s;
      }
      number = (number - a) / 62;
    }
    return s;
  }

  public Random getRandom() {
    return random;
  }

}
