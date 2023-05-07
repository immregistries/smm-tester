package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import org.immregistries.smm.transform.Transformer;
import org.immregistries.smm.transform.procedure.HyphenateVariation.Field;
import org.junit.Test;

public class HyphenateVariationTest extends ProcedureCommonTest {

  private enum TestingGroup {
                             FIRST_NAME("PID-5.2", Field.FIRST_NAME,
                                 ProcedureFactory.FIRST_NAME_HYPHENATE_VARIATION),
                             MIDDLE_NAME("PID-5.3", Field.MIDDLE_NAME,
                                 ProcedureFactory.MIDDLE_NAME_HYPHENATE_VARIATION),
                             LAST_NAME("PID-5.1", Field.LAST_NAME,
                                 ProcedureFactory.LAST_NAME_HYPHENATE_VARIATION),
                             MOTHERS_MAIDEN_NAME("PID-6.1", Field.MOTHERS_MAIDEN_NAME,
                                 ProcedureFactory.MOTHERS_MAIDEN_NAME_HYPHENATE_VARIATION),
                             MOTHERS_FIRST_NAME("PID-6.2", Field.MOTHERS_FIRST_NAME,
                                 ProcedureFactory.MOTHERS_FIRST_NAME_HYPHENATE_VARIATION),
                             ADDRESS_STREET("PID-11.1", Field.ADDRESS_STREET,
                                 ProcedureFactory.ADDRESS_STREET_HYPHENATE_VARIATION),
                             ADDRESS_CITY("PID-11.3", Field.ADDRESS_CITY,
                                 ProcedureFactory.ADDRESS_CITY_HYPHENATE_VARIATION),
                             EMAIL("PID-13.4", Field.EMAIL,
                                 ProcedureFactory.EMAIL_HYPHENATE_VARIATION);

    String location;
    Field field;
    String procedure;

    private TestingGroup(String location, Field field, String procedure) {
      this.location = location;
      this.field = field;
      this.procedure = procedure;
    }
  }

  @Test
  public void test() {
    Transformer transformer = new Transformer();

    for (TestingGroup group : TestingGroup.values()) {
      String location = group.location;
      Field field = group.field;
      String procedure = group.procedure;

      if (group == TestingGroup.EMAIL) {
        String email = "bob@gmail.com";
        String varied = HyphenateVariation.varyName(email, transformer, field);
        assertTrue(varied.length() > email.length());
        assertTrue(varied.startsWith("bob-"));
        assertTrue(varied.endsWith("@gmail.com"));

        email = "bob-jones@gmail.com";
        varied = HyphenateVariation.varyName(email, transformer, field);
        assertTrue(varied.length() > email.length());
        assertTrue(varied.startsWith("bob-"));
        assertTrue(varied.endsWith("@gmail.com"));
        continue;
      }

      testVariation("Smith-Jones", "Smith Jones", transformer, location, field, procedure);
      testVariation("Smith Jones", "Smith-Jones", transformer, location, field, procedure);
      testVariation("Smith-jones", "Smith Jones", transformer, location, field, procedure);
      testVariation("smith-jones", "smith jones", transformer, location, field, procedure);
      assertEquals("Smith-Jones", HyphenateVariation.varyName(
          HyphenateVariation.varyName("Smith-Jones", transformer, field), transformer, field));
      testVariationDifferent("Smith", transformer, location, field, procedure);
      testVariationDifferent("JONES", transformer, location, field, procedure);
      testVariationDifferent("carpenter", transformer, location, field, procedure);
    }
  }

  private void testVariation(String startValue, String endValue, Transformer transformer,
      String location, Field field, String procedure) {
    assertEquals(endValue, HyphenateVariation.varyName(startValue, transformer, field));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    String testEnd = transform(DEFAULT_TEST_MESSAGE, location + "=" + endValue);
    testEquals(testStart, testEnd, procedure);
  }

  private void testVariationDifferent(String startValue, Transformer transformer, String location,
      Field field, String procedure) {
    assertNotEquals(startValue, HyphenateVariation.varyName(startValue, transformer, field));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessage(testStart, procedure);
  }
}
