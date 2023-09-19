package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class SuffixVariation extends ProcedureCommon implements ProcedureInterface {

  // order is important, III has to come before II, Jr. before Jr, Sr. before Sr
  protected static final String[] SUFFIXES =
      new String[] {"Jr.", "Jr", "Junior", "Sr.", "Sr", "Senior", "III", "II", "2nd", "3rd"};

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        int fieldPos = 5;
        String lastName = readValue(fields, fieldPos, 1);
        String firstName = readValue(fields, fieldPos, 2);
        String suffix = readValue(fields, fieldPos, 4);
        String[] names = varyName(lastName, firstName, suffix);
        updateValue(names[0], fields, fieldPos, 1);
        updateValue(names[1], fields, fieldPos, 2);
        updateValue(names[2], fields, fieldPos, 4);
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String[] varyName(String lastName, String firstName, String suffix) {
    boolean upperCase =
        lastName.toUpperCase().equals(lastName) && firstName.toUpperCase().equals(firstName);
    boolean lowerCase =
        lastName.toLowerCase().equals(lastName) && firstName.toLowerCase().equals(firstName);

    String[] names = new String[3];

    lastName = lastName.trim();
    firstName = firstName.trim();
    suffix = suffix.trim();

    names[0] = lastName;
    names[1] = firstName;
    names[2] = suffix;

    String suffixInLastName = getSuffix(lastName);
    String suffixInFirstName = getSuffix(firstName);
    String suffixInSuffix = getSuffix(suffix);

    if (suffixInSuffix != null) {
      names[0] = lastName + " " + suffixInSuffix;
      names[1] = firstName;
      names[2] = "";
    } else if (suffixInLastName != null) {
      names[0] = lastName.substring(0, lastName.length() - suffixInLastName.length()).trim();
      names[1] = firstName + " " + suffixInLastName;
      names[2] = "";
    } else if (suffixInFirstName != null) {
      names[0] = lastName;
      names[1] = firstName.substring(0, firstName.length() - suffixInFirstName.length()).trim();
      names[2] = suffixInFirstName;
    } else {
      names[0] = lastName;
      names[1] = firstName;
      names[2] = SUFFIXES[(int) (System.currentTimeMillis() % SUFFIXES.length)];
    }

    if (upperCase) {
      names[0] = names[0].toUpperCase();
      names[1] = names[1].toUpperCase();
    } else if (lowerCase) {
      names[0] = names[0].toLowerCase();
      names[1] = names[1].toLowerCase();
    }

    return names;
  }

  private static String getSuffix(String name) {
    name = name.toUpperCase();
    for (String suffix : SUFFIXES) {
      if (name.endsWith(suffix.toUpperCase())) {
        return suffix;
      }
    }
    return null;
  }

  public void setTransformer(Transformer transformer) {}
}
