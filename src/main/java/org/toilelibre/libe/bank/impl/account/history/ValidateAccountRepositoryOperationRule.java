package org.toilelibre.libe.bank.impl.account.history;

import java.util.Date;

import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperationRule;

public class ValidateAccountRepositoryOperationRule implements AccountHistoryOperationRule {

    @Override
    public boolean operationAllowed (final AccountHistoryOperation operation) {
        return operation.getDate ().before (new Date (System.currentTimeMillis () + 10)) && operation.getAmount () > 0;
    }

}
