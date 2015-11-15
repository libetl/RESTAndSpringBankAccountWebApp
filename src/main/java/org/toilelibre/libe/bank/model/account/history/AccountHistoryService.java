package org.toilelibre.libe.bank.model.account.history;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;

public interface AccountHistoryService {
    
    AccountHistory view (String iban) throws NoSuchAccountException;
    
    AccountHistoryOperation addToHistory (String iban, AccountHistoryOperation accountHistoryOperation, AccountHistoryOperationRule rule) throws BankAccountException;
    
}
