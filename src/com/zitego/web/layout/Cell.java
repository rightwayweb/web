package com.zitego.web.layout;

import com.zitego.markup.*;
import com.zitego.markup.tag.MarkupTag;
import com.zitego.markup.tag.CommentTag;
import com.zitego.markup.html.HtmlMarkupFactory;
import com.zitego.markup.html.tag.table.*;
import com.zitego.markup.xml.*;
import com.zitego.format.*;
import com.zitego.util.TextUtils;
import org.w3c.dom.*;

/**
 * This class is an extension of the Td tag. It handles xml input and output.
 *
 * @author John Glorioso
 * @version $Id: Cell.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class Cell extends Td implements XmlConverter
{
    /**
     * Creates a new Cell with a Row parent.
     *
     * @param Row The parent row.
     */
    public Cell(Row parent)
    {
        super(parent);
    }

    public void buildFromXml(Element root)
    {
        String val = root.getAttribute("align");
        if ( val != null && !"".equals(val) ) setAlign(val);
        val = root.getAttribute("bgcolor");
        if ( val != null && !"".equals(val) ) setBgColor(val);
        val = root.getAttribute("valign");
        if ( val != null && !"".equals(val) ) setValign(val);
        val = root.getAttribute("colspan");
        if ( val != null && !"".equals(val) ) setColspan( Integer.parseInt(val) );
        val = root.getAttribute("height");
        if ( val != null && !"".equals(val) ) setHeight(val);
        val = root.getAttribute("width");
        if ( val != null && !"".equals(val) ) setWidth(val);
        val = root.getAttribute("nowrap");
        if ( val != null && !"".equals(val) ) setNoWrap( new Boolean(val).booleanValue() );

        NodeList nodes = root.getChildNodes();
        int size = nodes.getLength();
        for (int i=0; i<size; i++)
        {
            Node n = nodes.item(i);
            if (n instanceof Element)
            {
                addChild( (Element)n );
            }
            else if (n instanceof Text)
            {
                String content = TextUtils.trim( n.getNodeValue() );
                if (content.length() > 1)
                {
                    try
                    {
                        content = XmlFormatter.unescape(content);
                        MarkupContent[] c = HtmlMarkupFactory.getInstance().parse
                        (
                            new StringBuffer(content), this, FormatType.HTML, true
                        );
                    }
                    catch (UnsupportedFormatException ufe)
                    {
                        //Just add it as text
                        addBodyContent(content);
                    }
                }
            }
        }
    }

    public void addChild(Element child)
    {
        if ( child.getNodeName().equalsIgnoreCase("section") )
        {
            //TO DO - nested sections here
        }
    }

    public void parse(Object objToParse, FormatType type) throws IllegalMarkupException, UnsupportedFormatException
    {
        if (objToParse instanceof Element && type == FormatType.XML) buildFromXml( (Element)objToParse );
        else super.parse(objToParse, type);
    }

    public boolean countDeepness(FormatType type)
    {
        Table parentTable = getParentTable();
        return
        (
            type == FormatType.XML &&
            !(parentTable instanceof com.zitego.web.layout.template.PageLayoutTemplate) &&
            !(parentTable instanceof com.zitego.web.layout.border.PageSectionBorder)
        );
    }

    protected String generateContent(FormatType type) throws UnsupportedFormatException
    {
        if (type != FormatType.XML) return super.generateContent(type);

        if ( hasChanged() )
        {
            StringBuffer ret = new StringBuffer();
            String padding = getPadding(type);
            ret.append(padding).append("<cell");
            if ( getAlign() != null) ret.append(" ").append( getAttribute("align") );
            if ( getBgColor() != null) ret.append(" ").append( getAttribute("bgcolor") );
            if ( getValign() != null) ret.append(" ").append( getAttribute("valign") );
            if ( getColspan() > 1) ret.append(" ").append( getAttribute("colspan") );
            if ( getHeight() != null) ret.append(" ").append( getAttribute("height") );
            if ( getWidth() != null) ret.append(" ").append( getAttribute("width") );
            if ( getNoWrap() ) ret.append(" nowrap=\"true\"");
            ret.append(">").append(Newline.CHARACTER);

            int size = getBodySize();
            if (size > 0) ret.append(padding).append(" ");
            for (int i=0; i<size; i++)
            {
                MarkupContent c = getBodyContent(i);
                if (c instanceof PageSection)
                {
                    ret.append( ((PageSection)c).format(type) );
                }
                else if ( !(c instanceof CommentTag) )
                {
                    //Need this to print out inline
                    String out = null;
                    if (c instanceof MarkupTag)
                    {
                        boolean inline = ( (MarkupTag)c ).isEmbeddedInLine();
                        if (!inline) ( (MarkupTag)c ).setIsEmbeddedInLine(true);
                        out = c.format(FormatType.XML);
                        if (!inline) ( (MarkupTag)c ).setIsEmbeddedInLine(false);
                    }
                    else
                    {
                        boolean hasPadding = c.hasPadding();
                        boolean newline = c.hasNewline();
                        if (hasPadding) c.setHasPadding(false);
                        if (newline) c.setHasNewline(false);
                        out = c.format(FormatType.XML);
                        if (hasPadding) c.setHasPadding(true);
                        if (newline) c.setHasNewline(true);
                    }
                    ret.append(out);
                }
            }
            if (size > 0) ret.append(Newline.CHARACTER);

            ret.append(padding).append("</cell>");
            return ret.toString();
        }
        else
        {
            return (String)getCachedContent(type);
        }
    }

    protected MarkupBody createMarkupBody()
    {
        return new PageLayoutMarkupBody(this);
    }
}