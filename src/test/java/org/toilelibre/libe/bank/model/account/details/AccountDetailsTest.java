package org.toilelibre.libe.bank.model.account.details;

import javax.inject.Inject;

import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import org.toilelibre.libe.bank.impl.account.details.CustomerAccountDetails;
import org.toilelibre.libe.bank.impl.account.details.PersonAccountDetailsContact;
import org.toilelibre.libe.bank.impl.account.details.PersonAccountDetailsContact.Title;
import org.toilelibre.libe.bank.ioc.InMemoryAccountsAppConfig;
import org.toilelibre.libe.bank.testutils.LogbackConfigRule;
import org.toilelibre.libe.bank.testutils.SmartLogRule;

@ContextConfiguration (loader = AnnotationConfigContextLoader.class, classes = { InMemoryAccountsAppConfig.class })
public class AccountDetailsTest {
    
    // The test does not need to know what impl of the account value object it
    // uses
    @ClassRule
    public static final LogbackConfigRule LOGBACK_CONFIG_RULE = new LogbackConfigRule ();
                                                              
    @ClassRule
    public static final SpringClassRule   SPRING_CLASS_RULE   = new SpringClassRule ();
                                                              
    @Rule
    public final SpringMethodRule         springMethodRule    = new SpringMethodRule ();
                                                              
    @Rule
    public SmartLogRule                   smartLogRule        = new SmartLogRule ();
                                                              
    @Inject
    private AccountDetailsRule            accountDetailsRule;
                                          
    @Test (expected = IllegalSwiftCodeException.class)
    public void builderWithIncorrectSwiftCodeShouldThrowIllegalIbanException () throws IllegalSwiftCodeException {
        new CustomerAccountDetails.Builder ().withSwiftCode ("test").withContact (new PersonAccountDetailsContact (Title.MR, "John", "Doe")).withAddress ("112 downing street")
                .withCity ("London").withZipCode ("EC1A 1BB").withState ("England").withCountry ("UK").build (this.accountDetailsRule);
    }
    
    @Test
    public void builderWithCorrectSwiftCodeShouldWork () throws IllegalSwiftCodeException {
        new CustomerAccountDetails.Builder ().withSwiftCode ("EBAPFRPPPSA").withContact (new PersonAccountDetailsContact (Title.MR, "John", "Doe"))
                .withAddress ("112 downing street").withCity ("London").withZipCode ("EC1A 1BB").withState ("England").withCountry ("UK").build (this.accountDetailsRule);
    }
    
}
