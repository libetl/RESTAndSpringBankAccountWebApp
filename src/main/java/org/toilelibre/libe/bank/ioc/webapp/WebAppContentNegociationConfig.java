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
                  .favorParameter (false)
                  .ignoreAcceptHeader (false)
                  .useJaf (false)
                  .defaultContentTypeStrategy (new HeaderContentNegotiationStrategy ())
                  .defaultContentType (MediaType.APPLICATION_JSON);
    }
}
