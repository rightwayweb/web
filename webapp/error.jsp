<%--
  - Handles writing out errors and logging them if necessary.
  - @author John Glorioso
  - @version $Id: error.jsp,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
  --%>
<%@ page
    import="java.io.IOException"
    import="java.io.PrintWriter"
    import="javax.servlet.jsp.JspException"
    import="com.zitego.util.NonFatalException"
    import="com.zitego.util.NonFatalExceptionWithJavascript"
    isErrorPage="true"
%>
<%@ taglib uri="/taglib.tld" prefix="util" %>
<util:error />
<util:webappproperty id="debug" name="debug" type="java.lang.String" />
<util:param id="subject" name="subject" />

  <% if (compileError != null) { %>
  <span class="error_text">Compile Error:</span><br>
  <pre><b><%= compileError %></b></pre>
  <% } %>
  <p><span class="error_text"><%= userError %></span></p>
  <% if ( !(throwable instanceof NonFatalException) ) { %>
  <p>
   System administrators have been notified of the error, but you can further help us by sending an
   <a href="mailto:support@zitego.com<%= (!"".equals(subject) ? "?subject="+subject : "") %>">email</a> to us that explains what you were attempting
   to do when the error occurred. Please include:
  </p>
  <p>
   <ol>
    <li> The page you were on.
    <li> The action you were trying to perform.
    <li> The browser type and version you are using.
    <li> The operating system you are running.
   </ol>
  </p>
  <p>Thank you for helping us improve the system. We apologize for this inconvenience.</p>
  <%    if ( "1".equals(debug) && throwable != null )
        {
            if (throwable instanceof JspException)
            {
                 Throwable t2 = ( (JspException)throwable ).getRootCause();
                 if (t2 != null) throwable = t2;
            }
  %>
  <br><br>
  <table>
   <tr>
    <td>
     <pre><% throwable.printStackTrace( new PrintWriter(out) ); %></pre>
    </td>
   </tr>
  </table>
  <%    } %>
  <% } else if (throwable instanceof NonFatalExceptionWithJavascript) { %>
  <script language="Javascript"><%= ( (NonFatalExceptionWithJavascript)throwable ).getJavascript() %></script>
  <% } %>