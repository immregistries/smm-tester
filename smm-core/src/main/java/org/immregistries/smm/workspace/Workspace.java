package org.immregistries.smm.workspace;

import java.io.File;

/**
 * A user's private area for files and settings. Each workspace is a directory managed by
 * {@link WorkspaceStore}.
 */
public class Workspace {

  private final String id;
  private final File directory;
  private final String ownerEmail;
  private final String ownerName;
  private final String organization;
  private final String created;

  public Workspace(String id, File directory, String ownerEmail, String ownerName,
      String organization, String created) {
    this.id = id;
    this.directory = directory;
    this.ownerEmail = ownerEmail;
    this.ownerName = ownerName;
    this.organization = organization;
    this.created = created;
  }

  public String getId() {
    return id;
  }

  public File getDirectory() {
    return directory;
  }

  public String getOwnerEmail() {
    return ownerEmail;
  }

  public String getOwnerName() {
    return ownerName;
  }

  public String getOrganization() {
    return organization;
  }

  /**
   * @return when the workspace was first created, as an ISO-8601 timestamp
   */
  public String getCreated() {
    return created;
  }
}
