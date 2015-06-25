package com.zitego.web.servlet;

import com.zitego.util.FormattedConstant;
import com.zitego.util.SortColumn;
import com.zitego.report.DataSetCollection;
import com.zitego.sql.DBHandle;
import javax.servlet.http.*;
import javax.servlet.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;
import java.util.Hashtable;

/**
 * All reporting jsp pages need to extend this servlet. It is unfortunately tied to
 * the tomcat servlet container at the moment until a better solution can be implemented.
 * The reason is because it needs to extend the tomcat implemented jsp base class.
 *
 * Ex (in jsp page):<br>
 * <code>
 * <pre>
 * &lt;%@ page extends="com.zitego.servlet.ReportServlet"&gt;
 * &lt;%@ taglib uri="/manager.tld" prefix="mgr" %&gt;
 * &lt;mgr:sort sortable="&lt;%= handleCollection(FKEY, request, usr.getDBHandle() ) %&gt;" cols="&lt;%= getSortColumns(FKEY) %&gt;" />
 * .
 * .
 * <i>Display the report table here by looping through the dataset collection...</i>
 * .
 * .
 * &lt;%!
 * private static SimpleDateFormat _sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
 * private static final FormattedConstant ID = new FormattedConstant("Id", 0);
 * private static final FormattedConstant NAME = new FormattedConstant("Name", 1);
 * private static final FormattedConstant CREATION_DATE = new FormattedConstant("Creation Date", 2, _sdf);
 * private static final FormattedConstant LAST_UPDATED = new FormattedConstant("Last Updated", 3, sdf);
 * private static final String FKEY = "user/users.jsp";
 * static
 * {
 *     registerPage
 *     (
 *         FKEY,
 *         new String[] { "id", "name" },
 *         new FormattedConstant[] { ID },
 *         new FormattedConstant[] { NAME, CREATION_DATE, LAST_UPDATED }
 *     );
 * }
 * public void jspDestroy() { clearFields(FKEY); }
 * protected DataSetCollection getData(DBHandle db, String[] args) throws SQLException
 * {
 *     DataSetCollection ret = new DataSetCollection();
 *     String id = args[0], name = args[1];
 *     if (id == null && name == null) return ret;
 *     StringBuffer sql = new StringBuffer();
 *     PreparedStatementSupport support = new PreparedStatementSupport();
 *     if (!"".equals(id) && id != null)
 *     {
 *         sql.append(" AND id = ?");
 *         support.add( Long.parseLong(id) );
 *     }
 *     if (!"".equals(name) && name != null)
 *     {
 *         sql.append(" AND name LIKE ?");
 *         support.add("%"+name+"%");
 *     }
 *     db.connect();
 *     try
 *     {
 *         sql.insert(0, "SELECT id, name, creation_date, last_updated FROM user");
 *         support.setSql(sql);
 *         PreparedStatement pst = support.bindValues(db);
 *         ResultSet rs = pst.executeQuery();
 *         while ( rs.next() )
 *         {
 *             DataSet ds = new DataSet();
 *             ds.put( ID, rs.getLong("id") );
 *             ds.put( NAME, rs.getString("name") );
 *             ds.put( CREATION_DATE, rs.getTimestamp("creation_date") );
 *             ds.put( LAST_UPDATED, rs.getTimestamp("last_updated") );
 *             ret.add(ds);
 *         }
 *     }
 *     finally
 *     {
 *         db.disconnect();
 *     }
 *     return ret;
 * }
 * %&gt;
 * </pre>
 * </code>
 *
 * @author John Glorioso
 * @version $Id: ReportServlet.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class ReportServlet extends org.apache.jasper.runtime.HttpJspBase
{
	/** All fields of all extending pages. */
	protected static Hashtable _fields = new Hashtable();
	/** The viewable fields in the report of all the fields. */
	protected static Hashtable _viewableFields = new Hashtable();
	/** The sort columns for the viewable fields. */
	protected static Hashtable _sortColumns = new Hashtable();
	/** The request fields to pass to the getDataMethod. */
	protected static Hashtable _requestParams = new Hashtable();

	/**
	 * Registers a new page by providing a key, request parameters to pass to the defined getData method,
	 * non viewable report fields, and viewable report fields. This creates the sort columns as well. This
	 * will throw an IllegalArgumentException if the key is not unique.
	 *
	 * @param String The key for the fields to add to.
	 * @param String[] The request fields to pass to the getData method.
	 * @param FormattedConstant[] The non viewable fields.
	 * @param FormattedConstant[] The viewable fields.
	 * @throws IllegalArgumentException if a page with the given alias has already been registered or
	 *                                  the key or visible fields are null.
	 */
	protected static void registerPage(String key, String[] params, FormattedConstant[] invisibleFields,
	                                   FormattedConstant[] visibleFields)
    throws IllegalArgumentException
	{
	    if (key == null) throw new IllegalArgumentException("Key cannot be null");
	    if (_fields.get(key) != null) throw new IllegalArgumentException(key + " has already been registered");
	    if (visibleFields == null) throw new IllegalArgumentException("Visible fields cannot be null");

		Vector fields = new Vector();
		Vector vfields = new Vector();

		//Add the nonviewable fields
		if (invisibleFields == null) invisibleFields = new FormattedConstant[0];
		for (int i=0; i<invisibleFields.length; i++)
		{
		    fields.add(invisibleFields[i]);
		}
		//Add the viewable fields
		Vector tmp = new Vector();
		for (int i=0; i<visibleFields.length; i++)
		{
		    fields.add(visibleFields[i]);
		    vfields.add(visibleFields[i]);
		    tmp.add( new SortColumn(visibleFields[i]) );
		}
		_fields.put(key, fields);
		_viewableFields.put(key, vfields);

		//Add the request params
		if (params == null) params = new String[0];
		_requestParams.put(key, params);

		//Create the sortable columns
		SortColumn[] cols = new SortColumn[tmp.size()];
		tmp.copyInto(cols);
		_sortColumns.put(key, cols);
	}

	/**
	 * Evaluates the field based on its id and the specified key.
	 *
	 * @param String The key.
	 * @param int The id.
	 * @return FormattedConstant
	 * @throws IllegalArgumentException if the key does not exist.
	 */
	protected static FormattedConstant evaluate(String key, int val) throws IllegalArgumentException
	{
	    if (key == null) throw new IllegalArgumentException("Key cannot be null");
		Vector fields = (Vector)_fields.get(key);
		if (fields == null) throw new IllegalArgumentException("Invalid key: "+key);

		int count = fields.size();
		for (int i=0; i<count; i++)
		{
			FormattedConstant c = (FormattedConstant)fields.elementAt(i);
			if (c.getValue() == val) return c;
	    }
		return null;
	}

	/**
	 * An abstract method that all jsp pages need to declare that will build and return
	 * a DataSetCollection based on the arguments passed in.
	 *
	 * @param DBHandle The database handle to use for querying.
	 * @param String[] Any required request parameters.
	 * @param Object[] Any additional arguments needed.
	 * @return DataSetCollection
	 * @throws SQLException
	 */
	protected abstract DataSetCollection getData(DBHandle db, String[] args, Object[] additionalArgs) throws SQLException;

	/**
	 * Builds and/or sorts the collection based on the values in the request object and the key. If
	 * a DataSetCollection object is not in the session stored as the "manager.dsc" attribute, then
	 * it will call getData.
	 *
	 * @param String The key.
	 * @param DBHandle The database handle to use.
	 * @param HttpServletRequest The request object.
	 * @return DataSetCollection
	 * @throws SQLException if an error occurs running the sql.
	 * @throws IllegalArgumentException if the key is not valid.
	 */
	protected DataSetCollection handleCollection(String key, DBHandle db, HttpServletRequest request)
	throws SQLException, IllegalArgumentException
	{
	    return handleCollection(key, db, request, null);
	}

	/**
	 * Builds and/or sorts the collection based on the values in the request object and the key. If
	 * a DataSetCollection object is not in the session stored as the "manager.dsc" attribute, then
	 * it will call getData.
	 *
	 * @param String The key.
	 * @param DBHandle The database handle to use.
	 * @param HttpServletRequest The request object.
	 * @param Object[] Any additional arguments needed.
	 * @return DataSetCollection
	 * @throws SQLException if an error occurs running the sql.
	 * @throws IllegalArgumentException if the key is not valid.
	 */
	protected DataSetCollection handleCollection(String key, DBHandle db, HttpServletRequest request, Object[] additionalArgs)
	throws SQLException, IllegalArgumentException
	{
	    if (key == null || _viewableFields.get(key) == null) throw new IllegalArgumentException("Invalid key: "+key);
	    HttpSession session = request.getSession(true);
		DataSetCollection dsc = (DataSetCollection)session.getAttribute("manager.dsc");
		if (dsc == null || request.getParameter("col") == null)
		{
		    session.removeAttribute("manager.dsc");
		    String[] params = (String[])_requestParams.get(key);
		    String[] args = new String[params.length];
		    for (int i=0; i<args.length; i++)
		    {
		        args[i] = request.getParameter(params[i]);
		    }
			dsc = getData(db, args, additionalArgs);
			if (dsc.size() > 0) session.setAttribute("manager.dsc", dsc);
		}
		return dsc;
	}

	/**
	 * Returns the viewable fields for the specified key.
	 *
	 * @param String The key.
	 * @return Vector
	 * @throws IllegalArgumentException if the key is null or does not exist.
	 */
	protected Vector getViewableFields(String key) throws IllegalArgumentException
	{
	    if (key == null) throw new IllegalArgumentException("Invalid key: "+key);
		Vector fields = (Vector)_viewableFields.get(key);
		if (fields == null) throw new IllegalArgumentException("Invalid key: " + key);
		return fields;
	}

	/**
	 * Returns all fields for the specified key.
	 *
	 * @param String The key.
	 * @return Vector
	 * @throws IllegalArgumentException if the key is null or does not exist.
	 */
	protected Vector getFields(String key) throws IllegalArgumentException
	{
	    if (key == null) throw new IllegalArgumentException("Invalid key: "+key);
		Vector fields = (Vector)_fields.get(key);
		if (fields == null) throw new IllegalArgumentException("Invalid key: " + key);
		return fields;
	}

	/**
	 * Returns the sort columns.
	 *
	 * @param String The key of columns to retrieve.
	 * @return SortColumn[]
	 */
	public SortColumn[] getSortColumns(String key)
	{
	    return (SortColumn[])_sortColumns.get(key);
	}

	/**
	 * Clears the fields for the specified key.
	 *
	 * @param String The key.
	 * @throws IllegalArgumentException if the key is null.
	 */
	protected void clearFields(String key) throws IllegalArgumentException
	{
	    if (key == null) throw new IllegalArgumentException("Invalid key: "+key);
		_fields.remove(key);
		_viewableFields.remove(key);
		_sortColumns.remove(key);
		_requestParams.remove(key);
	}

	/**
	 * This method must be overridden in the jsp page to call clearFields(key) with the name
	 * of the key that you made for your page. This is for proper recompiling while still enforcing
	 * unique key names.
	 */
	public abstract void jspDestroy();
}