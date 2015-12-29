package org.toilelibre.libe.bank.ioc.webapp;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.toilelibre.libe.bank.ioc.webapp.html.HtmlMessageConverter;
import org.toilelibre.libe.bank.ioc.webapp.yaml.YamlMessageConverter;

@Configuration
@EnableWebMvc
public class WebAppContentNegociationConfig extends WebMvcConfigurerAdapter {

    @Override
    public void configureContentNegotiation (final ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension (true).favorParameter (false).ignoreAcceptHeader (false).useJaf (false).defaultContentTypeStrategy (new HeaderContentNegotiationStrategy ())
                .defaultContentType (MediaType.APPLICATION_JSON).mediaType ("yaml", MediaType.parseMediaType ("application/yaml"));
    }

    @Override
    public void extendMessageConverters (final List<HttpMessageConverter<?>> converters) {
        converters.add (new YamlMessageConverter ());
        converters.add (new HtmlMessageConverter ());
        super.extendMessageConverters (converters);
    }

}
