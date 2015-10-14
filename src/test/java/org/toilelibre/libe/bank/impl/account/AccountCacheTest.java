package org.toilelibre.libe.bank.impl.account;

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
import org.toilelibre.libe.bank.model.account.AccountRule;
import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.FindAccountService;
import org.toilelibre.libe.bank.model.account.RemoveAccountService;
import org.toilelibre.libe.bank.model.account.CreateAccountService;
import org.toilelibre.libe.bank.testutils.AccountHelper;
import org.toilelibre.libe.bank.testutils.LogbackConfigRule;
import org.toilelibre.libe.bank.testutils.SmartLogRule;
import org.toilelibre.libe.bank.testutils.TestConfig;

@ContextConfiguration (loader = AnnotationConfigContextLoader.class, classes = { InMemoryAccountsAppConfig.class, TestConfig.class })
public class AccountCacheTest {

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
    private FindAccountService            findAccountService;
    @Inject
    private CreateAccountService  updateOrCreateAccountService;
    @Inject
    private RemoveAccountService          removeAccountService;
    @Inject
    private AccountHelper                 accountHelper;

    @Before
    public void clearData () {
        this.removeAccountService.removeAll ();
    }

    @Test
    public void createAndFindAccountShouldRememberTheObjectInCache () throws BankAccountException {
        // given
        final String account = this.accountHelper.getEmptyAccount ();
        this.updateOrCreateAccountService.create (account, this.accountRule);
        // should be slow
        final long time1 = System.currentTimeMillis ();
        final String account2 = this.findAccountService.find (account);
        final long time2 = System.currentTimeMillis ();
        Assertions.assertThat (time2 - time1).isGreaterThanOrEqualTo (1000);
        // should be fast
        final long time3 = System.currentTimeMillis ();

        // when
        this.findAccountService.find (account);
        final long time4 = System.currentTimeMillis ();

        // then
        Assertions.assertThat (time4 - time3).isLessThan (100);
        Assertions.assertThat (account2).isNotNull ().isEqualTo (account);
    }
}
