package org.immregistries.smm.transform.procedure;

import org.junit.Test;

public class SuffixVariationNumbersTest extends AbstractSuffixVariationTest {

  @Override
  protected AbstractSuffixVariation getInstance() {
    return new SuffixVariationNumbers();
  }

  @Override
  protected String getProcedureFactoryName() {
    return ProcedureFactory.SUFFIX_VARIATION_NUMBERS;
  }
  
  @Test
  public void test() {
    // first bunch will create a suffix
    // the null indicates we don't know which suffix will be randomly selected
    // but we will test that one is there

    testVariation("Smith", "Sam", "2nd", "Smith 2nd", "Sam", "");
    testVariation("Smith 2nd", "Sam", "", "Smith", "Sam 2nd", "");
    testVariation("Smith", "Sam 2nd", "", "Smith", "Sam", "2nd");

    testVariation("Smith", "Sam", "3rd", "Smith 3rd", "Sam", "");
    testVariation("Smith 3rd", "Sam", "", "Smith", "Sam 3rd", "");
    testVariation("Smith", "Sam 3rd", "", "Smith", "Sam", "3rd");

    testVariation("SMITH", "SAM", "3rd", "SMITH 3RD", "SAM", "");
    testVariation("SMITH 3RD", "SAM", "", "SMITH", "SAM 3RD", "");
    testVariation("SMITH", "SAM 3RD", "", "SMITH", "SAM", "3rd");
  }
}
