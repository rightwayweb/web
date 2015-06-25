package com.zitego.web.layout.body;

import com.zitego.web.layout.*;
import com.zitego.web.layout.section.*;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.*;

/**
 * This class represents a plain text body layout. This layout contains only a text page
 * section.
 *
 * @author John Glorioso
 * @version $Id: TextLayout.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class TextLayout extends PageBodyLayout
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        TextLayout t = new TextLayout( page.getBodyTag() );
        t.addToParent();

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a holder body layout with a HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected TextLayout(HtmlMarkupTag parent)
    {
        super(parent);
        if (parent instanceof Td)
        {
            ( (Td)parent ).setAlign("center");
            ( (Td)parent ).setValign("top");
        }
        else
        {
            setAlign("center");
        }
        //Create the row and cell this is in
        Td cell = createRow().createCell();
        cell.setAlign("center");
        setAlternateSection(PageSectionType.TEXT);
    }

    /**
     * Overrides to make sure that only a text section type is set.
     *
     * @param PageSectionType The page section type.
     * @return PageSection
     */
    public PageSection setMainSection(PageSectionType type)
    {
        if (type != PageSectionType.TEXT) throw new IllegalArgumentException("Can only add text types to TextLayout");
        return super.setMainSection(type);
    }

    /**
     * Overrides to set the alternate section in the main section.
     *
     * @param PageSectionType The type of section to set.
     * @return PageSection
     */
    public PageSection setAlternateSection(PageSectionType type)
    {
        return setMainSection(type);
    }

    public PageSection getAlternateSection()
    {
        return getMainSection();
    }

    protected Td getMainSectionCell()
    {
        return getFirstRow().getFirstCell();
    }

    protected Td getAlternateSectionCell()
    {
        return getFirstRow().getFirstCell();
    }

    public PageBodyLayoutType getType()
    {
        return PageBodyLayoutType.TEXT;
    }

    protected String getBeginComment()
    {
        return "Begin Text Section";
    }

    protected String getEndComment()
    {
        return "End Text Section";
    }
}