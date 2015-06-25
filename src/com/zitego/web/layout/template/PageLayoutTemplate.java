package com.zitego.web.layout.template;

import com.zitego.web.layout.*;
import com.zitego.web.layout.body.*;
import com.zitego.markup.IllegalMarkupException;
import com.zitego.markup.Newline;
import com.zitego.markup.html.tag.*;
import com.zitego.markup.html.tag.table.*;
import com.zitego.web.menu.MenuEntity;
import com.zitego.sql.DBHandle;
import com.zitego.sql.NoDataException;
import com.zitego.format.*;
import java.sql.SQLException;
import org.w3c.dom.*;

/**
 * This is a base class for all page layout templates. A design has a name, a description,
 * a type, and an icon.
 *
 * @author John Glorioso
 * @version $Id: PageLayoutTemplate.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class PageLayoutTemplate extends PageSection
{
    /** The page header. */
    protected PageHeader _header;
    /** The body. */
    protected PageBodyLayout _body;
    /** The page footer. */
    protected PageFooter _footer;
    /** The east body panel. */
    protected BodyPanel _eastPanel;
    /** The west body panel. */
    protected BodyPanel _westPanel;
    /** The menu section. */
    protected PageSection _menuSection;
    /** The menu id to use. */
    protected long _menuId = -1;
    /** The template position. FULL_WIDTH_NO_PADDING by default. */
    protected TemplatePosition _position = TemplatePosition.FULL_WIDTH_NO_PADDING;
    /** This is used for initializing the menu entity. */
    protected DBHandle _db;
    /** The cell to hold body content. */
    private Td _bodyCell;
    /** The dtd domain url in the format of http://<somewhere>/ */
    private String _dtdUrl;

    /**
     * Creates a new PageLayoutTemplate.
     *
     * @param parent The parent.
     * @param db The database handle to use for loading sub entities from the database.
     */
    public PageLayoutTemplate(Body parent, DBHandle db)
    {
        super(parent);
        _db = db;
        addToParent();
    }

    /**
     * Creates the body layout given the type passed in. If the type is null then it returns.
     *
     * @param type The type.
     */
    public PageBodyLayout createBodyLayout(PageBodyLayoutType type)
    {
        setBodyLayout( PageBodyLayoutFactory.createLayout(type, getBodyLayoutCell()) );
        return _body;
    }

    /**
     * Sets the body layout in this page layout. If there is one already, then it is replaced with
     * the new one. The new body layout's parent will be set to the appropriate cell. If the new body is
     * null then the body layout will be set to one of type HOLDER.
     *
     * @param body The new body layout.
     */
    public void setBodyLayout(PageBodyLayout body)
    {
        int index = 0;
        boolean createBody = (_body == null);
        //Get the body layout cell
        Td parent = getBodyLayoutCell();
        //Remove the current body if there is one
        if (!createBody) index = _body.removeFromParent();
        //If the new body is null, the create a holder body
        if (body == null) body = PageBodyLayoutFactory.createLayout(PageBodyLayoutType.HOLDER, parent);
        else body.setParent(parent);
        _body = body;
        //Add it where it was
        _body.addToParentAt(index);
    }

    /**
     * Creates and/or returns the the body layout cell that we will add the body layout to.
     *
     * @return Td
     */
    protected Td getBodyLayoutCell()
    {
        if (_bodyCell == null) _bodyCell = createRow().createCell();
        return _bodyCell;
    }

    /**
     * Returns the body layout.
     *
     * @return PageBodyLayout
     */
    public PageBodyLayout getBodyLayout()
    {
        return _body;
    }


    /**
     * Returns the body panel at the specified orientation. If there is not a body panel at the
     * specified orientation, then one is added. If there is not a body layout yet, then an illegal state
     * exception will be thrown.
     *
     * @return BodyPanel
     * @throws IllegalStateException if the body layout is null.
     */
    public BodyPanel getBodyPanel(PanelOrientation orientation) throws IllegalStateException
    {
        if (orientation == PanelOrientation.EAST)
        {
            if (_eastPanel == null)
            {
                //Make sure we have a body
                if (_body == null) throw new IllegalStateException("Page body has not yet been created");
                Tr row = (Tr)_body.getParent().getParent();
                Td cell = row.createCell();
                _eastPanel = new BodyPanel(cell);
                _eastPanel.addToParent();
            }
            return _eastPanel;
        }
        else if (orientation == PanelOrientation.WEST)
        {
            if (_westPanel == null)
            {
                //Make sure we have a body
                if (_body == null) throw new IllegalStateException("Page body has not yet been created");
                Tr row = (Tr)_body.getParent().getParent();
                Td cell = row.createCellAt(0);
                _westPanel = new BodyPanel(cell);
                _westPanel.addToParent();
            }
            return _westPanel;
        }
        else
        {
            throw new IllegalArgumentException("PanelOrientation "+orientation+" is invalid.");
        }
    }

    /**
     * Returns whether the specific body panel has been defined.
     *
     * @param orientation The orientation of the panel to the body.
     * @return boolean
     */
    public boolean hasBodyPanel(PanelOrientation orientation)
    {
        if (orientation == PanelOrientation.EAST) return (_eastPanel != null);
        else if (orientation == PanelOrientation.WEST) return (_westPanel != null);
        else return false;
    }

    /**
     * Creates the menu section. This needs to be defined in extending classes.
     */
    protected abstract void createMenuSection();

    /**
     * Returns the menu section. This is the western body panel.
     *
     * @return PageSection
     */
    public PageSection getMenuSection()
    {
        return _menuSection;
    }

    /**
     * Sets the menu id for the section.
     *
     * @param id The id.
     */
    public void setMenuId(long id)
    {
        _menuId = id;
    }

    /**
     * Returns the menu id.
     *
     * @return long
     */
    public long getMenuId()
    {
        return _menuId;
    }

    /**
     * Sets the position of the template within the body given the type. If the
     * type is null, then it will be set to FULL_WIDTH_NO_PADDING.
     *
     * @param pos The position.
     */
    public void setPosition(TemplatePosition pos)
    {
        _position = pos;
        if (_position == null) _position = TemplatePosition.FULL_WIDTH_NO_PADDING;

        if (_position == TemplatePosition.FULL_WIDTH_NO_PADDING)
        {
            //100% wide no margins
            getHeadTag().getStyleTag().createStyleDeclaration("body").setProperty("margin", "0px");
            setCellPadding("0");
            setCellSpacing("0");
            setWidth("100%");
            //Make sure the sub sections have no alignment (left)
            if ( getType().isHeaderType() ) getHeader().setAlign(null);
            getBodyLayout().setAlign(null);
            if ( getType().isFooterType() ) getFooter().setAlign(null);
        }
        else if (_position == TemplatePosition.FULL_WIDTH_PADDING)
        {
            //100% wide
            getHeadTag().getStyleTag().createStyleDeclaration("body").removeProperty("margin");
            setCellPadding(null);
            setCellSpacing(null);
            setWidth("100%");
            //Make sure the sub sections have no alignment (left)
            if ( getType().isHeaderType() ) getHeader().setAlign(null);
            getBodyLayout().setAlign(null);
            if ( getType().isFooterType() ) getFooter().setAlign(null);
        }
        else if (_position == TemplatePosition.CENTERED_NO_PADDING)
        {
            //Centered no padding
            getHeadTag().getStyleTag().createStyleDeclaration("body").setProperty("margin", "0px");
            setWidth(null);
            setCellPadding("0");
            setCellSpacing("0");
            setAlign("center");
            //Make sure the sub sections are centered
            if ( getType().isHeaderType() ) getHeader().setAlign("center");
            getBodyLayout().setAlign("center");
            if ( getType().isFooterType() ) getFooter().setAlign("center");
        }
        else if (_position == TemplatePosition.CENTERED_PADDING)
        {
            //Centered
            getHeadTag().getStyleTag().createStyleDeclaration("body").removeProperty("margin");
            setWidth(null);
            setCellPadding(null);
            setCellSpacing(null);
            setAlign("center");
            //Make sure the sub sections are centered
            if ( getType().isHeaderType() ) getHeader().setAlign("center");
            getBodyLayout().setAlign("center");
            if ( getType().isFooterType() ) getFooter().setAlign("center");
        }
        else if (_position == TemplatePosition.LEFT_NO_PADDING)
        {
            //Left justified no padding
            getHeadTag().getStyleTag().createStyleDeclaration("body").setProperty("margin", "0px");
            setWidth(null);
            setCellPadding("0");
            setCellSpacing("0");
            setAlign("left");
            //Make sure the sub sections are aligned to the left
            if ( getType().isHeaderType() ) getHeader().setAlign("left");
            getBodyLayout().setAlign("left");
            if ( getType().isFooterType() ) getFooter().setAlign("left");
        }
        else if (_position == TemplatePosition.LEFT_PADDING)
        {
            //Left justified
            getHeadTag().getStyleTag().createStyleDeclaration("body").removeProperty("margin");
            setWidth(null);
            setCellPadding(null);
            setCellSpacing(null);
            setAlign("left");
            //Make sure the sub sections are aligned to the left
            if ( getType().isHeaderType() ) getHeader().setAlign("left");
            getBodyLayout().setAlign("left");
            if ( getType().isFooterType() ) getFooter().setAlign("left");
        }
    }

    /**
     * Returns the template position.
     *
     * @return TemplatePosition
     */
    public TemplatePosition getPosition()
    {
        return _position;
    }

    /**
     * Sets the template window title.
     *
     * @param title The title.
     */
    public void setTitle(String title)
    {
        getHeadTag().setTitle(title);
    }

    /**
     * Returns the layout title.
     *
     * @return String
     */
    public String getTitle()
    {
        return getHeadTag().getTitle();
    }

    /**
     * Sets the list of search engine keywords.
     *
     * @param list The list.
     */
    public void setKeywords(String list)
    {
        getHeadTag().setKeywords(list);
    }

    /**
     * Returns the list of search engine keywords.
     *
     * @return String
     */
    public String getKeywords()
    {
        return getHeadTag().getKeywords();
    }

    /**
     * Sets the search engine description.
     *
     * @param desc The description.
     */
    public void setDescription(String desc)
    {
        getHeadTag().setDescription(desc);
    }

    /**
     * Returns the search engine description.
     *
     * @return String
     */
    public String getDescription()
    {
        return getHeadTag().getDescription();
    }

    /**
     * Sets the body background color.'
     *
     * @param col The color.
     */
    public void setBgColor(String col)
    {
        if ( col != null && !"".equals(col) ) getBodyTag().setBgColor(col);
        else getBodyTag().setBgColor(null);
    }

    /**
     * Returns the bg color.
     *
     * @return String
     */
    public String getBgColor()
    {
        return getBodyTag().getBgColor();
    }

    /**
     * Sets the background image.
     *
     * @param img The image.
     */
    public void setBgImage(String img)
    {
        if ( img != null && !"".equals(img) ) getBodyTag().setBackground(img);
        else getBodyTag().setBackground(null);
    }

    /**
     * Returns the background image.
     *
     * @return String
     */
    public String getBgImage()
    {
        return getBodyTag().getBackground();
    }

    /**
     * Sets the body text color.
     *
     * @param col The text color.
     */
    public void setTextColor(String col)
    {
        getHeadTag().setTextColor(col);
    }

    /**
     * Returns the text color.
     *
     * @return String
     */
    public String getTextColor()
    {
        return getHeadTag().getTextColor();
    }

    /**
     * Sets the body text font style.
     *
     * @param s The font style.
     */
    public void setFontStyle(String s)
    {
        getHeadTag().setFontStyle(s);
    }

    /**
     * Returns the font style.
     *
     * @return String
     */
    public String getFontStyle()
    {
        return getHeadTag().getFontStyle();
    }

    /**
     * Sets the body text font size.
     *
     * @param size The size.
     */
    public void setFontSize(String size)
    {
        getHeadTag().setFontSize(size);
    }

    /**
     * Returns the body text size.
     *
     * @return String
     */
    public String getFontSize()
    {
        return getHeadTag().getFontSize();
    }

    /**
     * Copies the properties of the given header into this layout's header.
     *
     * @param header The header to copy.
     */
    public void setHeader(PageHeader header)
    {
        if ( header == null || !getType().isHeaderType() ) return;

        try
        {
            setHeader( header.formatAsRoot(FormatType.HTML) );
            if (header.getPageSectionBorder() != null) getHeader().setPageSectionBorder( header.getPageSectionBorder() );
        }
        catch (Exception e) { }
    }

    /**
     * Creates a header based on the given header html String.
     *
     * @param html The html.
     * @throws UnsupportedFormatException
     * @throws IllegalMarkupException
     */
    public void setHeader(String html) throws UnsupportedFormatException, IllegalMarkupException
    {
        if ( html == null || !getType().isHeaderType() ) return;

        getHeader().parse(new StringBuffer(html), FormatType.HTML);
    }

    /**
     * Returns the page header. If one does not exist yet, it is created.
     *
     * @return PageHeader
     */
    public PageHeader getHeader()
    {
        if (_header == null)
        {
            Tr row = null;
            //See if there is a body there. If so, move it to row 1 by setting the header at row 0
            if (_body != null) row = createRowAt(0);
            else row = createRow();
            Td cell = row.createCell();
            _header = new PageHeader(cell);
            _header.addToParent();
        }
        return _header;
    }

    /**
     * Copies the properties of the given footer into this layout's footer.
     *
     * @param footer The footer to copy.
     */
    public void setFooter(PageFooter footer)
    {
        if (footer == null) return;

        try
        {
            setFooter( footer.formatAsRoot(FormatType.HTML) );
            if (footer.getPageSectionBorder() != null) getFooter().setPageSectionBorder( footer.getPageSectionBorder() );
        }
        catch (Exception e) { }
    }

    /**
     * Creates a footer based on the given footer html String.
     *
     * @param html The html.
     * @throws UnsupportedFormatException
     * @throws IllegalMarkupException
     */
    public void setFooter(String html) throws UnsupportedFormatException, IllegalMarkupException
    {
        if ( html == null || !getType().isHeaderType() ) return;

        getFooter().parse(new StringBuffer(html), FormatType.HTML);
    }

    /**
     * Returns the page footer. If one does not exist yet, it is created. If there is no page body
     * then an IllegalStateException will be thrown. A page body is required to add a footer.
     *
     * @return PageFooter
     * @throws IllegalStateException if the page body has not been created.
     */
    public PageFooter getFooter()
    {
        if (_footer == null)
        {
            //Make sure that we have a body
            if (_body == null) throw new IllegalStateException("Page body has not yet been created");
            Tr row = createRow();
            Td cell = row.createCell();
            _footer = new PageFooter(cell);
            _footer.addToParent();
        }
        return _footer;
    }

    /**
     * Sets the dtd url domain.
     *
     * @param domain The dtd url.
     */
    public void setDtdUrl(String url)
    {
        _dtdUrl = url;
    }

    /**
     * Returns the dtd url.
     *
     * @return String
     */
    public String getDtdUrl()
    {
        return _dtdUrl;
    }

    /**
     * Loads the menu section from the database based on the menu id. If the id is -1, then
     * nothing is loaded.
     *
     * @throws SQLException if a database error occurs.
     * @throws NoDataException if the menu does not exist.
     */
    public void loadMenu() throws SQLException, NoDataException
    {
        if (_menuId == -1) return;
        MenuEntity e = new MenuEntity(_menuId, _db);
        e.init();
        _menuId = e.getId();
        getMenuSection().clearRows();
        getMenuSection().addContent( e.getMenu() );
    }

    /**
     * Returns the type of template this is.
     *
     * @return TemplateType
     */
    public abstract TemplateType getType();

    protected String getBeginComment()
    {
        return "Begin " + getType();
    }

    protected String getEndComment()
    {
        return "End " + getType();
    }

    public void buildFromXml(Element root)
    {
        DocumentType dtdUrl = root.getOwnerDocument().getDoctype();
        if (dtdUrl != null) setDtdUrl( dtdUrl.getSystemId() );
        
        setPosition( TemplatePosition.evaluate(Integer.parseInt(root.getAttribute("position"))) );
        String val = root.getAttribute("bgcolor");
        if ( val != null && !"".equals(val) ) setBgColor(val);
        val = root.getAttribute("background");
        if ( val != null && !"".equals(val) ) setBgImage(val);

        NodeList nodes = root.getChildNodes();
        int size = nodes.getLength();
        for (int i=0; i<size; i++)
        {
            Node n = nodes.item(i);
            if (n instanceof Element)
            {
                addChild( (Element)n );
            }
        }
    }

    public void addChild(Element child)
    {
        if ( child.getNodeName().equalsIgnoreCase("title") )
        {
            setTitle( child.getChildNodes().item(0).getNodeValue() );
        }
        else if ( child.getNodeName().equalsIgnoreCase("keywords") )
        {
            setKeywords( child.getChildNodes().item(0).getNodeValue() );
        }
        else if ( child.getNodeName().equalsIgnoreCase("description") )
        {
            setDescription( child.getChildNodes().item(0).getNodeValue() );
        }
        else if ( child.getNodeName().equalsIgnoreCase("text") )
        {
            setTextColor( child.getAttribute("color") );
            NodeList nodes = child.getChildNodes();
            //Keep going till we get to an element node
            int size = nodes.getLength();
            for (int i=0; i<size; i++)
            {
                Node n = nodes.item(i);
                if (n instanceof Element)
                {
                    Element font = (Element)n;
                    setFontStyle( font.getAttribute("style") );
                    setFontSize( font.getAttribute("size") );
                    break;
                }
            }
        }
        else if ( child.getNodeName().equalsIgnoreCase("header") )
        {
            getHeader().buildFromXml(child);
        }
        else if ( child.getNodeName().equalsIgnoreCase("panel") )
        {
            getBodyPanel( PanelOrientation.evaluate(Integer.parseInt(child.getAttribute("orientation"))) ).buildFromXml(child);
        }
        else if ( child.getNodeName().equalsIgnoreCase("footer") )
        {
            getFooter().buildFromXml(child);
        }
        else if ( child.getNodeName().equalsIgnoreCase("menu") )
        {
            _menuId = Long.parseLong( child.getAttribute("id") );
            try
            {
                loadMenu();
            }
            catch (Exception e)
            {
                throw new RuntimeException("An error has occurred loading the menu.", e);
            }
        }
    }

    public boolean countDeepness(FormatType type)
    {
        return (type == FormatType.XML);
    }

    /**
     * Just a comment to say that setDtdUrl must be called before generating xml content.
     * If not, an IllegalStateException will be thrown.
     *
     * @throws UnsupportedFormatException if the format is not supported.
     * @throws IllegalStateException if the dtd url domain has not been set.
     */
    protected String generateContent(FormatType type) throws UnsupportedFormatException, IllegalStateException
    {
        if (type != FormatType.XML) return super.generateContent(type);

        if ( hasChanged() )
        {
            if (_dtdUrl == null) throw new IllegalStateException("dtd url domain must be set before generating xml content.");
            String domain = _dtdUrl;
            int index = domain.indexOf("//");
            if (index == -1) throw new IllegalStateException("dtd url: "+_dtdUrl+" is not valid. Format must be http://<domain>/<path to dtd>");
            domain = domain.substring(index+2);
            index = domain.indexOf("/");
            if (index == -1) throw new IllegalStateException("dtd url: "+_dtdUrl+" is not valid. Format must be http://<domain>/<path to dtd>");
            domain = domain.substring(0, index);
            StringBuffer ret = new StringBuffer();

            ret.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>").append(Newline.CHARACTER)
               .append(Newline.CHARACTER)
               .append("<!DOCTYPE layout").append(Newline.CHARACTER)
               .append("     PUBLIC \"-//").append(domain).append(" //DTD Page Layout 2.2//EN\"").append(Newline.CHARACTER)
               .append("    \"").append(_dtdUrl).append("\">").append(Newline.CHARACTER)
               .append(Newline.CHARACTER);

            ret.append("<layout type=\"").append( getType().getValue() ).append("\" ")
               .append("position=\"").append( _position.getValue() ).append("\"");
            String val = getBgColor();
            if (val != null) ret.append(" bgcolor=\"").append(val).append("\"");
            val = getBgImage();
            if (val != null) ret.append(" background=\"").append(val).append("\"");
            ret.append(">").append(Newline.CHARACTER);

            val = getTitle();
            if (val != null) ret.append(" <title>").append(val).append("</title>").append(Newline.CHARACTER);
            val = getKeywords();
            if (val != null) ret.append(" <keywords>").append(val).append("</keywords>").append(Newline.CHARACTER);
            val = getDescription();
            if (val != null) ret.append(" <description>").append(val).append("</description>").append(Newline.CHARACTER);
            val = getTextColor();
            String val2 = getFontStyle();
            String val3 = getFontSize();
            if (val != null || val2 != null || val3 != null)
            {
                ret.append(" <text");
                if (val != null) ret.append(" color=\"").append(val).append("\"");
                if (val2 != null || val3 != null)
                {
                    ret.append(">").append(Newline.CHARACTER)
                       .append("  <font");
                    if (val2 != null) ret.append(" style=\"").append(val2).append("\"");
                    if (val3 != null) ret.append(" size=\"").append(val3).append("\"");
                    ret.append(" />").append(Newline.CHARACTER)
                       .append(" </text");
                }
                ret.append(">").append(Newline.CHARACTER);
            }
            if ( getType().isHeaderType() ) ret.append( getHeader().format(type) ).append(Newline.CHARACTER);
            if ( hasBodyPanel(PanelOrientation.EAST) ) ret.append( getBodyPanel(PanelOrientation.EAST).format(type) ).append(Newline.CHARACTER);
            if ( hasBodyPanel(PanelOrientation.WEST) ) ret.append( getBodyPanel(PanelOrientation.WEST).format(type) ).append(Newline.CHARACTER);
            if ( getType().isFooterType() ) ret.append( getFooter().format(type) ).append(Newline.CHARACTER);
            if ( getType().isMenuType() && _menuId > -1 ) ret.append(" <menu id=\"").append(_menuId).append("\" />").append(Newline.CHARACTER);

            ret.append("</layout>");
            return ret.toString();
        }
        else
        {
            return (String)getCachedContent(type);
        }
    }
}