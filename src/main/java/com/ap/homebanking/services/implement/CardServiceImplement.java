package com.ap.homebanking.services.implement;

import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.CardColor;
import com.ap.homebanking.models.CardType;
import com.ap.homebanking.repositories.CardRepository;
import com.ap.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CardServiceImplement implements CardService {
    @Autowired
    CardRepository cardRepository;

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public boolean cardExistsByNumber(String number) {
        return cardRepository.existsByNumber(number);
    }

    @Override
    public boolean cardExistsByCvv(int cvv) {
        return cardRepository.existsByCvv(cvv);
    }

    @Override
    public Card createCard(String cardHolder, CardType type, CardColor color, String number, int cvv, LocalDate fromDate, LocalDate thruDate) {
        return new Card(cardHolder, type, color, number, cvv, fromDate, thruDate);
    }
}
