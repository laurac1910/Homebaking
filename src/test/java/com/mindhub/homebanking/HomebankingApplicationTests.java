package com.mindhub.homebanking;

import com.mindhub.homebanking.utils.metodos;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.HashSet;
import java.util.Set;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class HomebankingApplicationTests {

    @Test
    void contextLoads() {
    }

    @RepeatedTest(100)
    void testGenerateCardNumber() {
        String cardNumber = metodos.generateCardNumber();
        assertThat(cardNumber.length(), equalTo(19));

        String cardFormat = metodos.generateCardNumber();
        assertTrue(cardFormat.matches("\\d{4}-\\d{4}-\\d{4}-\\d{4}"));
    }

    @RepeatedTest(100)
    void testGenerateCVV() {
        int cvv = metodos.generateCVV();
        assertThat(cvv, allOf(greaterThanOrEqualTo(100), lessThanOrEqualTo(999)));
    }

    @RepeatedTest(100)
    void testGenerateAccountNumber() {
        String accountNumber = metodos.generateAccountNumber();
        assertThat(accountNumber, startsWith("VIN-"));
        assertThat(accountNumber.length(), equalTo(12));


        Set<String> generatedNumbers = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            String accountNumber2 = metodos.generateAccountNumber();
            generatedNumbers.add(accountNumber2);
        }
        assertTrue(generatedNumbers.size() > 900);
    }

}
