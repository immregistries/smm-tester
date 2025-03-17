package org.immregistries.hart.util;


public class StringManipulation {
  public static int indexOfFirstConsonantAfterVowel(String input) {
    int pos = 0;
    String fn = input.toUpperCase();
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

  public static int indexOfSecondUppercase(String input) {
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

  public static String dateFromInts(int year, int month, int day) {
    String dob;
    dob = "" + year;
    if (month < 10) {
      dob += "0" + month;
    } else {
      dob += month;
    }
    if (day < 10) {
      dob += "0" + day;
    } else {
      dob += day;
    }
    return dob;
  }
}
