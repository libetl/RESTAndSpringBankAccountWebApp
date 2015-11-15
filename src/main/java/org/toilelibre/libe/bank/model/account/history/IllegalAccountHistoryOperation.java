package org.toilelibre.libe.bank.model.account.history;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.error.ErrorCode;

public class IllegalAccountHistoryOperation extends BankAccountException {
    
    /**
     *
     */
    private static final long             serialVersionUID = 2481702858074663139L;
    private final AccountHistoryOperation accountHistoryOperation;
                                          
    public IllegalAccountHistoryOperation (final AccountHistoryOperation illegalAccountHistoryOperation) {
        this.accountHistoryOperation = illegalAccountHistoryOperation;
    }
    
    @Override
    public String toString () {
        return "Illegal operation on account : " + this.accountHistoryOperation.getDate () + " " + this.accountHistoryOperation.getIssuer () + " "
                + this.accountHistoryOperation.getType ().name () + " " + this.accountHistoryOperation.getAmount ();
    }
    
    @Override
    public ErrorCode getCode () {
        return ErrorCode.IllegalAccountHistoryOperation;
    }
}
