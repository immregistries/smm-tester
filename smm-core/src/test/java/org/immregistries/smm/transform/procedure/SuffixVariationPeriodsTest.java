package org.immregistries.smm.transform.procedure;

import org.junit.Test;

public class SuffixVariationPeriodsTest extends AbstractSuffixVariationTest {

  @Override
  protected AbstractSuffixVariation getInstance() {
    return new SuffixVariationPeriods();
  }

  @Override
  protected String getProcedureFactoryName() {
    return ProcedureFactory.SUFFIX_VARIATION_PERIODS;
  }
  
  @Test
  public void test() {
    // first bunch will create a suffix
    // the null indicates we don't know which suffix will be randomly selected
    // but we will test that one is there

    testVariation("Smith", "Sam", "Jr.", "Smith Jr.", "Sam", "");
    testVariation("Smith Jr.", "Sam", "", "Smith", "Sam Jr.", "");
    testVariation("Smith", "Sam Jr.", "", "Smith", "Sam", "Jr.");

    testVariation("Smith", "Sam", "Sr.", "Smith Sr.", "Sam", "");
    testVariation("Smith Sr.", "Sam", "", "Smith", "Sam Sr.", "");
    testVariation("Smith", "Sam Sr.", "", "Smith", "Sam", "Sr.");
  }
}
