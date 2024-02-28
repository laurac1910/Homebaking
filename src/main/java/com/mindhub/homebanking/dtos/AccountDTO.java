package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.text.DecimalFormat;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    private Long id;
    private String number;
    private double balance;

    private String balanceInUSD;
    private Set<TransactionDTO> transactions;



    public AccountDTO(Account account) {
        this.id = account.getId();

        this.number = account.getNumber();
        this.balance = account.getBalance();
        this.balanceInUSD = convertToUSD(balance);
        this.transactions = account.getTransactions().stream()
                .map(TransactionDTO::new)
                .collect(Collectors.toSet());
    }

    public Long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }



    public String getBalanceInUSD() {
        return balanceInUSD;
    }

    public String convertToUSD(double balance) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return "$" + df.format(balance);
    }


    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }


}

