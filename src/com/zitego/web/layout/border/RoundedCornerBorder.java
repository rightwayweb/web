package com.zitego.web.layout.border;

import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.*;

/**
 * This represents a border with rounded corners. This is accomplished by creating
 * images of the four corners based on the padding of the table (width of the border)
 * and the background color of it. If either of those two attributes are changed, then
 * getCornerImages will need to be called again to get the new images.
 *
 * @author John Glorioso
 * @version $Id: RoundedCornerBorder.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class RoundedCornerBorder extends PageSectionBorder
{
    private Object[] _corners = null;

    /**
     * Creates a rounded corner border with a parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    public RoundedCornerBorder(HtmlMarkupTag parent)
    {
        super(parent);
    }

    public PageSectionBorderType getType()
    {
        return PageSectionBorderType.ROUNDED_CORNER;
    }

    public Object[] getCornerImages()
    {
        if (_corners == null) createCornerImages();
        return _corners;
    }

    /**
     * Creates the corner images.
     */
    private void createCornerImages()
    {
        //NW corner

        //NE corner

        //SE corner

        //SW corner
    }

    public void setBgColor(String col)
    {
        _corners = null;
        super.setBgColor(col);
    }
}