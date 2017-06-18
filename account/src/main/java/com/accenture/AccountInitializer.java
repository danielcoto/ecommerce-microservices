package com.accenture;

import com.accenture.entity.Account;
import com.accenture.repository.AccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AccountInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AccountInitializer.class);

    private final AccountRepository accountRepository;

    @Autowired
    public AccountInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... strings) throws Exception {

        //--------------------- Account repository initial content ----------------------------------------

        accountRepository.save(new Account("Admin", "Admin", "Calle Admin",
                "admin", "admin"));

        accountRepository.save(new Account("Luis", "Ballestin", "Calle 1",
                "lb", "lb"));

        accountRepository.save(new Account("Sergiy", "Shvayka", "Calle 2",
                "ss", "ss"));

        accountRepository.save(new Account("Daniel", "Coto", "Calle 3",
                "dc", "dc"));
    }
}