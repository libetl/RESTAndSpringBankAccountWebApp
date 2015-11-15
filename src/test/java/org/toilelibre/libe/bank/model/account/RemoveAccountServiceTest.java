package org.toilelibre.libe.bank.model.account;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import org.toilelibre.libe.bank.ioc.InMemoryAccountsAppConfig;
import org.toilelibre.libe.bank.model.account.AccountRule;
import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.CreateAccountService;
import org.toilelibre.libe.bank.model.account.FindAccountService;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;
import org.toilelibre.libe.bank.model.account.RemoveAccountService;
import org.toilelibre.libe.bank.testutils.AccountHelper;
import org.toilelibre.libe.bank.testutils.LogbackConfigRule;
import org.toilelibre.libe.bank.testutils.SmartLogRule;
import org.toilelibre.libe.bank.testutils.TestConfig;

@ContextConfiguration (loader = AnnotationConfigContextLoader.class, classes = { InMemoryAccountsAppConfig.class, TestConfig.class })
public class RemoveAccountServiceTest {
    
    @ClassRule
    public static final LogbackConfigRule LOGBACK_CONFIG_RULE = new LogbackConfigRule ();
                                                              
    @ClassRule
    public static final SpringClassRule   SPRING_CLASS_RULE   = new SpringClassRule ();
                                                              
    @Rule
    public final SpringMethodRule         springMethodRule    = new SpringMethodRule ();
                                                              
    @Rule
    public SmartLogRule                   smartLogRule        = new SmartLogRule ();
                                                              
    @Inject
    private AccountRule                   accountRule;
                                          
    @Inject
    private CreateAccountService          createAccountService;
                                          
    @Inject
    private RemoveAccountService          removeAccountService;
                                          
    @Inject
    private FindAccountService            findAccountService;
                                          
    @Inject
    private AccountHelper                 accountHelper;
                                          
    @Before
    public void clearData () {
        this.removeAccountService.removeAll ();
    }
    
    @Test (expected = NoSuchAccountException.class)
    public void removeAccountShouldWork () throws BankAccountException {
        // given an account
        this.createAccountService.create (this.accountHelper.getEmptyAccount (), this.accountRule);
        
        // when
        this.removeAccountService.remove (this.accountHelper.getEmptyAccount ());
        
        // then should fail
        this.findAccountService.find (this.accountHelper.getEmptyAccount ());
    }
    
    @Test (expected = NoSuchAccountException.class)
    public void removeNotExistingAccountShouldThrowNoSuchAccountException () throws BankAccountException {
        // given nothing
        
        // when
        this.removeAccountService.remove (this.accountHelper.getEmptyAccount ());
        
        // then should fail
        
    }
}
