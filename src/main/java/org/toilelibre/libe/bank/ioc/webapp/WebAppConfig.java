package org.toilelibre.libe.bank.ioc.webapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.toilelibre.libe.bank.actions.LinkHelper;
import org.toilelibre.libe.bank.actions.LinkLister;
import org.toilelibre.libe.bank.ioc.InMemoryAccountsAppConfig;

@EnableWebMvc
@ComponentScan (basePackages = "org.toilelibre.libe.bank")
public class WebAppConfig extends InMemoryAccountsAppConfig {
    
    @Bean
    public LinkHelper linkHelper () {
        return new LinkHelper ();
    }
    
    @Bean
    public LinkLister linkLister () {
        return new LinkLister ();
    }
}
