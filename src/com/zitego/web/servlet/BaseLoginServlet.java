package com.zitego.web.servlet;

import com.zitego.util.InvalidLoginException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Logs the user into the zitego mail application.
 *
 * @author John Glorioso
 * @version $Id: BaseLoginServlet.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public abstract class BaseLoginServlet extends BaseServlet
{
    /**
     * This method creates a new session, then calls checkRequiredFields and createUserObject.
     * If an InvalidLoginException is thrown by either of these two methods, the exception
     * is stored in the request and the user is passed on to the page returned by getInvalidLoginPage().
     * If the two methods succeed, then we go to the page returned by getSuccessPage().
     *
     * @param HttpServletRequest The request object.
     * @param HttpServletResponse The response object.
     * @throws IOException if an io error occurs.
     * @throws ServletException if an unexpected servlet exception occurs.
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        //Get a clean session object
        HttpSession session = request.getSession(true);
        session.invalidate();
        session = request.getSession(true);

        try
        {
            if ( checkRequiredFields(request) )
            {
                createUserObject(request, session);
                setCookies(request, response);
                gotoPage(getSuccessPage(), request, response);
            }
            else
            {
                gotoPage(getInvalidLoginPage(), request, response);
            }
            return;
        }
        catch (InvalidLoginException ile)
        {
            gotoPage(getInvalidLoginPage(), ile, request, response);
            return;
        }
        catch (Exception e)
        {
            gotoPage(getErrorPage(), e, request, response);
            return;
        }
    }

    /**
     * Needs to be defined to check that all required fields are present in the request.
     * If there are missing or invalid values, then this method should throw an exception
     * detailing the error. If you prefer the client to be referred to the invalid login
     * page without an error message, then just return false. If everything looks good,
     * then return true.
     *
     * @param HttpServletRequest The request object.
     * @return boolean
     * @throws InvalidLoginException if values are missing or invalid.
     */
    protected abstract boolean checkRequiredFields(HttpServletRequest request) throws InvalidLoginException;

    /**
     * This method should define how to create and store the user object in the session.
     * If an error occurs creating this object for known reasons (such as invalid authentication)
     * and InvalidLoginException should be thrown.
     *
     * @param HttpServletRequest The request object.
     * @param HttpSession The session object.
     * @throws InvalidLoginException if an error logging the user in.
     * @throws Exception if an unexpected exception occurs.
     */
    protected abstract void createUserObject(HttpServletRequest request, HttpSession session)
    throws InvalidLoginException, Exception;

    /**
     * This is called after createUserObject and by default does nothing. If you want to set
     * cookies, you will need to extend the method.
     *
     * @param request The request object.
     * @param response The response to write the cookie(s) to.
     */
    public void setCookies(HttpServletRequest request, HttpServletResponse response)
    {

    }

    /**
     * This should return the page to go to if an InvalidLoginException occurs.
     *
     * @return String
     */
    protected abstract String getInvalidLoginPage();

    /**
     * This should return the page to go to if everything succeeds.
     *
     * @return String
     */
    protected abstract String getSuccessPage();
}