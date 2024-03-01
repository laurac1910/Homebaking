package com.mindhub.homebanking.dtos;

import java.time.LocalDate;

public record TransferDTO (String accountOrigin , String accountDestination, double amount, String description
, LocalDate creationDate)
{
    public TransferDTO {
        if (accountOrigin.isBlank() && accountDestination.isBlank() && amount <= 0 &&description.isBlank() ) {
            throw new IllegalArgumentException("All fields are required");
        }
    }
}
