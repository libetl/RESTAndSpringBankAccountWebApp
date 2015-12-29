package org.toilelibre.libe.bank.model.account.details;

public interface AccountDetailsRepository {

    void update (String iban, AccountDetails details);

    AccountDetails view (String iban);

}
