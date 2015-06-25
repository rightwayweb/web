package com.zitego.web.jsp;

import javax.servlet.jsp.tagext.*;
import java.util.Vector;

/**
 * Handles variable information for setting iterated objects into the page context.
 * Use getType(String) to convert a primitive string name into an object class type.
 *
 * @author John Glorioso
 * @version $Id: IterateTagExtraInfo.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class IterateTagExtraInfo extends TagExtraInfo
{
    public VariableInfo[] getVariableInfo(TagData data)
	{
	    Vector items = new Vector();
		items.add
		(
		    new VariableInfo
		    (
		        data.getId(),
		        getType( (String)data.getAttribute("type") ),
		        true,
		        VariableInfo.NESTED
		    )
		);
		if (data.getAttribute("nextColorId") != null)
		{
		    items.add
		    (
    		    new VariableInfo
    		    (
    		        (String)data.getAttribute("nextColorId"),
    		        "java.lang.String",
    		        true,
    		        VariableInfo.AT_BEGIN
    		    )
    		);
        }
        if (data.getAttribute("countId") != null)
        {
            items.add
            (
    		    new VariableInfo
    		    (
    		        (String)data.getAttribute("countId"),
    		        "java.lang.Integer",
    		        true,
    		        VariableInfo.AT_BEGIN
    		    )
    		);
		}

		VariableInfo arr[] = new VariableInfo[items.size()];
		items.copyInto(arr);

		return arr;
	}

    public static String getType(String type)
    {
        if (type != null && type.indexOf(".") == -1)
	    {
	        type = type.toLowerCase();
    	    if ( "int".equals(type) ) return "java.lang.Integer";
    	    else if ( "double".equals(type) ) return "java.lang.Double";
    	    else if ( "short".equals(type) ) return "java.lang.Short";
    	    else if ( "long".equals(type) ) return "java.lang.Long";
    	    else if ( "float".equals(type) ) return "java.lang.Float";
    	    else if ( "byte".equals(type) ) return "java.lang.Byte";
    	    else if ( "boolean".equals(type) ) return "java.lang.Boolean";
    	    else if ( "char".equals(type) ) return "java.lang.Character";
    	    else return type;
    	}
    	else
    	{
    	    return type;
    	}
    }
}