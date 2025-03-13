package org.immregistries.hart.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.hart.transform.Transformer;
import org.junit.Test;

public class FirstNameConvertNicknameTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer transformer = new Transformer();

    assertEquals("Alexander", FirstNameConvertNickname.varyText("Xander", transformer));
    assertEquals("Carolina", FirstNameConvertNickname.varyText("Caro", transformer));
    assertEquals("Ignacio", FirstNameConvertNickname.varyText("Nacho", transformer));

    assertEquals("Shaquille", FirstNameConvertNickname.varyText("Shaq", transformer));
    assertEquals("shaquille", FirstNameConvertNickname.varyText("shaq", transformer));
    assertEquals("SHAQUILLE", FirstNameConvertNickname.varyText("SHAQ", transformer));

    assertEquals("Shaq", FirstNameConvertNickname.varyText("Shaquille", transformer));
    assertEquals("shaq", FirstNameConvertNickname.varyText("shaquille", transformer));
    assertEquals("SHAQ", FirstNameConvertNickname.varyText("SHAQUILLE", transformer));

    assertNotEquals("", FirstNameConvertNickname.varyText("", transformer));

    testVariationDifferent("Avery", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
    testVariationDifferent("Chloe", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
    testVariationDifferent("Harper", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
    testVariationDifferent("Aaron", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
    testVariationDifferent("Alexander", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
    testVariationDifferent("Benjamin", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
    testVariationDifferent("Benny", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
    testVariationDifferent("Elizabeth", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
    testVariationDifferent("Lizzie", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
    testVariationDifferent("William", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
    testVariationDifferent("DeeDee", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
    testVariationDifferent("Lils", "PID-5.2", ProcedureFactory.FIRST_NAME_CONVERT_TO_NICKNAME,
        transformer);
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer) {
    assertNotEquals(startValue, FirstNameConvertNickname.varyText(startValue, transformer));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessageAndDoesNotContain(testStart, startValue, procedure);
  }
}
