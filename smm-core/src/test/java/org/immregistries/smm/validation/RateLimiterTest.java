package org.immregistries.smm.validation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicLong;
import org.junit.Test;

public class RateLimiterTest {

  private final AtomicLong now = new AtomicLong(1_000_000);

  @Test
  public void limitsEachClient() {
    RateLimiter limiter = new RateLimiter(5, 100, now::get);
    assertEquals(0, limiter.tryAcquire("a", 3));
    assertEquals(0, limiter.tryAcquire("a", 2));
    assertTrue(limiter.tryAcquire("a", 1) > 0);
    // Another client still has its own allowance
    assertEquals(0, limiter.tryAcquire("b", 5));
  }

  @Test
  public void limitsAllClientsTogether() {
    RateLimiter limiter = new RateLimiter(5, 8, now::get);
    assertEquals(0, limiter.tryAcquire("a", 5));
    assertTrue(limiter.tryAcquire("b", 4) > 0);
    assertEquals(0, limiter.tryAcquire("b", 3));
  }

  @Test
  public void refusedRequestUsesNothing() {
    RateLimiter limiter = new RateLimiter(5, 100, now::get);
    assertEquals(0, limiter.tryAcquire("a", 4));
    assertTrue(limiter.tryAcquire("a", 2) > 0);
    assertEquals(0, limiter.tryAcquire("a", 1));
  }

  @Test
  public void resetsEachMinuteAndReportsWait() {
    RateLimiter limiter = new RateLimiter(2, 100, now::get);
    assertEquals(0, limiter.tryAcquire("a", 2));
    now.addAndGet(45_000);
    assertEquals(15, limiter.tryAcquire("a", 1));
    now.addAndGet(15_000);
    assertEquals(0, limiter.tryAcquire("a", 2));
  }
}
