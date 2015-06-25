package com.zitego.web.layout.template;

import com.zitego.web.layout.*;
import com.zitego.web.layout.body.PageBodyLayoutType;
import com.zitego.web.layout.section.PageSectionType;
import com.zitego.markup.html.tag.Body;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.Td;
import com.zitego.sql.DBHandle;

/**
 * This class represents a page with a menu up top, a header, and a body content area.
 *
 * @author John Glorioso
 * @version $Id: TopMenuHeader.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class TopMenuHeader extends PageLayoutTemplate
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        TopMenuHeader t = new TopMenuHeader(page.getBodyTag(), null);
        com.zitego.web.layout.body.TopCenter tc = (com.zitego.web.layout.body.TopCenter)t.createBodyLayout
        (
            com.zitego.web.layout.body.PageBodyLayoutType.TOP_CENTER
        );

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new TopMenuHeader layout with a Body tag parent. This is added as the
     * first content in the body.
     *
     * @param Body The parent.
     * @param DBHandle The datbase handle to use for nested elements.
     */
    protected TopMenuHeader(Body parent, DBHandle db)
    {
        super(parent, db);
        //Create the menu section
        createMenuSection();
        //Create the header
        getHeader();
        //Create a body holder
        createBodyLayout(PageBodyLayoutType.HOLDER);
    }

    public TemplateType getType()
    {
        return TemplateType.TOP_MENU_HEADER;
    }

    protected void createMenuSection()
    {
        //Create the menu area
        _menuSection = addSectionNextRow(PageSectionType.GENERIC);
        Td c = (Td)_menuSection.getParent();
        c.setWidth("100%");
        c.moveBodyContentToBefore( _menuSection, c.addComment("Begin Menu Section.") );
        c.moveBodyContentToAfter( _menuSection, c.addComment("End Menu Section.") );
    }
}