package org.toilelibre.libe.bank.impl.account.history;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.FindAccountService;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;
import org.toilelibre.libe.bank.model.account.history.AccountHistory;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperationRule;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryRepository;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryService;
import org.toilelibre.libe.bank.model.account.history.IllegalAccountHistoryOperation;

public class AccountHistoryInMemoryService implements AccountHistoryService {
    
    @Inject
    private AccountHistoryRepository accountHistoryRepository;
    @Inject
    private FindAccountService       findAccountService;
                                     
    @Override
    @Cacheable (cacheNames = "accountHistory")
    public AccountHistory view (final String iban) throws NoSuchAccountException {
        this.findAccountService.find (iban);
        return this.accountHistoryRepository.viewHistory (iban);
    }
    
    @Override
    @CacheEvict (cacheNames = "accountHistory")
    public AccountHistoryOperation addToHistory (final String iban, final AccountHistoryOperation accountHistoryOperation, final AccountHistoryOperationRule rule)
            throws BankAccountException {
        if (!rule.operationAllowed (accountHistoryOperation)) {
            throw new IllegalAccountHistoryOperation (accountHistoryOperation);
        }
        this.findAccountService.find (iban);
        this.accountHistoryRepository.addToHistory (iban, accountHistoryOperation);
        return accountHistoryOperation;
    }
    
}
