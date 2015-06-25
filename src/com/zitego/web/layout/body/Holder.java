package com.zitego.web.layout.body;

import com.zitego.web.layout.*;
import com.zitego.web.layout.section.*;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.*;

/**
 * This class represents a holder page body layout. This layout cannot be used for anything
 * it is simply here to hold the space of the body in a page layout so that other areas can
 * be configured first.
 *
 * @author John Glorioso
 * @version $Id: Holder.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class Holder extends PageBodyLayout
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        Holder t = new Holder( page.getBodyTag() );
        t.addToParent();

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a holder body layout with a HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected Holder(HtmlMarkupTag parent)
    {
        super(parent);
        createEmptyRows(5);
        Td cell = createRow().createCell();
        cell.setAlign("center");
        cell.addBodyContent("BODY CONTENT WILL GO HERE");
        createEmptyRows(5);
    }

    /**
     * This returns null.
     */
    protected Td getMainSectionCell()
    {
        return null;
    }

    /**
     * This returns null.
     */
    protected Td getAlternateSectionCell()
    {
        return null;
    }

    public PageBodyLayoutType getType()
    {
        return PageBodyLayoutType.HOLDER;
    }

    protected String getBeginComment()
    {
        return null;
    }

    protected String getEndComment()
    {
        return null;
    }
}