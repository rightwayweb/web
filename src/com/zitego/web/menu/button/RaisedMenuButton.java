package com.zitego.web.menu.button;

import com.zitego.format.FormatType;
import com.zitego.format.UnsupportedFormatException;
import com.zitego.markup.Newline;
import com.zitego.markup.tag.TagAttribute;
import com.zitego.markup.html.tag.Head;
import com.zitego.markup.html.HtmlMarkupMap;
import com.zitego.web.menu.Menu;
import org.w3c.dom.Element;

/**
 * This class represents a menu button that appears raised when the mouse is over it,
 * pressed when the mouse is down, and normal when not observed.
 *
 * @author John Glorioso
 * @version $Id: RaisedMenuButton.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see com.zitego.menu.Menu#createButton(MenuButtonType)
 */
public class RaisedMenuButton extends MenuButton
{
    /** The border width surrounding the button. Default is 1 pixel. */
    protected TagAttribute _borderWidth = new TagAttribute("border-width", "1");
    /** The border style surrounding the button. Default is solid. */
    protected TagAttribute _borderStyle = new TagAttribute("border-style", "solid");
    /** The border color of the surrounding border. This needs to match
        the background color of the button. Default is #FFFFFF. */
    protected TagAttribute _borderColor = new TagAttribute("border-color", "#FFFFFF");
    /** The highlighted border color. Default is #EFEFEF. */
    protected String _highlightedBorderColor = "#EFEFEF";
    /** The shaded border color. Default is #000000. */
    protected String _shadedBorderColor = "#000000";

    /**
     * Creates a new raised menu button with a Menu parent.
     *
     * @param parent The parent menu.
     */
    RaisedMenuButton(Menu parent)
    {
        super(parent);
        setType(MenuButtonType.RAISED);
        updateStyle();
        setIdAttribute( "menu" + parent.getMapId() + "_button" + parent.getButtons().size() );
    }

    public void loadWith(MenuButton button)
    {
        super.loadWith(button);
        Menu parent = getMenu();
        setIdAttribute( "menu" + parent.getMapId() + "_button"+ parent.getButtons().indexOf(this) );

        //Remove any swapped image calls
        ButtonLink l = getLink();
        if (l != null)
        {
            l.removeJsEventHandler("onMouseOver");
            l.removeJsEventHandler("onMouseOut");
            l.removeJsEventHandler("onMouseDown");
        }
    }

    public void setPropertiesWith(MenuButton button)
    {
        String id = getIdAttribute();
        super.setPropertiesWith(button);
        setIdAttribute(id);

        //Set the borders if button is a RaisedMenuButton
        if (button instanceof RaisedMenuButton)
        {
            RaisedMenuButton r = (RaisedMenuButton)button;
            setBorderWidth( r.getBorderWidth() );
            setBorderColor( r.getBorderColor() );
            setBorderStyle( r.getBorderStyle() );
            setHighlightedBorderColor( r.getHighlightedBorderColor() );
            setShadedBorderColor( r.getShadedBorderColor() );
        }
    }

    /**
     * Sets the border width in pixels.
     *
     * @param width The number of pixels wide.
     */
    public void setBorderWidth(int width)
    {
        _borderWidth.setValue( String.valueOf(width) );
        updateStyle();
    }

    /**
     * Returns the border width in pixels.
     *
     * @return int
     */
    public int getBorderWidth()
    {
        return Integer.parseInt( _borderWidth.getValue() );
    }

    /**
     * Sets the border style.
     *
     * @param style The border style.
     */
    public void setBorderStyle(String style)
    {
        _borderStyle.setValue(style);
        updateStyle();
    }

    /**
     * Returns the border style.
     *
     * @return String
     */
    public String getBorderStyle()
    {
        return _borderStyle.getValue();
    }

    /**
     * Sets the background border color.
     *
     * @param color The border color.
     */
    public void setBorderColor(String color)
    {
        _borderColor.setValue(color);
        updateStyle();
        updateOnMouseOut();
    }

    /**
     * Returns the background border color.
     *
     * @return String
     */
    public String getBorderColor()
    {
        return _borderColor.getValue();
    }

    /**
     * Sets the highlighted border color.
     *
     * @param color The border color.
     */
    public void setHighlightedBorderColor(String color)
    {
        _highlightedBorderColor = color;
        updateOnMouseOver();
        updateOnMouseDown();
    }

    /**
     * Returns the highlighted border color.
     *
     * @return String
     */
    public String getHighlightedBorderColor()
    {
        return _highlightedBorderColor;
    }

    /**
     * Sets the shaded border color.
     *
     * @param color The border color.
     */
    public void setShadedBorderColor(String color)
    {
        _shadedBorderColor = color;
        updateOnMouseOver();
        updateOnMouseDown();
    }

    /**
     * Returns the shaded border color.
     *
     * @return String
     */
    public String getShadedBorderColor()
    {
        return _shadedBorderColor;
    }

    /**
     * Overrides setIdAttribute so that the onMouseOut, onMouseOver, and onMouseDown functions are updated.
     *
     * @param id The id.
     */
    public void setIdAttribute(String id)
    {
        super.setIdAttribute(id);
        updateOnMouseOver();
        updateOnMouseDown();
        updateOnMouseOut();
    }

    /**
     * Overrides setStyle so that it does not get set.
     *
     * @param ignored Not used.
     * @param ignored2 Not used.
     */
    public void setStyle(String ignored, String ignored2) { }

    /**
     * Overrides setOnMouseOver so that it does not get set.
     *
     * @param ignored Not used.
     */
    public void setOnMouseOver(String ignored) { }

    /**
     * Overrides setOnMouseDown so that it does not get set.
     *
     * @param ignored Not used.
     */
    public void setOnMouseDown(String ignored) { }

    /**
     * Overrides setOnMouseOut so that it does not get set.
     *
     * @param ignored Not used.
     */
    public void setOnMouseOut(String ignored) { }

    /**
     * Sets the style attribute of the td tag.
     */
    private void updateStyle()
    {
        super.setStyle( "border-width", _borderWidth.getValue()+"px" );
        super.setStyle( "border-style", _borderStyle.getValue() );
        super.setStyle( "border-color", _borderColor.getValue() );
    }

    /**
     * Updates the onMouseOver function.
     */
    private void updateOnMouseOver()
    {
        StringBuffer js = new StringBuffer()
            .append("showRaisedButton('").append( getIdAttribute() ).append("', '").append(_shadedBorderColor).append("', '")
            .append(_highlightedBorderColor).append("')");
        super.setOnMouseOver( js.toString() );
        //Register the source file
        registerJsSourceFile("menu.js");
        registerJsSourceFile("dhtml.js");
    }

    /**
     * Updates the onMouseDown function.
     */
    private void updateOnMouseDown()
    {
        StringBuffer js = new StringBuffer()
            .append("showPressedButton('").append( getIdAttribute() ).append("', '").append(_shadedBorderColor).append("', '")
            .append(_highlightedBorderColor).append("')");
        super.setOnMouseDown( js.toString() );
        //Register the source file
        registerJsSourceFile("menu.js");
        registerJsSourceFile("dhtml.js");
    }

    /**
     * Updates the onMouseOut function.
     */
    private void updateOnMouseOut()
    {
        StringBuffer js = new StringBuffer()
            .append("clearButton('").append( getIdAttribute() ).append("', '").append( _borderColor.getValue() ).append("')");
        super.setOnMouseOut( js.toString() );
        //Register the source file
        registerJsSourceFile("menu.js");
        registerJsSourceFile("dhtml.js");
    }

    public void registerHeaderTags()
    {
        super.registerHeaderTags();
        updateOnMouseOut();
        updateOnMouseOver();
        updateOnMouseDown();
    }

    /**
     * Registers the javascript source file with the head tag. If the head tag does not exist,
     * then nothing happens. This method relies on the root javascript directory being set. If it
     * was not manually set, then the default is /js/<src> in which case, you will need to make
     * sure that file is present at that path of the server on which this page will be shown.
     *
     * @param src The javascript source file to register.
     */
    private void registerJsSourceFile(String src)
    {
        Head h = ( (HtmlMarkupMap)getMap() ).getHeadTag();
        if (h != null) h.registerJsSourceFile(getRootJsDirectory()+src);
    }

    public void addChild(Element child)
    {
        String name = child.getNodeName().toLowerCase();
        if ( name.equals("raised") )
        {
            String val = child.getAttribute("border-width");
            setBorderWidth( Integer.parseInt(val) );
            val = child.getAttribute("border-style");
            setBorderStyle(val);
            val = child.getAttribute("border-color");
            setBorderColor(val);
        }
        else
        {
            super.addChild(child);
        }
    }

    /**
     * Extends to generate the style content.
     *
     * @return String
     * @throws UnsupportedFormatException but not really...
     */
    public String generateXmlContent() throws UnsupportedFormatException
    {
        StringBuffer ret = new StringBuffer( super.generateXmlContent() );

        //Raised element
        ret.append( getPadding(FormatType.XML) ).append(" <raised ")
           .append(_borderWidth).append(" ").append(_borderStyle).append(" ").append(_borderColor).append(" />")
           .append(Newline.CHARACTER);
        return ret.toString();
    }
}