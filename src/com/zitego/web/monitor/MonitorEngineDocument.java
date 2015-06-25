package com.zitego.web.monitor;

import com.zitego.markup.xml.XmlTag;

/**
 * This class is the full description of a monitor engine document. It has a list of monitors,
 * a list of database connections, and failure recipients.
 * <xmp>
 * <monitor-engine>
 *  <username>some_username</username>
 *  <password>some_password</password>
 *  <type>com.zitego.remoteCommandProcessor.FileCopier</type>
 *  <processor-arguments>
 *   <copy>
 *    <from>/home/directory1/file_or_directory</from>
 *    <to>/home/directory2</to>
 *   </copy>
 *   ...
 *  </processor-arguments>
 * </monitor-engine>
 * </xmp>
 *
 * @author John Glorioso
 * $version $Id: MonitorEngineDocument.java,v 1.1.1.1 2008/02/20 14:53:27 jglorioso Exp $
 */
public class MonitorEngineDocument extends XmlTag
{
    /**
     * Creates a new processor arguments object.
     */
    public MonitorEngineDocument()
    {
        super("monitor-engine");
    }
}