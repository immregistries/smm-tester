# HL7 v2 Validation

SMM can check HL7 v2.5.1 immunization messages against the CDC *Immunization Messaging IG Release 1.5* conformance profiles. It does this through the public API of the [Valitheus Immunization Test Suite Edition 2](https://tools.valitheus.com/immunization-edition2/#/), which Prometheus Computing maintains.

This is a general capability, not part of the school roster work. It's offered in three ways:

| Where | Who uses it | Sign-in |
| --- | --- | --- |
| **Tools → Validate Message** (`/ValidateMessageServlet`) | People using SMM | Required (InteropHub) |
| **REST API** (`/api/validate`, `/api/profiles`, `/api/openapi.json`) | Scripts and ChatGPT custom GPT actions | None; rate limited |
| **MCP server** (`/mcp`) | AI assistants: Claude, ChatGPT, Claude Code, Codex, Gemini CLI, … | None; rate limited |

Valitheus stores a report of every validation, so only synthetic test data should be sent. The page, the API description, and the MCP tool descriptions all say this.

## Profiles

Valitheus publishes these eight profiles. Each message's profile is detected from MSH-21.1 unless the caller chooses one.

| Key | MSH-21.1 | Message type | Purpose |
| --- | --- | --- | --- |
| `vxu-z22` | Z22 | VXU^V04^VXU_V04 | Send unsolicited immunization update |
| `ack-z23` | Z23 | ACK^V04^ACK | Acknowledgement of an update |
| `qbp-z34` | Z34 | QBP^Q11^QBP_Q11 | Request complete immunization history |
| `qbp-z44` | Z44 | QBP^Q11^QBP_Q11 | Request evaluated history and forecast |
| `rsp-z31` | Z31 | RSP^K11^RSP_K11 | List of candidate patients |
| `rsp-z32` | Z32 | RSP^K11^RSP_K11 | Complete immunization history |
| `rsp-z33` | Z33 | RSP^K11^RSP_K11 | No person records (none found or too many) |
| `rsp-z42` | Z42 | RSP^K11^RSP_K11 | Evaluated history and forecast |

## Code

**`smm-core`, package `org.immregistries.smm.validation`**

| Class | Role |
| --- | --- |
| `ValidatorSource`, `ValidationProfile` | A validation service and its profiles. `ValidatorSource.VALITHEUS_IZ_EDITION2` is the only source today. |
| `HL7Validator`, `ValitheusClient` | Validate one message against one profile. `ValitheusClient` calls the Valitheus REST API with `java.net.http`. If a call fails, it looks the profile's test context id up again from the site's published test plans (in case the site was republished) and retries once. |
| `ValidationService` | Splits text into messages (any line endings; FHS/BHS/BTS/FTS dropped), chooses each message's profile from MSH-21, and validates each one. |
| `ValidationResult`, `ValidationFinding`, `MessageValidation` | Results. Errors are listed first. *Routine* findings (empty RE elements, value sets the validator does not check, informational notes) are counted rather than listed. |
| `RateLimiter` | Per-client and overall limits in one-minute windows. |

**`smm-web`, package `org.immregistries.smm.web.validation`**

| Class | Role |
| --- | --- |
| `ValidationGateway` | The one way into validation from the web app. Checks size and rate limits before anything is sent to Valitheus. |
| `ValidateMessageServlet` | The signed-in page. |
| `ValidationApiServlet`, `OpenApiDocument` | The REST API and its OpenAPI 3.1 description. |
| `McpServlet`, `McpHandler` | The MCP server: Streamable HTTP transport, stateless (no sessions, no server-sent events), JSON-RPC handled by hand. Tools: `validate_hl7_message` and `list_validation_profiles`. |
| `PublicApiFilter` | On `/api/*` and `/mcp`: per-client request limit, request size limit, CORS. `AuthenticationFilter` lets these paths through without sign-in. |
| `ValidationJson`, `ApiResponses` | JSON and text output shared by the API and MCP. |

Jackson (`jackson-databind`) is the one new dependency. It reads the Valitheus reports, which are large and nested, and writes the API's JSON.

### Adding a validator source

When Prometheus publishes another validator, such as school roster profiles at a different URL:

1. Define another `ValidatorSource` with its own profile keys.
2. If it uses the same Valitheus API, create a `ValitheusClient` for it. Otherwise, write another `HL7Validator`.
3. Add the validator to the list in `ValidationService.getDefault()`. When two profiles share an MSH-21 value, the earlier source wins, so order matters. Callers can always choose a profile by key.

The page, API, OpenAPI document, and MCP tools all build their profile lists from the service, so they pick up the new profiles without changes.

## Limits

These are deployment settings, read like the other `SmmWebConfig` settings: system property, then environment variable (`SMM_VALIDATION_MAX_MESSAGES`, …), then `web.xml`.

| Setting | Default | Meaning |
| --- | --- | --- |
| `smm.api.requests.per.minute` | 60 | Requests one client may make to `/api` and `/mcp` |
| `smm.validation.messages.per.minute` | 30 | Messages one client (or signed-in user) may validate |
| `smm.validation.messages.total.per.minute` | 300 | Messages all clients together may validate; this protects Valitheus |
| `smm.validation.max.messages` | 20 | Messages in one request |
| `smm.validation.max.bytes` | 262144 | Characters of HL7 in one request |
| `smm.trust.forwarded.for` | false | Set to `true` when SMM is behind a reverse proxy that sets `X-Forwarded-For`. Otherwise every caller appears to have the proxy's address and shares one allowance. |

Responses: `413` for too much text or too many messages, `429` with `Retry-After` when a limit is reached, `502` when Valitheus cannot be reached.

## Using the API

```bash
# HL7 text as the body; the profile comes from MSH-21
curl -X POST -H 'Content-Type: text/plain' --data-binary @message.hl7 \
  https://informatics.immregistries.org/smm/api/validate

# JSON body, choosing the profile
curl -X POST -H 'Content-Type: application/json' \
  -d '{"message": "MSH|^~\\&|...", "profile": "vxu-z22", "includeAllFindings": false}' \
  https://informatics.immregistries.org/smm/api/validate
```

The response has `pass` (every message passed), a `summary`, and one entry in `messages` per message. Each entry has a `status` (`PASS`, `FAIL`, or `NOT_VALIDATED` with a `problem`), `counts`, the errors and other notable `findings` (line, element path, description), and counts of `routineFindings`.

## Connecting AI assistants

- **ChatGPT custom GPT:** in the GPT editor, choose *Create new action → Import from URL* and enter `https://informatics.immregistries.org/smm/api/openapi.json`. Set Authentication to *None*. In the GPT's instructions, tell it to validate every HL7 message it writes and to fix only the errors that are reported.
- **ChatGPT (developer mode connectors) and Claude.ai (custom connectors):** add a connector with the URL `https://informatics.immregistries.org/smm/mcp` and no authentication.
- **Claude Code:** `claude mcp add --transport http smm-validation https://informatics.immregistries.org/smm/mcp`
- **Codex, Gemini CLI, and other MCP clients:** configure a Streamable HTTP (remote) MCP server at the same URL.

## Testing

Unit tests use a captured Valitheus report (`smm-core/src/test/resources/validation/`) and a fake validator, so they never call Valitheus. The branch was also checked against a local Tomcat:

- the page, API, and MCP server returned the expected results for the Indiana roster examples
- the MCP server worked with the official MCP TypeScript SDK client
- each limit returned `413` or `429` as expected
