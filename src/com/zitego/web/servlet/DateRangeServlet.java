package com.zitego.web.servlet;

import com.zitego.util.DateRange;
import com.zitego.util.DateRangeType;
import com.zitego.util.DateRangeFactory;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet handles creating a DateRange to be used by the target page. In order for this
 * servlet to function properly, it needs the following four parameters to be passed as part of the
 * request:
 * <ul>
 *   <li>date_range_type - Contains a valid value to evaluate into {@link com.zitego.util.DateRangeType}
 *   <li>custom_date_page - Contains the name of the JSP file to use to allow the user to enter a custom date
 *   <li>target - After the user enters a date (either predefined or custom ) the JSP/Servlet to send the user
 *   <li>date_range_prop - The name of the request attribute containing the {@link com.zitego.util.DateRange} object
 * </ul>
 * <p>
 *
 * @author John Glorioso
 * @version $Id: DateRangeServlet.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class DateRangeServlet extends HttpServlet
{
    /**
     * Creates and stores the date range.
     *
     * @param request The request.
     * @param response The response.
     * @throws ServletException if an error occurs.
     * @throws IOException if an error occurs
     */
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String timezoneParam = request.getParameter("date_range_tz");
        if (timezoneParam == null || timezoneParam.length() == 0) timezoneParam = "GMT";

        DateRangeType filterType = DateRangeType.NONE;
        GregorianCalendar gc = new GregorianCalendar( TimeZone.getTimeZone(timezoneParam) );
        Date date = new Date();

        filterType = DateRangeType.evaluate( Integer.parseInt(request.getParameter( "date_range_type" )) );

        DateRange dateRange;
        if (filterType == DateRangeType.CUSTOM)
        {
            String smon = request.getParameter("start_month"),
                   sday = request.getParameter("start_day"),
                   syear = request.getParameter("start_year"),
                   emon = request.getParameter("end_month"),
                   eday = request.getParameter("end_day"),
                   eyear = request.getParameter("end_year");

            if (smon != null && sday != null && syear != null && emon != null && eday != null && eyear != null)
            {
                GregorianCalendar start_gc = new GregorianCalendar( TimeZone.getTimeZone(timezoneParam) );
                start_gc.set( Calendar.MONTH, Integer.parseInt(smon) );
                start_gc.set( Calendar.DATE, Integer.parseInt(sday) );
                start_gc.set( Calendar.YEAR, Integer.parseInt(syear) );
                String start_hour = request.getParameter("start_hour24");
                int iStartHour = 0;
                if (start_hour != null) iStartHour = Integer.parseInt(start_hour);
                start_gc.set(Calendar.HOUR_OF_DAY, iStartHour);
                start_gc.set(Calendar.MINUTE, 0);
                start_gc.set(Calendar.SECOND, 0);

                GregorianCalendar end_gc = new GregorianCalendar( TimeZone.getTimeZone(timezoneParam) );
                end_gc.set( Calendar.MONTH, Integer.parseInt(emon) );
                end_gc.set( Calendar.DATE, Integer.parseInt(eday) );
                end_gc.set( Calendar.YEAR, Integer.parseInt(eyear) );
                String end_hour = request.getParameter("end_hour24");
                int iEndHour = 0;
                if (end_hour != null) iEndHour = Integer.parseInt(end_hour);
                end_gc.set(Calendar.HOUR_OF_DAY, iEndHour);
                end_gc.set(Calendar.MINUTE, 59);
                end_gc.set(Calendar.SECOND, 59);
                dateRange = DateRangeFactory.buildDateRange(start_gc, end_gc);
            }
            else
            {
                getServletContext().getRequestDispatcher( request.getParameter("custom_date_page") ).forward(request, response);
                return;
            }
        }
        else
        {
            dateRange = DateRangeFactory.getDateRange( filterType, TimeZone.getTimeZone(timezoneParam) );
        }

        String url = request.getParameter("target");
        String propName = request.getParameter("date_range_prop");

        request.setAttribute(propName, dateRange);

        getServletContext().getRequestDispatcher(url).forward(request, response);
    }
}