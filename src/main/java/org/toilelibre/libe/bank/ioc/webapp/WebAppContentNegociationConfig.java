package org.toilelibre.libe.bank.ioc.webapp;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
@EnableWebMvc
public class WebAppContentNegociationConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureContentNegotiation (ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension (true)
                  .favorParameter (true)
                  .parameterName ("t")
                  .ignoreAcceptHeader (false)
                  .useJaf (false)
                  .defaultContentTypeStrategy (new HeaderContentNegotiationStrategy ())
                  .defaultContentType (MediaType.TEXT_HTML)
                  .mediaType ("html", MediaType.TEXT_HTML)
                  .mediaType ("xml", MediaType.APPLICATION_XML)
                  .mediaType ("json", MediaType.APPLICATION_JSON);
    }

}
