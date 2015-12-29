package org.toilelibre.libe.bank.ioc.webapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.method.annotation.MapMethodProcessor;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;
import org.springframework.web.servlet.mvc.method.annotation.ServletRequestMethodArgumentResolver;
import org.toilelibre.libe.bank.ioc.webapp.argresolver.RequestBodyPathAnnotationArgumentResolver;

public class RequestMappingHandlerAdapterWithRequestBodyPathResolver extends RequestMappingHandlerAdapter {


    public void afterPropertiesSet () {
        this.configureAllowedArgumentResolvers ();
        super.afterPropertiesSet ();
    }

    private void configureAllowedArgumentResolvers () {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<HandlerMethodArgumentResolver>();

        resolvers.add(new RequestBodyPathAnnotationArgumentResolver());

        resolvers.add(new RequestResponseBodyMethodProcessor(this.getMessageConverters()));
        
        resolvers.add(new ServletRequestMethodArgumentResolver());
        
        resolvers.add(new PathVariableMethodArgumentResolver());

        resolvers.add(new MapMethodProcessor());

        this.setArgumentResolvers (resolvers);
    }
}