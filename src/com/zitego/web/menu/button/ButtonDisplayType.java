package com.zitego.web.menu.button;

import java.util.Vector;
import com.zitego.util.Constant;

/**
 * This constant class defines different button display types.
 *
 * @author John Glorioso
 * @version $Id: ButtonDisplayType.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public final class ButtonDisplayType extends Constant
{
    public static final ButtonDisplayType IMAGE = new ButtonDisplayType("Image");
    public static final ButtonDisplayType TEXT = new ButtonDisplayType("Text");
    /** Gets incremented as format types are initialized. */
    private static int _nextId = 0;
    /** To keep track of each type. */
    private static Vector _types;

    /**
     * Creates a new ButtonDisplayType given the description.
     *
     * @param String The description.
     */
    private ButtonDisplayType(String desc)
    {
        super(_nextId++, desc);
        if (_types == null) _types = new Vector();
        _types.add(this);
    }

    /**
     * Returns an ButtonDisplayType based on the id passed in. If the id does not match the id of
     * a constant, then we return null. If there are two constants with the same id, then
     * the first one is returned.
     *
     * @param int The constant id.
     * @return ButtonDisplayType
     */
    public static ButtonDisplayType evaluate(int id)
    {
        return (ButtonDisplayType)Constant.evaluate(id, _types);
    }

    /**
     * Returns an Constant based on the description passed in. If the description does not
     * match the description of a constant, then we return null. If there are two constants
     * with the same description, then the first one is returned.
     *
     * @param String The description.
     * @return ButtonDisplayType
     */
    protected static ButtonDisplayType evaluate(String name)
    {
        return (ButtonDisplayType)Constant.evaluate(name, _types);
    }

    public Vector getTypes()
    {
        return _types;
    }
}