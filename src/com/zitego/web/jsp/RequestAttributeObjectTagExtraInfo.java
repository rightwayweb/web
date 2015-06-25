package com.zitego.web.jsp;

import javax.servlet.jsp.tagext.*;

/**
 * Sets the variable information for storing request attributes into the
 * page context.
 *
 * @author John Glorioso
 * @version $Id: RequestAttributeObjectTagExtraInfo.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class RequestAttributeObjectTagExtraInfo extends TagExtraInfo
{
	public RequestAttributeObjectTagExtraInfo() {}

	public VariableInfo[] getVariableInfo(TagData data)
	{
	    VariableInfo[] ret = new VariableInfo[0];
	    String id = (String)data.getAttribute("id");
	    String type = (String)data.getAttribute("type");
	    if (id != null && type != null)
	    {
	        ret = new VariableInfo[]
	        {
	            new VariableInfo(id, type, true, VariableInfo.AT_BEGIN)
	        };
		}
		return ret;
	}
}