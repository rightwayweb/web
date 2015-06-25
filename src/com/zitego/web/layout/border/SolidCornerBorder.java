package com.zitego.web.layout.border;

import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.*;

/**
 * This represents a border with solid (square) corners. No corner images are returned
 * because none are needed. Coloring the background of this table handles that.
 *
 * @author John Glorioso
 * @version $Id: SolidCornerBorder.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class SolidCornerBorder extends PageSectionBorder
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        SolidCornerBorder b = new SolidCornerBorder( page.getBodyTag() );

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+b.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a solid corner border with a parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    public SolidCornerBorder(HtmlMarkupTag parent)
    {
        super(parent);
    }

    public PageSectionBorderType getType()
    {
        return PageSectionBorderType.SOLID_CORNER;
    }

    public Object[] getCornerImages()
    {
        return new Object[0];
    }
}