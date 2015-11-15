package org.toilelibre.libe.bank.model.account;

public interface RemoveAccountService {
    
    boolean remove (String iban) throws NoSuchAccountException;
    
    void removeAll ();
}
