package com.zitego.web.layout.body;

import com.zitego.web.layout.*;
import com.zitego.web.layout.section.*;
import com.zitego.markup.html.tag.table.*;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import java.util.Vector;

/**
 * This class represents a page body layout where an image or some section is in the
 * top left or right corner based on what row we are in. It starts with a left section
 * and then creates a right section, alternating back and forth. The alternate sections
 * can be anything, but text is recommended.
 *
 * @author John Glorioso
 * @version $Id: ZigZagLeft.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class ZigZagLeft extends PageBodyLayout
{
    /** The vector of zig zag sections. */
    private Vector _sections = new Vector();

    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        ZigZagLeft t = new ZigZagLeft( page.getBodyTag() );
        t.addToParent();

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /**
     * Creates a new zig zag left layout with an HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected ZigZagLeft(HtmlMarkupTag parent)
    {
        super(parent);
    }

    /**
     * Returns the next section. If the last section was a top left corner, then next will be top
     * right. This starts with a top left.
     * @return PageSection
     */
    public PageSection getNextSection()
    {
        if (_sections.size() > 0) createEmptyRow();
        Tr row = createRow();
        Td cell = row.createCell();
        PageSection section = PageBodyLayoutFactory.createLayout(getNextType(), cell);
        cell.addBodyContent(section);
        _sections.add(section);
        return section;
    }

    /**
     * Returns which type of section to return next based on the nextSection call and what section
     * we had last.
     *
     * @return PageBodyLayoutType
     */
    protected PageBodyLayoutType getNextType()
    {
        return (_sections.size() % 2 == 0 ? PageBodyLayoutType.TOP_LEFT_CORNER : PageBodyLayoutType.TOP_RIGHT_CORNER);
    }

    /**
     * Returns the sections.
     *
     * @return Vector
     */
    public Vector getSections()
    {
        return _sections;
    }

    protected Td getMainSectionCell()
    {
        return null;
    }

    protected Td getAlternateSectionCell()
    {
        return null;
    }

    /**
     * Not used. Call getNextSection.
     *
     * @param PageSectionType Not used.
     * @return PageSection
     * @throws IllegalArgumentException
     */
    public PageSection setMainSection(PageSectionType type) throws IllegalArgumentException
    {
        throw new IllegalArgumentException("Cannot call setMainSection in this class.");
    }

    /**
     * Not used. Call getNextSection.
     *
     * @param PageSectionType Not used.
     * @return PageSection
     * @throws IllegalArgumentException
     */
    public PageSection setAlternateSection(PageSectionType type) throws IllegalArgumentException
    {
        throw new IllegalArgumentException("Cannot call setAlternateSection in this class.");
    }

    protected String getBeginComment()
    {
        return "Begin Zig Zag Left Section";
    }

    protected String getEndComment()
    {
        return "End Zig Zag Left Section";
    }

    public PageBodyLayoutType getType()
    {
        return PageBodyLayoutType.ZIG_ZAG_LEFT;
    }
}