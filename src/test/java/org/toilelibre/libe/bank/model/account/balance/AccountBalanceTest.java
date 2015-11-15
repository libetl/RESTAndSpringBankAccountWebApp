package org.toilelibre.libe.bank.model.account.balance;

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
import org.toilelibre.libe.bank.testutils.LogbackConfigRule;
import org.toilelibre.libe.bank.testutils.SmartLogRule;

@ContextConfiguration (loader = AnnotationConfigContextLoader.class, classes = { InMemoryAccountsAppConfig.class })
public class AccountBalanceTest {
    
    private static final double           ARBITRARY_CEIL_AMOUNT = 500;
                                                                
    // The test does not need to know what impl of the account value object it
    // uses
    @ClassRule
    public static final LogbackConfigRule LOGBACK_CONFIG_RULE   = new LogbackConfigRule ();
                                                                
    @Inject
    private AccountBalance                accountBalance;
                                          
    @Inject
    private AccountBalanceRule            accountBalanceRule;
                                          
    @ClassRule
    public static final SpringClassRule   SPRING_CLASS_RULE     = new SpringClassRule ();
                                                                
    @Rule
    public final SpringMethodRule         springMethodRule      = new SpringMethodRule ();
                                                                
    @Rule
    public SmartLogRule                   smartLogRule          = new SmartLogRule ();
                                                                
    @Before
    public void beforeTest () throws BankAccountException {
        // clears the account before the next test
        this.accountBalance.withdrawAndReportBalance (this.accountBalance.getBalance (), this.accountBalanceRule);
        this.accountBalance.setOverdraft (0.0d, this.accountBalanceRule);
    }
    
    /**
     * Tests that an empty account always has a balance of 0.0, not a NULL.
     */
    @Test
    public void accountWithoutMoneyShouldHaveZeroBalance () {
        
        // given the account, when an account is created
        
        // then
        Assertions.assertThat (this.accountBalance.getBalance ()).isNotNull ().isEqualTo (0);
    }
    
    /**
     * Adds money to the account and checks that the new balance is as expected.
     */
    @Test
    public void addPositiveAmountShouldAddItToTheBalance () {
        
        // given the account and two random amounts
        final double randomAmount1 = Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT;
        final double randomAmount2 = Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT;
        
        // when
        try {
            this.accountBalance.add (randomAmount1, this.accountBalanceRule);
            this.accountBalance.add (randomAmount2, this.accountBalanceRule);
        } catch (final IllegalAddOperationException realCause) {
            Assertions.fail ("Amount add should work", realCause);
        }
        
        // then
        Assertions.assertThat (this.accountBalance.getBalance ()).isNotNull ().isEqualTo (randomAmount1 + randomAmount2);
    }
    
    /**
     * Tests that an illegal withdrawal throws the expected exception. Use the
     * logic contained in CustomerAccountRule; feel free to refactor the
     * existing code.
     *
     * @throws IllegalBalanceException
     *             expected exception
     */
    @Test (expected = IllegalBalanceException.class)
    public void withdrawAndReportBalanceShouldThrowIllegalBalanceIfBalanceNegative () throws IllegalBalanceException {
        // given the account and two random amounts, having withdrawn > added
        final double added = Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT;
        final double withdrawn = Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT + added;
        try {
            this.accountBalance.add (added, this.accountBalanceRule);
        } catch (final IllegalAddOperationException realCause) {
            Assertions.fail ("Amount add should work", realCause);
        }
        
        // when
        try {
            this.accountBalance.withdrawAndReportBalance (withdrawn, this.accountBalanceRule);
        } catch (final IllegalBalanceException illegalBalanceException) {
            // then
            Assertions.assertThat (this.accountBalance.getBalance ()).isNotNull ().isEqualTo (added);
            throw illegalBalanceException;
        }
    }
    
    // Also implement missing unit tests for the above functionalities.
    /**
     * Add negative amount of Money should not work
     *
     * @throws IllegalAddOperationException
     *             expected exception
     */
    @Test (expected = IllegalAddOperationException.class)
    public void addNegativeAmountShouldThrowIllegalAddOperationException () throws IllegalAddOperationException {
        
        // given the account and two random amounts
        final double randomAmount1 = Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT;
        final double randomAmount2 = -Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT;
        try {
            this.accountBalance.add (randomAmount1, this.accountBalanceRule);
        } catch (final IllegalAddOperationException realCause) {
            Assertions.fail ("Amount add should work the first time", realCause);
        }
        
        // when
        try {
            this.accountBalance.add (randomAmount2, this.accountBalanceRule);
        } catch (final IllegalAddOperationException illegalAddOperationException) {
            // then
            Assertions.assertThat (this.accountBalance.getBalance ()).isNotNull ().isEqualTo (randomAmount1);
            throw illegalAddOperationException;
        }
    }
    
    /**
     * Tests that an withdrawal with a negative result balance can be allowed
     * iff the overdraft value is big enough
     *
     */
    @Test
    public void withdrawAndReportBalanceShouldWorkIfTheOverdraftIsBigEnough () throws IllegalBalanceException {
        // given the account and two random amounts, having withdrawn > added
        // and added - withDrawn > -overdraft
        final double added = Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT;
        final double withdrawn = Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT + added;
        
        final double overdraft = 2 * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT;
        try {
            this.accountBalance.setOverdraft (overdraft, this.accountBalanceRule);
        } catch (final IllegalOverdraftValueException realCause) {
            Assertions.fail ("Setting the overdraft should work", realCause);
        }
        try {
            this.accountBalance.add (added, this.accountBalanceRule);
        } catch (final IllegalAddOperationException realCause) {
            Assertions.fail ("Amount add should work", realCause);
        }
        
        // when
        try {
            this.accountBalance.withdrawAndReportBalance (withdrawn, this.accountBalanceRule);
        } catch (final IllegalBalanceException realCause) {
            Assertions.fail ("Amount withdraw should work", realCause);
        }
        
        // then
        Assertions.assertThat (this.accountBalance.getBalance ()).isNotNull ().isLessThan (0);
    }
    
    /**
     * Tests that an withdrawal with a negative result balance can be allowed
     * iff the overdraft value is big enough
     *
     */
    @Test (expected = IllegalBalanceException.class)
    public void withdrawAndReportBalanceShouldFailIfTheOverdraftIsNotEnough () throws IllegalBalanceException {
        // given the account and two random amounts, having withdrawn > added
        // and added - withDrawn > -overdraft
        final double added = Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT;
        final double withdrawn = Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT + added;
        
        // an overdraft that always will be lower than Math.abs (added -
        // withdrawn)
        final double overdraft = Math.abs (added - withdrawn - 1) - Math.abs (added - withdrawn - 1) % 100;
        
        try {
            this.accountBalance.setOverdraft (overdraft, this.accountBalanceRule);
        } catch (final IllegalOverdraftValueException realCause) {
            Assertions.fail ("Setting the overdraft should work", realCause);
        }
        try {
            this.accountBalance.add (added, this.accountBalanceRule);
        } catch (final IllegalAddOperationException realCause) {
            Assertions.fail ("Amount add should work", realCause);
        }
        
        // when
        try {
            this.accountBalance.withdrawAndReportBalance (withdrawn, this.accountBalanceRule);
        } catch (final IllegalBalanceException illegalBalanceException) {
            // then
            Assertions.assertThat (this.accountBalance.getBalance ()).isNotNull ().isEqualTo (added);
            throw illegalBalanceException;
        }
    }
    
    /**
     * Tests that setting an overdraft not rounded fails
     *
     */
    @Test (expected = IllegalOverdraftValueException.class)
    public void setOverdraftShouldFailIfTheOverdraftIsNotRounded () throws IllegalOverdraftValueException {
        // given the account and an overdraft not rounded
        final double overdraft = Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT - Math.random () * AccountBalanceTest.ARBITRARY_CEIL_AMOUNT / 100 + 1;
        
        // when
        try {
            this.accountBalance.setOverdraft (overdraft, this.accountBalanceRule);
        } catch (final IllegalOverdraftValueException illegalOverdraftValueException) {
            // then
            throw illegalOverdraftValueException;
        }
    }
    
    /**
     * Tests that setting a new overdraft > than the current balance fails
     *
     */
    @Test (expected = IllegalOverdraftValueException.class)
    public void setOverdraftShouldFailIfTheNewOverdraftIsGreaterThanTheBalance () throws IllegalOverdraftValueException {
        // given the account with a balance of -300 and an overdraft of -400
        final double overdraft = 400;
        final double newOverdraft = 200;
        final double withdrawAmount = 300;
        
        try {
            this.accountBalance.setOverdraft (overdraft, this.accountBalanceRule);
        } catch (final IllegalOverdraftValueException realCause) {
            Assertions.fail ("Setting the overdraft should work", realCause);
        }
        try {
            this.accountBalance.withdrawAndReportBalance (withdrawAmount, this.accountBalanceRule);
        } catch (final IllegalBalanceException realCause) {
            Assertions.fail ("Amount withdraw should work", realCause);
        }
        
        // when
        try {
            this.accountBalance.setOverdraft (newOverdraft, this.accountBalanceRule);
        } catch (final IllegalOverdraftValueException illegalOverdraftValueException) {
            // then
            throw illegalOverdraftValueException;
        }
    }
}
