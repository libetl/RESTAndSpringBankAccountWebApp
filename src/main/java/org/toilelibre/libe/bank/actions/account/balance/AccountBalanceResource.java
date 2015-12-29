package org.toilelibre.libe.bank.actions.account.balance;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.toilelibre.libe.bank.actions.LinkHelper;
import org.toilelibre.libe.bank.actions.Response;
import org.toilelibre.libe.bank.ioc.webapp.argresolver.RequestBodyPath;
import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;
import org.toilelibre.libe.bank.model.account.balance.AccountBalance;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRule;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceService;

@RestController
public class AccountBalanceResource {
    
    private static Logger         LOGGER = LoggerFactory.getLogger (AccountBalanceResource.class);
                                         
    @Inject
    private AccountBalanceService accountBalanceService;
                                  
    @Inject
    private AccountBalanceRule    accountBalanceRule;
                                  
    @Inject
    private LinkHelper            linkHelper;
                                  
    @RequestMapping (method = RequestMethod.GET, path = "/account/{iban}/balance")
    public Response<AccountBalance> getBalance (@PathVariable final String iban) throws NoSuchAccountException {
        AccountBalanceResource.LOGGER.info ("Trying to find account balance of account " + iban);
        return new Response<AccountBalance> (this.linkHelper.get (), this.accountBalanceService.get (iban));
    }
    
    @RequestMapping (method = RequestMethod.PUT, path = "/account/{iban}/overdraft")
    public Response<AccountBalance> setOverdraft (@PathVariable final String iban, @RequestBodyPath ("amount") final double amount) throws BankAccountException {
        AccountBalanceResource.LOGGER.info ("Setting a new overdraft for account " + iban);
        return new Response<AccountBalance> (this.linkHelper.get (), this.accountBalanceService.setOverdraft (iban, amount, this.accountBalanceRule));
    }
}
