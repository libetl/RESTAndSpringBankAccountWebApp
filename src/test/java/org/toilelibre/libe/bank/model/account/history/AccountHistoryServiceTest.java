package org.toilelibre.libe.bank.model.account.history;

import java.util.List;

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
import org.toilelibre.libe.bank.model.account.CreateAccountService;
import org.toilelibre.libe.bank.model.account.RemoveAccountService;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRule;
import org.toilelibre.libe.bank.model.account.balance.IllegalBalanceException;
import org.toilelibre.libe.bank.model.account.operation.AccountOperationService;
import org.toilelibre.libe.bank.testutils.AccountHelper;
import org.toilelibre.libe.bank.testutils.LogbackConfigRule;
import org.toilelibre.libe.bank.testutils.SmartLogRule;
import org.toilelibre.libe.bank.testutils.TestConfig;

@ContextConfiguration (loader = AnnotationConfigContextLoader.class, classes = { InMemoryAccountsAppConfig.class, TestConfig.class })
public class AccountHistoryServiceTest {

    @ClassRule
    public static final LogbackConfigRule LOGBACK_CONFIG_RULE = new LogbackConfigRule ();

    @ClassRule
    public static final SpringClassRule   SPRING_CLASS_RULE   = new SpringClassRule ();

    @Rule
    public final SpringMethodRule         springMethodRule    = new SpringMethodRule ();

    @Rule
    public SmartLogRule                   smartLogRule        = new SmartLogRule ();
    @Inject
    private CreateAccountService          createAccountService;
    
    @Inject
    private RemoveAccountService          removeAccountService;
    
    @Inject
    private AccountHistoryService         accountHistoryService;
    
    @Inject
    private AccountHistoryOperationRule   historyRule;
    
    @Inject
    private AccountBalanceRule            accountBalanceRule;
    
    @Inject
    private AccountHelper               accountHelper;
    
    @Inject
    private AccountRule                   accountRule;
    
    @Inject
    private AccountOperationService       accountOperationService;

    @Before
    public void clearAccounts () {
        this.removeAccountService.removeAll ();
    }
    
    @Test
    public void anOperationShouldAppendToTheHistoryLines () throws BankAccountException {
        //given
        String iban = this.accountHelper.getEmptyAccount ();
        this.createAccountService.create (iban, accountRule);
        
        //when
        this.accountOperationService.deposit  (iban, 100, this.accountBalanceRule, this.historyRule);
        
        //then
        List<AccountHistoryOperation> operations = this.accountHistoryService.view (iban).getHistoryLines ();
        Assertions.assertThat (operations).isNotNull ().isNotEmpty ().hasSize (1);
        Assertions.assertThat (operations.get (0).getAmount ()).isEqualTo (100);
    }
    
    @Test (expected = IllegalBalanceException.class)
    public void withdrawTooMuchShouldDiscardTheTransaction () throws BankAccountException {
        //given
        String iban = this.accountHelper.getEmptyAccount ();
        this.createAccountService.create (iban, accountRule);
        this.accountOperationService.deposit  (iban, 100, this.accountBalanceRule, this.historyRule);
        
        //when
        try {
            this.accountOperationService.withdraw  (iban, 130, this.accountBalanceRule, this.historyRule);
        } catch (IllegalBalanceException ibe) {
            //then
            List<AccountHistoryOperation> operations = this.accountHistoryService.view (iban).getHistoryLines ();
            Assertions.assertThat (operations).isNotNull ().isNotEmpty ().hasSize (1);
            Assertions.assertThat (operations.get (0).getAmount ()).isEqualTo (100);
            throw ibe;
        }
        
    }
}
