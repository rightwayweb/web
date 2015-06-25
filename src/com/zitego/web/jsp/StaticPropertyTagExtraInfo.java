package com.zitego.web.jsp;

import javax.servlet.jsp.tagext.*;

/**
 * Defines the variable information for storing static properties in
 * the page context.
 *
 * @author John Glorioso
 * @version $Id: StaticPropertyTagExtraInfo.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class StaticPropertyTagExtraInfo extends TagExtraInfo
{
	public StaticPropertyTagExtraInfo() {}

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