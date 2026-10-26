package org.immregistries.smm.web.validation;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import org.immregistries.smm.tester.ClientServlet;
import org.immregistries.smm.validation.MessageValidation;
import org.immregistries.smm.validation.ValidationFinding;
import org.immregistries.smm.validation.ValidationProfile;
import org.immregistries.smm.validation.ValidationResult;
import org.immregistries.smm.web.SmmPage;
import org.immregistries.smm.web.SmmWebConfig;
import org.immregistries.smm.web.auth.InteropHubAuthenticationProvider;
import org.immregistries.smm.web.auth.SmmSession;
import org.immregistries.smm.web.auth.SmmUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Validates pasted HL7 v2 messages against the national immunization conformance profiles and
 * shows the results. The same validation is available without sign-in through the API and MCP
 * endpoints, which this page links to.
 */
public class ValidateMessageServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

  private static final String PARAM_MESSAGES = "messages";
  private static final String PARAM_PROFILE = "profile";
  private static final String PARAM_INCLUDE_ALL = "includeAll";

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    processRequest(request, response, null, null);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    ValidationGateway gateway = ValidationGateway.get(getServletContext());
    SmmUser user = SmmSession.getUser(request);
    String client = "user:" + (user == null ? request.getRemoteAddr() : user.getUsername());
    try {
      List<MessageValidation> validations = gateway.validate(
          request.getParameter(PARAM_MESSAGES), request.getParameter(PARAM_PROFILE), client);
      processRequest(request, response, validations, null);
    } catch (ValidationApiException e) {
      processRequest(request, response, null, e.getMessage());
    }
  }

  private void processRequest(HttpServletRequest request, HttpServletResponse response,
      List<MessageValidation> validations, String problem) throws IOException {
    response.setContentType("text/html;charset=UTF-8");
    ValidationGateway gateway = ValidationGateway.get(getServletContext());
    String messages = valueOf(request.getParameter(PARAM_MESSAGES));
    String profile = valueOf(request.getParameter(PARAM_PROFILE));
    boolean includeAll = request.getParameter(PARAM_INCLUDE_ALL) != null;

    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, "Validate Message", request);
      printPageHeader(out, "Validate Message",
          "Check HL7 v2.5.1 immunization messages against the CDC Immunization IG Release 1.5 "
              + "conformance profiles.");
      out.println("<div class=\"aira-stack\">");
      printForm(out, gateway, messages, profile, includeAll);
      if (problem != null) {
        out.println("<div class=\"aira-alert aira-alert--error\" role=\"alert\"><p>"
            + SmmPage.escapeHtml(problem) + "</p></div>");
      }
      if (validations != null) {
        printSummary(out, validations);
        for (MessageValidation validation : validations) {
          printValidation(out, validation, includeAll);
        }
      }
      printAbout(out, request, gateway);
      out.println("</div>");
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  private static void printForm(PrintWriter out, ValidationGateway gateway, String messages,
      String profile, boolean includeAll) {
    out.println("<section class=\"aira-panel\">");
    out.println("  <div class=\"aira-panel__body\">");
    out.println("    <form class=\"aira-form\" action=\"ValidateMessageServlet\" method=\"POST\">");
    out.println("      <div class=\"aira-field\">");
    out.println("        <label for=\"" + PARAM_MESSAGES + "\">HL7 Messages</label>");
    out.println("        <textarea class=\"aira-textarea smm-code\" id=\"" + PARAM_MESSAGES
        + "\" name=\"" + PARAM_MESSAGES + "\" rows=\"12\" wrap=\"off\" required>"
        + SmmPage.escapeHtml(messages) + "</textarea>");
    out.println("        <p class=\"aira-field-help\">Paste one or more messages, each starting "
        + "with MSH (at most " + gateway.getMaxMessages() + "). Use synthetic test data only: "
        + "messages are sent to an outside validation service.</p>");
    out.println("      </div>");
    out.println("      <div class=\"aira-field\">");
    out.println("        <label for=\"" + PARAM_PROFILE + "\">Profile</label>");
    out.println("        <select class=\"aira-select smm-select-auto\" id=\"" + PARAM_PROFILE
        + "\" name=\"" + PARAM_PROFILE + "\">");
    out.println("          <option value=\"\">Detect from MSH-21</option>");
    for (ValidationProfile p : gateway.getProfiles()) {
      out.println("          <option value=\"" + SmmPage.escapeHtml(p.getKey()) + "\""
          + (p.getKey().equals(profile) ? " selected" : "") + ">"
          + SmmPage.escapeHtml(p.getMessageProfileId() + " " + p.getMessageType() + " - "
              + p.getDescription())
          + "</option>");
    }
    out.println("        </select>");
    out.println("      </div>");
    out.println("      <label class=\"aira-check\"><input type=\"checkbox\" name=\""
        + PARAM_INCLUDE_ALL + "\" value=\"true\"" + (includeAll ? " checked" : "")
        + "> List routine findings too (empty RE elements, value sets not checked)</label>");
    out.println("      <div class=\"aira-form-actions\">");
    out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\">"
        + "Validate</button>");
    out.println("      </div>");
    out.println("    </form>");
    out.println("  </div>");
    out.println("</section>");
  }

  private static void printSummary(PrintWriter out, List<MessageValidation> validations) {
    long passed = validations.stream().filter(MessageValidation::isPass).count();
    boolean allPassed = passed == validations.size();
    out.println("<div class=\"aira-alert " + (allPassed ? "aira-alert--info" : "aira-alert--warning")
        + "\" role=\"status\"><p>" + (validations.size() == 1
            ? (allPassed ? "The message passed." : "The message did not pass.")
            : passed + " of " + validations.size() + " messages passed.")
        + "</p></div>");
  }

  private static void printValidation(PrintWriter out, MessageValidation validation,
      boolean includeAll) {
    String status = ValidationJson.status(validation);
    String controlId = ValidationJson.readMessageControlId(validation.getMessage());
    out.println("<section class=\"aira-table-panel\">");
    out.println("  <div class=\"aira-table-panel__header\"><div>");
    out.println("    <h2 class=\"aira-table-panel__title\">Message " + validation.getIndex()
        + (controlId == null ? "" : " &middot; " + SmmPage.escapeHtml(controlId)) + " "
        + (status.equals(ValidationJson.STATUS_NOT_VALIDATED) ? statusBadge("Not validated")
            : statusBadge(status))
        + "</h2>");
    ValidationResult result = validation.getResult();
    if (validation.getProfile() != null) {
      String counts = result == null ? ""
          : " &middot; " + result.getErrorCount() + " errors, " + result.getWarningCount()
              + " warnings, " + result.getAlertCount() + " alerts";
      out.println("    <p class=\"aira-table-panel__description\">"
          + SmmPage.escapeHtml(validation.getProfile().getKey() + ": "
              + validation.getProfile().getDescription())
          + counts + "</p>");
    }
    out.println("  </div></div>");

    if (result == null) {
      out.println("  <div class=\"aira-panel__body\"><div class=\"aira-alert aira-alert--error\""
          + " role=\"alert\"><p>" + SmmPage.escapeHtml(validation.getProblem())
          + "</p></div></div>");
    } else {
      List<ValidationFinding> findings =
          includeAll ? result.getFindings() : result.getNotableFindings();
      out.println("  <div class=\"aira-table-wrap\">");
      out.println("    <table class=\"aira-table\">");
      out.println("      <caption class=\"aira-visually-hidden\">Findings for message "
          + validation.getIndex() + "</caption>");
      out.println("      <thead><tr><th scope=\"col\">Finding</th>"
          + "<th scope=\"col\" class=\"aira-table__cell--numeric\">Line</th>"
          + "<th scope=\"col\">Location</th><th scope=\"col\">Description</th></tr></thead>");
      out.println("      <tbody>");
      if (findings.isEmpty()) {
        out.println("        <tr><td class=\"aira-table__empty\" colspan=\"4\">No errors or other "
            + "notable findings.</td></tr>");
      }
      for (ValidationFinding finding : findings) {
        out.println("        <tr><td>" + findingBadge(finding) + " "
            + SmmPage.escapeHtml(finding.getCategory()) + "</td>"
            + "<td class=\"aira-table__cell--numeric\">" + finding.getLine() + "</td>"
            + "<td class=\"aira-table__cell--code\">" + SmmPage.escapeHtml(finding.getPath())
            + "</td><td class=\"aira-table__cell--wrap\">"
            + SmmPage.escapeHtml(finding.getDescription()) + "</td></tr>");
      }
      out.println("      </tbody>");
      out.println("    </table>");
      out.println("  </div>");
      out.println("  <div class=\"aira-panel__body\">");
      if (!includeAll) {
        out.println("    <p class=\"aira-muted\">Routine findings not listed: "
            + result.getReUsageWarningCount() + " empty RE (required, but may be empty) "
            + "elements and " + result.getExcludedValueSetAlertCount()
            + " value sets the validator does not check.</p>");
      }
    }
    if (result == null) {
      out.println("  <div class=\"aira-panel__body\">");
    }
    out.println("    <details><summary>Message as validated</summary>");
    out.println("      <pre class=\"smm-hl7 smm-scroll-short\">"
        + SmmPage.escapeHtml(validation.getMessage().replace('\r', '\n')) + "</pre>");
    out.println("    </details>");
    out.println("  </div>");
    out.println("</section>");
  }

  private static String findingBadge(ValidationFinding finding) {
    String modifier = ValidationFinding.ERROR.equals(finding.getClassification())
        || ValidationFinding.SPEC_ERROR.equals(finding.getClassification()) ? "danger"
            : "outline";
    return "<span class=\"aira-badge aira-badge--" + modifier + "\">"
        + SmmPage.escapeHtml(finding.getClassification()) + "</span>";
  }

  private void printAbout(PrintWriter out, HttpServletRequest request,
      ValidationGateway gateway) {
    String baseUrl = InteropHubAuthenticationProvider
        .getExternalUrl(SmmWebConfig.get(getServletContext()), request);
    out.println("<section class=\"aira-panel\">");
    out.println("  <div class=\"aira-panel__header\">"
        + "<h2 class=\"aira-panel__title\">About This Validator</h2></div>");
    out.println("  <div class=\"aira-panel__body aira-prose\">");
    out.println("    <p>Messages are checked by the <a href=\"" + SmmPage.escapeHtml(
        gateway.getService().getSources().get(0).getWebsiteUrl())
        + "\">Valitheus Immunization Test Suite</a>, which stores a report of each validation. "
        + "A message passes when it has no errors. Empty RE elements and value sets the "
        + "validator does not check are routine and are only counted.</p>");
    out.println("    <p>The same validation is available without signing in, for scripts and "
        + "AI assistants, at up to " + gateway.getMessagesPerMinute()
        + " messages a minute:</p>");
    out.println("    <ul>");
    out.println("      <li>REST API: <code>POST " + SmmPage.escapeHtml(baseUrl)
        + "/api/validate</code>, described by <a href=\"api/openapi.json\">"
        + SmmPage.escapeHtml(baseUrl) + "/api/openapi.json</a> (import it into a ChatGPT custom "
        + "GPT as an action)</li>");
    out.println("      <li>MCP server for Claude, ChatGPT, and other assistants: <code>"
        + SmmPage.escapeHtml(baseUrl) + "/mcp</code></li>");
    out.println("    </ul>");
    out.println("  </div>");
    out.println("</section>");
  }

  private static String valueOf(String value) {
    return value == null ? "" : value;
  }
}
