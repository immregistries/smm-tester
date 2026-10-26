package org.immregistries.smm.validation;

import java.util.List;

/**
 * An external HL7 v2 validation service and the profiles it offers. SMM currently uses the
 * Valitheus Immunization Test Suite Edition 2; other services (for example a future school roster
 * validator) can be added as further sources.
 */
public final class ValidatorSource {

  /**
   * The Valitheus Immunization Test Suite Edition 2, maintained by Prometheus Computing. It checks
   * messages against the CDC HL7 v2.5.1 Immunization Messaging IG Release 1.5 profiles. The test
   * context ids were current in October 2026.
   */
  public static final ValidatorSource VALITHEUS_IZ_EDITION2 = new ValidatorSource(
      "valitheus-iz-edition2", "Valitheus Immunization Test Suite Edition 2",
      "https://tools.valitheus.com/immunization-edition2/#/",
      "https://tools.valitheus.com/immunization-edition2/api", "iz-hti-1-svap",
      List.of(
          new ValidationProfile("vxu-z22", "Z22", "VXU^V04^VXU_V04",
              "Send unsolicited immunization update", "VXU-Z22-Submit", 473),
          new ValidationProfile("ack-z23", "Z23", "ACK^V04^ACK",
              "Acknowledgement of an immunization update", "ACK-Z23-Acknowledgement", 469),
          new ValidationProfile("qbp-z34", "Z34", "QBP^Q11^QBP_Q11",
              "Query: request complete immunization history", "QBP-Z34-Query", 471),
          new ValidationProfile("qbp-z44", "Z44", "QBP^Q11^QBP_Q11",
              "Query: request evaluated history and forecast", "QBP-Z44-Query", 474),
          new ValidationProfile("rsp-z31", "Z31", "RSP^K11^RSP_K11",
              "Response: list of candidate patients", "RSP-Z31-Response", 475),
          new ValidationProfile("rsp-z32", "Z32", "RSP^K11^RSP_K11",
              "Response: complete immunization history", "RSP-Z32-Response", 470),
          new ValidationProfile("rsp-z33", "Z33", "RSP^K11^RSP_K11",
              "Response: acknowledgement with no person records (none found or too many)",
              "RSP-Z33-Response", 476),
          new ValidationProfile("rsp-z42", "Z42", "RSP^K11^RSP_K11",
              "Response: evaluated history and forecast", "RSP-Z42-Response", 472)));

  private final String key;
  private final String name;
  private final String websiteUrl;
  private final String apiBaseUrl;
  private final String domain;
  private final List<ValidationProfile> profiles;

  public ValidatorSource(String key, String name, String websiteUrl, String apiBaseUrl,
      String domain, List<ValidationProfile> profiles) {
    this.key = key;
    this.name = name;
    this.websiteUrl = websiteUrl;
    this.apiBaseUrl = apiBaseUrl;
    this.domain = domain;
    this.profiles = List.copyOf(profiles);
  }

  public String getKey() {
    return key;
  }

  public String getName() {
    return name;
  }

  /**
   * @return the web page people can use to validate messages by hand
   */
  public String getWebsiteUrl() {
    return websiteUrl;
  }

  public String getApiBaseUrl() {
    return apiBaseUrl;
  }

  /**
   * @return the source's domain (tool scope) key, used to list its published test plans
   */
  public String getDomain() {
    return domain;
  }

  public List<ValidationProfile> getProfiles() {
    return profiles;
  }
}
