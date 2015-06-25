package com.zitego.web.jsp;

import com.zitego.markup.MarkupContent;
import com.zitego.markup.html.tag.table.*;
import com.zitego.markup.html.tag.form.*;
import com.zitego.markup.tag.SpecialChar;
import com.zitego.format.FormatType;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 * This class writes out a table with two multi select boxes in it with buttons
 * between to transfer items back and forth between the two. There are a few
 * attributes that can be passed in the tags. They are:
 * <ul>
 *  <li>formName - The name of the form that holds this swap box.
 *  <li>allowDups - Whether to allow duplicates in the destination select box.
 *  <li>max - The maximum allowed number of options in the destination select box.
 *  <li>fromTitle - The column header title of the from swap box select tag. (optional)
 *  <li>toTitle - The column header title of the to swap box select tag. (optional)
 *  <li>titleClass - The style class to apply to the from/to title.
 * </ul>
 * This tag must be used in conjunction with SwapBoxSelectTag in order to create
 * the select objects. For example:<br>
 * <code>
 * <pre>
 * <util:swapBox formName="test_form" allowDups="false" fromTitle="From" toTitle="To">
 *  <util:swapBoxSelect type="from" name="from_opts" size="5">
 *   <util:iterate id="opt" type="int" start="0" max="5">
 *    <option value="<%= opt %>">Option <%= opt %>
 *   </util:iterate>
 *  </util:swapBoxSelect>
 *  <util:swapBoxSelect type="to" name="to_opts" size="5" />
 * </util:swapBox>
 * </pre>
 * </code>
 * The titleClass style attribute will only be applied to the from and the two table cells.
 * If either is not set, then it will not be applied.
 *
 * @author John Glorioso
 * @version $Id: SwapBoxTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class SwapBoxTag extends BodyTagSupport
{
    /** The form that houses this swap box. */
    private Form _form;
    /** The table that houses this swap box. */
    private Table _table;
    /** The from select. */
    private Select _from;
    /** The to select. */
    private Select _to;
    /** The name of the form. */
    protected String _formName;
    /** Whether or not to allow dups. */
    protected boolean _allowDups = false;
    /** The max number of options to allow. */
    protected int _max = -1;
    /** The from column title. */
    protected String _fromTitle;
    /** The to column title. */
    protected String _toTitle;
    /** The class attribute style to apply to the set titles. */
    protected String _titleClass;

    /**
     * Sets the form name.
     *
     * @param name The form name.
     */
    public void setFormName(String name)
    {
        _formName = name;
    }

    /**
     * Whether or not to allow dups when swapping.
     *
     * @param flag The flag.
     */
    public void setAllowDups(boolean flag)
    {
        _allowDups = flag;
    }

    /**
     * Whether or not to allow dups when swapping.
     *
     * @param flag The flag (true or false).
     */
    public void setAllowDups(String flag)
    {
        setAllowDups( new Boolean(flag).booleanValue() );
    }

    /**
     * Sets the maximum amount of options in the destination list.
     *
     * @param max The max.
     */
    public void setMax(int max)
    {
        _max = max;
    }

    /**
     * Sets the maximum amount of options in the destination list.
     *
     * @param max The max.
     */
    public void setMax(String max)
    {
        setMax( Integer.parseInt(max) );
    }

    /**
     * Sets the from title text.
     *
     * @param from The from title text.
     */
    public void setFromTitle(String from)
    {
        _fromTitle = from;
    }

    /**
     * Sets the to title text.
     *
     * @param to The to title text.
     */
    public void setToTitle(String to)
    {
        _toTitle = to;
    }

    /**
     * Sets the title class attribute style.
     *
     * @param titleClass The class attribute style.
     */
    public void setTitleClass(String titleClass)
    {
        _titleClass = titleClass;
    }

    /**
     * Creates the table and the form tag.
     *
     * @return SKIP_BODY
     */
    public int doStartTag() throws javax.servlet.jsp.JspException
    {
        _table = new Table();
        _table.setCellPadding("3");
        _table.setBorder(0);
        Tr row = _table.createRow();
        //from list cell
        row.createCell();
        //button cell
        row.createCell();
        //to list cell
        row.createCell();
        if (_fromTitle != null || _toTitle != null)
        {
            Td cell = row.getCell(0);
            cell.setIsOnOwnLine(true);
            if (_fromTitle != null)
            {
                cell.addBodyContent(_fromTitle);
                if (_titleClass != null) cell.setClassAttribute(_titleClass);
            }
            cell = row.getCell(1);
            cell.setIsOnOwnLine(true);
            cell.addBodyContent( SpecialChar.NBSP.getSymbol() );
            cell = row.getCell(2);
            cell.setIsOnOwnLine(true);
            if (_toTitle != null)
            {
                cell.addBodyContent(_toTitle);
                if (_titleClass != null) cell.setClassAttribute(_titleClass);
            }
            row = _table.createRow();
            row.createCell();
            row.createCell();
            row.createCell();
        }
        //Create a blank form to use
        _form = new Form(_formName);

        return EVAL_BODY_BUFFERED;
    }

    /**
     * Writes out the table and the select tags.
     *
     * @return EVAL_PAGE
     */
    public int doEndTag() throws javax.servlet.jsp.JspException
    {
        //Create the on double click events
        _from.setOnDblClick("moveOption(this, document."+_formName+"."+_to.getName()+", "+_allowDups+", "+_max+")");
        _to.setOnDblClick("moveOption(this, document."+_formName+"."+_from.getName()+", "+_allowDups+", "+_max+")");

        //Add move buttons in center cell
        int r = (_fromTitle != null || _toTitle != null ? 1 : 0);
        Td cell = _table.getRow(r).getCell(1);
        cell.setAlign("center");
        Button button = new Button(cell, _form);
        button.setValue("<");
        button.setOnClick("moveOption(document."+_formName+"."+_to.getName()+", document."+_formName+"."+_from.getName()+", "+_allowDups+(_max > 0 ? ", "+_max : "")+")");
        button = new Button(cell, _form);
        button.setValue(">");
        button.setOnClick("moveOption(document."+_formName+"."+_from.getName()+", document."+_formName+"."+_to.getName()+", "+_allowDups+(_max > 0 ? ", "+_max : "")+")");
        button.addLineBreak();
        //Add move all buttons
        button = new Button(cell, _form);
        button.setValue("<<");
        button.setOnClick("moveAllOptions(document."+_formName+"."+_to.getName()+", document."+_formName+"."+_from.getName()+", "+_allowDups+(_max > 0 ? ", "+_max : "")+")");
        button = new Button(cell, _form);
        button.setValue(">>");
        button.setOnClick("moveAllOptions(document."+_formName+"."+_from.getName()+", document."+_formName+"."+_to.getName()+", "+_allowDups+(_max > 0 ? ", "+_max : "")+")");

        try
        {
            pageContext.getOut().write( _table.format(FormatType.HTML) );
        }
        catch (Exception e)
        {
              throw new JspException("Could not write out the table tag", e);
        }
        return EVAL_PAGE;
    }

    /**
     * Creates a select tag for use with this swap box. It takes the type.
     *
     * @param int The swap box select type.
     * @return Select
     */
    Select createSelectTag(int type)
    {
        boolean from = (type == SwapBoxSelectTag.FROM);
        int r = (_fromTitle != null || _toTitle != null ? 1 : 0);
        Select sel = new Select(_table.getRow(r).getCell( (from ? 0 : 2) ), _form);
        if (from) _from = sel;
        else _to = sel;
        return sel;
    }
}