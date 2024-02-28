package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;


import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

    @Autowired
    private PasswordEncoder passwordEncoder;
    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner data(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository, LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {

        return args -> {

            Client melba = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("123456"));
            Account cuenta1 = new Account("VIN001", LocalDate.now(), 5000, melba);
            Account cuenta2 = new Account("VIN002", LocalDate.now().plusDays(1), 7500, melba);
            Transaction transaction1 = new Transaction(TransactionType.DEBIT, -2000, LocalDate.now());
            Transaction transaction2 = new Transaction(TransactionType.CREDIT, 1000, LocalDate.now());


            melba.addAccount(cuenta1);
            melba.addAccount(cuenta2);
            cuenta1.addTransaction(transaction1);
            cuenta2.addTransaction(transaction2);

            clientRepository.save(melba);
            accountRepository.save(cuenta1);
            accountRepository.save(cuenta2);
            transactionRepository.save(transaction1);
            transactionRepository.save(transaction2);

            Loan mortagage = new Loan("Mortagage", 500000, Arrays.asList(12, 24, 36, 48, 60));
            Loan personal = new Loan("Personal", 100000, Arrays.asList(6, 12, 24));
            Loan auto = new Loan("Auto", 300000, Arrays.asList(6, 12, 24, 36));

            loanRepository.save(mortagage);
            loanRepository.save(personal);
            loanRepository.save(auto);


            ClientLoan mortagage1 = new ClientLoan(400000, 60);
            mortagage1.setClient(melba);
            mortagage1.setLoan(mortagage);
            clientLoanRepository.save(mortagage1);

            ClientLoan personal1 = new ClientLoan(50000, 12);
            personal1.setClient(melba);
            personal1.setLoan(personal);
            clientLoanRepository.save(personal1);
            clientRepository.save(melba);

            Card nuevacard = new Card("123456789", CardType.DEBIT, CardColor.GOLD, 123, LocalDate.now(), LocalDate.now().plusYears(5));
            nuevacard.setClient(melba);
            nuevacard.setCardHolder(melba);
            clientRepository.save(melba);
            cardRepository.save(nuevacard);

            Card card2 = new Card("987654321",CardType.CREDIT,CardColor.TITANIUM,321,LocalDate.now(),LocalDate.now().plusYears(5));
            card2.setClient(melba);
            card2.setCardHolder(melba);
            clientRepository.save(melba);
            cardRepository.save(card2);


            Client Laura = new Client("Laura", "Camargo", "laura@gmail.com", passwordEncoder.encode("123456789"));
            Account cuentaLaura = new Account("L1", LocalDate.now(), 10000.00, Laura);
            Laura.addAccount(cuentaLaura);
            clientRepository.save(Laura);
            accountRepository.save(cuentaLaura);
            clientRepository.save(Laura);



        };
    }

}
