package com.zitego.web.jsp;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Retrieves request object attributes and stores them in the page context.
 *
 * @author John Glorioso
 * @version $Id: RequestAttributeObjectTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class RequestAttributeObjectTag extends TagSupport
{
    /** The name of the tag. */
  	private String _name;
  	/** The value. */
  	private Object _val;

  	/**
  	 * Sets the name of the request attribute variable to retrieve.
  	 *
  	 * @param String The name.
  	 */
  	public void setName(String name)
  	{
  		_name = name;
  	}

  	/**
  	 * Sets the type of the object to retrieve.
  	 *
  	 * @param String The class name.
  	 */
  	public void setType(String type) { }

	/**
	 * Sets the value of the attribute to this.
	 *
	 * @param Object The value.
	 */
	public void setValue(Object val)
	{
	    _val = val;
	}

  	/**
   	 * Stores the retrieved object in the page context and returns SKIP_BODY.
   	 *
   	 * @return int
   	 */
  	public int doStartTag() throws JspException
  	{
  		ServletRequest request = pageContext.getRequest();

        String id = getId();
  		if (id != null)
  		{
  		    Object val = request.getAttribute(_name);
            if (val != null) pageContext.setAttribute(id, val);
        }
        if (_val != null) request.setAttribute(_name, _val);

   		return SKIP_BODY;
  	}
}