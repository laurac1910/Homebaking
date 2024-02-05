package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
public class HomebankingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomebankingApplication.class, args);
    }

    @Bean
    public CommandLineRunner data(ClientRepository clientRepository, AccountRepository accountRepository) {

        return args -> {
            Client melba = new Client("Melba", "Morel", "melba@mindhub.com");
            Account cuenta1 = new Account("1", LocalDate.now(), 5000);
            Account cuenta2 = new Account("2",LocalDate.now().plusDays(1),7500);
            melba.addAccount(cuenta1);
            melba.addAccount(cuenta2);
            clientRepository.save(melba);
            accountRepository.save(cuenta1);
            accountRepository.save(cuenta2);

            System.out.println(melba);


            Client Laura = new Client( "Laura","Camargo","laura@gmail.com");
            Account cuentaLaura = new Account("L1",LocalDate.now(),10000.00);
            Laura.addAccount(cuentaLaura);
            clientRepository.save(Laura);
           accountRepository.save(cuentaLaura);




        };
    }

}
