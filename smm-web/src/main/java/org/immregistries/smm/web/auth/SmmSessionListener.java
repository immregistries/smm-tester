package org.immregistries.smm.web.auth;

import org.immregistries.smm.roster.SchoolRosterScanner;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;

public class SmmSessionListener implements HttpSessionListener {

  @Override
  public void sessionDestroyed(HttpSessionEvent event) {
    Object user = event.getSession().getAttribute(SmmSession.ATTRIBUTE_USER);
    if (user instanceof SmmUser smmUser) {
      SchoolRosterScanner.remove(smmUser);
    }
  }
}
