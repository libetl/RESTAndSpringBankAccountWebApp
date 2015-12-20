package org.toilelibre.libe.bank.actions;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
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
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

public class LinkLister {
    
    @Inject
    private HttpServletRequest           httpServletRequest;
                                         
    @Inject
    private RequestMappingHandlerMapping handlerMapping;
                                         
    private List<Link>                   links;
                                         
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
    
    private Link entryToLink (Entry<RequestMappingInfo, HandlerMethod> entry) {
        return new Link (this.methodToFriendlyName (entry.getValue ().getMethod ()),
                this.getBaseUrl () + entry.getKey ().getPatternsCondition ().getPatterns ().iterator ().next ().replaceFirst ("^/", ""),
                entry.getValue ().getMethodAnnotation (RequestMapping.class).method (),
                this.filterNotPayloadParameters (entry.getValue ().getMethod (), entry.getValue ().getMethod ().getParameterTypes ()));
    }
    
    private String getBaseUrl () {
        final StringBuffer url = this.httpServletRequest.getRequestURL ();
        final String uri = this.httpServletRequest.getRequestURI ();
        final String ctx = this.httpServletRequest.getContextPath () + this.httpServletRequest.getServletPath ();
        return url.substring (0, url.length () - uri.length () + ctx.length ()) + "/";
    }
    
    private String methodToFriendlyName (final Method method) {
        return this.methodToFriendlyName (method.getDeclaringClass ().getSimpleName (), method.getName ());
    }
    
    private String methodToFriendlyName (final String className, final String methodName) {
        return className.replace ("Resource", "") + StringUtils.capitalize (methodName);
    }
    
    String [] filterNotPayloadParameters (Method method, Class<?> [] parameterTypes) {
        List<String> classes = new LinkedList<String> ();
        for (int i = 0 ; i < parameterTypes.length ; i++) {
            if (!this.annotationsIncludePathVariable (Arrays.asList (method.getParameterAnnotations () [i])) && ! (ServletRequest.class.isAssignableFrom (parameterTypes [i]))
                    && ! (ServletResponse.class.isAssignableFrom (parameterTypes [i]))) {
                classes.add (parameterTypes [i].getSimpleName ());
            }
        }
        return classes.toArray (new String [classes.size ()]);
    }
    
    private boolean annotationsIncludePathVariable (List<Annotation> annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof PathVariable) {
                return true;
            }
        }
        return false;
    }
    
    String stackTraceElementToFriendlyName (final StackTraceElement ste) {
        return this.methodToFriendlyName (ste.getClassName ().substring (ste.getClassName ().lastIndexOf ('.') + 1), ste.getMethodName ());
    }
    
}
