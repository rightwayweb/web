package com.zitego.web.monitor;

/**
 * This class is a data holder for a monitor task result.
 * The result contains a result of SUCCESS or FAILURE and
 * a possible error message that lists the cause of the
 * FAILURE.
 *
 * @author John Glorioso
 * @version $Id: MonitorResult.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class MonitorResult
{
    /** A successful monitor task result. */
    public static final int SUCCESS = 1;
    /** A failed monitor task result. */
    public static final int FAILURE = 0;
    private int _result;
    private String _errorMsg;

    /**
     * Creates a new monitor result with a success error code.
     */
    public MonitorResult()
    {
        this(SUCCESS, null);
    }

    /**
     * Creates a new monitor with a failure error code and an error message.
     *
     * @param msg The error message.
     */
    public MonitorResult(String msg)
    {
        this(FAILURE, msg);
    }

    /**
     * Creates a new monitor with a code and an error message. If the code is > FAILURE,
     * it will be considered SUCCESS, otherwise, it will be a FAILURE.
     *
     * @param result The result code.
     * @param msg The error message.
     */
    public MonitorResult(int result, String msg)
    {
        _result = (result > FAILURE ? SUCCESS : FAILURE);
        _errorMsg = msg;
    }

    /**
     * Returns the result.
     *
     * @return int
     */
    public int getResult()
    {
        return _result;
    }

    /**
     * Returns the string.
     *
     * @return String
     */
    public String getErrorMessage()
    {
        return _errorMsg;
    }
}