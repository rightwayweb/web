package com.zitego.web.layout.section;

import com.zitego.web.layout.PageSection;
import com.zitego.markup.MarkupContent;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.block.*;

/**
 * This class represents section of text. The section is simply a holder for paragraphs. This
 * class is a table, but is not added to the parent. Instead, it adds content to the parent.
 *
 * @author John Glorioso
 * @version $Id: TextSection.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class TextSection extends PageSection
{
    /** The index of the first comment. */
    private int _firstCommentIndex = 0;
    /** The index to which content is added before. This is the index of the last comment. */
    private int _contentIndex = 0;

    /**
     * Creates a new text page section with an HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected TextSection(HtmlMarkupTag parent)
    {
        super(parent);
        setName("text_section");
        setBeginComment();
        _firstCommentIndex = parent.indexOfInBody( getBeginCommentTag() );
        setEndComment();
        _contentIndex = parent.indexOfInBody( getEndCommentTag() );
    }

    /**
     * Adds a paragraph of text to the section and returns it.
     *
     * @param String The paragraph.
     * @return Paragraph
     */
    public Paragraph addParagraph(String text)
    {
        HtmlMarkupTag parent = (HtmlMarkupTag)getParent();
        Paragraph p = (Paragraph)BlockFormatFactory.getBlockFormat(BlockFormatType.PARAGRAPH, parent);
        p.addText(text);
        return (Paragraph)parent.moveBodyContentTo(_contentIndex++, p);
    }

    /**
     * Adds the array of markup content to this text section.
     *
     * @param MarkupContent[] The content.
     */
    public void addContent(MarkupContent[] content)
    {
        if (content != null)
        {
            HtmlMarkupTag parent = (HtmlMarkupTag)getParent();
            for (int i=0; i<content.length; i++)
            {
                content[i].setParent(parent);
                parent.moveBodyContentTo(_contentIndex++, content[i]);
            }
        }
    }

    /**
     * Returns the body content for this text section.
     *
     * @return MarkupContent[]
     */
    public MarkupContent[] getContent()
    {
        MarkupContent parent = getParent();
        int size = parent.getBodySize();
        MarkupContent[] ret = new MarkupContent[_contentIndex-_firstCommentIndex-1];
        int counter = 0;
        for (int i=_firstCommentIndex+1; i<_contentIndex; i++)
        {
            ret[counter++] = parent.getBodyContent(i);
        }
        return ret;
    }

    /**
     * Removes all content from this text section (except for the comments). This
     * method must be used in order to keep the index at which new content is added
     * valid.
     */
    public void clearContent()
    {
        MarkupContent[] content = getContent();
        MarkupContent parent = getParent();
        for (int i=0; i<content.length; i++)
        {
            parent.removeBodyContent(content[i]);
            _contentIndex--;
        }
    }

    protected String getBeginComment()
    {
        return "Begin Text Section";
    }

    protected String getEndComment()
    {
        return "End Text Section";
    }
}