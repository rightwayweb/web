package com.zitego.web.search;

import com.zitego.markup.html.tag.form.Select;
import com.zitego.markup.html.tag.form.Option;
import com.zitego.markup.html.tag.form.TextArea;
import com.zitego.markup.html.tag.form.Checkbox;
import com.zitego.markup.html.tag.form.FormElement;
import com.zitego.markup.html.tag.form.Sizeable;
import com.zitego.markup.html.tag.form.Radio;
import com.zitego.markup.xml.XmlConverter;
import com.zitego.util.Constant;
import com.zitego.markup.MarkupContent;
import com.zitego.markup.IllegalMarkupException;
import com.zitego.format.FormatType;
import com.zitego.format.UnsupportedFormatException;
import java.util.Vector;
import java.lang.reflect.Field;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class wraps an html form element that is used to display a search option field.
 * The parent is the search form.
 *
 * @author John Glorioso
 * @version $Id: SearchFormElement.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class SearchFormElement implements XmlConverter
{
    /** The search form parent. */
    protected SearchForm _parent;
    /** The form element this wraps. */
    protected FormElement _element;
    /** The label for the input element. */
    protected String _label;
    /** The validation regular expression for this element. */
    protected String _validation;
    /** Whether this attribute is required. Default is false. */
    protected boolean _required = false;
    /** The error message to display if validation fails or required is not met. */
    protected String _err;

    /**
     * Creates a new SearchFormElement with a SearchForm parent.
     *
     * @param parent The parent.
     */
    SearchFormElement(SearchForm parent)
    {
        _parent = parent;
    }

    /**
     * Returns the label.
     *
     * @return String
     */
    public String getLabel()
    {
        return _label;
    }

    /**
     * Returns the validation.
     *
     * @return String
     */
    public String getValidation()
    {
        return _validation;
    }

    /**
     * Returns whether this element is required or not.
     *
     * @return boolean
     */
    public boolean isRequired()
    {
        return _required;
    }

    /**
     * Returns the error message for this element.
     *
     * @return String
     */
    public String getErrorMessage()
    {
        return _err;
    }

    /**
     * Returns the form element this wraps.
     *
     * @return FormElement
     */
    public FormElement getFormElement()
    {
        return _element;
    }

    public void buildFromXml(Element root)
    {
        //Create the form element
        String val = root.getAttribute("type");
        if ( "textfield".equalsIgnoreCase(val) ) _element = new com.zitego.markup.html.tag.form.Text(_parent);
        else if ( "textarea".equalsIgnoreCase(val) ) _element = new TextArea(_parent);
        else if ( "select".equalsIgnoreCase(val) ) _element = new Select(_parent);
        else if ( "radio".equalsIgnoreCase(val) ) _element = new Radio(_parent);
        else if ( "checkbox".equalsIgnoreCase(val) ) _element = new Checkbox(_parent);

        _element.setName( root.getAttribute("name") );
        if (_element instanceof Sizeable)
        {
            val = root.getAttribute("size");
            if (!"".equals(val) && val != null) ( (Sizeable)_element ).setSize( Integer.parseInt(val) );
            val = root.getAttribute("maxlength");
            if (!"".equals(val) && val != null) ( (Sizeable)_element ).setMaxLength( Integer.parseInt(val) );
        }
        if (_element instanceof TextArea)
        {
            val = root.getAttribute("rows");
            if (!"".equals(val) && val != null) ( (TextArea)_element ).setRows( Integer.parseInt(val) );
            val = root.getAttribute("cols");
            if (!"".equals(val) && val != null) ( (TextArea)_element ).setCols( Integer.parseInt(val) );
        }

        val = root.getAttribute("label");
        if (!"".equals(val) && val != null) _label = val;
        val = root.getAttribute("validate");
        if (!"".equals(val) && val != null) _validation = val;
        val = root.getAttribute("required");
        if (!"".equals(val) && val != null) _required = new Boolean(val).booleanValue();
        val = root.getAttribute("error_message");
        if (!"".equals(val) && val != null) _err = val;

        //Add the elements
        NodeList nodes = root.getChildNodes();
        int size = nodes.getLength();
        for (int i=0; i<size; i++)
        {
            Node n = nodes.item(i);
            if (n instanceof Element)
            {
                addChild( (Element)n );
            }
        }
    }

    public void addChild(Element child)
    {
        if (_element instanceof Select)
        {
            if ( child.getTagName().equalsIgnoreCase("option") )
            {
                Option opt = new Option( (Select)_element );
                opt.setValue( child.getAttribute("value") );
                opt.setText( child.getAttribute("name") );
                ( (Select)_element ).addOption(opt);
            }
            else if ( child.getTagName().equalsIgnoreCase("options") )
            {
                //Add constant select option is supposed to
                String val = child.getAttribute("constant");
                if (!"".equals(val) && val != null)
                {
                    int index = val.lastIndexOf(".");
                    String name = val.substring(index+1);
                    val = val.substring(0, index);
                    try
                    {
                        Class c = Class.forName(val);
                        Field f = c.getField(name);
                        Constant con = (Constant)f.get(null);
                        Vector types = con.getTypes();
                        int size = types.size();
                        for (int i=0; i<size; i++)
                        {
                            Constant c2 = (Constant)types.get(i);
                            Option opt = new Option( (Select)_element );
                            opt.setValue( String.valueOf(c2.getValue()) );
                            opt.addBodyContent( c2.getDescription() );
                            ( (Select)_element ).addOption(opt);
                        }
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException("Could not set options with constant: "+val+"."+name, e);
                    }
                }
            }
        }
    }

    public void parse(Object objToParse, FormatType type) throws IllegalMarkupException, UnsupportedFormatException
    {
        if (objToParse == null) return;
        else if (objToParse instanceof Element && type == FormatType.XML) buildFromXml( (Element)objToParse );
        else throw new IllegalMarkupException("Unable to format object of type: "+objToParse.getClass());
    }

    public String format(FormatType type) throws UnsupportedFormatException
    {
        throw new UnsupportedFormatException(type.getDescription() + " is not supported");
    }

    public MarkupContent getParent()
    {
        return _element;
    }

    public void setParent(MarkupContent parent)
    {

    }

    public MarkupContent getAsMarkupContent()
    {
        return _element;
    }
}