package org.toilelibre.libe.bank.model.account.history;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;

public interface AccountHistoryService {

    AccountHistoryOperation addToHistory (String iban, AccountHistoryOperation accountHistoryOperation, AccountHistoryOperationRule rule) throws BankAccountException;

    AccountHistory view (String iban) throws NoSuchAccountException;

}
