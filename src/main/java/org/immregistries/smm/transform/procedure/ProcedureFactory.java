package org.immregistries.smm.transform.procedure;

import org.apache.commons.lang3.StringUtils;
import org.immregistries.smm.transform.Transformer;

public class ProcedureFactory {

  // alternative vowels
  public static final String FIRST_NAME_ALTERNATIVE_VOWELS = "FIRST_NAME_ALTERNATIVE_VOWELS";
  public static final String MIDDLE_NAME_ALTERNATIVE_VOWELS = "MIDDLE_NAME_ALTERNATIVE_VOWELS";
  public static final String LAST_NAME_ALTERNATIVE_VOWELS = "LAST_NAME_ALTERNATIVE_VOWELS";
  public static final String MOTHERS_MAIDEN_NAME_ALTERNATIVE_VOWELS =
      "MOTHERS_MAIDEN_NAME_ALTERNATIVE_VOWELS";
  public static final String MOTHERS_FIRST_NAME_ALTERNATIVE_VOWELS =
      "MOTHERS_FIRST_NAME_ALTERNATIVE_VOWELS";
  public static final String ADDRESS_STREET_ALTERNATIVE_VOWELS =
      "ADDRESS_STREET_ALTERNATIVE_VOWELS";
  public static final String ADDRESS_CITY_ALTERNATIVE_VOWELS = "ADDRESS_CITY_ALTERNATIVE_VOWELS";
  public static final String EMAIL_ALTERNATIVE_VOWELS = "EMAIL_ALTERNATIVE_VOWELS";

  // alternative beginnings
  public static final String FIRST_NAME_ALTERNATIVE_BEGINNINGS =
      "FIRST_NAME_ALTERNATIVE_BEGINNINGS";
  public static final String MIDDLE_NAME_ALTERNATIVE_BEGINNINGS =
      "MIDDLE_NAME_ALTERNATIVE_BEGINNINGS";
  public static final String LAST_NAME_ALTERNATIVE_BEGINNINGS = "LAST_NAME_ALTERNATIVE_BEGINNINGS";
  public static final String MOTHERS_MAIDEN_NAME_ALTERNATIVE_BEGINNINGS =
      "MOTHERS_MAIDEN_NAME_ALTERNATIVE_BEGINNINGS";
  public static final String MOTHERS_FIRST_NAME_ALTERNATIVE_BEGINNINGS =
      "MOTHERS_FIRST_NAME_ALTERNATIVE_BEGINNINGS";
  public static final String ADDRESS_STREET_ALTERNATIVE_BEGINNINGS =
      "ADDRESS_STREET_ALTERNATIVE_BEGINNINGS";
  public static final String ADDRESS_CITY_ALTERNATIVE_BEGINNINGS =
      "ADDRESS_CITY_ALTERNATIVE_BEGINNINGS";
  public static final String EMAIL_ALTERNATIVE_BEGINNINGS = "EMAIL_ALTERNATIVE_BEGINNINGS";

  // alternative endings
  public static final String FIRST_NAME_ALTERNATIVE_ENDINGS = "FIRST_NAME_ALTERNATIVE_ENDINGS";
  public static final String MIDDLE_NAME_ALTERNATIVE_ENDINGS = "MIDDLE_NAME_ALTERNATIVE_ENDINGS";
  public static final String LAST_NAME_ALTERNATIVE_ENDINGS = "LAST_NAME_ALTERNATIVE_ENDINGS";
  public static final String MOTHERS_MAIDEN_NAME_ALTERNATIVE_ENDINGS =
      "MOTHERS_MAIDEN_NAME_ALTERNATIVE_ENDINGS";
  public static final String MOTHERS_FIRST_NAME_ALTERNATIVE_ENDINGS =
      "MOTHERS_FIRST_NAME_ALTERNATIVE_ENDINGS";
  public static final String ADDRESS_STREET_ALTERNATIVE_ENDINGS =
      "ADDRESS_STREET_ALTERNATIVE_ENDINGS";
  public static final String ADDRESS_CITY_ALTERNATIVE_ENDINGS = "ADDRESS_CITY_ALTERNATIVE_ENDINGS";
  public static final String EMAIL_ALTERNATIVE_ENDINGS = "EMAIL_ALTERNATIVE_ENDINGS";

  // add variation
  public static final String FIRST_NAME_ADD_VARIATION = "FIRST_NAME_ADD_VARIATION";
  public static final String MIDDLE_NAME_ADD_VARIATION = "MIDDLE_NAME_ADD_VARIATION";
  public static final String LAST_NAME_ADD_VARIATION = "LAST_NAME_ADD_VARIATION";
  public static final String MOTHERS_MAIDEN_NAME_ADD_VARIATION =
      "MOTHERS_MAIDEN_NAME_ADD_VARIATION";
  public static final String MOTHERS_FIRST_NAME_ADD_VARIATION = "MOTHERS_FIRST_NAME_ADD_VARIATION";
  public static final String ADDRESS_STREET_ADD_VARIATION = "ADDRESS_STREET_ADD_VARIATION";
  public static final String ADDRESS_CITY_ADD_VARIATION = "ADDRESS_CITY_ADD_VARIATION";
  public static final String EMAIL_ADD_VARIATION = "EMAIL_ADD_VARIATION";

  // typo
  public static final String FIRST_NAME_TYPO = "FIRST_NAME_TYPO";
  public static final String MIDDLE_NAME_TYPO = "MIDDLE_NAME_TYPO";
  public static final String LAST_NAME_TYPO = "LAST_NAME_TYPO";
  public static final String MOTHERS_MAIDEN_NAME_TYPO = "MOTHERS_MAIDEN_NAME_TYPO";
  public static final String MOTHERS_FIRST_NAME_TYPO = "MOTHERS_FIRST_NAME_TYPO";
  public static final String ADDRESS_STREET_TYPO = "ADDRESS_STREET_TYPO";
  public static final String ADDRESS_CITY_TYPO = "ADDRESS_CITY_TYPO";
  public static final String PHONE_TYPO = "PHONE_TYPO";
  public static final String EMAIL_TYPO = "EMAIL_TYPO";

  // transpose
  public static final String FIRST_NAME_TRANSPOSE = "FIRST_NAME_TRANSPOSE";
  public static final String MIDDLE_NAME_TRANSPOSE = "MIDDLE_NAME_TRANSPOSE";
  public static final String LAST_NAME_TRANSPOSE = "LAST_NAME_TRANSPOSE";
  public static final String MOTHERS_MAIDEN_NAME_TRANSPOSE = "MOTHERS_MAIDEN_NAME_TRANSPOSE";
  public static final String MOTHERS_FIRST_NAME_TRANSPOSE = "MOTHERS_FIRST_NAME_TRANSPOSE";
  public static final String ADDRESS_STREET_TRANSPOSE = "ADDRESS_STREET_TRANSPOSE";
  public static final String ADDRESS_CITY_TRANSPOSE = "ADDRESS_CITY_TRANSPOSE";
  public static final String PHONE_TRANSPOSE = "PHONE_TRANSPOSE";
  public static final String EMAIL_TRANSPOSE = "EMAIL_TRANSPOSE";

  // letter to number
  public static final String FIRST_NAME_LETTER_TO_NUMBER = "FIRST_NAME_LETTER_TO_NUMBER";
  public static final String MIDDLE_NAME_LETTER_TO_NUMBER = "MIDDLE_NAME_LETTER_TO_NUMBER";
  public static final String LAST_NAME_LETTER_TO_NUMBER = "LAST_NAME_LETTER_TO_NUMBER";
  public static final String MOTHERS_MAIDEN_NAME_LETTER_TO_NUMBER =
      "MOTHERS_MAIDEN_NAME_LETTER_TO_NUMBER";
  public static final String MOTHERS_FIRST_NAME_LETTER_TO_NUMBER =
      "MOTHERS_FIRST_NAME_LETTER_TO_NUMBER";
  public static final String ADDRESS_STREET_LETTER_TO_NUMBER = "ADDRESS_STREET_LETTER_TO_NUMBER";
  public static final String ADDRESS_CITY_LETTER_TO_NUMBER = "ADDRESS_CITY_LETTER_TO_NUMBER";
  public static final String PHONE_LETTER_TO_NUMBER = "PHONE_LETTER_TO_NUMBER";
  public static final String EMAIL_LETTER_TO_NUMBER = "EMAIL_LETTER_TO_NUMBER";

  // number to letter
  public static final String FIRST_NAME_NUMBER_TO_LETTER = "FIRST_NAME_NUMBER_TO_LETTER";
  public static final String MIDDLE_NAME_NUMBER_TO_LETTER = "MIDDLE_NAME_NUMBER_TO_LETTER";
  public static final String LAST_NAME_NUMBER_TO_LETTER = "LAST_NAME_NUMBER_TO_LETTER";
  public static final String MOTHERS_MAIDEN_NAME_NUMBER_TO_LETTER =
      "MOTHERS_MAIDEN_NAME_NUMBER_TO_LETTER";
  public static final String MOTHERS_FIRST_NAME_NUMBER_TO_LETTER =
      "MOTHERS_FIRST_NAME_NUMBER_TO_LETTER";
  public static final String ADDRESS_STREET_NUMBER_TO_LETTER = "ADDRESS_STREET_NUMBER_TO_LETTER";
  public static final String ADDRESS_CITY_NUMBER_TO_LETTER = "ADDRESS_CITY_NUMBER_TO_LETTER";
  public static final String PHONE_NUMBER_TO_LETTER = "PHONE_NUMBER_TO_LETTER";
  public static final String EMAIL_NUMBER_TO_LETTER = "EMAIL_NUMBER_TO_LETTER";

  // change
  public static final String FIRST_NAME_CHANGE = "FIRST_NAME_CHANGE";
  public static final String MIDDLE_NAME_CHANGE = "MIDDLE_NAME_CHANGE";
  public static final String LAST_NAME_CHANGE = "LAST_NAME_CHANGE";
  public static final String MOTHERS_MAIDEN_NAME_CHANGE = "MOTHERS_MAIDEN_NAME_CHANGE";
  public static final String MOTHERS_FIRST_NAME_CHANGE = "MOTHERS_FIRST_NAME_CHANGE";
  public static final String ADDRESS_STREET_CHANGE = "ADDRESS_STREET_CHANGE";
  public static final String ADDRESS_CITY_CHANGE = "ADDRESS_CITY_CHANGE";
  public static final String EMAIL_CHANGE = "EMAIL_CHANGE";
  public static final String PHONE_CHANGE = "PHONE_CHANGE";

  // repeated consonants
  public static final String FIRST_NAME_REPEATED_CONSONANTS = "FIRST_NAME_REPEATED_CONSONANTS";
  public static final String MIDDLE_NAME_REPEATED_CONSONANTS = "MIDDLE_NAME_REPEATED_CONSONANTS";
  public static final String LAST_NAME_REPEATED_CONSONANTS = "LAST_NAME_REPEATED_CONSONANTS";
  public static final String MOTHERS_MAIDEN_NAME_REPEATED_CONSONANTS =
      "MOTHERS_MAIDEN_NAME_REPEATED_CONSONANTS";
  public static final String MOTHERS_FIRST_NAME_REPEATED_CONSONANTS =
      "MOTHERS_FIRST_NAME_REPEATED_CONSONANTS";
  public static final String ADDRESS_STREET_REPEATED_CONSONANTS =
      "ADDRESS_STREET_REPEATED_CONSONANTS";
  public static final String ADDRESS_CITY_REPEATED_CONSONANTS = "ADDRESS_CITY_REPEATED_CONSONANTS";
  public static final String EMAIL_REPEATED_CONSONANTS = "EMAIL_REPEATED_CONSONANTS";

  // hyphen variation
  public static final String FIRST_NAME_HYPHENATE_VARIATION = "FIRST_NAME_HYPHENATE_VARIATION";
  public static final String MIDDLE_NAME_HYPHENATE_VARIATION = "MIDDLE_NAME_HYPHENATE_VARIATION";
  public static final String LAST_NAME_HYPHENATE_VARIATION = "LAST_NAME_HYPHENATE_VARIATION";
  public static final String MOTHERS_MAIDEN_NAME_HYPHENATE_VARIATION =
      "MOTHERS_MAIDEN_NAME_HYPHENATE_VARIATION";
  public static final String MOTHERS_FIRST_NAME_HYPHENATE_VARIATION =
      "MOTHERS_FIRST_NAME_HYPHENATE_VARIATION";
  public static final String ADDRESS_STREET_HYPHENATE_VARIATION =
      "ADDRESS_STREET_HYPHENATE_VARIATION";
  public static final String ADDRESS_CITY_HYPHENATE_VARIATION = "ADDRESS_CITY_HYPHENATE_VARIATION";
  public static final String EMAIL_HYPHENATE_VARIATION = "EMAIL_HYPHENATE_VARIATION";

  // remove aira suffix
  public static final String FIRST_NAME_REMOVE_AIRA_SUFFIX = "FIRST_NAME_REMOVE_AIRA_SUFFIX";
  public static final String LAST_NAME_REMOVE_AIRA_SUFFIX = "LAST_NAME_REMOVE_AIRA_SUFFIX";
  public static final String MOTHERS_MAIDEN_NAME_REMOVE_AIRA_SUFFIX =
      "MOTHERS_MAIDEN_NAME_REMOVE_AIRA_SUFFIX";

  // birthday
  public static final String DATE_OF_BIRTH_DAY_SHIFT = "DATE_OF_BIRTH_DAY_SHIFT";
  public static final String DATE_OF_BIRTH_MONTH_DAY_SWAP = "DATE_OF_BIRTH_MONTH_DAY_SWAP";
  public static final String DATE_OF_BIRTH_MONTH_SHIFT = "DATE_OF_BIRTH_MONTH_SHIFT";
  public static final String DATE_OF_BIRTH_RECTIFY = "DATE_OF_BIRTH_RECTIFY";
  public static final String DATE_OF_BIRTH_YEAR_SHIFT = "DATE_OF_BIRTH_YEAR_SHIFT";

  // obx
  public static final String ADD_FUNDING_ELGIBILITY_TO_ALL_RXA =
      "ADD_FUNDING_ELGIBILITY_TO_ALL_RXA";
  public static final String ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_ADMINISTERED_RXA =
      "ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_ADMINISTERED_RXA";
  public static final String ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_RXA =
      "ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_RXA";
  public static final String ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_ADMINISTERED_RXA =
      "ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_ADMINISTERED_RXA";
  public static final String ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_RXA =
      "ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_RXA";

  // misc
  public static final String ADMINISTRATIVE_SEX_VARIATION = "ADMINISTRATIVE_SEX_VARIATION";
  public static final String ANONYMIZE_AND_UPDATE_RECORD = "ANONYMIZE_AND_UPDATE_RECORD";
  public static final String FIRST_NAME_CONVERT_TO_NICKNAME = "FIRST_NAME_CONVERT_TO_NICKNAME";
  public static final String LAST_NAME_HYPHENATE_OR_SWAP = "LAST_NAME_HYPHENATE_OR_SWAP";
  public static final String LAST_NAME_PREFIX_VARIATION = "LAST_NAME_PREFIX_VARIATION";
  public static final String MIDDLE_NAME_IN_FIRST_NAME_VARIATION =
      "MIDDLE_NAME_IN_FIRST_NAME_VARIATION";
  public static final String POPULATE_QUERY_FROM_UPDATE = "POPULATE_QUERY_FROM_UPDATE";
  public static final String REMOVE_VACCINATION_GROUPS = "REMOVE_VACCINATION_GROUPS";
  public static final String SUFFIX_VARIATION = "SUFFIX_VARIATION";

  private ProcedureFactory() {}

  public static ProcedureInterface getProcedure(String procedureName, Transformer transformer) {
    if (StringUtils.isBlank(procedureName)) {
      return null;
    }

    ProcedureInterface procedureInterface = switchOnName(procedureName);

    if (procedureInterface != null) {
      procedureInterface.setTransformer(transformer);
    }

    return procedureInterface;
  }

  private static ProcedureInterface switchOnName(String procedureName) {
    switch (procedureName.toUpperCase()) {

      // alternative vowels
      case FIRST_NAME_ALTERNATIVE_VOWELS:
        return new AlternativeVowels(AlternativeVowels.Field.FIRST_NAME);
      case MIDDLE_NAME_ALTERNATIVE_VOWELS:
        return new AlternativeVowels(AlternativeVowels.Field.MIDDLE_NAME);
      case LAST_NAME_ALTERNATIVE_VOWELS:
        return new AlternativeVowels(AlternativeVowels.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_ALTERNATIVE_VOWELS:
        return new AlternativeVowels(AlternativeVowels.Field.MOTHERS_MAIDEN_NAME);
      case MOTHERS_FIRST_NAME_ALTERNATIVE_VOWELS:
        return new AlternativeVowels(AlternativeVowels.Field.MOTHERS_FIRST_NAME);
      case ADDRESS_STREET_ALTERNATIVE_VOWELS:
        return new AlternativeVowels(AlternativeVowels.Field.ADDRESS_STREET);
      case ADDRESS_CITY_ALTERNATIVE_VOWELS:
        return new AlternativeVowels(AlternativeVowels.Field.ADDRESS_CITY);
      case EMAIL_ALTERNATIVE_VOWELS:
        return new AlternativeVowels(AlternativeVowels.Field.EMAIL);

      // alternative beginnings
      case FIRST_NAME_ALTERNATIVE_BEGINNINGS:
        return new AlternativeBeginnings(AlternativeBeginnings.Field.FIRST_NAME);
      case MIDDLE_NAME_ALTERNATIVE_BEGINNINGS:
        return new AlternativeBeginnings(AlternativeBeginnings.Field.MIDDLE_NAME);
      case LAST_NAME_ALTERNATIVE_BEGINNINGS:
        return new AlternativeBeginnings(AlternativeBeginnings.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_ALTERNATIVE_BEGINNINGS:
        return new AlternativeBeginnings(AlternativeBeginnings.Field.MOTHERS_MAIDEN_NAME);
      case MOTHERS_FIRST_NAME_ALTERNATIVE_BEGINNINGS:
        return new AlternativeBeginnings(AlternativeBeginnings.Field.MOTHERS_FIRST_NAME);
      case ADDRESS_STREET_ALTERNATIVE_BEGINNINGS:
        return new AlternativeBeginnings(AlternativeBeginnings.Field.ADDRESS_STREET);
      case ADDRESS_CITY_ALTERNATIVE_BEGINNINGS:
        return new AlternativeBeginnings(AlternativeBeginnings.Field.ADDRESS_CITY);
      case EMAIL_ALTERNATIVE_BEGINNINGS:
        return new AlternativeBeginnings(AlternativeBeginnings.Field.EMAIL);

      // alternative endings
      case FIRST_NAME_ALTERNATIVE_ENDINGS:
        return new AlternativeEndings(AlternativeEndings.Field.FIRST_NAME);
      case MIDDLE_NAME_ALTERNATIVE_ENDINGS:
        return new AlternativeEndings(AlternativeEndings.Field.MIDDLE_NAME);
      case LAST_NAME_ALTERNATIVE_ENDINGS:
        return new AlternativeEndings(AlternativeEndings.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_ALTERNATIVE_ENDINGS:
        return new AlternativeEndings(AlternativeEndings.Field.MOTHERS_MAIDEN_NAME);
      case MOTHERS_FIRST_NAME_ALTERNATIVE_ENDINGS:
        return new AlternativeEndings(AlternativeEndings.Field.MOTHERS_FIRST_NAME);
      case ADDRESS_STREET_ALTERNATIVE_ENDINGS:
        return new AlternativeEndings(AlternativeEndings.Field.ADDRESS_STREET);
      case ADDRESS_CITY_ALTERNATIVE_ENDINGS:
        return new AlternativeEndings(AlternativeEndings.Field.ADDRESS_CITY);
      case EMAIL_ALTERNATIVE_ENDINGS:
        return new AlternativeEndings(AlternativeEndings.Field.EMAIL);

      // add variation
      case FIRST_NAME_ADD_VARIATION:
        return new AddVariation(AddVariation.Field.FIRST_NAME);
      case MIDDLE_NAME_ADD_VARIATION:
        return new AddVariation(AddVariation.Field.MIDDLE_NAME);
      case LAST_NAME_ADD_VARIATION:
        return new AddVariation(AddVariation.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_ADD_VARIATION:
        return new AddVariation(AddVariation.Field.MOTHERS_MAIDEN_NAME);
      case MOTHERS_FIRST_NAME_ADD_VARIATION:
        return new AddVariation(AddVariation.Field.MOTHERS_FIRST_NAME);
      case ADDRESS_STREET_ADD_VARIATION:
        return new AddVariation(AddVariation.Field.ADDRESS_STREET);
      case ADDRESS_CITY_ADD_VARIATION:
        return new AddVariation(AddVariation.Field.ADDRESS_CITY);
      case EMAIL_ADD_VARIATION:
        return new AddVariation(AddVariation.Field.EMAIL);

      // typo
      case FIRST_NAME_TYPO:
        return new TextTypo(AbstractTypoProcedure.Field.FIRST_NAME);
      case MIDDLE_NAME_TYPO:
        return new TextTypo(AbstractTypoProcedure.Field.MIDDLE_NAME);
      case LAST_NAME_TYPO:
        return new TextTypo(AbstractTypoProcedure.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_TYPO:
        return new TextTypo(AbstractTypoProcedure.Field.MOTHERS_MAIDEN_NAME);
      case MOTHERS_FIRST_NAME_TYPO:
        return new TextTypo(AbstractTypoProcedure.Field.MOTHERS_FIRST_NAME);
      case ADDRESS_STREET_TYPO:
        return new TextTypo(AbstractTypoProcedure.Field.ADDRESS_STREET);
      case ADDRESS_CITY_TYPO:
        return new TextTypo(AbstractTypoProcedure.Field.ADDRESS_CITY);
      case EMAIL_TYPO:
        return new TextTypo(AbstractTypoProcedure.Field.EMAIL);
      case PHONE_TYPO:
        return new TextTypo(AbstractTypoProcedure.Field.PHONE);

      // transpose
      case FIRST_NAME_TRANSPOSE:
        return new TextTranspose(AbstractTypoProcedure.Field.FIRST_NAME);
      case MIDDLE_NAME_TRANSPOSE:
        return new TextTranspose(AbstractTypoProcedure.Field.MIDDLE_NAME);
      case LAST_NAME_TRANSPOSE:
        return new TextTranspose(AbstractTypoProcedure.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_TRANSPOSE:
        return new TextTranspose(AbstractTypoProcedure.Field.MOTHERS_MAIDEN_NAME);
      case MOTHERS_FIRST_NAME_TRANSPOSE:
        return new TextTranspose(AbstractTypoProcedure.Field.MOTHERS_FIRST_NAME);
      case ADDRESS_STREET_TRANSPOSE:
        return new TextTranspose(AbstractTypoProcedure.Field.ADDRESS_STREET);
      case ADDRESS_CITY_TRANSPOSE:
        return new TextTranspose(AbstractTypoProcedure.Field.ADDRESS_CITY);
      case EMAIL_TRANSPOSE:
        return new TextTranspose(AbstractTypoProcedure.Field.EMAIL);
      case PHONE_TRANSPOSE:
        return new TextTranspose(AbstractTypoProcedure.Field.PHONE);

      // letter to number
      case FIRST_NAME_LETTER_TO_NUMBER:
        return new TextLetterToNumber(AbstractTypoProcedure.Field.FIRST_NAME);
      case MIDDLE_NAME_LETTER_TO_NUMBER:
        return new TextLetterToNumber(AbstractTypoProcedure.Field.MIDDLE_NAME);
      case LAST_NAME_LETTER_TO_NUMBER:
        return new TextLetterToNumber(AbstractTypoProcedure.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_LETTER_TO_NUMBER:
        return new TextLetterToNumber(AbstractTypoProcedure.Field.MOTHERS_MAIDEN_NAME);
      case MOTHERS_FIRST_NAME_LETTER_TO_NUMBER:
        return new TextLetterToNumber(AbstractTypoProcedure.Field.MOTHERS_FIRST_NAME);
      case ADDRESS_STREET_LETTER_TO_NUMBER:
        return new TextLetterToNumber(AbstractTypoProcedure.Field.ADDRESS_STREET);
      case ADDRESS_CITY_LETTER_TO_NUMBER:
        return new TextLetterToNumber(AbstractTypoProcedure.Field.ADDRESS_CITY);
      case EMAIL_LETTER_TO_NUMBER:
        return new TextLetterToNumber(AbstractTypoProcedure.Field.EMAIL);
      case PHONE_LETTER_TO_NUMBER:
        return new TextLetterToNumber(AbstractTypoProcedure.Field.PHONE);

      // number to letter
      case FIRST_NAME_NUMBER_TO_LETTER:
        return new TextNumberToLetter(AbstractTypoProcedure.Field.FIRST_NAME);
      case MIDDLE_NAME_NUMBER_TO_LETTER:
        return new TextNumberToLetter(AbstractTypoProcedure.Field.MIDDLE_NAME);
      case LAST_NAME_NUMBER_TO_LETTER:
        return new TextNumberToLetter(AbstractTypoProcedure.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_NUMBER_TO_LETTER:
        return new TextNumberToLetter(AbstractTypoProcedure.Field.MOTHERS_MAIDEN_NAME);
      case MOTHERS_FIRST_NAME_NUMBER_TO_LETTER:
        return new TextNumberToLetter(AbstractTypoProcedure.Field.MOTHERS_FIRST_NAME);
      case ADDRESS_STREET_NUMBER_TO_LETTER:
        return new TextNumberToLetter(AbstractTypoProcedure.Field.ADDRESS_STREET);
      case ADDRESS_CITY_NUMBER_TO_LETTER:
        return new TextNumberToLetter(AbstractTypoProcedure.Field.ADDRESS_CITY);
      case EMAIL_NUMBER_TO_LETTER:
        return new TextNumberToLetter(AbstractTypoProcedure.Field.EMAIL);
      case PHONE_NUMBER_TO_LETTER:
        return new TextNumberToLetter(AbstractTypoProcedure.Field.PHONE);


      // change
      case FIRST_NAME_CHANGE:
        return new TextChange(TextChange.Field.FIRST_NAME);
      case MIDDLE_NAME_CHANGE:
        return new TextChange(TextChange.Field.MIDDLE_NAME);
      case LAST_NAME_CHANGE:
        return new TextChange(TextChange.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_CHANGE:
        return new TextChange(TextChange.Field.MOTHERS_MAIDEN_NAME);
      case MOTHERS_FIRST_NAME_CHANGE:
        return new TextChange(TextChange.Field.MOTHERS_FIRST_NAME);
      case ADDRESS_STREET_CHANGE:
        return new AddressStreetVariation();
      case ADDRESS_CITY_CHANGE:
        return new TextChange(TextChange.Field.ADDRESS_CITY);
      case EMAIL_CHANGE:
        return new TextChange(TextChange.Field.EMAIL);
      case PHONE_CHANGE:
        return new TextChange(TextChange.Field.PHONE);

      // repeated consonants
      case FIRST_NAME_REPEATED_CONSONANTS:
        return new RepeatedConsonants(RepeatedConsonants.Field.FIRST_NAME);
      case MIDDLE_NAME_REPEATED_CONSONANTS:
        return new RepeatedConsonants(RepeatedConsonants.Field.MIDDLE_NAME);
      case LAST_NAME_REPEATED_CONSONANTS:
        return new RepeatedConsonants(RepeatedConsonants.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_REPEATED_CONSONANTS:
        return new RepeatedConsonants(RepeatedConsonants.Field.MOTHERS_MAIDEN_NAME);
      case MOTHERS_FIRST_NAME_REPEATED_CONSONANTS:
        return new RepeatedConsonants(RepeatedConsonants.Field.MOTHERS_FIRST_NAME);
      case ADDRESS_STREET_REPEATED_CONSONANTS:
        return new RepeatedConsonants(RepeatedConsonants.Field.ADDRESS_STREET);
      case ADDRESS_CITY_REPEATED_CONSONANTS:
        return new RepeatedConsonants(RepeatedConsonants.Field.ADDRESS_CITY);
      case EMAIL_REPEATED_CONSONANTS:
        return new RepeatedConsonants(RepeatedConsonants.Field.EMAIL);

      // hyphen variation
      case FIRST_NAME_HYPHENATE_VARIATION:
        return new HyphenateVariation(HyphenateVariation.Field.FIRST_NAME);
      case MIDDLE_NAME_HYPHENATE_VARIATION:
        return new HyphenateVariation(HyphenateVariation.Field.MIDDLE_NAME);
      case LAST_NAME_HYPHENATE_VARIATION:
        return new HyphenateVariation(HyphenateVariation.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_HYPHENATE_VARIATION:
        return new HyphenateVariation(HyphenateVariation.Field.MOTHERS_MAIDEN_NAME);
      case MOTHERS_FIRST_NAME_HYPHENATE_VARIATION:
        return new HyphenateVariation(HyphenateVariation.Field.MOTHERS_FIRST_NAME);
      case ADDRESS_STREET_HYPHENATE_VARIATION:
        return new HyphenateVariation(HyphenateVariation.Field.ADDRESS_STREET);
      case ADDRESS_CITY_HYPHENATE_VARIATION:
        return new HyphenateVariation(HyphenateVariation.Field.ADDRESS_CITY);
      case EMAIL_HYPHENATE_VARIATION:
        return new HyphenateVariation(HyphenateVariation.Field.EMAIL);

      // remove aira suffix
      case FIRST_NAME_REMOVE_AIRA_SUFFIX:
        return new RemoveAiraSuffix(RemoveAiraSuffix.Field.FIRST_NAME);
      case LAST_NAME_REMOVE_AIRA_SUFFIX:
        return new RemoveAiraSuffix(RemoveAiraSuffix.Field.LAST_NAME);
      case MOTHERS_MAIDEN_NAME_REMOVE_AIRA_SUFFIX:
        return new RemoveAiraSuffix(RemoveAiraSuffix.Field.MOTHERS_MAIDEN_NAME);

      // birthday
      case DATE_OF_BIRTH_MONTH_DAY_SWAP:
        return new DateOfBirthMonthDaySwap();
      case DATE_OF_BIRTH_RECTIFY:
        return new DateOfBirthRectify();
      case DATE_OF_BIRTH_MONTH_SHIFT:
        return new DateOfBirthMonthShift();
      case DATE_OF_BIRTH_YEAR_SHIFT:
        return new DateOfBirthYearShift();
      case DATE_OF_BIRTH_DAY_SHIFT:
        return new DateOfBirthDayShift();

      // obx
      case ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_RXA:
        return new AddFundingToRxa(AddFundingToRxa.Type.ELIGIBILITY,
            AddFundingToRxa.VaccinationGroups.ALL);
      case ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_RXA:
        return new AddFundingToRxa(AddFundingToRxa.Type.SOURCE,
            AddFundingToRxa.VaccinationGroups.ALL);
      case ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_ADMINISTERED_RXA:
        return new AddFundingToRxa(AddFundingToRxa.Type.SOURCE,
            AddFundingToRxa.VaccinationGroups.ADMINISTERED_ONLY);
      case ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_ADMINISTERED_RXA:
        return new AddFundingToRxa(AddFundingToRxa.Type.ELIGIBILITY,
            AddFundingToRxa.VaccinationGroups.ADMINISTERED_ONLY);

      // misc
      case REMOVE_VACCINATION_GROUPS:
        return new RemoveVaccinationGroupsProcedure();

      case ADD_FUNDING_ELGIBILITY_TO_ALL_RXA:
        return new AddFundingEligibilityToAllRxa();

      case ANONYMIZE_AND_UPDATE_RECORD:
        return new AnonymizeAndUpdateRecord();

      case FIRST_NAME_CONVERT_TO_NICKNAME:
        return new FirstNameConvertNickname();

      case LAST_NAME_HYPHENATE_OR_SWAP:
        return new LastNameHyphenateOrSwap();

      case LAST_NAME_PREFIX_VARIATION:
        return new LastNamePrefixVariation();

      case MIDDLE_NAME_IN_FIRST_NAME_VARIATION:
        return new MiddleNameInFirstNameVariation();

      case SUFFIX_VARIATION:
        return new SuffixVariation();

      case ADMINISTRATIVE_SEX_VARIATION:
        return new AdministrativeSexVariation();

      case POPULATE_QUERY_FROM_UPDATE:
        return new PopulateQueryFromUpdate();

      default:
        return null;
    }
  }
}
