package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class TextTypoTest extends TestCase {


  @Test
  public void test() {
    Transformer transformer = new Transformer();

    assertNotEquals("Samuel", TextTypo.varyText("Samuel", transformer));
    assertNotEquals("Emily", TextTypo.varyText("Emily", transformer));
    assertNotEquals("Mary Sue", TextTypo.varyText("Mary Sue", transformer));
    assertNotEquals("Ye Sune", TextTypo.varyText("Ye Sune", transformer));
    assertNotEquals("CARPENTER", TextTypo.varyText("CARPENTER", transformer));
    assertNotEquals("4356180", TextTypo.varyText("4356180", transformer));
    assertNotEquals("something@gmail.com", TextTypo.varyText("something@gmail.com", transformer));

  }

}
