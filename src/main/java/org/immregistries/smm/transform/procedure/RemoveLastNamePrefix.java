package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;
import org.immregistries.smm.transform.procedure.LastNamePrefixVariation.Prefix;

public class RemoveLastNamePrefix extends ProcedureCommon implements ProcedureInterface {

  public enum Field {
                     LAST_NAME,
                     MOTHERS_MAIDEN_NAME
  }

  private Field field;
  private Transformer transformer;

  public RemoveLastNamePrefix(Field field) {
    this.field = field;
  }

  @Override
  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);

    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        int fieldPos = 5;
        int subPos = 1;

        if (field == Field.LAST_NAME) {
          subPos = 1;
        } else if (field == Field.MOTHERS_MAIDEN_NAME) {
          fieldPos = 6;
          subPos = 1;
        }

        String lastName = readValue(fields, fieldPos, subPos);
        lastName = varyName(lastName, transformer);
        updateValue(lastName, fields, fieldPos, subPos);
      }
    }

    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyName(String name, Transformer transformer) {
    boolean upperCase = name.toUpperCase().equals(name);
    boolean lowerCase = name.toLowerCase().equals(name);

    String nameCapitalized = name.toUpperCase();

    for (Prefix prefix : LastNamePrefixVariation.getPrefixList()) {
      if (nameCapitalized.startsWith(prefix.prefixSpaceCapitalized)) {
        // check if our prefix with a space matches
        // so we would match "Mc" to "McDonald" or "De La" to "De La Hoya"
        name = name.substring(prefix.prefixSpaceCapitalized.length());
        break;
      }

      if (nameCapitalized.startsWith(prefix.prefixNoSpaceCapitalized)) {
        name = name.substring(prefix.prefixNoSpaceCapitalized.length());
        break;
      }

      if (nameCapitalized.startsWith(prefix.prefixSpaceCapitalized.trim())) {
        // check to see if we might match a prefix that has a space in the middle of it
        // but in the last name also doesn't have a prefix at the end
        // so we would match "De La" to "De LaHoya"
        name = name.substring(prefix.prefixSpaceCapitalized.trim().length());
        break;
      }
    }

    name = name.trim();

    if (StringUtils.isEmpty(name)) {
      name = transformer.getRandomValue("LAST_NAME");
    }

    if (upperCase) {
      name = name.toUpperCase();
    } else if (lowerCase) {
      name = name.toLowerCase();
    } else if (name.length() > 0) {
      // we want to make sure the first letter is capitalized like it was likely before
      name = name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    return name;
  }

  @Override
  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }
}
