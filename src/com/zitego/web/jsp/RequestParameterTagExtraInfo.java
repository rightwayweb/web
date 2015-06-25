package com.zitego.web.jsp;

import javax.servlet.jsp.tagext.*;

/**
 * Stores variable information for request parameters to be stored
 * in the page context.
 *
 * @author John Glorioso
 * @version $Id: RequestParameterTagExtraInfo.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class RequestParameterTagExtraInfo extends TagExtraInfo
{
	public RequestParameterTagExtraInfo() {}

	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
		    new VariableInfo
		    (
		        (String)data.getAttribute("id"),
		        "java.lang.String",
		        true,
		        VariableInfo.AT_BEGIN
		    )
		};
	}
}