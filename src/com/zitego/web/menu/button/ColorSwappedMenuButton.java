package com.zitego.web.menu.button;

import com.zitego.format.FormatType;
import com.zitego.format.UnsupportedFormatException;
import com.zitego.markup.Newline;
import com.zitego.markup.html.HtmlMarkupMap;
import com.zitego.markup.html.tag.Body;
import com.zitego.markup.html.tag.Head;
import com.zitego.web.menu.Menu;
import org.w3c.dom.Element;

/**
 * This class represents a menu button that is an image or text that has swapping background
 * colors with on mouse over and/or on mouse down.
 *
 * @author John Glorioso
 * @version $Id: ColorSwappedMenuButton.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see com.zitego.menu.Menu#createButton(MenuButtonType)
 */
public class ColorSwappedMenuButton extends MenuButton
{
    /** The color to be shown on mouse over. */
    private String _mouseOverColor;
    /** The mouse down color. */
    private String _mouseDownColor;

    /**
     * Creates a new menu button where the colors are swapped when the mouse is over
     * it or down.
     *
     * @param parent The parent menu.
     */
    ColorSwappedMenuButton(Menu parent)
    {
        super(parent);
        setType(MenuButtonType.COLOR_SWAPPED);
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

        //Set the colors if button is a ColorSwappedMenuButton
        if (button instanceof ColorSwappedMenuButton)
        {
            ColorSwappedMenuButton c = (ColorSwappedMenuButton)button;
            setBgColor( c.getBgColor() );
            setMouseOverColor( c.getMouseOverColor() );
            setMouseDownColor( c.getMouseDownColor() );
        }
    }

    /**
     * Sets the background color.
     *
     * @param col The color.
     */
    public void setBgColor(String col)
    {
        super.setBgColor(col);
        updateOnMouseOut();
    }

    /**
     * Sets the onMouseOver color.
     *
     * @param col The on mouse over color.
     */
    public void setMouseOverColor(String col)
    {
        _mouseOverColor = col;
        updateOnMouseOver();
    }

    /**
     * Returns the onMouseOver color.
     *
     * @return String
     */
    public String getMouseOverColor()
    {
        return _mouseOverColor;
    }

    /**
     * Sets the onMouseDown color.
     *
     * @param col The on mouse down color.
     */
    public void setMouseDownColor(String col)
    {
        _mouseDownColor = col;
        updateOnMouseDown();
    }

    /**
     * Returns the onMouseDown color.
     *
     * @return String
     */
    public String getMouseDownColor()
    {
        return _mouseDownColor;
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
     * Overrides setOnMouseOver so that it does not get set.
     *
     * @param ignored Not used.
     */
    public void setOnMouseOver(String ignored) { }

    /**
     * Overrides setOnMouseDown so that it does not get set.
     *
     * @param ifnored Not used.
     */
    public void setOnMouseDown(String ignored) { }

    /**
     * Overrides setOnMouseOut so that it does not get set.
     *
     * @param ignored Not used.
     */
    public void setOnMouseOut(String ignored) { }

    /**
     * Updates the onMouseOver function.
     */
    private void updateOnMouseOver()
    {
        StringBuffer js = new StringBuffer()
            .append("swapColor('").append( getIdAttribute() ).append("', '").append(_mouseOverColor).append("')");
        super.setOnMouseOver( js.toString() );
        //Register the source file
        registerJsSourceFile("dhtml.js");
    }

    /**
     * Updates the onMouseDown function.
     */
    private void updateOnMouseDown()
    {
        StringBuffer js = new StringBuffer()
            .append("swapColor('").append( getIdAttribute() ).append("', '").append(_mouseDownColor).append("')");
        super.setOnMouseDown( js.toString() );
        //Register the source file
        registerJsSourceFile("dhtml.js");
    }

    /**
     * Updates the onMouseOut function.
     */
    private void updateOnMouseOut()
    {
        StringBuffer js = new StringBuffer()
            .append("swapColor('").append( getIdAttribute() ).append("', '").append( getBgColor() ).append("')");
        super.setOnMouseOut( js.toString() );
        //Register the source file
        registerJsSourceFile("dhtml.js");
    }

    public void registerHeaderTags()
    {
        super.registerHeaderTags();
        Head h = ( (HtmlMarkupMap)getMap() ).getHeadTag();
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
        if (h != null) h.registerJsSourceFile( getRootJsDirectory()+src);
    }

    public void addChild(Element child)
    {
        String name = child.getNodeName().toLowerCase();
        if ( name.equals("color_swapped") )
        {
            String val = child.getAttribute("mouseover_color");
            if ( !"".equals(val) ) setMouseOverColor(val);
            val = child.getAttribute("mousedown_color");
            if ( !"".equals(val) ) setMouseDownColor(val);
        }
        else
        {
            super.addChild(child);
        }
    }

    public String generateXmlContent() throws UnsupportedFormatException
    {
        StringBuffer ret = new StringBuffer( super.generateXmlContent() );

        //Color swapped element
        ret.append( getPadding(FormatType.XML) ).append(" <color_swapped");
        if (_mouseOverColor != null) ret.append(" mouseover_color=\"").append(_mouseOverColor).append("\"");
        if (_mouseDownColor != null) ret.append(" mousedown_color=\"").append(_mouseDownColor).append("\"");
        ret.append(" />").append(Newline.CHARACTER);
        return ret.toString();
    }
}