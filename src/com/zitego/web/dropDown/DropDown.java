package com.zitego.web.dropDown;

import com.zitego.format.FormatType;
import com.zitego.format.UnsupportedFormatException;
import com.zitego.markup.MarkupContent;
import com.zitego.markup.TextContent;
import com.zitego.markup.html.tag.form.Select;
import com.zitego.markup.html.tag.form.Option;
import java.util.Vector;

/**
 * This is an abstract class that contains constant select drop down options. By default
 * it automatically sets the tabindex to be 1. This can be changed.
 *
 * @author John Glorioso
 * @version $Id: DropDown.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class DropDown extends Select implements Cloneable
{
    /** The first option. */
    protected Option _firstOpt;

    /**
     * Creates a new dropdown with a name.
     *
     * @param String The name of the select.
     */
    public DropDown(String name)
    {
        super();
        setName(name);
        setIsOnOwnLine(true);
        setTabIndex(1);
    }

    /**
     * Creates a new set of options with a name, first option label, first
     * option value, and a selected value.
     *
     * @param String The form element name.
     * @param String The label for the first option.
     * @param String The value for the first option.
     */
    public DropDown(String name, String firstOpt, String firstVal)
    {
        setName(name);
        setIsOnOwnLine(true);
        if (firstOpt != null)
        {
            _firstOpt = new Option(this);
            _firstOpt.setText(firstOpt);
            if (firstVal != null) _firstOpt.setValue(firstVal);
            else _firstOpt.setValue("");
            addOption(_firstOpt);
        }
        setTabIndex(1);
    }

    /**
     * Returns the first option.
     *
     * @return Option
     */
    protected Option getFirstOption()
    {
        return _firstOpt;
    }

    /**
     * Sets the first option.
     *
     * @param Option The first option.
     */
    protected void setFirstOption(Option opt)
    {
        _firstOpt = opt;
    }

    /**
     * Formats the tag to HTML and returns it as a string.
     *
     * @return String
     * @throws RuntimeException if the content could not be formatted.
     */
    public String toString()
    {
        try
        {
            return format(FormatType.HTML);
        }
        catch (UnsupportedFormatException ufe)
        {
            throw new RuntimeException("Could not format the drop down", ufe);
        }
    }

    /**
     * Creates a shallow copy of this drop down by setting the underlying select object's
     * attributes, event handlers, and options.
     *
     * @return Object
     */
    public Object clone()
    {
        DropDown ret = createInstance();
        ret.setAttributes( getAttributes() );
        ret.setJsEventHandlers( getJsEventHandlers() );
        Vector options = getOptions();
        int size = options.size();
        for (int i=0; i<size; i++)
        {
            Option o = (Option)options.get(i);
            Option o2 = new Option(ret);
            o2.setAttributes( o.getAttributes() );
            o2.setText( o.getText() );
        }
        return ret;
    }

    /**
     * For extended classes to implement in order to create an instance of the drop down
     * to be cloned.
     *
     * @return DropDown
     */
    protected abstract DropDown createInstance();
}