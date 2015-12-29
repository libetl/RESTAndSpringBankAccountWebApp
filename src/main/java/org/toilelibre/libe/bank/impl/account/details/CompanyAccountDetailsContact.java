package org.toilelibre.libe.bank.impl.account.details;

import org.toilelibre.libe.bank.model.account.details.AccountDetailsContact;

public class CompanyAccountDetailsContact implements AccountDetailsContact {

    public enum Status {
        INC, PLC, LLC, UN
    }

    private final Status status;
    private final String corporateName;

    public CompanyAccountDetailsContact (final String corporateName1, final Status status1) {
        super ();
        this.corporateName = corporateName1;
        this.status = status1;
    }

    public String getCorporateName () {
        return this.corporateName;
    }

    @Override
    public String getReadableName () {
        return (this.getCorporateName () + this.status) != null ? " " + this.status : "";
    }

    public Status getStatus () {
        return this.status;
    }

}
