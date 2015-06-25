package com.zitego.web.menu.button;

import com.zitego.format.*;
import com.zitego.web.menu.Menu;
import com.zitego.markup.*;
import com.zitego.markup.tag.*;
import com.zitego.markup.xml.XmlConverter;
import com.zitego.markup.html.tag.table.*;
import com.zitego.markup.html.tag.*;
import com.zitego.markup.html.tag.textEffect.*;
import java.util.Vector;
import org.w3c.dom.*;

/**
 * This class represents a MenuButton. The button contains a ButtonDisplay which is
 * what is shown to the viewer, a ButtonLink which is the http url that the button leads
 * to, and an optional sub-menu which is another Menu instance.
 *
 * @author John Glorioso
 * @version $Id: MenuButton.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see com.zitego.menu.Menu#createButton(MenuButtonType)
 */
public class MenuButton extends Td
{
    /** The type of button this is. */
    private MenuButtonType _type;
    /** The parent menu. */
    private Menu _parent;
    /** The ButtonDisplay to show to the viewer. */
    private ButtonDisplay _display;
    /** The http link that the button leads to. */
    private ButtonLink _link;
    /** The sub-menu that this button contains. */
    private Menu _subMenu;
    /** The color of the display foreground. This is here rather than in ButtonDisplay because it is a td attribute. */
    private String _displayColor;

    /**
     * Creates a new menu button with a Menu parent.
     *
     * @param menu The parent menu.
     */
    MenuButton(Menu parent)
    {
        super( parent.getRowToAddTo() );
        setIsOnOwnLine(true);
        _parent = parent;
        setType(MenuButtonType.NORMAL);
    }

    /**
     * Creates a new TextButtonDisplay with the specified label that is set as the menu display.
     *
     * @param label The text to use in the display.
     * @return TextButtonDisplay
     */
    public TextButtonDisplay createTextDisplay(String label)
    {
        setButtonDisplay( new TextButtonDisplay(getDisplayParent(), label) );
        return (TextButtonDisplay)_display;
    }

    /**
     * Creates a new TextButtonDisplay with the specified label that is set as the menu display.
     *
     * @param type The text effect type to wrap the text with.
     * @param label The text to use in the display.
     * @return TextButtonDisplay
     */
    public TextButtonDisplay createTextDisplay(TextEffectType type, String label)
    {
        TextEffect effect = TextEffectFactory.getTextEffect( type, getDisplayParent() );
        TextButtonDisplay disp = new TextButtonDisplay(effect, label);
        setButtonDisplay( disp );
        return (TextButtonDisplay)_display;
    }

    /**
     * Creates a new ImageButtonDisplay with the specified src url that is set as the menu display.
     *
     * @param src The src url.
     * @return ImageButtonDisplay
     */
    public ImageButtonDisplay createImageDisplay(String src)
    {
        setButtonDisplay( new ImageButtonDisplay(getDisplayParent(), src) );
        return (ImageButtonDisplay)_display;
    }

    /**
     * Sets the button display from the given display. This will clear any existing button display
     * first. Then add it to this and set the appropriate body content and parents. If the display
     * is null, then nothing is changed.
     *
     * @param display The display.
     */
    public void setButtonDisplay(ButtonDisplay display)
    {
        if (display == null || display == _display) return;
        clearButtonDisplay();
        HtmlMarkupTag parent = getDisplayParent();
        display.setParent(parent);
        _display = display;
    }

    /**
     * Removes the button display from it's parent and if it's parent is a text effect, it is removed
     * from it's parent too. This returns the HtmlMarkupTag parent of the DisplayTag.
     */
    private void clearButtonDisplay()
    {
        //Only do this if the display has been set
        if (_display != null)
        {
            _display.getParent().removeBodyContent( _display.getAsMarkupContent() );
        }
    }

    /**
     * Returns the ButtonDisplay.
     *
     * @return ButtonDisplay
     */
    public ButtonDisplay getButtonDisplay()
    {
        return _display;
    }

    /**
     * Returns the display's markup content parent. If a link is set, then it is returned, otherwise
     * it returns the button itself.
     *
     * @return HtmlMarkupTag
     */
    public HtmlMarkupTag getDisplayParent()
    {
        if (_link != null) return _link;
        else return this;
    }

    /**
     * Creates the Button link for this menu button. If one does not already exist, then it is
     * created. Once it is created and added, it is then returned.
     *
     * @param url The url.
     * @return MenuLink
     */
    public ButtonLink createButtonLink(String url)
    {
        //If the link has not already been created
        if (_link == null)
        {
            //Link has not been created, so create one
            _link = new ButtonLink(this, url);
            //Set the button display's parent as the link if it has been set already
            if (_display != null) _display.setParent(_link);
        }
        else
        {
            _link.setHref(url);
        }
        return _link;
    }

    /**
     * Removes the button link and sets the display if any to have a new parent.
     */
    public void removeButtonLink()
    {
        removeBodyContent(_link);
        _link = null;
        if (_display != null) _display.setParent(this);
    }

    /**
     * Returns the link.
     *
     * @return ButtonLink
     */
    public ButtonLink getLink()
    {
        return _link;
    }

    /**
     * Sets the type of button this is.
     *
     * @param type The button type.
     * @throws IllegalArgumentException if the type is null.
     */
    public void setType(MenuButtonType type)
    {
        if (type == null) throw new IllegalArgumentException("MenuButtonType cannot be null.");
        _type = type;
    }

    /**
     * Returns the type of button this is.
     *
     * @return MenuButtonType
     */
    public MenuButtonType getType()
    {
        return _type;
    }

    /**
     * Returns the menu this belongs to.
     *
     * @return Menu
     */
    public Menu getMenu()
    {
        return _parent;
    }

    /**
     * Sets the button display color.
     *
     * @param color The color.
     */
    public void setDisplayColor(String color)
    {
        _displayColor = color;
        //Set the link style too if there is a link
        if (_link != null) _link.setDisplayColor(color);
        //Need to set the style tag
        if (_displayColor != null) super.setStyle("color", _displayColor);
        else removeStyle("color");
    }

    /**
     * Returns the button display color.
     *
     * @return String
     */
    public String getDisplayColor()
    {
        return _displayColor;
    }

    /**
     * Sets the sub menu.
     *
     * @param submenu The sub menu.
     */
    public void setSubMenu(Menu submenu)
    {
        _subMenu = submenu;

        if (_parent.getOrientation() == ButtonOrientation.HORIZONTAL)
        {
            //Need to add a row and move this menu to start on what button index this is
            Tr row = _parent.createRow();
            Td cell = row.createCell();
        }
        else
        {
            //Need to get first row and add a cell and make the rowspan as long as the thing has buttons
            Tr row = (Tr)getParent();
            row.setIsOnOwnLine(false);
            setRowspan( submenu.getButtons().size() );
        }

        //Create a div tag to make this a dropdown menu
        /* TO DO - move this when DropDownMenu comes in
        Body body = ( (HtmlMarkupMap)getMap() ).getBodyTag();
        if (body != null)
        {
            Div div = (Div)BlockFormatFactory.getBlockFormat(BlockFormatType.DIV, body);
            div.setIdAttribute( "menu" + _parent.getMapId() + "_button" + _parent.getButtons().size() );
            submenu.setParent(div);
            body.moveBodyContentTo(0, div);
        }*/
    }

    /**
     * Returns the sub menu.
     *
     * @return Menu
     */
    public Menu getSubMenu()
    {
        return _subMenu;
    }

    /**
     * This does nothing for this class, but is meant for extending classes that use javascript or
     * styles that need to be declared in the head section to be notified to register them.
     */
    public void registerHeaderTags()
    {
        //Notify all children
        if (_subMenu != null)
        {
            Vector buttons = _subMenu.getButtons();
            int size = buttons.size();
            for (int i=0; i<size; i++)
            {
                ( (MenuButton)buttons.get(i) ).registerHeaderTags();
            }
        }
    }

    /**
     * Sets this button's properties with the given button and replaces it in the
     * MarkupMap.
     *
     * @param button The button to load from.
     */
    public void loadWith(MenuButton button)
    {
        if (button == null) return;

        //First replace the id
        replaceContentInMap(button);

        AttributeList attributes = button.getAttributes();
        int size = attributes.size();
        for (int i=0; i<size; i++)
        {
            TagAttribute attr = (TagAttribute)attributes.get(i);
            setAttribute( attr.getName(), attr.getValue() );
        }
        setDisplayColor( button.getDisplayColor() );
        clearButtonDisplay();
        removeButtonLink();
        _link = button.getLink();
        _display = button.getButtonDisplay();
        if (_link == null) _display.setParent(this);
        else _link.setParent(this);
    }

    /**
     * Sets the button's properties with the given button.
     *
     * @param button The button to load from.
     */
    public void setPropertiesWith(MenuButton button)
    {
        if (button == null) return;

        //Set the attributes and display color
        AttributeList attributes = button.getAttributes();
        int size = attributes.size();
        for (int i=0; i<size; i++)
        {
            TagAttribute attr = (TagAttribute)attributes.get(i);
            setAttribute( attr.getName(), attr.getValue() );
        }

        ButtonLink buttonLink = button.getLink();
        //If they have a link, we need to set ours to match it's style. If not, then we leave it alone.
        if (buttonLink != null && _link != null) _link.setUnderlined( buttonLink.isUnderlined() );

        //Need to change the display. If we have the same type, then we just need to set properties, otherwise
        //we need to replace. If they don't have one, then we don't do squat.
        ButtonDisplay buttonDisplay = button.getButtonDisplay();
        if (buttonDisplay != null)
        {
            //If we don't have one, then create one
            if (_display == null || _display.getType() != buttonDisplay.getType() )
            {
                setButtonDisplay( ButtonDisplayFactory.createButtonDisplay(buttonDisplay.getType(), getDisplayParent()) );
            }
            if (_display.getType() == ButtonDisplayType.IMAGE)
            {
                ImageButtonDisplay disp = (ImageButtonDisplay)_display;
                ImageButtonDisplay buttonDisp = (ImageButtonDisplay)buttonDisplay;
                disp.setBorder( buttonDisp.getBorder() );
                disp.setWidth( buttonDisp.getWidth() );
                disp.setHeight( buttonDisp.getHeight() );
                disp.setAlign( buttonDisp.getAlign() );
            }
            else
            {
                TextButtonDisplay disp = (TextButtonDisplay)_display;
                //Set the display color now that the link and display have been loaded
                setDisplayColor( button.getDisplayColor() );
                TextEffect buttonTextEffect = ( (TextButtonDisplay)buttonDisplay ).getTextEffect();
                if (buttonTextEffect != null) disp.setTextEffect( TextEffectFactory.getTextEffect(buttonTextEffect.getType(), getDisplayParent()) );
                else disp.setTextEffect(null);
                setNoWrap( button.getNoWrap() );
            }
        }
    }

    public void buildFromXml(Element root)
    {
        //Set the cell properties if there are any
        String val = root.getAttribute("bgcolor");
        if ( val != null && !"".equals(val) ) setBgColor(val);
        val = root.getAttribute("width");
        if ( val != null && !"".equals(val) ) setWidth(val);
        val = root.getAttribute("height");
        if ( val != null && !"".equals(val) ) setHeight(val);
        val = root.getAttribute("align");
        if ( val != null && !"".equals(val) ) setAlign(val);
        val = root.getAttribute("valign");
        if ( val != null && !"".equals(val) ) setValign(val);
        val = root.getAttribute("fgcolor");
        if ( val != null && !"".equals(val) ) setDisplayColor(val);
        val = root.getAttribute("nowrap");
        setNoWrap( "1".equals(val) );

        //Set the link and display
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
        String name = child.getNodeName().toLowerCase();
        if ( name.equals("link") )
        {
            createButtonLink( child.getAttribute("href") );
            _link.buildFromXml(child);
        }
        else if ( name.equals("display") )
        {
            ButtonDisplayType type = ButtonDisplayType.evaluate( Integer.parseInt(child.getAttribute("type")) );
            ButtonDisplay display = ButtonDisplayFactory.createButtonDisplay( type, getDisplayParent() );
            display.buildFromXml(child);
            setButtonDisplay(display);
        }
        else if ( name.equals("menu") )
        {
            //Set to horizontal, it will get set when built if it is a dropdown
            Menu subMenu = new Menu(this, ButtonOrientation.HORIZONTAL);
            subMenu.buildFromXml(child);
            //Need to keep this the same as the parent menu (till we do the dropdown)
            //TO DO - extend menu to DropDownMenu
            if ( subMenu.getOrientation() != _parent.getOrientation() ) subMenu.setOrientation( _parent.getOrientation() );
            setSubMenu(subMenu);
        }
    }

    public boolean countDeepness(FormatType type)
    {
        return (type == FormatType.XML);
    }

    public String generateContent(FormatType type) throws UnsupportedFormatException
    {
        if (type != FormatType.XML) return super.generateContent(type);

        if ( hasChanged() )
        {
            String padding = getPadding(type);
            StringBuffer ret = new StringBuffer()
                .append(padding).append("<button ")
                .append("type=\"").append( _type.getValue() ).append("\"");
            String val = getBgColor();
            if (val != null) ret.append(" ").append( getAttribute("bgcolor") );
            val = getWidth();
            if (val != null) ret.append(" ").append( getAttribute("width") );
            val = getHeight();
            if (val != null) ret.append(" ").append( getAttribute("height") );
            val = getAlign();
            if (val != null) ret.append(" ").append( getAttribute("align") );
            val = getValign();
            if (val != null) ret.append(" ").append( getAttribute("valign") );
            val = getStyle();
            if (val != null)
            {
                int index = val.indexOf("color:");
                if (index > -1) ret.append(" fgcolor=\"").append( val.substring(index+7, val.indexOf(";",index)) ).append("\"");
            }
            val = (getNoWrap() ? "1" : "0");
            if (val != null) ret.append(" nowrap=\"").append(val).append("\"");
            ret.append(">").append(Newline.CHARACTER)
               .append( generateXmlContent() );
            if (_subMenu != null) ret.append( _subMenu.format(type) ).append(Newline.CHARACTER);
            ret.append(padding).append("</button>");
            return ret.toString();
        }
        else
        {
            return (String)getCachedContent(type);
        }
    }

    /**
     * This generates the link and display xml content.
     *
     * @return String
     * @throws UnsupportedFormatException if we cannot format to XML.
     */
    public String generateXmlContent() throws UnsupportedFormatException
    {
        StringBuffer ret = new StringBuffer();

        //Link
        if (_link != null) ret.append( _link.format(FormatType.XML) ).append(Newline.CHARACTER);
        //Display
        if (_display != null) ret.append( _display.format(FormatType.XML) ).append(Newline.CHARACTER);

        return ret.toString();
    }
}