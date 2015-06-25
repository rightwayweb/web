package com.zitego.web.dropDown;

import com.zitego.markup.html.tag.form.Option;
import com.zitego.sql.DBHandle;
import com.zitego.sql.PreparedStatementSupport;
import com.zitego.util.Constant;
import java.util.Vector;
import java.sql.*;

/**
 * This is a class for creating a drop down out of a sql statement that is cached.
 * The drop down can be refreshed by calling refresh.
 *
 * @author John Glorioso
 * @version $Id: CachedDatabaseDropDown.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class CachedDatabaseDropDown extends DatabaseDropDown
{
    /** The sql statement. */
    protected String _sql;
    /** The array of Objects to set as parameters in the sql. */
    protected Object[] _params = new Object[0];

    /**
     * Creates a new drop down. This is for the clone method so that a db call is not made.
     *
     * @param String The form element name.
     * @param String The sql statement.
     * @param Object[] The statement parameters.
     */
    private CachedDatabaseDropDown(String name, String sql, Object[] params)
    {
        super(name, (String)null, (String)null);
        _sql = sql;
        _params = params;
    }


    /**
     * Creates a new set of options with a name, a sql statement,
     * an array of values, and a DBHandle. This assumes that the option
     * value is the first parameter and that the option text is the
     * second. It will pull the first based on the getLong method and
     * the second on getString.
     *
     * @param String The form element name.
     * @param String The sql statement.
     * @param Object[] The statement parameters.
     * @param DBHandle The database handle to use for the query.
     * @throws SQLException if a database error occurs.
     */
    public CachedDatabaseDropDown(String name, String sql, Object[] params, DBHandle db) throws SQLException
    {
        this(name, sql, params, db, null, null, null);
    }

    /**
     * Creates a new set of options with a name, a sql statement,
     * an array of values, a DBHandle, and selected value.
     * This assumes that the option value is the first parameter and
     * that the option text is the second. It will pull the first based
     * on the getLong method and the second on getString.
     *
     * @param String The form element name.
     * @param String The sql statement.
     * @param Object[] The statement parameters.
     * @param DBHandle The database handle to use for the query.
     * @param String The selected value.
     * @throws SQLException if a database error occurs.
     */
    public CachedDatabaseDropDown(String name, String sql, Object[] params, DBHandle db, String sel) throws SQLException
    {
        this(name, sql, params, db, null, null, sel);
    }

    /**
     * Creates a new set of options with a name, a sql statement,
     * an array of values, a DBHandle, first option label, first option value,
     * and the selected value. This assumes that the option value is the
     * first parameter and that the option text is the second. It will pull
     * the first based on the getLong method and the second on getString. You
     * can set a label for the first option. If the first option is null, then
     * it will not be set. This also sets the option to select.
     *
     * @param String The form element name.
     * @param String The sql statement.
     * @param Object[] The statement parameters.
     * @param DBHandle The database handle to use for the query.
     * @param String The label for the first option.
     * @param String The value for the first option.
     * @param String The selected value.
     * @throws SQLException if a database error occurs.
     */
    public CachedDatabaseDropDown(String name, String sql, Object[] params, DBHandle db, String firstOpt,
                                  String firstVal, String sel)
    throws SQLException
    {
        super(name, firstOpt, firstVal);

        if (sql == null) throw new IllegalArgumentException("sql cannot be null");
        _sql = sql;
        _params = params;

        refresh(db, sel);
    }

    /**
     * Runs the sql statement and populates the drop down. It first clears the dropdown, then
     * re-inserts the first option if there is one.
     *
     * @param DBHandle The handle to use.
     * @param String The value to select.
     * @throws SQLException if a database error occurs.
     */
    public void refresh(DBHandle db, String sel) throws SQLException
    {
        db.connect();
        try
        {
            PreparedStatementSupport supp = new PreparedStatementSupport();
            if (_params != null)
            {
                for (int i=0; i<_params.length; i++)
                {
                    supp.add(_params[i]);
                }
            }
            supp.setSql(_sql);
            PreparedStatement pst = supp.bindValues(db);
            Option first = getFirstOption();
            clearOptions();
            if (first != null)
            {
                addBodyContent(first);
                addOption(first);
            }
            storeResultSet( pst.executeQuery() );
            if (sel != null) setSelected(sel);
        }
        finally
        {
            db.disconnect();
        }
    }

    protected DropDown createInstance()
    {
        return new CachedDatabaseDropDown(getName(), _sql, _params);
    }
}