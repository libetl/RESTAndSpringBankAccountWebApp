package org.toilelibre.libe.bank.actions.account.details;

import java.util.Map;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.toilelibre.libe.bank.actions.LinkHelper;
import org.toilelibre.libe.bank.actions.Response;
import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;
import org.toilelibre.libe.bank.model.account.details.AccountDetails;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsRule;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsService;

@RestController
public class AccountDetailsResource {
    
    private static Logger          LOGGER = LoggerFactory.getLogger (AccountDetailsResource.class);
                                          
    @Inject
    private AccountDetailsService  accountDetailsService;
                                   
    @Inject
    private LinkHelper             linkHelper;
                                   
    @Inject
    private AccountDetails.Builder detailsBuilder;
                                   
    @Inject
    private AccountDetailsRule     accountDetailsRule;
                                   
    @RequestMapping (method = RequestMethod.GET, path = "/account/{iban}/details")
    public Response<AccountDetails> getDetails (@PathVariable final String iban) throws NoSuchAccountException {
        AccountDetailsResource.LOGGER.info ("Trying to find account details of account " + iban);
        return new Response<AccountDetails> (this.linkHelper.get (), this.accountDetailsService.view (iban));
    }
    
    @RequestMapping (method = RequestMethod.PUT, path = "/account/{iban}/details")
    public Response<AccountDetails> setDetails (@PathVariable final String iban, @RequestBody final JsonNode input) throws BankAccountException {
        AccountDetailsResource.LOGGER.info ("Setting the details of the account " + iban);
        final ObjectMapper mapper = new ObjectMapper ();
        @SuppressWarnings ("unchecked")
        final Map<String, Object> inputAsMap = mapper.convertValue (input, Map.class);
        final AccountDetails details = this.detailsBuilder.initFromMap (inputAsMap).build (this.accountDetailsRule);
        this.accountDetailsService.update (iban, details);
        return new Response<AccountDetails> (this.linkHelper.get (), this.accountDetailsService.view (iban));
    }
}
