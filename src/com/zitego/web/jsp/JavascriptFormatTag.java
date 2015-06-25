package com.zitego.web.jsp;

/**
 * Formats an object to be shown in a javascript argument string enclosed in either
 * double or single quotes.
 *
 * @author John Glorioso
 * @version $Id: JavascriptFormatTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class JavascriptFormatTag extends FormatTag
{
	protected String formatAsString()
    {
        String ret = super.formatAsString();
        //Escape double quotes
        ret = ret.replaceAll("\"", "\\\\\"");
        //Escape single quotes
        ret = ret.replaceAll("'", "\\\\'");
        //Escape newlines and carriage returns
        ret = ret.replaceAll("\r", "\\\\r");
        ret = ret.replaceAll("\n", "\\\\n");
        return ret;
    }
}