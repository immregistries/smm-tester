package org.immregistries.smm.roster.loader;

import static org.junit.Assert.assertEquals;
import org.immregistries.smm.roster.model.Grade;
import org.junit.Test;

public class GradeDeterminerTest {
  
  @Test
  public void testBlankInputReturnsUnknown() {
    assertEquals(Grade.UNKNOWN, GradeDeterminer.determine(""));
    assertEquals(Grade.UNKNOWN, GradeDeterminer.determine("   "));
    assertEquals(Grade.UNKNOWN, GradeDeterminer.determine(null));
  }

  @Test
  public void testUngraded() {
    assertEquals(Grade.UNGRADED, GradeDeterminer.determine("U"));
    assertEquals(Grade.UNGRADED, GradeDeterminer.determine("ungraded"));
    assertEquals(Grade.UNGRADED, GradeDeterminer.determine("UNGRADED"));
  }

  @Test
  public void testPreKindergarten() {
    assertEquals(Grade.PRE_KINDERGARTEN, GradeDeterminer.determine("PK"));
    assertEquals(Grade.PRE_KINDERGARTEN, GradeDeterminer.determine("Pre-K"));
    assertEquals(Grade.PRE_KINDERGARTEN, GradeDeterminer.determine("pre-k"));
    assertEquals(Grade.PRE_KINDERGARTEN, GradeDeterminer.determine("PreK"));
    assertEquals(Grade.PRE_KINDERGARTEN, GradeDeterminer.determine("Pre-Kindergarten"));
  }

  @Test
  public void testKindergarten() {
    assertEquals(Grade.KINDERGARTEN, GradeDeterminer.determine("K"));
    assertEquals(Grade.KINDERGARTEN, GradeDeterminer.determine("k"));
    assertEquals(Grade.KINDERGARTEN, GradeDeterminer.determine("Kindergarten"));
    assertEquals(Grade.KINDERGARTEN, GradeDeterminer.determine("kindergarten"));
    assertEquals(Grade.KINDERGARTEN, GradeDeterminer.determine("KIN"));
  }

  @Test
  public void testPrimaryGrades() {
    assertEquals(Grade.FIRST, GradeDeterminer.determine("1"));
    assertEquals(Grade.FIRST, GradeDeterminer.determine("1st grade"));
    assertEquals(Grade.FIRST, GradeDeterminer.determine("first"));

    assertEquals(Grade.SECOND, GradeDeterminer.determine("2"));
    assertEquals(Grade.SECOND, GradeDeterminer.determine("2nd grade"));
    assertEquals(Grade.SECOND, GradeDeterminer.determine("second"));

    assertEquals(Grade.THIRD, GradeDeterminer.determine("3"));
    assertEquals(Grade.THIRD, GradeDeterminer.determine("3rd grade"));
    assertEquals(Grade.THIRD, GradeDeterminer.determine("third"));
  }

  @Test
  public void testUpperGrades() {
    assertEquals(Grade.FOURTH, GradeDeterminer.determine("4"));
    assertEquals(Grade.FOURTH, GradeDeterminer.determine("4th grade"));
    assertEquals(Grade.FOURTH, GradeDeterminer.determine("fourth"));

    assertEquals(Grade.FIFTH, GradeDeterminer.determine("5"));
    assertEquals(Grade.FIFTH, GradeDeterminer.determine("5th grade"));
    assertEquals(Grade.FIFTH, GradeDeterminer.determine("fifth"));

    assertEquals(Grade.SIXTH, GradeDeterminer.determine("6"));
    assertEquals(Grade.SIXTH, GradeDeterminer.determine("6th grade"));
    assertEquals(Grade.SIXTH, GradeDeterminer.determine("sixth"));

    assertEquals(Grade.SEVENTH, GradeDeterminer.determine("7"));
    assertEquals(Grade.SEVENTH, GradeDeterminer.determine("7th grade"));
    assertEquals(Grade.SEVENTH, GradeDeterminer.determine("seventh"));

    assertEquals(Grade.EIGHTH, GradeDeterminer.determine("8"));
    assertEquals(Grade.EIGHTH, GradeDeterminer.determine("8th grade"));
    assertEquals(Grade.EIGHTH, GradeDeterminer.determine("eighth"));

    assertEquals(Grade.NINTH, GradeDeterminer.determine("9"));
    assertEquals(Grade.NINTH, GradeDeterminer.determine("9th grade"));
    assertEquals(Grade.NINTH, GradeDeterminer.determine("ninth"));

    assertEquals(Grade.TENTH, GradeDeterminer.determine("10"));
    assertEquals(Grade.TENTH, GradeDeterminer.determine("10th grade"));
    assertEquals(Grade.TENTH, GradeDeterminer.determine("tenth"));

    assertEquals(Grade.ELEVENTH, GradeDeterminer.determine("11"));
    assertEquals(Grade.ELEVENTH, GradeDeterminer.determine("11th grade"));
    assertEquals(Grade.ELEVENTH, GradeDeterminer.determine("eleventh"));

    assertEquals(Grade.TWELFTH, GradeDeterminer.determine("12"));
    assertEquals(Grade.TWELFTH, GradeDeterminer.determine("12th grade"));
    assertEquals(Grade.TWELFTH, GradeDeterminer.determine("twelfth"));
  }

  @Test
  public void testUnknownValues() {
    assertEquals(Grade.UNKNOWN, GradeDeterminer.determine("abc"));
    assertEquals(Grade.UNKNOWN, GradeDeterminer.determine("grade 99"));
  }
}   
