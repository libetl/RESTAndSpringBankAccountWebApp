package org.toilelibre.libe.bank.model.account.details;

import java.util.Map;

import org.toilelibre.libe.bank.impl.account.details.CustomerAccountDetails;
import org.toilelibre.libe.bank.model.BankObject;

public interface AccountDetails extends BankObject {

    interface Builder {

        CustomerAccountDetails build (AccountDetailsRule accountDetailsRule) throws IllegalSwiftCodeException;

        CustomerAccountDetails buildWithUnknownValues ();

        Builder initFromMap (Map<String, Object> inputAsMap) throws IllegalDetailsException;

        Builder withAddress (String address);

        Builder withCity (String city);

        Builder withContact (AccountDetailsContact contact);

        Builder withCountry (String country);

        Builder withState (String state);

        Builder withSwiftCode (String swiftCode);

        Builder withZipCode (String zipCode);

    }

    /**
     * Address of the owner
     *
     * @return address as string
     */
    String getAddress ();

    /**
     * City of the owner
     *
     * @return City as string
     */
    String getCity ();

    /**
     * Contact of the owner of the account
     *
     * @return an AccountDetailsContact
     */
    AccountDetailsContact getContact ();

    /**
     * Country of the owner
     *
     * @return Country as string
     */
    String getCountry ();

    /**
     * State of the owner
     *
     * @return State as string
     */
    String getState ();

    /**
     * SWIFT Code number
     *
     * @return string representation of the SWIFT code
     */
    String getSwiftCode ();

    /**
     * Zip code of the owner
     *
     * @return Zip code as string
     */
    String getZipCode ();
}
