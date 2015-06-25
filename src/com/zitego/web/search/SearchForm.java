package com.zitego.web.search;

import com.zitego.markup.html.tag.Html;
import com.zitego.markup.html.tag.Body;
import com.zitego.markup.html.tag.form.Form;
import com.zitego.http.HttpMethodType;
import com.zitego.markup.xml.XmlConverter;
import com.zitego.markup.IllegalMarkupException;
import com.zitego.format.FormatType;
import com.zitego.format.UnsupportedFormatException;
import java.util.Vector;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class wraps an html form that is used to display searching options. All search
 * forms submit to / of the manager application. The action attribute is written out as
 * a hidden parameter as the "d" element that the TransportServlet will interpret.
 *
 * @author John Glorioso
 * @version $Id: SearchForm.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see SearchFormMap
 */
public class SearchForm extends Form implements XmlConverter
{
    /** The html parent for all search forms. */
    protected static Body _parent = new Html().getBodyTag();
    /** The id. */
    protected String _id;
    /** The embedded form action (d param) of this search form. */
    protected String _dParam;
    /** The title of the search form. */
    protected String _title;
    /** The SearchFormElements. */
    protected SearchFormElement[] _elements = new SearchFormElement[0];

    /**
     * Creates a new SearchForm object with no properties.
     *
     * @param id The id.
     */
    SearchForm(String id)
    {
        super(id, _parent);
        _id = id;
        setMethodType(HttpMethodType.POST);
        setOnSubmit("return doSearch()");
    }

    /**
     * Returns the id.
     *
     * @return String
     */
    public String getId()
    {
        return _id;
    }

    /**
     * Returns the embedded action (d param) of this search form.
     *
     * @return String
     */
    public String getDParam()
    {
        return _dParam;
    }

    /**
     * Returns the title of this search form.
     *
     * @return String
     */
    public String getTitle()
    {
        return _title;
    }

    /**
     * Returns the search form elements.
     *
     * @return SearchFormElement[]
     */
    public SearchFormElement[] getSearchFormElements()
    {
        return _elements;
    }

    public void buildFromXml(Element root)
    {
        //Set the action (d param)
        _id = root.getAttribute("id");
        setNameAttribute(_id);
        _dParam = root.getAttribute("action");
        _title = root.getAttribute("title");
        if ( "".equals(_title) ) _title = null;

        //Add the elements
        NodeList nodes = root.getChildNodes();
        int size = nodes.getLength();
        Vector tmp = new Vector();
        for (int i=0; i<size; i++)
        {
            Node n = nodes.item(i);
            if (n instanceof Element)
            {
                tmp.add( (Element)n );
            }
        }
        size = tmp.size();
        _elements = new SearchFormElement[size];
        for (int i=0; i<size; i++)
        {
            addChild( (Element)tmp.get(i) );
        }
    }

    public void addChild(Element child)
    {
        SearchFormElement elem = new SearchFormElement(this);
        elem.buildFromXml(child);
        //Find out where this gets added
        int i = 0;
        for (; i<_elements.length; i++)
        {
            if (_elements[i] == null) break;
        }
        _elements[i] = elem;
    }

    public void parse(Object objToParse, FormatType type) throws IllegalMarkupException, UnsupportedFormatException
    {
        if (objToParse instanceof Element && type == FormatType.XML) buildFromXml( (Element)objToParse );
        else super.parse(objToParse, type);
    }
}