package com.zitego.web.layout.template;

import com.zitego.web.layout.*;
import com.zitego.web.layout.section.PageSectionType;
import com.zitego.markup.html.tag.Body;
import com.zitego.markup.html.tag.table.*;
import com.zitego.sql.DBHandle;

/**
 * This class represents a page with a header, a menu below it, and a body content area.
 *
 * @author John Glorioso
 * @version $Id: MenuUnderHeader.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class MenuUnderHeader extends Header
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        MenuUnderHeader t = new MenuUnderHeader(page.getBodyTag(), null);
        com.zitego.web.layout.body.TopCenter tc = (com.zitego.web.layout.body.TopCenter)t.createBodyLayout
        (
            com.zitego.web.layout.body.PageBodyLayoutType.TOP_CENTER
        );

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new MenuUnderHeader layout with a Body tag parent. This is added as the
     * first content in the body.
     *
     * @param Body The parent.
     * @param DBHandle The datbase handle to use for nested elements.
     */
    protected MenuUnderHeader(Body parent, DBHandle db)
    {
        super(parent, db);
        createMenuSection();
    }

    public TemplateType getType()
    {
        return TemplateType.MENU_UNDER_HEADER;
    }

    protected void createMenuSection()
    {
        //Create the menu area
        _menuSection = addSectionNextRow(PageSectionType.GENERIC);
        Td c = (Td)_menuSection.getParent();
        c.setWidth("100%");
        c.moveBodyContentToBefore( _menuSection, c.addComment("Begin Menu Section.") );
        c.moveBodyContentToAfter( _menuSection, c.addComment("End Menu Section.") );
        //The rows are currently, header, body, menu so move the body to after the menu
        moveRowTo( 1, (Tr)_menuSection.getParent().getParent() );
    }
}