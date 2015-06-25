package com.zitego.web.menu.button;

import com.zitego.web.menu.Menu;

/**
 * This class handles creating a specific type of menu button based on the
 * MenuButtonType passed in.
 *
 * @author John Glorioso
 * @version $Id: MenuButtonFactory.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class MenuButtonFactory
{
    /**
     * Returns a menu button given the type, a menu, and an HtmlMarkupTag parent.
     *
     * @param MenuButtonType The type.
     * @param Menu The parent menu.
     * @return MenuButton
     * @throws IllegalArgumentException when the type is invalid or the menu is null.
     */
    public static MenuButton createButton(MenuButtonType type, Menu menu)
    {
        MenuButton ret = null;
        if (type == MenuButtonType.NORMAL)
        {
            ret = new MenuButton(menu);
        }
        else if (type == MenuButtonType.RAISED)
        {
            ret = new RaisedMenuButton(menu);
        }
        else if (type == MenuButtonType.IMAGE_SWAPPED)
        {
            ret = new ImageSwappedMenuButton(menu);
        }
        else if (type == MenuButtonType.COLOR_SWAPPED)
        {
            ret = new ColorSwappedMenuButton(menu);
        }
        else
        {
            throw new IllegalArgumentException("Invalid menu button type: "+type);
        }
        return ret;
    }

    /**
     * Transforms the given button to the specified type.
     *
     * @param MenuButton The button to transform.
     * @param MenuButtonType The type to transform to.
     * @return MenuButton
     * @throws IllegalArgumentException if the button is null or the type is invalid.
     */
    public static MenuButton transformButton(MenuButton button, MenuButtonType type)
    {
        if (button == null) throw new IllegalArgumentException("button cannot be null");
        if (button.getType() == type) return button;

        MenuButton ret = null;
        Menu menu = button.getMenu();
        if (type == MenuButtonType.NORMAL)
        {
            ret = new MenuButton(menu);
        }
        else if (type == MenuButtonType.RAISED)
        {
            ret = new RaisedMenuButton(menu);
        }
        else if (type == MenuButtonType.IMAGE_SWAPPED)
        {
            ret = new ImageSwappedMenuButton(menu);
        }
        else if (type == MenuButtonType.COLOR_SWAPPED)
        {
            ret = new ColorSwappedMenuButton(menu);
        }
        else
        {
            throw new IllegalArgumentException("Invalid menu button type: "+type);
        }

        //First replace the id
        menu.replaceButton(button, ret);
        ret.loadWith(button);
        return ret;
    }
}