package com.zitego.web.jsp;

import com.zitego.markup.html.HtmlFormatter;

/**
 * This tag escapes or unescapes content given the action key. The action can either be
 * escape or unescape. If it is not unescape, then escape is assumed. You may also
 * convert spaces to &nbsp; if the action is escape. You can set whether to convert
 * line breaks as well. The default is true.
 *
 * @author John Glorioso
 * @version $Id: HtmlFormatTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class HtmlFormatTag extends FormatTag
{
    /** Whether we are escaping or unescaping. */
    protected boolean _unescape = false;
    /** Whether to convert spaces to &nbsp;. */
    protected boolean _convertSpaces = false;
    /** Whether to line breaks to &lt;br&gt;. */
    protected boolean _convertLineBreaks = true;

    /**
     * Sets the whether to unescape.
     *
     * @param boolean Whether to unescape or not.
     */
    public void setUnescape(boolean un)
    {
        _unescape = un;
    }

    /**
     * Sets whether to unescape given a string.
     *
     * @param String "true" if to unescape.
     */
    public void setUnescape(String un)
    {
        setUnescape( new Boolean(un).booleanValue() );
    }

    /**
     * Sets the whether to convert spaces.
     *
     * @param boolean Whether to convert or not.
     */
    public void setConvertSpaces(boolean convert)
    {
        _convertSpaces = convert;
    }

    /**
     * Sets whether or not to convert spaces given a string.
     *
     * @param String "true" if to convert.
     */
    public void setConvertSpaces(String convert)
    {
        setConvertSpaces( new Boolean(convert).booleanValue() );
    }

    /**
     * Sets the whether to convert line breaks.
     *
     * @param boolean Whether to convert or not.
     */
    public void setConvertLineBreaks(boolean convert)
    {
        _convertLineBreaks = convert;
    }

    /**
     * Sets whether or not to convert line breaks given a string.
     *
     * @param String "true" if to convert.
     */
    public void setConvertLineBreaks(String convert)
    {
        setConvertLineBreaks( new Boolean(convert).booleanValue() );
    }

    protected String formatAsString()
    {
        String ret = super.formatAsString();
        if (_unescape) ret = HtmlFormatter.unescape(ret);
        else ret = HtmlFormatter.escape(ret, _convertLineBreaks);
        if (_convertSpaces) ret = HtmlFormatter.convertSpaces(ret);
        return ret;
    }
}