package org.immregistries.smm.web.auth;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class DevLoginAuthenticationProviderTest {

  @Test
  public void localPathKeepsOnlyPathInsideApplication() {
    assertEquals("/smm/ManualQueryServlet?a=1",
        DevLoginAuthenticationProvider.toLocalPath("http://localhost:8080/smm/ManualQueryServlet?a=1", "/smm"));
    assertEquals("/smm", DevLoginAuthenticationProvider.toLocalPath("http://localhost:8080/smm", "/smm"));
  }

  @Test
  public void otherSitesAndOtherAppsAreRejected() {
    assertNull(DevLoginAuthenticationProvider.toLocalPath("http://evil.example/other", "/smm"));
    assertNull(DevLoginAuthenticationProvider.toLocalPath("http://localhost:8080/smmx/page", "/smm"));
    assertNull(DevLoginAuthenticationProvider.toLocalPath("", "/smm"));
    assertNull(DevLoginAuthenticationProvider.toLocalPath(null, "/smm"));
    assertNull(DevLoginAuthenticationProvider.toLocalPath("not a url ::", "/smm"));
  }
}
