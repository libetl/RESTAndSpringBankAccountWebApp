package org.toilelibre.libe.bank.impl.account;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.toilelibre.libe.bank.model.account.AccountAlreadyExistsException;
import org.toilelibre.libe.bank.model.account.AccountRepository;
import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;

public class BasicHashSetAccountRepository implements AccountRepository {
    
    private final Set<String> knownAccounts;
    
    public BasicHashSetAccountRepository () {
        this.knownAccounts = new HashSet<String> ();
    }
    
    /*
     * (non-Javadoc)
     *
     * @see
     * org.toilelibre.libe.bank.impl.account.AccountRepository#addOrUpdate(org.
     * toilelibre.libe.bank.model.account.Account)
     */
    @Override
    public String add (final String iban) throws BankAccountException {
        final boolean alreadyExistingIban = this.knownAccounts.contains (iban);
        
        if (alreadyExistingIban) {
            throw new AccountAlreadyExistsException (iban);
        }
        
        this.knownAccounts.add (iban);
        return iban;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see
     * org.toilelibre.libe.bank.impl.account.AccountRepository#get(java.lang.
     * String)
     */
    @Override
    public String get (final String iban) throws NoSuchAccountException {
        this.simulateSlowService ();
        if (!this.knownAccounts.contains (iban)) {
            throw new NoSuchAccountException (iban);
        }
        return iban;
    }
    
    /*
     * (non-Javadoc)
     *
     * @see org.toilelibre.libe.bank.impl.account.AccountRepository#list()
     */
    @Override
    public Collection<String> list () {
        this.simulateSlowService ();
        return this.knownAccounts;
    }
    
    // Don't do this at home
    private void simulateSlowService () {
        try {
            final long time = 1000L;
            Thread.sleep (time);
        } catch (final InterruptedException e) {
            throw new IllegalStateException (e);
        }
    }
    
    @Override
    public boolean remove (final String iban) throws NoSuchAccountException {
        this.simulateSlowService ();
        this.get (iban);
        return this.knownAccounts.remove (iban);
    }
    
    @Override
    public void clear () {
        this.knownAccounts.clear ();
    }
}
