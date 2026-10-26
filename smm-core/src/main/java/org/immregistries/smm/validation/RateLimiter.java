package org.immregistries.smm.validation;

import java.util.HashMap;
import java.util.Map;
import java.util.function.LongSupplier;

/**
 * Limits how much work each client, and all clients together, can ask for in each one-minute
 * window. It keeps the public validation endpoints from being used to flood SMM or the validator.
 */
public class RateLimiter {

  private static final long WINDOW_MILLIS = 60_000;

  private final int perClientLimit;
  private final int totalLimit;
  private final LongSupplier clock;
  private final Map<String, Integer> clientCounts = new HashMap<>();
  private long windowStart;
  private int totalCount;

  /**
   * @param perClientLimit the most units one client may use in a minute
   * @param totalLimit the most units all clients together may use in a minute
   */
  public RateLimiter(int perClientLimit, int totalLimit) {
    this(perClientLimit, totalLimit, System::currentTimeMillis);
  }

  RateLimiter(int perClientLimit, int totalLimit, LongSupplier clock) {
    this.perClientLimit = perClientLimit;
    this.totalLimit = totalLimit;
    this.clock = clock;
    this.windowStart = clock.getAsLong();
  }

  /**
   * Uses units for a client if both its own limit and the overall limit allow it. Nothing is used
   * when the request is refused.
   *
   * @param client identifies the caller, for example its IP address
   * @param units how much work the request needs, for example the number of messages
   * @return 0 if the request is allowed, otherwise the number of seconds until it can be retried
   */
  public synchronized long tryAcquire(String client, int units) {
    long now = clock.getAsLong();
    if (now - windowStart >= WINDOW_MILLIS) {
      windowStart = now - (now - windowStart) % WINDOW_MILLIS;
      clientCounts.clear();
      totalCount = 0;
    }
    int clientCount = clientCounts.getOrDefault(client, 0);
    if (clientCount + units > perClientLimit || totalCount + units > totalLimit) {
      return Math.max(1, (windowStart + WINDOW_MILLIS - now + 999) / 1000);
    }
    clientCounts.put(client, clientCount + units);
    totalCount += units;
    return 0;
  }

  public int getPerClientLimit() {
    return perClientLimit;
  }

  public int getTotalLimit() {
    return totalLimit;
  }
}
