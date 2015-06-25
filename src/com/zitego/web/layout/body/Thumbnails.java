package com.zitego.web.layout.body;

import com.zitego.web.layout.*;
import com.zitego.web.layout.section.*;
import com.zitego.markup.MarkupContent;
import com.zitego.markup.html.*;
import com.zitego.markup.html.tag.*;
import com.zitego.markup.html.tag.table.*;
import com.zitego.format.*;
import java.util.Vector;

/**
 * This class represents a page body layout of image thumbnails. The number of thumbnails
 * across is used to determine what row the next thumbnail added will reside. The default
 * number of thumbnails in each row is 5. This can be changed.
 *
 * @author John Glorioso
 * @version $Id: Thumbnails.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class Thumbnails extends PageBodyLayout
{
    public static void main(String[] args) throws Exception
    {
        com.zitego.markup.html.tag.Html page = new com.zitego.markup.html.tag.Html();
        Thumbnails t = new Thumbnails( page.getBodyTag() );
        t.addToParent();

        System.out.println("Html: \r\n"+page.format(com.zitego.format.FormatType.HTML)+"\r\n");
        System.out.println("Xml: \r\n"+t.format(com.zitego.format.FormatType.XML));
    }

    /** The number of thumbnails across to display per row. */
    private int _thumbnailsPerRow = 5;
    /** The text section. */
    private TextSection _textSection;
    /** The thumbnails. */
    private Vector _thumbnails = new Vector();

    /**
     * Creates a thumbnails layout with a HtmlMarkupTag parent.
     *
     * @param HtmlMarkupTag The parent.
     */
    protected Thumbnails(HtmlMarkupTag parent)
    {
        super(parent);
        setValign("top");
        if (parent instanceof Td) ( (Td)parent ).setValign("top");
        Tr row = createRow();
        Td cell = row.createCell();
        cell.setColspan(_thumbnailsPerRow);
        _textSection = (TextSection)setAlternateSection(PageSectionType.TEXT);
        createEmptyRow();
    }

    /**
     * Sets the number of thumbnails per row.
     *
     * @param int The number.
     */
    public void setThumbnailsPerRow(int num)
    {
        //Get out of here if it didn't change
        if (_thumbnailsPerRow == num) return;

        _thumbnailsPerRow = num;
        int size = _thumbnails.size();
        if (size > 0)
        {
            //Clear out the thumbnails rows
            int start = (_textSection != null ? 2 : 0);
            while (getRows().size() > start)
            {
                Tr row = getRow(start);
                row.clearCells();
                removeRow(row);
            }
            //Re-add the thumbnails
            for (int i=0; i<size; i++)
            {
                Td cell = getNextCell();
                ImageSection section = (ImageSection)_thumbnails.get(i);
                section.setParent(cell);
                section.addToParent();
            }
        }
        //Reset the colspan on the text section if it is there
        if (_textSection != null)
        {
            Td cell = getRow(0).getCell(0);
            cell.setColspan(_thumbnailsPerRow);
            cell = getRow(1).getCell(0);
            cell.setColspan(_thumbnailsPerRow);
        }
    }

    /**
     * Returns the number of thumbnails per row.
     *
     * @return int
     */
    public int getThumbnailsPerRow()
    {
        return _thumbnailsPerRow;
    }

    /**
     * Adds a thumbnail with an image source and a caption and returns the image section. If the
     * caption is null, it is not set. If the width or height is null, then they will not be set.
     *
     * @param String The image source.
     * @param String The caption.
     * @param String The width.
     * @param String The height.
     * @param String The alternate comment for the image.
     * @return ImageSection
     */
    public void addThumbnail(String src, String caption, String width, String height, String alt)
    {
        Td cell = getNextCell();
        ImageSection ret = (ImageSection)PageSectionFactory.createPageSection(PageSectionType.IMAGE, cell);
        ret.setImage(src);
        Img img = ret.getImage();
        if (caption != null) ret.setCaption(caption);
        if (width != null) img.setWidth(width);
        if (height != null) img.setHeight(height);
        if (alt != null) img.setAlt(alt);
        _thumbnails.add(ret);
    }

    /**
     * Adds a thumbnail with a string image tag and a caption.
     *
     * @param String The html image tag.
     * @param String The caption.
     * @throws UnsupportedFormatException
     */
    public void addThumbnail(String imageTag, String caption) throws UnsupportedFormatException
    {
        Img image = (Img)HtmlMarkupFactory.getInstance().parse(new StringBuffer(imageTag), null, FormatType.HTML, true)[0];
        addThumbnail( image.getSrc(), caption, image.getWidth(), image.getHeight(), image.getAlt() );
    }

    /**
     * Returns the next row/cell tag.
     *
     * @return Td
     */
    private Td getNextCell()
    {
        Tr row = ( _thumbnails.size() == 0 ? createRow() : getLastRow() );
        //Check to see if there is a cell in this row that is empty
        int size = row.getCells().size();
        Td cell = null;
        for (int i=0; i<size; i++)
        {
            cell = row.getCell(i);
            if ( cell.isEmpty() ) return cell;
        }
        if (row.getCells().size() == _thumbnailsPerRow) row = createRow();
        cell = row.createCell();
        cell.setAlign("center");
        return cell;
    }

    /**
     * Returns all the thumbnail image sections.
     *
     * @return Vector
     */
    public Vector getThumbnails()
    {
        return _thumbnails;
    }

    /**
     * Returns the thumbnail at the given index. If the index is not in the thumbnail Vector
     * range, then null is returned.
     *
     * @param int The index.
     * @return ImageSection
     */
    public ImageSection getThumbnail(int index)
    {
        if ( index < 0 || index >= _thumbnails.size() ) return null;
        else return (ImageSection)_thumbnails.get(index);
    }

    /**
     * Returns a Vector of thumbnails for the given row. If the row is out of range, then
     * a vector of nulls is returned with a size of the number of _thumbnailsPerRow.
     *
     * @return Vector
     * @throws IllegalArgumentException if the row index is negative.
     */
    public Vector getThumbnailsForRow(int row)
    {
        if (row < 0) throw new IllegalArgumentException("Row must be positive");
        Vector ret = new Vector();
        int start = row*_thumbnailsPerRow;
        int end = start+_thumbnailsPerRow;
        int size = _thumbnails.size();
        for (int i=start; i<end&&i<size; i++)
        {
            ret.add( _thumbnails.get(i) );
        }
        while (ret.size() < _thumbnailsPerRow)
        {
            ret.add(null);
        }
        return ret;
    }

    protected Td getMainSectionCell()
    {
        return null;
    }

    protected Td getAlternateSectionCell()
    {
        return getFirstRow().getFirstCell();
    }

    /**
     * Not used. Call getThumbnails.
     *
     * @param PageSectionType Not used.
     * @return PageSection
     * @throws IllegalArgumentException
     */
    public PageSection setMainSection(PageSectionType type) throws IllegalArgumentException
    {
        throw new IllegalArgumentException("Cannot call setMainSection in this class.");
    }

    public PageBodyLayoutType getType()
    {
        return PageBodyLayoutType.THUMBNAILS;
    }

    protected String getBeginComment()
    {
        return "Begin Thumbnails Section";
    }

    protected String getEndComment()
    {
        return "End Thumbnails Section";
    }
}