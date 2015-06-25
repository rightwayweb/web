package com.zitego.web.layout.template;

import com.zitego.web.layout.*;
import com.zitego.markup.html.tag.Body;
import com.zitego.markup.html.tag.table.Td;
import com.zitego.sql.DBHandle;

/**
 * This class represents a page with a header, a body content area,
 * a right menu, and a footer.
 *
 * @author John Glorioso
 * @version $Id: HeaderRightMenuFooter.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class HeaderRightMenuFooter extends HeaderRightMenu
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        HeaderRightMenuFooter t = new HeaderRightMenuFooter(page.getBodyTag(), null);
        /*com.zitego.web.layout.body.TopCenter tc = (com.zitego.web.layout.body.TopCenter)t.createBodyLayout
        (
            com.zitego.web.layout.body.PageBodyLayoutType.TOP_CENTER
        );*/

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new HeaderRightMenuFooter layout with a Body tag parent. This is added as the
     * first content in the body.
     *
     * @param Body The parent.
     * @param DBHandle The datbase handle to use for nested elements.
     */
    protected HeaderRightMenuFooter(Body parent, DBHandle db)
    {
        super(parent, db);
        //Create the footer
        ( (Td)getFooter().getParent() ).setColspan(2);
        ( (Td)getHeader().getParent() ).setColspan(2);
    }

    public TemplateType getType()
    {
        return TemplateType.HEADER_RIGHT_MENU_FOOTER;
    }
}