package org.toilelibre.libe.bank.impl.account.balance;

import javax.inject.Inject;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.FindAccountService;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;
import org.toilelibre.libe.bank.model.account.balance.AccountBalance;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRepository;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRule;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceService;

public class CustomerAccountBalanceService implements AccountBalanceService {
    
    @Inject
    private AccountBalanceRepository accountBalanceRepository;
    @Inject
    private FindAccountService       findAccountService;
                                     
    @Override
    @Cacheable (cacheNames = "accountBalance")
    public AccountBalance get (final String iban) throws NoSuchAccountException {
        this.findAccountService.find (iban);
        return this.accountBalanceRepository.get (iban);
    }
    
    @Override
    @CachePut (cacheNames = "accountBalance")
    public AccountBalance add (final String iban, final Double amount, final AccountBalanceRule rule) throws BankAccountException {
        this.findAccountService.find (iban);
        return this.accountBalanceRepository.add (iban, amount, rule);
    }
    
    @Override
    @CachePut (cacheNames = "accountBalance")
    public AccountBalance substract (final String iban, final Double amount, final AccountBalanceRule rule) throws BankAccountException {
        this.findAccountService.find (iban);
        return this.accountBalanceRepository.substract (iban, amount, rule);
    }
    
    @Override
    @CachePut (cacheNames = "accountBalance")
    public AccountBalance setOverdraft (String iban, double amount, final AccountBalanceRule rule) throws BankAccountException {
        this.findAccountService.find (iban);
        return this.accountBalanceRepository.setOverdraft (iban, amount, rule);
    }
}
