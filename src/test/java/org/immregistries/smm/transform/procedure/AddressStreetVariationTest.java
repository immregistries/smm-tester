package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class AddressStreetVariationTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    assertNotEquals("1562 Weerijnen Ln",
        AddressStreetVariation.varyAddress("1562 Weerijnen Ln", transformer));
    
    testProcedureChangesMessage(DEFAULT_TEST_MESSAGE, ProcedureFactory.ADDRESS_STREET_CHANGE);

  }

}
