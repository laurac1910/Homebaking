package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class ClientLoanDTO {
    private Long id;
    private Long loanId;
    private String loanName;

    private double amount;

    private double payments;


    public ClientLoanDTO(ClientLoan clientLoan) {
        this.id=clientLoan.getId();
        this.loanId= clientLoan.getLoan().getId();
      this.loanName=clientLoan.getLoan().getName();
        this.amount = clientLoan.getAmount();
        this.payments = clientLoan.getPayments();

    }

    public Long getLoanId() {
        return loanId;
    }

    public String getLoanName() {
        return loanName;
    }

    public double getAmount() {
        return amount;
    }


    public double getPayments() {
        return payments;
    }


    public Long getId() {
        return id;
    }
}
