package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String number;
    LocalDate creationDate;
    double balance;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private Client owner;

    @OneToMany (mappedBy = "account",fetch = FetchType.EAGER)
    Set<Transaction> transactions = new HashSet<>();

    public  Set<Transaction> getTransactions(){
        return transactions;
    }
    public  void addTransaction(Transaction transaction){
        transaction.setAccount(this);
        transactions.add(transaction);
    }
    public Account() {}

    public Account(String number, LocalDate creationDate, double balance,Client owner){
        this.number = number;
        this.creationDate = creationDate;
        this.balance = balance;
        this.owner = owner;

    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getOwner() {
        return owner;
    }

    public void setOwner(Client owner) {
        this.owner = owner;
    }


}
