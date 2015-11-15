package org.toilelibre.libe.bank.ioc;

import org.springframework.cache.CacheManager;

import org.toilelibre.libe.bank.model.account.AccountRepository;
import org.toilelibre.libe.bank.model.account.AccountRule;
import org.toilelibre.libe.bank.model.account.FindAccountService;
import org.toilelibre.libe.bank.model.account.RemoveAccountService;
import org.toilelibre.libe.bank.model.account.CreateAccountService;
import org.toilelibre.libe.bank.model.account.balance.AccountBalance;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRepository;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRule;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceService;
import org.toilelibre.libe.bank.model.account.details.AccountDetails;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsRepository;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsRule;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsService;
import org.toilelibre.libe.bank.model.account.history.AccountHistory;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperationRule;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryRepository;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryService;
import org.toilelibre.libe.bank.model.account.operation.AccountOperationService;

interface AppConfig {
    
    CacheManager cacheManager ();
    
    AccountBalance getAccountBalance ();
    
    AccountBalanceRepository getAccountBalanceRepository ();
    
    AccountBalanceRule getAccountBalanceRule ();
    
    AccountBalanceService getAccountBalanceService ();
    
    AccountDetails.Builder getAccountDetailsBuilder ();
    
    AccountDetailsRepository getAccountDetailsRepository ();
    
    AccountDetailsRule getAccountDetailsRule ();
    
    AccountDetailsService getAccountDetailsService ();
    
    AccountHistory getAccountHistory ();
    
    AccountHistoryOperation getAccountHistoryOperation ();
    
    AccountHistoryOperationRule getAccountHistoryOperationRule ();
    
    AccountHistoryRepository getAccountHistoryRepository ();
    
    AccountHistoryService getAccountHistoryService ();
    
    AccountOperationService getAccountOperationService ();
    
    AccountRepository getAccountRepository ();
    
    AccountRule getAccountRule ();
    
    FindAccountService getFindAccountService ();
    
    RemoveAccountService getRemoveAccountService ();
    
    CreateAccountService getUpdateOrCreateAccountService ();
}
