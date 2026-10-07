# AIRA Web Layout — Plan

**Status (2026-10-07):** decisions recorded below; pass 1 (shell, navigation with right rails, compatibility CSS, home dashboard, landing page) is done and verified on the local Tomcat 10. Pass 2 (page-by-page conversion) is next.

Workstream 4 of [modernization-plan.md](modernization-plan.md): "Adopt the shared AIRA Web theme and Java servlet components" for the SMM web application, which is also the demonstration website. Sign-on (workstream 3) is done, so this is the next step.

Sources: `aira-web` 0.1.12 (`docs/adoption-guide.md`, `docs/components-guide.md`, `docs/AIRA-Web-Application-Standards.md`) and InteropHub's `InteropAiraPageFactory` as a working example.

## Starting point

- **Shared header and footer:** 26 of the 28 page servlets write them through `ClientServlet.printHtmlHead(out, title, request)` and `ClientServlet.printHtmlFoot(out)`. The shell can therefore change in one place. The other two are `DownloadServlet` (file downloads) and the new sign-in/sign-out servlets.
- **Menu:** a table of links (`Home`, `Connect to IIS`, `Manage Test Cases`, `Edit Test Case`, `Send Message`, `Sign out`) built by `ClientServlet.makeMenu`. The `title` argument selects the highlighted item, and most pages pass `MENU_HEADER_HOME`.
- **Page content:** mostly `<table border="1">` grids, plain forms, and `<pre>` HL7 blocks, styled by `index.css` classes (`pass`, `fail`, `nottested`, `running`, `boxed`, `scrollbox`, `help`, `smallTitle`, `different`, `blank`, `hl7`, `dashTable`, `viewMenu`, `submenu`).
- **Reports:** `printHtmlHeadForFile` writes stand-alone HTML reports with inline styles. They are saved files, not pages in the app.
- **Landing page:** `/smm/` serves the old static `index.html`, while sign-in falls back to `HomeServlet`. The first page after signing in therefore isn't always the home page.

## Dependency

- **Artifacts:** add `org.immregistries:aira-web-components:0.1.12`, which brings in `aira-web-theme`. They aren't on Maven Central, so keep copies (and the `aira-web` parent POM) in `repo/`, the same as InteropHub-Client.
- **Ticket:** publishing to Maven Central is tracked in [aira-web#1](https://github.com/immregistries/aira-web/issues/1) (like [InteropHub-Client#1](https://github.com/immregistries/InteropHub-Client/issues/1)). Mismo already depends on aira-web-components and can only build where the artifact is in the local Maven cache.
- **Resources:** the theme JAR serves `/aira/css/aira.css` and the AIRA logo from `META-INF/resources`. `AuthenticationFilter` already leaves `.css` and `.png` public, so they load on the sign-in error page too.

## Shell

Add `org.immregistries.smm.web.SmmPage`, a small page factory like InteropHub's `InteropAiraPageFactory`, that builds an `AiraPage` for each request:

| Shell part | SMM content |
| --- | --- |
| Logo | Default AIRA logo (`AiraDefaults.DEFAULT_LOGO_PATH`) |
| Application name / subtitle | "Simple Message Mover / Tester" / "IIS HL7 testing" (subtitle to be confirmed) |
| Version | `SoftwareVersion.VERSION` |
| Account area | Signed-in user's name, with a **Sign out** link to `/logout` |
| Environment badge | New setting `smm.environment` (for example "Local" or "Demo"); none when empty |
| Navigation | Horizontal primary navigation (below) |
| Footer | AIRA's required institutional footer, rendered by `AiraPage` |
| Local stylesheet | `/css/smm.css`, for SMM-specific classes only |

`ClientServlet.printHtmlHead` and `printHtmlFoot` keep their signatures and call `SmmPage`, so every page gets the new shell at once. The old `toggleLayer` script moves into the shell. The `message` request attribute becomes an `aira-alert`.

## Navigation

The standard calls for horizontal navigation when an application has five or fewer stable primary areas. SMM fits:

| Area | Pages |
| --- | --- |
| **Home** | `HomeServlet` (redesigned as the dashboard) |
| **Connect** | `ConnectServlet`, `InstallCertServlet` |
| **Send** | `SubmitServlet` (Send Message), `ManualQueryServlet` (Query IIS), `BulkQueryServlet` |
| **Test Cases** | `SetupServlet` (Manage), `CreateTestCaseServlet` (Edit), `TestCaseServlet` (`/testCase`, Run), `TestCaseMessageViewerServlet` |
| **Tools** | `ModifyMessageServlet`, `MessageViewerServlet`, `GenerateDataServlet`, `GenerateExamplesServlet`, `InterfaceProfileServlet` (`/interfaceProfile`), `StressTestServlet` |

- **Page grouping:** each page declares its area instead of passing a menu title. Pages within an area link to each other with a small sub-navigation (`aira-` tabs or links) at the top of the content.
- **School rosters:** a sixth area, **Rosters**, arrives in workstream 5 for the school roster upload and results. At that point, switching to a sidebar may be worth considering.
- **Admin only** (`smm.admin.emails`): the Message Mover status page (`/smm`) and the SMM installer pages (`/install/*`, `/installDQA/*`), under an **Admin** action in the header.
- **No navigation:** the machine endpoints (`/wsdl*`, `/wsdl-demo*`, `/CatchServlet`) get the shell but stay public.

## Home page and landing

- **Dashboard:** rebuild `HomeServlet` with a workspace panel (who is signed in, their workspace, current IIS connection, or a prompt to connect), cards for the primary tasks (Connect, Send Message, Test Cases), and a compact list of Tools.
- **Landing:** make `HomeServlet` the welcome page and retire the static `index.html`. `/smm/` and sign-in then both land on the dashboard. A bookmarked deep link still returns to the page that was requested.

## Page content

The work is done in two passes, so the whole app looks consistent early and pages are polished in priority order:

1. **Shell and compatibility CSS:**
   - Swap the shell.
   - Replace `index.css` with `css/smm.css`. It keeps only the content classes the pages still use (`pass`, `fail`, `boxed`, `scrollbox`, `hl7`, and so on), restyled with the AIRA CSS custom properties (colors, spacing, monospace) instead of the old palette.
   - Remove the old menu and body styles.
2. **Page by page:**
   - **Conversion:** tables become `aira-table` inside `aira-table-wrap`, forms use `aira-form` controls and `aira-button` variants, pass/fail states use `aira-badge--success`/`--danger`, and HL7 is shown in a monospace `pre` block.
   - **Escaping:** while converting, escape every value that's echoed back. The Modify Message page currently writes its text boxes unescaped.
   - **Order:**
     1. Home
     2. Connect
     3. Send Message
     4. Modify Message
     5. Sign-in error page
     6. Test Cases pages
     7. the remaining tools
     8. the admin and installer pages

The stand-alone HTML reports (`printHtmlHeadForFile`) keep inline styles. They're opened outside the app and can't load `aira.css`.

## Verification

- Deploy to the local Tomcat 10 with InteropHub.
- Open every page signed in, and check:
  - the active navigation item
  - the account area and sign-out
  - the environment badge
  - the footer
  - the alert for `message`
  - that admin-only links are hidden for non-admins
- Check narrow widths and browser zoom.
- Run the AIRA quality checklist (standards doc §16).

## Decisions (2026-10-07)

- **Retired:** the Dashboard (`/dash`) and the SMM installer pages (`/install/*`, `/installDQA/*`), along with the connection templates, `ConnectionConfiguration`, and the `software.dir` setting that only they used.
- **Kept:** Generate Examples, Stress Test, Bulk Query, and the WSDL demo.
- **Header:** application name "Simple Message Mover", subtitle "Connection and Testing Tools". No environment badge for now.
- **Navigation:** the five areas. Tools holds everything that doesn't fit elsewhere. Each area shows its pages in a right rail on every page in that area.

## Pass 1 notes

- **Shell code:** `SmmNavigation` holds the areas and pages, keyed by servlet path. `SmmPage` writes the shell and the rail, and `ClientServlet.printHtmlHead`/`printHtmlFoot` delegate to it, so no page needed changing. The `message` request attribute shows as an `aira-alert--warning`.
- **Pages that need a mover connection:** Install Certificate and Bulk Query only work when an admin has attached a folder-based mover connection. They're hidden from the rails and home cards otherwise, as the old home page did. Bulk Query now redirects home like Install Certificate, instead of throwing a NullPointerException.
- **Landing page:** `HomeServlet` is the welcome file. The old `index.html`, `index.css`, `style.css`, `logo.png`, and the unreferenced `reportExplanation.html` were removed.
- **Footer version:** `SoftwareVersion.VERSION` (shown in the footer) now comes from the Maven project version, through a filtered `smm-version.properties`, instead of the stale "2.24".
- **Sign-in error page:** now uses the AIRA shell.

## Open questions

1. **Pages to retire.** Fewer pages make simpler navigation:
   - `DashboardServlet` (`/dash`) isn't linked anywhere.
   - `GenerateExamplesServlet` is linked only from Modify Message.
   - `StressTestServlet` and `BulkQueryServlet` may not be needed.
   - The installer pages (`/install/*`, `/installDQA/*`) belong to the old downloadable SMM.
   - The WSDL demo (`/wsdl-demo/*`) may also be unnecessary.
2. **Subtitle and environment label** for the local and demonstration deployments.
3. **The five-area split above**, especially whether Query IIS belongs under Send and Install Cert under Connect.
