package com.mindhub.homebanking;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class HomebankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
	}
@Bean
	 public CommandLineRunner data (ClientRepository clientRepository){

		return args -> {
			Client laura = new Client("Melba", "Morel", "melba@mindhub.com");

			clientRepository.save(laura);

			System.out.println(laura);
		};
	 }

}
