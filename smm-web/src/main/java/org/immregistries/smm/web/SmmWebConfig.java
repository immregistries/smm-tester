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
  public static final String API_REQUESTS_PER_MINUTE = "smm.api.requests.per.minute";
  public static final String VALIDATION_MESSAGES_PER_MINUTE = "smm.validation.messages.per.minute";
  public static final String VALIDATION_MESSAGES_TOTAL_PER_MINUTE =
      "smm.validation.messages.total.per.minute";
  public static final String VALIDATION_MAX_MESSAGES = "smm.validation.max.messages";
  public static final String VALIDATION_MAX_BYTES = "smm.validation.max.bytes";
  public static final String TRUST_FORWARDED_FOR = "smm.trust.forwarded.for";

  private static final String DEFAULT_HUB_URL = "http://localhost:8080/hub";
  private static final String DEFAULT_APP_CODE = "smm";
  private static final String CONTEXT_ATTRIBUTE = SmmWebConfig.class.getName();

  private final String hubUrl;
  private final String externalUrl;
  private final String appCode;
  private final String workspaceDir;
  private final Set<String> adminEmails;
  private final int apiRequestsPerMinute;
  private final int validationMessagesPerMinute;
  private final int validationMessagesTotalPerMinute;
  private final int validationMaxMessages;
  private final int validationMaxBytes;
  private final boolean trustForwardedFor;

  private SmmWebConfig(ServletContext context) {
    hubUrl = read(context, HUB_URL, DEFAULT_HUB_URL);
    externalUrl = read(context, EXTERNAL_URL, "");
    appCode = read(context, APP_CODE, DEFAULT_APP_CODE);
    workspaceDir = read(context, WORKSPACE_DIR, "");
    String admins = read(context, ADMIN_EMAILS, "");
    adminEmails = admins.isEmpty() ? Collections.emptySet()
        : Arrays.stream(admins.split("[,;\\s]+")).map(s -> s.trim().toLowerCase(Locale.ROOT))
            .filter(s -> !s.isEmpty()).collect(Collectors.toUnmodifiableSet());
    apiRequestsPerMinute = readInt(context, API_REQUESTS_PER_MINUTE, 60);
    validationMessagesPerMinute = readInt(context, VALIDATION_MESSAGES_PER_MINUTE, 30);
    validationMessagesTotalPerMinute = readInt(context, VALIDATION_MESSAGES_TOTAL_PER_MINUTE, 300);
    validationMaxMessages = readInt(context, VALIDATION_MAX_MESSAGES, 20);
    validationMaxBytes = readInt(context, VALIDATION_MAX_BYTES, 256 * 1024);
    trustForwardedFor = Boolean.parseBoolean(read(context, TRUST_FORWARDED_FOR, "false"));
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

  private static int readInt(ServletContext context, String name, int defaultValue) {
    String value = read(context, name, "");
    try {
      return value.isEmpty() ? defaultValue : Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
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

  /**
   * @return the most requests one client may make to the public API and MCP endpoints in a minute
   */
  public int getApiRequestsPerMinute() {
    return apiRequestsPerMinute;
  }

  /**
   * @return the most messages one client may validate in a minute
   */
  public int getValidationMessagesPerMinute() {
    return validationMessagesPerMinute;
  }

  /**
   * @return the most messages all clients together may validate in a minute
   */
  public int getValidationMessagesTotalPerMinute() {
    return validationMessagesTotalPerMinute;
  }

  /**
   * @return the most messages one validation request may hold
   */
  public int getValidationMaxMessages() {
    return validationMaxMessages;
  }

  /**
   * @return the largest validation request body accepted, in bytes
   */
  public int getValidationMaxBytes() {
    return validationMaxBytes;
  }

  /**
   * @return true if SMM runs behind a reverse proxy that sets X-Forwarded-For, so the client
   *         address for rate limiting is read from that header
   */
  public boolean isTrustForwardedFor() {
    return trustForwardedFor;
  }

  public boolean isAdmin(String email) {
    return email != null && adminEmails.contains(email.trim().toLowerCase(Locale.ROOT));
  }
}
