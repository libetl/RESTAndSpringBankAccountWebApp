package org.toilelibre.libe.bank.actions;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LinkHelper {

    @Inject
    private RequestMappingHandlerMapping handlerMapping;
    @Inject
    private HttpServletRequest           httpServletRequest;

    public Link get () {
        return new Link (this.stackTraceElementToFriendlyName (Thread.currentThread ().getStackTrace () [2]), this.httpServletRequest.getRequestURL ().toString ());
    }

    public ObjectNode addLink (final String name, final ObjectNode node) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode array = (ArrayNode) node.get ("links");
        if (array == null) {
            array = factory.arrayNode ();
            node.set ("links", array);
        }
        final Link link = this.matchLinkWithName (name);
        String href = link.getHref ();
        href = this.findPossibleReplacements (node, href);
        array.add (factory.objectNode ().put ("rel", link.getRel ()).put ("href", href));
        return node;
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

    private Link matchLinkWithName (final String name) {
        for (final Entry<RequestMappingInfo, HandlerMethod> entry : this.handlerMapping.getHandlerMethods ().entrySet ()) {
            if (this.methodToFriendlyName (entry.getValue ().getMethod ()).equals (name)) {
                return new Link (name, this.getBaseUrl () + entry.getKey ().getPatternsCondition ().getPatterns ().iterator ().next ().replaceFirst ("^/", ""));
            }
        }
        return null;
    }

    public String methodToFriendlyName (final String className, final String methodName) {
        return className.replace ("Resource", "") + StringUtils.capitalize (methodName);
    }

    public String methodToFriendlyName (final Method method) {
        return this.methodToFriendlyName (method.getDeclaringClass ().getSimpleName (), method.getName ());
    }

    public String stackTraceElementToFriendlyName (final StackTraceElement ste) {
        return this.methodToFriendlyName (ste.getClassName ().substring (ste.getClassName ().lastIndexOf ('.') + 1), ste.getMethodName ());
    }

    private String getBaseUrl () {
        final StringBuffer url = this.httpServletRequest.getRequestURL ();
        final String uri = this.httpServletRequest.getRequestURI ();
        final String ctx = this.httpServletRequest.getContextPath () + this.httpServletRequest.getServletPath ();
        return url.substring (0, url.length () - uri.length () + ctx.length ()) + "/";
    }
}
