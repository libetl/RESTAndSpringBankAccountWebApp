package org.toilelibre.libe.bank.model.account.balance;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;

public interface AccountBalanceService {

    AccountBalance add (String iban, Double amount, AccountBalanceRule rule) throws BankAccountException;

    AccountBalance get (String iban) throws NoSuchAccountException;

    AccountBalance setOverdraft (String iban, double amount, AccountBalanceRule rule) throws BankAccountException;

    AccountBalance substract (String iban, Double amount, AccountBalanceRule rule) throws BankAccountException;

}
