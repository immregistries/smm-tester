package org.immregistries.smm.mover.install;

import java.io.PrintWriter;
import org.immregistries.smm.tester.connectors.ConnectorFactory;

public class ConnectionConfiguration {
  public static final String FIELD_LABEL = "label";
  public static final String FIELD_TYPE = "type";
  public static final String FIELD_URL = "url";
  public static final String FIELD_USERID = "userid";
  public static final String FIELD_OTHERID = "otherid";
  public static final String FIELD_PASSWORD = "password";
  public static final String FIELD_FACILITYID = "faclityid";
  public static final String FIELD_KEY_STORE_PASSWORD = "keyStorePassword";
  public static final String FIELD_ENABLE_TIME_START = "enableTimeStart";
  public static final String FIELD_ENABLE_TIME_END = "enableTimeEnd";

  private String label = "";
  private String type = "";
  private String url = "";
  private String userid = "";
  private String otherid = "";
  private String password = "";
  private String facilityid = "";
  private String baseDir = "";
  private String folderName = "";
  private String instructions = "";
  private String receiverName = "the receiver";
  private String keyStorePassword = "";
  private String enableTimeEnd = "";
  private String enableTimeStart = "";

  public String getKeyStorePassword() {
    return keyStorePassword;
  }

  public void setKeyStorePassword(String keyStorePassword) {
    this.keyStorePassword = keyStorePassword;
  }

  public String getOtherid() {
    return otherid;
  }

  public void setOtherid(String otherid) {
    this.otherid = otherid;
  }


  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  public String getInstructions() {
    return instructions;
  }

  public void setInstructions(String instructions) {
    this.instructions = instructions;
  }

  public String getBaseDir() {
    return baseDir;
  }

  public void setBaseDir(String baseDir) {
    this.baseDir = baseDir;
  }

  public String getFolderName() {
    return folderName;
  }

  public void setFolderName(String folderName) {
    this.folderName = folderName;
  }

  public String getEnableTimeEnd() {
    return enableTimeEnd;
  }

  public void setEnableTimeEnd(String enableTimeEnd) {
    this.enableTimeEnd = enableTimeEnd;
  }

  public String getEnableTimeStart() {
    return enableTimeStart;
  }

  public void setEnableTimeStart(String enableTimeStart) {
    this.enableTimeStart = enableTimeStart;
  }

  private boolean typeShow = true;
  private boolean urlShow = true;
  private boolean useridShow = true;
  private boolean otheridShow = false;
  private boolean passwordShow = true;
  private boolean facilityidShow = true;
  private boolean useridRequired = false;
  private boolean otheridRequired = false;
  private boolean enableTimeShow = false;
  private boolean enableTimeRequired = false;
  private boolean keyStorePasswordShow = false;
  private boolean keyStorePasswordRequired = false;
  private String urlLabel = "URL";
  private String useridLabel = "User Id";
  private String otheridLabel = "Other Id";
  private String passwordLabel = "Password";
  private String facilityidLabel = "Facility Id";
  private String keyStorePasswordLabel = "Key Store Password";

  public boolean isKeyStorePasswordShow() {
    return keyStorePasswordShow;
  }

  public void setKeyStorePasswordShow(boolean keyStorePasswordShow) {
    this.keyStorePasswordShow = keyStorePasswordShow;
  }

  public boolean isKeyStorePasswordRequired() {
    return keyStorePasswordRequired;
  }

  public void setKeyStorePasswordRequired(boolean keyStorePasswordRequired) {
    this.keyStorePasswordRequired = keyStorePasswordRequired;
  }

  public String getKeyStorePasswordLabel() {
    return keyStorePasswordLabel;
  }

  public void setKeyStorePasswordLabel(String keyStorePasswordLabel) {
    this.keyStorePasswordLabel = keyStorePasswordLabel;
  }

  public boolean isOtheridShow() {
    return otheridShow;
  }

  public void setOtheridShow(boolean otheridShow) {
    this.otheridShow = otheridShow;
  }

  public boolean isOtheridRequired() {
    return otheridRequired;
  }

  public void setOtheridRequired(boolean otheridRequired) {
    this.otheridRequired = otheridRequired;
  }

  public String getOtheridLabel() {
    return otheridLabel;
  }

  public void setOtheridLabel(String otheridLabel) {
    this.otheridLabel = otheridLabel;
  }

  public boolean isEnableTimeShow() {
    return enableTimeShow;
  }

  public void setEnableTimeShow(boolean enableTimeShow) {
    this.enableTimeShow = enableTimeShow;
  }

  public boolean isEnableTimeRequired() {
    return enableTimeRequired;
  }

  public void setEnableTimeRequired(boolean enableTimeRequired) {
    this.enableTimeRequired = enableTimeRequired;
  }

  public String getUrlLabel() {
    return urlLabel;
  }

  public void setUrlLabel(String urlLabel) {
    this.urlLabel = urlLabel;
  }

  public String getUseridLabel() {
    return useridLabel;
  }

  public void setUseridLabel(String useridLabel) {
    this.useridLabel = useridLabel;
  }

  public String getPasswordLabel() {
    return passwordLabel;
  }

  public void setPasswordLabel(String passwordLabel) {
    this.passwordLabel = passwordLabel;
  }

  public String getFacilityidLabel() {
    return facilityidLabel;
  }

  public void setFacilityidLabel(String facilityidLabel) {
    this.facilityidLabel = facilityidLabel;
  }

  public boolean isUseridRequired() {
    return useridRequired;
  }

  public void setUseridRequired(boolean useridRequired) {
    this.useridRequired = useridRequired;
  }

  public boolean isPasswordRequired() {
    return passwordRequired;
  }

  public void setPasswordRequired(boolean passwordRequired) {
    this.passwordRequired = passwordRequired;
  }

  public boolean isFacilityidRequired() {
    return facilityidRequired;
  }

  public void setFacilityidRequired(boolean facilityidRequired) {
    this.facilityidRequired = facilityidRequired;
  }

  private boolean passwordRequired = false;
  private boolean facilityidRequired = false;

  public boolean isTypeShow() {
    return typeShow;
  }

  public void setTypeShow(boolean typeShow) {
    this.typeShow = typeShow;
  }

  public boolean isUrlShow() {
    return urlShow;
  }

  public void setUrlShow(boolean urlShow) {
    this.urlShow = urlShow;
  }

  public boolean isUseridShow() {
    return useridShow;
  }

  public void setUseridShow(boolean useridShow) {
    this.useridShow = useridShow;
  }

  public boolean isPasswordShow() {
    return passwordShow;
  }

  public void setPasswordShow(boolean passwordShow) {
    this.passwordShow = passwordShow;
  }

  public boolean isFacilityidShow() {
    return facilityidShow;
  }

  public void setFacilityidShow(boolean facilityidShow) {
    this.facilityidShow = facilityidShow;
  }

  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUserid() {
    return userid;
  }

  public void setUserid(String userid) {
    this.userid = userid;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getFacilityid() {
    return facilityid;
  }

  public void setFacilityid(String facilityid) {
    this.facilityid = facilityid;
  }

  public void printForm(PrintWriter out) {
    if (!instructions.equals("")) {
      out.println("<p>" + instructions + "</p>");
    }
    out.println("<form action=\"ConfigureServlet\" method=\"GET\">");
    out.println("  <table class=\"boxed\">");
    out.println("  <tr class=\"boxed\">");
    out.println("    <th class=\"boxed\">Label</th>");
    out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_LABEL
        + "\" size=\"20\" value=\"" + label + "\"></td>");
    out.println(
        "    <td class=\"boxed\">A short description for this connection that you will recognize.</td>");
    out.println("  </tr>");
    if (typeShow) {
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">Type</th>");
      out.println("    <td class=\"boxed\">");
      out.println("      <select name=\"" + FIELD_TYPE + "\">");
      out.println("        <option value=\"\">select</option>");
      for (String[] option : ConnectorFactory.TYPES) {
        out.println("        <option value=\"" + option[0] + "\""
            + (type.equals(option[0]) ? " selected=\"true\"" : "") + ">" + option[1] + "</option>");
      }
      out.println("      </select>");
      out.println("    </td>");
      out.println("    <td class=\"boxed\">The transport method used.</td>");
      out.println("  </tr>");
    } else {
      out.println("<input type=\"hidden\" name=\"" + FIELD_TYPE + "\" value=\"" + type + "\">");
    }
    if (urlShow) {
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">" + urlLabel + "</th>");
      out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_URL
          + "\" size=\"40\" value=\"" + url + "\"></td>");
      out.println("    <td class=\"boxed\">The end point where the data will be sent.</td>");
      out.println("  </tr>");
    } else {
      out.println("<input type=\"hidden\" name=\"" + FIELD_URL + "\" value=\"" + url + "\">");
    }
    if (useridShow) {
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">" + useridLabel + "</th>");
      out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_USERID
          + "\" size=\"10\" value=\"" + userid + "\"></td>");
      out.println(
          "    <td class=\"boxed\">The unique identifier supplied by " + receiverName + ".</td>");
      out.println("  </tr>");
    } else {
      out.println("<input type=\"hidden\" name=\"" + FIELD_USERID + "\" value=\"" + userid + "\">");
    }
    if (passwordShow) {
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">" + passwordLabel + "</th>");
      out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_PASSWORD
          + "\" size=\"10\" value=\"" + password + "\"></td>");
      out.println(
          "    <td class=\"boxed\">The secret password assigned by " + receiverName + ".</td>");
      out.println("  </tr>");
    } else {
      out.println(
          "<input type=\"hidden\" name=\"" + FIELD_PASSWORD + "\" value=\"" + password + "\">");
    }
    if (facilityidShow) {
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">" + facilityidLabel + "</th>");
      out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_FACILITYID
          + "\" size=\"10\" value=\"" + facilityid + "\"></td>");
      out.println(
          "    <td class=\"boxed\">The identifier for the facility assigned by the receiver. </td>");
      out.println("  </tr>");
    } else {
      out.println(
          "<input type=\"hidden\" name=\"" + FIELD_FACILITYID + "\" value=\"" + facilityid + "\">");
    }
    if (otheridShow) {
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">" + otheridLabel + "</th>");
      out.println("    <td class=\"boxed\"><input type=\"text\" name=\"" + FIELD_OTHERID
          + "\" size=\"10\" value=\"" + otherid + "\"></td>");
      out.println("    <td class=\"boxed\">Another unique identifier supplied by " + receiverName
          + ".</td>");
      out.println("  </tr>");
    } else {
      out.println(
          "<input type=\"hidden\" name=\"" + FIELD_OTHERID + "\" value=\"" + otherid + "\">");
    }
    if (keyStorePasswordShow) {
      out.println("  <tr class=\"boxed\">");
      out.println("    <th class=\"boxed\">" + keyStorePasswordLabel + "</th>");
      out.println("    <td class=\"boxed\">");
      out.println("      <input type=\"text\" name=\"" + FIELD_KEY_STORE_PASSWORD
          + "\" size=\"10\" value=\"" + keyStorePassword + "\"> ");
      out.println("    </td>");
      out.println("    <td class=\"boxed\">The password for the keystore file. </td>");
      out.println("  </tr>");
    }
    out.println("  <tr class=\"boxed\">");
    out.println("    <th class=\"boxed\">&nbsp;</th>");
    out.println(
        "    <td class=\"boxed\"><input type=\"submit\" name=\"action\" value=\"Step 2: Download and Save\"></td>");
    if (baseDir.equals("") && folderName.equals("")) {
      out.println(
          "    <td class=\"boxed\">Download and save in the <a href=\"https://openimmunizationsoftware.net/interfacing/smm/installation.html#IISTransferFolder\" target=\"blank\">IIS Transfer Folder</a>.</td>");
    } else if (!baseDir.equals("") && !folderName.equals("")) {
      out.println(
          "    <td class=\"boxed\">Download and save in: " + baseDir + folderName + " </td>");
    } else if (!folderName.equals("")) {
      out.println(
          "    <td class=\"boxed\">Download and save in the <a href=\"https://openimmunizationsoftware.net/interfacing/smm/installation.html#IISTransferFolder\" target=\"blank\">IIS Transfer Folder</a>: "
              + folderName + " </td>");
    } else {
      out.println(
          "    <td class=\"boxed\">Download and save in the IIS Tranfer Folder in the <a href=\"https://openimmunizationsoftware.net/interfacing/smm/installation.html#SSMRootFolder\" target=\"blank\">SMM Root Folder</a>: "
              + baseDir + " </td>");
    }
    out.println("  </tr>");
    out.println(" </table>");
    out.println("</form>");

  }
}
