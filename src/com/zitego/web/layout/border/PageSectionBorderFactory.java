package com.zitego.web.layout.border;

import com.zitego.markup.html.tag.HtmlMarkupTag;

/**
 * This class creates page section borders depending on the type passed in.
 *
 * @author John Glorioso
 * @version $Id: PageSectionBorderFactory.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see PageSectionBorderType
 */
public class PageSectionBorderFactory
{
    /**
     * Returns a PageSectionBorder given the parent cell and the type.
     *
     * @param PageSectionBorderType The type.
     * @param HtmlMarkupTag The parent.
     * @return PageSectionBorder
     * @throws IllegalArgumentException if the type is not valid.
     */
    public static PageSectionBorder createBorder(PageSectionBorderType type, HtmlMarkupTag parent) throws IllegalArgumentException
    {
        if (type == PageSectionBorderType.SOLID_CORNER) return new SolidCornerBorder(parent);
        else if (type == PageSectionBorderType.ROUNDED_CORNER) return new RoundedCornerBorder(parent);
        else throw new IllegalArgumentException("Invalid page section border type: "+type);
    }
}