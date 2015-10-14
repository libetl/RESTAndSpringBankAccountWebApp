package org.toilelibre.libe.bank.actions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestClientException;
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

    public Link get() {
        Method method = null;
        try {
            method = this.findMethod(Thread.currentThread().getStackTrace()[2]);
        } catch (ClassNotFoundException e) {
            throw new RestClientException("API method not found", e);
        }
        if (method == null) {
            throw new RestClientException("API method not found");
        }
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        return new Link(this.stackTraceElementToFriendlyName(Thread.currentThread().getStackTrace()[2]), this.httpServletRequest.getRequestURL().toString(), new RequestMethod[] { RequestMethod.valueOf(this.httpServletRequest.getMethod()) }, factory.pojoNode(this.filterNotPayloadParameters(method,
                method.getParameterTypes())));
    }

    private Method findMethod(StackTraceElement stackTraceElement) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(stackTraceElement.getClassName());
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(stackTraceElement.getMethodName())) {
                return method;
            }
        }
        return null;
    }

    public List<Link> getLinks(final String requestURL) {
        final List<Link> result = new LinkedList<Link>();
        for (final Entry<RequestMappingInfo, HandlerMethod> entry : this.handlerMapping.getHandlerMethods().entrySet()) {
            result.add(this.entryToLink(entry));
        }
        return result;
    }

    public ObjectNode addLink(final String name, final ObjectNode node) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        ArrayNode array = (ArrayNode) node.get("links");
        if (array == null) {
            array = factory.arrayNode();
            node.set("links", array);
        }
        final Link link = this.matchLinkWithName (name);
        String href = link.getHref ();
        href = this.findPossibleReplacements (node, href);
        array.add(((ObjectNode) factory.objectNode ().put("rel", link.getRel ()).put ("href", href).set ("params", link.getParams ())).set ("methods", factory.pojoNode (link.getMethods())));
        return node;
    }

    private String findPossibleReplacements(final ObjectNode node, String href) {
        final Iterator<String> fieldNames = node.fieldNames();
        while (fieldNames.hasNext()) {
            final String fieldName = fieldNames.next();
            if (!"".equals(node.get(fieldName).asText())) {
                href = href.replaceAll("\\{" + fieldName + "\\}", node.get(fieldName).asText());
            }
        }
        return href;
    }

    private Link matchLinkWithName(final String name) {
        for (final Entry<RequestMappingInfo, HandlerMethod> entry : this.handlerMapping.getHandlerMethods().entrySet()) {
            if (this.methodToFriendlyName (entry.getValue().getMethod ()).equals (name)) {
                return this.entryToLink (entry);
            }
        }
        return null;
    }

    private Link entryToLink(Entry<RequestMappingInfo, HandlerMethod> entry) {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        return new Link(this.methodToFriendlyName(entry.getValue().getMethod()), this.getBaseUrl() + entry.getKey().getPatternsCondition().getPatterns().iterator().next().replaceFirst("^/", ""), entry.getValue().getMethodAnnotation(RequestMapping.class).method(), factory.pojoNode(this
                .filterNotPayloadParameters(entry.getValue().getMethod(), entry.getValue().getMethod().getParameterTypes())));
    }

    private String[] filterNotPayloadParameters (Method method, Class<?>[] parameterTypes) {
        List<String> classes = new LinkedList<String> ();
        for (int i = 0; i < parameterTypes.length; i++) {
            if (!this.annotationsIncludePathVariable (Arrays.asList (method.getParameterAnnotations () [i])) &&
                    !(ServletRequest.class.isAssignableFrom (parameterTypes [i])) &&
                    !(ServletResponse.class.isAssignableFrom (parameterTypes [i]))) {
                classes.add (parameterTypes [i].getSimpleName());
            }
        }
        return classes.toArray (new String [classes.size()]);
    }

    private boolean annotationsIncludePathVariable(List<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof PathVariable) {
                return true;
            }
        }
        return false;
    }

    public String methodToFriendlyName(final String className, final String methodName) {
        return className.replace ("Resource", "") + StringUtils.capitalize (methodName);
    }

    public String methodToFriendlyName(final Method method) {
        return this.methodToFriendlyName (method.getDeclaringClass ().getSimpleName (), method.getName ());
    }

    public String stackTraceElementToFriendlyName(final StackTraceElement ste) {
        return this.methodToFriendlyName (ste.getClassName().substring (ste.getClassName ().lastIndexOf ('.') + 1), ste.getMethodName ());
    }

    private String getBaseUrl() {
        final StringBuffer url = this.httpServletRequest.getRequestURL ();
        final String uri = this.httpServletRequest.getRequestURI ();
        final String ctx = this.httpServletRequest.getContextPath () + this.httpServletRequest.getServletPath ();
        return url.substring (0, url.length () - uri.length () + ctx.length ()) + "/";
    }
}
