package org.toilelibre.libe.bank.model.account.history;

import java.util.Date;

public interface AccountHistoryOperation {
    
    enum Type {
        DEBIT, CREDIT;
    }
    
    /**
     * Date of the operation
     *
     * @return a date
     */
    Date getDate ();
    
    /**
     * Type of operation (DEBIT or CREDIT)
     *
     * @return a Type
     */
    Type getType ();
    
    /**
     * Issuer of the operation
     *
     * @return issuer as a string
     */
    String getIssuer ();
    
    /**
     * Amount of the operation
     *
     * @return an amount
     */
    Double getAmount ();
    
    AccountHistoryOperation newDebit (String issuer, double amount);
    
    AccountHistoryOperation newCredit (String issuer, double amount);
}
