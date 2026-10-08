package org.immregistries.smm.tester;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.immregistries.smm.transform.ModifyMessageRequest;
import org.immregistries.smm.transform.ModifyMessageService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author nathan
 */
public class GenerateExamplesServlet extends ClientServlet {
  private static final long serialVersionUID = 1L;
  
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
      printPageHeader(out, "Generate Examples",
          "Apply a modification script to a starting message, as many times as needed, and"
              + " download the results as a text file.");
      out.println("<section class=\"aira-panel\">");
      out.println("  <div class=\"aira-panel__body\">");
      out.println("    <form class=\"aira-form\" action=\"GenerateExamplesServlet\" method=\"POST\">");
      out.println("      <div class=\"aira-field\">");
      out.println("        <label for=\"" + PARAM_MESSAGE + "\">Starting Message</label>");
      out.println("        <textarea class=\"aira-textarea smm-code\" id=\"" + PARAM_MESSAGE
          + "\" name=\"" + PARAM_MESSAGE + "\" rows=\"12\" wrap=\"off\"></textarea>");
      out.println("      </div>");
      out.println("      <div class=\"aira-field-row\">");
      printTextField(out, PARAM_DOB_START, "Vary Date of Birth From", "");
      printTextField(out, PARAM_DOB_END, "Vary Date of Birth To", "");
      printTextField(out, PARAM_COUNT, "Repeat Count", "1");
      out.println("      </div>");
      out.println("      <div class=\"aira-field\">");
      out.println("        <label for=\"" + PARAM_SCRIPT + "\">Script</label>");
      out.println("        <textarea class=\"aira-textarea smm-code\" id=\"" + PARAM_SCRIPT
          + "\" name=\"" + PARAM_SCRIPT + "\" rows=\"10\" wrap=\"off\"></textarea>");
      out.println("      </div>");
      out.println("      <div class=\"aira-form-actions\">");
      out.println("        <button class=\"aira-button aira-button--primary\" type=\"submit\""
          + " name=\"action\" value=\"Generate\">Generate</button>");
      out.println("      </div>");
      out.println("    </form>");
      out.println("  </div>");
      out.println("</section>");

      ClientServlet.printHtmlFoot(out);
    } catch (Exception e) {
      printException(out, e);
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
