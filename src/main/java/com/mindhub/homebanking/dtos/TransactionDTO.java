package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TransactionDTO {
    private Long id;
    private TransactionType type;
    private double amount;
    private String amountInUSD;
    LocalDate creationDate;



    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.amountInUSD = convertToUSD(amount);
        this.creationDate = transaction.getCreationDate();
    }

    public Long getId() {
        return id;
    }


    public TransactionType getType() {
        return type;
    }

    public String getAmountInUSD() {
        return amountInUSD;
    }
    public String convertToUSD(double balance) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return "$" + df.format(balance);
    }

    public String getCreationDate() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return  creationDate.format(formatter);

    }


}
