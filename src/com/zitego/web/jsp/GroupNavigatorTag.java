package com.zitego.web.jsp;

import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import com.zitego.util.GroupNavigator;

/**
 * This displays links for iterating through a sortable group. The parameters
 * that can be specified for this tag are:
 * <ul>
 *  <li>navigator - The sortable group navigator (required).
 *  <li>linkUrl - The base link url that displays the navigator results.
 *  <li>linkClass - The link class attribute to use.
 * </ul>
 *
 * @author John Glorioso
 * @version $Id: GroupNavigatorTag.java,v 1.4 2012/07/10 01:57:04 jglorioso Exp $
 */
public class GroupNavigatorTag extends TagSupport
{
    private String _linkUrl;
    private String _linkClass;
    private String _activeClass;
    private String _groupParam = "group";
    private String _prevText = "&lt;&lt;&nbsp;Previous";
    private String _nextText = "Next &gt;&gt;";
    private String _prevDelimiter;
    private String _nextDelimiter;
    private int _numPageLinks = 10;
    GroupNavigator _navigator;

    public void setLinkUrl(String url)
    {
        _linkUrl = url;
    }

    public void setLinkClass(String c)
    {
        _linkClass = c;
    }

    public void setActiveClass(String c)
    {
        _activeClass = c;
    }

    public void setNumPageLinks(int numPageLinks)
    {
        _numPageLinks = numPageLinks;
    }

    public void setNumPageLinks(String numPageLinks)
    {
        try
        {
            _numPageLinks = Integer.parseInt(numPageLinks);
        }
        catch (NumberFormatException nfe) { }
    }

    public void setGroupParam(String p)
    {
        _groupParam = p;
    }

    public void setPrevText(String prev)
    {
        if (prev == null) prev = "";
        if ( !prev.trim().equals("") ) _prevText = prev;
    }

    public void setNextText(String next)
    {
        if (next == null) next = "";
        if ( !next.trim().equals("") ) _nextText = next;
    }

    public void setPrevDelimiter(String delim)
    {
        _prevDelimiter = delim;
    }

    public void setNextDelimiter(String delim)
    {
        _nextDelimiter = delim;
    }

    public void setNavigator(GroupNavigator nav)
    {
        _navigator = nav;
    }

    /**
     * Writes out the links depending on where we are in the navigator.
     *
     * @return either a EVAL_BODY or a SKIP_BODY
     */
    public int doStartTag() throws JspException
    {
        StringBuffer ret = new StringBuffer();
        if (_navigator.getGroupCount() <= 1) return SKIP_BODY;

        boolean jsFunc = ( _linkUrl != null && _linkUrl.startsWith("javascript:") );
        //If function, remove the last ) to insert the group to navigate to as the last arg
        if (jsFunc)
        {
            int idx = _linkUrl.lastIndexOf(")");
            if (idx > -1) _linkUrl = _linkUrl.substring(0, idx);
        }
        if ( _navigator.hasPreviousGroup() ) writeLink(ret, jsFunc, _navigator.getCurrentGroupIndex()-1, _prevText, _prevDelimiter);

        int start = _navigator.getCurrentGroupIndex();
        int end = start + _numPageLinks;
        int count = _navigator.getGroupCount();
        if (end > count)
        {
            end = count;
            start = end - _numPageLinks;
        }
        if (start < 0) start = 0;
        if (start > 0) writeLink(ret, jsFunc, 0, "1", (start>1 ? "..." : "")+"&nbsp;");
        for (int i=start; i<end; i++)
        {
            if (_navigator.getCurrentGroupIndex() != i)
            {
                writeLink(ret, jsFunc, i, String.valueOf((i+1)) );
            }
            else
            {
                if (_activeClass != null) ret.append("<span class=\"").append(_activeClass).append("\">");
                ret.append( (i+1) );
                if (_activeClass != null) ret.append("</span>");
                ret.append("&nbsp;");
            }
        }

        if (end < count) writeLink( ret, jsFunc, count-1, "..."+(count-1) );
        if ( _navigator.hasNextGroup() )
        {
            if (_nextDelimiter != null) ret.append(_nextDelimiter);
            writeLink(ret, jsFunc, _navigator.getCurrentGroupIndex()+1, _nextText, "");
        }

        try
        {
            pageContext.getOut().print(ret);
        }
        catch(IOException e)
        {
              throw new JspException( "Could not write out navigator tag: " + e.getMessage() );
        }
        return SKIP_BODY;
    }

    private void writeLink(StringBuffer ret, boolean jsFunc, int group, String txt)
    {
        writeLink(ret, jsFunc, group, txt, null);
    }

    private void writeLink(StringBuffer ret, boolean jsFunc, int group, String txt, String postLinkText)
    {
        ret.append("<a href=\"");
        if (_linkUrl != null)
        {
            ret.append(_linkUrl);
            if (!jsFunc)
            {
                if (_linkUrl.indexOf("?") < 0) ret.append("?");
                else ret.append("&");
            }
        }
        else
        {
            ret.append("?");
        }
        if (jsFunc) ret.append(group).append(")");
        else ret.append(_groupParam).append("=").append(group);
        ret.append("\"");
        if (_linkClass != null) ret.append(" class=\"").append(_linkClass).append("\"");
        ret.append(">").append(txt).append("</a>").append( (postLinkText != null ? postLinkText : "&nbsp;") );
    }
}
