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
import org.toilelibre.libe.bank.testutils.AccountHelper;
import org.toilelibre.libe.bank.testutils.LogbackConfigRule;
import org.toilelibre.libe.bank.testutils.SmartLogRule;
import org.toilelibre.libe.bank.testutils.TestConfig;

@ContextConfiguration (loader = AnnotationConfigContextLoader.class, classes = { InMemoryAccountsAppConfig.class, TestConfig.class })
public class CreateAccountServiceTest {

    @ClassRule
    public static final LogbackConfigRule LOGBACK_CONFIG_RULE = new LogbackConfigRule ();

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule ();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule ();

    @Rule
    public SmartLogRule smartLogRule = new SmartLogRule ();

    @Inject
    private AccountRule accountRule;

    @Inject
    private CreateAccountService createAccountService;

    @Inject
    private RemoveAccountService removeAccountService;

    @Inject
    private AccountHelper accountHelper;

    @Before
    public void clearData () {
        this.removeAccountService.removeAll ();
    }

    @Test (expected = BankAccountException.class)
    public void createExistingIbanShouldThrowAccountAlreadyExistsException () throws BankAccountException {
        // given an account
        final String iban = this.accountHelper.getEmptyAccount ();
        this.createAccountService.create (iban, this.accountRule);

        // when
        this.createAccountService.create (iban, this.accountRule);

        // then should fail
    }
}
