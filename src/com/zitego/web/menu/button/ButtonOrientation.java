package com.zitego.web.menu.button;

import com.zitego.util.Constant;
import java.util.Vector;

/**
 * A that reprensts menu button orientations. The only two orientations are horizontal
 * and vertical.
 *
 * @author John Glorioso
 * @version $Id: ButtonOrientation.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class ButtonOrientation extends Constant
{
    public static final ButtonOrientation HORIZONTAL = new ButtonOrientation("Horizontal");
    public static final ButtonOrientation VERTICAL = new ButtonOrientation("Vertical");
    /** Gets incremented as format _types are initialized. */
    private static int _nextId = 0;
    private static Vector _types;

    /**
     * Creates a new ButtonOrientation.
     *
     * @param String The description.
     */
    private ButtonOrientation(String desc)
    {
        super(_nextId++, desc);
        if (_types == null) _types = new Vector();
        _types.add(this);
    }

    /**
     * Returns an ButtonOrientation based on the id passed in. If the id does not match the id of
     * a constant, then we return null. If there are two constants with the same id, then
     * the first one is returned.
     *
     * @param int The constant id.
     * @return ButtonOrientation
     */
    public static ButtonOrientation evaluate(int id)
    {
        return (ButtonOrientation)Constant.evaluate(id, _types);
    }

    /**
     * Returns an Constant based on the description passed in. If the description does not
     * match the description of a constant, then we return null. If there are two constants
     * with the same description, then the first one is returned.
     *
     * @param String The description.
     * @return ButtonOrientation
     */
    protected static ButtonOrientation evaluate(String name)
    {
        return (ButtonOrientation)Constant.evaluate(name, _types);
    }

    public Vector getTypes()
    {
        return _types;
    }
}