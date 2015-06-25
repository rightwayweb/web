package com.zitego.web.jsp;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Retrieves request parameters from the request object and stores them
 * in the page context.
 *
 * @author John Glorioso
 * @version $Id: RequestParameterTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class RequestParameterTag extends TagSupport
{
    /** The name of the request parameter to retrieve. */
  	private String _name;
  	/** The default value if the attribute is not there. Default is an empty string. */
  	private String _default = "";

  	/**
  	 * Set the name of the request parameter to retrieve
  	 *
  	 * @param String The name.
  	 */
  	public void setName(String name)
  	{
  		_name = name;
  	}

  	/**
  	 * Set the default value for the parameer. If the parameter is not found,
  	 * this is the value used.
  	 *
  	 * @param String The value.
  	 */
  	public void setDefault(String val)
  	{
  		_default = (val == null ? "" : val);
  	}

  	/**
   	 * Sets the request parameter into the page context and returns SKIP_BODY.
   	 *
   	 * @return int
   	 */
  	public int doStartTag() throws JspException
  	{
  		ServletRequest request = pageContext.getRequest();
  		String value = request.getParameter(_name);
  		if (value == null) value = _default;

  		pageContext.setAttribute(getId(), value);

   		return SKIP_BODY;
    }
}