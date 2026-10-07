package org.immregistries.smm.web;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import jakarta.servlet.ServletContext;

/**
 * Deployment settings for the web application. Each setting is read from, in order: a Java system
 * property (for example <code>-Dsmm.hub.url=...</code>), an environment variable (for example
 * <code>SMM_HUB_URL</code>), and a <code>context-param</code> in web.xml.
 */
public class SmmWebConfig {

  public static final String HUB_URL = "smm.hub.url";
  public static final String EXTERNAL_URL = "smm.external.url";
  public static final String APP_CODE = "smm.app.code";
  public static final String WORKSPACE_DIR = "smm.workspace.dir";
  public static final String ADMIN_EMAILS = "smm.admin.emails";

  private static final String DEFAULT_HUB_URL = "http://localhost:8080/hub";
  private static final String DEFAULT_APP_CODE = "smm";
  private static final String CONTEXT_ATTRIBUTE = SmmWebConfig.class.getName();

  private final String hubUrl;
  private final String externalUrl;
  private final String appCode;
  private final String workspaceDir;
  private final Set<String> adminEmails;

  private SmmWebConfig(ServletContext context) {
    hubUrl = read(context, HUB_URL, DEFAULT_HUB_URL);
    externalUrl = read(context, EXTERNAL_URL, "");
    appCode = read(context, APP_CODE, DEFAULT_APP_CODE);
    workspaceDir = read(context, WORKSPACE_DIR, "");
    String admins = read(context, ADMIN_EMAILS, "");
    adminEmails = admins.isEmpty() ? Collections.emptySet()
        : Arrays.stream(admins.split("[,;\\s]+")).map(s -> s.trim().toLowerCase(Locale.ROOT))
            .filter(s -> !s.isEmpty()).collect(Collectors.toUnmodifiableSet());
  }

  public static SmmWebConfig get(ServletContext context) {
    Object config = context.getAttribute(CONTEXT_ATTRIBUTE);
    if (config instanceof SmmWebConfig) {
      return (SmmWebConfig) config;
    }
    SmmWebConfig newConfig = new SmmWebConfig(context);
    context.setAttribute(CONTEXT_ATTRIBUTE, newConfig);
    return newConfig;
  }

  private static String read(ServletContext context, String name, String defaultValue) {
    String value = System.getProperty(name);
    if (isBlank(value)) {
      value = System.getenv(name.toUpperCase(Locale.ROOT).replace('.', '_'));
    }
    if (isBlank(value)) {
      value = context.getInitParameter(name);
    }
    return isBlank(value) ? defaultValue : value.trim();
  }

  private static boolean isBlank(String value) {
    return value == null || value.trim().isEmpty();
  }

  /**
   * @return the InteropHub base URL, for example <code>https://example.org/hub</code>
   */
  public String getHubUrl() {
    return hubUrl;
  }

  /**
   * @return the public base URL of this application, or an empty string to derive it from the
   *         incoming request
   */
  public String getExternalUrl() {
    return externalUrl;
  }

  /**
   * @return the code this application is registered under in InteropHub
   */
  public String getAppCode() {
    return appCode;
  }

  /**
   * @return the configured workspace directory, or an empty string to use the default location
   */
  public String getWorkspaceDir() {
    return workspaceDir;
  }

  public boolean isAdmin(String email) {
    return email != null && adminEmails.contains(email.trim().toLowerCase(Locale.ROOT));
  }
}
