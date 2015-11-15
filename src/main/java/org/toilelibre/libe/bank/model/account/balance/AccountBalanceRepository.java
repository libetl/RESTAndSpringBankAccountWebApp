package org.toilelibre.libe.bank.model.account.balance;

import org.toilelibre.libe.bank.model.account.BankAccountException;

public interface AccountBalanceRepository {
    
    AccountBalance get (String iban);
    
    AccountBalance add (String iban, Double amount, AccountBalanceRule rule) throws IllegalAddOperationException;
    
    AccountBalance substract (String iban, Double amount, AccountBalanceRule rule) throws BankAccountException;
    
    AccountBalance setOverdraft (String iban, double amount, AccountBalanceRule rule) throws IllegalOverdraftValueException;
    
}
