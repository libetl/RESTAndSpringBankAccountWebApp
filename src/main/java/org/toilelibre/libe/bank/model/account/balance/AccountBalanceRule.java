package org.toilelibre.libe.bank.model.account.balance;

/**
 * Checks if the requested operation is permitted.
 */
public interface AccountBalanceRule {

    /**
     * Checks if the resulting account balance after a withdrawal is OK for the
     * specific type of account.
     *
     * @param resultingAccountBalance
     *            - the amount resulting of the withdrawal
     * @param allowedOverdraft
     *            the account overdraft
     * @return true if the operation is permitted, false otherwise
     */
    boolean withdrawPermitted (Double resultingAccountBalance, Double allowedOverdraft);

    /**
     * Checks if the amount passed in parameter can be added to the balance
     *
     * @param addedAmount
     *            - the amount to be added
     * @return true if the operation is permitted, false otherwise
     */
    boolean canAddThisAmount (Double addedAmount);

    /**
     * Checks if the overdraft passed in parameter can be set
     *
     * @param resultingAccountBalance
     *            - the acurrent balance value
     * @param overdraft
     *            - the overdraft to be set
     * @return true if the operation is permitted, false otherwise
     */
    boolean canSetThisOverdraft (Double balance, Double overdraft);
}
