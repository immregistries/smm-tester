package org.immregistries.smm.tester;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class CatchServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    System.out.println("== POST called ==========================================");
    dumpDetails(req);
    String contentType = req.getHeader("content-type");
    if (contentType.contains("urn:cdc:iisb:2011:submitSingleMessage")) {
      System.out.println("Detected submit single message, returning canned response");
      PrintWriter out = resp.getWriter();
      try {
        resp.setContentType("application/soap+xml; charset=UTF-8");
        out.println("<?xml version='1.0' encoding='UTF-8'?>\r\n"
            + "<Envelope xmlns=\"http://www.w3.org/2003/05/soap-envelope\">\r\n"
            + "  <Header ></Header>\r\n" + "   <Body>\r\n"
            + "      <submitSingleMessageResponse xmlns=\"urn:cdc:iisb:2011\">\r\n"
            + "        <return><![CDATA[MSH|^~\\&||Dummy Message from SMM-Tester|||20230330072547-0600||ACK^V04^ACK|168018274734222851|P|2.5.1|||NE|NE|||||Z23^CDCPHINVS\r\n"
            + "MSA|AA|X62O4982.Yb\r\n" + "]]></return>\r\n"
            + "      </submitSingleMessageResponse>\r\n" + "  </Body>\r\n" + "</Envelope>\r\n"
            + "");
      } finally {
        out.close();
      }
    } else {
      printForm(req, resp);
    }
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    System.out.println("== GET called ===========================================");
    dumpDetails(req);
    resp.setContentType("text/html;charset=UTF-8");
    printForm(req, resp);
  }

  private void printForm(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    try {
      printHtmlHead(out, MENU_HEADER_HOME, req);
      out.println("<form action=\"CatchServlet\" method=\"POST\">");
      out.println("  <input type=\"text\" name=\"name\" value=\"\"/>");
      out.println("  <input type=\"submit\" name=\"submit\" value=\"Submit\"/>");
      out.println("</form>");
      printHtmlFoot(out);
    } finally {
      out.close();
    }
  }

  private void dumpDetails(HttpServletRequest req) throws IOException {

    System.out.println(" + Path: " + req.getPathInfo());
    System.out.println(" + Query: " + req.getQueryString());
    System.out.println(" + Protocol: " + req.getProtocol());
    System.out.println(" + URI: " + req.getRequestURI());
    System.out.println(" + Header Params: ");
    Enumeration<?> headerNames = req.getHeaderNames();
    while (headerNames.hasMoreElements()) {
      Object headerName = headerNames.nextElement();
      System.out.println("    - " + headerName + ": " + req.getHeader(headerName.toString()));
    }

    System.out.println(" + Content (length = " + req.getContentLength() + ")");

    try {
      BufferedInputStream in = new BufferedInputStream(req.getInputStream());
      int b;
      while ((b = in.read()) != -1) {
        System.out.print((char) b);
      }
      in.close();
      System.out.println();
    } catch (Exception e) {
      e.printStackTrace(System.out);
    }

    System.out.println("=========================================================");
    System.out.println();

  }
}
