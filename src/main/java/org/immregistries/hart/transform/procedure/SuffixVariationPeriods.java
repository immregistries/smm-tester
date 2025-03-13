package org.immregistries.hart.transform.procedure;

public class SuffixVariationPeriods extends AbstractSuffixVariation {

  protected static final String[] SUFFIXES = new String[] {"Jr.", "Sr."};
  
  @Override
  protected String[] getSuffixes() {
    return SUFFIXES;
  }
}
