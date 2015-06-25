package com.zitego.web.layout.body;

import com.zitego.markup.html.tag.table.Td;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.Body;

/**
 * This class creates page body layouts depending on the type passed in.
 *
 * @author John Glorioso
 * @version $Id: PageBodyLayoutFactory.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see PageBodyLayoutType
 */
public class PageBodyLayoutFactory
{
    /**
     * Returns a PageBodyLayout given the parent cell and the type.
     *
     * @param PageBodyLayoutType The type.
     * @param Td The parent cell.
     * @return PageBodyLayout
     * @throws IllegalArgumentException if the type is not valid.
     */
    public static PageBodyLayout createLayout(PageBodyLayoutType type, Td parent) throws IllegalArgumentException
    {
        return createBodyLayout(type, parent);
    }

    /**
     * Returns a PageBodyLayout given the parent body tag and the type.
     *
     * @param PageBodyLayoutType The type.
     * @param Body The parent body tag.
     * @return PageBodyLayout
     * @throws IllegalArgumentException if the type is not valid.
     */
    public static PageBodyLayout createLayout(PageBodyLayoutType type, Body body) throws IllegalArgumentException
    {
        return createBodyLayout(type, body);
    }

    private static PageBodyLayout createBodyLayout(PageBodyLayoutType type, HtmlMarkupTag parent) throws IllegalArgumentException
    {
        PageBodyLayout ret = null;
        if (type == PageBodyLayoutType.TEXT) ret = new TextLayout(parent);
        else if (type == PageBodyLayoutType.TOP_CENTER) ret = new TopCenter(parent);
        else if (type == PageBodyLayoutType.TOP_LEFT) ret = new TopLeft(parent);
        else if (type == PageBodyLayoutType.TOP_LEFT_CORNER) ret = new TopLeftCorner(parent);
        else if (type == PageBodyLayoutType.TOP_RIGHT) ret = new TopRight(parent);
        else if (type == PageBodyLayoutType.TOP_RIGHT_CORNER) ret = new TopRightCorner(parent);
        else if (type == PageBodyLayoutType.BOTTOM_CENTER) ret = new BottomCenter(parent);
        else if (type == PageBodyLayoutType.ZIG_ZAG_LEFT) ret = new ZigZagLeft(parent);
        else if (type == PageBodyLayoutType.ZIG_ZAG_RIGHT) ret = new ZigZagRight(parent);
        else if (type == PageBodyLayoutType.THUMBNAILS) ret = new Thumbnails(parent);
        else if (type == PageBodyLayoutType.HOLDER) ret = new Holder(parent);
        else throw new IllegalArgumentException("Invalid page body layout type: "+type);
        ret.addToParent();
        return ret;
    }
}