package org.toilelibre.libe.bank.impl.account.balance;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.balance.AccountBalance;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRepository;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRule;
import org.toilelibre.libe.bank.model.account.balance.IllegalAddOperationException;
import org.toilelibre.libe.bank.model.account.balance.IllegalOverdraftValueException;

public class BasicHashMapAccountBalanceRepository implements AccountBalanceRepository {
    
    private final Map<String, AccountBalance> balanceMap;
                                              
    @Inject
    private AccountBalance                    templateAccountBalance;
                                              
    public BasicHashMapAccountBalanceRepository () {
        this.balanceMap = new HashMap<String, AccountBalance> ();
    }
    
    @Override
    public AccountBalance get (final String iban) {
        this.ensureBalanceExists (iban);
        return this.balanceMap.get (iban);
    }
    
    @Override
    public AccountBalance add (final String iban, final Double amount, final AccountBalanceRule rule) throws IllegalAddOperationException {
        this.ensureBalanceExists (iban);
        this.balanceMap.get (iban).add (amount, rule);
        return this.balanceMap.get (iban);
    }
    
    @Override
    public AccountBalance substract (final String iban, final Double amount, final AccountBalanceRule rule) throws BankAccountException {
        this.ensureBalanceExists (iban);
        this.balanceMap.get (iban).withdrawAndReportBalance (amount, rule);
        return this.balanceMap.get (iban);
    }
    
    private void ensureBalanceExists (final String iban) {
        if (!this.balanceMap.containsKey (iban)) {
            this.balanceMap.put (iban, this.templateAccountBalance.asCopy ());
        }
    }
    
    @Override
    public AccountBalance setOverdraft (String iban, double amount, AccountBalanceRule rule) throws IllegalOverdraftValueException {
        this.ensureBalanceExists (iban);
        this.balanceMap.get (iban).setOverdraft (amount, rule);
        return this.balanceMap.get (iban);
    }
}
