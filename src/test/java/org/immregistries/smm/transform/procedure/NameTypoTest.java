package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class NameTypoTest extends TestCase {


  @Test
  public void test() {
    Transformer transformer = new Transformer();

    assertNotEquals("Samuel", NameTypo.varyName("Samuel", transformer));
    assertNotEquals("Emily", NameTypo.varyName("Emily", transformer));
    assertNotEquals("Mary Sue", NameTypo.varyName("Mary Sue", transformer));
    assertNotEquals("Ye Sune", NameTypo.varyName("Ye Sune", transformer));
    assertNotEquals("CARPENTER", NameTypo.varyName("CARPENTER", transformer));

  }

}
