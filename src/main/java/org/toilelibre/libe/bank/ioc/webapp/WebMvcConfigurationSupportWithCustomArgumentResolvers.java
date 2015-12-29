package org.toilelibre.libe.bank.ioc.webapp;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewRequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.JsonViewResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Configuration
public class WebMvcConfigurationSupportWithCustomArgumentResolvers extends WebMvcConfigurationSupport {

    @Override
    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter () {
        final List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<HandlerMethodArgumentResolver> ();
        this.addArgumentResolvers (argumentResolvers);

        final List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<HandlerMethodReturnValueHandler> ();
        this.addReturnValueHandlers (returnValueHandlers);

        final RequestMappingHandlerAdapter adapter = new RequestMappingHandlerAdapterWithRequestBodyPathResolver ();
        adapter.setContentNegotiationManager (this.mvcContentNegotiationManager ());
        adapter.setMessageConverters (this.getMessageConverters ());
        adapter.setWebBindingInitializer (this.getConfigurableWebBindingInitializer ());
        adapter.setCustomArgumentResolvers (argumentResolvers);
        adapter.setCustomReturnValueHandlers (returnValueHandlers);

        final List<RequestBodyAdvice> requestBodyAdvices = new ArrayList<RequestBodyAdvice> ();
        requestBodyAdvices.add (new JsonViewRequestBodyAdvice ());
        adapter.setRequestBodyAdvice (requestBodyAdvices);

        final List<ResponseBodyAdvice<?>> responseBodyAdvices = new ArrayList<ResponseBodyAdvice<?>> ();
        responseBodyAdvices.add (new JsonViewResponseBodyAdvice ());
        adapter.setResponseBodyAdvice (responseBodyAdvices);

        final AsyncSupportConfigurer configurer = new AsyncSupportConfigurer ();
        this.configureAsyncSupport (configurer);

        return adapter;
    }
}