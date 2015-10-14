package org.toilelibre.libe.bank.impl.account.balance;

import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRule;

public class CustomerAccountBalanceRule implements AccountBalanceRule {

    private static final int CORRECT_ROUND_VALUE_FOR_AN_OVERDRAFT = 100;

    /*
     * (non-Javadoc)
     *
     * @see
     * org.toilelibre.libe.bank.account.AccountRule#withdrawPermitted(java.lang.Double)
     */
    @Override
    public boolean withdrawPermitted (final Double resultingAccountBalance, final Double allowedOverdraft) {
        return resultingAccountBalance >= -allowedOverdraft;
    }

    @Override
    public boolean canAddThisAmount (final Double addedAmount) {
        return addedAmount > 0;
    }

    @Override
    public boolean canSetThisOverdraft (final Double balance, final Double overdraft) {
        return -overdraft <= balance && overdraft % CustomerAccountBalanceRule.CORRECT_ROUND_VALUE_FOR_AN_OVERDRAFT == 0;
    }

}
