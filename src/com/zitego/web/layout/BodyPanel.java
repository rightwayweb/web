package com.zitego.web.layout;

import com.zitego.markup.html.tag.HtmlMarkupTag;

/**
 * This class represents a page body panel.
 *
 * @author John Glorioso
 * @version $Id: BodyPanel.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class BodyPanel extends PageSection
{
    /**
     * Creates a new body panel with a PageLayout parent.
     *
     * @param HtmlMarkupTag The parent page layout.
     */
    public BodyPanel(HtmlMarkupTag parent)
    {
        super(parent);
    }
}