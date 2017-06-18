package com.accenture.login;

import com.accenture.entity.Account;
import com.accenture.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Gets and converts the account from the repository to UserDetails
 */
@Component
public class LoginUserDetailsService implements UserDetailsService {

    private AccountRepository accountRepository;

    /**
     * Default constructor to allow injecting the repository class as dependency.
     * @param accountRepository the repository of all accounts
     */
    @Autowired
    public LoginUserDetailsService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Creates a UserDetails object from the account retrieved
     * @param username username of the account
     * @return a UserDetails object with username, password and authorities
     * @throws UsernameNotFoundException exception threw if implementation cannot locate a User by its username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.findByAccountUsername(username);
        if (account == null)
            return null;

        List<GrantedAuthority> auth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_USER");

        if (username.equals("admin"))
            auth = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN");

        String password = account.getAccountPassword();
        return new User(username, password, auth);
    }

    /**
     * Gets the account identifier using the username
     * @param username username of the account
     * @return the account id of the requested account
     */
    public Integer getAccountId (String username) {
        Account account = accountRepository.findByAccountUsername(username);
        return account.getAccountId();
    }
}