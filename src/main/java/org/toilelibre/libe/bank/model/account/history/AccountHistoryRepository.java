package org.toilelibre.libe.bank.model.account.history;

public interface AccountHistoryRepository {
    
    AccountHistory viewHistory (String iban);
    
    void addToHistory (String iban, AccountHistoryOperation accountHistoryOperation);
    
}
