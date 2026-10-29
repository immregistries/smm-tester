package org.immregistries.smm.tester.manager.query;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Builds a risk-based QBP (profile Z45) from a VXU message. The Z45 profile extends Z44 with one
 * OBX segment per contextual condition (local code system 99CDS), placed between QPD and RCP.
 * Conditions are given as a comma separated list of code=value pairs, for example
 * <code>007=Y,170=20260701</code>. A value of Y or N is sent as value type ID, an 8 digit
 * date is sent as value type DT.
 *
 * Based on the draft "Enabling Risk-Based CDS through HL7 V2 Query and Response" guidance.
 */
public class QueryConverterQBPZ45 extends QueryConverter {

  public static final String PROFILE_ID = "Z45";
  public static final String PROFILE_NAME =
      "Request Evaluated Immunization History and Risk-Based Forecast";
  public static final String CODE_SYSTEM = "99CDS";

  /** Contextual conditions listed in the draft (Table 7), code to description. */
  public static final Map<String, String> KNOWN_CONDITIONS = new LinkedHashMap<String, String>();
  static {
    KNOWN_CONDITIONS.put("007", "Patient is currently pregnant");
    KNOWN_CONDITIONS.put("170", "Onset of pregnancy");
    KNOWN_CONDITIONS.put("016", "Chronic heart disease");
    KNOWN_CONDITIONS.put("023",
        "Laboratory Evidence of Immunity or confirmation of Varicella disease");
    KNOWN_CONDITIONS.put("024",
        "Healthcare provider verified history of or diagnosis of Varicella");
    KNOWN_CONDITIONS.put("045",
        "Travel to country with a Yellow Fever vaccination entry requirement");
    KNOWN_CONDITIONS.put("053", "Rabies researchers");
    KNOWN_CONDITIONS.put("055", "Healthcare personnel");
    KNOWN_CONDITIONS.put("056",
        "Health-care workers who have close contact with patients who might be excreting wild polioviruses");
    KNOWN_CONDITIONS.put("057", "Public safety worker exposed to blood or infection body fluids");
    KNOWN_CONDITIONS.put("058",
        "Staff of institutions for persons with developmental disabilities");
    KNOWN_CONDITIONS.put("059", "Occupation exposure for Hepatitis A");
    KNOWN_CONDITIONS.put("278", "Birth mother received RSV vaccine during pregnancy");
  }

  private String conditions = "";

  public QueryConverterQBPZ45() {}

  public QueryConverterQBPZ45(String conditions) {
    setConditions(conditions);
  }

  public String getConditions() {
    return conditions;
  }

  public void setConditions(String conditions) {
    this.conditions = conditions == null ? "" : conditions.trim();
  }

  @Override
  public String convert(String message) {
    String z44 = new QueryConverterQBPZ44().convert(message);
    StringBuilder sb = new StringBuilder();
    boolean obxWritten = false;
    for (String segment : z44.split("\r")) {
      if (segment.startsWith("MSH|")) {
        segment = segment.replace("|Z44^CDCPHINVS|", "|" + PROFILE_ID + "^CDCPHINVS|");
      } else if (segment.startsWith("QPD|")) {
        segment = segment.replace("Z44^Request Evaluated History and Forecast^CDCPHINVS",
            PROFILE_ID + "^" + PROFILE_NAME + "^CDCPHINVS");
      } else if (segment.startsWith("RCP|") && !obxWritten) {
        sb.append(buildObxSegments());
        obxWritten = true;
      }
      sb.append(segment).append('\r');
    }
    if (!obxWritten) {
      sb.append(buildObxSegments());
    }
    return sb.toString();
  }

  /** Creates one OBX segment per condition, each ending with a carriage return. */
  public String buildObxSegments() {
    StringBuilder sb = new StringBuilder();
    if (conditions.length() == 0) {
      return "";
    }
    int setId = 0;
    for (String pair : conditions.split("[,;\\s]+")) {
      if (pair.length() == 0) {
        continue;
      }
      String code = pair;
      String value = "Y";
      int eq = pair.indexOf('=');
      if (eq >= 0) {
        code = pair.substring(0, eq).trim();
        value = pair.substring(eq + 1).trim();
      }
      if (code.length() == 0) {
        continue;
      }
      setId++;
      String description = KNOWN_CONDITIONS.get(code);
      if (description == null) {
        description = "Unrecognized condition";
      }
      String valueType = value.matches("\\d{8}") ? "DT" : "ID";
      sb.append("OBX|").append(setId).append('|').append(valueType).append('|').append(code);
      sb.append('^').append(description).append('^').append(CODE_SYSTEM).append('|');
      sb.append(setId).append('|').append(value.toUpperCase()).append("||||||F\r");
    }
    return sb.toString();
  }
}
