package org.immregistries.smm.workspace;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.OffsetDateTime;
import java.util.Locale;
import java.util.Properties;

/**
 * Keeps one workspace directory per user under a base directory. No database is needed: each
 * workspace is a folder named for its owner's email address, holding a
 * <code>workspace.properties</code> file that describes it.
 */
public class WorkspaceStore {

  public static final String PROPERTIES_FILE_NAME = "workspace.properties";

  private static final String KEY_OWNER_EMAIL = "owner.email";
  private static final String KEY_OWNER_NAME = "owner.name";
  private static final String KEY_ORGANIZATION = "organization";
  private static final String KEY_CREATED = "created";

  private final File baseDirectory;

  public WorkspaceStore(File baseDirectory) {
    this.baseDirectory = baseDirectory;
  }

  public File getBaseDirectory() {
    return baseDirectory;
  }

  /**
   * Chooses the base directory for workspaces. A configured directory is used when given;
   * otherwise workspaces go under the servlet container's base directory
   * (<code>catalina.base</code>), or the user's home directory when that is not available.
   */
  public static File resolveBaseDirectory(String configuredDirectory) {
    if (configuredDirectory != null && !configuredDirectory.trim().isEmpty()) {
      return new File(configuredDirectory.trim());
    }
    String root = System.getProperty("catalina.base");
    if (root == null || root.trim().isEmpty()) {
      root = System.getProperty("user.home");
    }
    return new File(new File(root, "smm"), "workspaces");
  }

  /**
   * Returns the workspace owned by the given email address, creating an empty one the first time
   * this user is seen.
   */
  public synchronized Workspace openForOwner(String ownerEmail, String ownerName,
      String organization) throws IOException {
    if (ownerEmail == null || ownerEmail.trim().isEmpty()) {
      throw new IllegalArgumentException("A workspace owner email is required");
    }
    String id = makeWorkspaceId(ownerEmail);
    File directory = new File(baseDirectory, id);
    File propertiesFile = new File(directory, PROPERTIES_FILE_NAME);
    Properties properties = new Properties();
    if (propertiesFile.exists()) {
      try (Reader in = Files.newBufferedReader(propertiesFile.toPath(), StandardCharsets.UTF_8)) {
        properties.load(in);
      }
    } else {
      Files.createDirectories(directory.toPath());
      properties.setProperty(KEY_OWNER_EMAIL, ownerEmail.trim());
      properties.setProperty(KEY_OWNER_NAME, ownerName == null ? "" : ownerName);
      properties.setProperty(KEY_ORGANIZATION, organization == null ? "" : organization);
      properties.setProperty(KEY_CREATED, OffsetDateTime.now().toString());
      try (Writer out = Files.newBufferedWriter(propertiesFile.toPath(), StandardCharsets.UTF_8)) {
        properties.store(out, "SMM workspace");
      }
    }
    return new Workspace(id, directory, properties.getProperty(KEY_OWNER_EMAIL, ownerEmail),
        properties.getProperty(KEY_OWNER_NAME, ""), properties.getProperty(KEY_ORGANIZATION, ""),
        properties.getProperty(KEY_CREATED, ""));
  }

  /**
   * Turns an email address into a directory name that is safe on every file system, for example
   * <code>pat.smith@example.org</code> becomes <code>pat.smith_at_example.org</code>.
   */
  public static String makeWorkspaceId(String ownerEmail) {
    String email = ownerEmail.trim().toLowerCase(Locale.ROOT).replace("@", "_at_");
    StringBuilder sb = new StringBuilder(email.length());
    for (char c : email.toCharArray()) {
      if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.' || c == '-' || c == '_') {
        sb.append(c);
      } else {
        sb.append('_');
      }
    }
    String id = sb.toString();
    while (id.startsWith(".")) {
      id = id.substring(1);
    }
    if (id.isEmpty()) {
      throw new IllegalArgumentException("Unable to make a workspace id from '" + ownerEmail + "'");
    }
    return id;
  }
}
