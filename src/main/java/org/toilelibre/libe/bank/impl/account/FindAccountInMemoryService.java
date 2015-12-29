package org.toilelibre.libe.bank.impl.account;

import java.util.Collection;

import javax.inject.Inject;

import org.springframework.cache.annotation.Cacheable;
import org.toilelibre.libe.bank.model.account.AccountRepository;
import org.toilelibre.libe.bank.model.account.FindAccountService;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;

public class FindAccountInMemoryService implements FindAccountService {

    @Inject
    private AccountRepository accountRepository;

    @Override
    @Cacheable (cacheNames = "account")
    public String find (final String iban) throws NoSuchAccountException {
        return this.accountRepository.get (iban);
    }

    @Override
    @Cacheable (cacheNames = "account")
    public Collection<String> findAll () {
        return this.accountRepository.list ();
    }
}
