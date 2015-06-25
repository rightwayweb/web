package com.zitego.web.search;

import com.zitego.web.servlet.BaseConfigServlet;
import com.zitego.markup.MarkupContent;
import com.zitego.markup.IllegalMarkupException;
import com.zitego.markup.xml.XmlConverter;
import com.zitego.markup.xml.XmlErrorHandler;
import com.zitego.format.FormatType;
import com.zitego.format.UnsupportedFormatException;
import com.zitego.util.FileUtils;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Hashtable;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.StringReader;
import java.io.File;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * This class handles loading, caching, and updating manager search forms by
 * parsing the provided xml document and caching them in a hashtable. An internal
 * Timer is started to check to see if the maps have changed every five minutes.
 *
 * @author John Glorioso
 * @version $Id: SearchFormMap.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 * @see SearchForm
 */
public class SearchFormMap extends Hashtable implements XmlConverter
{
    /** The path to the xml configuration. */
    protected static String _xmlPath;
    private static Timer _xmlTimer;
    private static XmlChecker _xmlChecker;

    public static void main(String[] args) throws Exception
    {
        SearchFormMap map = new SearchFormMap(args[0]);
        System.out.println(map);
    }

    /**
     * Creates a new search form map with the given xml file path.
     *
     * @param xmlPath The xml file path.
     */
    public SearchFormMap(String xmlPath)
    {
        _xmlPath = xmlPath;
        //Set up the config file loader
        _xmlTimer = new Timer(true);
        _xmlChecker = new XmlChecker();
        //Check every five minutes and execute immediately
        _xmlTimer.scheduleAtFixedRate(_xmlChecker, 0l, 1000l*60l*5l);
    }

    /**
     * Returns the map with the given id. If the map does not exist, a SearchMapException
     * is thrown.
     *
     * @param id The map id.
     * @return SearchForm
     * @throws SearchMapException if the map does not exist.
     */
    public SearchForm getForm(String id) throws SearchMapException
    {
        SearchForm frm = (SearchForm)get(id);
        if (frm == null) throw new SearchMapException
        (
            "An error occurred processing your request: Unable to retrieve search form: "+id+" from the map."
        );
        return frm;
    }

    /**
     * Loads the xml and builds the search form objects.
     *
     * @throws SAXException if the xml document is invalid.
     * @throws IOException if the file could not be loaded.
     * @throws ParserConfigurationException if the parser is not configured properly.
     */
    protected void loadForms() throws SAXException, IOException, ParserConfigurationException
    {
        String xml = FileUtils.getFileContents(_xmlPath);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource src = new InputSource( new StringReader(xml) );
        builder.setErrorHandler( new XmlErrorHandler() );
        Document doc = builder.parse(src);
        buildFromXml( doc.getDocumentElement() );
    }

    public void buildFromXml(Element root)
    {
        //Add the forms
        NodeList nodes = root.getChildNodes();
        int size = nodes.getLength();
        for (int i=0; i<size; i++)
        {
            Node n = nodes.item(i);
            if (n instanceof Element)
            {
                addChild( (Element)n );
            }
        }
    }

    public void addChild(Element child)
    {
        String id = child.getAttribute("id");
        SearchForm form = new SearchForm(id);
        put(id, form);
        form.buildFromXml(child);
    }

    public void parse(Object objToParse, FormatType type) throws IllegalMarkupException, UnsupportedFormatException
    {
        if (objToParse == null) return;
        else if (objToParse instanceof Element && type == FormatType.XML) buildFromXml( (Element)objToParse );
        else throw new IllegalMarkupException("Unable to format object of type: "+objToParse.getClass());
    }

    private class XmlChecker extends TimerTask
    {
        /** The last time the xml file was modified. */
        private long _lastModified = 0L;

        XmlChecker() { }

        /**
         * Checks to see if the xml file may have changed.
         */
        public void run()
        {
            try
            {
                File f = new File(_xmlPath);
                if (f.exists() && f.lastModified() > _lastModified)
                {
                    loadForms();
                    _lastModified = f.lastModified();
                }
            }
            catch (Exception e)
            {
                StringWriter trace = new StringWriter();
                e.printStackTrace( new PrintWriter(trace, true) );
                BaseConfigServlet.logSevereError( "Could not load search forms xml configuration: "+trace.toString() );
            }
        }
    }

    public String format(FormatType type) throws UnsupportedFormatException
    {
        throw new UnsupportedFormatException(type.getDescription() + " is not supported");
    }

    public MarkupContent getParent()
    {
        return null;
    }

    public void setParent(MarkupContent parent)
    {

    }

    public MarkupContent getAsMarkupContent()
    {
        return null;
    }
}