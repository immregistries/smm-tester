# SMM/Tester Modernization Plan

This document collects the parts of the *Indiana School Immunization Interoperability Technical Plan* (working decisions as of September 29, 2026) that apply to SMM/Tester. That plan is the source of truth. This file is the SMM-focused working copy, and we will update it as decisions are made.

The work happens on the `modernize` branch. `master` stays unchanged until `modernize` is ready to replace it.

## Why SMM is involved

Indiana schools can already send rosters to CHIRP (Indiana's IIS) through manual or flat-file processes, but those are slow and leave long gaps between enrollment updates. The project defines a small HL7 v2 extension for **school, grade, and Indiana DOE student ID**, plus the usual patient-identifying fields CHIRP needs for matching. It also demonstrates sending that information over HL7.

SMM is where the implementation is built. It already handles IIS endpoint configuration, message transmission, response handling, and diagnostic files. The project adds a school roster layer on top of those features, and the modernized SMM web application is itself the demonstration website.

## Starting point

- Latest release: `v3.1.0` (Feb 2025). `master` has 8 unreleased Dependabot bumps after it, including CXF 3.5.5 → 4.1.1.
- Single Maven module, `org.immregistries:smm-tester`, packaged as a WAR. It compiles for Java 1.8. `modernize` is now `4.0.0-SNAPSHOT`.
- The build also produces a `client` classified JAR. AART used to consume it but moved to `hart` in 2025.
- Pushing any tag runs `.github/workflows/maven-publish.yml`, which deploys to Maven Central. **Do not push tags from `modernize`** until publishing for the new modules is set up on purpose.
- Main code is in `org.immregistries.smm` (`mover`, `tester`, `transform`, `install`, `cdc`), plus generated SOAP/web-service client code under `com.microsoft`, `faultcontracts`, `gov.nist`, and `servicecontracts`.

## Workstreams

### 1. Preserve the stable line ✅ (branches created)

- `maintenance/3.x` keeps the current code so existing users can stay on it.
- AART now consumes `hart` (from the `hart` branch), not the SMM client JAR. EHR-Sandbox uses `smm-tester:2.31.0:client`.

### 2. Java 17 and the core/web split

- Move the new line to **Java 17**.
- Split the build into a **reusable core JAR** and a **web application**.
- The split must **preserve existing core behavior**. It should make an eventual AART migration possible but must not require one. AART can stay on the current client JAR.
- Keep these existing capabilities: IIS endpoint configuration, message transmission, response handling, diagnostic files, and the other useful functions SMM already has.

### 3. Replace authentication and add authorization

- **Remove** SMM's old local username/password mechanism from the new line. Do not keep it as an alternate sign-on option.
- Integrate **InteropHub sign-on** using [InteropHub-Client](https://github.com/immregistries/InteropHub-Client).
- After sign-on, the application creates **its own session** and checks access to each workspace and operation.
- Put authentication behind an **application boundary**, so that a later Indiana-hosted deployment can use a different credential method (not chosen yet; to be decided with Indiana).

### 4. Web interface on AIRA Web

- Adopt the shared [AIRA Web](https://github.com/immregistries/aira-web) theme and Java servlet components.
- Provide:
  - manual file upload
  - configuration matched to each tester's permissions
  - a way to follow submissions and inspect their results
- **Workspace model**: each tester acts as a school or district and normally sees only the workflows and data in their own workspace.

### 5. School roster layer

This layer is built around SMM's existing HL7 workflow:

1. **Accept** a flat-file roster through **SFTP** or **manual upload** in the web application.
2. **Validate and map** the records, including the preloaded mapping to school identifiers.
3. **Generate HL7 v2** messages that follow the new guidance, and **send** them through SMM's existing connection mechanism.
4. **Keep** the generated HL7 and the IIS responses so testers and developers can inspect them.
5. **Show school-readable results**: file and row problems, transmission outcomes, and the IIS response, without making the school tester read HL7.

- Roster input and readable results get their own folders, or equivalent managed views, next to SMM's existing send and receive folders.
- Not designed yet: file naming, retry behavior, status correlation, and the exact result format.
- **A successful HL7 ACK must not be shown as proof that CHIRP stored the school data.**

### 6. Demonstration deployment

- An isolated deployment that uses **synthetic data only**. Identifiable student information is prohibited. Real school export *formats* can guide the mapping, but every row must be synthetic.
- The usual destination is the updated **IIS Sandbox**. IIS endpoints stay configurable, so authorized STChealth developers can point a suitable test setup at their own endpoint.
- Define who is allowed to change destinations in the shared environment.

### Related work outside this repo

These pieces belong to the same overall demonstration but are not SMM code:

- a school-side demonstrator that produces and submits representative roster files
- **IIS Sandbox** updates to receive the new messages and show the intended IIS behavior
- the HL7 v2 guidance itself (being decided by the technical small group)

## Work that can begin now (SMM items)

- [x] Preserve the stable branch.
- [x] Confirm the artifact AART currently consumes. It's `org.immregistries:hart:0.3.3`, built from this repo's `hart` branch, not the SMM client JAR. See [java17-core-web-split.md](java17-core-web-split.md).
- [x] Plan the Java 17 upgrade and the core/web build split ([java17-core-web-split.md](java17-core-web-split.md)).
- [ ] Remove the old web authentication and set up an application-level session/authorization boundary.
- [ ] Integrate AIRA Web and InteropHub sign-on.
- [ ] Set up an isolated synthetic-data demonstration deployment and the user workspace model.
- [ ] Prototype file submission and processing around SMM's existing HL7 folders, connections, and response handling.
- [ ] Define a representative synthetic roster file and draft the school-readable result format.
- [ ] Prepare initial HL7 examples (for refinement by the technical small group).

None of these depend on the final HL7 field mapping, Indiana's credential method, or CHIRP's implementation.

## Open decisions affecting SMM

| Item | To decide or verify |
| --- | --- |
| HL7 v2 representation | Exact segments and fields, what "school" means and how it is identified, the identifier type and assigning authority for the DOE ID, grade coding, and handling of repeated or corrected submissions. Earlier options included PID/OBX and a VXU with a no-vaccination placeholder, but nothing is final. |
| School flat-file contract | Demographics CHIRP needs for matching, supported source formats and mappings, school identifier mapping, file naming, partial errors, retries, and the readable result format. |
| SMM modules | Exact boundary and artifact names for the core and web builds; API and dependency compatibility for AART's eventual upgrade. |
| Tester workspaces | How SFTP credentials map to InteropHub users and workspaces; whether a district-level tester can manage several schools; how long synthetic files are kept and how they are cleaned up. |
| Authorization and endpoints | Who may upload, view HL7, administer workspaces, or change IIS endpoints (especially when STChealth tests against its own system). |
| Indiana authentication | A replacement for InteropHub sign-on in an Indiana-hosted deployment. This is decided later and must not bring back the old credentials. |
| Demonstration boundaries | Criteria for telling apart generation, delivery, acknowledgment, and actual application of the school data. |

## Boundaries for SMM

- No school year in the initial workflow; this covers the current enrollment cycle only.
- No full enrollment history, roster lifecycle model, exemption exchange, or extra school data elements.
- No identifiable student records in the AIRA deployment, and no claim that it is ready for production school data.
- No forced migration of AART to Java 17 or the new core JAR.
- No keeping the old SMM username/password system as an alternate sign-on.
- No CHIRP production integration by AIRA. STChealth controls any CHIRP HL7 intake.
- No FERPA/HIPAA policy decisions about which students a school may export.
