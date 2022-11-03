package org.immregistries.smm.transform.procedure;

import org.immregistries.smm.transform.Transformer;

public class ProcedureFactory {
  public static final String REMOVE_VACCINATION_GROUPS = "REMOVE_VACCINATION_GROUPS";
  public static final String ADD_FUNDING_ELGIBILITY_TO_ALL_RXA =
      "ADD_FUNDING_ELGIBILITY_TO_ALL_RXA";

  public static final String ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_RXA =
      "ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_RXA";
  public static final String ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_ADMINISTERED_RXA =
      "ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_ADMINISTERED_RXA";
  public static final String ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_RXA =
      "ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_RXA";
  public static final String ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_ADMINISTERED_RXA =
      "ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_ADMINISTERED_RXA";

  public static final String ANONYMIZE_AND_UPDATE_RECORD = "ANONYMIZE_AND_UPDATE_RECORD";

  public static final String FIRST_NAME_REMOVE_AIRA_SUFFIX = "FIRST_NAME_REMOVE_AIRA_SUFFIX";
  public static final String LAST_NAME_REMOVE_AIRA_SUFFIX = "LAST_NAME_REMOVE_AIRA_SUFFIX";
  public static final String FIRST_NAME_CONVERT_TO_NICKNAME = "FIRST_NAME_CONVERT_TO_NICKNAME";
  public static final String FIRST_NAME_ADD_VARIATION = "FIRST_NAME_ADD_VARIATION";
  public static final String LAST_NAME_HYPHENATE_OR_SWAP = "LAST_NAME_HYPHENATE_OR_SWAP";
  public static final String LAST_NAME_HYPHENATE_VARIATION = "LAST_NAME_HYPHENATE_VARIATION";
  public static final String LAST_NAME_PREFIX_VARIATION = "LAST_NAME_PREFIX_VARIATION";
  public static final String FIRST_NAME_ALTERNATIVE_VOWELS = "FIRST_NAME_ALTERNATIVE_VOWELS";
  public static final String FIRST_NAME_REPEATED_CONSONANTS = "FIRST_NAME_REPEATED_CONSONANTS";
  public static final String FIRST_NAME_ALTERNATIVE_BEGINNINGS =
      "FIRST_NAME_ALTERNATIVE_BEGINNINGS";
  public static final String FIRST_NAME_ALTERNATIVE_ENDINGS = "FIRST_NAME_ALTERNATIVE_ENDINGS";
  public static final String MIDDLE_NAME_IN_FIRST_NAME_VARIATION =
      "MIDDLE_NAME_IN_FIRST_NAME_VARIATION";
  public static final String SUFFIX_VARIATION = "SUFFIX_VARIATION";
  public static final String LAST_NAME_TYPOS = "LAST_NAME_TYPOS";
  public static final String FIRST_NAME_TYPOS = "FIRST_NAME_TYPOS";
  public static final String ADDRESS_STREET_CHANGE = "ADDRESS_STREET_CHANGE";
  public static final String ADDRESS_CITY_TYPO = "ADDRESS_CITY_TYPO";
  public static final String DATE_OF_BIRTH_MONTH_DAY_SWAP =
      "DATE_OF_BIRTH_MONTH_DAY_SWAP";
  public static final String DATE_OF_BIRTH_RECTIFY = "DATE_OF_BIRTH_RECTIFY";
  public static final String DATE_OF_BIRTH_MONTH_SHIFT = "DATE_OF_BIRTH_MONTH_SHIFT";
  public static final String DATE_OF_BIRTH_YEAR_SHIFT = "DATE_OF_BIRTH_YEAR_SHIFT";
  public static final String DATE_OF_BIRTH_DAY_SHIFT = "DATE_OF_BIRTH_DAY_SHIFT";
  public static final String ADMINISTRATIVE_SEX_VARIATION = "ADMINISTRATIVE_SEX_VARIATION";
  public static final String EMAIL_CHANGE = "EMAIL_CHANGE";
  public static final String EMAIL_TYPO = "EMAIL_TYPO";
  public static final String PHONE_CHANGE = "PHONE_CHANGE";
  public static final String PHONE_TYPO = "PHONE_TYPO";

  public static ProcedureInterface getProcedure(String procedureName, Transformer transformer) {
    ProcedureInterface procedureInterface = null;
    if (procedureName.equalsIgnoreCase(REMOVE_VACCINATION_GROUPS)) {
      procedureInterface = new RemoveVaccinationGroupsProcedure();
    } else if (procedureName.equalsIgnoreCase(ADD_FUNDING_ELGIBILITY_TO_ALL_RXA)) {
      procedureInterface = new AddFundingEligibilityToAllRxa();
    } else if (procedureName.equalsIgnoreCase(ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_RXA)) {
      procedureInterface = new AddFundingToRxa(AddFundingToRxa.Type.ELIGIBILITY,
          AddFundingToRxa.VaccinationGroups.ALL);
    } else if (procedureName.equalsIgnoreCase(ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_RXA)) {
      procedureInterface =
          new AddFundingToRxa(AddFundingToRxa.Type.SOURCE, AddFundingToRxa.VaccinationGroups.ALL);
    } else if (procedureName.equalsIgnoreCase(ADD_OBX_FOR_FUNDING_SOURCE_TO_ALL_ADMINISTERED_RXA)) {
      procedureInterface = new AddFundingToRxa(AddFundingToRxa.Type.SOURCE,
          AddFundingToRxa.VaccinationGroups.ADMINISTERED_ONLY);
    } else if (procedureName
        .equalsIgnoreCase(ADD_OBX_FOR_FUNDING_ELIGIBILITY_TO_ALL_ADMINISTERED_RXA)) {
      procedureInterface = new AddFundingToRxa(AddFundingToRxa.Type.ELIGIBILITY,
          AddFundingToRxa.VaccinationGroups.ADMINISTERED_ONLY);
    } else if (procedureName.equalsIgnoreCase(ANONYMIZE_AND_UPDATE_RECORD)) {
      procedureInterface = new AnonymizeAndUpdateRecord();
    } else if (procedureName.equalsIgnoreCase(FIRST_NAME_REMOVE_AIRA_SUFFIX)) {
      procedureInterface = new RemoveAiraSuffix(RemoveAiraSuffix.Field.FIRST_NAME);
    } else if (procedureName.equalsIgnoreCase(LAST_NAME_REMOVE_AIRA_SUFFIX)) {
      procedureInterface = new RemoveAiraSuffix(RemoveAiraSuffix.Field.LAST_NAME);
    } else if (procedureName.equalsIgnoreCase(FIRST_NAME_CONVERT_TO_NICKNAME)) {
      procedureInterface = new FirstNameConvertNickname();
    } else if (procedureName.equalsIgnoreCase(FIRST_NAME_ADD_VARIATION)) {
      procedureInterface = new FirstNameAddVariation();
    } else if (procedureName.equalsIgnoreCase(LAST_NAME_HYPHENATE_OR_SWAP)) {
      procedureInterface = new LastNameHyphenateOrSwap();
    } else if (procedureName.equalsIgnoreCase(LAST_NAME_HYPHENATE_VARIATION)) {
      procedureInterface = new LastNameHyphenateVariation();
    } else if (procedureName.equalsIgnoreCase(LAST_NAME_PREFIX_VARIATION)) {
      procedureInterface = new LastNamePrefixVariation();
    } else if (procedureName.equalsIgnoreCase(FIRST_NAME_ALTERNATIVE_VOWELS)) {
      procedureInterface = new FirstNameAlternativeVowels();  
    } else if (procedureName.equalsIgnoreCase(FIRST_NAME_REPEATED_CONSONANTS)) {
      procedureInterface = new FirstNameRepeatedConsonants();  
    } else if (procedureName.equalsIgnoreCase(FIRST_NAME_ALTERNATIVE_BEGINNINGS)) {
      procedureInterface = new FirstNameAlternativeBeginnings();
    } else if (procedureName.equalsIgnoreCase(FIRST_NAME_ALTERNATIVE_ENDINGS)) {
      procedureInterface = new FirstNameAlternativeEndings();
    } else if (procedureName.equalsIgnoreCase(MIDDLE_NAME_IN_FIRST_NAME_VARIATION)) {
      procedureInterface = new MiddleNameInFirstNameVariation();
    } else if (procedureName.equalsIgnoreCase(SUFFIX_VARIATION)) {
      procedureInterface = new SuffixVariation();
    } else if (procedureName.equalsIgnoreCase(LAST_NAME_TYPOS)) {
      procedureInterface = new NameTypo(NameTypo.Field.LAST_NAME);
    } else if (procedureName.equalsIgnoreCase(FIRST_NAME_TYPOS)) {
      procedureInterface = new NameTypo(NameTypo.Field.FIRST_NAME);
    } else if (procedureName.equalsIgnoreCase(ADDRESS_STREET_CHANGE)) {
      
    } else if (procedureName.equalsIgnoreCase(ADDRESS_CITY_TYPO)) {
      procedureInterface = new NameTypo(NameTypo.Field.CITY);
    } else if (procedureName.equalsIgnoreCase(DATE_OF_BIRTH_MONTH_DAY_SWAP)) {
      procedureInterface = new DateOfBirthMonthDaySwap();
    } else if (procedureName.equalsIgnoreCase(DATE_OF_BIRTH_RECTIFY)) {
      procedureInterface = new DateOfBirthRectify();
    } else if (procedureName.equalsIgnoreCase(DATE_OF_BIRTH_MONTH_SHIFT)) {
      procedureInterface = new DateOfBirthMonthShift();
    } else if (procedureName.equalsIgnoreCase(DATE_OF_BIRTH_YEAR_SHIFT)) {
      procedureInterface = new DateOfBirthYearShift();
    } else if (procedureName.equalsIgnoreCase(DATE_OF_BIRTH_DAY_SHIFT)) {
      procedureInterface = new DateOfBirthDayShift();
    } else if (procedureName.equalsIgnoreCase(ADMINISTRATIVE_SEX_VARIATION)) {
    } else if (procedureName.equalsIgnoreCase(EMAIL_CHANGE)) {
    } else if (procedureName.equalsIgnoreCase(EMAIL_TYPO)) {
      
    } else if (procedureName.equalsIgnoreCase(PHONE_CHANGE)) {
    } else if (procedureName.equalsIgnoreCase(PHONE_TYPO)) {
    }


    if (procedureInterface != null) {
      procedureInterface.setTransformer(transformer);
    }
    return procedureInterface;
  }
}
