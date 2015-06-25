package com.zitego.web.layout.border;

import com.zitego.markup.Newline;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.web.layout.PageSection;
import com.zitego.markup.IllegalMarkupException;
import com.zitego.markup.html.tag.table.*;
import com.zitego.markup.xml.*;
import com.zitego.format.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import javax.xml.parsers.*;

/**
 * This class is simply a border to wrap page sections. All it is, is a table that wraps
 * the section with north west, north, north east, east, south east, south, south west,
 * and western borders. Any of these cells may be set to contain content.
 *
 * @author John Glorioso
 * @version $Id: PageSectionBorder.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class PageSectionBorder extends Table implements XmlConverter
{
    /** The page section this wraps. */
    protected PageSection _section;
    /** The location of the center cell. */
    private TableLocation _centerLoc = new TableLocation(1, 1);

    /**
     * Creates a new page section border with a parent. This creates three rows in the table.
     * One for the northern borders, one for the eastern/western borders and page section, and
     * one for the southern borders.
     *
     * @param HtmlMarkupTag The parent.
     */
    public PageSectionBorder(HtmlMarkupTag parent)
    {
        super(parent);
        Tr row = createRow();
        row.createCell();
        row.createCell();
        row.createCell();
        row = createRow();
        row.createCell();
        row.createCell();
        row.createCell();
        row = createRow();
        row.createCell();
        row.createCell();
        row.createCell();
    }

    /**
     * Creates a new page section border with all borders specified with image urls. The border
     * cells start at north west and rotate around clockwise. All borders must be images for this
     * constructor.
     *
     * @param HtmlMarkupTag The parent.
     * @param String[] The image urls.
     * @throws IllegalArgumentException if the image array is not 9 elements long or any
     *                                  element is null.
     */
    public PageSectionBorder(HtmlMarkupTag parent, String[] imageUrls)
    {
        this(parent);
    }

    /**
     * Sets the border at the given direction from the page section cell.
     *
     * @param CellDirection The cell direction.
     * @param String The image url.
     */
    public void setBorderUrl(CellDirection dir, String url)
    {
        getCell(_centerLoc, dir).createImage(url);
    }

    /**
     * Sets the page section.
     *
     * @param PageSection The page section.
     */
    public void setPageSection(PageSection section)
    {
        _section = section;
        Td cell = getCell(_centerLoc);
        cell.clearContent();
        _section.setParent(cell);
        _section.addToParent();
    }

    /**
     * Returns the type of border this is.
     *
     * @return PageSectionBorderType
     */
    public abstract PageSectionBorderType getType();

    /**
     * Returns the corner images as a four element array of byte arrays. These byte
     * arrays can then be saved as gif files. The returned array is of type Object,
     * but the length will always be 0 or 4 and the type will always be byte[]. If
     * this is not the case, then refer to the documentation of the extending class
     * for more information.
     *
     * @return Object[]
     */
    public abstract Object[] getCornerImages();

    public void buildFromXml(Element root)
    {
        String val = root.getAttribute("bgcolor");
        if (val != null) setBgColor(val);
    }

    public void addChild(Element child)
    {

    }

    public void parse(Object objToParse, FormatType type) throws IllegalMarkupException, UnsupportedFormatException
    {
        if (objToParse instanceof Element && type == FormatType.XML) buildFromXml( (Element)objToParse );
        else super.parse(objToParse, type);
    }

    protected String generateContent(FormatType type) throws UnsupportedFormatException
    {
        if (type != FormatType.XML) return super.generateContent(type);

        if ( hasChanged() )
        {
            StringBuffer ret = new StringBuffer();

            String padding = getPadding(type);
            //Need to pad two spaces in after the padding
            ret.append(padding).append("  <border type=\"").append( getType().getValue() ).append("\"");
            if ( getBgColor() != null) ret.append(" ").append( getAttribute("bgcolor") );
            ret.append(" />");
            return ret.toString();
        }
        else
        {
            return (String)getCachedContent(type);
        }
    }
}