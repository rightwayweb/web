package com.zitego.web.thirdPartyAPI;

import com.zitego.http.PostData;
import com.zitego.format.*;
import java.io.IOException;
import java.net.HttpURLConnection;

/**
 * This class encapsulates a request made to a third party api with a post.
 *
 * @author John Glorioso
 * @version $Id: PostAPIRequest.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class PostAPIRequest extends APIRequest
{
    /**
     * Creates a new PostAPIRequest.
     *
     * @param String The url to connect to.
     * @throws IllegalArgumentException if the url is null or has an invalid format.
     */
    public PostAPIRequest(String url) throws IllegalArgumentException
    {
        super(url);
        super.setPostData( new PostData() );
    }

    /**
     * You cannot manually set the post data in an xml api request. It is done automatically
     * with add field values.
     *
     * @param PostData Not used.
     * @throws IllegalStateException because this is not legal.
     */
    public void setPostData(PostData data) throws IllegalStateException
    {
        throw new IllegalStateException("cannot manually set post data");
    }

    /**
     * Adds a field to the post data.
     *
     * @param String The name of the field to add.
     * @param String The value of the field to add.
     * @throws IllegalArgumentException if the field name is null.
     */
    public void addField(String name, String val) throws IllegalArgumentException
    {
        if ( name == null || "".equals(name) ) throw new IllegalArgumentException("Request field name cannot be empty");
        getPostData().addField(name, val);
    }

    /**
     * Returns the given field from the post data.
     *
     * @param String The field.
     * @return String
     */
    public String getField(String name)
    {
        return getPostData().getField(name);
    }
}