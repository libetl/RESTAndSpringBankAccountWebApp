package org.toilelibre.libe.bank.model.account.history;

public interface AccountHistoryRepository {

    void addToHistory (String iban, AccountHistoryOperation accountHistoryOperation);

    AccountHistory viewHistory (String iban);

}
