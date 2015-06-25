package com.zitego.web.layout.body;

import com.zitego.web.layout.*;
import com.zitego.web.layout.section.*;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.*;

/**
 * This class represents a page body layout where a an image or some section is on the
 * top right. The section on the left can be anything.
 *
 * @author John Glorioso
 * @version $Id: TopRight.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class TopRight extends PageBodyLayout
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        TopRight t = new TopRight( page.getBodyTag() );
        t.addToParent();

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new top right body layout with a HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected TopRight(HtmlMarkupTag parent)
    {
        super(parent);
        if (parent instanceof Td)
        {
            ( (Td)parent ).setAlign("center");
            ( (Td)parent ).setValign("top");
        }
        //Setup left and right side
        Tr row = getFirstRow();
        Td cell = row.createCell();
        cell.setAlign("left");
        cell.setValign("top");
        cell = row.createCell();
        cell.setAlign("right");
        setMainSection(PageSectionType.IMAGE);
        setAlternateSection(PageSectionType.TEXT);
    }

    protected Td getMainSectionCell()
    {
        return getFirstRow().getLastCell();
    }

    protected Td getAlternateSectionCell()
    {
        return getFirstRow().getFirstCell();
    }

    public PageBodyLayoutType getType()
    {
        return PageBodyLayoutType.TOP_RIGHT;
    }

    protected String getBeginComment()
    {
        return "Begin Top Right Section";
    }

    protected String getEndComment()
    {
        return "End Top Right Section";
    }
}