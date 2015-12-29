package org.toilelibre.libe.bank.model.account.details;

import javax.inject.Inject;

import org.fest.assertions.api.Assertions;
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
import org.toilelibre.libe.bank.testutils.AccountHelper;
import org.toilelibre.libe.bank.testutils.LogbackConfigRule;
import org.toilelibre.libe.bank.testutils.SmartLogRule;
import org.toilelibre.libe.bank.testutils.TestConfig;

@ContextConfiguration (loader = AnnotationConfigContextLoader.class, classes = { InMemoryAccountsAppConfig.class, TestConfig.class })
public class AccountDetailsServiceTest {

    @ClassRule
    public static final LogbackConfigRule LOGBACK_CONFIG_RULE = new LogbackConfigRule ();

    @ClassRule
    public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule ();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule ();

    @Rule
    public SmartLogRule          smartLogRule = new SmartLogRule ();
    @Inject
    private CreateAccountService createAccountService;

    @Inject
    private FindAccountService findAccountService;

    @Inject
    private AccountDetailsService accountDetailsService;

    @Inject
    private AccountDetailsRule accountDetailsRule;

    @Inject
    private AccountDetails.Builder detailsBuilder;

    @Inject
    private AccountHelper accountHelper;

    @Inject
    private AccountRule accountRule;

    @Test
    public void setDetailsShouldWorkIfAllFieldsAreSet () throws BankAccountException {
        // given an account without detail
        final String iban = this.createAccountService.create (this.accountHelper.getEmptyAccount (), this.accountRule);

        // when
        this.accountDetailsService.update (iban, this.accountHelper.getDetails (this.detailsBuilder, this.accountDetailsRule));

        // then
        final AccountDetails details = this.accountDetailsService.view (iban);
        Assertions.assertThat (this.findAccountService.find (iban)).isEqualTo (iban);
        Assertions.assertThat (details).isNotNull ();
        Assertions.assertThat (details.getSwiftCode ()).isNotNull ();
        Assertions.assertThat (this.accountDetailsRule.hasCorrectSwiftCode (details.getSwiftCode ())).isTrue ();
    }
}
