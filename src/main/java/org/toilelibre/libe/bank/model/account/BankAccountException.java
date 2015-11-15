package org.toilelibre.libe.bank.model.account;

import org.toilelibre.libe.bank.model.account.error.ErrorCode;

public abstract class BankAccountException extends Exception {
    
    /**
     *
     */
    private static final long serialVersionUID = -7784462491935784057L;
    
    public abstract ErrorCode getCode ();
    
    @Override
    public String getMessage () {
        return this.toString ();
    }
}
