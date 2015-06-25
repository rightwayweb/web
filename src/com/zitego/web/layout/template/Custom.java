package com.zitego.web.layout.template;

import com.zitego.web.layout.*;
import com.zitego.web.layout.body.PageBodyLayoutType;
import com.zitego.markup.IllegalMarkupException;
import com.zitego.markup.html.tag.Body;
import com.zitego.markup.html.tag.table.*;
import com.zitego.sql.DBHandle;

/**
 * This class represents an empty customizable page.
 *
 * @author John Glorioso
 * @version $Id: Custom.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class Custom extends PageLayoutTemplate
{
    /** This is the cell that all content gets added too. */
    protected Td _mainCell;

    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        Custom t = new Custom(page.getBodyTag(), null);
        /*com.zitego.web.layout.body.TopCenter tc = (com.zitego.web.layout.body.TopCenter)t.createBodyLayout
        (
            com.zitego.web.layout.body.PageBodyLayoutType.TOP_CENTER
        );*/

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new Custom layout with a Body tag parent. This is added as the
     * first content in the body.
     *
     * @param Body The parent
     * @param DBHandle The database handle to use for nested elements.
     */
    protected Custom(Body parent, DBHandle db)
    {
        super(parent, db);
        Tr row = createRow();
        _mainCell = row.createCell();
        //Add comments around the cell holding all custom content
        row.moveBodyContentToBefore( _mainCell, row.addComment("Begin Body Content Section. This is where your pages will be inserted. DO NOT REMOVE THIS COMMENT!") );
        row.moveBodyContentToAfter( _mainCell, row.addComment("End Body Content Section. This is where your pages will be inserted. DO NOT REMOVE THIS COMMENT!") );
        createBodyLayout(PageBodyLayoutType.HOLDER);
    }

    /**
     * Creates the custom layout given html code.
     *
     * @param String The html.
     * @throws IllegalMarkupException if the template is invalid.
     */
    public Custom(String html) throws IllegalMarkupException
    {
        super(null, null);
        //TO DO - figure this out
    }

    private Td getParent(boolean newRow)
    {
        Tr row = ( newRow ? createRow() : getLastRow() );
        return row.createCell();
    }

    public TemplateType getType()
    {
        return TemplateType.CUSTOM;
    }

    protected Td getBodyLayoutCell()
    {
        return _mainCell;
    }

    protected void createMenuSection() { }
}