package com.zitego.web.menu;

import com.zitego.format.FormatType;
import com.zitego.format.UnsupportedFormatException;
import com.zitego.web.menu.button.*;
import com.zitego.markup.*;
import com.zitego.markup.xml.*;
import com.zitego.markup.html.HtmlMarkupMap;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.*;
import java.util.Vector;
import java.io.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class represents a menu. A menu has a MenuOrientation of either
 * HORIZONTAL or VERTICAL and a Vector of MenuButtons. The menu is an
 * extension of an html table. If the menu is horizontal, then the table
 * will have only one row and multiple td elements (buttons). If the
 * menu is vertical, then the table will have multiple single td (button)
 * rows. The menu can be formatted to either html or xml.
 *
 * The default cellpadding on a menu table is 3 and the default cellspacing
 * is 3.
 *
 * @author John Glorioso
 * @version $Id: Menu.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see Table
 */
public class Menu extends Table implements XmlConverter
{
    /** The button orientation. */
    ButtonOrientation _orientation;
    /** The buttons. */
    private Vector _buttons = new Vector();
    /** The dtd url. */
    private String _dtdUrl;

    public static void main(String[] args) throws Exception
    {
        if (args.length == 2)
        {
            FormatType formatType = FormatType.evaluate( Integer.parseInt(args[1]) );
            BufferedReader in = new BufferedReader( new FileReader(args[0]) );
            StringBuffer xml = new StringBuffer();
            String line = null;
            while ( (line = in.readLine()) != null )
            {
                xml.append(line).append("\r\n");
            }
            System.out.println("XML IN:");
            System.out.println(xml);
            Menu menu = Menu.buildMenu( xml.toString() );
            MarkupContent c = menu;
            if (formatType == FormatType.HTML)
            {
                com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
                menu.setParent( page.getBodyTag() );
                c = page;
            }
            System.out.println(formatType.getDescription()+" OUT:");
            System.out.println( c.format(formatType) );
            System.out.println( c.format(formatType) );
        }
        else
        {
            FormatType formatType = FormatType.evaluate( Integer.parseInt(args[0]) );
            MarkupContent c = null;
            Menu menu = null;
            if (formatType == FormatType.HTML)
            {
                com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
                menu = new Menu(page.getBodyTag(), ButtonOrientation.HORIZONTAL);
                c = page;
            }
            else
            {
                menu = new Menu(ButtonOrientation.HORIZONTAL);
                c = menu;
            }
            MenuButton button = menu.createButton(MenuButtonType.IMAGE_SWAPPED);
            ((ImageSwappedMenuButton)button).setMouseOverImageUrl("testy.jpg");
            ButtonLink link = button.createButtonLink("blooy");
            ImageButtonDisplay disp = button.createImageDisplay("bla_bla.gif");

            Menu subMenu = new Menu(button, ButtonOrientation.VERTICAL);
            button.setSubMenu(subMenu);
            MenuButton button2 = subMenu.createButton(MenuButtonType.IMAGE_SWAPPED);
            ((ImageSwappedMenuButton)button2).setMouseOverImageUrl("testy2.gif");
            ButtonLink link2 = button2.createButtonLink("blooy2");
            ImageButtonDisplay disp2 = button2.createImageDisplay("bla_bla2.gif");
            System.out.println(c.format(formatType));
        }
    }

    /**
     * Creates a new menu.
     */
    private Menu()
    {
        //Create with an empty body tag
        super( new com.zitego.markup.html.tag.Html().getBodyTag() );
        setCellPadding("3");
        setCellSpacing("0");
    }

    /**
     * Creates a new menu with a ButtonOrientation.
     *
     * @param orientation Either HORIZONTAL or VERTICAL.
     */
    public Menu(ButtonOrientation orientation)
    {
        this();
        _orientation = orientation;
    }

    /**
     * Creates a new menu with an HtmlMarkupTag parent and an orientation. The menu is automatically
     * added to the parent.
     *
     * @param parent The parent tag.
     * @param orientation The orientation.
     */
    public Menu(HtmlMarkupTag parent, ButtonOrientation orientation)
    {
        super(parent);
        _orientation = orientation;
        setCellPadding("3");
        setCellSpacing("0");
        setMap( new HtmlMarkupMap() );
    }

    /**
     * This overrides the parent so that it can notify buttons to register any javascript or styles
     * if the parent is not MenuButton tag.
     *
     * @param content The parent.
     */
    public void setParent(MarkupContent content)
    {
        super.setParent(content);
        if (content != null && !(content instanceof MenuButton) && _buttons != null)
        {
            int size = _buttons.size();
            for (int i=0; i<size; i++)
            {
                ( (MenuButton)_buttons.get(i) ).registerHeaderTags();
            }
        }
    }

    /**
     * Sets the orientation. This removes all rows from the underlying table, and re-adds each button.
     *
     * @param orientation The new orientation.
     * @throws IllegalArgumentException if the orientation is null.
     */
    public void setOrientation(ButtonOrientation orientation)
    {
        if (_orientation == orientation) return;
        _orientation = orientation;
        setAttribute( "orientation", String.valueOf(orientation.getValue()) );

        clearRows();
        //Re-add the buttons
        int size = _buttons.size();
        for (int i=0; i<size; i++)
        {
            getRowToAddTo().addCell( (MenuButton)_buttons.get(i) );
        }
    }

    /**
     * Returns the button orientation.
     *
     * @return ButtonOrientation
     */
    public ButtonOrientation getOrientation()
    {
        return _orientation;
    }

    /**
     * Creates a new MenuButton, adds it to the menu, and returns it.
     *
     * @param type The type of button to create.
     * @return MenuButton
     */
    public MenuButton createButton(MenuButtonType type)
    {
        Tr row = getRowToAddTo();
        MenuButton button = MenuButtonFactory.createButton(type, this);
        row.addCell(button);
        _buttons.add(button);
        return button;
    }

    /**
     * Replaces the given specified button with the given button. If the specified button
     * does not exist, then the given button is added to the menu.
     *
     * @param buttonToReplace The button to replace.
     * @param newButton The button to replace with.
     * @throws IllegalArgumentException if either button is null.
     */
    public void replaceButton(MenuButton buttonToReplace, MenuButton newButton)
    {
        if (buttonToReplace == null || newButton == null) throw new IllegalArgumentException("Buttons cannot be null.");

        int cellIndex = -1;
        int buttonIndex = _buttons.indexOf(buttonToReplace);

        Tr parent = getRowToAddTo();
        if (buttonIndex > -1)
        {
            parent = (Tr)buttonToReplace.getParent();
            _buttons.remove(buttonToReplace);
            cellIndex = parent.removeBodyContent(buttonToReplace);
        }

        if (cellIndex > -1) parent.addCell(cellIndex, newButton);
        else parent.addCell(newButton);
        if (buttonIndex > -1) _buttons.add(buttonIndex, newButton);
        else _buttons.add(newButton);
    }

    /**
     * Removes the given button from this menu and body content.
     *
     * @param button The button to remove.
     */
    public void removeButton(MenuButton button)
    {
        if ( button == null || !_buttons.contains(button) ) return;

        Tr parent = (Tr)button.getParent();
        parent.removeCell(button);
        _buttons.remove(button);
    }

    /**
     * Returns the row to add a button to. If there are no rows yet, then one is created.
     *
     * @return Tr
     */
    public Tr getRowToAddTo()
    {
        Tr ret = null;
        if (_orientation == ButtonOrientation.VERTICAL)
        {
            ret = createRow();
            ret.setIsOnOwnLine(true);
        }
        else
        {
            ret = getFirstRow();
            if (ret == null) ret = createRow();
        }
        return ret;
    }

    /**
     * This creates a menu from the given xml. It validates against a dtd to make sure that the
     * xml is well formed.
     *
     * @param xmlContent The xml document.
     * @return Menu
     * @throws SaxException when an error occurs in the xml content.
     * @throws IOException when an error occurs reading the content.
     * @throws ParserConfigurationException when an error occurs with the parser.
     */
    public static Menu buildMenu(String xmlContent) throws SAXException, IOException, ParserConfigurationException
    {
        Document doc = XmlUtils.parseXml(xmlContent, true);

        Menu menu = new Menu();
        menu.buildFromXml( doc.getDocumentElement() );

        return menu;
    }

    /*public void parseText(StringBuffer in, FormatType type) throws UnsupportedFormat, IllegalMarkupType
    {
        if (type == FormatType.XML)
        {
            buildFromXml( getXmlElement(in.toString()) );
        }
        else
        {
            super.parseText(in, type);
        }
    }*/

    /**
     * Sets the dtd url.
     *
     * @param url The dtd url.
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

    public void buildFromXml(Element root)
    {
        DocumentType dtdUrl = root.getOwnerDocument().getDoctype();
        if (dtdUrl != null) setDtdUrl( dtdUrl.getSystemId() );

        clearRows();
        _buttons.clear();

        String value = root.getAttribute("orientation");
        setOrientation( ButtonOrientation.evaluate(Integer.parseInt(value)) );
        value = root.getAttribute("cellpadding");
        if (!"".equals(value) && value != null) setCellPadding(value);
        value = root.getAttribute("cellspacing");
        if (!"".equals(value) && value != null) setCellSpacing(value);
        value = root.getAttribute("border");
        if (!"".equals(value) && value != null) setBorder( Integer.parseInt(value) );
        value = root.getAttribute("bgcolor");
        if (!"".equals(value) && value != null) setBgColor(value);

        //Add the buttons
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
        if ( child.getNodeName().equalsIgnoreCase("button") )
        {
            Tr row = getRowToAddTo();
            MenuButtonType type = MenuButtonType.evaluate( Integer.parseInt(child.getAttribute("type")) );
            MenuButton button = MenuButtonFactory.createButton(type, this);
            row.addCell(button);
            button.buildFromXml(child);
            _buttons.add(button);
        }
    }

    public void parse(Object objToParse, FormatType type) throws IllegalMarkupException, UnsupportedFormatException
    {
        if (objToParse instanceof Element && type == FormatType.XML) buildFromXml( (Element)objToParse );
        else super.parse(objToParse, type);
    }

    /**
     * Returns the first button in the Vector. If there are no buttons yet, it returns null.
     *
     * @return MenuButton
     */
    public MenuButton getFirstButton()
    {
        if (_buttons.size() > 0) return getButton(0);
        else return null;
    }

    /**
     * Returns the last button in the Vector. If there are no buttons yet, it returns null.
     *
     * @return MenuButton
     */
    public MenuButton getLastButton()
    {
        if (_buttons.size() > 0) return getButton(_buttons.size()-1);
        else return null;
    }

    /**
     * Returns the button at the specified index.
     *
     * @param index The button index.
     * @return MenuButton
     * @throws IndexOutOfBoundsException if the index is invalid.
     */
    public MenuButton getButton(int index) throws IndexOutOfBoundsException
    {
        return (MenuButton)_buttons.get(index);
    }

    /**
     * Returns all of the buttons in this Menu.
     *
     * @return Vector
     */
    public Vector getButtons()
    {
        return _buttons;
    }

    public boolean countDeepness(FormatType type)
    {
        return (type == FormatType.XML && getParent() instanceof MenuButton);
    }

    /**
     * Just a comment to say that setDtdUrl must be called before generating xml content.
     * If not, an IllegalStateException will be thrown.
     *
     * @throws UnsupportedFormatException if the format is not supported.
     * @throws IllegalStateException if the dtd url domain has not been set.
     */
    public String generateContent(FormatType type) throws UnsupportedFormatException, IllegalStateException
    {
        if (type != FormatType.XML) return super.generateContent(type);

        if ( hasChanged() )
        {
            if (_dtdUrl == null) throw new IllegalStateException("dtd url must be set before generating xml content.");
            String domain = _dtdUrl;
            int index = domain.indexOf("//");
            if (index == -1) throw new IllegalStateException("dtd url: "+_dtdUrl+" is not valid. Format must be http://<domain>/<path to dtd>");
            domain = domain.substring(index+2);
            index = domain.indexOf("/");
            if (index == -1) throw new IllegalStateException("dtd url: "+_dtdUrl+" is not valid. Format must be http://<domain>/<path to dtd>");
            domain = domain.substring(0, index);
            StringBuffer ret = new StringBuffer();

            String padding = getPadding(type);
            if ( !(getParent() instanceof MenuButton) )
            {
                ret.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>").append(Newline.CHARACTER)
                   .append(Newline.CHARACTER)
                   .append("<!DOCTYPE menu").append(Newline.CHARACTER)
                   .append("     PUBLIC \"-//").append(domain).append(" //DTD Page Layout 2.2//EN\"").append(Newline.CHARACTER)
                   .append("     \"").append(_dtdUrl).append("\">").append(Newline.CHARACTER)
                   .append(Newline.CHARACTER);
            }
            ret.append(padding).append("<menu orientation=\"").append( _orientation.getValue() ).append("\"");
            if (getCellPadding() != null) ret.append(" ").append( getAttribute("cellpadding") );
            if (getCellSpacing() != null) ret.append(" ").append( getAttribute("cellspacing") );
            if (getAttributeValue("border") != null) ret.append(" ").append( getAttribute("border") );
            if (getAttributeValue("bgcolor") != null) ret.append(" ").append( getAttribute("bgcolor") );
            ret.append(">").append(Newline.CHARACTER);

            int size = _buttons.size();
            for (int i=0; i<size; i++)
            {
                ret.append( ((MenuButton)_buttons.get(i)).format(type) ).append(Newline.CHARACTER);
            }
            ret.append(padding).append("</menu>");
            return ret.toString();
        }
        else
        {
            return (String)getCachedContent(type);
        }
    }
}