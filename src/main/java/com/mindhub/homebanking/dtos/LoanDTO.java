package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

public class LoanDTO {

    private Long id;
    private String name;
    private double maxAmount;

    private List<Integer> payments ;

   private List<ClientLoan> getClients;

    public LoanDTO(Loan loan) {
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.payments = loan.getPayments();
    }



    public String getName() {
        return name;
    }

    public double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }


}
