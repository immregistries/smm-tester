package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.junit.Test;
import junit.framework.TestCase;

public class SuffixVariationTest extends TestCase {


  @Test
  public void test() {

    {
      String[] test = SuffixVariation.varyName("Smith", "Sam", "Jr");
      assertEquals("Smith Jr", test[0]);
      assertEquals("Sam", test[1]);
      assertEquals("", test[2]);
      test = SuffixVariation.varyName("Smith Jr", "Sam", "");
      assertEquals("Smith", test[0]);
      assertEquals("Sam Jr", test[1]);
      assertEquals("", test[2]);
      test = SuffixVariation.varyName("Smith", "Sam Jr", "");
      assertEquals("Smith", test[0]);
      assertEquals("Sam", test[1]);
      assertEquals("Jr", test[2]);
    }
    {
      String[] test = SuffixVariation.varyName("Smith", "Sam", "");
      assertEquals("Smith", test[0]);
      assertEquals("Sam", test[1]);
      assertNotEquals("", test[2]);
    }
    {
      String[] test = SuffixVariation.varyName("Smith", "Sam", "");
      assertEquals("Smith", test[0]);
      assertEquals("Sam", test[1]);
      assertNotEquals("", test[2]);
    }
    {
      String[] test = SuffixVariation.varyName("Smith", "Sam", "");
      assertEquals("Smith", test[0]);
      assertEquals("Sam", test[1]);
      assertNotEquals("", test[2]);
    }
    {
      String[] test = SuffixVariation.varyName("Smith", "Sam", "UNKNOWN");
      assertEquals("Smith", test[0]);
      assertEquals("Sam", test[1]);
      assertNotEquals("", test[2]);
    }

  }

}
