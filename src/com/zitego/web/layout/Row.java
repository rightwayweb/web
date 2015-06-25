package com.zitego.web.layout;

import com.zitego.format.*;
import com.zitego.markup.*;
import com.zitego.markup.xml.*;
import com.zitego.markup.html.tag.table.*;
import org.w3c.dom.*;

/**
 * This class is an extension of the Tr tag. It handles xml input and
 * output.
 *
 * @author John Glorioso
 * @version $Id: Row.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class Row extends Tr implements XmlConverter
{
    /**
     * Creates a new Row tag with a PageSection parent.
     *
     * @param PageSection The parent page section.
     */
    public Row(PageSection parent)
    {
        super(parent);
    }

    public Td createCell()
    {
        return addCell( new Cell(this) );
    }

    public Td createCellAt(int index, boolean replace)
    {
        return addCell(index, new Cell(this), replace);
    }

    public Th createHeaderCell()
    {
        return (Th)addCell( new ThCell(this) );
    }

    public void buildFromXml(Element root)
    {
        String val = root.getAttribute("align");
        if ( val != null && !"".equals(val) ) setAlign(val);
        val = root.getAttribute("bgcolor");
        if ( val != null && !"".equals(val) ) setBgColor(val);
        val = root.getAttribute("valign");
        if ( val != null && !"".equals(val) ) setValign(val);

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
        if ( child.getNodeName().equalsIgnoreCase("cell") )
        {
            Cell cell = (Cell)createCell();
            cell.buildFromXml(child);
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
            ret.append(padding).append("<row");
            if ( getAlign() != null) ret.append(" ").append( getAttribute("align") );
            if ( getBgColor() != null) ret.append(" ").append( getAttribute("bgcolor") );
            if ( getValign() != null) ret.append(" ").append( getAttribute("valign") );
            ret.append(">").append(Newline.CHARACTER);

            int size = getCells().size();
            for (int i=0; i<size; i++)
            {
                ret.append( getCell(i).format(type) ).append(Newline.CHARACTER);
            }

            ret.append(padding).append("</row>");
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