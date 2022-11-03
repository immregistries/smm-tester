package org.immregistries.smm.transform.procedure;

import org.immregistries.smm.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class FirstNameRepeatedConsonantsTest extends TestCase {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    FirstNameRepeatedConsonants.varyName("Sammie", transformer);
    FirstNameRepeatedConsonants.varyName("Sam", transformer);
    FirstNameRepeatedConsonants.varyName("Kalyn", transformer);
    FirstNameRepeatedConsonants.varyName("Howard", transformer);
    FirstNameRepeatedConsonants.varyName("Heather", transformer);
    FirstNameRepeatedConsonants.varyName("Ivan", transformer);
  }

}
