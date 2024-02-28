package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;

    @PostMapping("/current")
    public ResponseEntity<?> createCard(@RequestBody CardRequest cardRequest) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Client client = clientRepository.findByEmail(userEmail);

            if (client == null) {
                return new ResponseEntity<>("Client Not Found", HttpStatus.FORBIDDEN);
            }


            long existingCardsCount = client.getCards().stream()
                    .filter(card -> card.getCardType() == cardRequest.getType() && card.getCardColor() == cardRequest.getColor())
                    .count();

            if (existingCardsCount >= 3) {
                return new ResponseEntity<>("The client already has the maximum number of cards of this type and color", HttpStatus.FORBIDDEN);
            }

            String cardNumber = generateCardNumber();
            int cvv = generateCVV();
            String cardHolderName = client.getName() + " " + client.getLastName();
            Card card = new Card(cardNumber, cardRequest.getType(), cardRequest.getColor(), cvv, LocalDate.now(), LocalDate.now().plusYears(5), cardHolderName);
            card.setClient(client);
            cardRepository.save(card);


            return new ResponseEntity<>( HttpStatus.CREATED);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Error creating card", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String generateCardNumber() {
        StringBuilder cardNumberBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cardNumberBuilder.append(random.nextInt(10));
            }
            if (i < 3) {
                cardNumberBuilder.append("-");
            }
        }

        return cardNumberBuilder.toString();
    }

    private int generateCVV() {
        Random random = new Random();
        return 100 + random.nextInt(900);
    }


    static class CardRequest {
        private CardType type;
        private CardColor color;



        public CardType getType() {
            return type;
        }

        public void setType(CardType type) {
            this.type = type;
        }

        public CardColor getColor() {
            return color;
        }

        public void setColor(CardColor color) {
            this.color = color;
        }
    }}