package com.zitego.web.menu.button;

import com.zitego.markup.Newline;
import com.zitego.format.FormatType;
import com.zitego.format.UnsupportedFormatException;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.Anchor;
import org.w3c.dom.Element;
import java.util.StringTokenizer;

/**
 * This class represents a menu link. The link is simply an extension of Anchor.
 *
 * @author John Glorioso
 * @see MenuButton#createButtonLink(String)
 */
public class ButtonLink extends Anchor
{
    /** The color of the display foreground. This is here rather than in ButtonDisplay because it is a link attribute. */
    private String _displayColor;

    /**
     * Creates a new MenuLink with an HtmlMarkupTag parent and a url. This is automatically added to
     * the parent's body.
     *
     * @param HtmlMarkupTag The parent.
     * @param String The url.
     */
    public ButtonLink(HtmlMarkupTag parent, String url)
    {
        super(parent);
        setHref(url);
    }

    /**
     * Sets the button display color.
     *
     * @param String The color.
     */
    public void setDisplayColor(String color)
    {
        _displayColor = color;
        //Need to set the style tag
        if (_displayColor != null) setStyle("color", _displayColor);
        else removeStyle("color");
    }

    public boolean countDeepness(FormatType type)
    {
        return (type == FormatType.XML);
    }

    public void buildFromXml(Element root)
    {
        String val = root.getAttribute("target");
        if (!"".equals(val) && val != null) setTarget(val);
        val = root.getAttribute("style");
        if (!"".equals(val) && val != null)
        {
            StringTokenizer st = new StringTokenizer(val, ";");
            while ( st.hasMoreTokens() )
            {
                String token = st.nextToken().trim();
                int index = token.indexOf(":");
                String style = null;
                if (index > -1)
                {
                    style = token.substring(0, index);
                    if ( "text-decoration".equals(style) ) setUnderlined(true);
                    else if ( "color".equals(style) ) setDisplayColor( token.substring(index+1).trim() );
                }
            }
        }
    }

    protected String generateContent(FormatType type) throws UnsupportedFormatException
    {
        if (type != FormatType.XML) return super.generateContent(type);

        if ( hasChanged() )
        {
            return new StringBuffer( getPadding(type) )
                .append("<link").append( getAttributes() ).append( getJsEventHandlers() ).append(" />").toString();
        }
        else
        {
            return (String)getCachedContent(type);
        }
    }
}