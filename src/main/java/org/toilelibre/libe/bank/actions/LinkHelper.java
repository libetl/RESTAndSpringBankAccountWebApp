package org.toilelibre.libe.bank.actions;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
import org.toilelibre.libe.bank.actions.entity.ArrayNode;
import org.toilelibre.libe.bank.actions.entity.Node;
import org.toilelibre.libe.bank.actions.entity.NodeFactory;
import org.toilelibre.libe.bank.actions.entity.ObjectNode;

public class LinkHelper {

    @Inject
    LinkLister linkLister;

    @Inject
    private HttpServletRequest httpServletRequest;

    private Method findMethod (final StackTraceElement stackTraceElement) throws ClassNotFoundException {
        final Class<?> clazz = Class.forName (stackTraceElement.getClassName ());
        final Method [] methods = clazz.getMethods ();
        for (final Method method : methods) {
            if (method.getName ().equals (stackTraceElement.getMethodName ())) {
                return method;
            }
        }
        return null;
    }

    private <T extends Node> String findPossibleReplacements (final ObjectNode<T> node, String href) {
        final Iterator<String> fieldNames = node.iterator ();
        while (fieldNames.hasNext ()) {
            final String fieldName = fieldNames.next ();
            if (!"".equals (node.get (fieldName).asText ())) {
                href = href.replaceAll ("\\{" + fieldName + "\\}", node.get (fieldName).asText ());
            }
        }
        return href;
    }

    public ObjectNode<Node> findSuggestedLink (final ObjectNode<Node> objectNode) {
        this.linkLister.getLinks ();
        final Link currentLink = this.get (Thread.currentThread ().getStackTrace () [2]);
        final List<Link> suggestedLinks = this.linkLister.getSuggestedLinks (currentLink);

        this.linksToObjectNode (suggestedLinks, objectNode, "suggestedLink");
        return objectNode;
    }

    public Link get () {
        return this.get (Thread.currentThread ().getStackTrace () [2]);
    }

    public Link get (final StackTraceElement stackTraceElement) {
        Method method = null;
        try {
            method = this.findMethod (stackTraceElement);
        } catch (final ClassNotFoundException e) {
            throw new RestClientException ("API method not found", e);
        }
        if (method == null) {
            throw new RestClientException ("API method not found");
        }
        return new Link (this.linkLister.stackTraceElementToFriendlyName (stackTraceElement), this.httpServletRequest.getRequestURL ().toString (),
                new RequestMethod [] { RequestMethod.valueOf (this.httpServletRequest.getMethod ()) }, this.linkLister.getLinkParams (method));
    }

    private Link getGenericLink (final Link link) {
        for (final Link tmpLink : this.linkLister.getLinks ()) {
            if (tmpLink.getRel ().equals (link.getRel ())) {
                return tmpLink;
            }
        }
        return null;
    }

    private List<Link> getListOfSublinks (final Link link) {
        final List<Link> result = new LinkedList<Link> ();
        for (final Link tmpLink : this.linkLister.getLinks ()) {
            if (tmpLink.getHref ().startsWith (link.getHref ()) && !tmpLink.getRel ().equals (link.getRel ())) {
                result.add (tmpLink);
            }
        }
        return result;
    }

    private void linksToObjectNode (final List<Link> links, final ObjectNode<Node> objectNode, final String nodeName) {
        final NodeFactory factory = NodeFactory.instance;
        ArrayNode array = (ArrayNode) objectNode.get (nodeName);
        if (array == null) {
            array = factory.arrayNode ();
            objectNode.set (nodeName, array);
        }
        for (final Link link : links) {
            String href = link.getHref ();
            href = this.findPossibleReplacements (objectNode, href);
            array.add (factory.objectNode ().put ("rel", link.getRel ()).put ("href", href).set ("params", factory.pojoNode (link.getParams ())).set ("methods",
                    factory.pojoNode (link.getMethods ())));

        }
    }

    public ObjectNode<Node> surroundWithLinks (final ObjectNode<Node> objectNode) {
        final Link currentLink = this.get (Thread.currentThread ().getStackTrace () [2]);
        final Link currentGenericLink = this.getGenericLink (currentLink);

        final List<Link> links = this.getListOfSublinks (currentGenericLink);
        this.linksToObjectNode (links, objectNode, "links");
        return objectNode;
    }
}
