package org.toilelibre.libe.bank.model.account.balance;

import org.toilelibre.libe.bank.model.account.BankAccountException;

public interface AccountBalanceRepository {

    AccountBalance add (String iban, Double amount, AccountBalanceRule rule) throws IllegalAddOperationException;

    AccountBalance get (String iban);

    AccountBalance setOverdraft (String iban, double amount, AccountBalanceRule rule) throws IllegalOverdraftValueException;

    AccountBalance substract (String iban, Double amount, AccountBalanceRule rule) throws BankAccountException;

}
