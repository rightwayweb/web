package com.zitego.web.layout.template;

import com.zitego.markup.html.tag.Body;
import com.zitego.markup.html.tag.Html;
import com.zitego.markup.xml.XmlUtils;
import com.zitego.sql.DBHandle;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;
import java.io.*;

/**
 * This class creates page layout templates depending on the type passed in.
 *
 * @author John Glorioso
 * @version $Id: TemplateFactory.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see TemplateType
 */
public class TemplateFactory
{
    /**
     * Returns a PageLayoutTemplate given the parent content, the type, and a
     * DBHandle for loading nested entities from the database.
     *
     * @param TemplateType The type.
     * @param Body The body tag parent.
     * @param DBHandle The database handle to use.
     * @return PageLayoutTemplate
     * @throws IllegalArgumentException if the type is not valid.
     */
    public static PageLayoutTemplate createTemplate(TemplateType type, Body parent, DBHandle db)
    throws IllegalArgumentException
    {
        if (type == TemplateType.HEADER) return new Header(parent, db);
        else if (type == TemplateType.HEADER_LEFT_MENU) return new HeaderLeftMenu(parent, db);
        else if (type == TemplateType.MENU_UNDER_HEADER) return new MenuUnderHeader(parent, db);
        else if (type == TemplateType.TOP_MENU_HEADER) return new TopMenuHeader(parent, db);
        else if (type == TemplateType.HEADER_FOOTER) return new HeaderFooter(parent, db);
        else if (type == TemplateType.HEADER_LEFT_MENU_FOOTER) return new HeaderLeftMenuFooter(parent, db);
        else if (type == TemplateType.MENU_UNDER_HEADER_FOOTER) return new MenuUnderHeaderFooter(parent, db);
        else if (type == TemplateType.TOP_MENU_HEADER_FOOTER) return new TopMenuHeaderFooter(parent, db);
        else if (type == TemplateType.HEADER_RIGHT_MENU) return new HeaderRightMenu(parent, db);
        else if (type == TemplateType.HEADER_RIGHT_MENU_FOOTER) return new HeaderRightMenuFooter(parent, db);
        else if (type == TemplateType.CUSTOM) return new Custom(parent, db);
        else throw new IllegalArgumentException("Invalid page body layout type: "+type);
    }

    /**
     * Creates a PageLayoutTemplate given the xml to build it with. No parent will be created for this
     * layout. Calling classes should catch SAXException to handle poorly formed xml. A database handle
     * is also required for loading nested content that is stored in the database such as menus.
     *
     * @param String the xml content.
     * @param DBHandle The database handle to use.
     * @return PageLayoutTemplate
     * @throws SAXException
     */
    public static PageLayoutTemplate buildTemplate(String xml, DBHandle db) throws SAXException
    {
        Document doc = null;
        try
        {
            doc = XmlUtils.parseXml(xml, true);
        }
        catch (ParserConfigurationException pce)
        {
            //Should never happen
            throw new SAXException(pce);
        }
        catch (IOException io) { /* Not gonna happen with a string data source. */ }
        Element elem = doc.getDocumentElement();

        PageLayoutTemplate ret = createTemplate
        (
            TemplateType.evaluate(Integer.parseInt(elem.getAttribute("type"))), new Html().getBodyTag(), db
        );
        ret.buildFromXml(elem);

        return ret;
    }
}