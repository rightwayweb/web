package com.zitego.web.servlet;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * This servlet simply forwards to index.jsp, the given servlet (passed in the
 * request as svlt=), or the given page (passed in the request as pg=). If pg
 * is supplied it will append ".jsp" automatically. If you want to pass a page
 * that already has an extension, use the svlt parameter. This servlet also
 * expects that a file called /error_full.jsp be placed in the application's
 * root directory that will handle errors.
 *
 * @author John Glorioso
 * @version $Id: TransportServlet.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class TransportServlet extends BaseServlet
{
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //Look to see if they want a servlet
        String svlt = request.getParameter("svlt");
        //No servlet? Look for a page
        if (svlt == null)
        {
            svlt = request.getParameter("pg");
            if (svlt != null) svlt = svlt + ".jsp";
            //Alright, just default to index.jsp
            else svlt = "/index.jsp";
        }
        response.setContentType("text/html");
        request.getRequestDispatcher(svlt).include(request, response);
    }

    protected String getErrorPage()
    {
        return "/error_full.jsp";
    }
}