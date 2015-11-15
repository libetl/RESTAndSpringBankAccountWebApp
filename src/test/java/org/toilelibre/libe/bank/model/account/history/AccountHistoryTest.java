package org.toilelibre.libe.bank.model.account.history;

import javax.inject.Inject;

import org.fest.assertions.api.Assertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import org.toilelibre.libe.bank.ioc.InMemoryAccountsAppConfig;
import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.testutils.AccountHelper;
import org.toilelibre.libe.bank.testutils.LogbackConfigRule;
import org.toilelibre.libe.bank.testutils.SmartLogRule;
import org.toilelibre.libe.bank.testutils.TestConfig;

@ContextConfiguration (loader = AnnotationConfigContextLoader.class, classes = { InMemoryAccountsAppConfig.class, TestConfig.class })
public class AccountHistoryTest {
    
    @ClassRule
    public static final LogbackConfigRule LOGBACK_CONFIG_RULE = new LogbackConfigRule ();
                                                              
    @ClassRule
    public static final SpringClassRule   SPRING_CLASS_RULE   = new SpringClassRule ();
                                                              
    @Rule
    public final SpringMethodRule         springMethodRule    = new SpringMethodRule ();
                                                              
    @Rule
    public SmartLogRule                   smartLogRule        = new SmartLogRule ();
                                                              
    @Inject
    private AccountHistory                accountHistory;
                                          
    @Inject
    private AccountHistoryOperation       accountHistoryOperation;
                                          
    @Before
    public void beforeTest () throws BankAccountException {
    }
    
    @Test
    public void addHistoryLineAppendsOneLineToTheHistory () {
        // given an empty history
        Assertions.assertThat (this.accountHistory.getHistoryLines ()).isEmpty ();
        final AccountHistoryOperation historyLine = AccountHelper.getNewHistoryLine (accountHistoryOperation);
        
        // when
        this.accountHistory.addHistoryLine (historyLine);
        
        // then
        Assertions.assertThat (this.accountHistory.getHistoryLines ()).contains (historyLine);
        
    }
}