package com.ap.homebanking.services;

import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;

import java.time.LocalDateTime;

public interface TransactionService {
    void saveTransaction(Transaction transaction);

    Transaction createTransaction(TransactionType type, long amount, String description, LocalDateTime date);
}
