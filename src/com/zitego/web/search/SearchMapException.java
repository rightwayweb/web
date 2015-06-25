package com.zitego.web.search;

/**
 * An exception to be thrown when an error occurs with a search map.
 *
 * @author John Glorioso
 * @version $Id: SearchMapException.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
*/
public class SearchMapException extends Exception
{
    /**
     * Creates a new exception with a message.
     *
     * @param msg The message.
     */
    public SearchMapException(String msg)
    {
        super(msg);
    }

    /**
     * Creates a new exception with a message.
     *
     * @param msg The message.
     * @param root The root exception.
     */
    public SearchMapException(String msg, Throwable root)
    {
        super(msg, root);
    }
}