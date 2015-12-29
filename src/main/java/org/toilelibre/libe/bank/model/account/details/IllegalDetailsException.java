package org.toilelibre.libe.bank.model.account.details;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.error.ErrorCode;

public class IllegalDetailsException extends BankAccountException {

    /**
     *
     */
    private static final long serialVersionUID = -2600846667785731118L;

    public IllegalDetailsException () {
    }

    @Override
    public ErrorCode getCode () {
        return ErrorCode.InvalidDetails;
    }

    @Override
    public String toString () {
        return "Invalid details typed";
    }
}
