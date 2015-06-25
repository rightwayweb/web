package com.zitego.web.monitor;

import com.zitego.logging.Logger;
import com.zitego.util.getopts.GetOpts;
import com.zitego.util.FileUtils;
import com.zitego.format.FormatType;
import com.zitego.markup.xml.XmlTag;
import com.zitego.sql.DBHandle;
import com.zitego.sql.DBHandleFactory;
import com.zitego.sql.DBConfig;
import com.zitego.mail.SMTPMail;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.Driver;
import java.io.IOException;
import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * This class is the engine that will run the configured monitors given the xml document
 * in the main routine. An example document is as follows:
 * <pre>
 * &lt;monitor-engine&gt;
 *  &lt;monitor name="james_monitor"&gt;
 *   &lt;description&gt;
 *    Checks that you can connect to and send an email to a test account, then
 *    checks that you can read that email.
 *   &lt;/description&gt;
 *   &lt;task name="smtp_check" class="com.zitego.web.monitor.task.SMTPTask"&gt;
 *    &lt;argument name="testAccount"&gt;monitor_test@zitego.com&lt;/argument&gt;
 *    &lt;argument name="msg"&gt;Test message&lt;/argument&gt;
 *    &lt;error-message&gt;
 *     Unable to send test message
 *    &lt;/error-message&gt;
 *   &lt;/task&gt;
 *   &lt;task type="pop_check" class="com.zitego.web.monitor.task.POPTask"&gt;
 *    &lt;argument name="testAccount"&gt;monitor_test@zitego.com&lt;/argument&gt;
 *    &lt;error-message&gt;
 *     Unable to send retrieve messages
 *    &lt;/error-message&gt;
 *   &lt;/task&gt;
 *  &lt;/monitor&gt;
 *
 *  &lt;database-connections&gt;
 *   &lt;database-connection name="maildb"&gt;
 *    &lt;driver&gt;org.gjt.mm.mysql.Driver&lt;/driver&gt;
 *    &lt;url&gt;jdbc:mysql://zg01db.zitego.com/mail&lt;/url&gt;
 *    &lt;user&gt;user&lt;/user&gt;
 *    &lt;password&gt;pass&lt;/password&gt;
 *    &lt;debug&gt;1&lt;/debug&gt;
 *   &lt;/database-connection&gt;
 *  &lt;/database-connections&gt;
 *
 *  &lt;failure-config&gt;
 *   &lt;mail-server&gt;localhost&lt;/mail-server&gt;
 *   &lt;subject&gt;Mail Monitor Failure&lt;/subject&gt;
 *   &lt;from&gt;monitor@zitego.com&;lt/from&gt;
 *   &lt;recipient&gt;jglorioso@zitego.com&lt;/recipient&gt;
 *   &lt;recipient&gt;griznakh@earthlink.net&lt;/recipient&gt;
 *  &lt;/failure-config&gt;
 *
 * &lt;/monitor-engine&gt;
 * </pre>
 *
 * @author John Glorioso
 * @version $Id: MonitorEngine.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class MonitorEngine
{
    private Monitor[] _monitors;
    private Hashtable _dbConns = new Hashtable();
    private String _mailServer;
    private String _failureSubject;
    private String _failureFromAddress;
    private String[] _failureRecipients = new String[0];
    protected Logger _logger;

    public static void main(String[] args)
    {
        String propsPath = null;
        String logFile = null;
        try
        {
            GetOpts opts = new GetOpts(new String[] { "properties::", "log_file::" }, args, GetOpts.OPTION_CASE_INSENSITIVE);
            int index;
            while ( (index=opts.getOptions()) != -1 )
            {
                String arg = opts.getOptionString(index);
                String value = opts.getOptarg();
                if ( "log_file".equals(arg) )
                {
                    logFile = value;
                }
                else if ( "properties".equals(arg) )
                {
                    propsPath = value;
                }
                else
                {
                    System.out.println("*** WARNING *** Ignoring invalid argument: " + arg);
                }
            }
        }
        catch(Throwable t)
        {
            System.out.println("*** ERROR *** Could not read properties: " + t);
            System.exit(1);
        }

        if (propsPath == null)
        {
            System.out.println("Usage: java com.zitego.web.monitor.MonitorEngine [-log_file <log>] [-properties <properties file>]");
            System.exit(1);
        }

        try
        {
            MonitorEngine engine = new MonitorEngine();
            engine.setLogFile(logFile);
            MonitorEngineDocument doc = new MonitorEngineDocument();
            String content = FileUtils.getFileContents(propsPath);
            doc.parse(content, FormatType.XML);
            engine.init(doc);

            StringBuffer errors = new StringBuffer();
            MonitorResult[][] results = engine.runTasks();
            for (int i=0; i<results.length; i++)
            {
                //Check each monitor
                Monitor monitor = engine.getMonitor(i);
                for (int j=0; j<results[i].length; j++)
                {
                    if (results[i][j].getResult() == MonitorResult.FAILURE)
                    {
                        errors.append("--- FAILURE ---\n")
                              .append("Monitor: ").append( monitor.getName() ).append("\n")
                              .append("   Task: ").append( monitor.getTask(j).getName() ).append("\n\n")
                              .append( results[i][j].getErrorMessage() ).append("\n\n\n");
                    }
                }
            }

            if (errors.length() > 0)
            {
                String[] recips = engine.getFailureRecipients();
                for (int i=0; i<recips.length; i++)
                {
                    SMTPMail mail = new SMTPMail( engine.getMailServer() );
                    mail.setFromAddress( (engine.getFailureFromAddress() != null ? engine.getFailureFromAddress() : "monitor@zitego.com") );
                    mail.setToAddress(recips[i]);
                    mail.setSubject( (engine.getFailureSubject() != null ? engine.getFailureSubject() : "Monitor Task Failures") );
                    mail.setBody( errors.toString() );
                    mail.sendMail();
                }
            }
        }
        catch (Throwable t)
        {
            StringWriter err = new StringWriter();
            t.printStackTrace( new PrintWriter(err) );
            System.out.println("*** ERROR *** Could not execute monitor engine:\r\n"+err);
            System.exit(1);
        }
    }

    /**
     * Creates a new monitor engine.
     */
    public MonitorEngine() { }

    /**
     * Initializes the monitor engine with a Document that is the document which will create
     * the monitor objects.
     *
     * @param doc The Document.
     * @throws MonitorInitializationException if an error occurs initializing.
     */
    public void init(MonitorEngineDocument doc) throws MonitorInitializationException
    {
        if (doc == null) return;
        //Create the database connections
        XmlTag dbConns = (XmlTag)doc.getFirstOccurrenceOf("database-connections");
        if (dbConns != null)
        {
            Vector conns = dbConns.getChildrenWithName("database-connection");
            int size = conns.size();
            for (int i=0; i<size; i++)
            {
                XmlTag conn = (XmlTag)conns.get(i);
                String name = conn.getTagAttribute("name");
                DBConfig config = null;
                try
                {
                    config = new DBConfig
                    (
                        conn.getChildValue("url"),
                        (Driver)Class.forName( conn.getChildValue("driver") ).newInstance(),
                        conn.getChildValue("user"), conn.getChildValue("password"),
                        DBConfig.MYSQL
                    );
                    if ( "1".equals(conn.getTagAttribute("debug")) )
                    {
                        if (_logger == null) setLogFile(null);
                        config.setLogSql(true);
                        config.setLogger(_logger);
                    }
                }
                catch (Exception e)
                {
                    throw new MonitorInitializationException("Could not create db config for connection: "+name, e);
                }
                _dbConns.put( name, DBHandleFactory.getDBHandle(config) );
            }
        }

        //Create the monitors
        Vector tmp = new Vector();
        Vector monitorElements = doc.getChildrenWithName("monitor");
        int size = monitorElements.size();
        for (int i=0; i<size; i++)
        {
            XmlTag tag = (XmlTag)monitorElements.get(i);
            String name = tag.getTagAttribute("name");
            if (name == null) throw new MonitorInitializationException("monitor name is required for all monitors");
            Monitor m = new Monitor(name);
            log( "Adding monitor: "+m.getName() );
            m.setDescription( tag.getChildValue("description") );
            m.init(tag.getChildrenWithName("task"), this);
            tmp.add(m);
        }
        _monitors = new Monitor[size];
        tmp.copyInto(_monitors);

        //Load the email recipients to notify on failure
        XmlTag failureConfig = doc.getFirstOccurrenceOf("failure-config");
        if (failureConfig == null) throw new MonitorInitializationException("Missing email recipients to notify on monitor failure");
        _mailServer = failureConfig.getChildValue("mail-server");
        if (_mailServer == null) throw new MonitorInitializationException("Missing mail-server element in failure-config");
        _failureSubject = failureConfig.getChildValue("subject");
        _failureFromAddress = failureConfig.getChildValue("from");
        Vector recips = failureConfig.getChildrenWithName("recipient");
        _failureRecipients = new String[recips.size()];
        for (int i=0; i<_failureRecipients.length; i++)
        {
            _failureRecipients[i] = ( (XmlTag)recips.get(i) ).getValue();
        }
    }

    /**
     * Runs the tasks in this monitor.
     *
     * @throws MonitorException if an error occurs.
     */
    public MonitorResult[][] runTasks() throws MonitorException
    {
        MonitorResult[][] results = new MonitorResult[_monitors.length][0];
        for (int i=0; i<results.length; i++)
        {
            log( "Running monitor: "+_monitors[i].getName() );
            results[i] = _monitors[i].runTasks();
        }
        return results;
    }

    /**
     * Sets the log file. If it is null, then it sets to standard out.
     *
     * @param String The path to the log file.
     * @throws IOException if the path is invalid.
     */
    public void setLogFile(String path) throws IOException
    {
        if (_logger == null) _logger = Logger.getInstance("com.zitego.web.monitor");
        if (path != null) _logger.registerFileHandler("file", path);
        else _logger.registerConsoleHandler("console");
        log("--------------------------------------------------------------------------------");
        log("Starting MonitorEngine");
    }

    /**
     * Logs a line of test. If the logger is null, it prints out to stdout.
     *
     * @param line The line of text to log.
     */
    public void log(String line)
    {
        if (_logger == null)
        {
            try { setLogFile(null); } catch (IOException ioe) {}
        }

        _logger.log(line);
    }

    /**
     * Returns a database connection given a name.
     *
     * @param name The database connection name.
     * @return DBHandle
     */
    public DBHandle getDBHandle(String name)
    {
        if (name == null) return null;
        else return (DBHandle)_dbConns.get(name);
    }

    /**
     * Returns the monitor at the given index.
     *
     * @param index The index of the monitor to return.
     * @return Monitor
     */
    public Monitor getMonitor(int index)
    {
        return _monitors[index];
    }

    /**
     * Returns the recipients to send failed task alerts to.
     *
     * @return String[]
     */
    public String[] getFailureRecipients()
    {
        return _failureRecipients;
    }

    /**
     * Returns the subject to use with failure emails.
     *
     * @return String
     */
    public String getFailureSubject()
    {
        return _failureSubject;
    }

    /**
     * Returns the from address to use with failure emails.
     *
     * @return String
     */
    public String getFailureFromAddress()
    {
        return _failureFromAddress;
    }

    /**
     * Returns the mail server to use with failure emails.
     *
     * @return String
     */
    public String getMailServer()
    {
        return _mailServer;
    }
}