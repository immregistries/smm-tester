package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class AddressStreetVariation extends ProcedureCommon {

  public AddressStreetVariation() {}

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    for (String[] fields : fieldsList) {
      String segmentName = fields[0];
      if ("PID".equals(segmentName)) {
        int fieldPos = 11;
        int subPos = 1;
        String address = readValue(fields, fieldPos, subPos).trim();
        address = varyAddress(address, transformer);
        updateValue(address, fields, fieldPos, subPos);
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyAddress(String address, Transformer transformer) {
    return replaceAddressStreet(address, transformer.getRandomValue("STREET_NAME"));
  }

  protected static int findFirstConsentAfterVowel(String firstName) {
    int pos = 0;
    String fn = firstName.toUpperCase();
    boolean foundVowel = false;
    while (pos < fn.length()) {
      char c = fn.charAt(pos);
      if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U') {
        foundVowel = true;
      } else if (foundVowel) {
        return pos;
      }
      pos++;
    }
    return -1;
  }

  protected static int findAnotherCapital(String firstName) {
    if (firstName.equals(firstName.toUpperCase())) {
      return -1;
    }
    int pos = 1;
    while (pos < firstName.length()) {
      char c = firstName.charAt(pos);
      if (c < 'a') {
        return pos;
      }
      pos++;
    }
    return -1;
  }

  private Transformer transformer = null;

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }
}
