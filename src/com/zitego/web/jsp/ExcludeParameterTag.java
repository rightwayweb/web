package com.zitego.web.jsp;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * Specifies to the QueryStringTag which parameters to exclude.
 *
 * @author John Glorioso
 * @version $Id: ExcludeParameterTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class ExcludeParameterTag extends TagSupport
{
    /** The name of the parameter to exclude. */
    private String _name;

    /**
     * Sets the parameter to exclude.
     *
     * @param String The parameter to exclude.
     */
    public void setName(String name)
    {
        _name = name;
    }

    /**
     * Sets the param to exclude in the parent tag.
     *
     * @return SKIP_BODY
     */
    public int doStartTag() throws JspException
    {
        Tag tag = findAncestorWithClass(this, QueryStringTag.class);
        ( (QueryStringTag)tag ).exclude(_name);
        return SKIP_BODY;
    }
}