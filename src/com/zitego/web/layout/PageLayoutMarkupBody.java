package com.zitego.web.layout;

import com.zitego.markup.*;
import com.zitego.markup.html.HtmlMarkupBody;
import com.zitego.format.*;

/**
 * This overrides the html markup body class to insure that parsing is done by the page
 * layout markup factory.
 *
 * @author John Glorioso
 * @version $Id: PageLayoutMarkupBody.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class PageLayoutMarkupBody extends HtmlMarkupBody
{
    /**
     * Creates a new PageLayoutMarkupBody.
     *
     * @param MarkupContent The parent.
     */
    public PageLayoutMarkupBody(MarkupContent content)
    {
        super(content);
    }

    public void parseText(StringBuffer text, FormatType type) throws IllegalMarkupException, UnsupportedFormatException
    {
        if (text == null) return;

        PageLayoutMarkupFactory.getInstance().parse(text, getParent(), type, true);
    }
}