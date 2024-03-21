package com.mindhub.homebanking.utils;

import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class metodos {
    public  static String generateCardNumber() {
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

    public static int generateCVV() {
        Random random = new Random();
        return 100 + random.nextInt(900);
    }

    public static String generateAccountNumber() {
        String prefix = "VIN-";
        int randomNumber = (int) (Math.random() * 90000000) + 10000000;
        return prefix + randomNumber;
    }

    public boolean validateEmail(String email) {
        return email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }
}
