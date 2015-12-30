package org.toilelibre.libe.bank.ioc.webapp.argresolver;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
        public Object run (final String body, final String path, final Class<?> parameterType) {
            return JsonPath.read (body, path);
        }

    }), YAML (MediaType.parseMediaType (YamlMessageConverter.APPLICATION_YAML), new Action () {

        @Override
        public Object run (final String body, final String path, final Class<?> parameterType) {
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
        public Object run (final String body, final String path, final Class<?> parameterType) {
            try {
                final DocumentBuilder builder = DocumentBuilderFactory.newInstance ().newDocumentBuilder ();
                final Document document = builder.parse (new ByteArrayInputStream (body.getBytes ("UTF-8")));

                final XPath xpath = XPathFactory.newInstance ().newXPath ();
                return xpath.evaluate ("/request" + path, document, XPathConstants.NODE);
            } catch (XPathExpressionException | SAXException | IOException | ParserConfigurationException e) {
                throw new RuntimeException (e);
            }

        }

    }), FORMDATA (MediaType.APPLICATION_FORM_URLENCODED, new Action () {

        @Override
        public Object run (final String body, final String path, final Class<?> parameterType) {
            try {
                final String [] pairs = body.split ("\\&");
                for (final String pair : pairs) {
                    final String [] fields = pair.split ("=");
                    final String name = URLDecoder.decode (fields [0], "UTF-8");
                    if (path.equals (name) && fields.length > 1) {
                        return new ObjectMapper ().convertValue (URLDecoder.decode (fields [1], "UTF-8"), parameterType);
                    }
                }
                return null;
            } catch (final UnsupportedEncodingException e) {
                throw new RuntimeException (e);
            }
        }

    });

    static interface Action {
        public Object run (String body, String path, Class<?> parameterType);
    }

    public static Object run (final MediaType mediaType, final String body, final String path, final Class<?> parameterType) {
        for (final ArgumentResolverAction resolverAction : ArgumentResolverAction.values ()) {
            if (mediaType.includes (resolverAction.mediaType)) {
                return resolverAction.action.run (body, path, parameterType);
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
