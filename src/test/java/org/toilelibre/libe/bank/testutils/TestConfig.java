package org.toilelibre.libe.bank.testutils;

import org.springframework.context.annotation.Bean;

public class TestConfig {
    
    @Bean
    public AccountHelper getNewAccountHelper () {
        return new AccountHelper ();
    }
    
}
