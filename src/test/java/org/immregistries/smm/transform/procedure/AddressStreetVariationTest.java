package org.immregistries.smm.transform.procedure;

import org.immregistries.smm.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class AddressStreetVariationTest extends TestCase {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    AddressStreetVariation.varyAddress("1562 Weerijnen Ln", transformer);
  }

}
