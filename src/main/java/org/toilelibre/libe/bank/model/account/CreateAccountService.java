package org.toilelibre.libe.bank.model.account;

public interface CreateAccountService {

    String create (String newIban, AccountRule accountRule) throws BankAccountException;
}
