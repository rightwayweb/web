package com.zitego.web.layout.template;

import com.zitego.web.layout.*;
import com.zitego.markup.html.tag.Body;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.Td;
import com.zitego.sql.DBHandle;

/**
 * This class represents a page with a header section, a body content area,
 * and a right menu.
 *
 * @author John Glorioso
 * @version $Id: HeaderRightMenu.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class HeaderRightMenu extends Header
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        HeaderRightMenu t = new HeaderRightMenu(page.getBodyTag(), null);
        /*com.zitego.web.layout.body.TopCenter tc = (com.zitego.web.layout.body.TopCenter)t.createBodyLayout
        (
            com.zitego.web.layout.body.PageBodyLayoutType.TOP_CENTER
        );*/

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new HeaderRightMenu layout with a Body tag parent. This is added as the
     * first content in the body.
     *
     * @param Body The parent.
     * @param DBHandle The datbase handle to use for nested elements.
     */
    protected HeaderRightMenu(Body parent, DBHandle db)
    {
        super(parent, db);
        createMenuSection();
        ( (Td)getHeader().getParent() ).setColspan(2);
    }

    public TemplateType getType()
    {
        return TemplateType.HEADER_RIGHT_MENU;
    }

    protected void createMenuSection()
    {
        //Create the menu section
        _menuSection = getBodyPanel(PanelOrientation.EAST);
        HtmlMarkupTag c = (HtmlMarkupTag)_menuSection.getParent();
        c.moveBodyContentToBefore( _menuSection, c.addComment("Begin Menu Section.") );
        c.moveBodyContentToAfter( _menuSection, c.addComment("End Menu Section.") );
    }
}