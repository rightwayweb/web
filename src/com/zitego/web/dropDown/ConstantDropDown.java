package com.zitego.web.dropDown;

import com.zitego.markup.html.tag.form.Option;
import com.zitego.util.Constant;
import java.util.Vector;

/**
 * This is a class for creating a drop down out of a Constant class.
 *
 * @author John Glorioso
 * @version $Id: ConstantDropDown.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class ConstantDropDown extends DropDown
{
    /**
     * Creates a new drop down. This is for the clone method.
     *
     * @param String The form element name.
     */
    private ConstantDropDown(String name)
    {
        super(name);
    }

    /**
     * Creates a new set of constant options.
     *
     * @param String The form element name.
     * @param Constant The constant to create the drop down from.
     */
    public ConstantDropDown(String name, Constant c)
    {
        this(name, c, null, null, null);
    }

    /**
     * Creates a new set of constant options with a selected value.
     *
     * @param String The form element name.
     * @param Constant The constant to create the drop down from.
     * @param String The selected value.
     */
    public ConstantDropDown(String name, Constant c, String sel)
    {
        this(name, c, null, null, sel);
    }

    /**
     * Creates a new drop down given the constant passed in. You can set a label for
     * the first option. If the first option is null, then it will not be set. This
     * also sets the option to select.
     *
     * @param String The form element name.
     * @param Constant The constant to create the drop down from.
     * @param String The label for the first option.
     * @param String The value for the first option.
     * @param String The selected value.
     */
    public ConstantDropDown(String name, Constant c, String firstOpt, String firstVal, String sel)
    {
        super(name, firstOpt, firstVal);

        Vector types = c.getTypes();
        if (types == null) types = new Vector();
        int size = types.size();
        for (int i=0; i<size; i++)
        {
            Constant c2 = (Constant)types.get(i);
            Option opt = new Option(this);
            opt.setText( c2.getDescription() );
            opt.setValue( String.valueOf(c2.getValue()) );
            addOption(opt);
        }

        if (sel != null) setSelected(sel);
    }

    /**
     * Removes the given option from the list.
     *
     * @param Constant The option to remove.
     */
    public void removeOption(Constant opt)
    {
        if (opt != null) removeOption( String.valueOf(opt.getValue()) );
    }

    protected DropDown createInstance()
    {
        return new ConstantDropDown( getName() );
    }
}