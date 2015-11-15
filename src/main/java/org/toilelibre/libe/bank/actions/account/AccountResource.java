package org.toilelibre.libe.bank.actions.account;

import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import org.toilelibre.libe.bank.actions.LinkHelper;
import org.toilelibre.libe.bank.actions.Response;
import org.toilelibre.libe.bank.model.account.AccountRule;
import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.FindAccountService;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;
import org.toilelibre.libe.bank.model.account.CreateAccountService;

@RestController
public class AccountResource {
    
    private static Logger        LOGGER = LoggerFactory.getLogger (AccountResource.class);
                                        
    @Inject
    private FindAccountService   findAccountService;
                                 
    @Inject
    private CreateAccountService updateOrCreateAccountService;
                                 
    @Inject
    private AccountRule          accountRule;
                                 
    @Inject
    private LinkHelper           linkHelper;
                                 
    @RequestMapping (method = RequestMethod.GET, path = "/account")
    public Response<Set<JsonNode>> list () {
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        AccountResource.LOGGER.info ("Fetching all existing accounts");
        final Set<JsonNode> results = new HashSet<JsonNode> ();
        for (final String iban : this.findAccountService.findAll ()) {
            results.add (this.linkHelper.surroundWithLinks (factory.objectNode ().put ("iban", iban)));
        }
        
        return new Response<Set<JsonNode>> (this.linkHelper.get (), results);
    }
    
    @RequestMapping (method = RequestMethod.GET, path = "/account/{iban}")
    public Response<JsonNode> getOneAccount (@PathVariable final String iban) throws NoSuchAccountException {
        AccountResource.LOGGER.info ("Trying to find account number " + iban);
        final JsonNodeFactory factory = JsonNodeFactory.instance;
        return new Response<JsonNode> (this.linkHelper.get (), this.linkHelper.surroundWithLinks (factory.objectNode ().put ("iban", this.findAccountService.find (iban))));
    }
    
    @RequestMapping (method = RequestMethod.POST, path = "/account/{iban}")
    public Response<String> create (@PathVariable final String iban) throws BankAccountException {
        this.updateOrCreateAccountService.create (iban, this.accountRule);
        AccountResource.LOGGER.info ("New Account created " + iban);
        return new Response<String> (this.linkHelper.get (), iban);
    }
}
