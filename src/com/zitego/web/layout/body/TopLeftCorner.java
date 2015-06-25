package com.zitego.web.layout.body;

import com.zitego.web.layout.*;
import com.zitego.web.layout.section.*;
import com.zitego.markup.MarkupContent;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.*;

/**
 * This class represents a page body layout where a an image or some section is in the
 * top left corner. The alternate section can be anything, but text is recommended.
 *
 * @author John Glorioso
 * @version $Id: TopLeftCorner.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class TopLeftCorner extends PageBodyLayout
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        TopLeftCorner t = new TopLeftCorner( page.getBodyTag() );
        t.addToParent();

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new top left corner body layout with a HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected TopLeftCorner(HtmlMarkupTag parent)
    {
        super(parent);
        //Setup left and right side
        Tr row = getFirstRow();
        Td cell = row.createCell();
        cell.setValign("top");
        setMainSection(PageSectionType.IMAGE);
        setAlternateSection(PageSectionType.TEXT);
    }

    protected Td getMainSectionCell()
    {
        return getFirstRow().getFirstCell();
    }

    protected Td getAlternateSectionCell()
    {
        return getFirstRow().getFirstCell();
    }

    /**
     * Creates and sets the top left corner section given the type and returns it. If there is already a
     * type, then it is discarded and the new one is used.
     *
     * @param PageSectionType The page section type.
     * @return PageSection
     */
    public PageSection setMainSection(PageSectionType type)
    {
        PageSection section = super.setMainSection(type);
        section.setAlign("left");
        section.setValign("top");
        MarkupContent parent = section.getParent();
        //Add the image section table before the last comment
        parent.addBodyContentAt(parent.getBodySize()-1, section);
        return section;
    }

    public PageBodyLayoutType getType()
    {
        return PageBodyLayoutType.TOP_LEFT_CORNER;
    }

    protected String getBeginComment()
    {
        return "Begin Top Left Corner Section";
    }

    protected String getEndComment()
    {
        return "End Top Left Corner Section";
    }
}