package com.zitego.web.layout.section;

import com.zitego.util.Constant;
import java.util.Vector;

/**
 * Represents different page section types.
 *
 * @author John Glorioso
 * @version $Id: PageSectionType.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class PageSectionType extends Constant
{
    public static final PageSectionType GENERIC = new PageSectionType("Generic");
    public static final PageSectionType IMAGE = new PageSectionType("Image");
    public static final PageSectionType TEXT = new PageSectionType("Text");
    /** Gets incremented as list types are initialized. */
    private static int _nextId = 0;
    /** The section types. */
    private static Vector _types;

    /**
     * Creates a new List.
     *
     * @param String The description.
     */
    private PageSectionType(String desc)
    {
        super(_nextId++, desc);
        if (_types == null) _types = new Vector();
        _types.add(this);
    }

    /**
     * Returns an PageSectionType based on the id passed in. If the id does not match the id of
     * a constant, then we return null. If there are two constants with the same id, then
     * the first one is returned.
     *
     * @param int The constant id.
     * @return PageSectionType
     */
    public static PageSectionType evaluate(int id)
    {
        return (PageSectionType)Constant.evaluate(id, _types);
    }

    /**
     * Returns an Constant based on the description passed in. If the description does not
     * match the description of a constant, then we return null. If there are two constants
     * with the same description, then the first one is returned.
     *
     * @param String The description.
     * @return PageSectionType
     */
    protected static PageSectionType evaluate(String name)
    {
        return (PageSectionType)Constant.evaluate(name, _types);
    }

    public Vector getTypes()
    {
        return _types;
    }
}