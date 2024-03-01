package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardRequestDTO;
import com.mindhub.homebanking.models.*;
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

    @PostMapping("/current") //Esta anotación indica que este método maneja las solicitudes HTTP POST enviadas a "/api/cards/current"
    public ResponseEntity<?> createCard(@RequestBody CardRequestDTO cardRequestDTO) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Client client = clientRepository.findByEmail(userEmail);

            boolean existingCard = cardRepository.existsCardByCardTypeAndCardColorAndClient(
                    CardType.valueOf(cardRequestDTO.cardType().toUpperCase()),
                    CardColor.valueOf(cardRequestDTO.cardColor().toUpperCase()),
                  client
            );

            if (existingCard) {
                return new ResponseEntity<>("You already have a card of this type or color", HttpStatus.BAD_REQUEST);
            }

            String cardNumber = metodos.generateCardNumber();
            int cvv = metodos.generateCVV();
            String cardHolderName = client.getName() + " " + client.getLastName();


            Card card = new Card(cardNumber, CardType.valueOf(cardRequestDTO.cardType().toUpperCase()), CardColor.valueOf(cardRequestDTO.cardColor().toUpperCase()), cvv, LocalDate.now(), LocalDate.now().plusYears(5), cardHolderName);;
            card.setClient(client);
            cardRepository.save(card);

            return new ResponseEntity<>(HttpStatus.CREATED);


        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Error creating card", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}



