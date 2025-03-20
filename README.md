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

>note: data classes are classes with only fields and possibly setters & getters

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
|DateOfBirth|Contains HL7 format DOB string manipulation methods|
|DateOfBirthDayShift|`ProcedureCommon`: Decrements day in a HL7 format DOB|
|DateOfBirthMonthDaySwap|`ProcedureCommon`: Swaps day and month in HL7 format DOB|
|DateOfBirthMonthShift|`ProcedureCommon`: Decrements month in a HL7 format DOB|
|DateOfBirthRectify|`ProcedureCommon`: fixes bad dates, so long as they're YYYYMMDD (HL7) format|
|DateOfBirthYearShift|`ProcedureCommon`: Decrements year in a HL7 format DOB|
|EvaluationActual|Data class used in `ForecastTesterManager` and `TestCaseMessage` (why "Actual"?)|
|ExtensionMapper|Auto-generated tlep connector class for mapping namespaceURI & typeNames to req/res handlers|
|Fault|CDC Fault data container|
|FaultDetail|CDC Fault message detail container|
|FilteredKeyManager|Filtered container for X509 trust store data/logic|
|FindMatchingSegment|Compares HL7 message segments between an `HL7Reader`'s "actual" response message and a `TestCaseMessage`|
|FirstNameConvertNickname|`ProcedureCommon`: Determines nickname by name via statically defined map and nicknames loaded from `/nicknames.csv`|
|ForecastActual|Extends `ForecastExpected`: Data class for forecasts|
|ForecastExpected|Data class for forecasts|
|ForecastTestCase|Data class for forecast test cases|
|ForecastTesterManager|Produce forecast list from `ForecastTestPanel` or report forecast results from HL7 `TestCaseMessage`|
|ForecastTestEvent|Data class for forecast events|
|HeaderReport|Data class for MSH (HL7 message headers)|
|HL7|Primarily defines static message type string constants. Has 2 methods: `readField` and `isFileBatchHeaderFooterSegment`|
|HL7Reader|For handling HL7 message text / segment manipulation|
|HttpConnector|extends `Connector`: HTTP/SSL authentication and data handling methods, XML stripping, etc.|
|HttpRawConnector|Minimal `HttpConnector`|
|HyphenateVariation|`ProcedureCommon`: Randomizes and/or shuffles field data, ensuring it's hyphenated|
|ImmunizationUpdate|`ImmunizationMessage` implementation|
|InstallCert|For finding and adding certificates within a java trust store|
|IssueCreator|Abstract class containing default implementation for `String` transform creation from a `TestCaseMessage`|
|IssueCreatorHeader|Extends `IssueCreator`: Adds MSH to transform text based on `Issue` and "`boolean not`"|
|IssueCreatorNextOfKin|Extends `IssueCreator`: Adds NK to transform text based on `Issue` and "`boolean not`"|
|IssueCreatorObservation|Extends `IssueCreator`: Adds OBX to transform text based on `Issue` and "`boolean not`"|
|IssueCreatorPatient|Extends `IssueCreator`: Adds PID to transform text based on `Issue` and "`boolean not`"|
|IssueCreatorVaccination|Extends `IssueCreator`: Adds RXA,RXR, or ORC to transform text based on `Issue` and "`boolean not`"|
|KeyPairCreator|Create and optionally write to disk key pairs (default 2048 bit RSA)|
|LastNameHyphenateOrSwap|`ProcedureCommon`: Hyphenates or swaps hyphenated field items, eg. John-Smith -> Smith-John|
|LastNamePrefixVariation|`ProcedureCommon`: Varies name prefixes between common name prefixes, eg. "Mac", "De", "Der" ...|
|MessageGenerationV2SoapClient|Generates SOAP messages and can check service status|
|MessageTooLargeFault|CDC fault variant|
|MessageTooLargeFault_Message|tlep "message too large" fault data class, extends `Exception`|
|MessageTooLargeFaultType|tlep "message too large" fault data class / parser, extends `Exception`|
|MessageValidationV2SoapClient|Validates SOAP messages and can check service status|
|MiddleNameInFirstNameVariation|`ProcedureCommon`: Separates middle from first name if first name includes it|
|MinuteStat|Private static class inside `UsiisLogReader`|
|PasswordEncryptUtil|Encrypts/decrypts password strings using DESede CBC with no padding and a hard-coded key and IV|
|Patient|HL7 PID data class|
|PopulateQueryFromUpdate|`ProcedureInterface`: Deserializes HL7 VXU/QBP message text into a `Transformer`|
|Prefix|Static scoped data class in `LastNamePrefixVariation` class for last name prefixes|
|ProcedureCommon|Abstract class with default implementations for generic HL7 field operations|
|ProcedureFactory|Gets a `ProcedureInterface` implementer based on a procedure name|
|Processor|Single-submit message (ssm) processing (essentially XML printing via a `PrintWriter` with embedded `CDCWSDLServer` output)|
|ProcessorAdditionalTag|Extends `Processor`, reimplementing everything for the scenario where an unexpected additional tag is present|
|ProcessorBadXml|Extends `Processor`, reimplementing everything for the scenario where malformed XML is provided|
|ProcessorBase64|Extends `Processor`, reimplementing minimally to handle base64 encoded data|
|ProcessorFactory|Produces `Processor`s via `String processorName` and `CDCWSDLServer`|
|ProcessorIncorrectImplementation|Extends `Processor`, reimplementing everything for the scenario correct XML is provided, but methods aren't implemented properly|
|ProcessorMistakes|Extends `Processor`, reimplementing everything for the scenario where correct XMl is provided, but bad tags|
|ProcessorNoWhitespace|Extends `Processor`, reimplementing everything for the scenario where correct XMl is provided but no whitespace|
|ProcessorUrlEncoded|Extends `Processor`, reimplementing minimally to handle valid XML but with URL encoded data|
|QueryConverter|Abstract class for switching QBP queries by type, eg. Z34, Z44, VXQ|
|QueryConverterQBPZ34|`QueryConverter` of message `String` to QBPZ34 formatted Headers (MSH) and Parameter Definitions (QPD)|
|QueryConverterQBPZ34Z44|`QueryConverter` of message `String` to QBPZ34 formatted Headers (MSH) and QBPZ44 Parameter Definitions (QPD)|
|QueryConverterQBPZ44|`QueryConverter` of message `String` to QBPZ44 formatted Headers (MSH) and Parameter Definitions (QPD)|
|QueryConverterVXQ|`QueryConverter` of message `String` to VXQ format|
|QueryRequest|Data class that extends `Patient` - only adds setter/getter for `QueryType`|
|QueryResponse|Data class that extends `ImmunizationUpdate` adding get/set `List<Patient>` and get/set `QueryResponseType`|
|RemoveAiraSuffix|`ProcedureCommon`: Removes the last 4 characters of a field|
|RemoveLastNamePrefix|`ProcedureCommon`: Removes common last name prefixes|
|RemoveVaccinationGroupsProcedure|`ProcedureInterface`: Removes vaccination groups "where RXA-20 equals 'RE'"|
|RepeatedConsonants|`ProcedureCommon`: Duplicates repeatable consonants in a name or email field|
|ResponseReader|Deserializes a `String` HL7 response message|
|SavingTrustManager|Nested private child class within `InstallCert`|
|ScenarioManager|Converts a `String` scenario to a test case message (also `String`)|
|SecurityFault|CDC fault dispatch for security faults specifically *OR* a tlep connector security fault|
|SecurityFault_Message|tlep connector security fault data class|
|SecurityFaultType|tlep connector security fault type data class / parser, extends `Exception`|
|SendViaCDCWsdl|Sends an `EXAMPLE_HL7` message to `florence.immregistries.org/iis-sandbox/soap`, specifically|
|SimpleSoapConnector|Opens a URL and sends XML HL7 message with userName, password, and flatWire (message body)|
|SoapConnector|Extends `HttpConnector` for SOAP messaging via tlep message submission|
|SoapFaultType|tlep connector for fault types|
|SpecialCharacters|`ProcedureCommon`: Toggle diacritics in fields|
|SpecialCharactersExtended|Extends `SpecialCharacters`, adding more diacritics|
|SubmitSingleMessage|Data class for SSM|
|SubmitSingleMessageRequestType|tlep connector class for SSM request type data and parsing SSM request types|
|SubmitSingleMessageResponse|tlep connector class for SSM response data and parsing|
|SubmitSingleMessageResponseType|tlep connector class for SSM response type data and parsing SSM response types|
|SuffixVariation|Extends `AbstractSuffixVariation` adding `String[] SUFFIXES` and `getSuffixes`|
|SuffixVariationNumbers|Extends `AbstractSuffixVariation` adding `String[] SUFFIXES` (numbers like 2nd, 3rd) and `getSuffixes`|
|SuffixVariationPeriods|Extends `AbstractSuffixVariation` adding `String[] SUFFIXES` (with periods, eg "Jr.") and `getSuffixes`|
|TenIISConverter|Inserts CRLF given specific message headers by line then returns the message count|
|TestCaseMessage|Comprehensive HL7 test case message class with some "kitchen sink" methods|
|TestError|Transform test error data class|
|TestRunner|Comprehensive HL7 test runner, validator, and response handler|
|TextChange|`ProcedureCommon`: Randomizes field values|
|TextLetterToNumberTypo|Extends `AbstractTypoProcedure` and replaces a letter with one or more numbers as a "typo"|
|TextNumberToLetterTypo|Extends `AbstractTypoProcedure` and replaces a number with one or more letters as a "typo"|
|TextRandomTypo|`ProcedureCommon`: applies randomly shuffled list of `Typo`, `TransposeTypo`, `LetterToNumber`, and `NumberToLetter` typos|
|TextTransposeTypo|Extends `AbstractTypoProcedure` and swaps 2 random differing characters within a field|
|TextTypo|Extends `AbstractTypoProcedure` and randomly replaces a letter with several different letters|
|Transform|Data class for HL7 transforms|
|Transformer|Comprehensive class for transforming HL7 messages and performing operations on fields|
|TransformRequest|Data class for transform requests|
|UnknownFault|CDC fault dispatch for Unknown faults specifically|
|UnknownFault_Message|tlep connector unknown fault data class|
|UnsupportedOperationFault|CDC fault dispatch for unsupported operation faults specifically *OR* a tlep connector unsupported operation fault|
|UnsupportedOperationFault_Message|tlep connector unknown fault data class|
|UnsupportedOperationFaultType|Auto-generated tlep connector type dispatch for unsupported operation faults|
|UsiisLogReader|Log file reader for US IIS's (not sure if strictly required outside of AART)|
|Vaccination|Data class for vaccinations|
|ValidationReport|Deserializes vaccination report XML|
