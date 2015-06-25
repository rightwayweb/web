package com.zitego.web.jsp;

import com.zitego.markup.MarkupContent;
import com.zitego.markup.html.tag.form.Form;
import com.zitego.markup.html.tag.form.Hidden;
import com.zitego.markup.html.tag.form.Text;
import com.zitego.markup.html.tag.form.Button;
import com.zitego.format.FormatType;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * This class writes out a disabled text field with the given text. Additionally, it
 * writes out the hidden tag to hold an optional value. There are a few
 * attributes that can be passed in the tags. They are:
 * <ul>
 *  <li>formName - The name of the form that holds this swap box.
 *  <li>fieldName - The name of the text field.
 *  <li>fieldSize - The size of the text field.
 *  <li>text - The text to display in the field.
 *  <li>hiddenName - The name of the hidden field.
 *  <li>hiddenValue - The hidden field value.
 *  <li>fieldClass - The text field class attribute.
 *  <li>buttonClass - The button class attribute.
 *  <li>buttonAction - The button action.
 * </ul>
 *
 * @author John Glorioso
 * @version $Id: SelectorFieldTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class SelectorFieldTag extends BodyTagSupport
{
    private String _formName;
    private String _fieldName;
    private String _fieldSize = "50";
    private String _text;
    private String _hiddenName;
    private String _hiddenValue;
    private String _buttonText = "Select";
    private String _buttonClass;
    private String _buttonAction;
    private String _buttonHtml;
    private String _fieldClass;

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
     * Sets the field name.
     *
     * @param name The field name.
     */
    public void setFieldName(String name)
    {
        _fieldName = name;
    }

    /**
     * Sets the field size.
     *
     * @param size The field size.
     */
    public void setFieldSize(String size)
    {
        _fieldSize = size;
    }

    /**
     * Sets the text.
     *
     * @param txt The text.
     */
    public void setText(String txt)
    {
        _text = txt;
    }

    /**
     * Sets the hidden field name.
     *
     * @param name The hidden field name.
     */
    public void setHiddenName(String name)
    {
        _hiddenName = name;
    }

    /**
     * Sets the hidden field value.
     *
     * @param val The hidden field value.
     */
    public void setHiddenValue(String val)
    {
        _hiddenValue = val;
    }

    /**
     * Sets the button text.
     *
     * @param txt The button text.
     */
    public void setButtonText(String txt)
    {
        _buttonText = txt;
    }

    /**
     * Sets the button class attribute.
     *
     * @param c The class attribute style.
     */
    public void setButtonClass(String c)
    {
        _buttonClass = c;
    }

    /**
     * Sets the button action.
     *
     * @param action The button action.
     */
    public void setButtonAction(String action)
    {
        _buttonAction = action;
    }

    /**
     * Sets the button html. If this is set, then the button will be printed out as the html and not as the button class, text, etc.
     *
     * @param html The button html.
     */
    public void setButtonHtml(String html)
    {
        _buttonHtml = html;
    }

    /**
     * Sets the text field class attribute.
     *
     * @param c The class attribute style.
     */
    public void setFieldClass(String c)
    {
        _fieldClass = c;
    }

    /**
     * Writes out the table and the select tags.
     *
     * @return EVAL_PAGE
     */
    public int doEndTag() throws javax.servlet.jsp.JspException
    {
        Form f = new Form();
        Text field = new Text(f);
        field.setName(_fieldName);
        field.setSize( Integer.parseInt(_fieldSize) );
        field.setClassAttribute(_fieldClass);
        field.setValue(_text);
        field.setDisabled(true);
        Hidden h = new Hidden(f);
        h.setName(_hiddenName);
        h.setValue(_hiddenValue);
        Button sel = null;
        if (_buttonHtml == null)
        {
            sel = new Button(f);
            sel.setValue(_buttonText);
            sel.setOnClick(_buttonAction);
            sel.setClassAttribute(_buttonClass);
        }

        try
        {
            pageContext.getOut().write
            (
                "<table cellpadding=\"0\" cellspacing=\"0\" border=\"0\"><tr><td>" + field.format(FormatType.HTML) + h.format(FormatType.HTML) + "</td>" + (_buttonHtml == null ? "<td>&nbsp;</td>" : "") + "<td>" + (_buttonHtml != null ? _buttonHtml : sel.format(FormatType.HTML)) + "</td></tr></table>"
            );
        }
        catch (Exception e)
        {
              throw new JspException("Could not write out the selector html markup", e);
        }
        return EVAL_PAGE;
    }
}
