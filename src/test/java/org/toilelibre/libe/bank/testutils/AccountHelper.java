package org.toilelibre.libe.bank.testutils;

import java.util.Random;

import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.details.AccountDetails;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsContact;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsRule;
import org.toilelibre.libe.bank.model.account.details.IllegalSwiftCodeException;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation;

public class AccountHelper {
    
    private String getRandomNumber (int digCount) {
        final Random generator = new Random (System.currentTimeMillis ());
        StringBuilder sb = new StringBuilder (digCount);
        for (int i = 0 ; i < digCount ; i++)
            sb.append ((char) ('0' + generator.nextInt (10)));
        return sb.toString ();
    }
    
    public String getEmptyAccount () throws BankAccountException {
        return "AL" + this.getRandomNumber (25);
    }
    
    public static AccountHistoryOperation getNewHistoryLine (AccountHistoryOperation templateObject) {
        return templateObject.newCredit ("Issuer 1", Math.random () * 100);
    }
    
    public AccountDetails getDetails (AccountDetails.Builder detailsBuilder, AccountDetailsRule rule) throws IllegalSwiftCodeException {
        return detailsBuilder.withAddress ("10 boulevard de Sebastopol").withCity ("Paris").withZipCode ("75001").withState ("IDF").withCountry ("France")
                .withSwiftCode ("EBAPFRPPPSA").withContact (new AccountDetailsContact () {
                    
                    @Override
                    public String getReadableName () {
                        return "Mr Olivier Martin";
                    }
                }).build (rule);
    }
}
