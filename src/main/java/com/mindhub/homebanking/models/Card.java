package com.mindhub.homebanking.models;

import jakarta.persistence.*;
import org.springframework.boot.autoconfigure.web.format.DateTimeFormatters;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;

    private Long cardNumber;

    private String cardHolder;
    private CardType cardType;
   private CardColor cardColor;

    private Integer cvv;

    LocalDate fromDate;
     LocalDate thruDate;

    @ManyToOne
    @JoinColumn(name = "Holder")
     private Client client;


    public Card() {
    }


    public Card(Long cardNumber, CardType cardType, CardColor cardColor, Integer cvv, LocalDate fromDate, LocalDate thruDate) {
        this.cardNumber = cardNumber;
        this.cardType = cardType;
        this.cardColor = cardColor;
        this.cvv = cvv;
        this.fromDate = fromDate;
        this.thruDate = thruDate;

    }

    public Long getId() {
        return id;
    }


    public Long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(Long cardNumber) {
        this.cardNumber = cardNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public CardColor getCardColor() {
        return cardColor;
    }

    public void setCardColor(CardColor cardColor) {
        this.cardColor = cardColor;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public LocalDate getThruDate() {

        return thruDate;
    }

    public void setFromDate(LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public void setThruDate(LocalDate thruDate) {
        this.thruDate = thruDate;
    }

    public LocalDate getFromDate() {
        return fromDate;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder( Client client) {
        this.cardHolder = client.getName()+" "+client.getLastName();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


}


