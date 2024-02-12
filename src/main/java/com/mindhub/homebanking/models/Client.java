package com.mindhub.homebanking.models;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Client {


    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;

    private String email;
 @OneToMany (mappedBy = "owner",fetch = FetchType.EAGER)
    Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
    List<ClientLoan> getLoans;

    @OneToMany(mappedBy = "client",fetch = FetchType.EAGER)
    public Set<Card> cards;

    public Client() {}

    public Client( String name, String lastName, String email) {
        this.name = name;
        this.lastName = lastName;
        this.email = email;
    }


    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public Set<Account> getAccounts(){
        return accounts;
    }

    public List<ClientLoan> getLoans() {
        return getLoans;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public Set<Card> getCards() {
        return cards;
    }

    public void setCards(Set<Card> cards) {
        this.cards = cards;
    }

    public void add(Client client) {
    }

    public void setOwner(Loan loan) {
    }
    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", accounts=" + accounts +
                '}';
    }
    public  void addAccount(Account account){
        account.setOwner(this);
        accounts.add(account);
    }


}
