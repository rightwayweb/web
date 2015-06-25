package com.zitego.web.menu.button;

import java.util.Vector;
import com.zitego.util.Constant;

/**
 * This constant class defines different menu button types. NORMAL is a static
 * button that does nothing special. RAISED is a button that appears raised
 * when the mouse hovers over it, pressed when the mouse is down, and normal
 * when it is unobserved. IMAGE_SWAPPED is a button that has an image that is
 * swapped with another when the mouse goes over the button.
 *
 * @author John Glorioso
 * @version $Id: MenuButtonType.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public final class MenuButtonType extends Constant
{
    public static final MenuButtonType NORMAL = new MenuButtonType("Normal");
    public static final MenuButtonType RAISED = new MenuButtonType("Raised");
    public static final MenuButtonType IMAGE_SWAPPED = new MenuButtonType("Image Swapped");
    public static final MenuButtonType COLOR_SWAPPED = new MenuButtonType("Color Swapped");
    /** Gets incremented as format types are initialized. */
    private static int _nextId = 0;
    /** To keep track of each type. */
    private static Vector _types;

    /**
     * Creates a new MenuButtonType given the description.
     *
     * @param String The description.
     */
    private MenuButtonType(String desc)
    {
        super(_nextId++, desc);
        if (_types == null) _types = new Vector();
        _types.add(this);
    }

    /**
     * Returns an MenuButtonType based on the id passed in. If the id does not match the id of
     * a constant, then we return null. If there are two constants with the same id, then
     * the first one is returned.
     *
     * @param int The constant id.
     * @return MenuButtonType
     */
    public static MenuButtonType evaluate(int id)
    {
        return (MenuButtonType)Constant.evaluate(id, _types);
    }

    /**
     * Returns an Constant based on the description passed in. If the description does not
     * match the description of a constant, then we return null. If there are two constants
     * with the same description, then the first one is returned.
     *
     * @param String The description.
     * @return MenuButtonType
     */
    protected static MenuButtonType evaluate(String name)
    {
        return (MenuButtonType)Constant.evaluate(name, _types);
    }

    public Vector getTypes()
    {
        return _types;
    }
}