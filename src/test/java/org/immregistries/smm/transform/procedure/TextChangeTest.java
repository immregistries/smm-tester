package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.junit.Test;

public class TextChangeTest extends ProcedureCommonTest {


  @Test
  public void test() {
    Transformer transformer = new Transformer();

    testVariationDifferent("4356180", "PID-13.7", ProcedureFactory.PHONE_CHANGE,
        TextChange.Field.PHONE, transformer);
    testVariationDifferent("something@gmail.com", "PID-13#2.7", ProcedureFactory.EMAIL_CHANGE,
        TextChange.Field.EMAIL, transformer);

  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      TextChange.Field field, Transformer transformer) {
    assertNotEquals(startValue, TextChange.varyText(startValue, field, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessageAndDoesNotContain(testStart, startValue, procedure);
  }

}
