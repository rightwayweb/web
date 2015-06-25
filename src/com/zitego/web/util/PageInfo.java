package com.zitego.web.util;

import java.util.*;
import javax.servlet.http.HttpServletRequest;

/**
 * This class is constructed with a request to capture all information about the
 * requested page including url, request parameters, and request attributes.
 *
 * @author John Glorioso
 * @version $Id: PageInfo.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class PageInfo
{
    /** The page (servlet path) requested. */
    private String _servletPath;
    /** The page request parameters. */
    private Hashtable _requestParams;
    /** The page request attributes. */
    private Hashtable _requestAttributes;

    /**
     * Creates a new PageInfo with a request.
     *
     * @param request The request object.
     */
    public PageInfo(HttpServletRequest request)
    {
        _servletPath = request.getServletPath();
        _requestParams = new Hashtable();
        for (Enumeration e=request.getParameterNames(); e.hasMoreElements();)
        {
            String name = (String)e.nextElement();
            _requestParams.put( name, request.getParameter(name) );
        }
        _requestAttributes = new Hashtable();
        for (Enumeration e=request.getAttributeNames(); e.hasMoreElements();)
        {
            String name = (String)e.nextElement();
            _requestParams.put( name, request.getAttribute(name) );
        }
    }

    /**
     * Returns the servlet path.
     *
     * @return String
     */
    public String getServletPath()
    {
        return _servletPath;
    }

    /**
     * Returns the request parameters.
     *
     * @return Hashtable
     */
    public Hashtable getRequestParameters()
    {
        return _requestParams;
    }

    /**
     * Returns the request attributes.
     *
     * @return Hashtable
     */
    public Hashtable getRequestAttributes()
    {
        return _requestAttributes;
    }
}