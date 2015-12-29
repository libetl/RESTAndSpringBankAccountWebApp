package org.toilelibre.libe.bank.ioc.webapp.argresolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.http.MediaType;
import org.toilelibre.libe.bank.ioc.webapp.yaml.YamlMessageConverter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;
import com.jayway.jsonpath.JsonPath;

public enum ArgumentResolverAction {

    JSON (MediaType.APPLICATION_JSON, new Action () {

        @Override
        public Object run (final String body, final String path) {
            return JsonPath.read (body, path);
        }

    }), YAML (MediaType.parseMediaType (YamlMessageConverter.APPLICATION_YAML), new Action () {

        @Override
        public Object run (final String body, final String path) {
            final Object bodyAsNode = new Yaml ().loadAs (body, HashMap.class);
            final StringWriter stringWriter = new StringWriter ();
            try {
                new ObjectMapper ().writeValue (stringWriter, bodyAsNode);
            } catch (final IOException e) {
                throw new RuntimeException (e);
            }
            return JsonPath.read (stringWriter.toString (), path);
        }

    }), XML (MediaType.APPLICATION_XML, new Action () {

        @Override
        public Object run (final String body, final String path) {
            try {
                final DocumentBuilder builder = DocumentBuilderFactory.newInstance ().newDocumentBuilder ();
                final Document document = builder.parse (new ByteArrayInputStream (body.getBytes ("UTF-8")));

                final XPath xpath = XPathFactory.newInstance ().newXPath ();
                return xpath.evaluate ("/request" + path, document, XPathConstants.NODE);
            } catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
                throw new RuntimeException (e);
            }

        }

    });

    static interface Action {
        public Object run (String body, String path);
    }

    public static Object run (final MediaType mediaType, final String body, final String path) {
        for (final ArgumentResolverAction resolverAction : ArgumentResolverAction.values ()) {
            if (mediaType.includes (resolverAction.mediaType)) {
                return resolverAction.action.run (body, path);
            }
        }
        return null;
    }

    private final MediaType mediaType;

    private final Action action;

    ArgumentResolverAction (final MediaType mediaType, final Action action) {
        this.mediaType = mediaType;
        this.action = action;
    }
}
