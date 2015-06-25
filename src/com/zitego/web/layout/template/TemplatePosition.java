package com.zitego.web.layout.template;

import com.zitego.util.Constant;
import java.util.Vector;

/**
 * Represents template positions within the body. This is a shorthand descriptor for
 * setting various table and body attributes within PageLayoutTemplate.
 *
 * @author John Glorioso
 * @version $Id: TemplatePosition.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class TemplatePosition extends Constant
{
    public static final TemplatePosition FULL_WIDTH_NO_PADDING = new TemplatePosition("100% Wide (no padding)");
    public static final TemplatePosition FULL_WIDTH_PADDING = new TemplatePosition("100% Wide (with padding)");
    public static final TemplatePosition CENTERED_NO_PADDING = new TemplatePosition("Centered (no padding)");
    public static final TemplatePosition CENTERED_PADDING = new TemplatePosition("Centered (with padding)");
    public static final TemplatePosition LEFT_NO_PADDING = new TemplatePosition("Left Justified (no padding)");
    public static final TemplatePosition LEFT_PADDING = new TemplatePosition("Left Justified (with padding)");
    /** Gets incremented as list types are initialized. */
    private static int _nextId = 0;
    /** The template types. */
    private static Vector _types;

    /**
     * Creates a new TemplatePosition with a description.
     *
     * @param String The description.
     */
    private TemplatePosition(String desc)
    {
        super(_nextId++, desc);
        if (_types == null) _types = new Vector();
        _types.add(this);
    }

    /**
     * Returns an TemplatePosition based on the id passed in. If the id does not match the id of
     * a constant, then we return null. If there are two constants with the same id, then
     * the first one is returned.
     *
     * @param int The constant id.
     * @return TemplatePosition
     */
    public static TemplatePosition evaluate(int id)
    {
        return (TemplatePosition)Constant.evaluate(id, _types);
    }

    public Vector getTypes()
    {
        return _types;
    }
}