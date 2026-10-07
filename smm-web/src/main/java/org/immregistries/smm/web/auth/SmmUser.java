package org.immregistries.smm.web.auth;

import java.io.Serializable;
import org.immregistries.smm.mover.SendData;
import org.immregistries.smm.workspace.Workspace;

/**
 * The person signed in to this SMM session: their identity, their workspace, and what they are
 * allowed to do.
 */
public class SmmUser implements Serializable {

  private static final long serialVersionUID = 1L;

  private final AuthenticatedIdentity identity;
  private final transient Workspace workspace;
  private final boolean admin;
  private transient SendData sendData = null;

  public SmmUser(AuthenticatedIdentity identity, Workspace workspace, boolean admin) {
    this.identity = identity;
    this.workspace = workspace;
    this.admin = admin;
  }

  public AuthenticatedIdentity getIdentity() {
    return identity;
  }

  public Workspace getWorkspace() {
    return workspace;
  }

  /**
   * @return the user's email address, which identifies them within SMM
   */
  public String getUsername() {
    return identity.getEmail();
  }

  public String getEmail() {
    return identity.getEmail();
  }

  public String getName() {
    return identity.getName();
  }

  public String getOrganization() {
    return identity.getOrganization();
  }

  /**
   * @return true if this user may administer SMM, for example by connecting to any of the
   *         folder-based IIS connections
   */
  public boolean isAdmin() {
    return admin;
  }

  /**
   * @return true if the session is connected to one of SMM's folder-based IIS connections
   */
  public boolean hasSendData() {
    return sendData != null;
  }

  public SendData getSendData() {
    return sendData;
  }

  public void setSendData(SendData sendData) {
    this.sendData = sendData;
  }
}
