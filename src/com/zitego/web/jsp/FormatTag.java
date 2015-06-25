package com.zitego.web.jsp;

import com.zitego.util.ObjectFormatter;
import com.zitego.util.TextUtils;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.text.Format;
import java.io.IOException;

/**
 * A Tag that returns a string value or a blank depending on if the object is null or not.
 * A default value can be optionally set to display in the case of the object being null.
 * If the object is not null, a formatter can be optionally set to format the Object.
 * Additionally, you can specify a string of methods to call on the provided object to
 * retrieve the actual object to format. If any of the objects along the way returned
 * are null, then the default value is returned. Ex on a com.zitego.customer.Customer object:<br>
 * <code>"getContactInfo.getFirstName"</code> will check to see if the
 * provided customer is null, if not it will attempt to call "getContactInfo".
 * If the object returned from that is not null, then "getFirstName" will
 * be called. The above mentioned logic will be performed on the first name
 * returned.
 *
 * @author John Glorioso
 * @version $Id: FormatTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class FormatTag extends TagSupport
{
    /** The object to format. */
    protected Object _object;
    /** What to return if the object is null. An empty string is default. */
    protected String _nullValue = "";
    /** The Formatter class. */
    private Format _format;
    /** The method string. */
    private String _methods;
    /** The max characters to display. */
    private int _maxChars = -1;

    /**
     * Sets the object.
     *
     * @param o The object.
     */
    public void setObj(Object o)
    {
        _object = o;
    }

    /**
     * Sets the null return value default.
     *
     * @param val The null value.
     */
    public void setNullValue(String val)
    {
        _nullValue = val;
    }

    /**
     * Sets the format class.
     *
     * @param f The format.
     */
    public void setFormat(Format f)
    {
        _format = f;
    }

    /**
     * Sets the max characters.
     *
     * @param max The max characters.
     */
    public void setMaxChars(String max)
    {
        setMaxChars(Integer.parseInt(max));
    }

    /**
     * Sets the max characters.
     *
     * @param max The max characters.
     */
    public void setMaxChars(int max)
    {
        _maxChars = max;
    }

    /**
     * Sets the method string.
     *
     * @param methods The method string.
     */
    public void setMethods(String methods)
    {
        _methods = methods;
    }

    /**
     * Doesn't do anything.
     *
     * @return SKIP_BODY
     */
    public int doStartTag() throws JspException
    {
        return SKIP_BODY;
    }

    /**
     * Writes out the stuff to be formatted.
     *
     * @return EVAL_PAGE
     */
    public int doEndTag() throws JspException
    {
        try
        {
            pageContext.getOut().write( formatAsString() );
        }
        catch(IOException e)
        {
            throw new JspException( "IOException: " + e.getMessage() );
        }
        return EVAL_PAGE;
    }

    /**
     * Formats the object as a string.
     *
     * @return String
     */
    protected String formatAsString()
    {
        String ret = ObjectFormatter.format(_object, _nullValue, _format, _methods);
        if (_maxChars > 0) ret = TextUtils.trunc(ret, _maxChars);
        return ret;
    }
}
