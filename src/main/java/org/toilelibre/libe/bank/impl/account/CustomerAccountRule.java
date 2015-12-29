package org.toilelibre.libe.bank.impl.account;

import org.toilelibre.libe.bank.model.account.AccountRule;

public class CustomerAccountRule implements AccountRule {

    @Override
    public boolean isValidIban (final String iban) {
        return iban.matches ("[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}");
    }

}
