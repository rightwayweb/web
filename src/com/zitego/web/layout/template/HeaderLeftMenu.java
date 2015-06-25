package com.zitego.web.layout.template;

import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.web.layout.*;
import com.zitego.markup.html.tag.Body;
import com.zitego.markup.html.tag.table.Td;
import com.zitego.sql.DBHandle;

/**
 * This class represents a page with a header section, a left menu,
 * and a body content area.
 *
 * @author John Glorioso
 * @version $Id: HeaderLeftMenu.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class HeaderLeftMenu extends Header
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        HeaderLeftMenu t = new HeaderLeftMenu(page.getBodyTag(), null);
        com.zitego.web.layout.body.TopCenter tc = (com.zitego.web.layout.body.TopCenter)t.createBodyLayout
        (
            com.zitego.web.layout.body.PageBodyLayoutType.TOP_CENTER
        );
        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new HeaderLeftMenu layout with a Body tag parent. This is added as the
     * first content in the body.
     *
     * @param Body The parent.
     * @param DBHandle The datbase handle to use for nested elements.
     */
    protected HeaderLeftMenu(Body parent, DBHandle db)
    {
        super(parent, db);
        Td headerCell = (Td)getHeader().getParent();
        headerCell.setColspan(2);
        createMenuSection();
    }

    public TemplateType getType()
    {
        return TemplateType.HEADER_LEFT_MENU;
    }

    protected void createMenuSection()
    {
        //Create the menu section
        _menuSection = getBodyPanel(PanelOrientation.WEST);
        HtmlMarkupTag c = (HtmlMarkupTag)_menuSection.getParent();
        c.moveBodyContentToBefore( _menuSection, c.addComment("Begin Menu Section.") );
        c.moveBodyContentToAfter( _menuSection, c.addComment("End Menu Section.") );
    }
}