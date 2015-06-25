package com.zitego.web.menu.button;

import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.web.menu.Menu;

/**
 * This class handles creating a specific type of button display based on the
 * ButtonDisplayType passed in.
 *
 * @author John Glorioso
 * @version $Id: ButtonDisplayFactory.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class ButtonDisplayFactory
{
    /**
     * Returns a menu button given the type, a menu, and an HtmlMarkupTag parent.
     *
     * @param ButtonDisplayType The type.
     * @param HtmlMarkupTag The parent.
     * @return ButtonDisplay
     * @throws IllegalArgumentException when the type is invalid or the parent is null.
     */
    public static ButtonDisplay createButtonDisplay(ButtonDisplayType type, HtmlMarkupTag parent)
    {
        ButtonDisplay ret = null;
        if (type == ButtonDisplayType.IMAGE)
        {
            ret = new ImageButtonDisplay(parent);
        }
        else if (type == ButtonDisplayType.TEXT)
        {
            ret = new TextButtonDisplay(parent);
        }
        else
        {
            throw new IllegalArgumentException("Invalid menu button display type: "+type);
        }
        return ret;
    }
}