package com.ap.homebanking;

import com.ap.homebanking.models.Account;
import com.ap.homebanking.models.Client;
import com.ap.homebanking.models.Transaction;
import com.ap.homebanking.models.TransactionType;
import com.ap.homebanking.repositories.AccountRepository;
import com.ap.homebanking.repositories.ClientRepository;
import com.ap.homebanking.repositories.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository){
		return (args -> {
            //clientRepository.save(new Client("Melba", "Morel", "melba@mindhub.com"));
            // clientRepository.save(new Client("Jaime", "Pereira", "jaime@mindhub.com"));

            Client client1 = new Client("Melba", "Morel", "melba@mindhub.com");
			Client client2 = new Client("Jaime", "Pereira", "jaime@mindhub.com");

			Account account1 = new Account("VIN001", LocalDate.now(),5000);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1),7500);
			Account account3 = new Account("VIN003", LocalDate.now().plusDays(3),55000);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT,7000,"Netflix", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT,9000,"Prime", LocalDateTime.now());
			Transaction transaction3 = new Transaction(TransactionType.CREDIT,60000,"Luz", LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.CREDIT,85000,"Impuestos", LocalDateTime.now());
			Transaction transaction5 = new Transaction(TransactionType.DEBIT,2000,"Transporte", LocalDateTime.now());
			Transaction transaction6 = new Transaction(TransactionType.CREDIT,56000,"Comida", LocalDateTime.now());

			clientRepository.save(client1);
			clientRepository.save(client2);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);

			clientRepository.save(client1);
			clientRepository.save(client2);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);
		});
	}
}
