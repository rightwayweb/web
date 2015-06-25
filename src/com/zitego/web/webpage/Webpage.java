package com.zitego.web.webpage;

import com.zitego.web.layout.template.LayoutEntity;
import com.zitego.web.layout.template.PageLayoutTemplate;
import com.zitego.web.layout.body.PageBodyLayout;
import com.zitego.web.layout.body.PageBodyLayoutType;
import com.zitego.markup.html.tag.Html;
import com.zitego.markup.html.tag.table.Td;
import com.zitego.format.*;
import com.zitego.sql.DatabaseEntity;
import com.zitego.sql.DBHandle;
import com.zitego.sql.NoDataException;
import com.zitego.util.FileUtils;
import java.sql.*;
import java.io.IOException;

/**
 * This is a base class for all webpages. A webpage has a page body layout type if
 * it is associated with a page layout template. All pages have a url, name, and
 * path to where they are on the server. The url is based off of the path and the
 * base path. There are three ways to get html for this webpage. If there is a layout
 * specified to be used with this page, then the layout is used to generate the html,
 * otherwise if there is only a body template to use, then it is used to generate
 * the html. If there is neither a layout or a body template, then the html is set
 * and get manually.
 *
 * @author John Glorioso
 * @version $Id: Webpage.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class Webpage extends DatabaseEntity
{
    /** The page layout template entity if assigned to one. */
    protected LayoutEntity _layoutEntity;
    /** The body layout template. */
    protected PageBodyLayout _bodyLayout;
    /** The website id this page belongs to. */
    protected long _websiteId = -1;
    /** The name of the web page. */
    protected String _name;
    /**
     * The base path to the webpage's relative url directory. For example, if the file path is
     * /home/httpd/hosted_domains/1/index.html, then the base path would be
     * /home/httpd/domains/hosted_domains/1/.
     */
    protected String _basePath;
    /** The file path to the page. */
    protected String _filePath;
    /** The html for this web page. */
    protected String _html;

    /**
     * Creates a new web page with a database handle.
     *
     * @param DBHandle The handle to use for querying.
     * @param String The base path to this webpage's html root.
     */
    public Webpage(DBHandle db, String basePath)
    {
        super(db);
        _basePath = basePath;
    }

    /**
     * Creates a web page with an id and a database handle.
     *
     * @param long The id.
     * @param DBHandle The database handle.
     * @param String The base path to this webpage's html root.
     */
    public Webpage(long id, DBHandle db, String basePath)
    {
        super(id, db);
        _basePath = basePath;
    }

    public void init() throws SQLException, NoDataException
    {
        StringBuffer sql = new StringBuffer()
            .append("SELECT name, file_path, page_layout_id, body_template_type_id, website_id, ")
            .append(    "status_id, creation_date, last_updated, last_updated_by ")
            .append("FROM webpage ")
            .append("WHERE webpage_id = ?");
        PageBodyLayoutType type = null;
        DBHandle db = getDBHandle();
        db.connect();
        try
        {
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setLong( 1, getId() );
            ResultSet rs = pst.executeQuery();
            if ( !rs.next() ) throw new NoDataException( "No such webpage with id: "+getId() );

            _name = rs.getString("name");
            _filePath = rs.getString("file_path");
            long id = rs.getLong("page_layout_id");
            if (id > -1) _layoutEntity = new LayoutEntity(id, db);
            type = PageBodyLayoutType.evaluate( rs.getInt("body_template_type_id") );
            _websiteId = rs.getLong("website_id");
            if ( rs.wasNull() ) _websiteId = -1;
            setStatus( rs.getInt("status_id") );
            setCreationDate( rs.getTimestamp("creation_date") );
            setLastUpdated( rs.getTimestamp("last_updated") );
            setLastUpdatedBy( rs.getLong("last_updated_by") );

            if (_layoutEntity != null) _layoutEntity.init();
        }
        finally
        {
            db.disconnect();
        }

        //Get the html
        try
        {
            _html = FileUtils.getFileContents(_filePath);
        }
        catch (IOException ioe)
        {
            throw new RuntimeException("Could not load file at: "+_filePath, ioe);
        }

        //Load the template
        if (type != null && _layoutEntity != null)
        {
            //Need to get the body (must be inside of <!-- Begin body Section. DO NOT REMOVE THIS COMMENT! -->
            //If the comment is not there, then we have to resort to a custom body
            int index = -1;
            if (_html != null && (index=_html.indexOf("<!-- Begin body Section")) > -1)
            {
                index = _html.indexOf("<table", index);
                _html = _html.substring(index);
                _bodyLayout = _layoutEntity.getLayout().createBodyLayout(type);
                try
                {
                    _bodyLayout.parse(new StringBuffer(_html), FormatType.HTML);
                    _html = _bodyLayout.formatAsRoot(FormatType.HTML);
                }
                catch (UnsupportedFormatException ufe)
                {
                    throw new RuntimeException("Could not parse the body layout", ufe);
                }
            }
            else
            {
                _bodyLayout = null;
                _layoutEntity = null;
            }
        }
    }

    public void update() throws SQLException
    {
        StringBuffer sql = new StringBuffer()
            .append("UPDATE webpage SET name = ?, file_path = ?, page_layout_id = ?, ")
            .append(    "body_template_type_id = ?, website_id = ?, status_id = ?, last_updated_by = ? ")
            .append("WHERE webpage_id = ?");
        DBHandle db = getDBHandle();
        db.connect();
        try
        {
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setString(1, _name);
            pst.setString(2, _filePath);
            if (_layoutEntity != null) pst.setLong( 3, _layoutEntity.getId() );
            else pst.setLong(3, -1);
            if (_bodyLayout != null) pst.setInt( 4, _bodyLayout.getType().getValue() );
            else pst.setLong(4, -1);
            pst.setLong(5, _websiteId);
            pst.setInt( 6, getStatus() );
            if (getLastUpdatedBy() > -1) pst.setLong( 7, getLastUpdatedBy() );
            else pst.setNull(7, Types.NUMERIC);
            pst.setLong( 8, getId() );
            pst.executeUpdate();
        }
        finally
        {
            db.disconnect();
        }
        try
        {
            updateFile();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not write file at: "+_filePath, e);
        }
    }

    public void insert() throws SQLException
    {
        StringBuffer sql = new StringBuffer()
            .append("INSERT INTO webpage ")
            .append(    "(name, file_path, page_layout_id, body_template_type_id, website_id, ")
            .append(     "status_id, creation_date, last_updated_by) ")
            .append("VALUES (?, ?, ?, ?, ?, ?, now(), ?)");
        DBHandle db = getDBHandle();
        db.connect();
        try
        {
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setString(1, _name);
            pst.setString(2, _filePath);
            if (_layoutEntity != null) pst.setLong( 3, _layoutEntity.getId() );
            else pst.setLong(3, -1);
            if (_bodyLayout != null) pst.setInt( 4, _bodyLayout.getType().getValue() );
            else pst.setLong(4, -1);
            pst.setLong(5, _websiteId);
            pst.setInt( 6, getStatus() );
            if (getLastUpdatedBy() > -1) pst.setLong( 7, getLastUpdatedBy() );
            else pst.setNull(7, Types.NUMERIC);
            pst.executeUpdate();
            setId( db.getLastId(null) );
        }
        finally
        {
            db.disconnect();
        }
        try
        {
            updateFile();
        }
        catch (Exception e)
        {
            throw new RuntimeException("Could not write file at: "+_filePath, e);
        }
    }

    /**
     * Updates the file on the server based on the set file path. If the file path has changed since
     * it was initialized, then the old file does not get deleted.
     *
     * @throws IOException if an error occurs writing the file out.
     * @throws UnsupportedFormatException if there was a problem formatting to html.
     */
    public void updateFile() throws IOException, UnsupportedFormatException
    {
        //If there is a _bodyLayout, then we write the parent, otherwise, we write the html string
        String html = _html;
        if (_layoutEntity != null) html = _layoutEntity.getLayout().getHtmlTag().format(FormatType.HTML);
        FileUtils.writeFileContents(_filePath, html);
    }

    /**
     * Sets the page layout entity.
     *
     * @param LayoutEntity The page layout entity.
     * @param String The base domain of this page.
     */
    public void setLayoutEntity(LayoutEntity layout, String domain)
    {
        _layoutEntity = layout;
        //Make sure to reset the body layout
        if (_bodyLayout != null)
        {
            //Set the body layout parent to a new html body tag
            _bodyLayout.setParent( new Html(domain).getBodyTag() );
            _bodyLayout.addToParent();
        }
    }

    /**
     * Returns the LayoutEntity.
     *
     * @return LayoutEntity
     */
    public LayoutEntity getLayoutEntity()
    {
        return _layoutEntity;
    }

    /**
     * This will set the page body layout. This method will check to see if the layout
     * is the same one that is in the LayoutEntity (if one exists). If it is the same,
     * then the layout does not change. If it is different, then the one in the layout
     * is replaced.
     *
     * @param PageBodyLayout The new layout.
     */
    public void setBodyLayout(PageBodyLayout layout)
    {
        if (_layoutEntity != null)
        {
            PageLayoutTemplate template = _layoutEntity.getLayout();
            if (template.getBodyLayout() != layout) template.setBodyLayout(layout);
        }
        _bodyLayout = layout;
    }

    /**
     * Returns the body layout.
     *
     * @return PageBodyLayout
     */
    public PageBodyLayout getBodyLayout()
    {
        return _bodyLayout;
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
     * Sets the file path.
     *
     * @param String The file path.
     */
    public void setFilePath(String path)
    {
        _filePath = path;
    }

    /**
     * Returns the file path.
     *
     * @return String
     */
    public String getFilePath()
    {
        return _filePath;
    }

    /**
     * Sets the url and implicitly sets the file path as well using the base path
     * given at construction time. This url should be the path of the web page
     * relative to the base path.
     *
     * @param String The url.
     */
    public void setUrl(String url)
    {
        if (url != null) _filePath = _basePath + url;
        else _filePath = null;
    }

    /**
     * Returns the url path portion of the file path.
     *
     * @return String
     */
    public String getPath()
    {
        if (_filePath != null) return _filePath.substring( _basePath.length(), _filePath.lastIndexOf("/")+1 );
        else return null;
    }

    /**
     * Returns the filename portion of the file path.
     *
     * @return String
     */
    public String getFilename()
    {
        if (_filePath != null) return _filePath.substring(_filePath.lastIndexOf("/")+1);
        else return null;
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

    /**
     * Sets the html of the body or for the full page. If there is a layout, then it will try
     * to parse the html with the current PageLayoutBodyType. If The html is null, then the layout
     * will be set to null as well.
     *
     * @param String The html.
     * @throws UnsupportedFormatException if the html is not supported.
     */
    public void setHtml(String html) throws UnsupportedFormatException
    {
        if ( html == null || "".equals(html) )
        {
            _html = null;
            _layoutEntity = null;
            _bodyLayout = null;
        }
        //Only need to set the html in the body layout, cause if there is a layout entity
        //then this layout is part of it.
        else if (_bodyLayout != null)
        {
            _bodyLayout.parse(new StringBuffer(_html), FormatType.HTML);
            _html = _bodyLayout.format(FormatType.HTML);
        }
        else
        {
            _html = html;
        }
    }

    /**
     * This will return the full html of the web page if there is no layout associated with it
     * or the body template html if there is. To get the full html when there is a layout, use
     * getFullHtml().
     *
     * @return String
     */
    public String getHtml()
    {
        return _html;
    }

    /**
     * Returns the full html of the page. To get only the body html of the layout, use getHtml().
     *
     * @return String
     * @throws UnsupportedFormatException if the html cannot be formatted.
     */
    public String getFullHtml() throws UnsupportedFormatException
    {
        if (_layoutEntity != null) return _layoutEntity.getLayout().getHtmlTag().format(FormatType.HTML);
        else if (_bodyLayout != null) return _bodyLayout.getHtmlTag().format(FormatType.HTML);
        else return _html;
    }

    /**
     * Sets the title in the page layout or body template depending on what they have done. Calling
     * this method with manual html using no templates will cause nothing to happen.
     *
     * @param String The title.
     */
    public void setTitle(String title)
    {
        if (_layoutEntity != null) _layoutEntity.getLayout().setTitle(title);
        else if (_bodyLayout != null) _bodyLayout.getHeadTag().setTitle(title);
    }

    /**
     * Returns the title from the page layout or the body template. If the html is being set manual,
     * then null is returned.
     *
     * @return String
     */
    public String getTitle()
    {
        if (_layoutEntity != null) return _layoutEntity.getLayout().getTitle();
        else if (_bodyLayout != null) return _bodyLayout.getHeadTag().getTitle();
        else return null;
    }

    /**
     * Sets the list of search engine keywords in the page layout or body template depending on what
     * they have done. Calling this method with manual html using no templates will cause nothing to
     * happen.
     *
     * @param String The list.
     */
    public void setKeywords(String list)
    {
        if (_layoutEntity != null) _layoutEntity.getLayout().setKeywords(list);
        else if (_bodyLayout != null) _bodyLayout.getHeadTag().setKeywords(list);
    }

    /**
     * Returns the list of search engine keywords from the page layout or the body template. If the
     * html is being set manual, then null is returned.
     *
     * @return String
     */
    public String getKeywords()
    {
        if (_layoutEntity != null) return _layoutEntity.getLayout().getKeywords();
        else if (_bodyLayout != null) return _bodyLayout.getHeadTag().getKeywords();
        else return null;
    }

    /**
     * Sets the search engine description in the page layout or body template depending on what
     * they have done. Calling this method with manual html using no templates will cause nothing
     * to happen.
     *
     * @param String The description.
     */
    public void setDescription(String desc)
    {
        if (_layoutEntity != null) _layoutEntity.getLayout().setDescription(desc);
        else if (_bodyLayout != null) _bodyLayout.getHeadTag().setDescription(desc);
    }

    /**
     * Returns the search engine description from the page layout or the body template. If the
     * html is being set manual, then null is returned.
     *
     * @return String
     */
    public String getDescription()
    {
        if (_layoutEntity != null) return _layoutEntity.getLayout().getDescription();
        else if (_bodyLayout != null) return _bodyLayout.getHeadTag().getDescription();
        else return null;
    }

    /**
     * Sets the body background color in the page layout or body template depending on what
     * they have done. Calling this method with manual html using no templates will cause nothing
     * to happen.
     *
     * @param String The color.
     */
    public void setBgColor(String col)
    {
        if (_layoutEntity != null)
        {
            _layoutEntity.getLayout().setBgColor(col);
        }
        else if (_bodyLayout != null)
        {
            if ( "".equals(col) ) col = null;
            _bodyLayout.getBodyTag().setBgColor(col);
        }
    }

    /**
     * Returns the bg color from the page layout or the body template. If the
     * html is being set manual, then null is returned.
     *
     * @return String
     */
    public String getBgColor()
    {
        if (_layoutEntity != null) return _layoutEntity.getLayout().getBgColor();
        else if (_bodyLayout != null) return _bodyLayout.getBodyTag().getBgColor();
        else return null;
    }

    /**
     * Sets the body background image in the page layout or body template depending on what
     * they have done. Calling this method with manual html using no templates will cause nothing
     * to happen.
     *
     * @param String The image.
     */
    public void setBgImage(String img)
    {
        if (_layoutEntity != null)
        {
            _layoutEntity.getLayout().setBgImage(img);
        }
        else if (_bodyLayout != null)
        {
            if ( "".equals(img) ) img = null;
            _bodyLayout.getBodyTag().setBackground(img);
        }
    }

    /**
     * Returns the bg image from the page layout or the body template. If the
     * html is being set manual, then null is returned.
     *
     * @return String
     */
    public String getBgImage()
    {
        if (_layoutEntity != null) return _layoutEntity.getLayout().getBgImage();
        else if (_bodyLayout != null) return _bodyLayout.getBodyTag().getBackground();
        else return null;
    }

    /**
     * Sets the body text color in the page layout or body template depending on what
     * they have done. Calling this method with manual html using no templates will cause nothing
     * to happen.
     *
     * @param String The text color.
     */
    public void setTextColor(String col)
    {
        if (_layoutEntity != null) _layoutEntity.getLayout().setTextColor(col);
        else if (_bodyLayout != null) _bodyLayout.getHeadTag().setTextColor(col);
    }

    /**
     * Returns the text color from the page layout or the body template. If the
     * html is being set manual, then null is returned.
     *
     * @return String
     */
    public String getTextColor()
    {
        if (_layoutEntity != null) return _layoutEntity.getLayout().getTextColor();
        else if (_bodyLayout != null) return _bodyLayout.getHeadTag().getTextColor();
        else return null;
    }

    /**
     * Sets the body text font style in the page layout or body template depending on what
     * they have done. Calling this method with manual html using no templates will cause nothing
     * to happen.
     *
     * @param String The font style.
     */
    public void setFontStyle(String s)
    {
        if (_layoutEntity != null) _layoutEntity.getLayout().setFontStyle(s);
        else if (_bodyLayout != null) _bodyLayout.getHeadTag().setFontStyle(s);
    }

    /**
     * Returns the font style from the page layout or the body template. If the
     * html is being set manual, then null is returned.
     *
     * @return String
     */
    public String getFontStyle()
    {
        if (_layoutEntity != null) return _layoutEntity.getLayout().getFontStyle();
        else if (_bodyLayout != null) return _bodyLayout.getHeadTag().getFontStyle();
        else return null;
    }

    /**
     * Sets the body text font size in the page layout or body template depending on what
     * they have done. Calling this method with manual html using no templates will cause nothing
     * to happen.
     *
     * @param String The size.
     */
    public void setFontSize(String size)
    {
        if (_layoutEntity != null) _layoutEntity.getLayout().setFontSize(size);
        else if (_bodyLayout != null) _bodyLayout.getHeadTag().setFontSize(size);
    }

    /**
     * Returns the body text size from the page layout or the body template. If the
     * html is being set manual, then null is returned.
     *
     * @return String
     */
    public String getFontSize()
    {
        if (_layoutEntity != null) return _layoutEntity.getLayout().getFontSize();
        else if (_bodyLayout != null) return _bodyLayout.getHeadTag().getFontSize();
        else return null;
    }
}