package org.toilelibre.libe.bank.model.account;

import org.toilelibre.libe.bank.model.account.error.ErrorCode;

public class IllegalIbanException extends BankAccountException {
    
    /**
     *
     */
    private static final long serialVersionUID = 2481702858074663139L;
    private final String      iban;
                              
    public IllegalIbanException (final String invalidIban) {
        this.iban = invalidIban;
    }
    
    @Override
    public String toString () {
        return "Invalid iban code : " + this.iban;
    }
    
    @Override
    public ErrorCode getCode () {
        return ErrorCode.IllegalIban;
    }
}
