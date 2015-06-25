package com.zitego.web.layout.template;

import com.zitego.sql.DatabaseEntity;
import com.zitego.sql.DBHandle;
import com.zitego.sql.PreparedStatementSupport;
import com.zitego.sql.NoDataException;
import com.zitego.format.FormatType;
import com.zitego.format.UnsupportedFormatException;
import com.zitego.markup.html.tag.*;
import com.zitego.markup.html.StyleDeclaration;
import java.sql.*;

/**
 * This is a database wrapper class around a page layout object so that it can be stored.
 * The only thing this class cares about it is the layout's xml configuration.
 *
 * @author John Glorioso
 * @version $Id: LayoutEntity.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class LayoutEntity extends DatabaseEntity
{
    /** The PageLayoutTemplate object that this wraps. */
    protected PageLayoutTemplate _layout;
    /** The layout name. */
    protected String _name;
    /** The website this belongs to. */
    protected long _websiteId = -1;
    /** The id of the menu that we are using. */
    protected long _menuId = -1;

    /**
     * Creates a new layout with a database handle.
     *
     * @param db The handle to use for querying.
     */
    public LayoutEntity(DBHandle db)
    {
        super(db);
    }

    /**
     * Creates a new layout with an id and a database handle.
     *
     * @param id The id.
     * @param db The database handle.
     */
    public LayoutEntity(long id, DBHandle db)
    {
        super(id, db);
    }

    public void init() throws SQLException, NoDataException
    {
        StringBuffer sql = new StringBuffer()
            .append("SELECT name, xml, website_id, status_id, creation_date, ")
            .append(    "last_updated, last_updated_by ")
            .append("FROM page_layout ")
            .append("WHERE page_layout_id = ?");
        DBHandle db = getDBHandle();
        db.connect();
        try
        {
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setLong( 1, getId() );
            ResultSet rs = pst.executeQuery();
            if ( !rs.next() ) throw new NoDataException( "No such layout with id: "+getId() );

            _name = rs.getString("name");
            try
            {
                _layout = TemplateFactory.buildTemplate(rs.getString("xml"), db);
            }
            catch (Exception e)
            {
                throw new RuntimeException("Could not build layout", e);
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
        if (_layout == null) throw new IllegalStateException("_layout needs to be set before update is called.");

        StringBuffer sql = new StringBuffer()
            .append("UPDATE page_layout SET name = ?, xml = ?, website_id = ?, status_id = ?, last_updated_by = ? ")
            .append("WHERE page_layout_id = ?");
        DBHandle db = getDBHandle();
        db.connect();
        try
        {
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setString(1, _name);
            //I know for a fact that page layout's handle xml content, so we can just ignore this. :)
            try
            {
                pst.setString( 2, _layout.format(FormatType.XML) );
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
        if (_layout == null) throw new IllegalStateException("_layout needs to be set before insert is called.");

        StringBuffer sql = new StringBuffer()
            .append("INSERT INTO page_layout (name, xml, website_id, status_id, creation_date, last_updated_by) ")
            .append("VALUES (?, ?, ?, ?, now(), ?)");
        DBHandle db = getDBHandle();
        db.connect();
        try
        {
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setString(1, _name);
            //I know for a fact that page layout's handle xml content, so we can just ignore this. :)
            try
            {
                pst.setString( 2, _layout.format(FormatType.XML) );
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
     * Returns the PageLayout object.
     *
     * @return PageLayoutTemplate
     */
    public PageLayoutTemplate getLayout()
    {
        return _layout;
    }

    /**
     * Sets the page layout object.
     *
     * @param layout The layout object.
     * @throws SQLException if there is a database problem loading the menu.
     * @throws NoDataException if there is no menu with the given id.
     */
    public void setLayout(PageLayoutTemplate layout) throws SQLException, NoDataException
    {
        if (_layout != null)
        {
            layout.setTitle( _layout.getTitle() );
            layout.setKeywords( _layout.getKeywords() );
            layout.setDescription( _layout.getDescription() );
            layout.setBgColor( _layout.getBgColor() );
            layout.setBgImage( _layout.getBgImage() );
            layout.setTextColor( _layout.getTextColor() );
            layout.setFontStyle( _layout.getFontStyle() );
            layout.setFontSize( _layout.getFontSize() );
            layout.setPosition( _layout.getPosition() );
            if ( layout.getType().isHeaderType() ) layout.setHeader( _layout.getHeader() );
            if ( layout.getType().isFooterType() ) layout.setFooter( _layout.getFooter() );
            if ( layout.getType().isMenuType() )
            {
                layout.setMenuId( _layout.getMenuId() );
                layout.loadMenu();
            }

            //See if they are going to a custom layout
            if (layout.getType() == TemplateType.CUSTOM)
            {
                //TO DO - figure this out
            }
        }
        _layout = layout;
    }

    /**
     * Sets the xml for the layout object. This will erase the current layout and create a new
     * one.
     *
     * @param xml the xml.
     * @throws Exception if an error occurs setting the xml.
     */
    public void setXml(String xml) throws Exception
    {
        PageLayoutTemplate tmp = null;
        tmp = TemplateFactory.buildTemplate( xml, getDBHandle() );
        _layout = tmp;
    }

    /**
     * Sets the name.
     *
     * @param name The name.
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
     * @param id The website id.
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

    /**
     * Sets the menu id of the menu section.
     *
     * @param id The id.
     */
    public void setMenuId(long id)
    {
        _menuId = id;
    }

    /**
     * Returns the menu if of the menu section.
     *
     * @return long
     */
    public long getMenuId()
    {
        return _menuId;
    }
}