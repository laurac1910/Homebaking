package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

import java.text.DecimalFormat;

public class ClientLoanDTO {
    private Long id;
    private Long loanId;
    private String loanName;

    private double amount;

    private String amountInUSD;

    private double payments;


    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id=clientLoan.getId();
        this.loanId= clientLoan.getLoan().getId();
      this.loanName=clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.amountInUSD = convertToUSD(amount);
        this.payments = clientLoan.getPayments();

    }

    public Long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }


    public String getAmountInUSD() {
        return amountInUSD;
    }
    public String convertToUSD(double balance) {
        DecimalFormat df = new DecimalFormat("#,###.00");
        return "$" + df.format(balance);
    }

    public double getPayments() {
        return payments;
    }


    public Long getId() {
        return id;
    }
}
