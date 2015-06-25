package com.zitego.web.menu;

import com.zitego.web.menu.button.ButtonOrientation;
import com.zitego.sql.DBHandle;
import com.zitego.sql.PreparedStatementSupport;
import com.zitego.sql.DatabaseEntity;
import com.zitego.sql.NoDataException;
import com.zitego.format.FormatType;
import com.zitego.format.UnsupportedFormatException;
import java.sql.*;

/**
 * This is a database wrapper class around a menu object so that it can be stored.
 * The only thing this class cares about it is the menu's xml configuration.
 *
 * @author John Glorioso
 * @version $Id: MenuEntity.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class MenuEntity extends DatabaseEntity
{
    /** The menu object that this wraps. Default is an empty object with horizontal buttons. */
    protected Menu _menu = new Menu(ButtonOrientation.HORIZONTAL);
    /** The menu name. */
    protected String _name;
    /** The website this belongs to. */
    protected long _websiteId = -1;

    /**
     * Creates a new menu with a database handle.
     *
     * @param DBHandle The handle to use for querying.
     */
    public MenuEntity(DBHandle db)
    {
        super(db);
    }

    /**
     * Creates a new menu with an id and a database handle.
     *
     * @param long The id.
     * @param DBHandle The database handle.
     */
    public MenuEntity(long id, DBHandle db)
    {
        super(id, db);
    }

    public void init() throws SQLException, NoDataException
    {
        StringBuffer sql = new StringBuffer()
            .append("SELECT name, xml, website_id, status_id, creation_date, ")
            .append(    "last_updated, last_updated_by ")
            .append("FROM menu ")
            .append("WHERE menu_id = ?");
        DBHandle db = getDBHandle();
        db.connect();
        try
        {
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setLong( 1, getId() );
            ResultSet rs = pst.executeQuery();
            if ( !rs.next() ) throw new NoDataException( "No such menu with id: "+getId() );

            _name = rs.getString("name");
            try
            {
                _menu = Menu.buildMenu( rs.getString("xml") );
            }
            catch (Exception e)
            {
                throw new RuntimeException( "Could not build menu: "+e.toString() );
            }
            _websiteId = rs.getLong("website_id");
            if ( rs.wasNull() ) _websiteId = -1;
            setStatus( rs.getInt("status_id") );
            setCreationDate( rs.getTimestamp("creation_date") );
            setLastUpdated( rs.getTimestamp("last_updated") );
            setLastUpdatedBy( rs.getLong("last_updated_by") );
        }
        finally
        {
            db.disconnect();
        }
    }

    public void update() throws SQLException
    {
        StringBuffer sql = new StringBuffer()
            .append("UPDATE menu SET name = ?, xml = ?, website_id = ?, status_id = ?, ")
            .append(    "last_updated_by = ? ")
            .append("WHERE menu_id = ?");
        DBHandle db = getDBHandle();
        db.connect();
        try
        {
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setString(1, _name);
            //I know for a fact that menu's handle xml content, so we can just ignore this. :)
            try
            {
                pst.setString( 2, _menu.format(FormatType.XML) );
            }
            catch (UnsupportedFormatException ufe) { }
            pst.setLong(3, _websiteId);
            pst.setInt( 4, getStatus() );
            if (getLastUpdatedBy() > -1) pst.setLong( 5, getLastUpdatedBy() );
            else pst.setNull(5, Types.NUMERIC);
            pst.setLong( 6, getId() );
            pst.executeUpdate();
        }
        finally
        {
            db.disconnect();
        }
    }

    public void insert() throws SQLException
    {
        StringBuffer sql = new StringBuffer()
            .append("INSERT INTO menu (name, xml, website_id, status_id, creation_date, last_updated_by) ")
            .append("VALUES (?, ?, ?, ?, now(), ?)");
        DBHandle db = getDBHandle();
        db.connect();
        try
        {
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setString(1, _name);
            //I know for a fact that menu's handle xml content, so we can just ignore this. :)
            try
            {
                pst.setString( 2, _menu.format(FormatType.XML) );
            }
            catch (UnsupportedFormatException ufe) { }
            pst.setLong(3, _websiteId);
            pst.setInt( 4, getStatus() );
            if (getLastUpdatedBy() > -1) pst.setLong( 5, getLastUpdatedBy() );
            else pst.setNull(5, Types.NUMERIC);
            pst.executeUpdate();
            setId( db.getLastId(null) );
        }
        finally
        {
            db.disconnect();
        }
    }

    /**
     * Returns the menu object. If the menu object is null, it returns a new Menu
     * with a default orientation of horizontal.
     *
     * @return Menu
     */
    public Menu getMenu()
    {
        return _menu;
    }

    /**
     * Sets the xml for the menu object. This will erase the current menu and create a new
     * one.
     *
     * @param String the xml.
     * @throws Exception if an error occurs setting the xml.
     */
    public void setXml(String xml) throws Exception
    {
        Menu tmp = null;
        tmp = Menu.buildMenu(xml);
        _menu = tmp;
    }

    /**
     * Sets the name.
     *
     * @param String The name.
     */
    public void setName(String name)
    {
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
     * Sets the website id this belongs to.
     *
     * @param long The website id.
     */
    public void setWebsiteId(long id)
    {
        _websiteId = id;
    }

    /**
     * Returns the website id this belongs to.
     *
     * @return long
     */
    public long getWebsiteId()
    {
        return _websiteId;
    }
}