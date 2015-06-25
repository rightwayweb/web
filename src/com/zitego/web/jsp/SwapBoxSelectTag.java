package com.zitego.web.jsp;

import com.zitego.markup.MarkupContent;
import com.zitego.format.FormatType;
import com.zitego.markup.html.tag.form.Select;
import com.zitego.markup.html.tag.form.Option;
import com.zitego.markup.html.HtmlMarkupFactory;
import java.io.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 * This tag represents a select box in a SwapBoxTag. It must have a name, a type
 * (from or to), and a size.
 *
 * @author John Glorioso
 * @version $Id: SwapBoxSelectTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class SwapBoxSelectTag extends BodyTagSupport
{
    static final int FROM = 0;
    static final int TO = 1;
    /** The type of select (from or to) */
    protected int _type = FROM;
    /** The name of the select. */
    protected String _name;
    /** The size of the select. */
    protected int _size = 0;

    /**
     * Sets the type of select this is. If the type is "from" (in any case),
     * the it is the from tag. Otherwise, it is the to tag.
     *
     * @param String The type.
     */
    public void setType(String type)
    {
        if ( "from".equalsIgnoreCase(type) ) _type = FROM;
        else _type = TO;
    }

    /**
     * Sets the name of the tag.
     *
     * @param String The tag name.
     */
    public void setName(String name)
    {
        _name = name;
    }

    /**
     * Sets the size of the select object.
     *
     * @param int The size.
     */
    public void setSize(int size)
    {
        _size = size;
    }

    /**
     * Sets the size of the select object.
     *
     * @param String The size.
     */
    public void setSize(String size)
    {
        setSize( Integer.parseInt(size) );
    }

    /**
     * Doesn't do anything other than return SKIP_BODY.
     *
     * @return SKIP_BODY
     */
    public int doStartTag() throws javax.servlet.jsp.JspException
    {
        return EVAL_BODY_BUFFERED;
    }

    /**
     * Creates the select tag and assigns it to the parent SwapBoxTag.
     *
     * @return EVAL_PAGE
     */
    public int doEndTag() throws javax.servlet.jsp.JspException
    {
        SwapBoxTag parent = (SwapBoxTag)findAncestorWithClass(this, SwapBoxTag.class);
        Select sel = parent.createSelectTag(_type);
        sel.setName(_name);
        sel.setSize(_size);
        sel.setMultiple(true);

        //Now get the body content and parse it for options
        BodyContent body = getBodyContent();
        try
        {
            //Create the options for the select
            MarkupContent[] options = HtmlMarkupFactory.getInstance().parse
            (
                new StringBuffer( body.getString().trim() ),
                sel, FormatType.HTML, true
            );
            if (options != null)
            {
                for (int i=0; i<options.length; i++)
                {
                    //Only care about the options
                    if (options[i] instanceof Option)
                    {
                        ( (Option)options[i] ).setIsOnOwnLine(true);
                    }
                }
            }
        }
        catch (Exception e)
        {
            StringWriter errStr = new StringWriter();
            e.printStackTrace( new PrintWriter(errStr) );
            throw new JspException(errStr.toString(), e);
        }
        //Clear the content out so it don't get printed.
        body.clearBody();

        return EVAL_PAGE;
    }
}