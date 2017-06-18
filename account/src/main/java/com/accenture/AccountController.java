package com.accenture;

import com.accenture.entity.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles the different web requests to routes starting with /accounts.
 */
@RestController
@RequestMapping(value = "/accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * Default constructor to allow injecting the service class as dependency.
     * @param accountService dependency class.
     */
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Maps the resource's route (/accounts) to the getAllAccounts() method.
     * Gets all accounts by calling the appropiate function in service class.
     * @return ResponseEntity with all accounts.
     */
    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        List<Account> accounts = accountService.retrieveAllAccounts();
        if (accounts.isEmpty())
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(accounts, HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/accounts) to the removeAllAccounts() method.
     * Deletes all accounts by calling the appropiate function in service class.
     * @return ResponseEntity with feedback message.
     */
    @DeleteMapping
    public ResponseEntity<?> removeAllAccounts() {
        List<Account> accounts = accountService.retrieveAllAccounts();
        if (accounts.isEmpty())
            return new ResponseEntity<>("NO CONTENT: there is no account stored.", HttpStatus.OK);

        accountService.deleteAllAccounts();
        return new ResponseEntity<>("OK: all accounts have been removed.", HttpStatus.OK);
    }

    /**
     * Maps the resource's route (/accounts) to the addAccount() method.
     * Inserts a new account by calling the appropiate function in service class.
     * @param newAccount the new account to be stored.
     * @return ResponseEntity with feedback message.
     */
    @PostMapping
    public ResponseEntity<?> addAccount(@RequestBody Account newAccount) {

        if (accountService.checkUsername(newAccount.getAccountUsername()))
            return new ResponseEntity<>("An account already exists for the provided username.", HttpStatus.CONFLICT);

        accountService.saveAccount(newAccount);
        return new ResponseEntity<>("Account was successfully created.", HttpStatus.CREATED);
    }

    /**
     * Maps the resource's route (/accounts/id={accountId}) to the getAccountById() method.
     * Gets one account by its id by calling the appropiate function in service class.
     * @param accountId the id of the account to be returned.
     * @return ResponseEntity with the requested account.
     */
    @GetMapping("/id={accountId}")
    public ResponseEntity<?> getAccountById(@PathVariable("accountId") Integer accountId) {

        if (!accountService.checkAccountExist(accountId))
            return new ResponseEntity<>("Account details requested do not exist", HttpStatus.NOT_FOUND);

        Account account = accountService.retrieveAccountById(accountId);
        String accountUsername = account.getAccountUsername();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principalName = authentication.getName();
        String authorities = authentication.getAuthorities().toString();

        if (accountUsername.equals(principalName) || (authorities.contains("ROLE_ADMIN")))
            return new ResponseEntity<>(account, HttpStatus.OK);

        return new ResponseEntity<>("Your account does not have permission to access to the requested details.", HttpStatus.FORBIDDEN);
    }

    /**
     * Maps the resource's route (/accounts/id={accountId}) to the editAccount() method.
     * Updates one account by calling the appropiate function in service class.
     * @param accountId the id of the account to be updated.
     * @param accountUpdate the content of the account to be changed.
     * @return ResponseEntity with feedback message.
     */
    @PutMapping("/id={accountId}")
    public ResponseEntity<?> editAccount(@PathVariable("accountId") Integer accountId, @RequestBody Account accountUpdate) {

        if (!accountService.checkAccountExist(accountId))
            return new ResponseEntity<>("Account details requested do not exist.", HttpStatus.NOT_FOUND);

        if (accountService.checkUsername(accountUpdate.getAccountUsername()))
            return new ResponseEntity<>("An account already exists for the provided username.", HttpStatus.CONFLICT);

        Account account = accountService.retrieveAccountById(accountId);
        String accountUsername = account.getAccountUsername();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principalName = authentication.getName();
        String authorities = authentication.getAuthorities().toString();

        if (accountUsername.equals(principalName) || (authorities.contains("ROLE_ADMIN"))) {
            accountService.updateAccount(accountId, accountUpdate);
            return new ResponseEntity<>("Account has been updated successfully.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Your account does not have permission to access to the requested details.", HttpStatus.FORBIDDEN);
    }

    /**
     * Maps the resource's route (/accounts/id={accountId}) to the removeProduct() method.
     * Deletes one account by calling the appropiate function in service class.
     * @param accountId the id of the account to be removed.
     * @return ResponseEntity with feedback message.
     */
    @DeleteMapping("/id={accountId}")
    public ResponseEntity<?> removeAccount(@PathVariable("accountId") Integer accountId) {

        if (!accountService.checkAccountExist(accountId))
            return new ResponseEntity<>("Account details requested do not exist", HttpStatus.NOT_FOUND);

        Account account = accountService.retrieveAccountById(accountId);
        String accountUsername = account.getAccountUsername();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String principalName = authentication.getName();
        String authorities = authentication.getAuthorities().toString();

        if (accountUsername.equals(principalName) || (authorities.contains("ROLE_ADMIN"))) {
            accountService.deleteAccount(accountId);
            return new ResponseEntity<>("OK: your account has been removed.", HttpStatus.OK);
        }
        return new ResponseEntity<>("Your account does not have permission to access to the requested details.", HttpStatus.FORBIDDEN);
    }
}