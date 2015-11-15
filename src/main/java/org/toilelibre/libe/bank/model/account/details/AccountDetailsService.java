package org.toilelibre.libe.bank.model.account.details;

import org.toilelibre.libe.bank.model.account.NoSuchAccountException;

public interface AccountDetailsService {
    
    AccountDetails view (String iban) throws NoSuchAccountException;
    
    void update (String iban, AccountDetails details) throws NoSuchAccountException;
    
}
