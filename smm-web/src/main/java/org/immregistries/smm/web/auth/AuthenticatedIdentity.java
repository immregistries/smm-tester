package org.immregistries.smm.web.auth;

import java.io.Serializable;

/**
 * Who a person is, as reported by the authentication provider that signed them in.
 */
public class AuthenticatedIdentity implements Serializable {

  private static final long serialVersionUID = 1L;

  private final String provider;
  private final String subject;
  private final String email;
  private final String name;
  private final String organization;
  private final String title;

  public AuthenticatedIdentity(String provider, String subject, String email, String name,
      String organization, String title) {
    this.provider = provider;
    this.subject = subject;
    this.email = email;
    this.name = name;
    this.organization = organization;
    this.title = title;
  }

  /**
   * @return the name of the provider that authenticated this person, for example "InteropHub"
   */
  public String getProvider() {
    return provider;
  }

  /**
   * @return the provider's identifier for this person
   */
  public String getSubject() {
    return subject;
  }

  public String getEmail() {
    return email;
  }

  public String getName() {
    return name;
  }

  public String getOrganization() {
    return organization;
  }

  public String getTitle() {
    return title;
  }
}
