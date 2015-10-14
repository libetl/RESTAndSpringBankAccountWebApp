package org.toilelibre.libe.bank.model.account.balance;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.error.ErrorCode;

public class IllegalAddOperationException extends BankAccountException {

    /**
     *
     */
    private static final long serialVersionUID = 2481702858074663139L;
    private final Double      amount;

    public IllegalAddOperationException (final Double illegalAmount) {
        this.amount = illegalAmount;
    }

    @Override
    public String toString () {
        return "Illegal amount to add : " + this.amount;
    }

    @Override
    public ErrorCode getCode () {
        return ErrorCode.IllegalAddOperation;
    }
}
