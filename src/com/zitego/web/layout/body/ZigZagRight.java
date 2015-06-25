package com.zitego.web.layout.body;

import com.zitego.web.layout.*;
import com.zitego.web.layout.section.*;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.*;

/**
 * This class represents a page body layout where an image or some section is in the
 * top left or right corner based on what row we are in. It starts with a right section
 * and then creates a left section, alternating back and forth. The alternate sections
 * can be anything, but text is recommended.
 *
 * @author John Glorioso
 * @version $Id: ZigZagRight.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class ZigZagRight extends ZigZagLeft
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        ZigZagRight t = new ZigZagRight( page.getBodyTag() );
        t.addToParent();

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new zig zag right layout with a HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected ZigZagRight(HtmlMarkupTag parent)
    {
        super(parent);
    }

    protected PageBodyLayoutType getNextType()
    {
        return (getSections().size() % 2 == 0 ? PageBodyLayoutType.TOP_RIGHT_CORNER : PageBodyLayoutType.TOP_LEFT_CORNER);
    }

    protected String getBeginComment()
    {
        return "Begin Zig Zag Right Section";
    }

    protected String getEndComment()
    {
        return "End Zig Zag Right Section";
    }

    public PageBodyLayoutType getType()
    {
        return PageBodyLayoutType.ZIG_ZAG_RIGHT;
    }
}