package edu.rice.rubis.beans.servlets;

import edu.rice.rubis.beans.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.transaction.UserTransaction;
import java.util.Enumeration;

/**
 * This servlets displays general information about the user loged in
 * and about his current bids or items to sell.
 * @author <a href="mailto:cecchet@rice.edu">Emmanuel Cecchet</a> and <a href="mailto:julie.marguerite@inrialpes.fr">Julie Marguerite</a>
 * @version 1.0
 */
public class AboutMe extends HttpServlet
{
  private ServletPrinter sp = null;
  private Context initialContext = null;


  private void printError(String errorMsg)
  {
    sp.printHTMLheader("RUBiS ERROR: About me");
    sp.printHTML("<h3>Your request has not been processed due to the following error :</h3><br>");
    sp.printHTML(errorMsg);
    sp.printHTMLfooter();
  }





  /**
   * Call <code>doPost</code> method.
   *
   * @param request a <code>HttpServletRequest</code> value
   * @param response a <code>HttpServletResponse</code> value
   * @exception IOException if an error occurs
   * @exception ServletException if an error occurs
   */
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    doPost(request, response);
  }


  /** 
   * Check username and password and build the web page that display the information about
   * the loged in user.
   *
   * @param request a <code>HttpServletRequest</code> value
   * @param response a <code>HttpServletResponse</code> value
   * @exception IOException if an error occurs
   * @exception ServletException if an error occurs
   */
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
  {
    String  password=null, username=null; 
    Integer userId=null;
    
    sp = new ServletPrinter(response, "About me");

    username = request.getParameter("nickname");
    password = request.getParameter("password");    
    // Authenticate the user
    if ((username != null && !username.equals("")) || (password != null && !password.equals("")))
    {
      try
      {
        initialContext = new InitialContext();
      } 
      catch (Exception e) 
      {
        printError("Cannot get initial context for JNDI: " + e+"<br>");
        return ;
      }
     }
    else
    {
      printError(" You must provide valid username and password.");
      return ;
    }
    sp.printHTMLheader("RUBiS: About Me");
    SB_AboutMeHome aboutMeHome;
    SB_AboutMe aboutMe;
    try 
    {
      System.out.println("1");
      aboutMeHome = (SB_AboutMeHome)PortableRemoteObject.narrow(initialContext.lookup("SB_AboutMeHome"),
                                                                SB_AboutMeHome.class);
      System.out.println("2");
      aboutMe = aboutMeHome.create();
      System.out.println("3");
    } 
    catch (Exception e)
    {
      printError("Cannot lookup SB_AboutMe: " +e+"<br>");
      return ;
    }
    String html;
    try 
    {
      html = aboutMe.getAboutMe(username, password);
       sp.printHTML(html);
     } 
     catch (Exception e)
     {
       printError("Cannot retrieve information about you: " +e+"<br>");
       return ;
     }
    sp.printHTMLfooter();
  }

}
