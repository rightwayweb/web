package com.zitego.web.jsp;

import com.zitego.format.FormatType;
import com.zitego.markup.html.tag.Anchor;
import com.zitego.markup.html.tag.Img;
import com.zitego.style.ServerStyleSheet;
import com.zitego.web.servlet.BaseConfigServlet;
import java.util.Properties;
import javax.servlet.jsp.tagext.*;
import javax.servlet.jsp.*;

/**
 * Writes out a help link using the help.gif file in the /images directory. This will automatically write
 * out the javascript code to open a window. The only required param is the context. The window will open
 * to the index page of the given context by default, but you can optionally set an anchor name (see below),
 * the window name, width, and height. The help url can also be set, but by default it will assume /help of
 * the current domain. In addition, you may set a server style or client style to use. If they are both
 * specified, then the server style is used.
 *
 * If an anchor is not specified, a pagePath can be given that will be processed in order to create an
 * anchor based on the path or looking for a HELP_PAGE_MAP_KEY that is a Hashtable stored in the BaseConfigServlet's
 * webapp properties. See setPagePath for more details.
 *
 * @author John Glorioso
 * @version $Id: HelpLinkTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class HelpLinkTag extends TagSupport
{
    /** The kep name for the help page map. */
    public static final String HELP_PAGE_MAP_KEY = "help_page_map";
    /** The help context to use. */
    private String _context;
    /** The anchor to browse to. */
    private String _anchor;
    /** The page path to create the anchor. */
    private String _pagePath;
    /** The window name. Default is "help" */
    private String _windowName = "help";
    /** The window width. Default is 600. */
    private int _width = 600;
    /** The window height. Default is 450. */
    private int _height = 450;
    /** The help url. "/help" by default unless a "help.root_url" is in the BaseConfigServlet StaticWebappProperties. */
    private String _helpUrl;
    /** The image src. "/images/help.gif" by default. */
    private String _helpImage;
    /** Optional link text to use instead of an image. If this is specified, the image src will always be ignored. */
    private String _linkText;
    /** The optional server side style to use for the link text. */
    private String _style;
    /** The optional client side style to use for the link text. */
    private String _class;

    /**
     * Sets the context to use.
     *
     * @param context The context.
     */
    public void setContext(String context)
    {
        _context = context;
    }

    /**
     * Sets anchor to browse to.
     *
     * @param anchor The anchor.
     */
    public void setAnchor(String anchor)
    {
        _anchor = anchor;
    }

    /**
     * Sets the page path in order to create the anchor. This will always overwrite the anchor even if it
     * was set manually. A Properties object may or may not exist in BaseConfigServlet's webapp properties.
     * If so, it will be store as HELP_PAGE_MAP_KEY. The rules for creating the anchor are as follows:
     * <ol>
     *  <li>A check to BaseConfigServlet.getWebappProperties().getProperty(HELP_PAGE_MAP_KEY) is called.
     *      If the page map exists and the path is in it, then the anchor returned is used. Else...
     *  <li>If the path has a forward slash in front, it will be cut off.
     *  <li>The first character and first character after each underscore or forward slash is capitalized.
     * </ol>
     *
     * @param path The page path.
     */
    public void setPagePath(String path)
    {
        if ( path == null || "".equals(path) ) return;

        Object helpPageMap = BaseConfigServlet.getWebappProperties().getProperty(HELP_PAGE_MAP_KEY);
        if (helpPageMap != null && helpPageMap instanceof Properties)
        {
            _anchor = ( (Properties)helpPageMap ).getProperty(path);
            if (_anchor != null) return;
        }

        if (path.charAt(0) == '/') path = path.substring(1);
        StringBuffer anchor = new StringBuffer( path.substring(0, 1).toUpperCase() );
        char c = (char)0;
        for (int i=1; i<path.length(); i++)
        {
            c = path.charAt(i);
            if (c == '_' || c == '/') anchor.append(c).append( String.valueOf(path.charAt(++i)).toUpperCase() );
            else anchor.append(c);
        }
        _anchor = anchor.toString();
    }

    /**
     * Sets the window name.
     *
     * @param name The window name.
     */
    public void setWindowName(String name)
    {
        _windowName = name;
    }

    /**
     * Sets the width of the window.
     *
     * @param w The width.
     */
    public void setWidth(int w) { _width = w; }

    /**
     * Sets the width of the window.
     *
     * @param w The width.
     */
    public void setWidth(String w)
    {
        setWidth( Integer.parseInt(w) );
    }

    /**
     * Sets the height of the window.
     *
     * @param h The height.
     */
    public void setHeight(int h)
    {
        _height = h;
    }

    /**
     * Sets the height of the window.
     *
     * @param h The height.
     */
    public void setHeight(String h)
    {
        setHeight( Integer.parseInt(h) );
    }

    /**
     * Sets the help url.
     *
     * @param url The url to the help.
     */
    public void setHelpUrl(String url)
    {
        _helpUrl = url;
    }

    /**
     * Sets the image src.
     *
     * @param src The image src line.
     */
    public void setImageSrc(String src)
    {
        _helpImage = src;
    }

    /**
     * Sets the link text. An image will not be used when this is set.
     *
     * @param text The link text.
     */
    public void setLinkText(String text)
    {
        _linkText = text;
    }

    /**
     * Sets the server side style for the link text.
     *
     * @param style The style.
     */
    public void setStyle(String style)
    {
        _style = style;
    }

    /**
     * Sets the client side style for the link text. This must be a class
     * for a span tag.
     *
     * @param c The class.
     */
    public void setClassAttribute(String c)
    {
        _class = c;
    }

    /**
     * Writes out the window link using an Anchor class.
     *
     * @return SKIP_BODY
     */
    public int doStartTag() throws JspException
    {
        Anchor link = new Anchor();
        link.setIsEmbeddedInLine(true);
        String contextPath = ( (javax.servlet.http.HttpServletRequest)pageContext.getRequest() ).getContextPath();
        StringBuffer href = new StringBuffer("javascript:openWindow('");
        if (_helpUrl != null) href.append(_helpUrl);
        else if ( BaseConfigServlet.getWebappProperties().hasProperty("help.root_url") ) href.append( (String)BaseConfigServlet.getWebappProperties().getProperty("help.root_url") );
        else href.append("/help");
        if (_anchor != null) href.append("?").append(_context).append(".").append(_anchor);
        href.append("', '").append(_windowName).append("', ").append(_width)
            .append(", ").append(_height).append(")");
        link.setHref( href.toString() );
        link.setToolTip("View Help");
        if (_linkText != null)
        {
            if (_style != null) _linkText = ServerStyleSheet.getHTMLStartTag(_style) + _linkText + ServerStyleSheet.getHTMLEndTag(_style);
            else if (_class != null) _linkText = "<span class=\"" + _class + "\">" + _linkText + "</span>";
            link.addBodyContent(_linkText);
        }
        else
        {
            Img img = null;
            if (_helpImage != null) img = link.createImage(_helpImage);
            else img = link.createImage(contextPath+"/images/help.gif");
            img.setBorder(0);
            img.setAlt("View Help");
        }
        try
        {
            pageContext.getOut().write( link.format(FormatType.HTML) );
        }
        catch(Exception e)
        {
              throw new JspException( "Error: " + e.getMessage() );
        }
        return SKIP_BODY;
    }
}