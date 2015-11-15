package org.toilelibre.libe.bank.impl.account.details;

import org.toilelibre.libe.bank.model.account.details.AccountDetailsRule;

public class CustomerAccountDetailsRule implements AccountDetailsRule {
    
    @Override
    public boolean hasCorrectSwiftCode (final String swiftCode) {
        return swiftCode.toUpperCase ().matches ("[A-Z]{4}[A-Z]{2}[A-Z0-9]{2}[A-Z0-9]{0,3}");
    }
    
}
