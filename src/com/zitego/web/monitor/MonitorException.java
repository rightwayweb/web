package com.zitego.web.monitor;

import com.zitego.util.DetailedMessageException;

/**
 * This is a general exception to be thrown when a general error occurs with monitors
 * monitoring via the engine or from the monitor class itself.
 *
 * @author John Glorioso
 * @version $Id: MonitorException.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class MonitorException extends DetailedMessageException
{
    /**
     * Creates a new exception with a message.
     *
     * @param String The message.
     */
    public MonitorException(String msg)
    {
        super(msg);
    }

    /**
     * Creates a new exception with a message.
     *
     * @param String The message.
     * @param String The detailed message.
     */
    public MonitorException(String msg, String detailedMessage)
    {
        super(msg, detailedMessage);
    }

    /**
     * Creates a new exception with a message and root cause.
     *
     * @param String The message.
     * @param Throwable The root cause.
     */
    public MonitorException(String msg, Throwable cause)
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
    public MonitorException(String msg, String detailedMessage, Throwable cause)
    {
        super(msg, detailedMessage, cause);
    }
}