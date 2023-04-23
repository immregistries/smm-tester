package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class LastNamePrefixVariation extends ProcedureCommon implements ProcedureInterface {



  public LastNamePrefixVariation() {

  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if ("PID".equals(segmentName)) {
          int fieldPos = 5;
          int subPos = 1;
          String lastName = readValue(fields, fieldPos, subPos);
          lastName = varyName(lastName, transformer);
          updateValue(lastName, fields, fieldPos, subPos);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  protected static String varyName(String lastName, Transformer transformer) {
    boolean upperCase = lastName.toUpperCase().equals(lastName);
    boolean lowerCase = lastName.toLowerCase().equals(lastName);

    String lastNameCapitalized = lastName.toUpperCase();

    boolean keepLooking = true;
    for (Prefix prefix : prefixList) {
      if (lastNameCapitalized.equals(prefix.prefixNoSpaceCapitalized)
          || lastNameCapitalized.equals(prefix.prefixSpaceCapitalized)) {
        lastName = prefix.prefixSpace + transformer.getRandomValue("LAST_NAME");
        keepLooking = false;
        break;
      } else if (lastNameCapitalized.startsWith(prefix.prefixSpaceCapitalized)) {
        // take space away
        lastName =
            prefix.prefixNoSpace + lastName.substring(prefix.prefixSpaceCapitalized.length());
        keepLooking = false;
        break;
      } else if (lastNameCapitalized.startsWith(prefix.prefixNoSpaceCapitalized)) {
        // add space
        lastName =
            prefix.prefixSpace + lastName.substring(prefix.prefixNoSpaceCapitalized.length());
        keepLooking = false;
        break;
      }
    }

    if (keepLooking) {
      lastName =
          prefixList.get(transformer.getRandom().nextInt(prefixList.size())).prefixSpace + lastName;
    }

    if (upperCase) {
      lastName = lastName.toUpperCase();
    } else if (lowerCase) {
      lastName = lastName.toLowerCase();
    }

    return lastName;
  }



  private Transformer transformer;

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }


  private static List<Prefix> prefixList = new ArrayList<>();

  private static class Prefix {
    private Prefix(String prefix) {
      prefixNoSpace = "";
      prefixSpace = prefix.trim() + " ";
      for (byte b : prefixSpace.getBytes()) {
        if (b != ' ') {
          prefixNoSpace += (char) b;
        }
      }
      prefixNoSpaceCapitalized = prefixNoSpace.toUpperCase();
      prefixSpaceCapitalized = prefixSpace.toUpperCase();
    }

    private String prefixNoSpace;
    private String prefixSpace;
    private String prefixNoSpaceCapitalized;
    private String prefixSpaceCapitalized;
  }


  private static void add(String prefix) {
    prefix = prefix.trim();
    prefixList.add(new Prefix(prefix));
  }

  static {
    // The order matters, the ones at the top will match first. This is important as the shorter ones 
    // will match with the same ones as the longer ones. 
    add("Von");
    add("Ven Het");
    add("Van Der");
    add("Van Den");
    add("Van De");
    add("Van");
    add("Ter");
    add("Mic");
    add("Mhic");
    add("Mck");
    add("Mc");
    add("Mac");
    add("Le");
    add("La");
    add("El");
    add("Du");
    add("Du");
    add("Dos");
    add("Di");
    add("Der");
    add("De La");
    add("Della");
    add("Del");
    add("De");
    add("Da");
    add("Bet");
    add("Ben");
    add("Bar");
    add("Ap");
    add("Al");
    add("Af");
    add("Abu");
    add("Ab");

  }

}
