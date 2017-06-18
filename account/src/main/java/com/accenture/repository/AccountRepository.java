package com.accenture.repository;

import com.accenture.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

/**
 * Repository class for accounts
 */
public interface AccountRepository extends JpaRepository<Account, Integer> {

    /**
     * Finds the account associated with the provided username
     * @param accountUsername username associated with the account
     * @return the account which has the provided username
     */
    Account findByAccountUsername(@Param("accountUsername") String accountUsername);
}