package com.zitego.web.search;

/**
 * <p>This is a very generic class for searching from web pages. The search form and results
 * are also very generic. Basically, all search pages have a form and results. The results
 * are in a tabular fashion with sortable column headers. Each html search will have a name
 * that identifies it to any outside class that is using it.</p>
 *
 * <p>The search form information includes an action that should point to the HtmlSearchServlet
 * class. The name described above will be used as the search= parameter to identify it. The
 * form has an optional search pane title. The form itself has one or more elements to it.</p>
 *
 * <p>Search results have an optional "New Element" link that is used to go to the element
 * being searched new element edit screen. This link simply has an href and either text or
 * an image. The table of search results will alternate colors. Default is #ffffff (white) and
 * #efefef (light gray).</p>
 *
 * <p>Search results have to have a number of columns. They are defined by a label (column header),
 * a format class (optional), and whether the column is a link. If it is, then the href needs
 * to be defined and what columns from the result set (if any) need to be substituted into it.</p>
 *
 * <p>Finally, the most important part of the search is the sql. The sql statement will define
 * how to perform the search. The sql only need go to the where clause or to the joins if
 * there are more then one table. For example, if the sql is from one table, it may look like:<br>
 * <code>SELECT id, name FROM user</code><br>
 * The search parameters will be appended if there are any.
 *
 * @author John Glorioso
 * @version $Id: HtmlSearch.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see HtmlSearchForm
 * @see HtmlSearchFormElement
 */
public class HtmlSearch
{
    //TO DO - finish this and replace search pages
    /*
Design:
- search name (this is also the key and use for the sort url base)
- action
- search pane title
- search pane form elements
 - type
 - name (request param name)
 - size
 - field label
 - js validate re
 - error message
 - required?
 - value type (int, long, double, String)
 - associated column name
- New Link
 - href root
 - link text or link image
- color1 and color2
- link columns
 - href
 - params from result set
- columns
 - label
 - format
- sql (up to the WHERE)
- object params
 -


SearchResultColumn:
- column name from the select
- associated request param (if any)
- format (if any)
- label
- href (if any)

SearchConstraintColumn:
- associated request param (if any)
- table column name
- table alias
- type
*/
}