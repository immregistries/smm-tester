package org.immregistries.smm.transform.procedure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import org.immregistries.smm.transform.TransformRequest;
import org.immregistries.smm.transform.Transformer;

public class FirstNameRepeatedConsonents extends ProcedureCommon implements ProcedureInterface {



  public FirstNameRepeatedConsonents() {

  }

  public void doProcedure(TransformRequest transformRequest, LinkedList<String> tokenList)
      throws IOException {
    List<String[]> fieldsList = readMessage(transformRequest);
    {
      for (String[] fields : fieldsList) {
        String segmentName = fields[0];
        if (segmentName.equals("PID")) {
          int fieldPos = 5;
          int subPos = 2;
          String lastName = readValue(fields, fieldPos, subPos);
          lastName = varyName(lastName, transformer);
          updateValue(lastName, fields, fieldPos, subPos);
        }
      }
    }
    putMessageBackTogether(transformRequest, fieldsList);
  }

  // n, l, t, s, b, d, f, m, n, p, r

  private static char[] REPEATABLE_CONSONANTS = new char[] {'b', 'c', 'd', 'f', 'g', 'l', 'm', 'n',
      'p', 'r', 's', 't', 'b', 'c', 'd', 'f', 'g', 'l', 'm', 'n', 'p', 'r', 's', 't', 'b', 'c', 'd',
      'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'y', 'z'};

  protected static String varyName(String firstName, Transformer transformer) {
    String firstNameOriginal = firstName;

    boolean upperCase = firstName.toUpperCase().equals(firstName);
    boolean lowerCase = firstName.toLowerCase().equals(firstName);

    Random random = transformer.getRandom();

    String firstNameLower = firstName.toLowerCase();
    for (int i = 0; i < 100; i++) {
      char c = REPEATABLE_CONSONANTS[random.nextInt(REPEATABLE_CONSONANTS.length)];
      int pos = firstNameLower.indexOf(c);
      if (pos > 0)
      {
        firstName = firstNameLower.substring(0, pos) + c + firstNameLower.substring(pos);
        break;
      }
    }

    if (upperCase) {
      firstName = firstName.toUpperCase();
    } else if (lowerCase) {
      firstName = firstName.toLowerCase();
    } else {
      firstName = capitalizeFirst(firstName);
    }

    System.out.println(firstNameOriginal + " --> " + firstName);
    return firstName;
  }


  private Transformer transformer;

  public void setTransformer(Transformer transformer) {
    this.transformer = transformer;
  }



}
