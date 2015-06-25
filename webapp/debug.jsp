<%--
  - Just shows all properties of StaticProperties, BaseConfigServlet.getWebappProperties,
  -
  - @author John Glorioso
  - @version $Id: debug.jsp,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
  --%>
<%@ page
    import="com.zitego.web.servlet.BaseServlet"
    import="com.zitego.web.servlet.BaseConfigServlet"
    import="com.zitego.util.StaticProperties"
%>
<% if ( "johnrules".equals(request.getParameter("pass")) ) { %>
<html>
 <head>
  <title>Debug Window</title>
 </head>
 <body>
  <br><b>StaticProperties------></b><br>
  <pre><%= StaticProperties.printProperties() %></pre>
  <br><b>Webapp Properties-----></b><br>
  <pre><%= BaseConfigServlet.getWebappProperties().printProperties() %></pre>
  <br><b>Request Properties----></b><br>
  <pre><%= BaseServlet.debug(request) %></pre>
  <jsp:include page="additional_debug.jsp" flush="true" />
<%
}
else
{
    out.println("No");
}
%>
 </body>
</html>