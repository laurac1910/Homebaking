package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardRequestDTO;
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
import com.mindhub.homebanking.utils.Metodos;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private Metodos metodos;

    @PostMapping("/current")
    public ResponseEntity<?> createCard(@RequestBody CardRequestDTO cardRequestDTO) {
        try {
            String cardType = cardRequestDTO.cardType();
            String cardColor = cardRequestDTO.cardColor();
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Client client = clientRepository.findByEmail(userEmail);

            if (client == null) {
                return new ResponseEntity<>("Client Not Found", HttpStatus.FORBIDDEN);
            }

            long existingCardsCount = client.getCards().stream()
                    .filter(card -> card.getCardType() == CardType.valueOf(cardType.toUpperCase()) && card.getCardColor() == CardColor.valueOf(cardColor.toUpperCase()))
                    .count();

            if (existingCardsCount >= 3) {
                return new ResponseEntity<>("The client already has the maximum number of cards of this type and color", HttpStatus.FORBIDDEN);
            }

            String cardNumber = metodos.generateCardNumber();
            int cvv = metodos.generateCVV();
            String cardHolderName = client.getName() + " " + client.getLastName();
            Card card = new Card(cardNumber, CardType.valueOf(cardType.toUpperCase()), CardColor.valueOf(cardColor.toUpperCase()), cvv, LocalDate.now(), LocalDate.now().plusYears(5), cardHolderName);
            card.setClient(client);
            cardRepository.save(card);

            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Error creating card", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}



