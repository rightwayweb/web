package com.zitego.web.jsp;

import com.zitego.util.*;
import javax.servlet.jsp.*;
import javax.servlet.*;
import javax.servlet.jsp.tagext.*;

/**
 * This handles sorting a Sortable collection given the columns passed in based on the "dir="
 * and "col=" request parameters produced by the SortableColumnHeaderTag. This will check
 * the request attributes too if there are none in the parameter.
 *
 * @author John Glorioso
 * @version $Id: SortTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see SortableColumnHeaderTag
 */
public class SortTag extends TagSupport
{
    /** The sortable object. */
    private Sortable _sortableObject;
    /** The sort columns. */
    private SortColumn[] _cols;

    /**
     * Sets the sortable object.
     *
     * @param Sortable The sortable object.
     */
    public void setSortable(Sortable obj)
    {
        _sortableObject = obj;
    }

    /**
     * Sets the sort columns.
     *
     * @param SortColumn[] The columns.
     */
    public void setCols(SortColumn[] cols)
    {
        _cols = cols;
    }

    public int doStartTag() throws JspException
    {
        if (_sortableObject == null || _cols == null) return SKIP_BODY;

        ServletRequest request = pageContext.getRequest();
        String arg = request.getParameter("dir");
        if (arg == null) arg = (String)request.getAttribute("dir");
        if (arg == null) return SKIP_BODY;
        if (Integer.parseInt(arg) > 0) _sortableObject.setSortAscending();
        else _sortableObject.setSortDescending();

        arg = request.getParameter("col");
        if (arg == null) arg = (String)request.getAttribute("col");
        if (arg == null) return SKIP_BODY;
        SortColumn col = evaluate( Integer.parseInt(arg) );
        if (col == null) return SKIP_BODY;

        _sortableObject.setSortColumn(col);
        _sortableObject.sort();

        return SKIP_BODY;
    }

    private SortColumn evaluate(int col)
    {
        for (int i=0; i<_cols.length; i++)
        {
            if (_cols[i].getValue() == col) return _cols[i];
        }
        return null;
    }
}