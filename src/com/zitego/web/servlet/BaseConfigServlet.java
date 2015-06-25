package com.zitego.web.servlet;

import com.zitego.logging.Logger;
import com.zitego.logging.NoSuchHandlerException;
import com.zitego.web.util.StaticWebappProperties;
import com.zitego.web.jsp.HelpLinkTag;
import com.zitego.util.StringValidation;
import java.lang.reflect.Method;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

/**
 * This is a base config servlet for use with different web applications. It contains
 * a method for loading configuration that must be implemented called reloadConfig. In addition
 * this requires that "props_path" be supplied in the ServletConfig at initialization time.
 * If the config file is to be checked to reload, then you must also supply "reload_frequency"
 * which is in minutes. Values must be greater then 0 and must be whole. You may also set
 * a "reload_password" in the config object that will force authentication before the service
 * method will do a reload.
 *
 * When the servlet is initialized, a call to createLogger will occur and look for a path to
 * the logger properties file called "logger_props_path". If this value is not supplied then
 * a logger is not created.
 *
 * Properties passed in with the servlet initialization as config parameters will be stored
 * in the webapp properties object. They will be retained when resetStaticProperties
 * is called. Like the logger described below, the StaticWebappProperties are static among
 * the name space of this context. Meaning, only one BaseConfigServlet's properties exist
 * in a single classpath.
 *
 * Lastly, as with all servlets the container manages how many instances are created, therefore
 * the logger instance is static across all instances. If you have two config servlets in the
 * same memory space (classpath), calling log(String) will log a message to <u>all</u> registered
 * loggers. Most servlet containers provide a means to have separate web applications eliminating
 * this problem. Each web application is meant to have only one config servlet.
 *
 * @author John Glorioso
 * @version $Id: BaseConfigServlet.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class BaseConfigServlet extends BaseServlet
{
    private static final String PROPS_FILE_ATTR = "props_path";
    private static final String RELOAD_PASS_ATTR = "reload_password";
    private static final String RELOAD_FREQ_ATTR = "reload_frequency";
    private static final String LOGGER_PROPS_FILE_ATTR = "logger_props_path";
    private static Hashtable _attributeNames = new Hashtable();
    static
    {
        _attributeNames.put(PROPS_FILE_ATTR, "1");
        _attributeNames.put(RELOAD_PASS_ATTR, "1");
        _attributeNames.put(RELOAD_FREQ_ATTR, "1");
        _attributeNames.put(LOGGER_PROPS_FILE_ATTR, "1");
    }
    /** The non-reset properties. */
    private Hashtable _nonResetProperties = new Hashtable();
    /** The properties file path. */
    private String _propertiesFilePath;
    /** The password to authenticate against when reloading. */
    private String _reloadPassword;
    /** The config checker timer. */
    private Timer _configTimer;
    /** The config file loader. */
    private ConfigChecker _configChecker;
    /** The logger for this servlet. */
    private static Logger _logger;
    /* To hold registered webapp properties. */
    private static Hashtable _registeredProperties;
    /** The properties of this webapp config. */
    private static StaticWebappProperties _props;

    /**
     * Sets up the configuration by calling the reloadConfig() method or by setting a timer
     * to do the reloading work if "reload_frequency" is specified. "props_path" must
     * be set and point to the properties file to load the configuration with.
     *
     * @param config The servlet config.
     * @throws ServletException if an error occurs.
     */
    public void init(ServletConfig config) throws ServletException
    {
        super.init(config);

        //Set the properties file path and err if if aint there
        _propertiesFilePath = config.getInitParameter(PROPS_FILE_ATTR);

        //Set the reload password
        _reloadPassword = config.getInitParameter(RELOAD_PASS_ATTR);

        _props = createWebappProperties();

        //Create the logger(s)
        try
        {
            createLogger();
        }
        catch (Exception e)
        {
            throw new ServletException("Could not create logger:", e);
        }

        for (Enumeration e=config.getInitParameterNames(); e.hasMoreElements();)
        {
            String attr = (String)e.nextElement();
            if (_attributeNames.get(attr) == null)
            {
                String val = config.getInitParameter(attr);
                _nonResetProperties.put(attr, "1");
                _props.setProperty(attr, val);
            }
        }

        //Determine if the config file should be reloaded or not
        String tmp = config.getInitParameter(RELOAD_FREQ_ATTR);
        if (tmp != null && _propertiesFilePath != null)
        {
            long reloadSeconds = 1000L * 60L * Long.parseLong(tmp);
            //Set up the config file loader as a daemon
            _configTimer = new Timer(true);
            _configChecker = new ConfigChecker(this);
            //Reload every <reloadSeconds>
            _configTimer.scheduleAtFixedRate(_configChecker, reloadSeconds, reloadSeconds);
        }
        //Force initial call of reload
        reload();
    }

    /**
     * Creates the loggers for this application. The log is set to rotate at midnight
     * each night. The logger is initialized with values from the properties file
     * specified by "logger_props_path" in the ServletConfig object. If this value
     * is not present, then no loggers are created.<br>
     * <br>
     * The expected values in the logger properties file are:
     * <ul>
     *  <li>logger_instance - The name to store this under in the Logger class.
     *                        Ex: "com.zitego.manager". This is required.
     *  <li>register_console - If value is "true" a console handler will be
     *                         initialized. This is optional.
     *  <li>email_handler.admin - The email address to send email alerts to. This
     *                            is optional.
     *  <li>email_handler.from - The from address that alerts will come from. This
     *                           is only required if email_handler.admin is set.
     *  <li>email_hander.subject - The subject of the email. This is only required
     *                             if email_handler.admin is set.
     *  <li>email_hander.server - The smtp server to send emails through. This is
     *                            only required if email_handler.admin is set.
     *  <li>email_handler.smtp_auth - The smtp authentication to be used for mailing in
     *                                username/password format. This is optional.
     *  <li>file_handler.path - The path to create the file handler log. This is
     *                          required.
     *  <li>alert_handler.admin - The email address to send email alerts to. This
     *                            is optional.
     *  <li>alert_handler.from - The from address that alerts will come from. This
     *                           is only required if alert_handler.admin is set.
     *  <li>alert_hander.subject - The subject of the email. This is only required
     *                             if alert_handler.admin is set.
     *  <li>alert_hander.server - The smtp server to send emails through. This is
     *                            only required if alert_handler.admin is set.
     *  <li>alert_handler.smtp_auth - The smtp authentication to be used for mailing in
     *                                username/password format. This is optional.
     * </ul>
     *
     * @throws IllegalArgumentException if any required values are not set.
     * @throws IOException if an error occurs reading the props file.
     * @throws FileNotFoundException if the props file cannot be found.
     */
    protected void createLogger() throws IllegalArgumentException, IOException, FileNotFoundException
    {
        String loggerPropsPath = getConfig().getInitParameter(LOGGER_PROPS_FILE_ATTR);
        if (loggerPropsPath == null) return;

        Properties props = new Properties();
        props.load( new FileInputStream(loggerPropsPath) );

        //Create the logger
        String prop = props.getProperty("logger_instance");
        if (prop == null) throw new IllegalArgumentException("logger_instance was not specified");
        _logger = Logger.getInstance(prop);

        //Register a console handler if we are supposed to
        if ( "true".equalsIgnoreCase(props.getProperty("register_console")) )
        {
            _logger.registerConsoleHandler("console");
        }

        //Register an email handler if we are supposed to
        prop = props.getProperty("email_handler.admin");
        if (prop != null)
        {
            String from = props.getProperty("email_handler.from");
            if (from == null) throw new IllegalArgumentException("email_handler.from is required for email handler");
            String subject = props.getProperty("email_handler.subject");
            if (subject == null) throw new IllegalArgumentException("email_handler.subject is required for email handler");
            String server = props.getProperty("email_handler.server");
            if (subject == null) throw new IllegalArgumentException("email_handler.server is required for email handler");
            String auth = props.getProperty("email_handler.smtp_auth");
            if (auth != null)
            {
                int index = auth.indexOf("/");
                if (index == -1) throw new IllegalArgumentException("Invalid smtp_auth: "+auth);
                _logger.registerEmailHandler
                (
                    "email", prop, from, subject, server, auth.substring(0, index), auth.substring(index+1)
                );
            }
            else
            {
                _logger.registerEmailHandler("email", prop, from, subject, server);
            }
        }

        //Register an email alert handler if we are supposed to
        prop = props.getProperty("alert_handler.admin");
        if (prop != null)
        {
            String from = props.getProperty("alert_handler.from");
            if (from == null) throw new IllegalArgumentException("alert_handler.from is required for email handler");
            String subject = props.getProperty("alert_handler.subject");
            if (subject == null) throw new IllegalArgumentException("alert_handler.subject is required for email handler");
            String server = props.getProperty("alert_handler.server");
            if (subject == null) throw new IllegalArgumentException("alert_handler.server is required for email handler");
            String auth = props.getProperty("alert_handler.smtp_auth");
            if (auth != null)
            {
                int index = auth.indexOf("/");
                if (index == -1) throw new IllegalArgumentException("Invalid smtp_auth: "+auth);
                _logger.registerEmailHandler
                (
                    "email_alerts", prop, from, subject, server, auth.substring(0, index), auth.substring(index+1)
                );
            }
            else
            {
                _logger.registerEmailHandler("email_alerts", prop, from, subject, server);
            }
        }

        //Create the error log
        //Rotate each day at midnight
        prop = props.getProperty("file_handler.path");
        if (prop == null) throw new IllegalArgumentException("file_handler.path was not specified");
        GregorianCalendar midnight = new GregorianCalendar();
        midnight.add(Calendar.DATE, 1);
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        _logger.registerFileHandler( "error_log", prop, midnight.getTime() );
    }

    /**
     * Returns the logger.
     *
     * @return Logger
     */
    public static Logger getLogger()
    {
        return _logger;
    }

    /**
     * Contains logic to call the reload method if the "reload" parameter is passed in.
     * In addition, a password can be set in the ServletConfig object as "reload_pass".
     * If you wish to disable reloading, then simply override the service method and do
     * <u>not</u> call super.service(request, response).
     *
     * @param request The request.
     * @param response The response.
     * @throws IOException if an error occurs.
     * @throws ServletException if a servlet exception occurs.
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        if (request.getParameter("reload") != null)
        {
            if ( _reloadPassword == null || _reloadPassword.equals(request.getParameter("pass")) )
            {
                reload();
                sendResponse(response, "OK");
            }
        }
    }

    /**
     * Logs some information and calls reloadConfig. If the string_validation.js_functions_path property
     * is set in the StaticWebappProperties, the StringValidation.createJsSourceFile() will method will
     * automatically write out the methods in it to javascript. @see com.zitego.util.StringValidation
     * for more details.
     * If there is a property in StaticWebappProperties called help_page_map.path, it will be used
     * to load the help page map from that file. Once the map is loaded it is stored as
     * HelpLinkTag.HELP_PAGE_MAP_KEY. In addition, help_page_map.path is removed.
     *
     * @throws ServletException if an error occurs reloading.
     */
    public synchronized final void reload() throws ServletException
    {
        logError("--------------------------------------------------------------------------------");
        logError("Starting " + getClass() + " - " + getApplicationName() + ":");

        try
        {
            resetStaticProperties();

            //Create the js source file
            StringValidation.createJsSourceFile
            (
                (String)_props.getProperty("string_validation.js_functions_path"),
                _logger
            );

            //If there is a help_page_map.path property set, load up the map and cache it
            String prop = (String)_props.getProperty("help_page_map.path");
            if (prop != null)
            {
                logError("Loading help page map");
                Properties map = new Properties();
                map.load( new FileInputStream(prop) );
                _props.setProperty(HelpLinkTag.HELP_PAGE_MAP_KEY, map);
                _props.removeProperty("help_page_map.path");
            }

            reloadConfig();
        }
        catch (Exception e)
        {
            logSevereError(e);
            throw new ServletException("An error occurred reloading", e);
        }

        logError("Finished " + getApplicationName() + " startup");
    }

    /**
     * This is where the guts of the configuration servlet goes. Here you can do pretty
     * much anything you want. This gets called in the init method when the servlet is
     * first initialized and from service is reload is enabled. This method does not
     * throw any exceptions because it is expected to handle them internally. This
     * method may need to be declared as synchronized to prevent concurrent modifications
     * from happening.
     *
     * @throws Exception if an error occurs reloading.
     */
    protected abstract void reloadConfig() throws Exception;

    /**
     * Returns the name of the application this is a config for.
     *
     * @return String
     */
    public abstract String getApplicationName();

    /**
     * Returns the properties file path.
     *
     * @return String
     */
    protected String getPropertiesFilePath()
    {
        return _propertiesFilePath;
    }

    /**
     * Logs an error to the logger. If the log is null, then it prints to System.err.
     *
     * @param msg The message to log.
     */
    public static void logError(String msg)
    {
        if (_logger == null) System.err.println(msg);
        else logError(_logger, msg);
    }

    /**
     * Logs a severe error to the logger. If the log is null, then it prints to System.err.
     *
     * @param msg The message to log.
     */
    public static void logSevereError(String msg)
    {
        if (_logger == null) System.err.println(msg);
        else _logger.log(msg, java.util.logging.Level.SEVERE);
    }

    /**
     * Logs a Throwable exception to the logger. If the log is null, then it prints to System.err.
     *
     * @param t The error to log.
     */
    public static void logSevereError(Throwable t)
    {
        StringWriter trace = new StringWriter();
        t.printStackTrace( new PrintWriter(trace, true) );
        logSevereError("Error! " + trace.toString() );
    }

    /**
     * Sends an email alert to the admins through the logger. If the logger is null,
     * then a severe error is logged noting the failure. Once the logger is obtained,
     * it will look for an "email" logger. If a NoSuchHandlerException is thrown, then
     * a severe error is logged noting the failure.
     *
     * @param msg The message to email.
     */
    public static void sendAlert(String msg)
    {
        if (_logger == null) logSevereError("Logger is not set. Alert failed. Message: "+msg);
        try
        {
            _logger.logTo("email_alerts", msg, java.util.logging.Level.SEVERE);
        }
        catch (NoSuchHandlerException nsh)
        {
            try
            {
                _logger.logTo("email", msg, java.util.logging.Level.SEVERE);
            }
            catch (NoSuchHandlerException nsh2)
            {
                logSevereError("Email handler does not exist. Alert failed. Message: "+msg);
            }
        }
    }

    /**
     * Logs an error using the specified logger.
     *
     * @param logger The logger.
     * @param msg The message to log.
     */
    public static void logError(Logger logger, String msg)
    {
        logger.log(msg);
    }

    /**
     * Adds a config timer task. This means that a file is added to be checked and
     * a method is specifed to call when the file has changed. This will not automatically
     * be called like the reload method on the props_path file. You must execute the
     * method manually first. The given method <u>must</u> be declared public or a
     * <code>IllegalAccessException</code> will be thrown when it is invoked. In addition,
     * this method cannot be called unless the props_path is set to be reloaded.
     *
     * @param path The file path to check.
     * @param method The method to call.
     * @throws IllegalStateException if the props_path is not reloadable.
     */
    protected void addConfigCheckerTask(String path, String method) throws IllegalStateException
    {
        if (_configChecker == null) throw new IllegalStateException("props_path must be set to reloadable");
        _configChecker.addTask(path, method);
    }

    /**
     * Overrides the BaseServlet method cause config servlets do not have error pages.
     * This returns null.
     *
     * @return String
     */
    public String getErrorPage()
    {
        return null;
    }

    /**
     * Resets all the properties in StaticWebappProperties that were not passed in with
     * the servlet config. It then reloads the properties file if it was specified.
     *
     * @throws FileNotFoundException if the properties file is not found.
     * @throws IOException if an io error occurs.
     */
    protected synchronized void resetStaticProperties() throws FileNotFoundException, IOException
    {
        if (_props == null) _props = createWebappProperties();

        Vector namesToRemove = new Vector();
        for (Iterator i=_props.getPropertyNames().iterator(); i.hasNext();)
        {
            String attr = (String)i.next();
            if (_nonResetProperties.get(attr) == null) namesToRemove.add(attr);
        }
        int size = namesToRemove.size();
        for (int i=0; i<size; i++)
        {
            _props.removeProperty( (String)namesToRemove.get(i) );
        }

        if (_propertiesFilePath != null)
        {
            //Load the properties file
            logError("Loading properties file: " + _propertiesFilePath);
            _props.loadProperties( new FileInputStream(_propertiesFilePath) );
        }
    }

    /**
     * Creates the webapp properties and returns it.
     *
     * @return StaticWebappProperties
     */
    protected StaticWebappProperties createWebappProperties()
    {
        return new StaticWebappProperties();
    }

    /**
     * Returns the StaticWebappProperties object.
     *
     * @return StaticWebappProperties
     */
    public static StaticWebappProperties getWebappProperties()
    {
        //Check to see if this was never initialized. If so, return an empty props
        //so that null pointers won't occur when calling classes call
        //BaseConfigServlet.getWebappProperties().getProperty(...)
        if (_props == null) _props = new StaticWebappProperties();
        return _props;
    }

    public void destroy()
    {
        logError("Shutting down " + getClass() + " - " + getApplicationName() + ".");
    }

    /**
     * This is a private class that just checks the last modified time on the "props_path"
     * file.
     */
    private class ConfigChecker extends TimerTask
    {
        /** The parent servlet. */
        private BaseConfigServlet _parent;
        /** The additional files to check. */
        private Vector _tasks = new Vector(4, 4);

        private ConfigChecker(BaseConfigServlet parent)
        {
            _parent = parent;
            addTask(_propertiesFilePath, "reload");
            //Set last checked as now so it does not get run twice immediately from
            //the forced reload call above
            ( (Task)_tasks.get(0) ).lastChecked = System.currentTimeMillis();
        }

        /**
         * Checks to see if the config file may have changed.
         */
        public void run()
        {
            int size = _tasks.size();
            for (int i=0; i<size; i++)
            {
                ( (Task)_tasks.get(i) ).check();
            }
        }

        /**
         * Adds a task.
         */
        private void addTask(String path, String method)
        {
            try
            {
                Task t = new Task();
                t.file = new File(path);
                t.method = _parent.getClass().getMethod(method, (Class[])null);
                if ( t.file.exists() ) t.lastChecked = t.file.lastModified();
                _tasks.add(t);
            }
            catch (NoSuchMethodException nsme)
            {
                logSevereError("Could not add task "+method+" for "+path+": "+nsme.toString());
            }
        }

        private class Task
        {
            private File file;
            private Method method;
            private long lastChecked = 0l;

            private Task() { }

            private void check()
            {
                if (file.exists() && file.lastModified() > lastChecked)
                {
                    try
                    {
                        method.invoke(_parent, (Object[])null);
                    }
                    catch (Exception e)
                    {
                        StringWriter trace = new StringWriter();
                        e.printStackTrace( new PrintWriter(trace, true) );
                        logSevereError( "Error invoking "+method.getName()+": "+trace.toString() );
                    }
                    //Leave this down here in case of an error it will not try repeatedly
                    lastChecked = file.lastModified();
                }
            }
        }
    }
}