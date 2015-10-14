package org.toilelibre.libe.bank.model.account.operation;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRule;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperationRule;

public interface AccountOperationService {

    AccountHistoryOperation deposit (String iban, double amount, AccountBalanceRule balanceRule, AccountHistoryOperationRule historyRule) throws BankAccountException;

    AccountHistoryOperation withdraw (String iban, double amount, AccountBalanceRule balanceRule, AccountHistoryOperationRule historyRule) throws BankAccountException;

    AccountHistoryOperation transfer (String iban, double amount, String recipient, AccountBalanceRule balanceRule, AccountHistoryOperationRule historyRule) throws BankAccountException;

}
