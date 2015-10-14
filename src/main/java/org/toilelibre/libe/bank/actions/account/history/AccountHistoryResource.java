package org.toilelibre.libe.bank.actions.account.history;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.toilelibre.libe.bank.actions.LinkHelper;
import org.toilelibre.libe.bank.actions.Response;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;
import org.toilelibre.libe.bank.model.account.history.AccountHistory;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryService;

@RestController
public class AccountHistoryResource {

    private static Logger         LOGGER = LoggerFactory.getLogger (AccountHistoryResource.class);

    @Inject
    private AccountHistoryService accountHistoryService;

    @Inject
    private LinkHelper            linkHelper;

    @RequestMapping (method = RequestMethod.GET, path = "/account/{iban}/history")
    public Response<AccountHistory> getHistory (@PathVariable final String iban) throws NoSuchAccountException {
        AccountHistoryResource.LOGGER.info ("Trying to find account history of account " + iban);
        return new Response<AccountHistory> (this.linkHelper.get (), this.accountHistoryService.view (iban));
    }
}
