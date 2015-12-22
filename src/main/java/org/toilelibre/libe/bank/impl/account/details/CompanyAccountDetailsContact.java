package org.toilelibre.libe.bank.impl.account.details;

import javax.xml.bind.annotation.XmlElement;

import org.toilelibre.libe.bank.model.account.details.AccountDetailsContact;

public class CompanyAccountDetailsContact implements AccountDetailsContact {
    
    public enum Status {
        INC, PLC, LLC, UN
    }

    @XmlElement (name="status")
    private final Status status;
    @XmlElement (name="corporateName")
    private final String corporateName;
                         
    public CompanyAccountDetailsContact (final String corporateName1, final Status status1) {
        super ();
        this.corporateName = corporateName1;
        this.status = status1;
    }
    
    public String getCorporateName () {
        return this.corporateName;
    }
    
    public Status getStatus () {
        return this.status;
    }
    
    @Override
    public String getReadableName () {
        return this.getCorporateName () + this.status != null ? " " + this.status : "";
    }
    
}
