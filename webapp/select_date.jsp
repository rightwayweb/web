<%--
  - Displays a calendar that allows the user to select a date.
  - @version $Id: select_date.jsp,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
  --%>
<%@ page
    import="java.util.Date"
    import="java.util.GregorianCalendar"
    import="java.util.Calendar"
    import="java.text.SimpleDateFormat"
    errorPage="/error_full.jsp"
%>
<%@ taglib uri="/taglib.tld" prefix="util" %>
<util:param id="show_this_date" name="show_this_date" default="" />
<util:param id="param" name="param" />
<util:param id="target" name="target" default="_parent" />
<util:param id="cust_func" name="cust_func" />
<%! private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy"); %>
<%
GregorianCalendar today = new GregorianCalendar();
//Display date needs to be in the request in MM/dd/yyyy format. If one is not passed, then use today
Date reqDate = ( "".equals(show_this_date) ? new Date() : DATE_FORMAT.parse(show_this_date) );
GregorianCalendar displayDate = new GregorianCalendar();
displayDate.setTime(reqDate);
displayDate.set(Calendar.DATE, 1);
displayDate.set(Calendar.HOUR_OF_DAY, 0);
displayDate.set(Calendar.MINUTE, 0);
displayDate.set(Calendar.SECOND, 0);

//date of first day of the month to display - this is a Sunday (the first of the month - however many days into the week we are)
GregorianCalendar start = new GregorianCalendar( displayDate.get(Calendar.YEAR), displayDate.get(Calendar.MONTH), displayDate.get(Calendar.DATE) );
//There is more than likely a much more efficient way of doing this, but I did not want to rely on the assumption that Calendar.SUNDAY will always be 1
while ( start.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY ) start.add(Calendar.DAY_OF_WEEK, -1);

//date final, this is a sunday as well, so add one month, then get to the sunday
GregorianCalendar end = new GregorianCalendar( displayDate.get(Calendar.YEAR), displayDate.get(Calendar.MONTH), displayDate.get(Calendar.DATE) );
end.add(Calendar.MONTH, 1);
//There is more than likely a much more efficient way of doing this, but I did not want to rely on the assumption that Calendar.SUNDAY will always be 1
while ( end.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY ) end.add(Calendar.DAY_OF_WEEK, 1);

//month option tags
StringBuffer monthOptions = new StringBuffer()
    .append("<OPTION VALUE=\"").append(Calendar.JANUARY).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.JANUARY ? " selected" : "" ).append(">January")
    .append("<OPTION VALUE=\"").append(Calendar.FEBRUARY).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.FEBRUARY ? " selected" : "" ).append(">February")
    .append("<OPTION VALUE=\"").append(Calendar.MARCH).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.MARCH ? " selected" : "" ).append(">March")
    .append("<OPTION VALUE=\"").append(Calendar.APRIL).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.APRIL ? " selected" : "" ).append(">April")
    .append("<OPTION VALUE=\"").append(Calendar.MAY).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.MAY ? " selected" : "" ).append(">May")
    .append("<OPTION VALUE=\"").append(Calendar.JUNE).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.JUNE ? " selected" : "" ).append(">June")
    .append("<OPTION VALUE=\"").append(Calendar.JULY).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.JULY ? " selected" : "" ).append(">July")
    .append("<OPTION VALUE=\"").append(Calendar.AUGUST).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.AUGUST ? " selected" : "" ).append(">August")
    .append("<OPTION VALUE=\"").append(Calendar.SEPTEMBER).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.SEPTEMBER ? " selected" : "" ).append(">September")
    .append("<OPTION VALUE=\"").append(Calendar.OCTOBER).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.OCTOBER ? " selected" : "" ).append(">October")
    .append("<OPTION VALUE=\"").append(Calendar.NOVEMBER).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.NOVEMBER ? " selected" : "" ).append(">November")
    .append("<OPTION VALUE=\"").append(Calendar.DECEMBER).append("\"").append( displayDate.get(Calendar.MONTH) == Calendar.DECEMBER ? " selected" : "" ).append(">December");
%>

<html>
<title>Select Date</title>
<head>
<script language="Javascript">
function showMonth()
{
    var frm = document.frmMonth;
    //need year
    if (frm.year.value == "")
    {
        alert("-Please provide a year.");
        return;
    }

    //build date
    var dt = new Date(frm.year.value, frm.month.options[frm.month.selectedIndex].value, 1);

    frm.show_this_date.value = (dt.getMonth()+1 < 10 ? "0" : "") + (dt.getMonth()+1) + "/" +
                               (dt.getDate() < 10 ? "0" : "") + dt.getDate() + "/" + dt.getFullYear();
    frm.submit();
}
</script>
</head>

<body topmargin="0" leftmargin="0" marginwidth="0" marginheight="0">

<form name="frmMonth" onSubmit="return false" method="POST">
<input type="hidden" name="show_this_date">
<input type="hidden" name="param" value="<%= param %>">
<input type="hidden" name="cust_func" value="<%= cust_func %>">

<table width="100%" cellpadding="0" cellspacing="0" Border="0">
 <tr>
  <td align="center" colspan="7">
   <select name="month" onChange="showMonth()"><%= monthOptions.toString() %></select>
   <input name="year" type="text" size="4" maxlength="4" value="<%=displayDate.get(Calendar.YEAR)%>">
   <input type="button" value="Go" onClick="showMonth()">
  </td>
 </tr>
 <tr>
  <th width="12%">S</th>
  <th width="15%">M</th>
  <th width="15%">T</th>
  <th width="15%">W</th>
  <th width="15%">T</th>
  <th width="15%">F</th>
  <th width="12%">S</th>
 </tr>
 <tr>
  <td colspan="7">
   <table border="1" cellspacing="0" cellpadding="0" align="center" width="100%">
<%
//Write the Calendar out
//helpers
String dayUrl = null;
String bgColor = "#FFFFFF";
String textColor = "#00000";
String weight = "normal";

//start with first day
GregorianCalendar day = new GregorianCalendar( start.get(Calendar.YEAR), start.get(Calendar.MONTH), start.get(Calendar.DATE) );

//loop until after last day
while ( day.before(end) )
{
    //if sunday, start row
    if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) out.println("    <tr valign=\"top\">");

    //figure out bg color
    if ( day.get(Calendar.DATE) == today.get(Calendar.DATE) && day.get(Calendar.MONTH) == today.get(Calendar.MONTH) && day.get(Calendar.YEAR) == today.get(Calendar.YEAR)) {
        bgColor = "#FFFFFF";
        weight = "bold";
        textColor = "blue";
    }
    else if ( day.get(Calendar.MONTH) != displayDate.get(Calendar.MONTH) ) {
        bgColor = "#COCOCO";
        weight = "normal";
        textColor = "#000000";
    }
    else {
        bgColor = "#FFFFFF";
        weight = "normal";
        textColor = "#000000";
    }

    //write link to show day
    dayUrl = "javascript:if(!window.opener.closed)window.opener.";
    if ( !"".equals(cust_func) )
    {
        dayUrl += cust_func+"('"+param+"','"+DATE_FORMAT.format( day.getTime() )+"')";
    }
    else
    {
        dayUrl += "document."+param+".value='"+DATE_FORMAT.format( day.getTime() )+"'";
    }
    dayUrl += ";window.close()";
%>
     <td align="center" bgcolor="<%=bgColor%>">
      <a target="<%= target %>" href="<%= dayUrl %>"><span style="color:<%= textColor %>; font-weight:<%= weight %>"><%= day.get(Calendar.DATE) %></font></a><br>
     </td>
<%
    //if saturday, end row
    if (day.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) out.println("    </tr>");

    //increment day
    day.add(Calendar.DATE, 1);
}
%>
   </table>
  </td>
 </tr>
</table>
</form>
<p>
</body>
</html>