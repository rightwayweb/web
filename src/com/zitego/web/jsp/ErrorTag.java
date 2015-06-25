package com.zitego.web.jsp;

import com.zitego.util.NonFatalException;
import com.zitego.web.servlet.BaseConfigServlet;
import java.io.IOException;
import java.io.StringReader;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * A tag to use for logging errors in a web application. This tag examines the request
 * for a java.lang.Throwable class stored under the alias "err". This will call the
 * logError and logSevereError methods in BaseConfigServlet. That being the case, as
 * long as the application using this has a Config servlet that defines the logger(s)
 * it will do as expected. If you do not, then error will simply be logged to
 * System.err.
 *
 * For more detailed information, a header will be appended to the log entry if an
 * attribute named "log_id" is stored in the session. This might be helpful if you
 * want to know the username or other helpful information with the error. If none
 * is specified, then nothing will be printed.
 *
 * @author John Glorioso
 * @version $Id: ErrorTag.java,v 1.2 2011/05/08 19:33:04 jglorioso Exp $
 */
public class ErrorTag extends TagSupport
{
    /** The default error message. */
    protected String _defaultErrorMsg = "An error has occurred processing your request";

    /**
     * Sets the default error message.
     *
     * @param msg The message.
     */
    public void setDefaultErrorMsg(String msg)
    {
        _defaultErrorMsg = msg;
    }

    /**
     * Looks for a java.lang.Throwable and stores information about it.
     *
     * @return SKIP_BODY
     */
    public int doStartTag() throws JspException
    {
        HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
        Throwable t = (Throwable)request.getAttribute("err");
        String userInfo = (String)request.getAttribute("user_info");

        if (t == null) t = (Throwable)request.getAttribute("javax.servlet.jsp.jspException");

        String msg = _defaultErrorMsg;
        if (t != null)
        {
            if (t.getMessage() != null) msg = t.getMessage();
        }
        else
        {
            logInfo(request);
        }
        pageContext.setAttribute("userError", msg);

        if (t instanceof org.apache.jasper.JasperException)
        {
            pageContext.setAttribute( "compileError", t.toString() );
        }
        else if ( !(t instanceof NonFatalException) )
        {
            Object id = null;
            HttpSession session = request.getSession();
            if (session != null) id = session.getAttribute("log_id");
            logError(t, (id != null ? id.toString() : null), request, userInfo);
        }
        if (t != null) pageContext.setAttribute("throwable", t);
        return SKIP_BODY;
    }

    private void logError(Throwable t, String headerId, HttpServletRequest request, String userInfo)
    {
        if (t == null) return;
        //Special handling for JspException
        if (t instanceof JspException)
        {
             Throwable t2 = ( (JspException)t ).getRootCause();
             if (t2 != null) t = t2;
        }
        StringWriter errStr = new StringWriter();
        t.printStackTrace( new PrintWriter(errStr) );
        BufferedReader in = new BufferedReader( new StringReader(errStr.toString()) );
        StringBuffer lines = new StringBuffer( (headerId != null ? headerId + " - " : "") );
        String line = null;
        try
        {
            while ( (line=in.readLine()) != null )
            {
                lines.append(" ").append( line.trim() );
            }
        }
        catch (IOException ioe)
        {
            lines.append( ioe.toString() );
        }
        lines.append("\n\n")
             .append("Requested Url: ").append( request.getRequestURI() ).append("\n")
             .append("Full Query String: ").append( request.getQueryString() ).append("\n")
             .append("Remote Ip: ").append( request.getRemoteAddr() ).append("\n");
        if (userInfo != null) lines.append("User: ").append(userInfo);
        logSevere( lines.toString() );
    }

    private void logInfo(HttpServletRequest request)
    {
        StringBuffer contents = new StringBuffer("No exception. Session attributes: ");

        HttpSession session = request.getSession();
        int count = 0;
        for (Enumeration e=session.getAttributeNames(); e.hasMoreElements();)
        {
            String prop = (String)e.nextElement();
            contents.append( (count > 0 ? ", " : "") ).append(prop).append("=").append( session.getAttribute(prop) );
            count++;
        }
        count = 0;
        contents.append("    Request Attributes: ");
        for (Enumeration e=request.getAttributeNames(); e.hasMoreElements();)
        {
            String prop = (String)e.nextElement();
            contents.append( (count > 0 ? ", " : "") ).append(prop).append("=").append( request.getAttribute(prop) );
            count++;
        }
        count = 0;
        contents.append("    Headers: ");
        for (Enumeration e=request.getHeaderNames(); e.hasMoreElements();)
        {
            String prop = (String)e.nextElement();
            contents.append( (count > 0 ? ", " : "") ).append(prop).append("=").append( request.getHeader(prop) );
            count++;
        }
        log( contents.toString() );
    }

    private void log(String msg)
    {
        BaseConfigServlet.logError(msg);
    }

    private void logSevere(String msg)
    {
        BaseConfigServlet.logSevereError(msg);
    }
}
