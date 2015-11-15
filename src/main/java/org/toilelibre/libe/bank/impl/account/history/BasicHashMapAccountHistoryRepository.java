package org.toilelibre.libe.bank.impl.account.history;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.toilelibre.libe.bank.model.account.history.AccountHistory;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryRepository;

public class BasicHashMapAccountHistoryRepository implements AccountHistoryRepository {
    
    private final Map<String, AccountHistory> historyMap;
                                              
    @Inject
    private AccountHistory                    templateAccountHistory;
                                              
    public BasicHashMapAccountHistoryRepository () {
        this.historyMap = new HashMap<String, AccountHistory> ();
    }
    
    @Override
    public AccountHistory viewHistory (final String iban) {
        this.ensureHistoryExists (iban);
        return this.historyMap.get (iban);
    }
    
    @Override
    public void addToHistory (final String iban, final AccountHistoryOperation accountHistoryOperation) {
        this.ensureHistoryExists (iban);
        this.historyMap.get (iban).addHistoryLine (accountHistoryOperation);
    }
    
    private void ensureHistoryExists (final String iban) {
        if (!this.historyMap.containsKey (iban)) {
            this.historyMap.put (iban, this.templateAccountHistory.asCopy ());
        }
    }
}
