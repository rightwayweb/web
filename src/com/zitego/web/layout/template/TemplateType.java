package com.zitego.web.layout.template;

import com.zitego.util.Constant;
import java.util.Vector;
import java.util.regex.*;

/**
 * Represents different page layout templates.
 *
 * @author John Glorioso
 * @version $Id: TemplateType.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class TemplateType extends Constant
{
    private static Pattern _idPattern = Pattern.compile("\\d+");
    public static final TemplateType HEADER = new TemplateType
    (
        "Header, Body", "header.gif",
        "Contains a custom header area with or without a logo and a customizable body area."
    );
    public static final TemplateType HEADER_FOOTER = new TemplateType
    (
        "Header, Body, Footer", "header_footer.gif",
        "Contains a custom header area with or without a logo, a customizable body area, and a custom footer area."
    );
    public static final TemplateType HEADER_LEFT_MENU = new TemplateType
    (
        "Header, Left Vertical Menu, Body", "header_left_menu.gif",
        "Contains a custom header area with or without a logo, a vertical menu on the left hand side, and a customizable body area."
    );
    public static final TemplateType HEADER_LEFT_MENU_FOOTER = new TemplateType
    (
        "Header, Left Vertical Menu, Body, Footer", "header_left_menu_footer.gif",
        "Contains a custom header area with or without a logo, a vertical menu on the left hand side, a customizable body area, and a custom footer area."
    );
    public static final TemplateType HEADER_RIGHT_MENU = new TemplateType
    (
        "Header, Body, Right Vertical Menu", "header_right_menu.gif",
        "Contains a custom header area with or without a logo, a customizable body area, and a vertical menu on the right hand side."
    );
    public static final TemplateType HEADER_RIGHT_MENU_FOOTER = new TemplateType
    (
        "Header, Body, Right Vertical Menu, Footer", "header_right_menu_footer.gif",
        "Contains a custom header area with or without a logo, a customizable body area, a vertical menu on the right hand side, and a custom footer area."
    );
    public static final TemplateType MENU_UNDER_HEADER = new TemplateType
    (
        "Header, Horizontal Menu Underneath, Body", "menu_under_header.gif",
        "Contains a custom header area with or without a logo, a horizontal menu below the header, and a customizable body area."
    );
    public static final TemplateType MENU_UNDER_HEADER_FOOTER = new TemplateType
    (
        "Header, Horizontal Menu Underneath, Body, Footer", "menu_under_header_footer.gif",
        "Contains a custom header area with or without a logo, a horizontal menu below the header, a customizable body area, and a custom footer."
    );
    public static final TemplateType TOP_MENU_HEADER = new TemplateType
    (
        "Menu at top, Header, Body", "top_menu_header.gif",
        "Contains a horizontal menu at the top of the screen, a custom header area with or without a logo, and a customizable body area."
    );
    public static final TemplateType TOP_MENU_HEADER_FOOTER = new TemplateType
    (
        "Menu at top, Header, Body, Footer", "top_menu_header_footer.gif",
        "Contains a horizontal menu at the top of the screen, a custom header area with or without a logo, a customizable body area, and a custom footer."
    );
    public static final TemplateType CUSTOM = new TemplateType
    (
        "Custom", "custom.gif",
        "Empty customizable layout."
    );
    /** The name. */
    private String _name;
    /** The icon file name. */
    private String _icon;
    /** Gets incremented as list types are initialized. */
    private static int _nextId = 0;
    /** The template types. */
    private static Vector _types;

    /**
     * Creates a new TemplateType with a name, icon, and description.
     *
     * @param String The name.
     * @param String The icon file name.
     * @param String The description.
     */
    private TemplateType(String name, String icon, String desc)
    {
        super(_nextId++, desc);
        _name = name;
        _icon = icon;
        if (_types == null) _types = new Vector();
        _types.add(this);
    }

    /**
     * Returns an TemplateType based on the id passed in. If the id does not match the id of
     * a constant, then we return null. If there are two constants with the same id, then
     * the first one is returned.
     *
     * @param int The constant id.
     * @return TemplateType
     */
    public static TemplateType evaluate(int id)
    {
        return (TemplateType)Constant.evaluate(id, _types);
    }

    /**
     * Evaluates the given string by locating an id in it, then returning the TemplateType.
     * If an valid id cannot be found, then null is returned.
     *
     * @param String A string with an id in it.
     * @return TemplateType
     */
    public static TemplateType evaluate(String str)
    {
        if (str == null) return null;
        Matcher m = _idPattern.matcher(str);
        int id = -1;
        if ( m.find() ) id = Integer.parseInt( m.group() );
        return (TemplateType)Constant.evaluate(id, _types);
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

    /**
     * Returns if this type is a menu template type (one that has a menu).
     *
     * @return boolean
     */
    public boolean isMenuType()
    {
        return (this == HEADER_LEFT_MENU || this == MENU_UNDER_HEADER || this == TOP_MENU_HEADER ||
                this == HEADER_LEFT_MENU_FOOTER || this == MENU_UNDER_HEADER_FOOTER ||
                this == TOP_MENU_HEADER_FOOTER || this == HEADER_RIGHT_MENU ||
                this == HEADER_RIGHT_MENU_FOOTER);
    }

    /**
     * Returns if this type is a header template type (one that has a header).
     *
     * @return boolean
     */
    public boolean isHeaderType()
    {
        return (this == HEADER_FOOTER || this == HEADER_LEFT_MENU_FOOTER || this == MENU_UNDER_HEADER_FOOTER ||
                this == TOP_MENU_HEADER_FOOTER || this == HEADER_RIGHT_MENU_FOOTER || this == HEADER ||
                this == HEADER_LEFT_MENU || this == HEADER_RIGHT_MENU || this == MENU_UNDER_HEADER ||
                this == TOP_MENU_HEADER);
    }

    /**
     * Returns if this type is a footer template type (one that has a footer).
     *
     * @return boolean
     */
    public boolean isFooterType()
    {
        return (this == HEADER_FOOTER || this == HEADER_LEFT_MENU_FOOTER || this == MENU_UNDER_HEADER_FOOTER ||
                this == TOP_MENU_HEADER_FOOTER || this == HEADER_RIGHT_MENU_FOOTER);
    }
}