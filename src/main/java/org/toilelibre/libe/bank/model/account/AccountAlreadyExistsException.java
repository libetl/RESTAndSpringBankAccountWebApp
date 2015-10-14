package org.toilelibre.libe.bank.model.account;

import org.toilelibre.libe.bank.model.account.error.ErrorCode;

public class AccountAlreadyExistsException extends BankAccountException {

    /**
     *
     */
    private static final long serialVersionUID = 2448686719364072998L;
    private final String      iban;

    public AccountAlreadyExistsException (final String existingIban) {
        this.iban = existingIban;
    }

    @Override
    public String toString () {
        return "Account already exists : " + this.iban;
    }

    @Override
    public ErrorCode getCode () {
        return ErrorCode.AccountAlreadyExists;
    }
}
