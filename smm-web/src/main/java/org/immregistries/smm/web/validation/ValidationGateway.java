package org.immregistries.smm.web.validation;

import java.util.List;
import org.immregistries.smm.validation.MessageValidation;
import org.immregistries.smm.validation.RateLimiter;
import org.immregistries.smm.validation.ValidationProfile;
import org.immregistries.smm.validation.ValidationService;
import org.immregistries.smm.web.SmmWebConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

/**
 * The single way into message validation for the web application. It applies the size and rate
 * limits before anything is sent to the validator, so the public API and MCP endpoints cannot be
 * used to flood SMM or the validator service.
 */
public class ValidationGateway {

  private static final String CONTEXT_ATTRIBUTE = ValidationGateway.class.getName();

  private final ValidationService service;
  private final RateLimiter messageLimiter;
  private final RateLimiter requestLimiter;
  private final int maxMessages;
  private final int maxBytes;
  private final boolean trustForwardedFor;

  ValidationGateway(ValidationService service, RateLimiter messageLimiter,
      RateLimiter requestLimiter, int maxMessages, int maxBytes, boolean trustForwardedFor) {
    this.service = service;
    this.messageLimiter = messageLimiter;
    this.requestLimiter = requestLimiter;
    this.maxMessages = maxMessages;
    this.maxBytes = maxBytes;
    this.trustForwardedFor = trustForwardedFor;
  }

  public static synchronized ValidationGateway get(ServletContext context) {
    Object gateway = context.getAttribute(CONTEXT_ATTRIBUTE);
    if (gateway instanceof ValidationGateway) {
      return (ValidationGateway) gateway;
    }
    SmmWebConfig config = SmmWebConfig.get(context);
    ValidationGateway newGateway = new ValidationGateway(ValidationService.getDefault(),
        new RateLimiter(config.getValidationMessagesPerMinute(),
            config.getValidationMessagesTotalPerMinute()),
        new RateLimiter(config.getApiRequestsPerMinute(), Integer.MAX_VALUE),
        config.getValidationMaxMessages(), config.getValidationMaxBytes(),
        config.isTrustForwardedFor());
    context.setAttribute(CONTEXT_ATTRIBUTE, newGateway);
    return newGateway;
  }

  public ValidationService getService() {
    return service;
  }

  public List<ValidationProfile> getProfiles() {
    return service.getProfiles();
  }

  public int getMaxMessages() {
    return maxMessages;
  }

  public int getMaxBytes() {
    return maxBytes;
  }

  public int getMessagesPerMinute() {
    return messageLimiter.getPerClientLimit();
  }

  /**
   * @return the address used to rate limit an anonymous caller
   */
  public String getClientAddress(HttpServletRequest request) {
    if (trustForwardedFor) {
      String forwardedFor = request.getHeader("X-Forwarded-For");
      if (forwardedFor != null && !forwardedFor.isBlank()) {
        // The last address is the one the trusted proxy saw; earlier ones are client-supplied
        String[] addresses = forwardedFor.split(",");
        return addresses[addresses.length - 1].trim();
      }
    }
    return request.getRemoteAddr();
  }

  /**
   * Counts one request from a client to the public endpoints.
   *
   * @throws ValidationApiException with status 429 if the client has made too many requests
   */
  public void checkRequestRate(String client) throws ValidationApiException {
    long retryAfter = requestLimiter.tryAcquire(client, 1);
    if (retryAfter > 0) {
      throw ValidationApiException.tooManyRequests(
          "Too many requests. Try again in " + retryAfter + " seconds.", retryAfter);
    }
  }

  /**
   * Splits the text into messages, checks the limits, and validates each message.
   *
   * @param text one or more HL7 v2 messages
   * @param profileKey the profile for every message, or null or blank to detect it from MSH-21
   * @param client identifies the caller for rate limiting
   * @throws ValidationApiException if the input is unusable or a limit is reached
   */
  public List<MessageValidation> validate(String text, String profileKey, String client)
      throws ValidationApiException {
    if (text != null && text.length() > maxBytes) {
      throw ValidationApiException.tooLarge(
          "The messages are too large; the limit is " + maxBytes + " characters.");
    }
    if (profileKey != null && !profileKey.isBlank() && service.findProfile(profileKey) == null) {
      throw ValidationApiException.badRequest(
          "Unknown profile '" + profileKey + "'. Use one of: " + profileKeys() + ".");
    }
    List<String> messages = ValidationService.splitMessages(text);
    if (messages.isEmpty()) {
      throw ValidationApiException.badRequest("No HL7 message was provided.");
    }
    if (messages.size() > maxMessages) {
      throw ValidationApiException.tooLarge("Too many messages (" + messages.size()
          + "); send at most " + maxMessages + " at a time.");
    }
    long retryAfter = messageLimiter.tryAcquire(client, messages.size());
    if (retryAfter > 0) {
      throw ValidationApiException.tooManyRequests("The validation limit of "
          + messageLimiter.getPerClientLimit() + " messages a minute was reached, or the service "
          + "is busy. Try again in " + retryAfter + " seconds.", retryAfter);
    }
    return service.validate(messages, profileKey);
  }

  String profileKeys() {
    StringBuilder keys = new StringBuilder();
    for (ValidationProfile profile : service.getProfiles()) {
      keys.append(keys.length() == 0 ? "" : ", ").append(profile.getKey());
    }
    return keys.toString();
  }
}
