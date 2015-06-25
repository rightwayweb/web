package com.zitego.web.layout.body;

import com.zitego.web.layout.PageSection;
import com.zitego.web.layout.section.*;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.Td;

/**
 * This class represents a page body layout.
 *
 * @author John Glorioso
 * @version $Id: PageBodyLayout.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class PageBodyLayout extends PageSection
{
    /** The main section. */
    protected PageSection _main;
    /** The alternate section. */
    protected PageSection _alt;

    /**
     * Creates a new page body layout with an HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected PageBodyLayout(HtmlMarkupTag parent)
    {
        super(parent);
    }

    /**
     * Returns the table cell that the main section needs to reside in.
     *
     * @return Td
     */
    protected abstract Td getMainSectionCell();

    /**
     * Creates and sets the main section given the type and returns it. If there is already a
     * type, then it is discarded and the new one is used.
     *
     * @param PageSectionType The page section type.
     * @return PageSection
     */
    public PageSection setMainSection(PageSectionType type)
    {
        Td cell = getMainSectionCell();
        PageSection main = getMainSection();
        if (main != null) cell.removeBodyContent(main);
        main = PageSectionFactory.createPageSection(type, cell);
        _main = main;
        return _main;
    }


    /**
     * Returns the main section.
     *
     * @return PageSection
     */
    public PageSection getMainSection()
    {
        return _main;
    }

    /**
     * Returns the cell to add the alternate section to.
     *
     * @return Td
     */
    protected abstract Td getAlternateSectionCell();

    /**
     * Sets the alternate section in the second row.
     *
     * @param PageSectionType The type of section to set.
     * @return PageSection
     */
    public PageSection setAlternateSection(PageSectionType type)
    {
        Td cell = getAlternateSectionCell();
        PageSection alt = getAlternateSection();
        if (alt != null) cell.removeBodyContent(alt);
        alt = PageSectionFactory.createPageSection(type, cell);
        _alt = alt;
        return _alt;
    }

    /**
     * Returns the alternate section.
     *
     * @return PageSection
     */
    public PageSection getAlternateSection()
    {
        return _alt;
    }

    /**
     * Returns the type of page body layout type.
     *
     * @return PageBodyLayoutType
     */
    public abstract PageBodyLayoutType getType();
}