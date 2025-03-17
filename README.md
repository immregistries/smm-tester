# HART
*HL7-ART is not an Aggregate Reporting Tool, rather, a library for sending and receiving HL7 messages.*

## Installation Instructions

### Software Needed

#### 1. Java JDK

Ensure that you have Java SE (or OpenJDK) version 8.0 (also displayed as 1.8) or later installed:

- To confirm the version of Java installed on your system, follow these steps:
  + Open a command window (in Windows click Start >> Run and type `cmd`, press enter)
  + Type `java -version` on the command-line
  + Verify the version number displayed (remember that 1.6 indicates Java version 6.0, and 1.7 is Java 7.0)

## Class outline

|Class|Purpose|
|---|---|
|AbstractSuffixVariation|For working with patient name suffixes|
|AbstractTypoProcedure|Handle repeat Fields (eg. phone or email)|
|AckAnalyzer|Determine IIS response Ack type and unmarshal|
|AddFundingEligibilityToAllRxa|"adds funding eligibility to all rxa" (pharmacy administration) segments|
|AddFundingToRxa|adds funding eligibility to a single RXA segment|
|AddressStreetVariation|allows for variation of street/address in a transform|
|AddVariation|capitalizes/shuffles all fields (in field list) in a transform|
|AdministrativeSexVariation|rotates admin sex through M->F->M U->F or U if none in a transform|
|AffirmationMessage|Data class for affirmation messages|
|AlternativeBeginnings|Name field manipulation in transforms|
|AlternativeEndings|Name field manipulation in transforms|
|AlternativeEndingVowels|Name field manipulation in transforms|
|AlternativeSpellings|Name field manipulation in transforms|
|AlternativeVowels|Name field manipulation in transforms|
|AnonymizeAndUpdateRecord|remove PII from a transform's fields|
|Assertion|Data container used only by nist/ValidationReport|
|CDCWSDLServer|For printing CDC WSDL XML and processing CDC XML messages|
|Certify|Loads data from `certify.txt` on `init()`, manipulation of `CertifyItem` data class|
|CertifyItem|Scoped inside `Certify` class to hold `code`s, `label`s, and `tables`|
|ClientConnection|Data class used in all connector classes|
|Client_Service|Auto-generated tlep connector for messaging|
|Client_ServiceCallbackHandler|Auto-generated tlep connector message handler|
|Comment|Data class within `TestCaseMessage`|
|CompareManager|For comparing IIS messages|
|Comparison|Used in `CompareManager` for message prioritization and execution attributes|
|ConnectivityTest|Auto-generated tlep connector connectivity tester|
|ConnectivityTestRequestType|Auto-generated tlep connector connectivity tester request types|
|ConnectivityTestResponse|Auto-generated tlep connector connectivity tester response handler|
|ConnectivityTestResponseType|Auto-generated tlep connector connectivity tester response types|
|Connector|Abstract class for IIS connections & message handling|
|ConnectorFactory|@Deprecated Manufactures `Connector`s (Just use `new SoapConnector(...)`)|
|DateOfBirthDayShift||
|DateOfBirthMonthDaySwap||
|DateOfBirthMonthShift||
|DateOfBirthRectify||
|DateOfBirthYearShift||
|EvaluationActual|Data class used in `ForecastTesterManager` and `TestCaseMessage` (why "Actual"?)|
|ExtensionMapper||
|Factory||
|Fault||
|FaultDetail||
|FilteredKeyManager||
|FindMatchingSegment||
|FindMatchingSegmentTest||
|FirstNameConvertNickname||
|FirstNameConvertNicknameTest||
|ForecastActual||
|ForecastExpected||
|ForecastTestCase||
|ForecastTesterManager||
|ForecastTestEvent||
|GenerateRandomPatientData||
|HeaderReport||
|HL7||
|HL7Reader||
|HttpConnector||
|HttpRawConnector||
|HyphenateVariation||
|HyphenateVariationTest||
|ImmunizationUpdate||
|InstallCert||
|IssueCreator||
|IssueCreatorHeader||
|IssueCreatorNextOfKin||
|IssueCreatorObservation||
|IssueCreatorPatient||
|IssueCreatorVaccination||
|KeyPairCreator||
|LastNameHyphenateOrSwap||
|LastNameHyphenateOrSwapTest||
|LastNamePrefixVariation||
|LastNamePrefixVariationTest||
|MessageGenerationV2SoapClient||
|MessageTooLargeFault||
|MessageTooLargeFault_Message||
|MessageTooLargeFaultType||
|MessageValidationV2SoapClient||
|MiddleNameInFirstNameVariation||
|MiddleNameInFirstNameVariationTest||
|MinuteStat||
|PasswordEncryptUtil||
|Patient||
|PopulateQueryFromUpdate||
|PopulateQueryFromUpdateTest||
|Prefix||
|ProcedureCommon||
|ProcedureCommonTest||
|ProcedureFactory||
|Processor||
|ProcessorAdditionalTag||
|ProcessorBadXml||
|ProcessorBase64||
|ProcessorFactory||
|ProcessorIncorrectImplementation||
|ProcessorMistakes||
|ProcessorNoWhitespace||
|ProcessorUrlEncoded||
|QueryConverter||
|QueryConverterQBPZ34||
|QueryConverterQBPZ34Z44||
|QueryConverterQBPZ44||
|QueryConverterVXQ||
|QueryRequest||
|QueryResponse||
|RemoveAiraSuffix||
|RemoveAiraSuffixTest||
|RemoveLastNamePrefix||
|RemoveLastNamePrefixTest||
|RemoveVaccinationGroupsProcedure||
|RemoveVaccinationGroupsProcedureTest||
|RepeatedConsonants||
|RepeatedConsonantsTest||
|ResponseReader||
|SavingTrustManager||
|ScenarioManager||
|SecurityFault||
|SecurityFault_Message||
|SecurityFaultType||
|SendViaCDCWsdl||
|SimpleSoapConnector||
|SoapConnector||
|SoapFaultType||
|SpecialCharacters||
|SpecialCharactersExtended||
|SpecialCharactersExtendedTest||
|SpecialCharactersTest||
|SubmitSingleMessage||
|SubmitSingleMessageRequestType||
|SubmitSingleMessageResponse||
|SubmitSingleMessageResponseType||
|SuffixVariation||
|SuffixVariationNumbers||
|SuffixVariationNumbersTest||
|SuffixVariationPeriods||
|SuffixVariationPeriodsTest||
|SuffixVariationTest||
|TenIISConverter||
|TestAckAnalyzer||
|TestAffirmationGeneration||
|TestCaseMessage||
|TestError||
|TestHL7Reader||
|TestRunner||
|TestRunnerTest||
|TextChange||
|TextChangeTest||
|TextLetterToNumberTypo||
|TextLetterToNumberTypoTest||
|TextNumberToLetterTypo||
|TextNumberToLetterTypoTest||
|TextRandomTypo||
|TextRandomTypoTest||
|TextTransposeTypo||
|TextTransposeTypoTest||
|TextTypo||
|TextTypoTest||
|Transform||
|Transformer||
|TransformerCopySegmentTest||
|TransformerTest||
|TransformRequest||
|Triplet||
|UnknownFault||
|UnknownFault_Message||
|UnsupportedOperationFault||
|UnsupportedOperationFault_Message||
|UnsupportedOperationFaultType||
|UsiisLogReader||
|Vaccination||
|ValidationReport||
