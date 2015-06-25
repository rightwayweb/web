package com.zitego.web.jsp;

import javax.servlet.jsp.tagext.*;

/**
 * Describes the variable that will be stored to hold the query string.
 *
 * @author John Glorioso
 * @version $Id: QueryStringTagExtraInfo.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class QueryStringTagExtraInfo extends TagExtraInfo
{
    public QueryStringTagExtraInfo() {}

    public VariableInfo[] getVariableInfo(TagData data)
    {
        return new VariableInfo[]
        {
            new VariableInfo
            (
                (String)data.getAttribute("id"), "java.lang.String", true, VariableInfo.AT_BEGIN
            )
        };
	}
}