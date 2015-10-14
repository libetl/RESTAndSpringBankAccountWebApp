package org.toilelibre.libe.bank.model.account.details;

public interface AccountDetailsRepository {

    AccountDetails view (String iban);

    void update (String iban, AccountDetails details);

}
