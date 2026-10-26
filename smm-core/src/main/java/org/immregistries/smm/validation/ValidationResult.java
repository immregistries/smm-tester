package org.immregistries.smm.validation;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The outcome of validating one message against one profile. A message passes when the report has
 * no errors.
 */
public final class ValidationResult {

  private final String profileIdentifier;
  private final String profileName;
  private final String reportId;
  private final int errorCount;
  private final int warningCount;
  private final int alertCount;
  private final int specErrorCount;
  private final List<ValidationFinding> findings;

  /**
   * @param profileIdentifier the profile identifier the validator reported, for example
   *        <code>VXU-Z22-Send</code>
   * @param profileName the profile name the validator reported
   * @param reportId the validator's id for the stored report, or null
   * @param findings the report's findings, without the affirmative (passed check) entries
   */
  public ValidationResult(String profileIdentifier, String profileName, String reportId,
      int errorCount, int warningCount, int alertCount, int specErrorCount,
      List<ValidationFinding> findings) {
    this.profileIdentifier = profileIdentifier;
    this.profileName = profileName;
    this.reportId = reportId;
    this.errorCount = errorCount;
    this.warningCount = warningCount;
    this.alertCount = alertCount;
    this.specErrorCount = specErrorCount;
    this.findings = List.copyOf(findings);
  }

  public boolean isPass() {
    return errorCount == 0;
  }

  public String getProfileIdentifier() {
    return profileIdentifier;
  }

  public String getProfileName() {
    return profileName;
  }

  public String getReportId() {
    return reportId;
  }

  public int getErrorCount() {
    return errorCount;
  }

  public int getWarningCount() {
    return warningCount;
  }

  public int getAlertCount() {
    return alertCount;
  }

  public int getSpecErrorCount() {
    return specErrorCount;
  }

  public List<ValidationFinding> getFindings() {
    return findings;
  }

  /**
   * @return the findings worth listing individually: errors, spec errors, and the warnings and
   *         alerts other than the routine kinds
   */
  public List<ValidationFinding> getNotableFindings() {
    return findings.stream().filter(ValidationFinding::isNotable).collect(Collectors.toList());
  }

  public int getReUsageWarningCount() {
    return (int) findings.stream().filter(ValidationFinding::isReUsageWarning).count();
  }

  public int getExcludedValueSetAlertCount() {
    return (int) findings.stream().filter(ValidationFinding::isExcludedValueSetAlert).count();
  }
}
