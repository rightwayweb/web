package com.zitego.web.thirdPartyAPI;

import com.zitego.util.DetailedMessageException;

/**
 * An exception to be thrown when an error occurs querying an api.
 *
 * @author John Glorioso
 * @version $Id: APIException.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
*/
public class APIException extends DetailedMessageException
{
    /**
     * Creates a new exception with a message.
     *
     * @param String The message.
     */
    public APIException(String msg)
    {
        this(msg, (String)null);
    }

    /**
     * Creates a new exception with a message.
     *
     * @param String The message.
     * @param Throwable The root exception.
     */
    public APIException(String msg, Throwable root)
    {
        super(msg, root);
    }

    /**
     * Creates a new exception with a message and a detailed message.
     *
     * @param String The message.
     * @param String The detailed message.
     */
    public APIException(String msg, String detailedMessage)
    {
        this(msg, detailedMessage, null, null);
    }

    /**
     * Creates a new exception with a message, detailed message, request, and response.
     *
     * @param String The message.
     * @param String The detailed message.
     * @param String The request.
     * @param String The response.
     */
    public APIException(String msg, String detailedMessage, String request, String response)
    {
        super( msg, buildDetailedMessage(detailedMessage, request, response) );
    }

    /**
     * Creates a new exception with a message and root cause.
     *
     * @param String The message.
     * @param String The detailed message.
     * @param String The request.
     * @param String The response.
     * @param Throwable The root cause.
     */
    public APIException(String msg, String detailedMessage, String request, String response, Throwable cause)
    {
        super(msg, buildDetailedMessage(detailedMessage, request, response), cause);
    }

    private static String buildDetailedMessage(String detailedMessage, String request, String response)
    {
        if (detailedMessage != null || request != null || response != null)
        {
            StringBuffer ret = new StringBuffer();
            if (detailedMessage != null) ret.append("Details: ").append(detailedMessage);
            if (request != null) ret.append(" Request: ").append(request);
            if (response != null) ret.append(" Response: ").append(response);
            return ret.toString();
        }
        else
        {
            return null;
        }
    }
}