package com.zitego.web.thirdPartyAPI;

import com.zitego.http.UrlContentReader;

/**
 * This is an abstract class to be extended for creating third party
 * apis. An api is used to do specific things. For example, a domain
 * hosting company may have a third party api to register and configure
 * domain names, however not all third party apis are the same. Therefore,
 * multiple types of api's can be configured to be used depending on the
 * vendor. The api extending this class needs to define what methods
 * can be called, what information needs to be passed, and what information
 * is to be returned when interfacing with the third party. In the domain
 * example, to register a domain, you may need to provide all contact
 * information, the domain name, and the number of years to register for.
 * The api should provide a method that captures that information,
 * formulates the request to the third party, parses the response, and
 * returns information back to the caller.
 *
 * @author John Glorioso
 * @version $Id: ThirdPartyAPI.java,v 1.2 2012/01/30 05:03:46 jglorioso Exp $
 */
public abstract class ThirdPartyAPI
{
    /** Whether we are debugging or not. Default is false. */
    private boolean _debug = false;

    /**
     * Sets whether we are debugging or not.
     *
     * @param String Evaluates to true if the string equals true.
     */
    public void setDebug(String flag)
    {
        setDebug( new Boolean(flag).booleanValue() );
    }

    /**
     * Sets whether we are debugging or not.
     *
     * @param boolean The flag.
     */
    public void setDebug(boolean flag)
    {
        _debug = flag;
    }

    /**
     * Returns if we are in debug mode or not.
     *
     * @return boolean
     */
    public boolean debugging()
    {
        return _debug;
    }

    /**
     * Creates an APIRequest given an array of Object arguments.
     *
     * @param Object[] The arguments.
     * @return APIRequest
     * @throws APIException if an error occurs.
     */
    protected abstract APIRequest getRequest(Object[] args) throws APIException;

    /**
     * Creates an APIResponse given the request and a text string response.
     *
     * @param APIRequest The request.
     * @param String The text.
     * @return APIResponse
     * @throws APIException if an error occurs.
     */
    protected abstract APIResponse createResponse(APIRequest request, String text) throws APIException;

    /**
     * Retrieves a response from the third party api given a request. If an error occurs,
     * an APIException is thrown with details.
     *
     * @param APIRequest The request.
     * @param String The detailed error message snippet.
     * @return APIResponse
     * @throws APIException if an error occurs.
     */
    protected APIResponse getResponse(APIRequest request, String errSnippet) throws APIException
    {
        APIResponse response = null;
        try
        {
            UrlContentReader reader = new UrlContentReader(request);
            String content = reader.getContent();
            if ( debugging() ) System.out.println("response="+content);
            response = createResponse(request, content);
        }
        catch (Exception e)
        {
            handleError
            (
                "An error occurred" + (errSnippet != null ? " " + errSnippet + " " : "") + ".",
                e, "Error processing api request", request, null
            );
        }
        return response;
    }

    /**
     * Throws a new DomainAPIException with a message, root exception, a details message, the
     * request, and the response. Except for the message, any value can be null.
     *
     * @param String The message.
     * @param Throwable The root exception.
     * @param String The details message.
     * @param APIRequest The request.
     * @param APIResponse The response.
     * @throws APIException
     */
    protected abstract void handleError(String msg, Throwable root, String details, APIRequest request, APIResponse response)
    throws APIException;
}