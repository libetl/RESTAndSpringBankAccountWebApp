package org.toilelibre.libe.bank.model.account.balance;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.error.ErrorCode;

public class IllegalBalanceException extends BankAccountException {

    private static final long serialVersionUID = -9204191749972551939L;

    private final Double      balance;

    public IllegalBalanceException (final Double illegalBalance) {
        this.balance = illegalBalance;
    }

    @Override
    public String toString () {
        return "Illegal account balance: " + this.balance;
    }

    @Override
    public ErrorCode getCode () {
        return ErrorCode.IllegalBalance;
    }
}
