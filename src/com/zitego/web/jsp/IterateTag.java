package com.zitego.web.jsp;

import java.util.*;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;

/**
 * This class iterates forward over a Collection or array of Objects and sets
 * the Object at each iteration to the variable name specified by
 * <code>id</code> in the tag itself. You can set the Collection by using the
 * attribute <code>collection</code> or you can set the array by using the
 * attribute <code>array</code>. If you do not set one or the other, the value
 * of the counter will be returned as a <code>java.lang.Integer</code>, but you
 * must set a max in this case or you will not get any iteration. You can set
 * the array with primitive types, however they will be returned as Objects in
 * the <code>id</code> variable.<br><br>
 *
 * In addition, you can set alternating colors to be returned by setting the
 * color1 and color2 attributes. This color can be referred to inside of the
 * iterate tags by setting the colorid attribute.<br><br>
 *
 * You can also reference the counter that the iterator is currently on by
 * setting the countid attribute in the tag. The start value of this can be
 * altered by setting the start attribute.
 *
 * @author John Glorioso
 * @version $Id: IterateTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class IterateTag extends BodyTagSupport
{
    /** The internal iterator for collections. */
    private Iterator _iterator;
    /** The array of objects. */
    private Object[] _array;
    /** The type of value we are setting. */
    private String _type;
    /** The current position of the iterator or array index. */
    private int _pos = 0;
    /** The start position of the iterator or array index. */
    private int _start = 0;
    /** The max value the array index can be. */
    private int _max = -1;
    /** The color for odd rows. */
    private String _color1 = "#ffffff";
    /** The color for even rows. */
    private String _color2 = "#ffffff";
    /** The id to store the color attribute in. */
    private String _colorId;
    /** The id to store the count in. */
    private String _countId;

    public void setCollection(Collection collection)
    {
        _iterator = collection.iterator();
    }

    public void setArray(Object[] obj)
    {
        _array = obj;
    }

    public void setArray(int[] ar)
    {
        Integer[] ar2 = new Integer[ar.length];
        System.arraycopy(ar, 0, ar2, 0, ar.length);
        setArray(ar2);
    }

    public void setArray(double[] ar)
    {
        Double[] ar2 = new Double[ar.length];
        System.arraycopy(ar, 0, ar2, 0, ar.length);
        setArray(ar2);
    }

    public void setArray(short[] ar)
    {
        Short[] ar2 = new Short[ar.length];
        System.arraycopy(ar, 0, ar2, 0, ar.length);
        setArray(ar2);
    }

    public void setArray(long[] ar)
    {
        Long[] ar2 = new Long[ar.length];
        System.arraycopy(ar, 0, ar2, 0, ar.length);
        setArray(ar2);
    }

    public void setArray(float[] ar)
    {
        Float[] ar2 = new Float[ar.length];
        System.arraycopy(ar, 0, ar2, 0, ar.length);
        setArray(ar2);
    }

    public void setArray(byte[] ar)
    {
        Byte[] ar2 = new Byte[ar.length];
        System.arraycopy(ar, 0, ar2, 0, ar.length);
        setArray(ar2);
    }

    public void setArray(boolean[] ar)
    {
        Boolean[] ar2 = new Boolean[ar.length];
        System.arraycopy(ar, 0, ar2, 0, ar.length);
        setArray(ar2);
    }

    public void setArray(char[] ar)
    {
        Character[] ar2 = new Character[ar.length];
        System.arraycopy(ar, 0, ar2, 0, ar.length);
        setArray(ar2);
    }

    public void setType(String type)
    {
        _type = IterateTagExtraInfo.getType(type);
    }

    public void setColor1(String color)
    {
        _color1 = color;
    }

    public String getColor1()
    {
        return _color1;
    }

    public void setColor2(String color)
    {
        _color2 = color;
    }

    public String getColor2()
    {
        return _color1;
    }

    /**
     * Returns the active Color for the current iteration. The first iteration uses _color1, and the
     * colors alternate from there.
     *
     * @return String
     */
    public String getColor()
    {
        if (_pos % 2 == 0) return _color1;
        else return _color2;
    }

    /**
     * Sets the max array index.
     *
     * @param The max.
     */
    public void setMax(int max)
    {
        _max = max;
    }

    /**
     * Sets the max array index.
     *
     * @param String
     */
    public void setMax(String max)
    {
        _max = Integer.parseInt(max);
    }

    /**
     * Sets the next color id to use.
     *
     * @param String The id.
     */
    public void setNextColorId(String id)
    {
        _colorId = id;
    }

    /**
     * Sets the count id to use.
     *
     * @param String The id.
     */
    public void setCountId(String id)
    {
        _countId = id;
    }

    /**
     * Returns the current position.
     *
     * @return int
     */
    public int getPos()
    {
        return _pos;
    }

    /**
     * Sets the array start.
     *
     * @param int The start index.
     */
    public void setStart(int start)
    {
        _start = start;
    }

    /**
     * Sets the array start.
     *
     * @param String The start index.
     */
    public void setStart(String start)
    {
        _start = Integer.parseInt(start);
    }

    public int doStartTag() throws JspException
    {
        //Initialize loop index
        _pos = _start;
        //Check to see if we can even go through once
        if ( hasNext() )
        {
            //Set countid, colorid, and id
            setPageVars();
            return EVAL_BODY_BUFFERED;
        }
        else
        {
            return SKIP_BODY;
        }
    }

    public int doAfterBody() throws JspException
    {
        //Increment the current position
        _pos++;
        //If we have another iteration, then set the page vars and iterate. If not, write the body
        //and stop iterating
        if ( hasNext() )
        {
            setPageVars();
            return EVAL_BODY_AGAIN;
        }
        else
        {
            writeBody();
            return SKIP_BODY;
        }
    }

    private void writeBody() throws JspException
    {
        try
        {
            //bodyContent is a protected member of BodyTagSupport
            if (bodyContent != null) bodyContent.writeOut( bodyContent.getEnclosingWriter() );
        }
        catch (java.io.IOException e)
        {
            throw new JspException( "IO Error: " + e.getMessage() );
        }
    }

    private boolean hasNext()
    {
        //See if we are working with an array or collection. The collection
        //has precedence
        if (_iterator != null)
        {
            return ( _iterator.hasNext() && (_max < 0 || _pos < _max) );
        }
        else if (_array != null)
        {
            return ( _pos < _array.length && (_max < 0 || _pos < _max) );
        }
        else
        {
            return (_pos < _max);
        }
    }

    private void setPageVars()
    {
        //Set the count id
        if (_countId != null)
        {
            pageContext.setAttribute( _countId, new Integer(_pos) );
        }
        //Set the color id
        if (_colorId != null)
        {
            pageContext.setAttribute( _colorId, getColor() );
        }
        //Set the id
        if (_iterator != null) pageContext.setAttribute( getId(), _iterator.next() );
        else if (_array != null) pageContext.setAttribute( getId(), _array[_pos] );
        else pageContext.setAttribute( getId(), new Integer(_pos) );
    }
}