package org.toilelibre.libe.bank.model.account.history;

import java.util.List;

public interface AccountHistory {

    /**
     * Method to get a list of the logged account operations
     *
     * @return a list of operations
     */
    List<AccountHistoryOperation> getHistoryLines ();

    void addHistoryLine (AccountHistoryOperation operation);

    /**
     * AccountHistory is an Entity, so it needs an asCopy method to get a clone
     *
     * @return a clone of this entity
     */
    public AccountHistory asCopy ();
}
