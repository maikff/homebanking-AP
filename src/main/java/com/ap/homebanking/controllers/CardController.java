package com.ap.homebanking.controllers;

import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.CardColor;
import com.ap.homebanking.models.CardType;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.repositories.CardRepository;
import com.ap.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Random;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @RequestMapping(path = "/clients/current/cards", method = RequestMethod.POST)
    public ResponseEntity <Object> createCard (
            @RequestParam CardColor cardColor,
            @RequestParam CardType cardType,
            Authentication authentication){

        if (authentication == null || authentication.getName() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication required.");
        }

        if (cardColor == null || cardType == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Card color and type are required.");
        }

        Client authenticatedClient = clientRepository.findByEmail(authentication.getName());

        if (authenticatedClient != null) {
            Set<Card> clientCards= authenticatedClient.getCards();

            int cardsOfTypeAndColor = (int) clientCards.stream().filter(card -> card.getType() == cardType && card.getColor() == cardColor).count();

            if (cardsOfTypeAndColor >= 1) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You cannot exceed the limit of 1 cards of this type and color.");
            }

            int cvv;
            String cardNumber;

            do {
                cvv = generateRandomCVV();
                cardNumber = generateRandomCardNumber();

            } while (cardRepository.existsByNumber(cardNumber) || cardRepository.existsByCvv(cvv));

            Card newCard = new Card(authenticatedClient.toString(), cardType, cardColor, cardNumber, cvv, LocalDate.now(),LocalDate.now().plusYears(5));
            cardRepository.save(newCard);

            authenticatedClient.addCard(newCard);
            clientRepository.save(authenticatedClient);

            return ResponseEntity.status(HttpStatus.CREATED).body("The card was created successfully");

        }else {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid authenticated client.");

        }

    }
    private String generateRandomCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();

        for(int i=0; i<4; i++) {
            int n = random.nextInt(9999);
            sb.append(String.format("%04d", n));
            if(i < 3) {
                sb.append("-");
            }
        }

        return sb.toString();
    }

    private int generateRandomCVV() {
        Random random = new Random();
        return random.nextInt(999);
    }
}