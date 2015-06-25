package com.zitego.web.layout;

import com.zitego.markup.*;
import com.zitego.markup.tag.SpecialChar;
import com.zitego.markup.tag.CommentTag;
import com.zitego.markup.html.tag.HtmlMarkupTag;
import com.zitego.markup.html.tag.table.*;
import com.zitego.web.layout.section.*;
import com.zitego.web.layout.border.*;
import com.zitego.markup.xml.*;
import com.zitego.format.*;
import java.util.Vector;
import org.w3c.dom.*;

/**
 * This class is a base class for all page sections. Page sections are an
 * extension of the table class. The methods are for adding new sections or
 * html content. Note that a page section is <u>NOT</u> automatically added
 * to the body of the parent tag. This must be added manually.
 * <br><br>
 * A PageSection can have a border and that border is added to the page. It
 * then becomes the parent of the page section, however to mask the border
 * from classes that use the section, set and getParent will set and get the
 * parent of the border. For example, if the parent of the section is a table
 * cell and there is no border, then getParent will return the cell. If
 * setPageSectionBorder is called then getParent will still return the table
 * cell. setParent is also overidden to set the parent according to whether
 * there is a border or not. Setting the parent of the section when there is
 * a border will just call setParent on the border.
 *
 * @author John Glorioso
 * @version $Id: PageSection.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class PageSection extends Table implements XmlConverter
{
    /** The section's name. */
    private String _name;
    /** This section's border. */
    private PageSectionBorder _border;
    /** The begin comment tag. */
    private CommentTag _beginComment;
    /** The end comment tag. */
    private CommentTag _endComment;

    /**
     * Creates a new page section with an html parent tag.
     *
     * @param HtmlMarkupTag The parent tag.
     */
    protected PageSection(HtmlMarkupTag parent)
    {
        super(parent);
        setCellPadding("0");
        setCellSpacing("0");
    }

    public Tr createRow()
    {
        return createRowAt( getRows().size() );
    }

    public Tr createRowAt(int index) throws IndexOutOfBoundsException
    {
        return addRow( index, new Row(this) );
    }

    /**
     * Overrides the parent to create a row if there is no first row.
     *
     * @return Tr
     */
    public Tr getFirstRow()
    {
        Tr row = super.getFirstRow();
        if (row == null) row = createRow();
        return row;
    }

    /**
     * Overrides the parent to create a row if there is no last row.
     *
     * @return Tr
     */
    public Tr getLastRow()
    {
        Tr row = super.getLastRow();
        if (row == null) row = createRow();
        return row;
    }

    /**
     * Adds an html tag content to the page section in the current row at the next cell.
     * If a current row does not exist, then a new one is first created.
     *
     * @param HtmlMarkupTag The content to add.
     * @throws IllegalArgumentException if the content is null.
     */
    public void addContent(HtmlMarkupTag content) throws IllegalArgumentException
    {
        Td parent = getParent(false);
        content.setParent(parent);
    }

    /**
     * Adds a text content to the page section in the current row at the next cell.
     * If a current row does not exist, then a new one is first created.
     *
     * @param TextContent The text to add.
     * @throws IllegalArgumentException if the content is null.
     */
    public void addContent(TextContent content) throws IllegalArgumentException
    {
        Td parent = getParent(false);
        content.setParent(parent);
    }

    /**
     * Adds text content to the page section in the current row at the next cell.
     * If a current row does not exist, then a new one is first created.
     *
     * @param String The text to add.
     * @throws IllegalArgumentException if the content is null.
     */
    public void addContent(String content) throws IllegalArgumentException
    {
        Td parent = getParent(false);
        parent.createTextContent(content);
    }

    /**
     * Adds a PageSection given the type to this page section in the current row at the next cell.
     * If a current row does not exist, then a new one is first created and returns the resulting
     * section.
     *
     * @param PageSectionType The section type to create and add.
     * @return PageSection
     * @throws IllegalArgumentException if the type is invalid.
     */
    public PageSection addSection(PageSectionType type) throws IllegalArgumentException
    {
        Td parent = getParent(false);
        //We don't add sections in here. We leave that to the section itself to do
        return (PageSection)PageSectionFactory.createPageSection(type, parent);
    }

    /**
     * Adds an html tag content to the page section to a new row as the first cell.
     *
     * @param HtmlMarkupTag The content to add.
     * @throws IllegalArgumentException if the content is null.
     */
    public void addContentNextRow(HtmlMarkupTag content) throws IllegalArgumentException
    {
        Td parent = getParent(true);
        content.setParent(parent);
    }

    /**
     * Adds a text content to the page section in a new row as the first cell.
     * If a current row does not exist, then a new one is first created.
     *
     * @param TextContent The text to add.
     * @throws IllegalArgumentException if the content is null.
     */
    public void addContentNextRow(TextContent content) throws IllegalArgumentException
    {
        Td parent = getParent(false);
        content.setParent(parent);
    }

    /**
     * Adds text content to the page section in a new row as the first cell.
     * If a current row does not exist, then a new one is first created.
     *
     * @param String The text to add.
     * @throws IllegalArgumentException if the content is null.
     */
    public void addContentNextRow(String content) throws IllegalArgumentException
    {
        Td parent = getParent(true);
        parent.createTextContent(content);
    }

    /**
     * Adds an page section to this page section to a new row as the first cell and returns
     * the resulting section.
     *
     * @param PageSectionType The section to add.
     * @return PageSection
     * @throws IllegalArgumentException if the section type is invalid.
     */
    public PageSection addSectionNextRow(PageSectionType type) throws IllegalArgumentException
    {
        Td parent = getParent(true);
        //We don't add sections in here. We leave that to the section itself to do
        return (PageSection)PageSectionFactory.createPageSection(type, parent);
    }

    /**
     * Adds an html tag content to the page section at the specified location.
     * If the specified row does not exist and it is one greater than the max, then
     * it is first created. If the location is otherwise invalid, then an exception
     * is thrown.
     *
     * @param HtmlMarkupTag The content to add.
     * @param TableLocation The location to add the content.
     * @throws IllegalArgumentException if the content is null or the location is invalid.
     */
    public void addContent(HtmlMarkupTag content, TableLocation loc) throws IllegalArgumentException
    {
        Td parent = getParent(loc);
        content.setParent(parent);
    }

    /**
     * Adds a text content to the page section at the specified location.
     * If the specified row does not exist and it is one greater than the max, then
     * it is first created. If the location is otherwise invalid, then an exception
     * is thrown.
     *
     * @param TextContent The text to add.
     * @param TableLocation The location to add the content.
     * @throws IllegalArgumentException if the content is null or the location is invalid.
     */
    public void addContent(TextContent content, TableLocation loc) throws IllegalArgumentException
    {
        Td parent = getParent(loc);
        content.setParent(parent);
    }

    /**
     * Adds text content to the page section at the specified location.
     * If the specified row does not exist and it is one greater than the max, then
     * it is first created. If the location is otherwise invalid, then an exception
     * is thrown.
     *
     * @param TextContent The text to add.
     * @param TableLocation The location to add the content.
     * @throws IllegalArgumentException if the content is null or the location is invalid.
     */
    public void addContent(String content, TableLocation loc) throws IllegalArgumentException
    {
        Td parent = getParent(loc);
        parent.createTextContent(content);
    }

    /**
     * Adds a PageSection given the type to the page section at the specified location.
     * If the specified row does not exist and it is one greater than the max, then
     * it is first created. If the location is otherwise invalid, then an exception
     * is thrown.
     *
     * @param PageSectionType The section type to create and add.
     * @param TableLocation The location to add the content.
     * @return PageSection
     * @throws IllegalArgumentException if the content is null or the location is invalid.
     */
    public PageSection addSection(PageSectionType type, TableLocation loc) throws IllegalArgumentException
    {
        Td parent = getParent(loc);
        //We don't add sections in here. We leave that to the section itself to do
        return (PageSection)PageSectionFactory.createPageSection(type, parent);
    }

    /**
     * Returns the parent cell based on the TableLocation passed in. If the table location
     * occurs in a row past the current size, then empty cells will first be added.
     *
     * @param TableLocation The location of the cell to add.
     * @return Td
     * @throws IllegalArgumentException if the TableLocation is invalid.
     */
    private Td getParent(TableLocation loc)
    {
        if (loc == null) throw new IllegalArgumentException("TableLocation cannot be null");

        Tr row = null;
        int size = getRows().size();
        if (loc.row < size) row = getRow(loc.row);
        else if (loc.row == size) row = createRow();
        else throw new IllegalArgumentException("Invalid row");

        while (row.getCells().size() < loc.column)
        {
            Cell cell = (Cell)row.createCell();
            cell.setIsOnOwnLine(true);
            cell.addBodyContent( SpecialChar.NBSP.getSymbol() );
        }
        return row.createCellAt(loc.column, false);
    }

    /**
     * Returns the parent cell based on whether we are adding to the next row or not.
     *
     * @param boolean Whether to create a new row.
     * @return Td
     */
    private Td getParent(boolean newRow)
    {
        Tr row = ( newRow ? createRow() : getLastRow() );
        return row.createCell();
    }

    /**
     * Sets the name of the section.
     *
     * @param String The name.
     */
    public void setName(String name)
    {
        _name = name;
    }

    /**
     * Returns the name.
     *
     * @return String
     */
    public String getName()
    {
        return _name;
    }

    /**
     * Removes the page section border.
     */
    public void removePageSectionBorder()
    {
        if (_border != null)
        {
            _border.getParent().removeBodyContent(_border);
            _border = null;
            //Add this and reset the parent
            setParent( getTrueParent() );
            addToParent();
        }
    }

    /**
     * Sets the page section border given the type and returns it.
     *
     * @param PageSectionBorderType The border type.
     * @return PageSectionBorder
     */
    public PageSectionBorder setPageSectionBorder(PageSectionBorderType type)
    {
        setPageSectionBorder( PageSectionBorderFactory.createBorder(type, (HtmlMarkupTag)getParent()) );
        return _border;
    }

    /**
     * Sets the given page section border.
     *
     * @param PageSectionBorder The border.
     */
    public void setPageSectionBorder(PageSectionBorder border)
    {
        //Remove the current border from the parent cell
        MarkupContent borderParent = getParent();
        if (_border != null) borderParent.removeBodyContent(_border);

        //See if the new border is null
        if (border != null)
        {
            //Set the new border's parent
            border.setParent(borderParent);
            //Set the current border to null
            _border = null;
            //Add this page section to it which will set the parent of this to the appropriate
            //cell in the border
            border.setPageSection(this);
        }
        else
        {
            //Need to make the parent of this what it was
            _border = null;
            setParent(borderParent);
            addToParent();
        }
        //Now set it as the border. Do this last, so calls to getParent and setParent work right
        _border = border;
    }

    /**
     * Returns the page section border.
     *
     * @return PageSectionBorder
     */
    public PageSectionBorder getPageSectionBorder()
    {
        return _border;
    }

    /**
     * This method is to set the begin comment of the section if there are any.
     * This is determined by making a call to getBeginComment(). If
     * there is a parent and the section resides within it, then the comment
     * will appear before the section, otherwise, it will simply be added to
     * the parent. If the parent is null, then the comment is added as the first
     * content within this section (before the first row). Once the comment has
     * been added it is returned.
     */
    protected void setBeginComment()
    {
        CommentTag tag = null;
        String comment = getBeginComment();
        if (_beginComment != null) _beginComment.getParent().removeBodyContent(_beginComment);
        if (comment != null)
        {
            HtmlMarkupTag parent = getTrueParent();
            if (parent != null)
            {
                int index = parent.indexOfInBody(this);
                if (index == -1) index = parent.getBodySize();
                tag = (CommentTag)parent.moveBodyContentTo( index, parent.addComment(comment) );
            }
            else
            {
                tag = (CommentTag)moveBodyContentTo( 0, addComment(comment) );
            }
        }
        _beginComment = tag;
    }

    /**
     * Returns the text of the begin comment. By default this returns null and needs to
     * be overwritten to return an actual comment.
     *
     * @return String
     */
    protected String getBeginComment()
    {
        return null;
    }

    /**
     * Returns the begin comment.
     *
     * @return CommentTag
     */
    public CommentTag getBeginCommentTag()
    {
        return _beginComment;
    }

    /**
     * This method is to set the comment after the page section if any.
     * This is determined by making a call to getBeginComment(). If
     * there is a parent and the section resides within it, then the comment
     * will appear after the section, otherwise, it will simply be added to
     * the parent. If the parent is null, then the comment is added as the last
     * content within this section (after the last row). Once the comment has
     * been added it is returned.
     */
    protected void setEndComment()
    {
        CommentTag tag = null;
        String comment = getEndComment();
        if (_endComment != null) _endComment.getParent().removeBodyContent(_endComment);
        if (comment != null)
        {
            HtmlMarkupTag parent = getTrueParent();
            if (parent != null)
            {
                int index = parent.indexOfInBody(this);
                if (index == -1) index = parent.getBodySize();
                else index++;
                tag = (CommentTag)parent.moveBodyContentTo( index, parent.addComment(comment) );
            }
            else
            {
                tag = (CommentTag)moveBodyContentTo( getBodySize(), addComment(comment) );
            }
        }
        _endComment = tag;
    }

    /**
     * Returns the text of the end comment. By default this returns null and needs to
     * be overwritten to return an actual comment.
     *
     * @return String
     */
    protected String getEndComment()
    {
        return null;
    }

    /**
     * Returns the end comment.
     *
     * @return CommentTag
     */
    public CommentTag getEndCommentTag()
    {
        return _endComment;
    }

    /**
     * This handles two things. First it takes into consideration the border if there
     * is one. If there is a border then the parent is set on that rather then this
     * section. If there is not a border, then we check to see if there are begin and
     * end comments surrounding this section. If there are, then we go ahead and remove
     * those from the parent. By nature of a page section, it is not added to the parent
     * in the setParent call, so it is the responsibility of the calling class to reset
     * the comments after this method has returned. This is done automatically if addToParent
     * is called.
     *
     * @param MarkupContent The new parent.
     */
    public void setParent(MarkupContent parent)
    {
        if (_border != null) _border.setParent(parent);
        else super.setParent(parent);
    }

    public MarkupContent getParent()
    {
        if (_border != null) return _border.getParent();
        else return getTrueParent();
    }

    /**
     * Returns the true parent of this section regardless of whether there is a border.
     *
     * @return HtmlMarkupTag
     */
    public HtmlMarkupTag getTrueParent()
    {
        return (HtmlMarkupTag)super.getParent();
    }

    /**
     * Returns whether or not this section is in the parent's MarkupBody.
     *
     * @return boolean
     */
    protected boolean isInParent()
    {
        MarkupContent parent = getTrueParent();
        return (parent != null && parent.indexOfInBody(this) > -1);
    }

    /**
     * Determines whether or not this is in the parent or not. If so, it returns the first
     * row's first cell. If the row/cell does not exist yet, then it is created. If this
     * does not exist within the parent, then the parent is returned.
     *
     * @return HtmlMarkupTag
     */
    protected HtmlMarkupTag getContentParent()
    {
        //See if we are in the parent or not
        boolean inParent = isInParent();
        //Create the cell if we need to
        if (inParent && getRows().size() == 0) createRow().createCell();
        return (HtmlMarkupTag)( inParent ? getFirstRow().getFirstCell() : getParent() );
    }

    /**
     * Adds this section to the parent based on the section's real parent. If there is a border
     * then a call to getParent will return the parent's border. This method will use the call
     * to getTrueParent() to add the section to the real parent. It will also adds the comments
     * by calling setBeginComment and setEndComment.
     */
    public void addToParent()
    {
        addToParentAt( getParent().getBodySize() );
    }

    /**
     * Adds this section to the parent based on the section's real parent at the given index. If
     * there is a border then a call to getParent will return the parent's border. This method
     * will use the call to getTrueParent() to add the section to the real parent. It will also
     * adds the comments by calling setBeginComment and setEndComment. If the index is greater
     * then the largest index, it will add it to the end.
     *
     * @param int The index
     */
    public void addToParentAt(int index)
    {
        if ( !addToParentOnInit() )
        {
            MarkupContent parent = getTrueParent();
            parent.addBodyContentAt(index, this);
            setBeginComment();
            setEndComment();
        }
    }

    /**
     * Removes this section and any comments from the parent and returns the index
     * of where the section was.
     *
     * @return in
     */
    public int removeFromParent()
    {
        int ret = -1;
        MarkupContent parent = getParent();
        if (_border != null)
        {
            ret = parent.removeBodyContent(_border);
        }
        else
        {
            ret = parent.removeBodyContent(this);
            if (_beginComment != null) ret = parent.removeBodyContent(_beginComment);
            if (_endComment != null) parent.removeBodyContent(_endComment);
        }
        return ret;
    }

    protected boolean addToParentOnInit()
    {
        return false;
    }

    public void buildFromXml(Element root)
    {
        String val = root.getAttribute("name");
        if ( val != null && !"".equals(val) ) setName(val);
        val = root.getAttribute("bgcolor");
        if ( val != null && !"".equals(val) ) setBgColor(val);

        NodeList nodes = root.getChildNodes();
        int size = nodes.getLength();
        for (int i=0; i<size; i++)
        {
            Node n = nodes.item(i);
            if (n instanceof Element)
            {
                addChild( (Element)n );
            }
        }
    }

    public void addChild(Element child)
    {
        if ( child.getNodeName().equalsIgnoreCase("border") )
        {
            PageSectionBorder border = PageSectionBorderFactory.createBorder( PageSectionBorderType.evaluate(Integer.parseInt(child.getAttribute("type"))), (HtmlMarkupTag)getParent() );
            border.buildFromXml(child);
            setPageSectionBorder(border);
        }
        else if ( child.getNodeName().equalsIgnoreCase("row") )
        {
            //Create a row
            Row row = (Row)createRow();
            row.buildFromXml(child);
        }
    }

    public void parse(Object objToParse, FormatType type) throws IllegalMarkupException, UnsupportedFormatException
    {
        if (objToParse instanceof Element && type == FormatType.XML) buildFromXml( (Element)objToParse );
        else super.parse(objToParse, type);
    }

    public boolean countDeepness(FormatType type)
    {
        return (type == FormatType.XML);
    }

    protected String generateContent(FormatType type) throws UnsupportedFormatException
    {
        if (type != FormatType.XML) return super.generateContent(type);

        if ( hasChanged() )
        {
            StringBuffer ret = new StringBuffer();
            String padding = getPadding(type);
            ret.append(padding).append("<section");
            if (_name != null) ret.append(" name=\"").append(_name).append("\"");
            if ( getBgColor() != null) ret.append(" ").append( getAttribute("bgcolor") );
            ret.append(">").append(Newline.CHARACTER);

            if (_border != null) ret.append( _border.format(FormatType.XML) ).append(Newline.CHARACTER);

            int size = getRows().size();
            for (int i=0; i<size; i++)
            {
                ret.append( getRow(i).format(type) ).append(Newline.CHARACTER);
            }

            ret.append(padding).append("</section>");
            return ret.toString();
        }
        else
        {
            return (String)getCachedContent(type);
        }
    }

    protected MarkupBody createMarkupBody()
    {
        return new PageLayoutMarkupBody(this);
    }
}