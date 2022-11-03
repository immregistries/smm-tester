package org.immregistries.smm.transform.procedure;

import org.junit.Test;
import junit.framework.TestCase;

public class FirstNameAlternativeEndingsTest extends TestCase {


  @Test
  public void test() {
    
    assertEquals("Sann", FirstNameAlternativeEndings.varyName("Sam"));
    assertEquals("Liann", FirstNameAlternativeEndings.varyName("Liam"));
    assertEquals("Noaph", FirstNameAlternativeEndings.varyName("Noah"));
    assertEquals("Oliverrie", FirstNameAlternativeEndings.varyName("Oliver"));
    assertEquals("Elijaph", FirstNameAlternativeEndings.varyName("Elijah"));
    assertEquals("Jamesz", FirstNameAlternativeEndings.varyName("James"));
    assertEquals("Williann", FirstNameAlternativeEndings.varyName("William"));
    assertEquals("Benjamimm", FirstNameAlternativeEndings.varyName("Benjamin"));
    assertEquals("Lucasz", FirstNameAlternativeEndings.varyName("Lucas"));
    assertEquals("Henrie", FirstNameAlternativeEndings.varyName("Henry"));
    assertEquals("Oliviay", FirstNameAlternativeEndings.varyName("Olivia"));
    assertEquals("Emmay", FirstNameAlternativeEndings.varyName("Emma"));
    assertEquals("Charlottee", FirstNameAlternativeEndings.varyName("Charlotte"));
    assertEquals("Ameliay", FirstNameAlternativeEndings.varyName("Amelia"));
    assertEquals("Avay", FirstNameAlternativeEndings.varyName("Ava"));
    assertEquals("Sophiay", FirstNameAlternativeEndings.varyName("Sophia"));
    assertEquals("Isabellay", FirstNameAlternativeEndings.varyName("Isabella"));
    assertEquals("Miay", FirstNameAlternativeEndings.varyName("Mia"));
    assertEquals("Evelymm", FirstNameAlternativeEndings.varyName("Evelyn"));
    assertEquals("Harperrie", FirstNameAlternativeEndings.varyName("Harper"));




  }

}
