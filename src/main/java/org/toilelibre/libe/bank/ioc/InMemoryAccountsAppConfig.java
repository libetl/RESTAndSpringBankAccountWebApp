package org.toilelibre.libe.bank.ioc;

import java.io.IOException;
import java.util.Date;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.toilelibre.libe.bank.impl.account.BasicHashSetAccountRepository;
import org.toilelibre.libe.bank.impl.account.CreateAccountInMemoryService;
import org.toilelibre.libe.bank.impl.account.CustomerAccountRule;
import org.toilelibre.libe.bank.impl.account.FindAccountInMemoryService;
import org.toilelibre.libe.bank.impl.account.RemoveAccountInMemoryService;
import org.toilelibre.libe.bank.impl.account.balance.BasicHashMapAccountBalanceRepository;
import org.toilelibre.libe.bank.impl.account.balance.CustomerAccountBalance;
import org.toilelibre.libe.bank.impl.account.balance.CustomerAccountBalanceRule;
import org.toilelibre.libe.bank.impl.account.balance.CustomerAccountBalanceService;
import org.toilelibre.libe.bank.impl.account.details.BasicHashMapAccountDetailsRepository;
import org.toilelibre.libe.bank.impl.account.details.CustomerAccountDetails;
import org.toilelibre.libe.bank.impl.account.details.CustomerAccountDetailsRule;
import org.toilelibre.libe.bank.impl.account.details.CustomerAccountDetailsService;
import org.toilelibre.libe.bank.impl.account.history.AccountHistoryInMemoryService;
import org.toilelibre.libe.bank.impl.account.history.BankAccountHistory;
import org.toilelibre.libe.bank.impl.account.history.BankAccountHistoryOperation;
import org.toilelibre.libe.bank.impl.account.history.BasicHashMapAccountHistoryRepository;
import org.toilelibre.libe.bank.impl.account.history.ValidateAccountRepositoryOperationRule;
import org.toilelibre.libe.bank.impl.account.operation.SimpleAccountOperationService;
import org.toilelibre.libe.bank.model.account.AccountRepository;
import org.toilelibre.libe.bank.model.account.AccountRule;
import org.toilelibre.libe.bank.model.account.CreateAccountService;
import org.toilelibre.libe.bank.model.account.FindAccountService;
import org.toilelibre.libe.bank.model.account.RemoveAccountService;
import org.toilelibre.libe.bank.model.account.balance.AccountBalance;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRepository;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceRule;
import org.toilelibre.libe.bank.model.account.balance.AccountBalanceService;
import org.toilelibre.libe.bank.model.account.details.AccountDetails.Builder;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsRepository;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsRule;
import org.toilelibre.libe.bank.model.account.details.AccountDetailsService;
import org.toilelibre.libe.bank.model.account.history.AccountHistory;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperation.Type;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryOperationRule;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryRepository;
import org.toilelibre.libe.bank.model.account.history.AccountHistoryService;
import org.toilelibre.libe.bank.model.account.operation.AccountOperationService;

@Configuration
@EnableCaching
public class InMemoryAccountsAppConfig implements AppConfig {

    @Override
    @Bean
    public CacheManager cacheManager () {
        return new ConcurrentMapCacheManager ("account", "accountBalance", "accountDetails", "accountHistory");
    }

    @Override
    @Bean
    public AccountBalance getAccountBalance () {
        return new CustomerAccountBalance ();
    }

    @Override
    @Bean
    public AccountBalanceRepository getAccountBalanceRepository () {
        return new BasicHashMapAccountBalanceRepository ();
    }

    @Override
    @Bean
    public AccountBalanceRule getAccountBalanceRule () {
        return new CustomerAccountBalanceRule ();
    }

    @Override
    @Bean
    public AccountBalanceService getAccountBalanceService () {
        return new CustomerAccountBalanceService ();
    }

    @Override
    @Bean
    public Builder getAccountDetailsBuilder () {
        return new CustomerAccountDetails.Builder ();
    }

    @Override
    @Bean
    public AccountDetailsRepository getAccountDetailsRepository () {
        return new BasicHashMapAccountDetailsRepository ();
    }

    @Override
    @Bean
    public AccountDetailsRule getAccountDetailsRule () {
        return new CustomerAccountDetailsRule ();
    }

    @Override
    @Bean
    public AccountDetailsService getAccountDetailsService () {
        return new CustomerAccountDetailsService ();
    }

    @Override
    @Bean
    public AccountHistory getAccountHistory () {
        return new BankAccountHistory ();
    }

    @Override
    @Bean
    public AccountHistoryOperation getAccountHistoryOperation () {
        return new BankAccountHistoryOperation (new Date (), Type.CREDIT, "", 0.0);
    }

    @Override
    @Bean
    public AccountHistoryOperationRule getAccountHistoryOperationRule () {
        return new ValidateAccountRepositoryOperationRule ();
    }

    @Override
    @Bean
    public AccountHistoryRepository getAccountHistoryRepository () {
        return new BasicHashMapAccountHistoryRepository ();
    }

    @Override
    @Bean
    public AccountHistoryService getAccountHistoryService () {
        return new AccountHistoryInMemoryService ();
    }

    @Override
    @Bean
    public AccountOperationService getAccountOperationService () {
        return new SimpleAccountOperationService ();
    }

    @Override
    @Bean
    public AccountRepository getAccountRepository () {
        return new BasicHashSetAccountRepository ();
    }

    @Override
    @Bean
    public AccountRule getAccountRule () {
        return new CustomerAccountRule ();
    }

    @Override
    @Bean
    public FindAccountService getFindAccountService () {
        return new FindAccountInMemoryService ();
    }

    @Override
    @Bean
    public IndexWriterConfig getIndexWriterConfig () {
        return new IndexWriterConfig (Version.LUCENE_36, new StandardAnalyzer (Version.LUCENE_36));
    }

    @Override
    @Bean
    public RemoveAccountService getRemoveAccountService () {
        return new RemoveAccountInMemoryService ();
    }

    @Override
    @Bean
    public SpellChecker getSpellChecker () throws IOException {
        return new SpellChecker (new RAMDirectory ());
    }

    @Override
    @Bean
    public CreateAccountService getUpdateOrCreateAccountService () {
        return new CreateAccountInMemoryService ();
    }
}
