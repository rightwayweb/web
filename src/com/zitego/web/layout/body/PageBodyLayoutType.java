package com.zitego.web.layout.body;

import com.zitego.util.Constant;
import java.util.Vector;

/**
 * Represents different body layout types.
 *
 * @author John Glorioso
 * @version $Id: PageBodyLayoutType.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class PageBodyLayoutType extends Constant
{
    public static final PageBodyLayoutType TEXT = new PageBodyLayoutType
    (
        "Text", "text.gif", "A plain centered text layout."
    );
    public static final PageBodyLayoutType TOP_CENTER = new PageBodyLayoutType
    (
        "Top Center", "top_center.gif", "Image at the top center with text below."
    );
    public static final PageBodyLayoutType TOP_LEFT = new PageBodyLayoutType
    (
        "Top Left", "top_left.gif", "Image at the top left with text to the right of it."
    );
    public static final PageBodyLayoutType TOP_LEFT_CORNER = new PageBodyLayoutType
    (
        "Top Left Corner", "top_left_corner.gif", "Image in the top left corner with text beside and below it."
    );
    public static final PageBodyLayoutType TOP_RIGHT = new PageBodyLayoutType
    (
        "Top Right", "top_right.gif", "Image at the top right with text to the left of it."
    );
    public static final PageBodyLayoutType TOP_RIGHT_CORNER = new PageBodyLayoutType
    (
        "Top Right Corner", "top_right_corner.gif", "Image in the top right corner with text to the left and below it."
    );
    public static final PageBodyLayoutType BOTTOM_CENTER = new PageBodyLayoutType
    (
        "Bottom Center", "bottom_center.gif", "Image centered at the bottom with text above it."
    );
    public static final PageBodyLayoutType ZIG_ZAG_LEFT = new PageBodyLayoutType
    (
        "Zig Zag Left", "zig_zag_left.gif", "Images starting in the top left corner and alternating left and right."
    );
    public static final PageBodyLayoutType ZIG_ZAG_RIGHT = new PageBodyLayoutType
    (
        "Zig Zag Right", "zig_zag_right.gif", "Images starting in the top right corner and alternating right and left."
    );
    public static final PageBodyLayoutType THUMBNAILS = new PageBodyLayoutType
    (
        "Thumbnails", "thumbnails.gif", "Rows of thumbnailed images with optional comments."
    );
    public static final PageBodyLayoutType HOLDER = new PageBodyLayoutType
    (
        "Holder", null, "Not an actual layout type."
    );
    /** The name. */
    private String _name;
    /** The icon file name. */
    private String _icon;
    /** Gets incremented as list types are initialized. */
    private static int _nextId = 0;
    /** The method types. */
    private static Vector _types;

    /**
     * Creates a new List.
     *
     * @param String The name.
     * @param String The icon file name.
     * @param String The description.
     */
    private PageBodyLayoutType(String name, String icon, String desc)
    {
        super(_nextId++, desc);
        _name = name;
        _icon = icon;
        if (_types == null) _types = new Vector();
        _types.add(this);
    }

    /**
     * Returns an PageBodyLayoutType based on the id passed in. If the id does not match the id of
     * a constant, then we return null. If there are two constants with the same id, then
     * the first one is returned.
     *
     * @param int The constant id.
     * @return PageBodyLayoutType
     */
    public static PageBodyLayoutType evaluate(int id)
    {
        return (PageBodyLayoutType)Constant.evaluate(id, _types);
    }

     /**
     * Returns the template name.
     *
     * @return String
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Returns the icon filename.
     *
     * @return String
     */
    public String getIcon()
    {
        return _icon;
    }

    public Vector getTypes()
    {
        return _types;
    }

    public String toString()
    {
        StringBuffer ret = new StringBuffer()
            .append("[").append(_name).append(",").append( getValue() ).append("]");
        return ret.toString();
    }
}