# InteropHub Sign-On and Workspaces

Workstream 3 of [modernization-plan.md](modernization-plan.md). This first version was built and verified on 2026-10-07 against a local InteropHub (`http://localhost:8080/hub`).

## What changed

- **Removed:**
  - the legacy username/password login (`tester.Authenticate`, `tester.LoginServlet`) and its hard-coded users
  - the `admin.username` and `admin.password` init parameters
  - the empty `defaultConnections.txt`
- **Added:** InteropHub sign-on through [InteropHub-Client](https://github.com/immregistries/InteropHub-Client) 1.1.0.
- **Workspaces:** each signed-in user gets a file-based workspace (no database). A new user starts with an empty one.

## How sign-in works

```
Browser → any protected SMM page
  → AuthenticationFilter: no SMM session
  → redirect to InteropHub /hub/home?app_code=smm&return_to=<SMM>/login&state=…&requested_url=<page>
  → person signs in to InteropHub (or already is)
  → InteropHub redirects to <SMM>/login?code=…&state=…
  → SignInServlet exchanges the code with InteropHub for the person's identity
  → WorkspaceStore opens (or creates) the person's workspace
  → SMM session started → redirect to the originally requested page
```

Signing out (`/logout`) ends the SMM session and returns to InteropHub's home page.

## Code (`smm-web`, package `org.immregistries.smm.web`)

| Class | Role |
| --- | --- |
| `auth.AuthenticationProvider` | The authentication boundary: start a sign-in, complete it at `/login`, and give a sign-out URL. A future Indiana-hosted deployment can add another implementation without touching the servlets. |
| `auth.InteropHubAuthenticationProvider` | The InteropHub implementation. |
| `auth.AuthenticatedIdentity`, `auth.SignInResult` | The provider's results. |
| `auth.SmmUser` | The signed-in user: identity, workspace, admin flag, and (for admins) a selected folder-based IIS connection. Replaces `Authenticate.User`. |
| `auth.SmmSession` | Keeps the user in the HTTP session. It sets the `user` and `username` attributes the existing servlets check, and replaces the session at sign-in. |
| `auth.AuthenticationFilter` | Requires a signed-in user for everything except `/login`, `/logout`, the machine endpoints (`/CatchServlet`, `/wsdl*`, `/wsdl-demo*`), and static `.css`/`.png`/`.ico` files. A GET returns to the requested page after sign-in. A POST returns to the home page so a form isn't replayed. |
| `auth.SignInServlet` (`/login`), `auth.SignOutServlet` (`/logout`) | Sign-in callback and sign-out. |
| `SmmWebConfig` | Deployment settings (below). |

`smm-core` has the workspace classes: `workspace.WorkspaceStore` and `workspace.Workspace`.

## Configuration

Each setting is read from a Java system property (`-Dsmm.hub.url=…`), then an environment variable (`SMM_HUB_URL`), then the `context-param` in `web.xml`.

| Setting | Default | Meaning |
| --- | --- | --- |
| `smm.hub.url` | `http://localhost:8080/hub` | InteropHub base URL |
| `smm.app.code` | `smm` | Code SMM is registered under in InteropHub |
| `smm.external.url` | *(empty)* | SMM's public base URL. When empty it's derived from each request (scheme, host, port, context path). Set it behind a proxy or load balancer. |
| `smm.workspace.dir` | *(empty)* | Workspace directory. When empty: `<catalina.base>/smm/workspaces`, or `<user.home>/smm/workspaces` outside Tomcat. |
| `smm.admin.emails` | *(empty)* | Comma-separated emails of SMM administrators |

## InteropHub registration

The local InteropHub has SMM registered as app 4 (`/hub/admin/apps?appId=4`):

| Field | Value |
| --- | --- |
| App code | `smm` |
| App name | Simple Message Mover / Tester |
| Managed by | AIRA |
| Default redirect URL | `http://localhost:8080/smm/HomeServlet` |
| Allowed base URL | `http://localhost:8080/smm` (covers the `/login` callback) |
| Enabled / Visible | yes / no |

Another deployment needs its own allowed base URL, for example `https://<server>/smm`.

## Workspaces

- **Location:** one directory per user under the workspace directory, named from the email address. For example, `nbunker@immregistries.org` becomes `nbunker_at_immregistries.org`.
- **Contents:** each holds a `workspace.properties` file recording the owner's email, name, organization, and creation time.
- **First sign-in:** creates the directory and properties file. Later sign-ins reuse them.
- **Now:** a workspace is empty apart from that file.
- **Later:** the school roster layer (workstream 5) keeps uploaded files, generated HL7, IIS responses, and results here.

### Relation to the folder-based mover connections

SMM's message mover can also read IIS connections from local folders. These are set with `ManagerServlet` init parameters in `web.xml`:
- `scan.start.folders`: a `;`-separated list of folders whose subfolders hold an `smm.config.txt`
- `folderScanEnabled`: whether those folders are scanned

The legacy login let someone sign in as one of those connections. Now only users listed in `smm.admin.emails` can attach their session to one, on the Connect page. Workspaces don't depend on any of this.

## Dependency note

InteropHub-Client is not on Maven Central. Following StepIntoCDSI, a copy of 1.1.0 (with SHA-1 checksums) is committed in `repo/` and declared as the `smm-local` repository in the parent POM. A build with an empty Maven cache resolves it from there. Publishing InteropHub-Client to Maven Central would remove the need for `repo/`; that is tracked in [InteropHub-Client#1](https://github.com/immregistries/InteropHub-Client/issues/1).

The client logs through SLF4J. `slf4j-jdk14` sends those messages to Tomcat's log.

## Verified on 2026-10-07 (local Tomcat 10.1.53 with InteropHub)

- An anonymous request to a protected page redirects to InteropHub with `app_code=smm`, `return_to=…/smm/login`, and the requested URL.
- Signed in to InteropHub as `nbunker@immregistries.org`:
  - The redirect returns to `/smm/login?code=…`, the code exchange succeeds, and the browser lands on the originally requested page (Send Message).
  - The header shows "Nathan Bunker".
- The workspace was created at `C:\Program Files\Apache Software Foundation\Tomcat 10.1\smm\workspaces\nbunker_at_immregistries.org` with only `workspace.properties`.
- `/logout` returns to InteropHub, and protected pages require sign-in again.
- `/wsdl`, `/wsdl-demo`, `/CatchServlet`, and `index.css` answer without sign-in. `HomeServlet`, the mover status page (`/smm`), and the install pages require it.

## Next

- Decide which pages and actions need which permissions as roles appear: upload, view HL7, administer workspaces, change IIS endpoints.
- Decide how SFTP credentials map to InteropHub users and workspaces.
- The connection a new workspace starts with (for example IIS Sandbox) was left empty for now on purpose.
- Use the AIRA Web theme for the sign-in error page and the rest of the UI (workstream 4).
