package org.immregistries.smm.web;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * The application's navigation: five primary areas across the top, and in each area a right rail
 * listing that area's pages. Pages are identified by their servlet path.
 */
public final class SmmNavigation {

  public enum Area {
    HOME("Home", "/HomeServlet"),
    CONNECT("Connect", "/ConnectServlet"),
    SEND("Send", "/SubmitServlet"),
    TEST_CASES("Test Cases", "/SetupServlet"),
    TOOLS("Tools", "/ModifyMessageServlet"),
    /** Pages for administrators, reached from a header action instead of the navigation. */
    ADMIN("Admin", "/smm");

    private final String label;
    private final String href;

    Area(String label, String href) {
      this.label = label;
      this.href = href;
    }

    public String getLabel() {
      return label;
    }

    public String getHref() {
      return href;
    }

    public boolean isPrimary() {
      return this != ADMIN;
    }
  }

  public static final class Page {
    private final String path;
    private final String label;
    private final Area area;
    private final boolean inRail;
    private final boolean needsMoverConnection;

    private Page(String path, String label, Area area, boolean inRail,
        boolean needsMoverConnection) {
      this.path = path;
      this.label = label;
      this.area = area;
      this.inRail = inRail;
      this.needsMoverConnection = needsMoverConnection;
    }

    public String getPath() {
      return path;
    }

    public String getLabel() {
      return label;
    }

    public Area getArea() {
      return area;
    }

    /**
     * @return true if the page is listed in its area's right rail; pages reached from another
     *         page, such as a test case message, are not
     */
    public boolean isInRail() {
      return inRail;
    }

    /**
     * @return true if the page only works when the session is attached to one of SMM's
     *         folder-based mover connections
     */
    public boolean isNeedsMoverConnection() {
      return needsMoverConnection;
    }
  }

  private static final Map<String, Page> PAGES = new LinkedHashMap<>();

  static {
    add("/HomeServlet", "Home", Area.HOME, false);

    add("/ConnectServlet", "Connect to IIS", Area.CONNECT, true);
    add("/InstallCertServlet", "Install Certificate", Area.CONNECT, true, true);

    add("/SubmitServlet", "Send Message", Area.SEND, true);
    add("/ManualQueryServlet", "Query IIS", Area.SEND, true);
    add("/BulkQueryServlet", "Bulk Query", Area.SEND, true, true);

    add("/SetupServlet", "Manage Test Cases", Area.TEST_CASES, true);
    add("/CreateTestCaseServlet", "Edit Test Case", Area.TEST_CASES, true);
    add("/testCase", "Run Tests", Area.TEST_CASES, true);
    add("/TestCaseMessageViewerServlet", "Test Case Message", Area.TEST_CASES, false);

    add("/ModifyMessageServlet", "Modify Message", Area.TOOLS, true);
    add("/MessageViewerServlet", "Message Viewer", Area.TOOLS, true);
    add("/GenerateDataServlet", "Generate Data", Area.TOOLS, true);
    add("/GenerateExamplesServlet", "Generate Examples", Area.TOOLS, true);
    add("/interfaceProfile", "Profile Interface", Area.TOOLS, true);
    add("/StressTestServlet", "Stress Test", Area.TOOLS, true);
    add("/wsdl-demo", "CDC WSDL Demo", Area.TOOLS, true);
    add("/wsdl", "CDC WSDL Test Server", Area.TOOLS, false);
    add("/CatchServlet", "Message Catcher", Area.TOOLS, false);

    add("/smm", "Message Mover", Area.ADMIN, true);
  }

  private SmmNavigation() {}

  private static void add(String path, String label, Area area, boolean inRail) {
    add(path, label, area, inRail, false);
  }

  private static void add(String path, String label, Area area, boolean inRail,
      boolean needsMoverConnection) {
    PAGES.put(path, new Page(path, label, area, inRail, needsMoverConnection));
  }

  /**
   * @return the page registered for a servlet path, or null if the path is not a known page
   */
  public static Page findPage(String servletPath) {
    return PAGES.get(servletPath);
  }

  /**
   * @param hasMoverConnection whether the session is attached to a folder-based mover connection;
   *        pages that need one are left out when it is not
   * @return the pages listed in an area's right rail, in display order
   */
  public static List<Page> getRailPages(Area area, boolean hasMoverConnection) {
    List<Page> pages = new ArrayList<>();
    for (Page page : PAGES.values()) {
      if (page.area == area && page.inRail
          && (hasMoverConnection || !page.needsMoverConnection)) {
        pages.add(page);
      }
    }
    return Collections.unmodifiableList(pages);
  }
}
