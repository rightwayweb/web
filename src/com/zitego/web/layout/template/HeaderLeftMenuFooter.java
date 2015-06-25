package com.zitego.web.layout.template;

import com.zitego.web.layout.*;
import com.zitego.markup.html.tag.Body;
import com.zitego.sql.DBHandle;

/**
 * This class represents a page with a header, a left menu, a body content
 * area, and a footer.
 *
 * @author John Glorioso
 * @version $Id: HeaderLeftMenuFooter.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class HeaderLeftMenuFooter extends HeaderLeftMenu
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        HeaderLeftMenuFooter t = new HeaderLeftMenuFooter(page.getBodyTag(), null);
        com.zitego.web.layout.body.TextLayout tc = (com.zitego.web.layout.body.TextLayout)t.createBodyLayout
        (
            com.zitego.web.layout.body.PageBodyLayoutType.TEXT
        );

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new HeaderLeftMenuFooter layout with a Body tag parent. This is added as the
     * first content in the body.
     *
     * @param Body The parent.
     * @param DBHandle The datbase handle to use for nested elements.
     */
    protected HeaderLeftMenuFooter(Body parent, DBHandle db)
    {
        super(parent, db);
        //Create the footer
        getFooter();
    }

    public TemplateType getType()
    {
        return TemplateType.HEADER_LEFT_MENU_FOOTER;
    }

}