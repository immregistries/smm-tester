package org.immregistries.smm.transform.procedure;

import org.junit.Test;
import junit.framework.TestCase;

public class FirstNameAlternativeBeginningsTest extends TestCase {


  @Test
  public void test() {
    
    assertEquals("Tsam", FirstNameAlternativeBeginnings.varyName("Sam"));
    assertEquals("Jiam", FirstNameAlternativeBeginnings.varyName("Liam"));
    assertEquals("Moah", FirstNameAlternativeBeginnings.varyName("Noah"));
    assertEquals("Aliver", FirstNameAlternativeBeginnings.varyName("Oliver"));
    assertEquals("Alijah", FirstNameAlternativeBeginnings.varyName("Elijah"));
    assertEquals("Games", FirstNameAlternativeBeginnings.varyName("James"));
    assertEquals("Villiam", FirstNameAlternativeBeginnings.varyName("William"));
    assertEquals("Venjamin", FirstNameAlternativeBeginnings.varyName("Benjamin"));
    assertEquals("Jucas", FirstNameAlternativeBeginnings.varyName("Lucas"));
    assertEquals("Venry", FirstNameAlternativeBeginnings.varyName("Henry"));
    assertEquals("Alivia", FirstNameAlternativeBeginnings.varyName("Olivia"));
    assertEquals("Amma", FirstNameAlternativeBeginnings.varyName("Emma"));
    assertEquals("Sarlotte", FirstNameAlternativeBeginnings.varyName("Charlotte"));
    assertEquals("Emelia", FirstNameAlternativeBeginnings.varyName("Amelia"));
    assertEquals("Eva", FirstNameAlternativeBeginnings.varyName("Ava"));
    assertEquals("Tsophia", FirstNameAlternativeBeginnings.varyName("Sophia"));
    assertEquals("Esabella", FirstNameAlternativeBeginnings.varyName("Isabella"));
    assertEquals("Nia", FirstNameAlternativeBeginnings.varyName("Mia"));
    assertEquals("Avelyn", FirstNameAlternativeBeginnings.varyName("Evelyn"));
    assertEquals("Varper", FirstNameAlternativeBeginnings.varyName("Harper"));




  }

}
