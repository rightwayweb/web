package com.zitego.web.util;

import com.zitego.web.dropDown.DropDown;
import com.zitego.util.PropertyStore;
import com.zitego.sql.*;
import java.util.Hashtable;

/**
 * A web application version of StaticProperties. Any properties stored within it are
 * static only within the web application context in which it was created with. If more
 * then one StaticWebappProperties object is created per classpath space, then the
 * stored properties will still be unique.
 *
 * This class has getDBHandle methods used for returning DBHandles from the
 * DBHandleFactory. In the DBHandleFactory, DBConfigs are assumed to be stored in
 * StaticProperties. Here, we store the DBConfig local and pass it to the
 * getDBHandle(DBConfig) method.
 *
 * @author John Glorioso
 * @version $Id: StaticWebappProperties.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class StaticWebappProperties extends PropertyStore
{
    /** The cached dropdowns. */
    protected Hashtable _cachedDropDowns = new Hashtable();

    /**
     * Creates a new set of webapp properties.
     */
    public StaticWebappProperties()
    {
        super();
    }

    /**
     * Returns the default DBHandle for this web app based on DBHandleFactory.DEFAULT_DBCONFIG_KEY.
     *
     * @return DBHandle
     */
    public DBHandle getDBHandle()
    {
        return getDBHandle(null);
    }

    /**
     * Returns the default DBHandle for this web app based on the provided key. If the
     * key is null then DBHandleFactory.DEFAULT_DBCONFIG_KEY is used. If there is no
     * DBConfig stored under that key, then null is returned. This just calls
     * DBHandleFactory.getDBHandle() with a DBConfiguration stored with the given
     * key.
     *
     * @param key The name of the key to use to get the DBHandle.
     * @return DBHandle
     */
    public DBHandle getDBHandle(String key)
    {
        if (key == null) key = DBHandleFactory.DEFAULT_DBCONFIG_KEY;
        DBConfig cfg = (DBConfig)getProperty(key);
        if (cfg == null) return null;
        else return DBHandleFactory.getDBHandle(cfg);
    }

    /**
     * Caches the given dropdown keyed by the provided name. If the key or dropdown is null, then nothing
     * is cached.
     *
     * @param key The name of the key to cache by.
     * @param dropDown The drop down to cache.
     */
    public void cacheDropDown(String key, DropDown dropdown)
    {
        if (key != null && dropdown != null) _cachedDropDowns.put(key, dropdown);
    }

    /**
     * Returns a DropDown given the name of its key. If it does not exist or the key is
     * null, then null is returned. The returned drop down is a clone of the one cached.
     *
     * @param key The key.
     * @return DropDown
     */
    public DropDown getDropDown(String key)
    {
        if (key != null)
        {
            DropDown ret = (DropDown)_cachedDropDowns.get(key);
            if (ret != null) ret = (DropDown)ret.clone();
            return ret;
        }
        else
        {
            return null;
        }
    }
}