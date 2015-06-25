package com.zitego.web.jsp;

import com.zitego.markup.html.tag.Anchor;
import com.zitego.format.FormatType;
import com.zitego.util.Sortable;
import com.zitego.style.ServerStyleSheet;
import java.io.IOException;
import javax.servlet.jsp.JspException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * This handles writing out the column header for a sortable column. The column header
 * is a table cell within a table row. The cell has a url, a text label, a sort column,
 * a sort direction, and an optional image directory. The image directory defaults to
 * request.getContextPath() + "/images" if not specified. The two images used are up.gif
 * and down.gif. This uses Sortable.ASCENDING and Sortable.DESCENDING for sort direction.
 *
 * @author John Glorioso
 * @version $Id: SortableColumnHeaderTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class SortableColumnHeaderTag extends TagSupport
{
    /** The url. */
      private String _url;
      /** The sort column id. */
      private int _sortCol = -1;
      /** The column passed in the request as col= */
      private int _col = -100;
      /** The direction passed in the request as dir= */
      private int _dir = 1;
      /** The text label. */
      private String _label;
      /** The image directory. */
      private String _imageDir;
      /** The attribute string for the td tag. */
      private String _tdAttributes;
      /** The style to use in place of the td tag. */
      private String _style;
      /** The class attribute to use for the td tag. */
      private String _classAttribute;
      /** The style to use for link text. */
      private String _linkStyle;
      /** The class attribute to use for the link tag. */
      private String _linkClassAttribute;
      private boolean _useSpan = false;
      private String _spanAttributes;
      private String _preAnchorMarkup = "";
      private String _postAnchorMarkup = "";

    /**
     * Sets the url.
     *
     * @param url The url.
     */
    public void setUrl(String url)
    {
        _url = url;
    }

    /**
     * Sets the sort column.
     *
     * @param col The sort column id.
     */
    public void setSortCol(int col)
    {
        _sortCol = col;
    }

    /**
     * Sets the image directory.
     *
     * @param dir The image directory.
     */
    public void setImageDir(String dir)
    {
        _imageDir = dir;
    }

    /**
     * Sets the text label.
     *
     * @param label The text label.
     */
    public void setLabel(String label)
    {
        _label = label;
    }

    /**
     * Sets the td attributes.
     *
     * @param attr The td attribute string.
     */
    public void setTdAttributes(String attr)
    {
        _tdAttributes = attr;
    }

    /**
     * Sets the serverside style.
     *
     * @param style The style.
     */
    public void setStyle(String style)
    {
        _style = style;
    }

    /**
     * Sets the client side td style by way of class attribute.
     *
     * @param style The class attribute.
     */
    public void setClassAttribute(String style)
    {
        _classAttribute = style;
    }

    /**
     * Sets the serverside link text style.
     *
     * @param style The style.
     */
    public void setLinkStyle(String style)
    {
        _linkStyle = style;
    }

    /**
     * Sets the client side link text style by way of class attribute.
     *
     * @param style The class attribute.
     */
    public void setLinkClassAttribute(String style)
    {
        _linkClassAttribute = style;
    }

    /**
     * Sets whether to use a span tag or a td tag.
     *
     * @param span Whether to use the span tag.
     */
    public void setUseSpan(String span)
    {
        setUseSpan( new Boolean(span).booleanValue() );
    }

    /**
     * Sets whether to use a span tag or a td tag.
     *
     * @param span Whether to use the span tag.
     */
    public void setUseSpan(boolean span)
    {
        _useSpan = span;
    }

    /**
     * Sets the span attributes.
     *
     * @param span The span attribute string.
     */
    public void setSpanAttributes(String span)
    {
        _spanAttributes = span;
    }

    /**
     * Sets the pre-anchor markup. This is markup that will appear after the
     * span or td tag and before the anchor tag.
     *
     * @param pre The pre anchor markup.
     */
    public void setPreAnchorMarkup(String pre)
    {
        if (pre == null) pre = "";
        _preAnchorMarkup = pre;
    }

    /**
     * Sets the post-anchor markup. This is markup that will appear after the
     * anchor tag and before the td or span tag.
     *
     * @param post The post anchor markup.
     */
    public void setPostAnchorMarkup(String post)
    {
        if (post == null) post = "";
        _postAnchorMarkup = post;
    }

    public int doStartTag() throws JspException
    {
        try
        {
            ServletRequest request = pageContext.getRequest();
            String arg = request.getParameter("col");
            _col = -100;
            if (arg == null) arg = (String)request.getAttribute("col");
            if (arg != null) _col = Integer.parseInt(arg);

            arg = request.getParameter("dir");
            if (arg == null) arg = (String)request.getAttribute("dir");
            _dir = 1;
            if (arg != null)
            {
                if (Integer.parseInt(arg) > 0) _dir = Sortable.ASCENDING;
                else _dir = Sortable.DESCENDING;
            }

            StringBuffer output = new StringBuffer();
            if (_style != null)
            {
                if (_tdAttributes != null) output.append( ServerStyleSheet.getHTMLStartTag(_style, _tdAttributes) );
                else output.append( ServerStyleSheet.getHTMLStartTag(_style) );
            }
            else if (_useSpan)
            {
                output.append("<span").append( (_spanAttributes != null ? " "+_spanAttributes : "") )
                      .append( (_classAttribute != null ? " class=\""+_classAttribute+"\"" : "") ).append(">");
            }
            else
            {
                output.append("<td").append( (_tdAttributes != null ? " "+_tdAttributes : "") )
                      .append( (_classAttribute != null ? " class=\""+_classAttribute+"\"" : "") ).append(">");
            }
            output.append(_preAnchorMarkup);
            Anchor link = new Anchor();
            link.setIsOnOwnLine(true);
            if (_url == null) _url = request.getParameter("d");
            if (_url == null) _url = "";
            //_url looks like either
            // javascript:transport('/somewhere/someplace') or
            // javascript:transport('/somewhere/someplace', '?id=2') or
            // /someUrl or
            // /someUrl?id=2
            int index = _url.indexOf("?");
            String params = "?";
            if (index > -1)
            {
                params += _url.substring(index+1);
                _url = _url.substring(0, index);
                //Has a query string. See if they are a transport url or not
                if (_url.indexOf("javascript:transport") == 0)
                {
                    //That means _url looks like javascript:transport('/somewhere/someplace', '
                    //and params looks like id=2')
                    index = _url.lastIndexOf("', '");
                    if (index > -1) _url = _url.substring(0, index);
                    index = params.lastIndexOf("')");
                    if (index > -1) params = params.substring(0, index);
                    _url += "', '" + params + "&col=" + _sortCol + "&dir=" + (_dir*-1) + "')";
                }
                else
                {
                    _url += params + "&col=" + _sortCol + "&dir=" + (_dir*-1);
                }
            }
            else
            {
                //No query string. See if they are a transport url or not
                if (_url.indexOf("javascript:transport") == 0)
                {
                    //That means _url looks like javascript:transport('/somewhere/someplace')
                    index = _url.lastIndexOf("')");
                    if (index > -1) _url = _url.substring(0, index);
                    _url += "', '?col=" + _sortCol + "&dir=" + (_dir*-1) + "')";
                }
                else if (_url.indexOf("javascript:") == 0)
                {
                    //That means _url looks like javascript:someFunc()
                    index = _url.lastIndexOf(")");
                    _url = _url.substring(0, index) + (_url.charAt(index-1) == '(' ? "" : ", ") + _sortCol + "," + (_dir*-1) + ")";
                }
                else
                {
                    _url += "?col=" + _sortCol + "&dir=" + (_dir*-1);
                }
            }
            link.setHref(_url);
            link.setToolTip("Sort "+(_dir<0?"As":"Des")+"cending");
            if (_linkClassAttribute != null) link.setClassAttribute(_linkClassAttribute);

            output.append( link.getStartTag(FormatType.HTML) );
            if (_linkStyle != null) output.append( ServerStyleSheet.getHTMLStartTag(_linkStyle) );

            pageContext.getOut().print( output.toString() );

        }
        catch(Exception e)
        {
            throw new JspException( "An error occurred writing out the tag" + e.getMessage() );
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspException
    {
        try
        {
            StringBuffer output = new StringBuffer()
                .append( (_label != null ? _label : "") );
            if (_linkStyle != null) output.append( ServerStyleSheet.getHTMLEndTag(_linkStyle) );
            output.append("</a>");

            if (_sortCol == _col)
            {
                if (_imageDir == null) _imageDir = ( (HttpServletRequest)pageContext.getRequest() ).getContextPath() + "/images";
                output.append("&nbsp;&nbsp;&nbsp;<img src=\"").append(_imageDir)
                      .append("/").append( (_dir > 0 ? "up" : "down") ).append(".gif\">");
            }
            output.append(_postAnchorMarkup);
            if (_useSpan) output.append("</span>");
            else output.append("</td>");
            pageContext.getOut().print( output.toString() );

        }
        catch(java.io.IOException e)
        {
              throw new JspException("IOException: " + e.getMessage());
        }
        return EVAL_PAGE;
    }
}