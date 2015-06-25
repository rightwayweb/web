package com.zitego.web.monitor;

import com.zitego.util.DetailedMessageException;

/**
 * This is a general exception to be thrown when errors occur initializing monitors
 * monitoring via the engine or from the monitor class itself.
 *
 * @author John Glorioso
 * @version $Id: MonitorInitializationException.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class MonitorInitializationException extends DetailedMessageException
{
    /**
     * Creates a new exception with a message.
     *
     * @param String The message.
     */
    public MonitorInitializationException(String msg)
    {
        super(msg);
    }

    /**
     * Creates a new exception with a message.
     *
     * @param String The message.
     * @param String The detailed message.
     */
    public MonitorInitializationException(String msg, String detailedMessage)
    {
        super(msg, detailedMessage);
    }

    /**
     * Creates a new exception with a message and root cause.
     *
     * @param String The message.
     * @param Throwable The root cause.
     */
    public MonitorInitializationException(String msg, Throwable cause)
    {
        super(msg, cause);
    }

    /**
     * Creates a new exception with a message and root cause.
     *
     * @param String The message.
     * @param String The detailed message.
     * @param Throwable The root cause.
     */
    public MonitorInitializationException(String msg, String detailedMessage, Throwable cause)
    {
        super(msg, detailedMessage, cause);
    }
}