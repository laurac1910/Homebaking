package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

public class AccountDTO {

    private Long id;

    private double balance;

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();

        this.balance = account.getBalance();
    }

    public Long getId() {
        return id;
    }

    public double getBalance() {
        return balance;
    }
}

