package org.toilelibre.libe.bank.model.account;

import java.util.Collection;

public interface FindAccountService {

    String find (String iban) throws NoSuchAccountException;

    Collection<String> findAll ();
}
