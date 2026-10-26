package org.immregistries.smm.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Validates HL7 v2 messages against the profiles of one or more validator sources. Input text may
 * hold several messages, each starting with MSH, with any line endings. The profile for each
 * message is chosen by key or detected from MSH-21.
 */
public class ValidationService {

  private static final ValidationService DEFAULT =
      new ValidationService(List.of(new ValitheusClient(ValidatorSource.VALITHEUS_IZ_EDITION2)));

  private final List<HL7Validator> validators;

  /**
   * @param validators the validators to use; when two profiles match a message, the earlier
   *        validator's profile is chosen
   */
  public ValidationService(List<HL7Validator> validators) {
    this.validators = List.copyOf(validators);
  }

  /**
   * @return the service backed by the validator sources SMM supports
   */
  public static ValidationService getDefault() {
    return DEFAULT;
  }

  public List<ValidatorSource> getSources() {
    List<ValidatorSource> sources = new ArrayList<>();
    for (HL7Validator validator : validators) {
      sources.add(validator.getSource());
    }
    return sources;
  }

  public List<ValidationProfile> getProfiles() {
    List<ValidationProfile> profiles = new ArrayList<>();
    for (HL7Validator validator : validators) {
      profiles.addAll(validator.getSource().getProfiles());
    }
    return profiles;
  }

  /**
   * @return the profile with this key, ignoring case, or null if there is none
   */
  public ValidationProfile findProfile(String key) {
    if (key == null) {
      return null;
    }
    for (ValidationProfile profile : getProfiles()) {
      if (profile.getKey().equalsIgnoreCase(key.trim())) {
        return profile;
      }
    }
    return null;
  }

  /**
   * @return the profile whose message profile id matches one of the message's MSH-21 values, or
   *         null if none does
   */
  public ValidationProfile detectProfile(String message) {
    for (String profileId : readMessageProfileIds(message)) {
      for (ValidationProfile profile : getProfiles()) {
        if (profile.getMessageProfileId().equalsIgnoreCase(profileId)) {
          return profile;
        }
      }
    }
    return null;
  }

  /**
   * @return the first component of each MSH-21 repetition, for example <code>Z22</code>
   */
  static List<String> readMessageProfileIds(String message) {
    List<String> ids = new ArrayList<>();
    if (message == null || !message.startsWith("MSH") || message.length() < 8) {
      return ids;
    }
    int segmentEnd = message.indexOf('\r');
    String msh = segmentEnd < 0 ? message : message.substring(0, segmentEnd);
    char fieldSeparator = msh.charAt(3);
    String encodingCharacters = msh.substring(4, Math.min(msh.length(), 8));
    char componentSeparator = encodingCharacters.charAt(0);
    char repetitionSeparator = encodingCharacters.length() > 1 ? encodingCharacters.charAt(1) : '~';
    // MSH-1 is the field separator itself, so MSH-n is at index n - 1 after splitting
    String[] fields = msh.split(Pattern.quote(String.valueOf(fieldSeparator)), -1);
    if (fields.length > 20) {
      for (String repetition : fields[20]
          .split(Pattern.quote(String.valueOf(repetitionSeparator)))) {
        int componentEnd = repetition.indexOf(componentSeparator);
        String id = (componentEnd < 0 ? repetition : repetition.substring(0, componentEnd)).trim();
        if (!id.isEmpty()) {
          ids.add(id.toUpperCase(Locale.ROOT));
        }
      }
    }
    return ids;
  }

  /**
   * Splits text into messages. Segments may be separated by CR, LF, or CRLF; blank lines and batch
   * envelope segments (FHS, BHS, BTS, FTS) are dropped, and each MSH starts a new message.
   *
   * @return the messages, with segments separated by carriage returns
   */
  public static List<String> splitMessages(String text) {
    List<StringBuilder> messages = new ArrayList<>();
    if (text == null) {
      return new ArrayList<>();
    }
    for (String line : text.replace("\r\n", "\n").replace('\r', '\n').split("\n")) {
      String segment = line.replace("﻿", "").strip();
      if (segment.isEmpty() || segment.startsWith("FHS") || segment.startsWith("BHS")
          || segment.startsWith("BTS") || segment.startsWith("FTS")) {
        continue;
      }
      if (segment.startsWith("MSH") || messages.isEmpty()) {
        messages.add(new StringBuilder(segment));
      } else {
        messages.get(messages.size() - 1).append('\r').append(segment);
      }
    }
    List<String> result = new ArrayList<>();
    for (StringBuilder message : messages) {
      result.add(message.toString());
    }
    return result;
  }

  /**
   * Validates each message in turn.
   *
   * @param messages messages as returned by {@link #splitMessages(String)}
   * @param profileKey the profile to use for every message, or null to detect each message's
   *        profile from MSH-21
   * @throws IllegalArgumentException if profileKey is not a known profile
   */
  public List<MessageValidation> validate(List<String> messages, String profileKey) {
    ValidationProfile forcedProfile = null;
    if (profileKey != null && !profileKey.isBlank()) {
      forcedProfile = findProfile(profileKey);
      if (forcedProfile == null) {
        throw new IllegalArgumentException("Unknown profile '" + profileKey + "'");
      }
    }
    List<MessageValidation> validations = new ArrayList<>();
    int index = 0;
    for (String message : messages) {
      index++;
      ValidationProfile profile = forcedProfile != null ? forcedProfile : detectProfile(message);
      if (!message.startsWith("MSH")) {
        validations.add(MessageValidation.notValidated(index, message, profile, null,
            "The message does not start with an MSH segment.", false));
        continue;
      }
      if (profile == null) {
        validations.add(MessageValidation.notValidated(index, message, null, null,
            "Could not choose a profile: MSH-21 does not name one of the supported profiles ("
                + profileKeys() + "). Set MSH-21 or choose a profile.",
            false));
        continue;
      }
      HL7Validator validator = findValidator(profile);
      try {
        validations.add(MessageValidation.completed(index, message, profile,
            validator.getSource(), validator.validate(profile, message)));
      } catch (ValidationException e) {
        validations.add(MessageValidation.notValidated(index, message, profile,
            validator.getSource(), e.getMessage(), e.isValidatorUnavailable()));
      }
    }
    return validations;
  }

  private HL7Validator findValidator(ValidationProfile profile) {
    for (HL7Validator validator : validators) {
      if (validator.getSource().getProfiles().contains(profile)) {
        return validator;
      }
    }
    throw new IllegalStateException("No validator offers profile " + profile.getKey());
  }

  private String profileKeys() {
    List<String> keys = new ArrayList<>();
    for (ValidationProfile profile : getProfiles()) {
      keys.add(profile.getKey());
    }
    return String.join(", ", keys);
  }
}
