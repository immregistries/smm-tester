package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class TextTypoTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();

    testVariationDifferent("CARPENTER", "PID-5.1", ProcedureFactory.LAST_NAME_TYPO, transformer);
    testVariationDifferent("Samuel", "PID-5.2", ProcedureFactory.FIRST_NAME_TYPO, transformer);
    testVariationDifferent("Emily", "PID-5.2", ProcedureFactory.FIRST_NAME_TYPO, transformer);
    testVariationDifferent("Mary Sue", "PID-5.2", ProcedureFactory.FIRST_NAME_TYPO, transformer);
    testVariationDifferent("Ye Sune", "PID-5.2", ProcedureFactory.FIRST_NAME_TYPO, transformer);
    testVariationDifferent("4356180", "PID-13.7", ProcedureFactory.PHONE_TYPO, transformer);
    testVariationDifferent("something@gmail.com", "PID-13#2.7", ProcedureFactory.EMAIL_TYPO, transformer);
    testVariationDifferent("5678 Wooster Ln", "PID-11.1", ProcedureFactory.ADDRESS_STREET_TYPO, transformer);
    testVariationDifferent("Jennifer", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TYPO, transformer);
    testVariationDifferent("Ryan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TYPO, transformer);
    testVariationDifferent("Frederick Dempsey", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TYPO, transformer);
    testVariationDifferent("Stan", "PID-5.3", ProcedureFactory.MIDDLE_NAME_TYPO, transformer);
    testVariationDifferent("Grace", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TYPO, transformer);
    testVariationDifferent("Patrick", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TYPO, transformer);
    testVariationDifferent("Ziegfried", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TYPO, transformer);
    testVariationDifferent("Threepwood", "PID-6.1", ProcedureFactory.MOTHERS_MAIDEN_NAME_TYPO, transformer);
  }
  
  private void testVariationDifferent(String startValue, String location, String procedure, Transformer transformer) {
    assertNotEquals(startValue, TextTypo.varyText(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessageAndDoesNotContain(testStart, startValue, procedure);
  }

}
