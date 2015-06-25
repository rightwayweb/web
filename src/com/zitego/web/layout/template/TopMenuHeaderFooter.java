package com.zitego.web.layout.template;

import com.zitego.web.layout.*;
import com.zitego.markup.html.tag.Body;
import com.zitego.sql.DBHandle;

/**
 * This class represents a page with a menu up top, a header, a body content area,
 * and a footer.
 *
 * @author John Glorioso
 * @version $Id: TopMenuHeaderFooter.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class TopMenuHeaderFooter extends TopMenuHeader
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        TopMenuHeaderFooter t = new TopMenuHeaderFooter(page.getBodyTag(), null);
        com.zitego.web.layout.body.BottomCenter tc = (com.zitego.web.layout.body.BottomCenter)t.createBodyLayout
        (
            com.zitego.web.layout.body.PageBodyLayoutType.BOTTOM_CENTER
        );

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new TopMenuHeaderFooter layout with a Body tag parent. This is added as the
     * first content in the body.
     *
     * @param Body The parent.
     * @param DBHandle The datbase handle to use for nested elements.
     */
    protected TopMenuHeaderFooter(Body parent, DBHandle db)
    {
        super(parent, db);
        //Create the footer
        getFooter();
    }

    public TemplateType getType()
    {
        return TemplateType.TOP_MENU_HEADER_FOOTER;
    }
}