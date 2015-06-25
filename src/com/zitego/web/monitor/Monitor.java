package com.zitego.web.monitor;

import com.zitego.markup.xml.XmlTag;
import com.zitego.sql.DBHandle;
import java.lang.reflect.Constructor;
import java.util.Vector;

/**
 * This is a class that will fulfill monitoring in any respect. It will be initialized
 * with a vector of MonitorTasks xml tags that will allow the monitor to construct
 * the tasks.
 *
 * @author John Glorioso
 * @version $Id: Monitor.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class Monitor
{
    private MonitorEngine _engine;
    private String _name;
    private String _description;
    private MonitorTask[] _tasks = new MonitorTask[0];

    /**
     * Creates a new monitor with a name.
     *
     * @param name The name.
     */
    public Monitor(String name)
    {
        setName(name);
    }

    /**
     * Initializes the monitor with an array of arguments.
     *
     * @param tasks The tasks as a Vector of XmlTags.
     * @param engine The monitor engine.
     * @throws MonitorInitializationException if an error occurs initializing.
     */
    public void init(Vector tasks, MonitorEngine engine) throws MonitorInitializationException
    {
        _engine = engine;
        Vector tmp = new Vector();
        int size = tasks.size();
        for (int i=0; i<size; i++)
        {
            XmlTag tag = (XmlTag)tasks.get(i);
            String name = tag.getTagAttribute("name");
            try
            {
                Constructor c = Class.forName( tag.getTagAttribute("class") ).getConstructor(new Class[] { String.class, Monitor.class });
                MonitorTask t = (MonitorTask)c.newInstance(new Object[] { name, this });
                t.init( tag.getChildrenWithName("argument") );
                t.setErrorMessage( tag.getChildValue("error-message") );
                log( "Adding task: " + t.getName() );
                tmp.add(t);
            }
            catch (Exception ex)
            {
                throw new MonitorInitializationException("An error occurred creating the monitor class: "+name, ex);
            }
        }
        _tasks = new MonitorTask[tmp.size()];
        tmp.copyInto(_tasks);
    }

    /**
     * Runs the tasks in this monitor.
     *
     * @throws MonitorException if an error occurs.
     */
    public MonitorResult[] runTasks() throws MonitorException
    {
        MonitorResult[] results = new MonitorResult[_tasks.length];
        for (int i=0; i<_tasks.length; i++)
        {
            log( "Running task: "+_tasks[i].getName() );
            results[i] = _tasks[i].exec();
        }
        return results;
    }

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
     * Sets the description.
     *
     * @param desc The description.
     */
    public void setDescription(String desc)
    {
        _description = desc;
    }

    /**
     * Returns the description.
     *
     * @return String
     */
    public String getDescription()
    {
        return _description;
    }

    /**
     * Returns a database connection given a name.
     *
     * @param name The database connection name.
     * @return DBHandle
     */
    public DBHandle getDBHandle(String name)
    {
        return _engine.getDBHandle(name);
    }

    /**
     * Returns the task at the given index.
     *
     * @param index The index of the task to return.
     * @return MonitorTask
     */
    public MonitorTask getTask(int index)
    {
        return _tasks[index];
    }

    /**
     * Logs a line of test.
     *
     * @param line The line of text to log.
     */
    public void log(String line)
    {
        _engine.log(line);
    }
}