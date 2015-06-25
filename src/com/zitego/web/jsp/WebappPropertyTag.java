package com.zitego.web.jsp;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import com.zitego.web.servlet.BaseConfigServlet;
import com.zitego.web.util.StaticWebappProperties;

/**
 * Retrieves properties out of the BaseConfigServlet's StaticWebappProperties object and sets them in the
 * page context based on their name and type.
 *
 * @author John Glorioso
 * @version $Id: WebappPropertyTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class WebappPropertyTag extends TagSupport
{
    /** The name of the property to retrieve. */
  	private String _name;
  	/** The type of property. */
  	private String _type;

  	/**
  	 * Set the name of the global property
  	 *
  	 * @param name The name.
  	 */
  	public void setName(String name)
  	{
  		_name = name;
  	}

  	/**
  	 * Set the type of the property being requested
  	 *
  	 * @param type The type.
  	 */
  	public void setType(String type)
  	{
  		_type  = type;
  	}

  	/**
   	 * Retrieves and sets the property if it exists.
   	 *
   	 * @return int
   	 */
  	public int doStartTag() throws JspException
  	{
  		Object o = BaseConfigServlet.getWebappProperties().getProperty(_name);
  		if ( o == null ) throw new JspException("Requested property: " + _name + " is not available");

  		pageContext.setAttribute(this.getId(), o);

   		return SKIP_BODY;
  	}
}