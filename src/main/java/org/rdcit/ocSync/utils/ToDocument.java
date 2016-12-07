/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rdcit.ocSync.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author sa841
 */
public class ToDocument {

    public Document stringToDocument(String xmlString) {
        Document document = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = new ByteArrayInputStream(xmlString.getBytes());
            document = builder.parse(inputStream);
            inputStream.close();
        } catch (ParserConfigurationException | IOException | SAXException ex) {
            System.out.println(ex.getMessage());
        }
        return document;
    }

    public Document nodeToDocument(Node node) {
        Document document = null;
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(node);
            transformer.transform(source, result);
            StringWriter sw = (StringWriter) result.getWriter();
            StringBuffer sb = sw.getBuffer();
            String finalstring = sb.toString();
            document = stringToDocument(finalstring);
            sw.close();
        } catch (IOException | TransformerException ex) {
            System.out.println(ex.getMessage());
        }
        return document;
    }

    public String elementToString(Element eNode) {
        String sNode = "";
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            StreamResult result = new StreamResult(new StringWriter());
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource((Node) eNode);
            transformer.transform(source, result);
            StringWriter sw = (StringWriter) result.getWriter();
            StringBuffer sb = sw.getBuffer();
            sNode = sb.toString();
            sw.close();
        } catch (IOException | TransformerException ex) {
            System.out.println(ex.getMessage());
        }
        return sNode;
    }

    public String toString(Document doc) {
        StringWriter sw = null;
        try {
            sw = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.transform(new DOMSource(doc), new StreamResult(sw));
        } catch (IllegalArgumentException | TransformerException ex) {
            System.out.println(ex.getMessage());
        }
        return sw.toString();
    }

}
