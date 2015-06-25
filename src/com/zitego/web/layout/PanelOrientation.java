package com.zitego.web.layout;

import com.zitego.util.Constant;
import java.util.Vector;

/**
 * A class that represents body panel orientations.
 *
 * @author John Glorioso
 * @version $Id: PanelOrientation.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class PanelOrientation extends Constant
{
    public static final PanelOrientation EAST = new PanelOrientation("East");
    public static final PanelOrientation WEST = new PanelOrientation("West");
    /** Gets incremented as format _orientations are initialized. */
    private static int _nextId = 0;
    private static Vector _orientations;

    /**
     * Creates a new PanelOrientation.
     *
     * @param String The description.
     */
    private PanelOrientation(String desc)
    {
        super(_nextId++, desc);
        if (_orientations == null) _orientations = new Vector();
        _orientations.add(this);
    }

    /**
     * Returns an PanelOrientation based on the id passed in. If the id does not match the id of
     * a constant, then we return null. If there are two constants with the same id, then
     * the first one is returned.
     *
     * @param int The constant id.
     * @return PanelOrientation
     */
    public static PanelOrientation evaluate(int id)
    {
        return (PanelOrientation)Constant.evaluate(id, _orientations);
    }

    /**
     * Returns an Constant based on the description passed in. If the description does not
     * match the description of a constant, then we return null. If there are two constants
     * with the same description, then the first one is returned.
     *
     * @param String The description.
     * @return PanelOrientation
     */
    protected static PanelOrientation evaluate(String name)
    {
        return (PanelOrientation)Constant.evaluate(name, _orientations);
    }

    public Vector getTypes()
    {
        return _orientations;
    }
}