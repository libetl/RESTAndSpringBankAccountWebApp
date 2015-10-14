package org.toilelibre.libe.bank.impl.account.details;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.toilelibre.libe.bank.model.account.details.AccountDetails;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsRepository;

public class BasicHashMapAccountDetailsRepository implements AccountDetailsRepository {

    private final Map<String, AccountDetails> detailsMap;

    @Inject
    private AccountDetails.Builder            detailsBuilder;

    public BasicHashMapAccountDetailsRepository () {
        this.detailsMap = new HashMap<String, AccountDetails> ();
    }

    @Override
    public AccountDetails view (final String iban) {
        if (this.detailsMap.get (iban) == null) {
            this.detailsMap.put (iban, this.detailsBuilder.buildWithUnknownValues ());
        }
        return this.detailsMap.get (iban);
    }

    @Override
    public void update (final String iban, final AccountDetails details) {
        this.detailsMap.put (iban, details);
    }

}
