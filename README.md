RELEASE NOTES:

Dependencies - common, markup, jasper-runtime.jar, servlet.jar

1.0.9  - Added groupParam, prevDelimiter, and nextDelimiter to GroupNavigatorTag
       - Changed to have third party api log responses to System.out instead of BaseConfigServlet.

1.0.8  - Added limit to format tag.
       - Added ability to specify previous and next link text in GroupNavigatorTag

1.0.7  - Added com.zitego.web.util.GenericRequestToken.

1.0.6a - Bug fix to group navigator tag for groups that have a lot of entries.

1.0.6  - Added pre/postAnchorMarkup properties to the sortcol tag.
       - Added DateRangeServlet

1.0.5a - Bug fix in group navigator tags.
       - Added useSpan to SortableColumnHeaderTag
       - Added awareness of javascript function for linkUrl in GroupNavigatorTag

1.0.5  - Completed build out of monitor package.

1.0.4  - Added search classes.
       - Added getFields to ReportServlet

1.0.3  - Added GroupNavigatorTag.

1.0.2d - Added html text to selector tag for button.

1.0.2c - Added bug fix for https url prefix in LinkTag.
       - Added id attribute to LinkTag.
       - Added SelectorFieldTag

1.0.2b - Special handling of JspException in ErrorTag.
       - Added getThirdPartyAPI method to ThirdPartyAPIManager.

1.0.2a - Added identification to error tag.
       - Added style attribute to link tag.

1.0.2  - Added context path argument for com.zitego.jsp.LinkTag.
       - Moved BaseConfigServlet, ReportServlet, BaseServlet, BaseLoginServlet,
         and TransportServlet from common.
       - Added StaticWebappProperties and altered BaseConfigServlet to use that
         instead of StaticProperties.
       - Changed all references to StaticProperties to look in BaseConfigServlet's
         webapp properties first.
       - Added monitor package
       - Bug fix in BaseConfigServlet for the scheduler.
       - Bug fix in ReportServlet where dsc was not getting removed on 0 results.

1.0.1  - Added discussion for message boarding

1.0    - Initial Release
