package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CardDTO {

        private  Long id;

        private Long cardNumber;

        private CardType cardType;
        private CardColor cardColor;

        private Integer cvv;

        private LocalDate fromDate;
       private LocalDate thruDate;

       private String clientName;

    private String clientLastName;

    public CardDTO(Card card) {
        this.id = card.getId();
        this.cardNumber = card.getCardNumber();
        this.cardType = card.getCardType();
        this.cardColor = card.getCardColor();
        this.cvv = card.getCvv();
        this.fromDate = card.getFromDate();
        this.thruDate = card.getThruDate();
       this.clientName = card.getClient().getName();
        this.clientLastName = card.getClient().getLastName();
    }

    public Long getId() {
        return id;
    }

    public Long getCardNumber() {
        return cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public Integer getCvv() {
        return cvv;
    }

    public String getFromDate() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        return  fromDate.format(formatter);
    }

    public String getThruDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yy");
        return  thruDate.format(formatter);

    }

    public String getClientName() {
        return clientName;
    }

    public String getClientLastName() {
        return clientLastName;
    }
}

