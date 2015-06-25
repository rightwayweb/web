package com.zitego.web.dropDown;

/**
 * This is a dropdown that is set manually by the user.
 *
 * @author John Glorioso
 * @version $Id: ManualDropDown.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class ManualDropDown extends DropDown
{
    /**
     * Creates a new set of options.
     *
     * @param String The form element name.
     */
    protected ManualDropDown(String name)
    {
        super(name);
    }

    protected DropDown createInstance()
    {
        return new ManualDropDown( getName() );
    }
}