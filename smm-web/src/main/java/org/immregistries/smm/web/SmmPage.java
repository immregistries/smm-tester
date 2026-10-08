package org.immregistries.smm.web;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import org.immregistries.aira.web.AiraAccountConfig;
import org.immregistries.aira.web.AiraContextConfig;
import org.immregistries.aira.web.AiraDefaults;
import org.immregistries.aira.web.AiraLogo;
import org.immregistries.aira.web.AiraNavigationItem;
import org.immregistries.aira.web.AiraPage;
import org.immregistries.smm.SoftwareVersion;
import org.immregistries.smm.web.SmmNavigation.Area;
import org.immregistries.smm.web.SmmNavigation.Page;
import org.immregistries.smm.web.auth.SmmSession;
import org.immregistries.smm.web.auth.SmmUser;
import jakarta.servlet.http.HttpServletRequest;

/**
 * Writes the AIRA Web application shell around SMM page content: the global header, the five
 * navigation areas, a right rail with the current area's pages, and the required footer.
 * <p>
 * Call {@link #writeStart} before the page content and {@link #writeEnd} after it, from the same
 * request thread.
 */
public final class SmmPage {

  public static final String APPLICATION_NAME = "Simple Message Mover";
  public static final String APPLICATION_SUBTITLE = "Connection and Testing Tools";
  public static final String STYLESHEET = "/css/smm.css";

  private static final ThreadLocal<OpenPage> OPEN_PAGE = new ThreadLocal<>();

  private static final class OpenPage {
    private final AiraPage airaPage;
    private final Area area;
    private final Page page;
    private final String contextPath;
    private final List<Page> railPages;

    private OpenPage(AiraPage airaPage, Area area, Page page, String contextPath,
        List<Page> railPages) {
      this.airaPage = airaPage;
      this.area = area;
      this.page = page;
      this.contextPath = contextPath;
      this.railPages = railPages;
    }
  }

  private SmmPage() {}

  /**
   * Writes the document start, shell, and the opening of the content area.
   *
   * @param title the title to use when the page is not one of the known navigation pages
   */
  public static void writeStart(PrintWriter out, HttpServletRequest request, String title) {
    Page page = SmmNavigation.findPage(request.getServletPath());
    Area area = page == null ? Area.HOME : page.getArea();
    String pageTitle = page == null ? title : page.getLabel();

    AiraPage airaPage = buildAiraPage(request, area, pageTitle);
    SmmUser user = SmmSession.getUser(request);
    List<Page> railPages =
        SmmNavigation.getRailPages(area, user != null && user.hasSendData());
    airaPage.writeStart(out);
    out.println("    <div class=\"aira-container--wide\">");
    if (!railPages.isEmpty()) {
      out.println("    <div class=\"aira-right-rail-layout\">");
      out.println("    <div class=\"smm-content\">");
    }
    OPEN_PAGE.set(new OpenPage(airaPage, area, page, request.getContextPath(), railPages));
  }

  /**
   * Writes the end of the content area, the area's right rail, the footer, and the document end.
   */
  public static void writeEnd(PrintWriter out) {
    OpenPage open = OPEN_PAGE.get();
    OPEN_PAGE.remove();
    if (open == null) {
      return;
    }
    if (!open.railPages.isEmpty()) {
      out.println("    </div>");
      writeRail(out, open);
      out.println("    </div>");
    }
    out.println("    </div>");
    open.airaPage.writeEnd(out);
  }

  /**
   * Builds the shell for pages that write their own content layout, such as the sign-in problem
   * page.
   */
  public static AiraPage buildAiraPage(HttpServletRequest request, Area area, String pageTitle) {
    SmmUser user = SmmSession.getUser(request);
    AiraPage.Builder builder = AiraPage.builder().applicationName(APPLICATION_NAME)
        .applicationSubtitle(APPLICATION_SUBTITLE).applicationVersion(SoftwareVersion.VERSION)
        .documentTitle(pageTitle + " - " + APPLICATION_NAME).contextPath(request.getContextPath())
        .identityHref(Area.HOME.getHref())
        .logo(new AiraLogo(AiraDefaults.DEFAULT_LOGO_PATH, AiraDefaults.DEFAULT_LOGO_ALT_TEXT))
        .addLocalStylesheet(STYLESHEET);
    if (user != null && user.isAdmin()) {
      builder.addGlobalAction(Area.ADMIN.getLabel(), Area.ADMIN.getHref(), "secondary");
    }
    if (user != null) {
      builder.account(new AiraAccountConfig(user.getName(), "Sign out", "/logout"));
    } else {
      builder.account(new AiraAccountConfig(null, "Sign in", "/login"));
    }
    List<AiraNavigationItem> items = new ArrayList<>();
    for (Area primary : Area.values()) {
      if (primary.isPrimary()) {
        items.add(new AiraNavigationItem(primary.getLabel(), primary.getHref(), primary == area));
      }
    }
    builder.context(new AiraContextConfig(area.getLabel(), items));
    return builder.build();
  }

  /**
   * Escapes text for use in HTML content or a quoted attribute value.
   */
  public static String escapeHtml(String value) {
    if (value == null) {
      return "";
    }
    return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        .replace("\"", "&quot;").replace("'", "&#39;");
  }

  private static void writeRail(PrintWriter out, OpenPage open) {
    out.println("    <aside class=\"aira-right-rail\" aria-label=\"" + open.area.getLabel()
        + " pages\">");
    out.println("      <section class=\"aira-sidebar-section\">");
    out.println("        <h2 class=\"aira-sidebar-title\">" + open.area.getLabel() + "</h2>");
    out.println("        <nav class=\"aira-sidebar-nav\">");
    for (Page railPage : open.railPages) {
      boolean current = open.page != null && open.page.getPath().equals(railPage.getPath());
      out.println("          <a class=\"aira-sidebar-link\" href=\"" + open.contextPath
          + railPage.getPath() + (railPage.getPath().equals("/wsdl-demo") ? "/" : "") + "\""
          + (current ? " aria-current=\"page\"" : "") + ">" + railPage.getLabel() + "</a>");
    }
    out.println("        </nav>");
    out.println("      </section>");
    out.println("    </aside>");
  }
}
