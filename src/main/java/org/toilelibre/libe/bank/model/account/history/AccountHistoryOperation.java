package org.toilelibre.libe.bank.model.account.history;

import java.util.Date;

public interface AccountHistoryOperation {

    enum Type {
        DEBIT, CREDIT;
    }

    /**
     * Amount of the operation
     *
     * @return an amount
     */
    Double getAmount ();

    /**
     * Date of the operation
     *
     * @return a date
     */
    Date getDate ();

    /**
     * Issuer of the operation
     *
     * @return issuer as a string
     */
    String getIssuer ();

    /**
     * Type of operation (DEBIT or CREDIT)
     *
     * @return a Type
     */
    Type getType ();

    AccountHistoryOperation newCredit (String issuer, double amount);

    AccountHistoryOperation newDebit (String issuer, double amount);
}
