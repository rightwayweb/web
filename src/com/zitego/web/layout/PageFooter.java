package com.zitego.web.layout;

import com.zitego.markup.Newline;
import com.zitego.markup.tag.CommentTag;
import com.zitego.markup.html.tag.table.*;
import com.zitego.format.*;
import org.w3c.dom.*;

/**
 * This class represents a page footer.
 *
 * @author John Glorioso
 * @version $Id: PageFooter.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class PageFooter extends PageSection
{
    /** A property to aid with padding xml output. */
    private boolean _formattingSelf = false;

    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        Table t = page.getBodyTag().createTable();
        t.setWidth("100%");
        Td cell = t.createRow().createCell();
        PageFooter f = new PageFooter(cell);
        f.addToParent();

        System.out.println("Html: \r\n"+page.format(FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+f.format(FormatType.XML));
    }

    /**
     * Creates a new page footer with a Td parent.
     *
     * @param Td The parent.
     */
    public PageFooter(Td parent)
    {
        super(parent);
        setName("footer_section");
        parent.setWidth("100%");
    }

    public void buildFromXml(Element root)
    {
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
        if ( child.getNodeName().equalsIgnoreCase("section") )
        {
            super.buildFromXml(child);
        }
        else
        {
            super.addChild(child);
        }
    }

    public boolean countDeepness(FormatType type)
    {
        if (_formattingSelf) return false;
        else return super.countDeepness(type);
    }

    protected String generateContent(FormatType type) throws UnsupportedFormatException
    {
        if (type != FormatType.XML) return super.generateContent(type);

        if ( hasChanged() )
        {
            StringBuffer ret = new StringBuffer();
            _formattingSelf = true;
            String padding = getPadding(type);
            _formattingSelf = false;
            ret.append(padding).append("<footer>").append(Newline.CHARACTER)
               .append( super.generateContent(type) ).append(Newline.CHARACTER)
               .append(padding).append("</footer>");
            return ret.toString();
        }
        else
        {
            return (String)getCachedContent(type);
        }
    }

    protected String getBeginComment()
    {
        return "Begin Footer Section";
    }

    protected String getEndComment()
    {
        return "End Footer Section";
    }
}