package com.zitego.web.jsp;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;
import com.zitego.markup.html.tag.Anchor;

/**
 * Handles writing out html links. You can set all the standard properties of an html anchor/event
 * driven tag. The properties are listed below:<br>
 *
 * Properties:<br>
 * <b>href</b> - The link destination.<br>
 * <b>name</b> - The name of the link.<br>
 * <b>linkText</b> - What to display as the text. (optional, this can be the body of the tag instead)<br>
 * <b>toolTip</b> - An optional text tip to display in the status bar on the onMouseOver event.
 *                  (will not interfere with any manual onMouseOver setting)<br>
 * <b>target</b> - An optional window or frame to launch to.<br>
 * <b>onClick</b> - The javascript to execute when the onClick event is called.<br>
 * <b>onMouseOver</b> - The javascript to execute when the onMouseOver event is called.<br>
 * <b>onMouseOut</b> - The javascript to execute when the onMouseOut event is called.<br>
 * <b>onMouseDown</b> - The javascript to execute when the onMouseDown event is called.<br>
 * <b>onMouseUp</b> - The javascript to execute when the onMouseUp event is called.<br>
 * <b>args</b> - Window arguments when launching to a new window.<br>
 * <b>canUseJs</b> - Whether or not the client browser supports Javascript.<br>
 * <b>isWindow</b> - Whether the link is to a window.<br>
 * <b>tabindex</b> - The tab index of the link.<br>
 * <b>classAttribute</b> - The stylesheet class for the anchor tag.
 * <b>style</b> - The style specification for the anchor tag.
 * <b>contextPath</b> - The context path for the prefix of the url.
 *
 * @author John Glorioso
 * @version $Id: LinkTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class LinkTag extends TagSupport
{
    /** The destination. */
    protected String _href;
    /** The link name. */
    protected String _name;
    /** The link text. */
    protected String _linkText;
    /** The tool tip. */
    protected String _toolTip;
    /** The target. */
    protected String _target;
    /** The onclick action. */
    protected String _onClick;
    /** The mouse down event. */
    protected String _mouseDown;
    /** The mouse up event. */
    protected String _mouseUp;
    /** The mouse over event. */
    protected String _mouseOver;
    /** The mouse out event. */
    protected String _mouseOut;
    /** The open window arguments. */
    protected String _args;
    /** Whether they can use javascript. */
    protected boolean _canUseJs = true;
    /** Whether this is a window popup. */
    protected boolean _isWindow = false;
    /** The tab index of the link. */
    protected int _tabIndex = -1;
    /** The class attribute for the anchor tag. */
    protected String _class;
    /** The style attribute for the anchor tag. */
    protected String _style;
    /** The context path for the prefix of the url. */
    protected String _contextPath;

    /**
     * Sets the href.
     *
     * @param href The href.
     */
    public void setHref(String href)
    {
        _href = href;
    }

    /**
     * Sets the name.
     *
     * @param name The name.
     */
    public void setName(String name)
    {
        _name = name;
    }

    /**
     * Sets the links text.
     *
     * @param txt The link text.
     */
    public void setLinkText(String txt)
    {
        _linkText = txt;
    }

    /**
     * Sets the tool tip.
     *
     * @param tip The tool tip.
     */
    public void setToolTip(String tip)
    {
        _toolTip = tip;
    }

    /**
     * Sets the target.
     *
     * @param target The target of the href.
     */
    public void setTarget(String target)
    {
        _target = target;
    }

    /**
     * Sets the onclick handler.
     *
     * @param handler The onlick javascript text.
     */
    public void setOnClick(String handler)
    {
        _onClick = handler;
    }

    /**
     * Sets the onMouseDown handler.
     *
     * @param handler the onmousedown javascript text.
     */
    public void setOnMouseDown(String handler)
    {
        _mouseDown = handler;
    }

    /**
     * Sets the onMouseUp handler.
     *
     * @param handler The onmouseup javascript text.
     */
    public void setOnMouseUp(String handler)
    {
        _mouseUp = handler;
    }

    /**
     * Sets the onMouseOver handler.
     *
     * @param handler The onmouseover javascript text.
     */
    public void setOnMouseOver(String handler)
    {
        _mouseOver = handler;
    }

    /**
     * Sets the onMouseOut handler.
     *
     * @param onmouseout The onmouseout javascript text.
     */
    public void setOnMouseOut(String handler)
    {
        _mouseOut = handler;
    }

    /**
     * Sets the window arguments.
     *
     * @param args The window javscript argument text.
     */
    public void setWinArgs(String args)
    {
        _args = args;
    }

    /**
     * Sets whether they can use javascript or not.
     *
     * @param flag Whether javascript should be used.
     */
    public void setCanUseJs(boolean flag)
    {
        _canUseJs = flag;
    }

    /**
     * Sets whether this is a window or not.
     *
     * @param flag Whether the link should open a window.
     */
    public void setIsWindow(boolean flag)
    {
        _isWindow = flag;
    }

    /**
     * Sets the tab index of the tag.
     *
     * @param index The index.
     */
    public void setTabindex(int index)
    {
        _tabIndex = index;
    }

    /**
     * Sets the tab index of the tag.
     *
     * @param index The index.
     */
    public void setTabindex(String index)
    {
        setTabindex( Integer.parseInt(index) );
    }

    /**
     * Sets the style class attribute of the tag.
     *
     * @param c The class attribute.
     */
    public void setClassAttribute(String c)
    {
        _class = c;
    }

    /**
     * Sets the style specification.
     *
     * @param style The style spec.
     */
    public void setStyle(String style)
    {
        _style = style;
    }

    /**
     * Sets the context path for the prefix of the href. Default is request.getContextPath().
     *
     * @param path The class attribute.
     */
    public void setContextPath(String path)
    {
        _contextPath = path;
    }

    /**
     * Method called before body.
     *
     * @return either a EVAL_BODY or a SKIP_BODY
     */
    public int doStartTag() throws JspException
    {
        Anchor link = new Anchor();
        link.setIsOnOwnLine(true);
        if (_href.indexOf("javascript:") != 0 && _href.indexOf("http:") != 0 && _href.indexOf("https:") != 0 && _href.indexOf("#") != 0)
        {
            if (_contextPath != null) _href = _contextPath + _href;
            else _href = ((javax.servlet.http.HttpServletRequest)pageContext.getRequest()).getContextPath() + _href;
        }
        if (_isWindow)
        {
            if (_canUseJs) _href = "javascript:openWindow('" + _href + "', " + _args + ")";
            else _target = "_blank";
        }
        link.setHref(_href);
        if (getId() != null) link.setIdAttribute( getId() );
        if (_toolTip != null) link.setToolTip(_toolTip);
        if (_name != null) link.setNameAttribute(_name);
        if (_target != null) link.setTarget(_target);
        if (_onClick != null) link.setOnClick(_onClick);
        if (_mouseDown != null) link.setOnMouseDown(_mouseDown);
        if (_mouseUp != null) link.setOnMouseUp(_mouseUp);
        if (_mouseOver != null) link.setOnMouseOver(_mouseOver);
        if (_mouseOut != null) link.setOnMouseOut(_mouseOut);
        if (_tabIndex > -1) link.setTabIndex(_tabIndex);
        if (_class != null) link.setClassAttribute(_class);
        if (_style != null)
        {
            int index = _style.indexOf(":");
            if (index == -1) throw new JspException("Style must be in the format of \"<style>: <declaration>\"");
            link.setStyle( _style.substring(0, index).trim(), _style.substring(index+1).trim() );
        }

        try
        {
            pageContext.getOut().write( link.getStartTag(com.zitego.format.FormatType.HTML) );
        }
        catch(Exception e)
        {
              throw new JspException( "An error occurred writing out the tag: " + e.getMessage() );
        }

        return EVAL_BODY_INCLUDE;
    }

    /**
     * Method Called after body.
     *
     * @return either EVAL_PAGE or SKIP_PAGE
     */
    public int doEndTag() throws javax.servlet.jsp.JspException
    {
        try
        {
            //include a link if needed
            JspWriter out = pageContext.getOut();
            if (_linkText != null) out.write(_linkText);
            out.write("</a>");
        }
        catch(IOException e)
        {
              throw new JspException( "IOException: " + e.getMessage() );
        }
        return EVAL_PAGE;
      }
}
