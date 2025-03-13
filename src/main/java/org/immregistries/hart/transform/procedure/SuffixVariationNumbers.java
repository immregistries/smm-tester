package org.immregistries.hart.transform.procedure;

public class SuffixVariationNumbers extends AbstractSuffixVariation {

  protected static final String[] SUFFIXES = new String[] {"2nd", "3rd"};
  
  @Override
  protected String[] getSuffixes() {
    return SUFFIXES;
  }
}
