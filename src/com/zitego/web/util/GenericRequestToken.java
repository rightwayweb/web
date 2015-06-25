package com.zitego.web.util;

import java.util.StringTokenizer;
import java.util.Enumeration;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

/**
 * Wraps a request object to allow for requests in the form:
 * document?token1.token2.token3...&param1=val1&param2=val2
 *
 * @author John Glorioso
 * @version $Id: GenericRequestToken.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class GenericRequestToken implements HttpServletRequest
{
    /** The underlying http request object. */
    protected HttpServletRequest _requestObj;
    protected HttpServletResponse _response;
    private StringBuffer _request;
    private Vector _tokens;
    private String _tokenString;

    /**
     * Creates a new token given a request.
     *
     * @param request The request object.
     * @param request The response object. This is not used for anything, but is set for extending classes.
     * @param request The response object.
     */
    public GenericRequestToken(HttpServletRequest request, HttpServletResponse response)
    {
        this(request, response, null);
    }

    /**
     * Creates a new token given a request, response, and a query string.
     *
     * @param request The request object.
     * @param request The response object. This is not used for anything, but is set for extending classes.
     * @param queryString The query string (not required).
     */
    public GenericRequestToken(HttpServletRequest request, HttpServletResponse response, String queryString)
    {
        _requestObj = request;
        _response = response;

        //Get the tokens
        if (queryString == null) queryString = getQueryString();
        _request = new StringBuffer();

        if (queryString == null) queryString = "";
        int index = queryString.indexOf("&");
        String tokens = (index > -1 ? queryString.substring(0, index) : queryString);
        StringTokenizer st = new StringTokenizer( tokens, ".");
        //Set other stuff here
        _tokens = new Vector();
        StringBuffer tokenString = new StringBuffer();
        while ( st.hasMoreTokens() )
        {
            tokens = st.nextToken();
            if (tokens.indexOf("=") > -1) continue;
            tokenString.append(tokens);
            _tokens.add(tokens);
            _request.append(tokens);
            if ( st.hasMoreTokens() )
            {
                _request.append(".");
                tokenString.append(".");
            }
            else
            {
                _request.append("&");
            }
        }
        _tokenString = tokenString.toString();
        for (Enumeration e=getParameterNames(); e.hasMoreElements();)
        {
            String key = (String)e.nextElement();
            if ( key.equals(_tokenString) ) continue;
            _request.append(key).append("=");
            String[] values = getParameterValues(key);
            if (values != null && values.length > 0)
            {
                _request.append(values[0]);
                for (int i=1; i<values.length; i++)
                {
                    _request.append(",").append(values[i]);
                }
            }
            _request.append("&");
        }
        if (_request.length() > 0 && _request.charAt(_request.length()-1) == '&') _request.deleteCharAt(_request.length()-1);
    }

    private static Cookie getCookie(HttpServletRequest request, String cookie)
	{
		if (cookie == null || request == null || request.getCookies() == null) return null;
		//Look for the specified cookie
		for (int i=0; i<request.getCookies().length; i++)
		{
			if ( cookie.equals(request.getCookies()[i].getName()) )
			{
				return request.getCookies()[i];
			}
		}
		return null;
	}

    public String toString()
    {
        return toString(null);
    }

    /**
     * Returns the request as a string excluding any specified tokens.
     *
     * @param excludes The tokens to exclude.
     */
    public String toString(String[] excludes)
    {
        if (excludes == null) excludes = new String[0];
        StringBuffer req = new StringBuffer( _request.toString() );
        for (int i=0; i<excludes.length; i++)
        {
            if (excludes[i] != null)
            {
                int index = req.indexOf(excludes[i]);
                if (index > 0)
                {
                    char c = req.charAt(index-1);
                    if (c == '.' || c == '?') req.delete( index, index+excludes[i].length() );
                    if (req.charAt(index-1) == '.') req.deleteCharAt(index-1);
                    else if (req.charAt(index-1) == '?' && req.length() > index && req.charAt(index) == '.') req.deleteCharAt(index);
                }
            }
        }
        return req.toString();
    }

    /**
     * Returns the token with the associated index.
     *
     * @param index The index of the token to retrieve.
     * @return String
     * @throws IndexOutOfBoundsException if the index is invalid.
     */
    public String getToken(int index) throws IndexOutOfBoundsException
    {
        return (String)_tokens.get(index);
    }

    /**
     * Inserts a token at the specified index. If the index is larger then the size, it
     * appends the token. Any token at a higher index is incremented by one. If the token
     * is null it is ignored.
     *
     * @param index The token to insert at.
     * @param token The token to insert.
     */
    public void insertToken(int index, String token)
    {
        if ( index >= _tokens.size() ) _tokens.add(token);
        else _tokens.insertElementAt(token, index);
    }

    /**
     * Returns the token portion of this query string.
     *
     * @return String
     */
    public String getTokenString()
    {
        return _tokenString;
    }

    /**
     * Returns the number of tokens.
     *
     * @return int
     */
    public int getNumTokens()
    {
        return _tokens.size();
    }

    /**
     * Returns the token with the associated index as a long.
     *
     * @param index The index of the token to retrieve.
     * @return long
     * @throws IndexOutOfBoundsException if the index is invalid.
     */
    public long getNumericToken(int index) throws IndexOutOfBoundsException
    {
        return Long.parseLong( getToken(index) );
    }

    public String getParameter(String param)
    {
        return _requestObj.getParameter(param);
    }

    public String[] getParameterValues(String param)
    {
        return _requestObj.getParameterValues(param);
    }

    public Object getAttribute(String attr)
    {
        return _requestObj.getAttribute(attr);
    }

    public Enumeration getAttributeNames()
    {
        return _requestObj.getAttributeNames();
    }

    public String getCharacterEncoding()
    {
        return _requestObj.getCharacterEncoding();
    }

    public int getContentLength()
    {
        return _requestObj.getContentLength();
    }

    public String getContentType()
    {
        return _requestObj.getContentType();
    }

    public javax.servlet.ServletInputStream getInputStream() throws java.io.IOException
    {
        return _requestObj.getInputStream();
    }

    public java.util.Locale getLocale()
    {
        return _requestObj.getLocale();
    }

    public Enumeration getLocales()
    {
        return _requestObj.getLocales();
    }

    public java.util.Map getParameterMap()
    {
        return _requestObj.getParameterMap();
    }

    public Enumeration getParameterNames()
    {
        return _requestObj.getParameterNames();
    }

    public String getProtocol()
    {
        return _requestObj.getProtocol();
    }

    public java.io.BufferedReader getReader() throws java.io.IOException
    {
        return _requestObj.getReader();
    }

    public String getRealPath(String path)
    {
        return _requestObj.getRealPath(path);
    }

    public String getRemoteAddr()
    {
        return _requestObj.getRemoteAddr();
    }

    public String getRemoteHost()
    {
        return _requestObj.getRemoteHost();
    }

    public javax.servlet.RequestDispatcher getRequestDispatcher(String path)
    {
        return _requestObj.getRequestDispatcher(path);
    }

    public String getScheme()
    {
        return _requestObj.getScheme();
    }

    public String getServerName()
    {
        return _requestObj.getServerName();
    }

    public int getServerPort()
    {
        return _requestObj.getServerPort();
    }

    public boolean isSecure()
    {
        return _requestObj.isSecure();
    }

    public void removeAttribute(String name)
    {
        _requestObj.removeAttribute(name);
    }

    public void setAttribute(String name, Object o)
    {
        _requestObj.setAttribute(name, o);
    }

    public void setCharacterEncoding(String env) throws java.io.UnsupportedEncodingException
    {
        _requestObj.setCharacterEncoding(env);
    }

    public String getAuthType()
    {
        return _requestObj.getAuthType();
    }

    public String getContextPath()
    {
        return _requestObj.getContextPath();
    }

    public javax.servlet.http.Cookie[] getCookies()
    {
        return _requestObj.getCookies();
    }

    public long getDateHeader(String name)
    {
        return _requestObj.getDateHeader(name);
    }

    public String getHeader(String name)
    {
        return _requestObj.getHeader(name);
    }

    public Enumeration getHeaderNames()
    {
        return _requestObj.getHeaderNames();
    }

    public Enumeration getHeaders(String name)
    {
        return _requestObj.getHeaders(name);
    }

    public int getIntHeader(String name)
    {
        return _requestObj.getIntHeader(name);
    }

    public String getMethod()
    {
        return _requestObj.getMethod();
    }

    public String getPathInfo()
    {
        return _requestObj.getPathInfo();
    }

    public String getPathTranslated()
    {
        return _requestObj.getPathTranslated();
    }

    public String getQueryString()
    {
        return _requestObj.getQueryString();
    }

    public String getRemoteUser()
    {
        return _requestObj.getRemoteUser();
    }

    public String getRequestedSessionId()
    {
        return _requestObj.getRequestedSessionId();
    }

    public String getRequestURI()
    {
        return _requestObj.getRequestURI();
    }

    public StringBuffer getRequestURL()
    {
        return _requestObj.getRequestURL();
    }

    public String getServletPath()
    {
        return _requestObj.getServletPath();
    }

    public HttpSession getSession()
    {
        return _requestObj.getSession();
    }

    public HttpSession getSession(boolean create)
    {
        return _requestObj.getSession(create);
    }

    public java.security.Principal getUserPrincipal()
    {
        return _requestObj.getUserPrincipal();
    }

    public boolean isRequestedSessionIdFromCookie()
    {
        return _requestObj.isRequestedSessionIdFromCookie();
    }

    public boolean isRequestedSessionIdFromUrl()
    {
        return _requestObj.isRequestedSessionIdFromUrl();
    }

    public boolean isRequestedSessionIdFromURL()
    {
        return _requestObj.isRequestedSessionIdFromURL();
    }

    public boolean isRequestedSessionIdValid()
    {
        return _requestObj.isRequestedSessionIdValid();
    }

    public boolean isUserInRole(String role)
    {
        return _requestObj.isUserInRole(role);
    }

    public int getLocalPort()
    {
        return _requestObj.getLocalPort();
    }

    public int getRemotePort()
    {
        return _requestObj.getRemotePort();
    }

    public String getLocalAddr()
    {
        return _requestObj.getLocalAddr();
    }

    public String getLocalName()
    {
        return _requestObj.getLocalName();
    }
}