package com.zitego.web.layout.section;

import com.zitego.web.layout.PageSection;
import com.zitego.markup.MarkupContent;
import com.zitego.markup.tag.CommentTag;
import com.zitego.markup.html.HtmlTextContent;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.Img;

/**
 * This class represents an image section. This is an extension of table, but it does not
 * truely get drawn out unless an outside class adds it. This determines whether it is
 * present within its parent and adds content accordingly.
 *
 * @author John Glorioso
 * @version $Id: ImageSection.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class ImageSection extends PageSection
{
    /** The image. */
    protected Img _image;
    /** The image caption. */
    protected HtmlTextContent _caption;
    /** The index at which the image will always get added if this is not in the parent. */
    private int _imageIndex = 0;

    /**
     * Creates a new image page section with an HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected ImageSection(HtmlMarkupTag parent)
    {
        super(parent);
        setName("image_section");
        setComments();
    }

    /**
     * Sets the comments for this section. If the comments are already set, then they
     * will be added again.
     */
    public void setComments()
    {
        setBeginComment();
        MarkupContent comment = getBeginCommentTag();
        _imageIndex = comment.getParent().indexOfInBody(comment)+1;
        setEndComment();
    }

    protected String getBeginComment()
    {
        return "Begin Image Section";
    }

    protected String getEndComment()
    {
        return "End Image Section";
    }

    /**
     * Sets the image for the image section given the image src.
     *
     * @param String The image src.
     */
    public void setImage(String src)
    {
        setImage( new Img(src) );
    }

    /**
     * Sets the image for the image section.
     *
     * @param Img The image.
     */
    public void setImage(Img img)
    {
        HtmlMarkupTag parent = getContentParent();
        if (_image != null) parent.removeBodyContent(_image);
        _image = img;
        if (_image != null)
        {
            if (_caption != null)
            {
                _image.setParent(parent);
                parent.moveBodyContentToBefore(_caption, _image);
            }
            else
            {
                if ( isInParent() )
                {
                    //No comments. Just add the image
                    _image.setParent(parent);
                }
                else
                {
                    //This means that the comments are around the image. Move the image to after
                    //the first comment (denoted by _imageIndex)
                    _image.setParent(parent);
                    parent.moveBodyContentTo(_imageIndex, _image);
                }
            }
        }
    }

    /**
     * Returns the image object.
     *
     * @return Img
     */
    public Img getImage()
    {
        return _image;
    }

    /**
     * Sets the caption for the image. If a caption already exists, it is replaced.
     *
     * @param String The caption.
     * @throws IllegalStateException if the image has not yet been set.
     */
    public void setCaption(String caption)
    {
        //Need to have an image by now
        if (_image == null) throw new IllegalStateException("Must set image before setting caption");
        HtmlMarkupTag parent = getContentParent();
        if (_caption != null) parent.removeBodyContent(_caption);
        else _image.addLineBreak();
        _caption = (HtmlTextContent)parent.createTextContent(caption);
        parent.moveBodyContentToAfter(_image, _caption);
    }

    /**
     * Returns the image caption.
     *
     * @return String
     */
    public String getCaption()
    {
        if (_caption != null) return _caption.getText();
        else return null;
    }

    public void addToParent()
    {
        setComments();
        if ( !isInParent() )
        {
            MarkupContent parent = getContentParent();
            if (_image != null) _image.setParent(parent);
            if (_caption != null) _caption.setParent(parent);
        }
        else
        {
            CommentTag begin = getBeginCommentTag();
            getTrueParent().addBodyContentAt(begin.getParent().indexOfInBody(begin)+1, this);
        }
    }
}