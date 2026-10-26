package org.immregistries.smm.validation;

/**
 * One finding from a validation report, such as a missing required field or a code that is not in
 * the profile's value set.
 */
public final class ValidationFinding {

  public static final String ERROR = "Error";
  public static final String WARNING = "Warning";
  public static final String ALERT = "Alert";
  public static final String SPEC_ERROR = "Spec Error";
  public static final String INFORMATIONAL = "Informational";

  /** Category of warnings about empty "Required, but may be Empty" elements. */
  public static final String CATEGORY_RE_USAGE = "RE-Usage";

  private final String classification;
  private final String category;
  private final int line;
  private final int column;
  private final String path;
  private final String description;

  /**
   * @param classification Error, Warning, Alert, Spec Error, or Informational
   * @param category the kind of finding, for example <code>Code Not Found</code>
   * @param line the segment number in the message, starting at 1
   * @param column the character position in the segment, starting at 1
   * @param path the element's location, for example <code>PID[1]-3[2].5</code>
   * @param description what was found
   */
  public ValidationFinding(String classification, String category, int line, int column,
      String path, String description) {
    this.classification = classification;
    this.category = category;
    this.line = line;
    this.column = column;
    this.path = path;
    this.description = description;
  }

  public String getClassification() {
    return classification;
  }

  public String getCategory() {
    return category;
  }

  public int getLine() {
    return line;
  }

  public int getColumn() {
    return column;
  }

  public String getPath() {
    return path;
  }

  public String getDescription() {
    return description;
  }

  /**
   * @return true for a warning that an RE (required, but may be empty) element is empty; these
   *         are common and usually expected
   */
  public boolean isReUsageWarning() {
    return WARNING.equals(classification) && CATEGORY_RE_USAGE.equals(category);
  }

  /**
   * @return true for an alert that the validator did not check a value set; these say what was not
   *         checked rather than reporting a problem
   */
  public boolean isExcludedValueSetAlert() {
    return ALERT.equals(classification) && description != null
        && description.contains("excluded from the validation");
  }

  /**
   * @return true unless the finding is one of the routine kinds (RE-usage warnings, excluded value
   *         set alerts, and informational notes) that summaries count instead of listing
   */
  public boolean isNotable() {
    return !isReUsageWarning() && !isExcludedValueSetAlert()
        && !INFORMATIONAL.equals(classification);
  }
}
