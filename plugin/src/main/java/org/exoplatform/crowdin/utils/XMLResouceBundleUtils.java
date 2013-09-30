/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.crowdin.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Created by The eXo Platform SAS 30 Sep 2013 Reuse from
 * org.exoplatform.services.resources.XMLResourceBundleParser with modifications
 */
public class XMLResouceBundleUtils {

  static Log log;

  public static void setLog(Log varLog) {
    log = varLog;
  }

  public static Log getLog() {
    if (log == null) {
      log = new SystemStreamLog();
    }

    return log;
  }

  public static Map<String, String> asMap(InputStream in) throws IOException,
                                                         SAXException,
                                                         ParserConfigurationException,
                                                         IllegalArgumentException {
    if (in == null) {
      throw new IllegalArgumentException("No null input stream allowed");
    }
    return readXMLToMap(new InputSource(in));
  }

  public static Map<String, String> asMap(Reader in) throws IOException,
                                                    SAXException,
                                                    ParserConfigurationException,
                                                    IllegalArgumentException {
    if (in == null) {
      throw new IllegalArgumentException("No null reader allowed");
    }
    return readXMLToMap(new InputSource(in));
  }

  public static Map<String, String> readXMLToMap(InputSource in) throws IOException,
                                                                SAXException,
                                                                ParserConfigurationException,
                                                                IllegalArgumentException {
    if (in == null) {
      throw new IllegalArgumentException("No null input source allowed");
    }
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder = factory.newDocumentBuilder();
    Document document = builder.parse(in);
    Element bundleElt = document.getDocumentElement();
    HashMap<String, String> bundle = new HashMap<String, String>();
    collect(new LinkedList<String>(), bundleElt, bundle);
    return bundle;
  }

  private static void collect(LinkedList<String> path, Element currentElt, Map<String, String> bundle) {

  }
  
  
  public static String saveMapToXMLFile(String xmlPathFile, Map<String, String> mapData){
    try{
      //TODO
      //return XML file path after save mapData to XML file
    }catch (Exception e) {
      log.error(e);
    }
    return null;
    
  }
}
