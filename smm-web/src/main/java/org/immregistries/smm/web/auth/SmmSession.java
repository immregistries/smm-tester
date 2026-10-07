package org.immregistries.smm.web.auth;

import java.io.IOException;
import org.immregistries.smm.web.SmmWebConfig;
import org.immregistries.smm.workspace.WorkspaceStore;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * Holds the signed-in user in the HTTP session, and the application-wide authentication provider
 * and workspace store.
 */
public final class SmmSession {

  /** Session attribute holding the {@link SmmUser}. */
  public static final String ATTRIBUTE_USER = "user";
  /** Session attribute holding the user's email; servlets check it to see if anyone is signed in. */
  public static final String ATTRIBUTE_USERNAME = "username";

  private static final String CONTEXT_PROVIDER = AuthenticationProvider.class.getName();
  private static final String CONTEXT_WORKSPACE_STORE = WorkspaceStore.class.getName();

  private SmmSession() {}

  public static SmmUser getUser(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session == null) {
      return null;
    }
    Object user = session.getAttribute(ATTRIBUTE_USER);
    return user instanceof SmmUser ? (SmmUser) user : null;
  }

  /**
   * Starts a new session for the user, replacing any earlier session so its identifier cannot be
   * reused.
   */
  public static HttpSession signIn(HttpServletRequest request, SmmUser user) {
    HttpSession oldSession = request.getSession(false);
    if (oldSession != null) {
      oldSession.invalidate();
    }
    HttpSession session = request.getSession(true);
    session.setAttribute(ATTRIBUTE_USER, user);
    session.setAttribute(ATTRIBUTE_USERNAME, user.getUsername());
    return session;
  }

  public static void signOut(HttpServletRequest request) {
    HttpSession session = request.getSession(false);
    if (session != null) {
      session.invalidate();
    }
  }

  public static synchronized AuthenticationProvider getAuthenticationProvider(
      ServletContext context) {
    Object provider = context.getAttribute(CONTEXT_PROVIDER);
    if (provider instanceof AuthenticationProvider) {
      return (AuthenticationProvider) provider;
    }
    AuthenticationProvider newProvider =
        new InteropHubAuthenticationProvider(SmmWebConfig.get(context));
    context.setAttribute(CONTEXT_PROVIDER, newProvider);
    return newProvider;
  }

  public static synchronized WorkspaceStore getWorkspaceStore(ServletContext context)
      throws IOException {
    Object store = context.getAttribute(CONTEXT_WORKSPACE_STORE);
    if (store instanceof WorkspaceStore) {
      return (WorkspaceStore) store;
    }
    WorkspaceStore newStore = new WorkspaceStore(
        WorkspaceStore.resolveBaseDirectory(SmmWebConfig.get(context).getWorkspaceDir()));
    context.log("SMM workspaces are stored in " + newStore.getBaseDirectory().getCanonicalPath());
    context.setAttribute(CONTEXT_WORKSPACE_STORE, newStore);
    return newStore;
  }
}
