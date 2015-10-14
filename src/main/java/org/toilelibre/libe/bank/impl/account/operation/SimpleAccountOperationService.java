package org.toilelibre.libe.bank.impl.account.operation;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRule;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceService;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperationRule;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryService;
import org.toilelibre.libe.bank.model.account.history.IllegalAccountHistoryOperation;
import org.toilelibre.libe.bank.model.account.operation.AccountOperationService;

public class SimpleAccountOperationService implements AccountOperationService {

    private static Logger           LOGGER = LoggerFactory.getLogger (SimpleAccountOperationService.class);

    @Inject
    private AccountHistoryService   accountHistoryService;

    @Inject
    private AccountBalanceService   accountBalanceService;

    @Inject
    private AccountHistoryOperation accountHistoryOperation;

    @Override
    public AccountHistoryOperation deposit (final String iban, final double amount, final AccountBalanceRule balanceRule, final AccountHistoryOperationRule historyRule) throws BankAccountException {
        this.accountBalanceService.add (iban, amount, balanceRule);
        return this.accountHistoryService.addToHistory (iban, this.accountHistoryOperation.newCredit ("Bank Deposit", amount), historyRule);
    }

    @Override
    public AccountHistoryOperation withdraw (final String iban, final double amount, final AccountBalanceRule balanceRule, final AccountHistoryOperationRule historyRule) throws BankAccountException {
        this.accountBalanceService.substract (iban, amount, balanceRule);
        try {
            return this.accountHistoryService.addToHistory (iban, this.accountHistoryOperation.newDebit ("Bank Withdrawal", amount), historyRule);
        } catch (final IllegalAccountHistoryOperation operation) {
            SimpleAccountOperationService.LOGGER.warn ("Withdraw operation not permitted, rollback", operation);
            this.accountBalanceService.get (iban).add (amount, balanceRule);
            throw operation;
        }
    }

    @Override
    public AccountHistoryOperation transfer (final String iban, final double amount, final String recipient, final AccountBalanceRule balanceRule, final AccountHistoryOperationRule historyRule) throws BankAccountException {
        this.accountBalanceService.substract (iban, amount, balanceRule);
        try {
            return this.accountHistoryService.addToHistory (iban, this.accountHistoryOperation.newDebit (recipient, amount), historyRule);
        } catch (final IllegalAccountHistoryOperation operation) {
            SimpleAccountOperationService.LOGGER.warn ("Withdraw operation not permitted, rollback", operation);
            this.accountBalanceService.get (iban).add (amount, balanceRule);
            throw operation;
        }
    }

}
