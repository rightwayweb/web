package com.zitego.web.thirdPartyAPI;

import com.zitego.http.HttpRequestData;

/**
 * This class encapsulates a request made to a third party api.
 *
 * @author John Glorioso
 * @version $Id: APIRequest.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class APIRequest extends HttpRequestData
{
    /**
     * Creates a new APIRequest.
     *
     * @param String The url to connect to.
     * @throws IllegalArgumentException if the url is null or has an invalid format.
     */
    public APIRequest(String url) throws IllegalArgumentException
    {
        super(url);
    }
}