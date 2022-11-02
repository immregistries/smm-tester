package org.immregistries.smm.transform.procedure;

import org.immregistries.smm.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class LastNamePrefixVariationTest extends TestCase {


  @Test
  public void test() {
    
    Transformer transformer = new Transformer();
    assertTrue(LastNamePrefixVariation.varyName("Smith", transformer).endsWith(" Smith"));
    assertTrue(LastNamePrefixVariation.varyName("Von", transformer).startsWith("Von "));
    assertEquals("VonWashington", LastNamePrefixVariation.varyName("Von Washington", transformer));
    assertEquals("Von Washington", LastNamePrefixVariation.varyName("VonWashington", transformer));
    assertEquals("DeLaWashington", LastNamePrefixVariation.varyName("De La Washington", transformer));
    assertEquals("De La Washington", LastNamePrefixVariation.varyName("DeLaWashington", transformer));
    assertTrue(LastNamePrefixVariation.varyName("smith", transformer).endsWith(" smith"));
    assertTrue(LastNamePrefixVariation.varyName("SMITH", transformer).endsWith(" SMITH"));
    
  }

}
