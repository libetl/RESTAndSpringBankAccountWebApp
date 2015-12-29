package org.toilelibre.libe.bank.actions.account;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.toilelibre.libe.bank.actions.LinkHelper;
import org.toilelibre.libe.bank.actions.Response;
import org.toilelibre.libe.bank.actions.entity.ArrayNode;
import org.toilelibre.libe.bank.actions.entity.Node;
import org.toilelibre.libe.bank.actions.entity.NodeFactory;
import org.toilelibre.libe.bank.actions.entity.ObjectNode;
import org.toilelibre.libe.bank.model.account.AccountRule;
import org.toilelibre.libe.bank.model.account.BankAccountException;
import org.toilelibre.libe.bank.model.account.CreateAccountService;
import org.toilelibre.libe.bank.model.account.FindAccountService;
import org.toilelibre.libe.bank.model.account.NoSuchAccountException;

@RestController
public class AccountResource {

    private static Logger LOGGER = LoggerFactory.getLogger (AccountResource.class);

    @Inject
    private FindAccountService findAccountService;

    @Inject
    private CreateAccountService updateOrCreateAccountService;

    @Inject
    private AccountRule accountRule;

    @Inject
    private LinkHelper linkHelper;

    @RequestMapping (method = RequestMethod.POST, path = "/account/{iban}")
    public Response<String> create (@PathVariable final String iban) throws BankAccountException {
        this.updateOrCreateAccountService.create (iban, this.accountRule);
        AccountResource.LOGGER.info ("New Account created " + iban);
        return new Response<String> (this.linkHelper.get (), iban);
    }

    @RequestMapping (method = RequestMethod.GET, path = "/account/{iban}")
    public Response<ObjectNode<Node>> getOneAccount (@PathVariable final String iban) throws NoSuchAccountException {
        AccountResource.LOGGER.info ("Trying to find account number " + iban);
        final NodeFactory factory = NodeFactory.instance;
        return new Response<ObjectNode<Node>> (this.linkHelper.get (), this.linkHelper.surroundWithLinks (factory.objectNode ().put ("iban", this.findAccountService.find (iban))));
    }

    @RequestMapping (method = RequestMethod.GET, path = "/account")
    public Response<ArrayNode> list () {
        final NodeFactory factory = NodeFactory.instance;
        AccountResource.LOGGER.info ("Fetching all existing accounts");
        final ArrayNode results = NodeFactory.instance.arrayNode ();
        for (final String iban : this.findAccountService.findAll ()) {
            results.add (this.linkHelper.surroundWithLinks (factory.objectNode ().put ("iban", iban)));
        }

        return new Response<ArrayNode> (this.linkHelper.get (), results);
    }
}
