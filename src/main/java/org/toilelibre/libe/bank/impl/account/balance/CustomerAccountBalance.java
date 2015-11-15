package org.toilelibre.libe.bank.impl.account.balance;

import org.toilelibre.libe.bank.model.account.balance.AccountBalance;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRule;
import org.toilelibre.libe.bank.model.account.balance.IllegalAddOperationException;
import org.toilelibre.libe.bank.model.account.balance.IllegalBalanceException;
import org.toilelibre.libe.bank.model.account.balance.IllegalOverdraftValueException;

public class CustomerAccountBalance implements AccountBalance {
    
    private double balance;
    private double overdraft;
                   
    public CustomerAccountBalance () {
        this.balance = 0;
        this.overdraft = 0;
    }
    
    @Override
    public void add (final Double addedAmount, final AccountBalanceRule rule) throws IllegalAddOperationException {
        if (!rule.canAddThisAmount (addedAmount)) {
            throw new IllegalAddOperationException (addedAmount);
        }
        this.balance += addedAmount;
    }
    
    @Override
    public Double getBalance () {
        return this.balance;
    }
    
    @Override
    public Double getOverdraft () {
        return this.overdraft;
    }
    
    @Override
    public Double withdrawAndReportBalance (final Double withdrawnAmount, final AccountBalanceRule rule) throws IllegalBalanceException {
        if (!rule.withdrawPermitted (this.balance - withdrawnAmount, this.overdraft)) {
            throw new IllegalBalanceException (this.balance - withdrawnAmount);
        }
        this.balance -= withdrawnAmount;
        return this.balance;
    }
    
    @Override
    public void setOverdraft (final Double overdraft1, final AccountBalanceRule rule) throws IllegalOverdraftValueException {
        if (!rule.canSetThisOverdraft (this.balance, overdraft1)) {
            throw new IllegalOverdraftValueException (overdraft1);
        }
        this.overdraft = overdraft1;
    }
    
    /**
     * AccountBalance is an entity
     *
     * It must be cloned if persisted in memory
     *
     * @return a clone of this accountBalance
     */
    @Override
    public AccountBalance asCopy () {
        final CustomerAccountBalance result = new CustomerAccountBalance ();
        result.balance = this.balance;
        result.overdraft = this.overdraft;
        return result;
    }
}
