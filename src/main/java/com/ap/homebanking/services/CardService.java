package com.ap.homebanking.services;

import com.ap.homebanking.models.Card;
import com.ap.homebanking.models.CardColor;
import com.ap.homebanking.models.CardType;

import java.time.LocalDate;

public interface CardService {

    void saveCard(Card card);

    boolean cardExistsByNumber(String number);

    boolean cardExistsByCvv(int cvv);

    Card createCard(String cardHolder, CardType type, CardColor color, String number, int cvv, LocalDate fromDate, LocalDate thruDate);
}
