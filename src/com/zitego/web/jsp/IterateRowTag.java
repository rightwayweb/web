package com.zitego.web.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.*;

/**
 * Handles coloring rows based on the parent iterate tag.
 *
 * @author John Glorioso
 * @version $Id: IterateRowTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class IterateRowTag extends TagSupport
{
    /** Additional attributes to append to the row tag. */
	private String _attributes = "";
	/** Sets the row color. */
	private String _color;

    /**
     * Sets the attributes.
     *
     * @param String The additional attributes.
     */
	public void setAttributes(String attr)
	{
		_attributes = attr;
	}

    /**
     * Sets the row color. This overrides calls to getColor in the iterate tag.
     *
     * @param String The color.
     */
	public void setColor(String color)
	{
		_color = color;
	}

    /**
     * Writes out the row tag and returns EVAL_BODY_INCLUDE.
     *
     * @return int
     */
	public int doStartTag() throws JspException
	{
		try
		{
			if (_color == null)
			{
				IterateTag it = (IterateTag)findAncestorWithClass(this, IterateTag.class);
				_color = it.getColor();
			}
			pageContext.getOut().write("<tr bgcolor=\"" + _color + "\" " + _attributes + ">");
		}
		catch(java.io.IOException e)
		{
			throw new JspException( "IO Error: " + e.getMessage() );
		}
		return EVAL_BODY_INCLUDE;
	}

    /**
     * Writes out the closing row tag and returns SKIP_BODY.
     *
     * @return int
     */
	public int doAfterBody() throws JspException
	{
		try
		{
			pageContext.getOut().write("</tr>");
			_color = null;
		}
		catch(java.io.IOException e)
		{
			throw new JspException("IO Error: " + e.getMessage());
		}
		return BodyTag.SKIP_BODY;
	}
}