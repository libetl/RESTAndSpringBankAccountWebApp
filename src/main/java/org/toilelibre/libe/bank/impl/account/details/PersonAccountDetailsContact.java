package org.toilelibre.libe.bank.impl.account.details;

import java.util.Objects;

import org.toilelibre.libe.bank.model.account.details.AccountDetailsContact;

public class PersonAccountDetailsContact implements AccountDetailsContact {

    public enum Title {
        MR, MS, DR, PROF, UN;
    }

    private final Title  title;
    private final String firstName;
    private final String lastName;

    public PersonAccountDetailsContact (final Title title1, final String firstName1, final String lastName1) {
        super ();
        this.title = Objects.requireNonNull (title1);
        this.firstName = Objects.requireNonNull (firstName1);
        this.lastName = Objects.requireNonNull (lastName1);
    }

    public Title getTitle () {
        return this.title;
    }

    public String getFirstName () {
        return this.firstName;
    }

    public String getLastName () {
        return this.lastName;
    }

    @Override
    public String getReadableName () {
        return this.title.name () + " " + this.firstName + " " + this.lastName;
    }

}
