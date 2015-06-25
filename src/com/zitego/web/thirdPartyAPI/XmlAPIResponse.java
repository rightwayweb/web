package com.zitego.web.thirdPartyAPI;

import com.zitego.markup.xml.XmlTag;
import com.zitego.format.*;
import com.zitego.markup.IllegalMarkupException;
import com.zitego.markup.MarkupContent;

/**
 * An xml response from the api.
 *
 * @author John Glorioso
 * @version $Id: XmlAPIResponse.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class XmlAPIResponse extends APIResponse
{
    /** The root tag of the response. */
    private XmlTag _xmlResponse;

    /**
     * Creates a new empty XmlAPIResponse.
     */
    public XmlAPIResponse()
    {
        super();
    }

    /**
     * Creates a new XmlAPIResponse from text.
     *
     * @param text The text response.
     */
    public XmlAPIResponse(String text) throws APIException
    {
        super(text, FormatType.XML);
    }

    public void parse(Object text, FormatType type) throws UnsupportedFormatException, IllegalMarkupException
    {
        _xmlResponse = createXmlTag();
        _xmlResponse.parse(text, type);
    }

    /**
     * This method is called when parse is called. It can be overridden to return a more specific
     * xml tag. By default, it returns an XmlTag object.
     *
     * @return XmlTag
     */
    protected XmlTag createXmlTag()
    {
        return new XmlTag();
    }

    /**
     * Returns the xml tag.
     *
     * @return XmlTag
     */
    protected XmlTag getXmlTag()
    {
        return _xmlResponse;
    }

    public String format(FormatType type) throws UnsupportedFormatException
    {
        return _xmlResponse.format(type);
    }

    public MarkupContent getAsMarkupContent()
    {
        return _xmlResponse;
    }
}