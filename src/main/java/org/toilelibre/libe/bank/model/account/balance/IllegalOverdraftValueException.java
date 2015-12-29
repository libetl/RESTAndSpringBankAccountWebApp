package org.toilelibre.libe.bank.model.account.balance;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.error.ErrorCode;

public class IllegalOverdraftValueException extends BankAccountException {

    /**
     *
     */
    private static final long serialVersionUID = 476752573612669793L;
    private final Double      overdraft;

    public IllegalOverdraftValueException (final Double illegalOverdraft) {
        this.overdraft = illegalOverdraft;
    }

    @Override
    public ErrorCode getCode () {
        return ErrorCode.IllegalOverdraft;
    }

    @Override
    public String toString () {
        return "Illegal overdraft value : " + this.overdraft;
    }
}
