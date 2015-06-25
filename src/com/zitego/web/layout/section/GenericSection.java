package com.zitego.web.layout.section;

import com.zitego.web.layout.PageSection;
import com.zitego.markup.MarkupContent;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.block.*;

/**
 * This is an generic page section that is to be used by page layouts.
 *
 * @author John Glorioso
 * @version $Id: GenericSection.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class GenericSection extends PageSection
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        GenericSection section = new GenericSection( page.getBodyTag() );
        com.zitego.markup.html.tag.table.Tr row = section.createRow();
    }

    /**
     * Creates a new generic page section with an HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected GenericSection(HtmlMarkupTag parent)
    {
        super(parent);
    }

    protected boolean addToParentOnInit()
    {
        return true;
    }
}