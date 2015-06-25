package com.zitego.web.thirdPartyAPI;

import com.zitego.format.*;
import com.zitego.markup.*;

/**
 * This class encapsulates a response from the third party api.
 *
 * @author John Glorioso
 * @version $Id: APIResponse.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class APIResponse implements Formattable, MarkupConverter
{
    /**
     * Creates an empty APIResponse.
     */
    protected APIResponse() { }

    /**
     * Creates a new APIResponse from text.
     *
     * @param text The text response.
     * @param type The format type.
     * @throws APIException if an error occurs parsing.
     */
    public APIResponse(String text, FormatType type) throws APIException
    {
        try
        {
            parse(new StringBuffer(text), type);
        }
        catch (UnsupportedFormatException ufe)
        {
            throw new APIException("Could not create an APIResponse", ufe);
        }
    }

    /**
     * A response has no parent, so this does nothing. It is here only
     * for implementation.
     *
     * @param parent Not used.
     */
    public void setParent(MarkupContent parent) { }

    /**
     * A response has no parent, so this returns null.
     *
     * @return MarkupContent
     */
    public MarkupContent getParent()
    {
        return null;
    }
}