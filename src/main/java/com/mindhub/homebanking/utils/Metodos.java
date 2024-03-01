package com.mindhub.homebanking.utils;

import org.springframework.stereotype.Component;

import java.util.Random;
@Component
public class Metodos {
    public String generateCardNumber() {
        StringBuilder cardNumberBuilder = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                cardNumberBuilder.append(random.nextInt(10));
            }
            if (i < 3) {
                cardNumberBuilder.append("-");
            }
        }

        return cardNumberBuilder.toString();
    }

    public int generateCVV() {
        Random random = new Random();
        return 100 + random.nextInt(900);
    }
}
