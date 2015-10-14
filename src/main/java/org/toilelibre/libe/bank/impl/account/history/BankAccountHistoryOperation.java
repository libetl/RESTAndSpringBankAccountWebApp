package org.toilelibre.libe.bank.impl.account.history;

import java.util.Date;
import java.util.Objects;

import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation;

/**
 * VO of a bank account history operation
 *
 */
public class BankAccountHistoryOperation implements AccountHistoryOperation {

    private final Date   date;
    private final Type   type;
    private final String issuer;
    private final Double amount;

    public BankAccountHistoryOperation (final Date date1, final Type type1, final String issuer1, final Double amount1) {
        this.date = Objects.requireNonNull (date1);
        this.type = Objects.requireNonNull (type1);
        this.issuer = Objects.requireNonNull (issuer1);
        this.amount = Objects.requireNonNull (amount1);
    }

    @Override
    public Date getDate () {
        return new Date (this.date.getTime ());
    }

    @Override
    public Type getType () {
        return this.type;
    }

    @Override
    public String getIssuer () {
        return this.issuer;
    }

    @Override
    public Double getAmount () {
        return this.amount;
    }

    @Override
    public AccountHistoryOperation newDebit (final String issuer, final double amount) {
        return new BankAccountHistoryOperation (new Date (), Type.DEBIT, issuer, amount);
    }

    @Override
    public AccountHistoryOperation newCredit (final String issuer, final double amount) {
        return new BankAccountHistoryOperation (new Date (), Type.CREDIT, issuer, amount);
    }

}
