package org.toilelibre.libe.bank.impl.account;

import javax.inject.Inject;

import org.springframework.cache.annotation.CachePut;

import org.toilelibre.libe.bank.model.account.AccountRepository;
import org.toilelibre.libe.bank.model.account.AccountRule;
import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.IllegalIbanException;
import org.toilelibre.libe.bank.model.account.CreateAccountService;

public class CreateAccountInMemoryService implements CreateAccountService {

    @Inject
    private AccountRepository accountRepository;

    @Override
    @CachePut (cacheNames = "account")
    public String create (final String newIban, final AccountRule accountRule) throws BankAccountException {
        if (!accountRule.isValidIban (newIban)) {
            throw new IllegalIbanException (newIban);
        }
        this.accountRepository.add (newIban);
        return newIban;
    }
}
