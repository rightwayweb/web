package com.zitego.web.menu.button;

import com.zitego.markup.xml.XmlConverter;

/**
 * This interface defines a display for a menu button.
 *
 * @author John Glorioso
 * @version $Id: ButtonDisplay.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public interface ButtonDisplay extends XmlConverter
{
    /**
     * Returns the type.
     *
     * @return ButtonDisplayType
     */
    public ButtonDisplayType getType();
}