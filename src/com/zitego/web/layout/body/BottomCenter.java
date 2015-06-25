package com.zitego.web.layout.body;

import com.zitego.web.layout.*;
import com.zitego.web.layout.section.*;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.*;

/**
 * This class represents a page body layout.
 *
 * @author John Glorioso
 * @version $Id: BottomCenter.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class BottomCenter extends PageBodyLayout
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        BottomCenter t = new BottomCenter( page.getBodyTag() );
        t.addToParent();
        System.out.println("Before html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        t.setPageSectionBorder(com.zitego.web.layout.border.PageSectionBorderType.SOLID_CORNER);

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
    }

    /**
     * Creates a new bottom center body layout with a HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected BottomCenter(HtmlMarkupTag parent)
    {
        super(parent);
        if (parent instanceof Td)
        {
            ( (Td)parent ).setAlign("center");
            ( (Td)parent ).setValign("top");
        }
        else
        {
            setAlign("center");
        }
        //Setup top and bottom
        Tr row = getFirstRow();
        Td cell = row.createCell();
        cell.setAlign("left");
        row = createRow();
        cell = row.createCell();
        cell.setAlign("center");
        setMainSection(PageSectionType.IMAGE);
        setAlternateSection(PageSectionType.TEXT);
    }

    protected Td getMainSectionCell()
    {
        return getLastRow().getFirstCell();
    }

    protected Td getAlternateSectionCell()
    {
        return getFirstRow().getFirstCell();
    }

    public PageBodyLayoutType getType()
    {
        return PageBodyLayoutType.BOTTOM_CENTER;
    }

    protected String getBeginComment()
    {
        return "Begin Bottom Center Section";
    }

    protected String getEndComment()
    {
        return "End Bottom Center Section";
    }
}