package com.zitego.web.layout.border;

import com.zitego.util.Constant;
import java.util.Vector;

/**
 * Represents different page section border types.
 *
 * @author John Glorioso
 * @version $Id: PageSectionBorderType.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class PageSectionBorderType extends Constant
{
    public static final PageSectionBorderType SOLID_CORNER = new PageSectionBorderType("Solid Corner");
    public static final PageSectionBorderType ROUNDED_CORNER = new PageSectionBorderType("Rounded Corner");
    /** Gets incremented as list types are initialized. */
    private static int _nextId = 0;
    /** The method types. */
    private static Vector _types;

    /**
     * Creates a new border type.
     *
     * @param String The description.
     */
    private PageSectionBorderType(String desc)
    {
        super(_nextId++, desc);
        if (_types == null) _types = new Vector();
        _types.add(this);
    }

    /**
     * Returns an PageSectionBorderType based on the id passed in. If the id does not match the id of
     * a constant, then we return null. If there are two constants with the same id, then
     * the first one is returned.
     *
     * @param int The constant id.
     * @return PageSectionBorderType
     */
    public static PageSectionBorderType evaluate(int id)
    {
        return (PageSectionBorderType)Constant.evaluate(id, _types);
    }

    public Vector getTypes()
    {
        return _types;
    }
}