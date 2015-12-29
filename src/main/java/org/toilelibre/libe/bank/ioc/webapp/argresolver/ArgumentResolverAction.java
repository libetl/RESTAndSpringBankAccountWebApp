package org.toilelibre.libe.bank.ioc.webapp.argresolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.http.MediaType;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.jayway.jsonpath.JsonPath;

public enum ArgumentResolverAction {

    JSON (MediaType.APPLICATION_JSON, new Action () {

        @Override
        public Object run (String body, String path) {
            return JsonPath.read (body, path);
        }
        
    }),
    XML (MediaType.APPLICATION_XML, new Action () {

        @Override
        public Object run (String body, String path) {
            try {
                DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                Document document = builder.parse (new ByteArrayInputStream (body.getBytes ("UTF-8")));

                XPath xpath = XPathFactory.newInstance().newXPath();
                return xpath.evaluate ("/request" + path, document, XPathConstants.NODE);
            } catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
                return null;
            }


        }
        
    });
    
    static interface Action {
        public Object run (String body, String path);
    }

    private final MediaType mediaType;
    private final Action action;
    
    ArgumentResolverAction (MediaType mediaType, Action action) {
        this.mediaType = mediaType;
        this.action = action;
    }
    
    public static Object run (MediaType mediaType, String body, String path) {
        for (ArgumentResolverAction resolverAction : ArgumentResolverAction.values ()) {
            if (mediaType.includes (resolverAction.mediaType)) {
                return resolverAction.action.run (body, path);
            }
        }
        return null;
    }
}
