package org.toilelibre.libe.bank.model.account.history;

public interface AccountHistoryOperationRule {

    boolean operationAllowed (AccountHistoryOperation operation);
}
