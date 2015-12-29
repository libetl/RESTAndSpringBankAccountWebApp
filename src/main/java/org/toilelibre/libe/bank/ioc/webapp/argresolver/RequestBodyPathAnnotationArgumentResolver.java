package org.toilelibre.libe.bank.ioc.webapp.argresolver;

import java.io.IOException;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class RequestBodyPathAnnotationArgumentResolver implements HandlerMethodArgumentResolver {
    
    private static final String BODY_ATTRIBUTE = "REQUEST_BODY";
    
    @Override
    public boolean supportsParameter (MethodParameter parameter) {
        return parameter.hasParameterAnnotation (RequestBodyPath.class);
    }
    
    @Override
    public Object resolveArgument (MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
            throws Exception {
        String body = this.getRequestBody (webRequest);
        String pathValue = parameter.getParameterAnnotation (RequestBodyPath.class).value ();

        MediaType mediaType = MediaType.parseMediaType (webRequest.getHeader (HttpHeaders.CONTENT_TYPE));
        return ArgumentResolverAction.run (mediaType, body, pathValue);
    }
    
    private String getRequestBody (NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest (HttpServletRequest.class);
        String entityBody = (String) servletRequest.getAttribute (BODY_ATTRIBUTE);
        if (entityBody == null) {
            try {
                Scanner scanner = new Scanner(servletRequest.getInputStream (),"UTF-8");
                String body = scanner.useDelimiter("\\A").next();
                servletRequest.setAttribute (BODY_ATTRIBUTE, body);
                scanner.close ();
                return body;
            } catch (IOException e) {
                throw new RuntimeException (e);
            }
        }
        return entityBody;
        
    }
}