package org.immregistries.smm.web.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    out.println("<!DOCTYPE html>");
    out.println("<html><head><meta charset=\"UTF-8\"><title>Sign-in Problem</title>");
    out.println("<link rel=\"stylesheet\" type=\"text/css\" href=\"index.css\" /></head><body>");
    out.println("<h1>Unable to Sign In</h1>");
    out.println("<p>" + escapeHtml(message) + "</p>");
    out.println("<p><a href=\"" + request.getContextPath() + "/login\">Try again</a> or return to "
        + "<a href=\"" + escapeHtml(provider.getSignOutUrl(request)) + "\">"
        + escapeHtml(provider.getName()) + "</a>.</p>");
    out.println("</body></html>");
  }

  static String escapeHtml(String value) {
    if (value == null) {
      return "";
    }
    return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        .replace("\"", "&quot;");
  }
}
