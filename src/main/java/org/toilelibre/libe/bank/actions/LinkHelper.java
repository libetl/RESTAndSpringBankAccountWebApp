package org.toilelibre.libe.bank.actions;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LinkHelper {
    
    @Inject
    LinkLister                 linkLister;
                               
    @Inject
    private HttpServletRequest httpServletRequest;
                               
    public Link get () {
        return this.get (Thread.currentThread ().getStackTrace () [2]);
    }
    
    public Link get (StackTraceElement stackTraceElement) {
        Method method = null;
        try {
            method = this.findMethod (stackTraceElement);
        } catch (ClassNotFoundException e) {
            throw new RestClientException ("API method not found", e);
        }
        if (method == null) {
            throw new RestClientException ("API method not found");
        }
        return new Link (this.linkLister.stackTraceElementToFriendlyName (stackTraceElement), this.httpServletRequest.getRequestURL ().toString (),
                new RequestMethod [] { RequestMethod.valueOf (this.httpServletRequest.getMethod ()) },
                this.linkLister.filterNotPayloadParameters (method, method.getParameterTypes ()));
    }
    
    private Method findMethod (StackTraceElement stackTraceElement) throws ClassNotFoundException {
        Class<?> clazz = Class.forName (stackTraceElement.getClassName ());
        Method [] methods = clazz.getMethods ();
        for (Method method : methods) {
            if (method.getName ().equals (stackTraceElement.getMethodName ())) {
                return method;
            }
        }
        return null;
    }
    
    private String findPossibleReplacements (final ObjectNode node, String href) {
        final Iterator<String> fieldNames = node.fieldNames ();
        while (fieldNames.hasNext ()) {
            final String fieldName = fieldNames.next ();
            if (!"".equals (node.get (fieldName).asText ())) {
                href = href.replaceAll ("\\{" + fieldName + "\\}", node.get (fieldName).asText ());
            }
        }
        return href;
    }
    
    public ObjectNode surroundWithLinks (ObjectNode node) {
        Link currentLink = this.get (Thread.currentThread ().getStackTrace () [2]);
        Link currentGenericLink = this.getGenericLink (currentLink);
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode array = (ArrayNode) node.get ("links");
        if (array == null) {
            array = factory.arrayNode ();
            node.set ("links", array);
        }
        final List<Link> links = this.getListOfSublinks (currentGenericLink);
        for (Link link : links) {
            String href = link.getHref ();
            href = this.findPossibleReplacements (node, href);
            array.add ( ((ObjectNode) factory.objectNode ().put ("rel", link.getRel ())
                                                           .put ("href", href)
                                                           .set ("params", factory.pojoNode (link.getParams ())))
                                                           .set ("methods",
                    factory.pojoNode (link.getMethods ())));
                    
        }
        return node;
    }
    
    private Link getGenericLink (Link link) {
        for (Link tmpLink : linkLister.getLinks ()) {
            if (tmpLink.getRel ().equals (link.getRel ())) {
                return tmpLink;
            }
        }
        return null;
    }
    
    private List<Link> getListOfSublinks (Link link) {
        List<Link> result = new LinkedList<Link> ();
        for (Link tmpLink : linkLister.getLinks ()) {
            if (tmpLink.getHref ().startsWith (link.getHref ()) && !tmpLink.getRel ().equals (link.getRel ())) {
                result.add (tmpLink);
            }
        }
        return result;
    }
}
