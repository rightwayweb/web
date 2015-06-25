package com.zitego.web.layout;

import com.zitego.markup.*;
import com.zitego.markup.tag.MarkupTag;
import com.zitego.markup.html.HtmlMarkupFactory;
import java.util.Hashtable;

/**
 * This class overrides the createTagByName method in HtmlMarkupFactory to account
 * for rows, cells, and th cells.
 *
 * @author John Glorioso
 * @version $Id: PageLayoutMarkupFactory.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see com.zitego.markup.html.HtmlMarkupFactory
 */
public class PageLayoutMarkupFactory extends HtmlMarkupFactory
{
    /** A singleton factory. */
    private static PageLayoutMarkupFactory _factory;
    /** The tag name map. */
    private static Hashtable _tags = new Hashtable();
    static
    {
        try
        {
            _tags.put( "table", Class.forName("com.zitego.web.layout.PageSection") );
            _tags.put( "tr", Class.forName("com.zitego.web.layout.Row") );
            _tags.put( "td", Class.forName("com.zitego.web.layout.Cell") );
            _tags.put( "th", Class.forName("com.zitego.web.layout.ThCell") );
        }
        catch (ClassNotFoundException cnfe)
        {
            throw new RuntimeException("Could initialize class tag map: "+cnfe);
        }
    }

    /**
     * Returns an instance of the PageLayoutMarkupFactory to use.
     *
     * @return MarkupFactory
     */
    public static synchronized MarkupFactory getInstance()
    {
        if (_factory == null) _factory = new PageLayoutMarkupFactory();
        return _factory;
    }

    protected MarkupTag createTagByName(String name, MarkupContent parent) throws Exception
    {
        if (name == null) return null;
        name = name.toLowerCase();
        Class tagClass = (Class)_tags.get(name);
        if (tagClass != null)
        {
            if ( parent == null && tagClass != _tags.get("table") ) throw new IllegalMarkupException(name + " must have a parent tag.");
            return createTagInstance(tagClass, parent);
        }
        else
        {
            return super.createTagByName(name, parent);
        }
    }
}