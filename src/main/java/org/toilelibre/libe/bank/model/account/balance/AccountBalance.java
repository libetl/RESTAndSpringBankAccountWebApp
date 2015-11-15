package org.toilelibre.libe.bank.model.account.balance;

/**
 * This class represents a simple account. It doesn't handle different
 * currencies, all money is supposed to be of standard currency EUR.
 */
public interface AccountBalance {
    
    /**
     * Adds money to this account.
     *
     * @param addedAmount
     *            - the money to add
     * @param accountRule
     *            - the AccountRule will determine if this add operation is
     *            allowed for this account
     * @throws IllegalAddOperationException
     *             if the amount added is negative
     */
    public void add (Double addedAmount, AccountBalanceRule accountRule) throws IllegalAddOperationException;
    
    /**
     * Withdraws money from the account.
     *
     * @param withdrawnAmount
     *            - the money to withdraw
     * @param rule
     *            - the AccountRule that defines which balance is allowed for
     *            this account
     * @return the remaining account balance
     * @throws IllegalBalanceException
     *             if the withdrawal leaves the account with a forbidden balance
     */
    public Double withdrawAndReportBalance (Double withdrawnAmount, AccountBalanceRule rule) throws IllegalBalanceException;
    
    /**
     * Set a new overdraft value.
     *
     * @param overdraft
     *            - the allowed overdraft
     * @param rule
     *            - the AccountRule that defines if an overdraft value is
     *            possible
     * @throws IllegalOverdraftValueException
     *             if the overdraft value is incorrect
     */
    public void setOverdraft (Double overdraft, AccountBalanceRule rule) throws IllegalOverdraftValueException;
    
    /**
     * Gets the current account balance.
     *
     * @return the account's balance
     */
    public Double getBalance ();
    
    /**
     * Gets the current account balance.
     *
     * @return the account's balance
     */
    public Double getOverdraft ();
    
    /**
     * AccountBalance is an Entity, so it needs an asCopy method to get a clone
     *
     * @return a clone of this entity
     */
    public AccountBalance asCopy ();
}
