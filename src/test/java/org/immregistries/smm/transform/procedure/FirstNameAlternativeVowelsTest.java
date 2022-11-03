package org.immregistries.smm.transform.procedure;

import org.immregistries.smm.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class FirstNameAlternativeVowelsTest extends TestCase {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    FirstNameAlternativeVowels.varyName("Sammie", transformer);
    FirstNameAlternativeVowels.varyName("Sammie", transformer);
    FirstNameAlternativeVowels.varyName("Sammie", transformer);
    FirstNameAlternativeVowels.varyName("Sammie", transformer);
    FirstNameAlternativeVowels.varyName("Sammie", transformer);
    FirstNameAlternativeVowels.varyName("Herbert", transformer);
    FirstNameAlternativeVowels.varyName("Herbert", transformer);
    FirstNameAlternativeVowels.varyName("Herbert", transformer);
    FirstNameAlternativeVowels.varyName("Herbert", transformer);
    FirstNameAlternativeVowels.varyName("Greyson", transformer);
    FirstNameAlternativeVowels.varyName("Greyson", transformer);
    FirstNameAlternativeVowels.varyName("Greyson", transformer);
    FirstNameAlternativeVowels.varyName("Greyson", transformer);
    FirstNameAlternativeVowels.varyName("Greyson", transformer);
    FirstNameAlternativeVowels.varyName("Emily", transformer);
    FirstNameAlternativeVowels.varyName("Emily", transformer);
    FirstNameAlternativeVowels.varyName("Emily", transformer);
    FirstNameAlternativeVowels.varyName("Emily", transformer);
    FirstNameAlternativeVowels.varyName("Emily", transformer);
    FirstNameAlternativeVowels.varyName("Emily", transformer);
    FirstNameAlternativeVowels.varyName("Earl", transformer);
    FirstNameAlternativeVowels.varyName("Earl", transformer);
    FirstNameAlternativeVowels.varyName("Earl", transformer);
    FirstNameAlternativeVowels.varyName("Earl", transformer);
    FirstNameAlternativeVowels.varyName("Earl", transformer);

    assertTrue(FirstNameAlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));
    assertTrue(FirstNameAlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));
    assertTrue(FirstNameAlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));
    assertTrue(FirstNameAlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));
    assertTrue(FirstNameAlternativeVowels.varyName("Annie", transformer).startsWith("Ann"));
    FirstNameAlternativeVowels.varyName("Annie", transformer);
    assertEquals("Ann", FirstNameAlternativeVowels.varyName("Ann", transformer));
    assertEquals("Ann", FirstNameAlternativeVowels.varyName("Ann", transformer));
    assertEquals("Ann", FirstNameAlternativeVowels.varyName("Ann", transformer));
    assertEquals("Ann", FirstNameAlternativeVowels.varyName("Ann", transformer));
    assertEquals("Ann", FirstNameAlternativeVowels.varyName("Ann", transformer));
    assertEquals("Ann", FirstNameAlternativeVowels.varyName("Ann", transformer));
  }

}
