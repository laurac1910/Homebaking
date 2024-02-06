package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Transaction {
@Id
@GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private TransactionType type;
    private double amount;
    LocalDate creationDate;
@ManyToOne
@JoinColumn(name = "account_id")
    private Account account;

    public Transaction() {}

    public Transaction(TransactionType type, double amount, LocalDate creationDate) {
        this.type = type;
       this.amount = amount;
        this.creationDate = creationDate;

    }

    public Long getId() {
        return id;
    }


    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }


    public void setAccount(Client client) {
    }
}

