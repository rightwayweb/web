package com.zitego.web.thirdPartyAPI;

import com.zitego.web.servlet.BaseConfigServlet;
import com.zitego.util.StaticProperties;
import java.util.Hashtable;
import java.lang.reflect.Method;

/**
 * <p>This is an abstract class to be the root of all 3rd party api manager classes.
 * A manager class is a class with static methods to access a 3rd party api and
 * get a response back. The api can use any network protocol to send and retrieve
 * information. This class is just a wrapper to insure the api is instantiated
 * and configured before use.</p>
 * <p>The api can be set using the setApi method or by letting it load automatically.
 * If a call is made to reference the api and it is not yet set, it will attempt
 * to be loaded by looking for a property configuration in the BaseConfigServlet's
 * StaticWebappProperties. If it is not found there, then it will check StaticProperties with
 * the property name provided to it. If this property does not exist,
 * then an IllegalStateException will be thrown from whichever method is being
 * called.</p>
 *
 * <p>The properties should be in the following format.<br>
 * <pre>
 * property_name=<classpath>=\
 *               property1=value1,\
 *               property2=value2,\
 *               etc...
 * </pre>
 * The classpath is the fully qualified class to the path of the api.
 * The property/value pairs are optional and will set a string property of
 * the given property name to the given value. The property uses java bean
 * reflection and looks for a setProperty method that matches the property.</p>
 * <p>In each method that makes a call to the api, getAPI() should be
 * called to instantiate it if necessary and return it for use.</p>
 *
 * @see ThirdPartyAPI
 * @author John Glorioso
 * @version $Id: ThirdPartyAPIManager.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class ThirdPartyAPIManager
{
    /** The 3rd party api instance. */
    private static Hashtable _apis = new Hashtable();

    /**
     * Creates a new third party api by looking for the api property provided
     * in either BaseConfigServlet.getWebappProperties or the StaticProperties
     * (in that order) class and returns it.
     *
     * @param prop The property name.
     * @throws RuntimeException if an error occurs creating the api.
     */
    private static ThirdPartyAPI createAPI(String prop) throws RuntimeException
    {
        if (prop == null) throw new IllegalArgumentException("Property name cannot be null");

        ThirdPartyAPI ret = null;
        //Make sure that two of the same api are not instantiated at the same time
        synchronized (prop)
        {
            //Check to see if it was already created
            ret = (ThirdPartyAPI)_apis.get(prop);
            if (ret != null) return ret;

            String propString = (String)BaseConfigServlet.getWebappProperties().getProperty(prop);
            if (propString == null) propString = (String)StaticProperties.getProperty(prop);
            if (propString == null) throw new RuntimeException("ThirdPartyAPI could not be created. "+prop+" not set in either BaseConfigServlet or StaticProperties");
            ret = createThirdPartyAPI(propString);
            _apis.put(prop, ret);
        }
        return ret;
    }
    
    /**
     * Returns a third party api given a property string. The api returned is dependant on
     * the property string being in the format as specified in the class documentation above.
     * If the property string is null, then null is returned.
     *
     * @param propString The property string.
     * @return ThirdPartyAPI
     * @throws RuntimeException if an error occurs.
     */
    public static ThirdPartyAPI createThirdPartyAPI(String propString)
    {
        if (propString == null) return null;
        ThirdPartyAPI ret = null;
        int index = propString.indexOf("=");
        if (index == -1) index = propString.length();
        try
        {
            Class apiClass = Class.forName( propString.substring(0, index) );
            ret = (ThirdPartyAPI)apiClass.newInstance();
            propString = propString.substring(index+1);
            String[] tokens = propString.split(",");
            if (tokens != null)
            {
                for (int i=0; i<tokens.length; i++)
                {
                    String[] token = tokens[i].split("=");
                    String t = token[0].trim();
                    Method m = apiClass.getMethod
                    (
                        (token[0].length() > 0 ? "set"+t.substring(0,1).toUpperCase()+t.substring(1) : ""),
                        new Class[] { String.class }
                    );
                    m.invoke(ret, new Object[] { token[1] });
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not create ThirdPartyAPI: ", e);
        }
        return ret;
    }

    /**
     * Sets the api given its property name. If the api is null, it is reset.
     *
     * @param api The api.
     * @param prop The property name.
     * @throws IllegalArgumentException if the property name is null.
     */
    public void setAPI(ThirdPartyAPI api, String prop) throws IllegalArgumentException
    {
        if (prop == null) throw new IllegalArgumentException("Property name cannot be null");
        if (api != null) _apis.put(prop, api);
        else _apis.remove(prop);
    }

    /**
     * Returns the api. If it is not yet instantiated, then it will make a call
     * to do so from the  given the property name.
     *
     * @param prop The property name.
     * @throws IllegalArgumentException if the property name is null.
     */
    protected static ThirdPartyAPI getAPI(String prop) throws IllegalArgumentException
    {
        if (prop == null) throw new IllegalArgumentException("Property name cannot be null");
        ThirdPartyAPI api = (ThirdPartyAPI)_apis.get(prop);
        if (api == null) api = createAPI(prop);
        return api;
    }
}