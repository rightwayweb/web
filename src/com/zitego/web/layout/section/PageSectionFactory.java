package com.zitego.web.layout.section;

import com.zitego.web.layout.PageSection;
import com.zitego.markup.html.tag.HtmlMarkupTag;

/**
 * This class creates page sections depending on the type passed in.
 *
 * @author John Glorioso
 * @version $Id: PageSectionFactory.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see PageSectionType
 */
public class PageSectionFactory
{
    /**
     * Returns a PageSection given the parent tag and the type.
     *
     * @param PageSectionType The type.
     * @param HtmlMarkupTag The parent.
     * @return PageSection
     * @throws IllegalArgumentException if the type is not valid.
     */
    public static PageSection createPageSection(PageSectionType type, HtmlMarkupTag parent) throws IllegalArgumentException
    {
        if (type == PageSectionType.GENERIC) return new GenericSection(parent);
        else if (type == PageSectionType.IMAGE) return new ImageSection(parent);
        else if (type == PageSectionType.TEXT) return new TextSection(parent);
        else throw new IllegalArgumentException("Invalid page section type: "+type);
    }
}