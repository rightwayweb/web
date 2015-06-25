package com.zitego.web.menu.button;

import com.zitego.format.*;
import com.zitego.markup.IllegalMarkupException;
import com.zitego.markup.MarkupContent;
import com.zitego.markup.Newline;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.Img;
import org.w3c.dom.*;

/**
 * This is an image menu button to show an image.
 *
 * @author John Glorioso
 * @version $Id: ImageButtonDisplay.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see MenuButton#createImageDisplay(String)
 */
public class ImageButtonDisplay extends Img implements ButtonDisplay
{
    /**
     * Creates a new image button display with a menu button parent.
     *
     * @param HtmlMarkupTag The parent html tag.
     */
    public ImageButtonDisplay(HtmlMarkupTag parent)
    {
        super(parent);
        setBorder(0);
    }

    /**
     * Creates a new image button display with a menu button parent and the specified source url.
     *
     * @param HtmlMarkupTag The parent html tag.
     * @param String The image src url.
     */
    public ImageButtonDisplay(HtmlMarkupTag parent, String src)
    {
        super(parent, src);
        setBorder(0);
    }

    public ButtonDisplayType getType()
    {
        return ButtonDisplayType.IMAGE;
    }

    public void buildFromXml(Element root)
    {
        //See if we have a text effect or not
        NodeList nodes = root.getChildNodes();
        int size = nodes.getLength();
        for (int i=0; i<size; i++)
        {
            Node n = nodes.item(i);
            if (n instanceof Element)
            {
                addChild( (Element)n );
            }
        }
    }

    public void addChild(Element child)
    {
        String name = child.getNodeName().toLowerCase();
        if ( name.equals("image") )
        {
            setSrc( child.getAttribute("src") );
            String val = child.getAttribute("border");
            if ( val != null && !"".equals(val) ) setBorder( Integer.parseInt(val) );
            val = child.getAttribute("align");
            if ( val != null && !"".equals(val) ) setAlign(val);
            val = child.getAttribute("alt");
            if ( val != null && !"".equals(val) ) setAlt(val);
            val = child.getAttribute("height");
            if ( val != null && !"".equals(val) ) setHeight( Integer.parseInt(val) );
            val = child.getAttribute("name");
            if ( val != null && !"".equals(val) ) setName(val);
            val = child.getAttribute("width");
            if ( val != null && !"".equals(val) ) setWidth(val);
        }
    }

    public void parse(Object objToParse, FormatType type) throws IllegalMarkupException, UnsupportedFormatException
    {
        if (objToParse instanceof Element && type == FormatType.XML) buildFromXml( (Element)objToParse );
        else super.parse(objToParse, type);
    }

    public boolean countDeepness(FormatType type)
    {
        return ( type == FormatType.XML && !(getParent() instanceof ButtonLink) );
    }

    public String generateContent(FormatType type) throws UnsupportedFormatException
    {
        if (type != FormatType.XML) return super.generateContent(type);

        if ( hasChanged() )
        {
            StringBuffer ret = new StringBuffer()
                .append( getPadding(type) ).append("<display ")
                .append("type=\"").append( getType().getValue() ).append("\">")
                .append("<image").append( getAttributes() ).append(" /></display>");
            return ret.toString();
        }
        else
        {
            return (String)getCachedContent(type);
        }
    }
}