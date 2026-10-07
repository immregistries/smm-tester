# Java 17 and Core/Web Split — Plan

Working plan for workstream 2 in [modernization-plan.md](modernization-plan.md). Everything under "Findings" was verified on the `modernize` branch on 2026-10-07 (version `4.0.0-SNAPSHOT`).

## Findings

### The build already runs on JDK 17

- With JDK 17 (and the current `source/target 1.8`), the project compiles and packages: WAR, `client` JAR, `jar-with-dependencies`, sources, and javadoc. Javadoc reports 20 errors, but `failOnError=false` keeps the build going.
- Switching the compiler to `<release>17</release>` changes nothing: it compiles and gives the same test results.
- CI (`pull-request.yml`, `maven-publish.yml`) already uses JDK 11. `cxf-rt-frontend-jaxws` 4.x requires at least Java 11, so the Java 8 target only appears in the pom now.
- The servlet code already uses `jakarta.servlet` (servlet API 6.1), so there's no javax-to-jakarta migration for the web layer.

### One test fails, and it is a date bug, not a Java problem

`TestTransformer.testNMSIIS` fails on JDK 17, with or without `release 17`, so the failure already exists on `master`. The transform rule `remove observation 64994-7 if 18+` computes age from today's date. The fixture patient `TEST_NMSIIS_CHILD_1` was born 2007-12-02 and turned 18 on 2025-12-02, so the transform now removes the observation the test expects to keep. The test has to be fixed so it no longer depends on the current date. Results: 223 tests, 1 failure.

### Dependency problems

- ~~**CXF versions are mixed.**~~ Resolved: CXF was used only by the NIST validator, and both have been removed (see "NIST validator" below).
- ~~**Axis2 and related libraries.**~~ Resolved: they were used only by the Axis2-generated connector stubs. Those connectors were removed and `SoapConnector` was rewritten (see "Connectors" below). Axis2, Axiom, Neethi, jakarta.activation, jakarta.xml.bind-api, commons-logging, and httpclient5 are gone. The WAR now bundles only commons-codec, commons-lang3, commons-text, and json.
- **Obsolete build pieces:**
  - Jetty 8 (`jetty-all-server` provided dependency and `jetty-maven-plugin`)
  - `maven-eclipse-plugin`
  - `lib.zip` in the repo root (17 MB of 2015-era JARs that Maven never uses)
  - source encoding `iso-8859-1`
- **Publishing.** `maven-publish.yml` deploys to `s01.oss.sonatype.org` (OSSRH) through `nexus-staging-maven-plugin`. Sonatype retired OSSRH in favor of the Central Portal, so this path probably no longer works. Verify before the first 4.x release.

### Who consumes SMM as a library

- **AART no longer uses SMM.** `aart-measure` (`develop`) depends on `org.immregistries:hart:0.3.3`. In March 2025 the `hart` branch of this repo stripped SMM down to an AART-only library and renamed its package to `org.immregistries.hart`. No AART source references `org.immregistries.smm`. AART's migration path is therefore HART, not the SMM client JAR.
- **EHR-Sandbox** (`EHR-Sandbox-Api/pom.xml`, last pushed 2026-05-14) depends on `smm-tester:2.31.0:client`. It is the only other consumer found in the `immregistries` org, and it is pinned to an old version.
- So no known consumer depends on the 3.x client JAR's API. The core JAR can keep the `org.immregistries.smm` packages for continuity, but doesn't need to match the old client JAR exactly.

### The code divides cleanly

Before the removals there were 417 main classes: 32 servlets (`jakarta.servlet` imports) and 385 others. Only 13 non-servlet classes referenced a servlet class. With `CertifyClient` gone, four edges remain:

| Non-servlet class | Depends on | Why | Fix |
| --- | --- | --- | --- |
| `cdc.Processor*` (9 classes) | `cdc.CDCWSDLServer` | Processors that simulate CDC WSDL server behavior | Keep the whole `cdc` package in **web**. It is the WSDL test server feature. |
| `mover.install.ConnectionConfiguration` | `ConfigureServlet` | Only the string `"ConfigureServlet"` in generated HTML | No compile dependency. Move the HTML rendering to web, or leave it as is. |
| `tester.Authenticate` | `HomeServlet`, `LoginServlet` | Legacy login state | Goes to **web** now, and is replaced by the new auth boundary (workstream 3). |
| `mover.ConnectionManager` | `tester.Authenticate` | `Authenticate.setupAdminUser(...)` when reading config | Remove the call from core. Admin setup moves to the web layer and later to the new auth. |
| `tester.query.QueryRunner` | `CreateTestCaseServlet.IIS_TEST_REPORT_FILENAME_PREFIX` | A constant | Move the constant to core. |

## Proposed module layout

```
smm-tester/                  (parent pom, packaging=pom)
├── smm-core/                (jar)  org.immregistries:smm-core
│     transform/**, tester/connectors/**, tester/manager/**, tester/transform,
│     tester/run, tester/query, mover (engine, not servlets),
│     mover/install/templates + ConnectionConfiguration, org.immregistries.smm root
└── smm-web/                 (war)  org.immregistries:smm-web
      all 32 servlets, cdc/**, tester/Authenticate (until replaced),
      src/main/webapp, web.xml
```

- Use `git mv` so file history survives the move.
- Tests move with the code they cover. Nearly all current tests cover core code.
- Later, the school roster layer adds its processing logic to core and its upload/results UI to web.

## Steps

Each step is its own commit, and `mvn test` must pass after each one (step 0 makes that possible).

0. **Fix the date-dependent test.** Make the `18+` rule testable against a fixed "today", or adjust the fixture DOBs. Prefer the first option so the test can't break again.
1. **Java 17 build:**
   - Set `<release>17</release>`.
   - Move CI workflows to JDK 17 and add `modernize` to the `pull-request` branch triggers.
   - Remove Jetty 8, `maven-eclipse-plugin`, and `lib.zip`.
2. **Dependency cleanup:**
   - ~~Remove the NIST validator and CXF.~~ Done.
   - ~~Remove the jurisdiction connectors, rewrite `SoapConnector`, and remove Axis2 and the unused dependencies.~~ Done.
   - Convert source encoding to UTF-8.
3. **Break the four remaining back-edges** listed above, still inside the single module.
4. **Restructure into parent + `smm-core` + `smm-web`.** `smm-core` replaces the `client` classifier JAR as the published library, and it should be the JAR AART can eventually move to.
5. **Smoke-test the WAR** on a current servlet-6 container (Tomcat 10.1+ or 11): start up, the mover manager, the CDC WSDL endpoint, and sending a test message to IIS Sandbox. Include a live `SoapConnector` connectivity test and submission. From this development machine `florence.immregistries.org` resolves to a private address and couldn't be reached, so run it from inside the network or against a local IIS Sandbox.
6. **Publishing:** move `maven-publish.yml` to the Sonatype Central Portal and publish only `smm-core` (the WAR is deployed, not consumed). Keep tag-triggered publishing off `modernize` until this is done.

Workstream 3 (removing `Authenticate`/`LoginServlet`, adding InteropHub sign-on) builds on step 4 and isn't part of this plan.

## NIST validator

**What it does:** it sends an HL7 message to NIST's HL7 v2 web-service validator (EVS, `SoftwareVersion.EVS_URL`), together with a conformance-profile OID. It then shows the returned assertions (errors, warnings, pass/fail).

- **Profiles** (`ValidationResource`): VXU (IG 1.4), VXU Z22, ACK Z23 / AIRA ACK, QBP Z34/Z44, RSP Z31/Z32/Z33/Z42 (IG 1.5). `TestRunner.ascertainValidationResource` picks one from MSH-9/MSH-21.
- **The one live entry point:** the **"Validate NIST" checkbox on the Submit page** (`SubmitServlet`). It validates both the sent message and the IIS response, and shows the results inline. `TestCaseMessageViewerServlet` also shows a validation report if a test case has one.
- **Dormant path:** `TestRunner.setValidateResponse(true)` would validate every test-run response, but nothing turns it on. `CertifyClient`, `TestConnect`, and `TestCovidReporting` all set it to `false`.
- `MessageGenerationV2SoapClient` (NIST message generation) is never called.

**The endpoint is gone.** On 2026-10-07, `hl7v2.ws.nist.gov` had no DNS address, so the feature can't work today, regardless of the CXF version problem.

**Removed 2026-10-07** ("the software has moved"; add it back deliberately if it's ever needed):
- `gov.nist.healthcare.hl7ws.**`
- `tester.manager.nist.**`
- `SoftwareVersion.EVS_URL`
- `TestRunner`'s `validateResponse` flag, `validateResponseWithNIST`, and `ascertainValidationResource`
- the validation fields on `TestCaseMessage`
- the checkbox and result rendering in `SubmitServlet` and `TestCaseMessageViewerServlet`
- the CXF and `javax.jws` (Geronimo ws-metadata) dependencies

## CertifyClient

**Removed 2026-10-07.** `CertifyClient` was a standalone command-line client (the `jar-with-dependencies` main class). It pulled certification test areas from AART URLs, ran them through SMM connections, and reported results back. Removed with it:
- `tester.CertifyClient`
- the `tester.certify` package (`CertifyRunner`, `CertifyArea`, `AartUrl`)
- `ConnectServlet.readNewConnection`
- the `maven-assembly-plugin` configuration

## Connectors

**Kept:** SOAP (`SoapConnector`, CDC WSDL `urn:cdc:iisb:2011`), POST (`HttpConnector`), MLLP, Raw (`HttpRawConnector`), and IZ Gateway.

**Removed 2026-10-07:**
- every jurisdiction-specific connector: AL, AZ, CA, CO, FL, HI, IL (WS and SOAP), KS, KY KHIE, MA (both), MO, ND, NJ, NM, OR, SC, WI, Envision/WebIZ
- `SimpleSoapConnector`
- the Axis2-generated stubs (`tlep`, `al`, `az`, `co`, `fl`, `hi`, `ks`, `nm`)
- the unreferenced Partner HIE stubs (`com.microsoft`, `faultcontracts`, `servicecontracts`)
- install templates that only worked with removed types: AL, AR, CA CAIR, CO, DE, IL, KS, KY, MA, MO, NJ, OR, PA PHIL, SC, WI

Together that is about 65k lines.

Template fixes made along the way:
- MD ImmuNet and ND NDIIS cast to removed state connectors even though their type was SOAP, so they would have failed. They now use `Connector`.
- AZ ASIIS keeps only its production template, which uses POST.

An existing `smm.config.txt` that names a removed type now falls back to POST (`Connector.addConnector`'s existing default).

**`SoapConnector` rewrite.** It now builds and parses the SOAP 1.2 envelope directly with JDK classes (`HttpURLConnection` and a hardened DOM parser), replacing the Axis2 client. It was checked against the bytes the Axis2 client sent, captured with a local server:
- **The same:** the envelope, `ns1` elements, `xsi:nil` for null fields, `&#13;` for carriage returns, the `action` content-type parameter, the 2-minute timeout, and URL-decoding of `MSH%7C` responses.
- **Differences:**
  - It sends `Content-Length` instead of chunked transfer encoding.
  - `>` is escaped as `&gt;`.
  - SOAP faults throw `SoapConnector.SoapFaultException` with the reason plus the CDC fault detail, for example `Security fault (SecurityFault: Bad password)`. Previously this was an `AxisFault` with only the reason.
  - HTTPS now uses the connection's keystore and the "disable server certificate check" setting, through `HttpConnector.setupSSLSocketFactory`. Axis2 ignored both.
- `SoapConnectorTest` covers the envelope, null fields, URL-encoded responses, the connectivity test, faults, and non-SOAP errors.

The IZ Gateway connector still builds its envelope with simpler string handling (no `<` escaping, a fixed `MessageID`, and `writeBytes`, which drops non-ASCII characters). It could later reuse the `SoapConnector` approach.

## Decisions

- 2026-10-07: Modules are named `smm-core` (JAR) and `smm-web` (WAR).
- 2026-10-07: Remove the NIST validator and its Submit-page checkbox (done).
- 2026-10-07: Remove `CertifyClient` and the runnable `jar-with-dependencies` (done).
- 2026-10-07: Keep only the SOAP, POST, MLLP, Raw, and IZ Gateway connectors, and rewrite `SoapConnector` without Axis2 (done).
- 2026-10-07: Keep a published library JAR (`smm-core`) as AART's future migration target.
- 2026-10-07: The legacy `Authenticate`/`LoginServlet` moves to `smm-web` temporarily and is removed when InteropHub sign-on replaces it (workstream 3).

## Open questions

- Should EHR-Sandbox be told about 4.x and `smm-core`, or left on 2.31.0?
- What happens to the `hart` branch? It is AART's real dependency source. Keep it as is, or move it to its own repo?
