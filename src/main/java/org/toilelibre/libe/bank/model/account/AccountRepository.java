package org.toilelibre.libe.bank.model.account;

import java.util.Collection;

public interface AccountRepository {

    public abstract String add (String ibanCode) throws BankAccountException;

    public abstract String get (String ibanCode) throws NoSuchAccountException;

    public abstract Collection<String> list ();

    public abstract boolean remove (String code) throws NoSuchAccountException;

    public abstract void clear ();

}