package com.zitego.web.layout.body;

import com.zitego.web.layout.*;
import com.zitego.web.layout.section.*;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.*;

/**
 * This class represents a page body layout.
 *
 * @author John Glorioso
 * @version $Id: TopCenter.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class TopCenter extends PageBodyLayout
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        TopCenter t = new TopCenter( page.getBodyTag() );
        t.addToParent();

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new top center body layout with a HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected TopCenter(HtmlMarkupTag parent)
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
        cell.setAlign("center");
        row = createRow();
        cell = row.createCell();
        cell.setAlign("left");
        setMainSection(PageSectionType.IMAGE);
        setAlternateSection(PageSectionType.TEXT);
    }

    protected Td getMainSectionCell()
    {
        return getFirstRow().getFirstCell();
    }

    protected Td getAlternateSectionCell()
    {
        return getLastRow().getFirstCell();
    }

    public PageBodyLayoutType getType()
    {
        return PageBodyLayoutType.TOP_CENTER;
    }

    protected String getBeginComment()
    {
        return "Begin Top Center Section";
    }

    protected String getEndComment()
    {
        return "End Top Center Section";
    }
}