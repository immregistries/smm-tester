package org.immregistries.hart.transform.procedure;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.hart.transform.TransformRequest;
import org.immregistries.hart.transform.Transformer;

public class AddressStreetVariation extends ProcedureCommon {

  public AddressStreetVariation() {}

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList) throws IOException {
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

  //TODO (klindgren): Does this belong here?
  protected static int findFirstConsonantAfterVowel(String firstName) {
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

  //TODO (klindgren): Does this belong here?
  protected static int findAnotherCapital(String input) {
    if (input.equals(input.toUpperCase())) {
      return -1;
    }
    int pos = 1;
    while (pos < input.length()) {
      char c = input.charAt(pos);
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
