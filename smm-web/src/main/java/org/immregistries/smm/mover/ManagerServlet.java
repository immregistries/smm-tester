package org.immregistries.smm.mover;

import static org.immregistries.smm.web.SmmPage.escapeHtml;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.immregistries.smm.tester.ClientServlet;
import org.immregistries.smm.tester.connectors.Connector;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManagerServlet extends ClientServlet {

  public static final String INIT_PARAM_KEY_STORE_PASSWORD = "keyStorePassword";
  public static final String INIT_PARAM_SUN_SECURITY_SSL_ALLOW_UNSAFE_RENEGOTIATION =
      "sun.security.ssl.allowUnsafeRenegotiation";
  public static final String INIT_PARAM_KEY_STORE = "keyStore";
  public static final String INIT_PARAM_SUPPORT_CENTER_CODE = "support_center.code";
  public static final String INIT_PARAM_SUPPORT_CENTER_URL = "support_center.url";
  public static final String INIT_PARAM_FOLDER_SCAN_ENABLED = "folderScanEnabled";
  public static final String INIT_PARAM_SCAN_START_FOLDERS = "scan.start.folders";

  private static ConnectionManager connectionManager = null;

  public static ConnectionManager getConnectionManager() {
    return connectionManager;
  }

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public void init() throws ServletException {
    super.init();
    if (connectionManager == null) {
      initializeConnectionManager();
    }
  }

  private synchronized void initializeConnectionManager() {
    if (connectionManager != null) {
      return;
    }
    connectionManager = new ConnectionManager();
    connectionManager.setScanStartFolders(getInitParameter(INIT_PARAM_SCAN_START_FOLDERS));
    System.out.println("SMM Initializing Manager Servlet");
    String checkIntervalInSeconds = getInitParameter("checkIntervalInSeconds");
    if (checkIntervalInSeconds != null) {
      long checkInterval = Long.parseLong(checkIntervalInSeconds);
      if (checkInterval <= 0) {
        checkInterval = 5;
      }
      ConnectionManager.setCheckInterval(checkInterval);
    }
    String folderScanEnabled = getInitParameter(INIT_PARAM_FOLDER_SCAN_ENABLED);
    if (folderScanEnabled != null) {
      ConnectionManager.setScanDirectories(!folderScanEnabled.equalsIgnoreCase("false"));
    }
    ConnectionManager.setSupportCenterCode(getInitParameter(INIT_PARAM_SUPPORT_CENTER_CODE));
    ConnectionManager.setSupportCenterUrl(getInitParameter(INIT_PARAM_SUPPORT_CENTER_URL));


    connectionManager.setKeyStore(getInitParameter(INIT_PARAM_KEY_STORE));
    connectionManager.setKeyStorePassword(getInitParameter(INIT_PARAM_KEY_STORE_PASSWORD));

    {
      String s = getInitParameter(INIT_PARAM_SUN_SECURITY_SSL_ALLOW_UNSAFE_RENEGOTIATION);
      connectionManager.setSunSecuritySslAllowUnsafeRenegotiation(s != null && s.equalsIgnoreCase("true"));
    }

    ShutdownInterceptor shutdownInterceptor = new ShutdownInterceptor();
    Runtime.getRuntime().addShutdownHook(shutdownInterceptor);
    connectionManager.init();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    try {
      printHtmlHead(out, "Simple Message Mover", request);
      printPageHeader(out, "Message Mover Status",
          "Data folders the Simple Message Mover is watching and sending from.");
      out.println("<div class=\"aira-stack\">");
      if (ConnectionManager.getFolderScanner() != null) {
        out.println("<section class=\"aira-panel\">");
        out.println("  <div class=\"aira-panel__header\">"
            + "<h2 class=\"aira-panel__title\">Automatic Data Folder Scanning</h2></div>");
        out.println("  <div class=\"aira-panel__body\">");
        if (ConnectionManager.getFolderScanner().isScanning()) {
          out.println("    <p><span class=\"aira-badge aira-badge--warning\">Scanning</span>"
              + " Scanner is currently looking for data folders.</p>");
        }
        out.println("    <p>Scan status: "
            + escapeHtml(ConnectionManager.getFolderScanner().getScanningStatus()) + "</p>");
        out.println("  </div>");
        out.println("</section>");
      }
      out.println("<section class=\"aira-table-panel\">");
      out.println("  <div class=\"aira-table-panel__header\"><div>"
          + "<h2 class=\"aira-table-panel__title\">Send Data Folders</h2></div></div>");
      out.println("  <div class=\"aira-table-wrap\">");
      out.println("    <table class=\"aira-table\">");
      out.println("      <caption class=\"aira-visually-hidden\">Send data folders</caption>");
      out.println("      <thead><tr><th scope=\"col\">Label</th><th scope=\"col\">Status</th>"
          + "<th scope=\"col\">Folder</th></tr></thead>");
      out.println("      <tbody>");
      if (ConnectionManager.getSendDataSet().isEmpty()) {
        out.println("        <tr><td colspan=\"3\" class=\"aira-table__empty\">No data folders."
            + "</td></tr>");
      }
      for (SendData sendData : ConnectionManager.getSendDataSet()) {
        out.println("        <tr>");
        Connector connector = sendData.getConnector();
        out.println("          <th scope=\"row\" class=\"aira-table__cell--primary\">"
            + (connector == null ? "-" : escapeHtml(connector.getLabelDisplay())) + "</th>");
        out.println("          <td>" + escapeHtml(String.valueOf(sendData.getScanStatus()))
            + "</td>");
        out.println("          <td class=\"aira-table__cell--code\">"
            + escapeHtml(String.valueOf(sendData.getRootDir())) + "</td>");
        out.println("        </tr>");
      }
      out.println("      </tbody>");
      out.println("    </table>");
      out.println("  </div>");
      out.println("</section>");
      out.println("</div>");
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    // TODO Auto-generated method stub
    super.doPost(req, resp);
  }
}
