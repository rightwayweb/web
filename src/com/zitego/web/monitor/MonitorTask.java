package com.zitego.web.monitor;

import com.zitego.sql.DBHandle;
import java.util.Vector;

/**
 * This class encapsulates argument and failure error message data
 * about a monitor task and actually performs the task itself. It
 * must be defined.
 *
 * @author John Glorioso
 * @version $Id: MonitorTask.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class MonitorTask
{
    private Monitor _monitor;
    private String _name;
    private String _errorMsg;

    /**
     * Creates a new monitor task class with a name and the monitor this
     * is a part of.
     *
     * @param name The monitor task name.
     * @param monitor The monitor.
     */
    public MonitorTask(String name, Monitor monitor)
    {
        _monitor = monitor;
        setName(name);
    }

    /**
     * Initializes the monitor task with an array of arguments.
     *
     * @param args The arguments as a Vector of XmlTags.
     * @throws MonitorInitializationException if an error occurs initializing.
     */
    public abstract void init(Vector args) throws MonitorInitializationException;

    /**
     * Define this method to perform the task.
     *
     * @return MonitorResult
     * @throws MonitorException if an error occurs.
     */
    public abstract MonitorResult exec() throws MonitorException;

    /**
     * Sets the name.
     *
     * @param name The name.
     * @throws IllegalArgumentException if the name is null.
     */
    public void setName(String name) throws IllegalArgumentException
    {
        if ( name == null || "".equals(name) ) throw new IllegalArgumentException("You must specify a name");
        _name = name;
    }

    /**
     * Returns the name.
     *
     * @return String
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Sets the error message.
     *
     * @param msg The error message.
     */
    public void setErrorMessage(String msg)
    {
        _errorMsg = msg;
    }

    /**
     * Returns the error message.
     *
     * @return String
     */
    public String getErrorMsg()
    {
        return _errorMsg;
    }

    /**
     * Returns a database connection given a name.
     *
     * @param name The database connection name.
     * @return DBHandle
     */
    public DBHandle getDBHandle(String name)
    {
        return _monitor.getDBHandle(name);
    }

    /**
     * Logs a line of test.
     *
     * @param line The line of text to log.
     */
    public void log(String line)
    {
        _monitor.log(line);
    }
}