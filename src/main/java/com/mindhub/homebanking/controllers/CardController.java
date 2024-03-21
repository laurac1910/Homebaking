package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardRequestDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ClientService clientService;
    @Autowired
    private com.mindhub.homebanking.utils.metodos metodos;

    @PostMapping("/current") //Esta anotación indica que este método maneja las solicitudes HTTP POST enviadas a "/api/cards/current"
    public ResponseEntity<?> createCard(@RequestBody CardRequestDTO cardRequestDTO) {
        try {
            String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
            Client client = clientService.getClientByEmail(userEmail);

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

            return new ResponseEntity<>("Card created successfully", HttpStatus.CREATED);


        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Error creating card", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/{cardId}") // Anotación para manejar solicitudes DELETE a la ruta /api/cards/{cardId}
    public ResponseEntity<?> deleteCard(@PathVariable Long cardId) {
        try {
            // Busca la tarjeta por su ID
            Optional<Card> optionalCard = cardRepository.findById(cardId);

            // Verifica si la tarjeta existe
            if (optionalCard.isPresent()) {
                // Si la tarjeta existe, elimínala
                cardRepository.delete(optionalCard.get());
                return new ResponseEntity<>("Card deleted successfully", HttpStatus.OK);
            } else {
                // Si la tarjeta no existe, devuelve un mensaje de error
                return new ResponseEntity<>("Card not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>("Error deleting card", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}




