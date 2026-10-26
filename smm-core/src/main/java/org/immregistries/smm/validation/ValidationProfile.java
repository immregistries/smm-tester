package org.immregistries.smm.validation;

/**
 * A conformance profile that a validator source can check messages against, such as the CDC
 * Immunization IG Release 1.5 Z22 VXU profile.
 */
public final class ValidationProfile {

  private final String key;
  private final String messageProfileId;
  private final String messageType;
  private final String description;
  private final String testObjectName;
  private final long testContextId;

  /**
   * @param key unique key callers use to select the profile, for example <code>vxu-z22</code>
   * @param messageProfileId the MSH-21.1 value that identifies messages for this profile, for
   *        example <code>Z22</code>
   * @param messageType the MSH-9 value, for example <code>VXU^V04^VXU_V04</code>
   * @param description a short description for people
   * @param testObjectName the name the validator source publishes the profile under; used to find
   *        the profile again if the source is republished
   * @param testContextId the validator source's id for the profile when it was last checked
   */
  public ValidationProfile(String key, String messageProfileId, String messageType,
      String description, String testObjectName, long testContextId) {
    this.key = key;
    this.messageProfileId = messageProfileId;
    this.messageType = messageType;
    this.description = description;
    this.testObjectName = testObjectName;
    this.testContextId = testContextId;
  }

  public String getKey() {
    return key;
  }

  public String getMessageProfileId() {
    return messageProfileId;
  }

  public String getMessageType() {
    return messageType;
  }

  public String getDescription() {
    return description;
  }

  public String getTestObjectName() {
    return testObjectName;
  }

  public long getTestContextId() {
    return testContextId;
  }
}
