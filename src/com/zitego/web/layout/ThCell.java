package com.zitego.web.layout;

import com.zitego.markup.MarkupBody;
import java.util.Hashtable;

/**
 * This class is an extension of the Cell tag to handle th cells and xml input/output.
 *
 * @author John Glorioso
 * @version $Id: ThCell.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class ThCell extends Cell
{
    /**
     * Creates a new ThCell tag with a Row parent.
     *
     * @param Row The parent Row.
     */
    public ThCell(Row parent)
    {
        super(parent);
        setTagName("th");
    }

    protected MarkupBody createMarkupBody()
    {
        return new PageLayoutMarkupBody(this);
    }
}