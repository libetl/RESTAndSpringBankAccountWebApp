package org.toilelibre.libe.bank.model.account;

import org.toilelibre.libe.bank.model.account.error.ErrorCode;

public class NoSuchAccountException extends BankAccountException {

    /**
     *
     */
    private static final long serialVersionUID = 2448686719364072998L;
    private final String      iban;

    public NoSuchAccountException (final String notExistingIban) {
        this.iban = notExistingIban;
    }

    @Override
    public ErrorCode getCode () {
        return ErrorCode.NoSuchAccount;
    }

    @Override
    public String toString () {
        return "No such account for Iban Code : " + this.iban;
    }
}
