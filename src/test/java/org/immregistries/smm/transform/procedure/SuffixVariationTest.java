package org.immregistries.smm.transform.procedure;

import org.junit.Test;

public class SuffixVariationTest extends AbstractSuffixVariationTest {

  @Override
  protected AbstractSuffixVariation getInstance() {
    return new SuffixVariation();
  }

  @Override
  protected String getProcedureFactoryName() {
    return ProcedureFactory.SUFFIX_VARIATION;
  }
  
  @Test
  public void test() {
    // first bunch will create a suffix
    // the null indicates we don't know which suffix will be randomly selected
    // but we will test that one is there
    testVariation("Smith", "Sam", "", "Smith", "Sam", null);
    testVariation("Smith", "Sam", "UNKNOWN", "Smith", "Sam", null);
    testVariation("Smith", "Sam", "the Second", "Smith", "Sam", null);
    testVariation("Smith", "Sam", "the Third", "Smith", "Sam", null);

    testVariation("Smith", "Sam", "Jr", "Smith Jr", "Sam", "");
    testVariation("Smith Jr", "Sam", "", "Smith", "Sam Jr", "");
    testVariation("Smith", "Sam Jr", "", "Smith", "Sam", "Jr");

    testVariation("Smith", "Sam", "Junior", "Smith Junior", "Sam", "");
    testVariation("Smith Junior", "Sam", "", "Smith", "Sam Junior", "");
    testVariation("Smith", "Sam Junior", "", "Smith", "Sam", "Junior");

    testVariation("Smith", "Sam", "Sr", "Smith Sr", "Sam", "");
    testVariation("Smith Sr", "Sam", "", "Smith", "Sam Sr", "");
    testVariation("Smith", "Sam Sr", "", "Smith", "Sam", "Sr");

    testVariation("Smith", "Sam", "Senior", "Smith Senior", "Sam", "");
    testVariation("Smith Senior", "Sam", "", "Smith", "Sam Senior", "");
    testVariation("Smith", "Sam Senior", "", "Smith", "Sam", "Senior");

    testVariation("Smith", "Sam", "II", "Smith II", "Sam", "");
    testVariation("Smith II", "Sam", "", "Smith", "Sam II", "");
    testVariation("Smith", "Sam II", "", "Smith", "Sam", "II");

    testVariation("Smith", "Sam", "III", "Smith III", "Sam", "");
    testVariation("Smith III", "Sam", "", "Smith", "Sam III", "");
    testVariation("Smith", "Sam III", "", "Smith", "Sam", "III");

    testVariation("smith", "sam", "jr", "smith jr", "sam", "");
    testVariation("smith jr", "sam", "", "smith", "sam jr", "");
    testVariation("smith", "sam jr", "", "smith", "sam", "Jr");
  }
}
