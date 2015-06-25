package com.zitego.web.jsp;

import javax.servlet.jsp.tagext.*;

/**
 * Sets the variable information for storing request attributes into the
 * page context.
 *
 * @author John Glorioso
 * @version $Id: ErrorTagExtraInfo.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class ErrorTagExtraInfo extends TagExtraInfo
{
	public ErrorTagExtraInfo() {}

	public VariableInfo[] getVariableInfo(TagData data)
	{
		return new VariableInfo[]
		{
		    new VariableInfo("userError", "java.lang.String", true, VariableInfo.AT_BEGIN),
		    new VariableInfo("compileError", "java.lang.String", true, VariableInfo.AT_BEGIN),
		    new VariableInfo("throwable", "java.lang.Throwable", true, VariableInfo.AT_BEGIN),
		};
	}
}