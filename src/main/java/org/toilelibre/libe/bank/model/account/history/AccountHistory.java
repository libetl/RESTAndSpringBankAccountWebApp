package org.toilelibre.libe.bank.model.account.history;

import java.util.List;

import org.toilelibre.libe.bank.model.BankObject;

public interface AccountHistory extends BankObject {

    void addHistoryLine (AccountHistoryOperation operation);

    /**
     * AccountHistory is an Entity, so it needs an asCopy method to get a clone
     *
     * @return a clone of this entity
     */
    public AccountHistory asCopy ();

    /**
     * Method to get a list of the logged account operations
     *
     * @return a list of operations
     */
    List<AccountHistoryOperation> getHistoryLines ();
}
