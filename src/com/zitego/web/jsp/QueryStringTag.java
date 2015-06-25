package com.zitego.web.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.Hashtable;
import java.util.Enumeration;
import javax.servlet.ServletRequest;

/**
 * Creates a string based on the values in the request formatted as ?param1=val1&param2=val2&.
 * If there are no parameters then the string is stored as only a ?. In either case, parameters
 * can be safely appended to the querystring or it can be used alone.
 *
 * @author John Glorioso
 * @version $Id: QueryStringTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class QueryStringTag extends BodyTagSupport
{
    /** The parameters. */
    private Hashtable _params = new Hashtable();

    /**
     * Removes the given parameter.
     *
     * @param String The parameter name.
     */
    public void exclude(String name)
    {
        _params.remove(name);
    }

    /**
     * Creates the query string and stores it in the page context.
     *
     * @return EVAL_BODY_INCLUDE
     */
    public int doStartTag() throws JspException
    {
        _params.clear();
        ServletRequest request = pageContext.getRequest();
        Enumeration names = request.getParameterNames();
        while ( names.hasMoreElements() )
        {
            String name = (String)names.nextElement();
            _params.put( name, request.getParameter(name) );
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException
    {
        StringBuffer querystring = new StringBuffer();
        Enumeration names = _params.keys();
        if ( names.hasMoreElements() ) querystring.append("?");
        while ( names.hasMoreElements() )
        {
            String name = (String)names.nextElement();
            querystring.append(name).append("=").append( (String)_params.get(name) ).append("&");
        }
        pageContext.setAttribute( getId(), querystring.toString() );

        return EVAL_PAGE;
    }
}