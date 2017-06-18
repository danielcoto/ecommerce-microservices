package com.accenture.entity;

import javax.persistence.*;

/**
 * Entity class for accounts
 */
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer accountId;

    private String accountName;
    private String accountSurname;
    private String accountAddress;
    private String accountUsername;
    private String accountPassword;

    /**
     * Account class constructor
     */
    public Account() {}

    /**
     * Account class constructor
     * @param accountName user name
     * @param accountSurname user surname
     * @param accountAddress user address
     * @param accountUsername username
     * @param accountPassword user password
     */
    public Account(String accountName, String accountSurname, String accountAddress,
                   String accountUsername, String accountPassword) {
        this.accountName = accountName;
        this.accountSurname = accountSurname;
        this.accountAddress = accountAddress;
        this.accountUsername = accountUsername;
        this.accountPassword = accountPassword;
    }

    public Integer getAccountId() {
        return accountId;
    }
    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getAccountName() {
        return accountName;
    }
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountSurname() {
        return accountSurname;
    }
    public void setAccountSurname(String accountSurname) {
        this.accountSurname = accountSurname;
    }

    public String getAccountAddress() {
        return accountAddress;
    }
    public void setAccountAddress(String accountAddress) {
        this.accountAddress = accountAddress;
    }

    public String getAccountUsername() {
        return accountUsername;
    }
    public void setAccountUsername(String accountUsername) {
        this.accountUsername = accountUsername;
    }

    public String getAccountPassword() {
        return accountPassword;
    }
    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", accountName='" + accountName + '\'' +
                ", accountSurname='" + accountSurname + '\'' +
                ", accountAddress='" + accountAddress + '\'' +
                ", accountUsername='" + accountUsername + '\'' +
                ", accountPassword='" + accountPassword + '\'' +
                '}';
    }
}