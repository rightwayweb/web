package com.zitego.web.layout.template;

import com.zitego.web.layout.*;
import com.zitego.web.layout.body.PageBodyLayoutType;
import com.zitego.markup.html.tag.Body;
import com.zitego.markup.html.tag.table.Td;
import com.zitego.sql.DBHandle;

/**
 * This class represents a page with a header and a body content area.
 *
 * @author John Glorioso
 * @version $Id: Header.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class Header extends PageLayoutTemplate
{
    /**
     * Creates a new Header layout with a Body tag parent. This is added as the
     * first content in the body.
     *
     * @param Body The parent.
     * @param DBHandle The datbase handle to use for nested elements.
     */
    protected Header(Body parent, DBHandle db)
    {
        super(parent, db);
        //Create the header
        getHeader();
        createBodyLayout(PageBodyLayoutType.HOLDER);
    }

    public TemplateType getType()
    {
        return TemplateType.HEADER;
    }

    protected void createMenuSection() { }
}