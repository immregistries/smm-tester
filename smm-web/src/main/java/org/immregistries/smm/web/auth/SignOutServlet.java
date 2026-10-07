package org.immregistries.smm.web.auth;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Handles <code>/logout</code>: ends the SMM session and returns the browser to the
 * authentication provider.
 */
public class SignOutServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    signOut(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    signOut(request, response);
  }

  private void signOut(HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    SmmSession.signOut(request);
    response.sendRedirect(
        SmmSession.getAuthenticationProvider(getServletContext()).getSignOutUrl(request));
  }
}
