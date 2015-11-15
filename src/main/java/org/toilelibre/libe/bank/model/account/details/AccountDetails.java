package org.toilelibre.libe.bank.model.account.details;

import java.util.Map;

import org.toilelibre.libe.bank.impl.account.details.CustomerAccountDetails;

public interface AccountDetails {
    
    /**
     * SWIFT Code number
     *
     * @return string representation of the SWIFT code
     */
    String getSwiftCode ();
    
    /**
     * Contact of the owner of the account
     *
     * @return an AccountDetailsContact
     */
    AccountDetailsContact getContact ();
    
    /**
     * Address of the owner
     *
     * @return address as string
     */
    String getAddress ();
    
    /**
     * Zip code of the owner
     *
     * @return Zip code as string
     */
    String getZipCode ();
    
    /**
     * City of the owner
     *
     * @return City as string
     */
    String getCity ();
    
    /**
     * State of the owner
     *
     * @return State as string
     */
    String getState ();
    
    /**
     * Country of the owner
     *
     * @return Country as string
     */
    String getCountry ();
    
    interface Builder {
        
        Builder withSwiftCode (String swiftCode);
        
        Builder withContact (AccountDetailsContact contact);
        
        Builder withAddress (String address);
        
        Builder withZipCode (String zipCode);
        
        Builder withCity (String city);
        
        Builder withState (String state);
        
        Builder withCountry (String country);
        
        Builder initFromMap (Map<String, Object> inputAsMap) throws IllegalDetailsException;
        
        CustomerAccountDetails buildWithUnknownValues ();
        
        CustomerAccountDetails build (AccountDetailsRule accountDetailsRule) throws IllegalSwiftCodeException;
        
    }
}
