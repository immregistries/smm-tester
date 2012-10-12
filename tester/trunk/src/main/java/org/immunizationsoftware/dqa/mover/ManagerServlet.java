package org.immunizationsoftware.dqa.mover;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.immunizationsoftware.dqa.tester.ClientServlet;
import org.immunizationsoftware.dqa.tester.connectors.Connector;

public class ManagerServlet extends ClientServlet
{

  public static final String STANDARD_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
  public static final String STANDARD_TIME_FORMAT = "HH:mm:ss";
  
  private static long checkInterval = 15;
  private static Set<SendData> sendDataSet = new HashSet<SendData>();
  private static String systemId = "";
  private static InetAddress localHostIp;
  private static byte[] localHostMac;
  private static Date startDate;
  private static String randomId = "";

  public static long getCheckInterval()
  {
    return checkInterval;
  }
  
  public static Set<SendData> getSendDataSet()
  {
    return sendDataSet;
  }

  public static String getRandomId()
  {
    return randomId;
  }

  public static Date getStartDate()
  {
    return startDate;
  }

  public static String getSystemId()
  {
    return systemId;
  }

  public static InetAddress getLocalHostIp()
  {
    return localHostIp;
  }

  public static byte[] getLocalHostMac()
  {
    return localHostMac;
  }

  protected static void registerFolder(File folder)
  {
    SendData sendData = new SendData(folder);
    sendDataSet.add(sendData);
    sendData.start();
  }

  private static FolderScanner folderScanner = null;

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Override
  public void init() throws ServletException
  {
    super.init();
    synchronized (sendDataSet)
    {
      if (folderScanner == null)
      {
        initializeManagerSettings();
      }
    }
  }

  private void initializeManagerSettings()
  {
    {
      // Set a unique system id by combining start time, a random 4 digit
      // number, the local ip, and the mac address
      startDate = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
      Random random = new Random();
      randomId = "" + (random.nextInt(9000) + 1000);
      systemId = "date" + sdf.format(startDate) + ":rand" + randomId + getIpMacAddress();
    }
    String scanStartFolders = getInitParameter("scan.start.folders");
    if (scanStartFolders != null)
    {
      String[] scanStartFolderNames = scanStartFolders.split("\\;");
      List<File> foldersToScan = new ArrayList<File>();
      for (String scanStartFolderName : scanStartFolderNames)
      {
        if (scanStartFolderName != null)
        {
          scanStartFolderName = scanStartFolderName.trim();
          if (scanStartFolderName.length() > 0)
          {
            File scanStartFile = new File(scanStartFolderName);
            if (scanStartFile.exists() && scanStartFile.isDirectory())
            {
              foldersToScan.add(scanStartFile);
            }
          }
        }
      }
      if (scanStartFolders.length() > 0)
      {
        folderScanner = new FolderScanner(foldersToScan);
        folderScanner.start();
      }
    }
  }

  private static String getIpMacAddress()
  {
    StringBuilder sb = new StringBuilder();
    try
    {
      localHostIp = InetAddress.getLocalHost();
      sb.append(":ip");
      sb.append(localHostIp.getHostAddress());
      NetworkInterface network = NetworkInterface.getByInetAddress(localHostIp);
      localHostMac = network.getHardwareAddress();
      sb.append(":mac");
      for (int i = 0; i < localHostMac.length; i++)
      {
        sb.append(String.format("%02X%s", localHostMac[i], (i < localHostMac.length - 1) ? "-" : ""));
      }
    } catch (UnknownHostException e)
    {
      sb.append(":ip{UnknownHostException:" + e.getMessage() + "}");
    } catch (SocketException e)
    {
      sb.append(":ip{SocketException:" + e.getMessage() + "}");
    }
    return sb.toString();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
  {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();
    try
    {
      printHtmlHead(out, "Simple Message Mover", request);
      out.println("<h1>Simple Message Mover</h1>");
      if (folderScanner != null)
      {
        out.println("<h3>Automatic Data Folder Scanning</h3>");
        if (folderScanner.isScanning())
        {
          out.println("<p><font color=\"red\">Scanner is currently looking for data folders.</font></p>");
        }
        out.println("<p>Scan status: " + folderScanner.getScanningStatus() + "</p>");
      }
      out.println("");
      out.println("<h3>Send Data Folders</h3>");
      out.println("<table>");
      out.println("  <tr>");
      out.println("    <th>Label</th>");
      out.println("    <th>Status</th>");
      out.println("    <th>Folder</th>");
      out.println("  </tr>");
      for (SendData sendData : sendDataSet)
      {
        out.println("  <tr>");
        Connector connector = sendData.getConnector();
        if (connector != null)
        {
          out.println("    <td>" + connector.getLabelDisplay() + "</td>");
        } else
        {
          out.println("    <td>-</td>");
        }
        out.println("    <td>" + sendData.getScanStatus() + "</td>");
        out.println("    <td>" + sendData.getRootDir() + "</td>");
        out.println("  </tr>");
      }
      out.println("</table>");
      printHtmlFoot(out);
    } finally
    {
      out.close();
    }
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
  {
    // TODO Auto-generated method stub
    super.doPost(req, resp);
  }
}
