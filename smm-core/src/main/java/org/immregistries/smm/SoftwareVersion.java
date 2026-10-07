package org.immregistries.smm;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SoftwareVersion {

  /** The project version, filled in from the Maven build (smm-version.properties). */
  public static final String VERSION = loadVersion();

  public static final String SOFTWARE_VENDOR = "AIRA";
  public static final String SOFTWARE_PRODUCT_NAME = "SMM/Tester";

  private static String loadVersion() {
    try (InputStream in = SoftwareVersion.class.getResourceAsStream("smm-version.properties")) {
      if (in != null) {
        Properties properties = new Properties();
        properties.load(in);
        String version = properties.getProperty("version", "").trim();
        if (!version.isEmpty() && !version.startsWith("${")) {
          return version;
        }
      }
    } catch (IOException ioe) {
      // fall through to unknown
    }
    return "unknown";
  }
}
