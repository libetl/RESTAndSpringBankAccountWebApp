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

    private String getRequestBody (final NativeWebRequest webRequest) {
        final HttpServletRequest servletRequest = webRequest.getNativeRequest (HttpServletRequest.class);
        final String entityBody = (String) servletRequest.getAttribute (RequestBodyPathAnnotationArgumentResolver.BODY_ATTRIBUTE);
        if (entityBody == null) {
            try {
                final Scanner scanner = new Scanner (servletRequest.getInputStream (), "UTF-8");
                final String body = scanner.useDelimiter ("\\A").next ();
                servletRequest.setAttribute (RequestBodyPathAnnotationArgumentResolver.BODY_ATTRIBUTE, body);
                scanner.close ();
                return body;
            } catch (final IOException e) {
                throw new RuntimeException (e);
            }
        }
        return entityBody;

    }

    @Override
    public Object resolveArgument (final MethodParameter parameter, final ModelAndViewContainer mavContainer, final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory) throws Exception {
        final String body = this.getRequestBody (webRequest);
        final String pathValue = parameter.getParameterAnnotation (RequestBodyPath.class).value ();

        final MediaType mediaType = MediaType.parseMediaType (webRequest.getHeader (HttpHeaders.CONTENT_TYPE));
        return ArgumentResolverAction.run (mediaType, body, pathValue, parameter.getParameterType ());
    }

    @Override
    public boolean supportsParameter (final MethodParameter parameter) {
        return parameter.hasParameterAnnotation (RequestBodyPath.class);
    }
}