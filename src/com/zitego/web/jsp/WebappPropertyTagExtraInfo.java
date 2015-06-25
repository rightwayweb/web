package com.zitego.web.jsp;

import javax.servlet.jsp.tagext.*;

/**
 * Defines the variable information for storing webapp properties in
 * the page context.
 *
 * @author John Glorioso
 * @version $Id: WebappPropertyTagExtraInfo.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class WebappPropertyTagExtraInfo extends TagExtraInfo
{
	public WebappPropertyTagExtraInfo() {}

	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
		    new VariableInfo
		    (
		        (String)data.getAttribute("id"),
		        (String)data.getAttribute("type"),
		        true,
		        VariableInfo.AT_BEGIN
		    )
        };
	}
}