package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<AccountDTO> accounts;
    private List<ClientLoanDTO>loans;

    private Set <CardDTO> cards;


    public ClientDTO(Client client) {
        this.id = client.getId();
        this.firstName = client.getName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());
        this.loans = client.getLoans().stream().map(ClientLoanDTO::new).toList();
        this.cards=client.getCards().stream().map(CardDTO::new).collect(Collectors.toSet());
    }

    public List<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }


    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }
}

