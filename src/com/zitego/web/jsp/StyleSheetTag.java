package com.zitego.web.jsp;

import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import com.zitego.style.ServerStyleSheet;

/**
 * A tag that uses the ServerStyleSheet class to format markup.
 *
 * @author John Glorioso
 * @version $Id: StyleSheetTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see ServerStyleSheet
 */
public class StyleSheetTag extends TagSupport
{
    /** The style text to format. */
  	String _text;
  	/** The style to use. */
  	String _style;
  	/** Any extra information to append to the style. */
  	String _extraInfo;

  	/*
   	 * Sets the text of the tag.
   	 *
   	 * @param String The text.
   	 */
  	public void setText(String text)
  	{
	    _text=text;
  	}

    /**
     * Sets the style of the tag.
     *
     * @param String The style.
     */
  	public void setStyle(String style)
  	{
	    _style=style;
  	}

    /**
     * Sets any extra information for the tag.
     *
     * @param String The extra info.
     */
  	public void setExtra(String extra)
  	{
	    _extraInfo=extra;
  	}

  	/**
   	 * Writes out the start tag. Returns EVAL_BODY.
   	 *
   	 * @return int
   	 */
  	public int doStartTag() throws JspException
  	{
  		try
  		{
  			if (_extraInfo != null) pageContext.getOut().write( ServerStyleSheet.getHTMLStartTag(_style, _extraInfo) );
  			else pageContext.getOut().write( ServerStyleSheet.getHTMLStartTag(_style) );
  		}
  		catch(java.io.IOException e)
  		{
      		throw new JspException( "IOException: " + e.getMessage() );
    	}
   		return EVAL_BODY_INCLUDE;
  	}

  	/**
   	 * Writes out the end tag. Returns EVAL_PAGE.
   	 *
   	 * @return int
   	 */
  	public int doEndTag() throws JspException
  	{
    	try
    	{
      		//include any text that is present in the tag
      		if (_text != null) pageContext.getOut().write(_text);
      		pageContext.getOut().write( ServerStyleSheet.getHTMLEndTag(_style) );
    	}
    	catch(java.io.IOException e)
    	{
      		throw new JspException( "IOException: " + e.getMessage() );
    	}
    	return EVAL_PAGE;
  	}
}