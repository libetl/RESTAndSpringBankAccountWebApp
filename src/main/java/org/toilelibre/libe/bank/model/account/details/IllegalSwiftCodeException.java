package org.toilelibre.libe.bank.model.account.details;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.error.ErrorCode;

public class IllegalSwiftCodeException extends BankAccountException {

    /**
     *
     */
    private static final long serialVersionUID = 2481702858074663139L;
    private final String      swiftCode;

    public IllegalSwiftCodeException (final String invalidSwiftCode) {
        this.swiftCode = invalidSwiftCode;
    }

    @Override
    public String toString () {
        return "Invalid swift code : " + this.swiftCode;
    }

    @Override
    public ErrorCode getCode () {
        return ErrorCode.InvalidSwiftCode;
    }
}
