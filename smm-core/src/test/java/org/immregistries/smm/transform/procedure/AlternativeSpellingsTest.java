package org.immregistries.smm.transform.procedure;

import static org.junit.Assert.assertNotEquals;
import java.util.Arrays;
import org.immregistries.smm.transform.Transformer;
import org.immregistries.smm.transform.procedure.AlternativeSpellings.Field;
import org.junit.Test;

public class AlternativeSpellingsTest extends ProcedureCommonTest {

  @Test
  public void test() {
    Transformer t = new Transformer();
    Field field = AlternativeSpellings.Field.FIRST_NAME;

    testVariationDifferent("Maya", "PID-5.2", ProcedureFactory.FIRST_NAME_ALTERNATIVE_SPELLINGS, t);

    assertEquals("Zack", AlternativeSpellings.varyText("Zach", t, field));
    assertEquals("Zach", AlternativeSpellings.varyText("Zack", t, field));
    assertEquals("Elena", AlternativeSpellings.varyText("Elaina", t, field));
    assertEquals("Elaina", AlternativeSpellings.varyText("Elena", t, field));
    assertNotEquals("", AlternativeSpellings.varyText("", t, field));

    // test 3+ alt spelling groups
    for (int i = 0; i < 1000; i++) {
      assertTrue(Arrays.asList("Zoey", "Zoie", "Zowie")
          .contains(AlternativeSpellings.varyText("Zoe", t, field)));
      assertTrue(Arrays.asList("Zoe", "Zoie", "Zowie")
          .contains(AlternativeSpellings.varyText("Zoey", t, field)));
      assertTrue(Arrays.asList("Zoey", "Zoe", "Zowie")
          .contains(AlternativeSpellings.varyText("Zoie", t, field)));
      assertTrue(Arrays.asList("Zoey", "Zoie", "Zoe")
          .contains(AlternativeSpellings.varyText("Zowie", t, field)));

      assertTrue(Arrays.asList("Kaden", "Caden", "Kaiden", "Cayden", "Kaeden", "Kadan")
          .contains(AlternativeSpellings.varyText("Kadyn", t, field)));
      assertTrue(Arrays.asList("Kadyn", "Caden", "Kaiden", "Cayden", "Kaeden", "Kadan")
          .contains(AlternativeSpellings.varyText("Kaden", t, field)));
      assertTrue(Arrays.asList("Kaden", "Kadyn", "Kaiden", "Cayden", "Kaeden", "Kadan")
          .contains(AlternativeSpellings.varyText("Caden", t, field)));
      assertTrue(Arrays.asList("Kaden", "Caden", "Kadyn", "Cayden", "Kaeden", "Kadan")
          .contains(AlternativeSpellings.varyText("Kaiden", t, field)));
      assertTrue(Arrays.asList("Kaden", "Caden", "Kaiden", "Kadyn", "Kaeden", "Kadan")
          .contains(AlternativeSpellings.varyText("Cayden", t, field)));
      assertTrue(Arrays.asList("Kaden", "Caden", "Kaiden", "Cayden", "Kadyn", "Kadan")
          .contains(AlternativeSpellings.varyText("Kaeden", t, field)));
      assertTrue(Arrays.asList("Kaden", "Caden", "Kaiden", "Cayden", "Kaeden", "Kadyn")
          .contains(AlternativeSpellings.varyText("Kadan", t, field)));
    }

    assertNotEquals("Brad", AlternativeSpellings.varyText("Brad", t, field));
    assertEquals("sky", AlternativeSpellings.varyText("skye", t, field));
    assertEquals("FREYA", AlternativeSpellings.varyText("FREYJA", t, field));
    assertNotEquals("", AlternativeSpellings.varyText("", t, field));

    field = AlternativeSpellings.Field.MIDDLE_NAME;

    testVariationDifferent("Bob", "PID-5.3", ProcedureFactory.MIDDLE_NAME_ALTERNATIVE_SPELLINGS, t);

    assertEquals("Steven", AlternativeSpellings.varyText("Stephen", t, field));
    assertEquals("Stephen", AlternativeSpellings.varyText("Steven", t, field));
    assertEquals("Elle", AlternativeSpellings.varyText("Ell", t, field));
    assertEquals("Ell", AlternativeSpellings.varyText("Elle", t, field));
    assertNotEquals("Ell", AlternativeSpellings.varyText("", t, field));

    field = AlternativeSpellings.Field.MOTHERS_FIRST_NAME;

    testVariationDifferent("Sarah", "PID-6.2",
        ProcedureFactory.MOTHERS_FIRST_NAME_ALTERNATIVE_SPELLINGS, t);

    assertEquals("Noel", AlternativeSpellings.varyText("Nowell", t, field));
    assertEquals("Nowell", AlternativeSpellings.varyText("Noel", t, field));
    assertNotEquals("Steven", AlternativeSpellings.varyText("Stephen", t, field));
    assertNotEquals("Stephen", AlternativeSpellings.varyText("Steven", t, field));
    assertNotEquals("", AlternativeSpellings.varyText("", t, field));

    field = AlternativeSpellings.Field.LAST_NAME;

    testVariationDifferent("Graham", "PID-5.1", ProcedureFactory.LAST_NAME_ALTERNATIVE_SPELLINGS,
        t);

    assertEquals("Turner", AlternativeSpellings.varyText("Terner", t, field));
    assertEquals("Terner", AlternativeSpellings.varyText("Turner", t, field));
    assertNotEquals("", AlternativeSpellings.varyText("White", t, field));

    field = AlternativeSpellings.Field.MOTHERS_MAIDEN_NAME;

    testVariationDifferent("Hughes", "PID-6.1",
        ProcedureFactory.MOTHERS_MAIDEN_NAME_ALTERNATIVE_SPELLINGS, t);

    assertEquals("Coleman", AlternativeSpellings.varyText("Kuhlman", t, field));
    assertEquals("Kuhlman", AlternativeSpellings.varyText("Coleman", t, field));
    assertNotEquals("Black", AlternativeSpellings.varyText("Black", t, field));
    assertNotEquals("", AlternativeSpellings.varyText("", t, field));

    field = AlternativeSpellings.Field.ADDRESS_STREET;

    testVariationDifferent("567 Service Lane", "PID-11.1",
        ProcedureFactory.ADDRESS_STREET_ALTERNATIVE_SPELLINGS, t);

    String out = AlternativeSpellings.varyText("123 Main St.", t, field);
    assertNotEquals("123 Main St.", out);
    assertTrue(out.startsWith("123 "));
    assertTrue(out.endsWith(" St."));

    assertEquals("1234 Myer Rd.", AlternativeSpellings.varyText("1234 Meyer Rd.", t, field));

    field = AlternativeSpellings.Field.ADDRESS_CITY;

    testVariationDifferent("Roanoke", "PID-11.3",
        ProcedureFactory.ADDRESS_CITY_ALTERNATIVE_SPELLINGS, t);

    assertEquals("Golfport", AlternativeSpellings.varyText("Gulfport", t, field));
    assertEquals("Gulfport", AlternativeSpellings.varyText("Golfport", t, field));
    assertNotEquals("Madison", AlternativeSpellings.varyText("Madison", t, field));
    assertNotEquals("", AlternativeSpellings.varyText("", t, field));
  }

  private void testVariationDifferent(String startValue, String location, String procedure,
      Transformer transformer) {
    assertNotEquals(startValue, AlternativeSpellings.varyText(startValue, transformer,
        AlternativeSpellings.Field.FIRST_NAME));
    String testStart = transform(DEFAULT_TEST_MESSAGE, location + "=" + startValue);
    testProcedureChangesMessageAndDoesNotContain(testStart, startValue, procedure);
  }
}
