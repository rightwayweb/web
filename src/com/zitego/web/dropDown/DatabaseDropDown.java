package com.zitego.web.dropDown;

import com.zitego.markup.html.tag.form.Option;
import com.zitego.util.Constant;
import java.util.Vector;
import java.sql.*;

/**
 * This is a class for creating a drop down out of a sql statement.
 *
 * @author John Glorioso
 * @version $Id: DatabaseDropDown.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class DatabaseDropDown extends DropDown
{
    /**
     * Creates a new set of options with a name, first option label, first
     * option value, and a selected value.
     *
     * @param String The form element name.
     * @param String The label for the first option.
     * @param String The value for the first option.
     */
    protected DatabaseDropDown(String name, String firstOpt, String firstVal)
    {
        super(name, firstOpt, firstVal);
    }

    /**
     * Creates a new set of options with a name and a result set.
     * This assumes that the option value is the first parameter and
     * that the option text is the second. It will pull the first based
     * on the getLong method and the second on getString.
     *
     * @param String The form element name.
     * @param ResultSet The result set to create the options from.
     * @throws SQLException if a database error occurs.
     */
    public DatabaseDropDown(String name, ResultSet rs) throws SQLException
    {
        this(name, rs, null, null, null);
    }

    /**
     * Creates a new set of options with a name, result set, and selected value.
     * This assumes that the option value is the first parameter and
     * that the option text is the second. It will pull the first based
     * on the getLong method and the second on getString.
     *
     * @param String The form element name.
     * @param ResultSet The result set to create the options from.
     * @param String The selected value.
     * @throws SQLException if a database error occurs.
     */
    public DatabaseDropDown(String name, ResultSet rs, String sel) throws SQLException
    {
        this(name, rs, null, null, sel);
    }

    /**
     * Creates a new set of options with a name, result set, and selected value.
     * This assumes that the option value is the first parameter and
     * that the option text is the second. It will pull the first based
     * on the getLong method and the second on getString. You can set a label for
     * the first option. If the first option is null, then it will not be set. This
     * also sets the option to select.
     *
     * @param String The form element name.
     * @param ResultSet The result set to create the options from.
     * @param String The label for the first option.
     * @param String The value for the first option.
     * @param String The selected value.
     * @throws SQLException if a database error occurs.
     */
    public DatabaseDropDown(String name, ResultSet rs, String firstOpt, String firstVal, String sel) throws SQLException
    {
        super(name, firstOpt, firstVal);
        storeResultSet(rs);
        if (sel != null) setSelected(sel);
    }

    /**
     * Runs the given result set and stores the options.
     *
     * @param ResultSet The result set to populate this drop down.
     * @throws SQLException if a database error occurs.
     */
    protected void storeResultSet(ResultSet rs) throws SQLException
    {
        if (rs != null)
        {
            while ( rs.next() )
            {
                Option opt = new Option(this);
                opt.setValue( String.valueOf(rs.getLong(1)) );
                opt.setText( rs.getString(2) );
                addOption(opt);
            }
        }
    }

    protected DropDown createInstance()
    {
        return new DatabaseDropDown(getName(), (String)null, (String)null);
    }
}