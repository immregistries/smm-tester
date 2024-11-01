package org.immregistries.smm.transform.procedure;

public class SuffixVariation extends AbstractSuffixVariation {

  //order is important, III has to come before II
  protected static final String[] SUFFIXES = new String[] {"Jr", "Junior", "Sr", "Senior", "III", "II"};
  
  @Override
  protected String[] getSuffixes() {
    return SUFFIXES;
  }
}
