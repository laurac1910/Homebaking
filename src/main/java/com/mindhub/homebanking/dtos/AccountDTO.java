package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;

import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private Long id;

    private double balance;
    private Set<TransactionDTO> transactions;

    public AccountDTO() {
    }

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.balance = account.getBalance();
        this.transactions = account.getTransactions().stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }


    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(Set<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}

