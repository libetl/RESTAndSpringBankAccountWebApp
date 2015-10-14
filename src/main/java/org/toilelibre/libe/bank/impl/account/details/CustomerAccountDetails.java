package org.toilelibre.libe.bank.impl.account.details;

import java.util.Map;
import java.util.Objects;

import org.toilelibre.libe.bank.impl.account.details.PersonAccountDetailsContact.Title;
import org.toilelibre.libe.bank.model.account.details.AccountDetails;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsContact;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsRule;
import org.toilelibre.libe.bank.model.account.details.IllegalDetailsException;
import org.toilelibre.libe.bank.model.account.details.IllegalSwiftCodeException;

/**
 * Account Details is A VO (even if there are some unique ids in the data) It
 * does not need to be cloned because the pieces of info are immutable
 */
public class CustomerAccountDetails implements AccountDetails {

    private final String                swiftCode;
    private final AccountDetailsContact contact;
    private final String                address;
    private final String                zipCode;
    private final String                city;
    private final String                state;
    private final String                country;

    private CustomerAccountDetails (final String swiftCode1, final AccountDetailsContact contact1, final String address1, final String zipCode1, final String city1, final String state1, final String country1) {
        this.swiftCode = Objects.requireNonNull (swiftCode1);
        this.contact = Objects.requireNonNull (contact1);
        this.address = Objects.requireNonNull (address1);
        this.zipCode = Objects.requireNonNull (zipCode1);
        this.city = Objects.requireNonNull (city1);
        this.state = state1;
        this.country = Objects.requireNonNull (country1);
    }

    @Override
    public String getSwiftCode () {
        return this.swiftCode;
    }

    @Override
    public AccountDetailsContact getContact () {
        return this.contact;
    }

    @Override
    public String getAddress () {
        return this.address;
    }

    @Override
    public String getZipCode () {
        return this.zipCode;
    }

    @Override
    public String getCity () {
        return this.city;
    }

    @Override
    public String getState () {
        return this.state;
    }

    @Override
    public String getCountry () {
        return this.country;
    }

    public static class Builder implements AccountDetails.Builder {
        private String                swiftCode1;
        private AccountDetailsContact contact1;
        private String                address1;
        private String                zipCode1;
        private String                city1;
        private String                state1;
        private String                country1;

        /*
         * (non-Javadoc)
         *
         * @see
         * org.toilelibre.libe.bank.impl.account.details.Builder#withSwiftCode(java
         * .lang.String)
         */
        @Override
        public Builder withSwiftCode (final String swiftCode) {
            this.swiftCode1 = swiftCode;
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see org.toilelibre.libe.bank.impl.account.details.Builder#withContact(org.toilelibre.libe.bank.model.account.details.AccountDetailsContact)
         */
        @Override
        public Builder withContact (final AccountDetailsContact contact) {
            this.contact1 = contact;
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.toilelibre.libe.bank.impl.account.details.Builder#withAddress(java.lang
         * .String)
         */
        @Override
        public Builder withAddress (final String address) {
            this.address1 = address;
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.toilelibre.libe.bank.impl.account.details.Builder#withZipCode(java.lang
         * .String)
         */
        @Override
        public Builder withZipCode (final String zipCode) {
            this.zipCode1 = zipCode;
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.toilelibre.libe.bank.impl.account.details.Builder#withCity(java.lang
         * .String)
         */
        @Override
        public Builder withCity (final String city) {
            this.city1 = city;
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.toilelibre.libe.bank.impl.account.details.Builder#withState(java.lang
         * .String)
         */
        @Override
        public Builder withState (final String state) {
            this.state1 = state;
            return this;
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.toilelibre.libe.bank.impl.account.details.Builder#withCountry(java.lang
         * .String)
         */
        @Override
        public Builder withCountry (final String country) {
            this.country1 = country;
            return this;
        }

        @Override
        public Builder initFromMap (final Map<String, Object> inputAsMap) throws IllegalDetailsException {
            this.withSwiftCode (this.safeAsString (inputAsMap.get ("swiftCode"))).withCountry (this.safeAsString (inputAsMap.get ("country"))).withAddress (this.safeAsString (inputAsMap.get ("address"))).withZipCode (this.safeAsString (inputAsMap.get ("zipCode")))
            .withCity (this.safeAsString (inputAsMap.get ("city"))).withState (this.safeAsString (inputAsMap.get ("state").toString ()));
            if (inputAsMap.get ("isCompany") != null && new Boolean (inputAsMap.get ("isCompany").toString ())) {
                this.withContact (new CompanyAccountDetailsContact (this.safeAsString (inputAsMap.get ("corporateName")), this.statusSafeValueOf (inputAsMap.get ("corporateName"))));
            } else {
                this.withContact (new PersonAccountDetailsContact (this.titleSafeValueOf (inputAsMap.get ("title")), this.safeAsString (inputAsMap.get ("firstName")), this.safeAsString (inputAsMap.get ("lastName"))));
            }
            return this;
        }

        private String safeAsString (final Object object) throws IllegalDetailsException {
            if (object == null) {
                throw new IllegalDetailsException ();
            }
            return object.toString ();
        }

        private Title titleSafeValueOf (final Object value) {
            if (value == null) {
                return Title.UN;
            }
            try {
                return Title.valueOf (value.toString ());
            } catch (final IllegalArgumentException iae) {
                return Title.UN;
            }
        }

        private CompanyAccountDetailsContact.Status statusSafeValueOf (final Object value) {
            if (value == null) {
                return CompanyAccountDetailsContact.Status.UN;
            }
            try {
                return CompanyAccountDetailsContact.Status.valueOf (value.toString ());
            } catch (final IllegalArgumentException iae) {
                return CompanyAccountDetailsContact.Status.UN;
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see
         * org.toilelibre.libe.bank.impl.account.details.Builder#build(org.toilelibre.libe.bank
         * .model.account.details.AccountDetailsRule)
         */
        @Override
        public CustomerAccountDetails build (final AccountDetailsRule accountDetailsRule) throws IllegalSwiftCodeException {
            if (!accountDetailsRule.hasCorrectSwiftCode (this.swiftCode1)) {
                throw new IllegalSwiftCodeException (this.swiftCode1);
            }
            return this.securedBuildMethod ();

        }

        @Override
        public CustomerAccountDetails buildWithUnknownValues () {
            return new CustomerAccountDetails.Builder ().withContact (new PersonAccountDetailsContact (Title.UN, "UNKNOWN", "UNKNOWN")).withSwiftCode ("UNKNOWN").withAddress ("UNKNOWN").withCity ("UNKNOWN").withZipCode ("UNKNOWN").withState ("UNKNOWN").withCountry ("UN").securedBuildMethod ();
        }

        private CustomerAccountDetails securedBuildMethod () {
            return new CustomerAccountDetails (this.swiftCode1, this.contact1, this.address1, this.zipCode1, this.city1, this.state1, this.country1);
        }
    }

}
