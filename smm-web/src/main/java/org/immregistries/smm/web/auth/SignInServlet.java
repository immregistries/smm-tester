package org.immregistries.smm.web.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.immregistries.aira.web.AiraPage;
import org.immregistries.smm.web.SmmNavigation.Area;
import org.immregistries.smm.web.SmmPage;
import org.immregistries.smm.web.SmmWebConfig;
import org.immregistries.smm.workspace.Workspace;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles <code>/login</code>. With a sign-in code from the authentication provider it completes
 * the sign-in, opens the user's workspace, and starts their SMM session. Without one it sends the
 * browser to the provider to sign in.
 */
public class SignInServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  private static final Logger LOG = Logger.getLogger(SignInServlet.class.getName());

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    AuthenticationProvider provider = SmmSession.getAuthenticationProvider(getServletContext());
    String code = request.getParameter("code");
    if (code == null || code.trim().isEmpty()) {
      SmmWebConfig config = SmmWebConfig.get(getServletContext());
      provider.startSignIn(request, response,
          InteropHubAuthenticationProvider.getExternalUrl(config, request) + "/HomeServlet");
      return;
    }

    SignInResult result = provider.completeSignIn(request);
    if (!result.isSuccess()) {
      printProblem(request, response, provider, result.getErrorMessage());
      return;
    }

    AuthenticatedIdentity identity = result.getIdentity();
    Workspace workspace;
    try {
      workspace = SmmSession.getWorkspaceStore(getServletContext())
          .openForOwner(identity.getEmail(), identity.getName(), identity.getOrganization());
    } catch (IOException | IllegalArgumentException e) {
      LOG.log(Level.WARNING, "Unable to open workspace for " + identity.getEmail(), e);
      printProblem(request, response, provider,
          "Signed in, but SMM could not open your workspace: " + e.getMessage());
      return;
    }
    boolean admin = SmmWebConfig.get(getServletContext()).isAdmin(identity.getEmail());
    SmmSession.signIn(request, new SmmUser(identity, workspace, admin));
    LOG.info("Signed in " + identity.getEmail() + " through " + identity.getProvider()
        + " with workspace " + workspace.getId() + (admin ? " (admin)" : ""));
    response.sendRedirect(result.getRedirectTarget());
  }

  private void printProblem(HttpServletRequest request, HttpServletResponse response,
      AuthenticationProvider provider, String message) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    AiraPage page = SmmPage.buildAiraPage(request, Area.HOME, "Unable to Sign In");
    page.writeStart(out);
    out.println("    <div class=\"aira-container--narrow aira-stack\">");
    out.println("      <h1 class=\"aira-page-title\">Unable to Sign In</h1>");
    out.println("      <div class=\"aira-alert aira-alert--error\" role=\"alert\"><p>"
        + escapeHtml(message) + "</p></div>");
    out.println("      <div class=\"aira-cluster\">");
    out.println("        <a class=\"aira-button aira-button--primary\" href=\""
        + request.getContextPath() + "/login\">Try again</a>");
    out.println("        <a class=\"aira-button aira-button--secondary\" href=\""
        + escapeHtml(provider.getSignOutUrl(request)) + "\">Return to "
        + escapeHtml(provider.getName()) + "</a>");
    out.println("      </div>");
    out.println("    </div>");
    page.writeEnd(out);
  }

  static String escapeHtml(String value) {
    if (value == null) {
      return "";
    }
    return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        .replace("\"", "&quot;");
  }
}
