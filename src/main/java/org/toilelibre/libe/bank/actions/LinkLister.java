package org.toilelibre.libe.bank.actions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.toilelibre.libe.bank.actions.Link.Param;
import org.toilelibre.libe.bank.ioc.webapp.argresolver.RequestBodyPath;

public class LinkLister {

    @Inject
    private HttpServletRequest httpServletRequest;

    @Inject
    private RequestMappingHandlerMapping handlerMapping;

    private List<Link> links;

    private Link entryToLink (final Entry<RequestMappingInfo, HandlerMethod> entry) {
        return new Link (this.methodToFriendlyName (entry.getValue ().getMethod ()),
                this.getBaseUrl () + entry.getKey ().getPatternsCondition ().getPatterns ().iterator ().next ().replaceFirst ("^/", ""),
                entry.getValue ().getMethodAnnotation (RequestMapping.class).method (), this.getLinkParams (entry.getValue ().getMethod ()));
    }

    @SuppressWarnings ("unchecked")
    private <T extends Annotation> T getAnnotationOfThisType (final Class<T> annot, final Annotation [] annotations) {
        for (final Annotation annotation : annotations) {
            if (annotation.annotationType () == annot) {
                return (T) annotation;
            }
        }
        return null;
    }

    private String getBaseUrl () {
        final StringBuffer url = this.httpServletRequest.getRequestURL ();
        final String uri = this.httpServletRequest.getRequestURI ();
        final String ctx = this.httpServletRequest.getContextPath () + this.httpServletRequest.getServletPath ();
        return url.substring (0, (url.length () - uri.length ()) + ctx.length ()) + "/";
    }

    Param [] getLinkParams (final Method method) {
        boolean canDisplayRequestStub = true;
        final Class<?> [] parameterTypes = method.getParameterTypes ();
        final List<Param> params = new LinkedList<Param> ();
        for (int i = 0 ; i < parameterTypes.length ; i++) {
            final RequestBodyPath requestBodyPath = this.getAnnotationOfThisType (RequestBodyPath.class, method.getParameterAnnotations () [i]);
            if (requestBodyPath != null) {
                params.add (new Param (requestBodyPath.value (), parameterTypes [i].getSimpleName ()));
            } else if (this.getAnnotationOfThisType (PathVariable.class, method.getParameterAnnotations () [i]) == null) {
                canDisplayRequestStub = false;
            }
        }
        if (!canDisplayRequestStub) {
            params.clear ();
            params.add (new Param ("unknownParameters", "object"));
        }
        return params.toArray (new Param [params.size ()]);
    }

    public List<Link> getLinks () {
        if (this.links == null) {
            this.links = this.initLinks ();
        }
        return this.links;
    }

    private List<Link> initLinks () {
        final List<Link> result = new LinkedList<Link> ();
        for (final Entry<RequestMappingInfo, HandlerMethod> entry : this.handlerMapping.getHandlerMethods ().entrySet ()) {
            result.add (this.entryToLink (entry));
        }
        return Collections.unmodifiableList (result);
    }

    private String methodToFriendlyName (final Method method) {
        return this.methodToFriendlyName (method.getDeclaringClass ().getSimpleName (), method.getName ());
    }

    private String methodToFriendlyName (final String className, final String methodName) {
        return className.replace ("Resource", "") + StringUtils.capitalize (methodName);
    }

    String stackTraceElementToFriendlyName (final StackTraceElement ste) {
        return this.methodToFriendlyName (ste.getClassName ().substring (ste.getClassName ().lastIndexOf ('.') + 1), ste.getMethodName ());
    }

}
