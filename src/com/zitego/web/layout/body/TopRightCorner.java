package com.zitego.web.layout.body;

import com.zitego.web.layout.*;
import com.zitego.web.layout.section.*;
import com.zitego.markup.MarkupContent;
import com.zitego.markup.html.tag.table.*;
import com.zitego.markup.html.tag.HtmlMarkupTag;

/**
 * This class represents a page body layout where an image or some section is in the
 * top right corner. The alternate section can be anything, but text is recommended.
 *
 * @author John Glorioso
 * @version $Id: TopRightCorner.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class TopRightCorner extends TopLeftCorner
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        TopRightCorner t = new TopRightCorner( page.getBodyTag() );
        t.addToParent();

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new top right corner body layout with a HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected TopRightCorner(HtmlMarkupTag parent)
    {
        super(parent);
        getMainSection().setAlign("right");
    }

    public PageBodyLayoutType getType()
    {
        return PageBodyLayoutType.TOP_RIGHT_CORNER;
    }

    protected String getBeginComment()
    {
        return "Begin Top Right Corner Section";
    }

    protected String getEndComment()
    {
        return "End Top Right Corner Section";
    }
}