package com.zitego.web.jsp;

import com.zitego.util.SessionManager;
import com.zitego.util.SessionManagerHolder;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * Retrieves an object out of the session and stores it in the page context. This can use a SessionManager
 * to automatically clean stale objects out of the session. If one is to be used, set the mgrHolderName
 * property to a SessionManager.
 *
 * @author John Glorioso
 * @version $Id: SessionObjectTag.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class SessionObjectTag extends TagSupport
{
    /** The attribute name. */
  	private String _name;
  	/** The value. */
  	private Object _val;
  	/** The session manager attribute name to retrieve out of the session. */
  	private String _mgrHolderName;
  	/** Whether to clean the session after retrieval. Default is false. */
    private boolean _clean = false;

    /**
  	 * Set the name of the session variable to retrieve
  	 *
  	 * @param String The name.
  	 */
  	public void setName(String name)
  	{
  		_name = name;
  	}

  	/**
  	 * Returns the name of the session variable to retrieve.
  	 *
  	 * @return String
  	 */
  	protected String getName()
  	{
  	    return _name;
  	}

    /**
  	 * Sets the type of the object to retrieve.
  	 *
  	 * @param String The class name.
  	 */
  	public void setType(String type) { }

    /**
	 * Sets the value of the attribute to this.
	 *
	 * @param Object The value.
	 */
	public void setValue(Object val)
	{
	    _val = val;
	}

	/**
  	 * Set the name of the session attribute for the session manager holder.
  	 *
  	 * @param String The name.
  	 */
  	public void setMgrHolderName(String name)
  	{
  		_mgrHolderName = name;
  	}

  	/**
     * Sets whether or not to clean the session after retrieval.
     *
     * @param boolean Whether to clean.
     */
    public void setClean(boolean clean)
    {
        _clean = clean;
    }

    /**
     * Sets whether or not to clean the session after retrieval.
     *
     * @param String Whether to clean.
     */
    public void setClean(String clean)
    {
        setClean( new Boolean(clean).booleanValue() );
    }

  	/**
   	 * Stores the given id attribute from the session into the page context
   	 * and returns SKIP_BODY.
   	 *
   	 * @return int
   	 */
  	public int doStartTag() throws JspException
  	{
  		HttpSession session = pageContext.getSession();
  		Object val = null;
  		String id = getId();
  		if (_mgrHolderName != null)
  		{
  		    SessionManagerHolder holder = (SessionManagerHolder)session.getAttribute(_mgrHolderName);
            SessionManager mgr = (holder != null ? holder.getSessionManager() : null);
            if (mgr != null)
            {
                val = mgr.getAttribute(session, getName(), _clean);
                return SKIP_BODY;
            }
        }

  		if (val == null) val = session.getAttribute(_name);
  		if (val != null) pageContext.setAttribute(id, val);
  		if (_val != null) session.setAttribute(_name, _val);

   		return SKIP_BODY;
  	}
}