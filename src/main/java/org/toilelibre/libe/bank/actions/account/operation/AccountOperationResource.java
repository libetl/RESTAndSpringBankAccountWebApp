package org.toilelibre.libe.bank.actions.account.operation;

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
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRule;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperationRule;
import org.toilelibre.libe.bank.model.account.operation.AccountOperationService;

@RestController
public class AccountOperationResource {
    
    private static Logger               LOGGER = LoggerFactory.getLogger (AccountOperationResource.class);
                                               
    @Inject
    private AccountOperationService     accountOperationService;
                                        
    @Inject
    private AccountBalanceRule          accountBalanceRule;
                                        
    @Inject
    private AccountHistoryOperationRule accountHistoryOperationRule;
                                        
    @Inject
    private LinkHelper                  linkHelper;
                                        
    @RequestMapping (method = RequestMethod.POST, path = "/account/{iban}/deposit")
    public Response<AccountHistoryOperation> deposit (@PathVariable final String iban, @RequestBodyPath ("amount") final double amount) throws BankAccountException {
        AccountOperationResource.LOGGER.info ("Deposit on account " + iban);
        return new Response<AccountHistoryOperation> (this.linkHelper.get (),
                this.accountOperationService.deposit (iban, amount, this.accountBalanceRule, this.accountHistoryOperationRule));
    }
    
    @RequestMapping (method = RequestMethod.POST, path = "/account/{iban}/withdraw")
    public Response<AccountHistoryOperation> withdraw (@PathVariable final String iban, @RequestBodyPath ("amount") final double amount) throws BankAccountException {
        AccountOperationResource.LOGGER.info ("Withdraw on account " + iban);
        return new Response<AccountHistoryOperation> (this.linkHelper.get (),
                this.accountOperationService.withdraw (iban, amount, this.accountBalanceRule, this.accountHistoryOperationRule));
    }
    
    @RequestMapping (method = RequestMethod.POST, path = "/account/{iban}/transfer")
    public Response<AccountHistoryOperation> transfer (@PathVariable final String iban, @RequestBodyPath ("amount") final double amount, @RequestBodyPath ("recipient") final String recipient) throws BankAccountException {
        AccountOperationResource.LOGGER.info ("Transfer on account " + iban);
        return new Response<AccountHistoryOperation> (this.linkHelper.get (),
                this.accountOperationService.transfer (iban, amount, recipient, this.accountBalanceRule, this.accountHistoryOperationRule));
    }
}
