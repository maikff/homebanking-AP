package com.ap.homebanking;

import com.ap.homebanking.models.*;
import com.ap.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}

	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository){
		return (args -> {
            /*//clientRepository.save(new Client("Melba", "Morel", "melba@mindhub.com"));
            // clientRepository.save(new Client("Jaime", "Pereira", "jaime@mindhub.com"));

            Client client1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123456"));
			Client client2 = new Client("Jaime", "Pereira", "jaime@mindhub.com", passwordEncoder.encode("123456"));

			Client admin = new Client("Prudencio", "Varela", "admin@mindhubbank.com", passwordEncoder.encode("admin"));

			Account account1 = new Account("VIN001", LocalDate.now(),5000);
			Account account2 = new Account("VIN002", LocalDate.now().plusDays(1),7500);
			Account account3 = new Account("VIN003", LocalDate.now().plusDays(3),55000);

			Transaction transaction1 = new Transaction(TransactionType.CREDIT,7000,"Netflix", LocalDateTime.now());
			Transaction transaction2 = new Transaction(TransactionType.DEBIT,9000,"Prime", LocalDateTime.now());
			Transaction transaction3 = new Transaction(TransactionType.CREDIT,60000,"Luz", LocalDateTime.now());
			Transaction transaction4 = new Transaction(TransactionType.CREDIT,85000,"Impuestos", LocalDateTime.now());
			Transaction transaction5 = new Transaction(TransactionType.DEBIT,2000,"Transporte", LocalDateTime.now());
			Transaction transaction6 = new Transaction(TransactionType.CREDIT,56000,"Comida", LocalDateTime.now());

			//List<Integer> payments1 = Arrays.asList(12, 24, 36, 48, 60);
			//List<Integer> payments2 = Arrays.asList(6, 12, 24);
			//List<Integer> payments3 = Arrays.asList(6, 12, 24, 36);

			Loan loan1 = new Loan("Mortgage",50000,Arrays.asList(12, 24, 36, 48, 60));
			Loan loan2 = new Loan("Personal",100000,Arrays.asList(6, 12, 24));
			Loan loan3 = new Loan("Automotive",300000,Arrays.asList(6, 12, 24, 36));*/

			/*ClientLoan clientLoan1 = new ClientLoan(client1, loan1, 60, 400000);
			ClientLoan clientLoan2 = new ClientLoan(client1, loan2, 12, 500000);
			ClientLoan clientLoan3 = new ClientLoan(client2, loan2, 24, 100000);
			ClientLoan clientLoan4 = new ClientLoan(client2, loan3, 36, 200000);

			Card card1 = new Card(client1.toString(),CardType.DEBIT,CardColor.GOLD,"123-456-789",123,LocalDate.now(),LocalDate.now().plusYears(5));
			Card card2 = new Card(client1.toString(),CardType.CREDIT,CardColor.TITANIUM,"987-654-321",321,LocalDate.now(),LocalDate.now().plusYears(5));
			Card card3 = new Card(client2.toString(),CardType.CREDIT,CardColor.SILVER,"234-567-891",231,LocalDate.now(),LocalDate.now().plusYears(5));

			clientRepository.save(client1);
			clientRepository.save(client2);

			clientRepository.save(admin);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);

			account1.addTransaction(transaction1);
			account1.addTransaction(transaction2);
			account2.addTransaction(transaction3);
			account2.addTransaction(transaction4);
			account3.addTransaction(transaction5);
			account3.addTransaction(transaction6);

			client1.addAccount(account1);
			client1.addAccount(account2);
			client2.addAccount(account3);


			loan1.addClientLoan(clientLoan1);
			loan2.addClientLoan(clientLoan2);
			loan2.addClientLoan(clientLoan3);
			loan3.addClientLoan(clientLoan4);

			client1.addClientLoan(clientLoan1);
			client1.addClientLoan(clientLoan2);
			client2.addClientLoan(clientLoan3);
			client2.addClientLoan(clientLoan4);

			client1.addCard(card1);
			client1.addCard(card2);
			client2.addCard(card3);

			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			clientRepository.save(client1);
			clientRepository.save(client2);

			accountRepository.save(account1);
			accountRepository.save(account2);
			accountRepository.save(account3);

			clientLoanRepository.save(clientLoan1);
			clientLoanRepository.save(clientLoan2);
			clientLoanRepository.save(clientLoan3);
			clientLoanRepository.save(clientLoan4);

			loanRepository.save(loan1);
			loanRepository.save(loan2);
			loanRepository.save(loan3);

			cardRepository.save(card1);
			cardRepository.save(card2);
			cardRepository.save(card3);*/
		});
	}
}
