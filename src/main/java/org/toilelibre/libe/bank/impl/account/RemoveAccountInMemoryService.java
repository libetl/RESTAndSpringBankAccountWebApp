package org.toilelibre.libe.bank.impl.account;

import javax.inject.Inject;

import org.springframework.cache.annotation.CacheEvict;

import org.toilelibre.libe.bank.model.account.AccountRepository;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;
import org.toilelibre.libe.bank.model.account.RemoveAccountService;

public class RemoveAccountInMemoryService implements RemoveAccountService {
    
    @Inject
    private AccountRepository accountRepository;
    
    @Override
    @CacheEvict (cacheNames = "account")
    public boolean remove (final String iban) throws NoSuchAccountException {
        return this.accountRepository.remove (iban);
    }
    
    @Override
    @CacheEvict (cacheNames = "account", allEntries = true)
    public void removeAll () {
        this.accountRepository.clear ();
    }
}
