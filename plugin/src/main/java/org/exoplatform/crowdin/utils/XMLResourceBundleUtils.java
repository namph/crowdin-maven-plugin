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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.xml.internal.bind.v2.runtime.reflect.ListIterator;

/**
 * Created by The eXo Platform SAS 30 Sep 2013 Reuse from
 * org.exoplatform.services.resources.XMLResourceBundleParser with modifications
 */
public class XMLResourceBundleUtils {

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

  public static Map<String, List<String>> asMap(InputStream in) {
    if (in == null) {
      throw new IllegalArgumentException("No null input stream allowed");
    }
    return readXMLToMap(new InputSource(in));
  }

  public static Map<String, List<String>> asMap(Reader in) {
    if (in == null) {
      throw new IllegalArgumentException("No null reader allowed");
    }
    return readXMLToMap(new InputSource(in));
  }

  public static Map<String, List<String>> readXMLToMap(InputSource in) {
    if (in == null) {
      throw new IllegalArgumentException("No null input source allowed");
    }
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    DocumentBuilder builder;
    Document document;
    HashMap<String, List<String>> bundle = new HashMap<String, List<String>>();
    try {
      builder = factory.newDocumentBuilder();
      document = builder.parse(in);

      Element bundleElt = document.getDocumentElement();

      LinkedList<String> path = new LinkedList<String>();
      path.addLast("//" + document.getDocumentElement().getNodeName());
      getLog().debug("Start traverse XML doc with root Node: "+document.getDocumentElement().getNodeName());
      collect(path, bundleElt, bundle);
      getLog().debug("End traverse XML doc with root Node: "+document.getDocumentElement().getNodeName());
    } catch (SAXException e) {
      getLog().error(e);
    } catch (IOException e) {
      getLog().error(e);
    } catch (ParserConfigurationException e) {
      getLog().error(e);
    }
    
    return bundle;
  }

  private static void collect(LinkedList<String> path,
                              Element currentElt,
                              Map<String, List<String>> bundle) {
    NodeList children = currentElt.getChildNodes();

    boolean text = true;
    for (int i = children.getLength() - 1; i >= 0; i--) {
      Node child = children.item(i);
      if (child.getNodeType() == Node.ELEMENT_NODE) {
        text = false;
        Element childElt = (Element) child;
        StringBuffer name = new StringBuffer();
        name.append(childElt.getTagName());

        NamedNodeMap atts = child.getAttributes();
        if (atts != null && atts.getLength() > 0) {
          name.append("[");
          for (int j = 0; j < atts.getLength(); j++) {
            name.append("@" + atts.item(j).getNodeName());
            name.append("='" + atts.item(j).getNodeValue() + "'");
            if (j < atts.getLength() - 1)
              name.append(" and ");
          }
          name.append("]");
        }
        getLog().debug("Current Node: " + name.toString());
        path.addLast(name.toString());
        collect(path, childElt, bundle);
        path.removeLast();
      }
    }
    if (text && path.size() > 0) {
      String valueStr = currentElt.getTextContent();
      StringBuffer sb = new StringBuffer();
      for (Iterator<String> i = path.iterator(); i.hasNext();) {
        String name = i.next();

        sb.append(name);
        if (i.hasNext()) {
          sb.append('/');
        }
      }
      String key = sb.toString();
      getLog().debug("Current path: " + key);
      if (bundle.containsKey(key)) {
        bundle.get(key).add(valueStr);
      } else {
        List<String> value = new LinkedList<String>();
        value.add(valueStr);
        bundle.put(key, value);
      }

    }
  }

  public static String saveMapToXMLFile(String xmlPathFile, Map<String, List<String>> mapData) {
    try {

      DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
      Document doc = docBuilder.parse(xmlPathFile);

      XPathFactory factory = XPathFactory.newInstance();
      XPath xpath = factory.newXPath();

      Iterator<Map.Entry<String, List<String>>> iterator = mapData.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, List<String>> mapEntry = (Map.Entry<String, List<String>>) iterator.next();
        System.out.println("The key is: " + mapEntry.getKey() + ",value is :" + mapEntry.getValue());

        // find the node
        Node node = (Node) xpath.evaluate(mapEntry.getKey() + "", doc, XPathConstants.NODE);

        // update new value for node
        node.setTextContent(null != mapEntry.getValue() ? mapEntry.getValue() + "" : "");
      }

      // write the content into xml file
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource source = new DOMSource(doc);
      StreamResult result = new StreamResult(new File(xmlPathFile));
      transformer.transform(source, result);

      // TODO
      // return XML file path after save mapData to XML file
    } catch (Exception e) {
      log.error(e);
    }
    return null;

  }
}
