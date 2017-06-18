package com.accenture;

import com.accenture.entity.Account;
import com.accenture.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Contains the different methods to process the requested actions
 */
@Component
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * Default constructor to allow injecting the repository class as dependency.
     * @param accountRepository the repository for the accounts.
     */
    @Autowired
    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Retrieves all the accounts
     * @return the list of all accounts stored
     */
    public List<Account> retrieveAllAccounts() {
        return accountRepository.findAll();
    }

    /**
     * Deletes all the accounts
     */
    public void deleteAllAccounts() {
        accountRepository.deleteAll();
    }

    /**
     * Finds and returns the requested account
     * @param accountId the id of the requested account
     * @return the requested account
     */
    public Account retrieveAccountById(Integer accountId) {
        return accountRepository.findOne(accountId);
    }

    /**
     * @param account the account to be saved
     * Saves the new account into the repository
     */
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    /**
     * Updates the account
     * @param accountId the id of the account to be updated
     * @param accountUpdate the content to be changed in the account
     */
    public void updateAccount(Integer accountId, Account accountUpdate) {

        Account account = accountRepository.findOne(accountId);

        account.setAccountId(account.getAccountId());
        account.setAccountName(accountUpdate.getAccountName());
        account.setAccountSurname(accountUpdate.getAccountSurname());
        account.setAccountAddress(accountUpdate.getAccountAddress());
        account.setAccountUsername(accountUpdate.getAccountUsername());
        account.setAccountPassword(accountUpdate.getAccountPassword());

        accountRepository.save(account);
    }

    /**
     * Deletes the account from the repository
     * @param accountId the id of the account to be deleted
     */
    public void deleteAccount(Integer accountId) {
        accountRepository.delete(accountId);
    }

    /**
     * Checks whether the username is already in use+
     * @param accountUsername the username to be checked
     * @return true if there is a registered account with same username; false if username is unused.
     */
    public boolean checkUsername(String accountUsername) {
        if (accountRepository.findByAccountUsername(accountUsername) == null)
            return false;
        return true;
    }

    /**
     * Verifies whether the account exists
     * @param accountId the account identifier to check if exists
     * @return true if account exists; false if not
     */
    public boolean checkAccountExist(Integer accountId) {
        if (accountRepository.findOne(accountId) == null)
            return false;
        return true;
    }
}