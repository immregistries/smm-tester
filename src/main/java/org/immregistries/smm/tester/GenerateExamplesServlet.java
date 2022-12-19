/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package org.immregistries.smm.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.immregistries.smm.transform.ModifyMessageRequest;
import org.immregistries.smm.transform.ModifyMessageService;

/**
 * 
 * @author nathan
 */
@SuppressWarnings("serial")
public class GenerateExamplesServlet extends ClientServlet {
  private static final String PARAM_MESSAGE = "message";
  private static final String PARAM_SCRIPT = "script";
  private static final String PARAM_COUNT = "count";
  private static final String PARAM_DOB_START = "dobStart";
  private static final String PARAM_DOB_END = "dobEnd";


  /**
   * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
   * 
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  protected void processRequest(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    response.setContentType("text/html;charset=UTF-8");
    PrintWriter out = response.getWriter();

    try {
      printHtmlHead(out, MENU_HEADER_HOME, request);
      out.println("<h2>Message Modifier Demo</h2>");
      out.println("    <form action=\"GenerateExamplesServlet\" method=\"POST\">");
      out.println("      <table>");
      out.println("        <tr>");
      out.println("          <td>Starting Message</td>");
      out.println("          <td><textarea name=\"" + PARAM_MESSAGE
          + "\" cols=\"120\" rows=\"15\" wrap=\"off\"></textarea></td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td>Vary Date of Birth</td>");
      out.println("          <td>");
      out.println("            From ");
      out.println("            <input type=\"text\" name=\"" + PARAM_DOB_START
          + "\" value=\"\" size=\"8\"/> to ");
      out.println("            <input type=\"text\" name=\"" + PARAM_DOB_END
          + "\" value=\"\" size=\"8\"/>");
      out.println("          </td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td>Script</td>");
      out.println("          <td><textarea name=\"" + PARAM_SCRIPT
          + "\" cols=\"120\" rows=\"15\" wrap=\"off\"></textarea></td>");
      out.println("        </tr>");
      out.println("        <tr>");
      out.println("          <td>Repeat Count</td>");
      out.println("          <td>");
      out.println(
          "            <input type=\"text\" name=\"" + PARAM_COUNT + "\" value=\"1\" size=\"3\"/>");
      out.println("          </td>");
      out.println("        </tr>");
      out.println("      </table>");
      out.println("      <input type=\"submit\" name=\"action\" value=\"Generate\"/>");
      out.println("    </form>");

      ClientServlet.printHtmlFoot(out);
    } catch (Exception e) {
      out.println("<p>Exception Occurred: " + e.getMessage() + "</p><pre>");
      e.printStackTrace(out);
      out.println("</pre>");
    } finally {
      out.close();
    }
  }

  // <editor-fold defaultstate="collapsed"
  // desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
  /**
   * Handles the HTTP <code>GET</code> method.
   * 
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    processRequest(request, response);

  }

  /**
   * Handles the HTTP <code>POST</code> method.
   * 
   * @param request servlet request
   * @param response servlet response
   * @throws ServletException if a servlet-specific error occurs
   * @throws IOException if an I/O error occurs
   */
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    String script = request.getParameter(PARAM_SCRIPT);
    if (script != null) {
      response.setContentType("text/plain;charset=UTF-8");
      response.setHeader("Content-Disposition",
          "attachment; filename=\"" + URLEncoder.encode("Examples.hl7.txt", "UTF-8") + "\"");
      PrintWriter out = response.getWriter();
      String message = request.getParameter(PARAM_MESSAGE);
      ModifyMessageRequest mmr = new ModifyMessageRequest();
      mmr.setMessageOriginal(message);

      int count = 1;
      try {
        count = Integer.parseInt(request.getParameter(PARAM_COUNT));
      } catch (NumberFormatException nfe) {
        // ignore
      }
      Date dobS = null;
      int daysBetween = 0;

      {
        Date dobE;
        String dobStartString = request.getParameter(PARAM_DOB_START);
        String dobEndString = request.getParameter(PARAM_DOB_END);
        if (dobStartString.length() == 8 && dobEndString.length() == 8) {
          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
          try {
            dobS = sdf.parse(dobStartString);
            dobE = sdf.parse(dobEndString);
            if (dobS.before(dobE))
            {
              daysBetween = (int) ( (dobE.getTime() - dobS.getTime()) / (1000 * 60 * 60 * 24));
            }
          } catch (ParseException e) {
            // do nothing
          }
        }
      }
      for (int i = 0; i < count; i++) {
        String changeDob = "";
        if (daysBetween > 0)
        {
          int daysToAdd = (int) (System.currentTimeMillis() % daysBetween);
          Calendar calendar = Calendar.getInstance();
          calendar.setTime(dobS);
          calendar.add(Calendar.DAY_OF_MONTH, daysToAdd);
          SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
          changeDob = "PID-7=" + sdf.format(calendar.getTime()) + "\r";
        }
        mmr.setTransformScript(changeDob + script);
        ModifyMessageService modifyMessageService = new ModifyMessageService();
        modifyMessageService.transform(mmr);
        out.print(mmr.getMessageFinal());
      }


      out.close();
    } else {
      processRequest(request, response);
    }

  }

  /**
   * Returns a short description of the servlet.
   * 
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Generates a set of HL7 messages";

  }// </editor-fold>
}
