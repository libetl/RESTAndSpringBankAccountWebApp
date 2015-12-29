package org.toilelibre.libe.bank.impl.account.history;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.toilelibre.libe.bank.model.account.history.AccountHistory;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation;

/**
 * Entity of a bank account history operation
 *
 */
public class BankAccountHistory implements AccountHistory {

    private final List<AccountHistoryOperation> historyLines;

    public BankAccountHistory () {
        this.historyLines = new LinkedList<AccountHistoryOperation> ();
    }

    @Override
    public void addHistoryLine (final AccountHistoryOperation operation) {
        this.historyLines.add (operation);
    }

    /**
     * As the Account History is an Entity (and not a VO) It must be cloned if
     * persisted in memory
     *
     * @return AccountHistory
     */
    @Override
    public AccountHistory asCopy () {
        final BankAccountHistory result = new BankAccountHistory ();
        result.historyLines.addAll (this.historyLines);
        return result;
    }

    @Override
    public List<AccountHistoryOperation> getHistoryLines () {
        return Collections.unmodifiableList (this.historyLines);
    }
}
