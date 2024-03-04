package com.mindhub.homebanking.dtos;

public record LoanRequestDTO(double amount, int payments, double interestRate, String clientId, String name , String destinationAccount){




}
