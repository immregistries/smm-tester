package org.immregistries.smm.transform.procedure;

import org.immregistries.smm.transform.Transformer;
import org.junit.Test;
import junit.framework.TestCase;

public class FirstNameRepeatedConsonentsTest extends TestCase {


  @Test
  public void test() {
    Transformer transformer = new Transformer();
    FirstNameRepeatedConsonents.varyName("Sammie", transformer);
    FirstNameRepeatedConsonents.varyName("Sam", transformer);
    FirstNameRepeatedConsonents.varyName("Kalyn", transformer);
    FirstNameRepeatedConsonents.varyName("Howard", transformer);
    FirstNameRepeatedConsonents.varyName("Heather", transformer);
    FirstNameRepeatedConsonents.varyName("Ivan", transformer);
  }

}
