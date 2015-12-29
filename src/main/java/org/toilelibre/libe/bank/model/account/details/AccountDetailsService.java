package org.toilelibre.libe.bank.model.account.details;

import org.toilelibre.libe.bank.model.account.NoSuchAccountException;

public interface AccountDetailsService {

    void update (String iban, AccountDetails details) throws NoSuchAccountException;

    AccountDetails view (String iban) throws NoSuchAccountException;

}
