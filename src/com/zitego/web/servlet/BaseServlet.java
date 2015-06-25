package com.zitego.web.servlet;

import com.zitego.util.NonFatalWebappException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Enumeration;
import java.io.*;

/**
 * This is a base servlet for handling requests. It includes methods to print out a request
 * parameter string. It is abstract and requires the extending class to define an error page
 * in getErrorPage().
 *
 * @author John Glorioso
 * @version $Id: BaseServlet.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class BaseServlet extends HttpServlet
{
    protected static ServletConfig _config;

    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);
        _config = config;
    }

    /**
     * Returns this servlet's config.
     *
     * @return ServletConfig
     */
    protected ServletConfig getConfig()
    {
        return _config;
    }

    /**
     * Returns the error page for this servlet.
     *
     * @return String
     */
    protected abstract String getErrorPage();

    /**
     * Returns the request parameters as a comma delimited string.
     *
     * @param HttpServletRequest The request object.
     * @return String
     */
    public String getParameterString(HttpServletRequest request)
    {
        StringBuffer ret = new StringBuffer();
        int count = 0;
        for (Enumeration e=request.getParameterNames(); e.hasMoreElements();)
        {
            String name = (String)e.nextElement();
            ret.append( (count++ > 0 ? "&" : "?") ).append(name).append("=").append( request.getParameter(name) );
        }
        return ret.toString();
    }

    /**
     * Returns a string with the request parameters, request attributes, and the session attributes.
     *
     * @param HttpServletRequest The request object.
     * @return String
     */
    public static String debug(HttpServletRequest request)
    {
        StringBuffer contents = new StringBuffer("Session Attributes:");
        HttpSession session = (HttpSession)request.getSession();
        for (Enumeration e=session.getAttributeNames(); e.hasMoreElements();)
        {
            String prop = (String)e.nextElement();
            contents.append("\r\n").append(prop).append("=").append( session.getAttribute(prop) );
        }
        contents.append("\r\nRequest Attributes:");
        for (Enumeration e=request.getAttributeNames(); e.hasMoreElements();)
        {
            String prop = (String)e.nextElement();
            contents.append("\r\n").append(prop).append("=").append( request.getAttribute(prop) );
        }
        contents.append("\r\nRequest Parameters:");
        for (Enumeration e=request.getParameterNames(); e.hasMoreElements();)
        {
            String prop = (String)e.nextElement();
            contents.append("\r\n").append(prop).append("=").append( request.getParameter(prop) );
        }
        contents.append("\r\nCookies:");
        Cookie c[] = request.getCookies();
        if (c != null)
        {
            for(int i=0; i < c.length; i++)
            {
                contents.append("\r\ncookie ").append( (i+1) ).append(": ")
                        .append("name=").append( c[i].getName() ).append(", ")
                        .append("value=").append( c[i].getValue() ).append(", ")
                        .append("domain=").append( c[i].getDomain() ).append(", ")
                        .append("comment=").append( c[i].getComment() ).append(", ")
                        .append("path=").append( c[i].getPath() );
            }
        }
        return contents.toString();
    }

    /**
     * Stores an exception (Throwable) in the request as an attribute and includes the
     * error page to display the error.
     *
     * @param HttpServletRequest The request object.
     * @param HttpServletResponse The response object.
     * @param Throwable The error.
     * @throws ServletException if a problem occurred including.
     * @throws IOException if a problem occurred including.
     */
    protected void handleError(HttpServletRequest request, HttpServletResponse response, Throwable err)
    throws ServletException, IOException
    {
        request.setAttribute("err", err);
        String errorPage = null;
        if (err instanceof NonFatalWebappException) errorPage = ( (NonFatalWebappException)err ).getForwardUrl();
        else errorPage = getErrorPage();
        response.setContentType("text/html");
        request.getRequestDispatcher(errorPage).include(request, response);
    }

    /**
     * Includes the specified page.
     *
     * @param String The url.
     * @param HttpServletRequest The request object.
     * @param HttpServletResponse The response object.
     */
    protected void gotoPage(String url, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        gotoPage(url, null, request, response);
    }

    /**
     * Includes the specified html page given the specified exception. If the exception is
     * not null, it is stored in the request as "err".
     *
     * @param String The url.
     * @param Exception The error exception if any.
     * @param HttpServletRequest The request object.
     * @param HttpServletResponse The response object.
     */
    protected void gotoPage(String url, Exception e, HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException
    {
        gotoPage(url, e, request, response, null);
    }

    /**
     * Includes the specified page given the specified exception and mimetype. If the exception is
     * not null, it is stored in the request as "err". If the mimetype is null, then it defaults to
     * text/html.
     *
     * @param String The url.
     * @param Exception The error exception if any.
     * @param HttpServletRequest The request object.
     * @param HttpServletResponse The response object.
     * @param String The mime type.
     */
    protected void gotoPage(String url, Exception e, HttpServletRequest request, HttpServletResponse response, String mime)
    throws ServletException, IOException
    {
        if (e != null) request.setAttribute("err", e);
        if (mime == null) mime = "text/html";
        response.setContentType(mime);
        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(url);
        dispatcher.include(request, response);
    }

    /**
     * Sends the given string to the output buffer sending back a 200 response.
     *
     * @param HttpServletResponse The response object.
     * @param String The string to output.
     * @throws IOException if an error occurs writing the string out.
     */
    protected void sendResponse(HttpServletResponse response, String msg) throws IOException
    {
        sendResponse("text/html", msg.toString().getBytes(), response);
    }

    /**
	 * Displays the content to the web browser in the specified mime type.
	 *
	 * @param String The mimetype to show.
	 * @param byte[] The content.
	 * @param HttpServletResponse The response object.
	 * @throws IOException When a problem occurs.
	 */
	public static void sendResponse(String mimeType, byte[] content, HttpServletResponse response) throws IOException
	{
		response.setContentType(mimeType);
		response.setContentLength(content.length);
		BufferedOutputStream out = new BufferedOutputStream( response.getOutputStream() );
		out.write(content, 0, content.length);
		out.flush();
	}

	/**
	 * Returns a 400 error to the client.
	 *
	 * @param HttpServletResponse The response object.
	 * @param String A more detailed message. This can be null.
	 * @throws IOException when something goes wrong.
	 */
	public static void show400Error(HttpServletResponse response, String msg) throws IOException
	{
		if (msg == null)
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST);
		}
		else
		{
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, msg);
		}
	}

	/**
	 * Returns a 403 error to the client.
	 *
	 * @param HttpServletResponse The response object.
	 * @throws IOException when something goes wrong.
	 */
	public static void show403Error(HttpServletResponse response) throws IOException
	{
		response.sendError(HttpServletResponse.SC_FORBIDDEN);
	}
}