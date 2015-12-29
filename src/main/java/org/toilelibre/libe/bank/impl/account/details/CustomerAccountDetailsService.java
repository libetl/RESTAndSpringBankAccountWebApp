package org.toilelibre.libe.bank.impl.account.details;

import javax.inject.Inject;

import org.toilelibre.libe.bank.model.account.FindAccountService;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;
import org.toilelibre.libe.bank.model.account.details.AccountDetails;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsRepository;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsService;

public class CustomerAccountDetailsService implements AccountDetailsService {

    @Inject
    private AccountDetailsRepository accountDetailsRepository;
    @Inject
    private FindAccountService       findAccountService;

    @Override
    public void update (final String iban, final AccountDetails details) throws NoSuchAccountException {
        this.findAccountService.find (iban);
        this.accountDetailsRepository.update (iban, details);
    }

    @Override
    public AccountDetails view (final String iban) throws NoSuchAccountException {
        this.findAccountService.find (iban);
        return this.accountDetailsRepository.view (iban);
    }

}
