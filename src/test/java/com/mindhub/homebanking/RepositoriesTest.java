package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static com.jayway.jsonpath.internal.Utils.notEmpty;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

import static org.hamcrest.Matchers.empty;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RepositoriesTest {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private LoanRepository loanRepository;

    // cards
    @Test
    public void testFindAll() {
        List<Card> cards = cardRepository.findAll();
        assertThat(cards, not(empty()));
    }

    @Test
    public void testClientHasMaxThreeCards() {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            assertFalse(client.getCards().size() > 3, "Client " + client.getName() + " can't have more than 3 cards.");
        }
    }
    @Test
    public void testCardNumberIsUnique() {
        List<Card> cards = cardRepository.findAll();
        for (Card card : cards) {
            String cardNumber = card.getCardNumber();
            long count = cards.stream().filter(ca -> ca.getCardNumber().equals(cardNumber)).count();
            assertTrue(count == 1, "Card number " + cardNumber + " is not unique.");
        }
    }

    // clients
    @Test
    public void testEmailIsUnique() {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            String email = client.getEmail();
            long count = clients.stream().filter(mail -> mail.getEmail().equals(email)).count();
            assertTrue(count == 1, "Email " + email + " is not unique.");

        }
    }

    @Test
    public void testPasswordIsNotEmpty() {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            assertThat(client.getPassword(), not(isEmptyString()));
        }
    }

    @Test
    public void testAccountHasClient() {
        List<Client> clients = clientRepository.findAll();
        for (Client client : clients) {
            assertFalse(client.getAccounts().isEmpty(), "Client " + client.getName() + " has no accounts.");
        }
    }

    //accounts

    @Test
    public void testAccountNumberIsUnique() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            String accountNumber = account.getNumber();
            long count = accounts.stream().filter(acc -> acc.getNumber().equals(accountNumber)).count();
            assertTrue(count == 1, "Account number " + accountNumber + " is not unique.");
        }
    }

    @Test
    public void testDateIsNotNull() {
        List<Account> accounts = accountRepository.findAll();
        for (Account account : accounts) {
            assertThat(account.getCreationDate(), notNullValue());
        }
    }

    // transactions
    @Test
    public void testTransactionHasAccount() {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            assertTrue(transaction.getAccount() != null, "Transaction " + transaction.getId() + " is not associated with any account.");
        }
    }

    @Test
    public void testTransactionAmountIsNotNull() {
        List<Transaction> transactions = transactionRepository.findAll();
        for (Transaction transaction : transactions) {
            assertTrue(transaction.getAmount() != 0.0, "Transaction amount should not be empty.");
        }
    }

    //loan
    @Test
    public void existLoans() {
        List<Loan> loans = loanRepository.findAll();
        assertThat(loans, is(not(empty())));

    }

    @Test
    public void existPersonalLoan() {

        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Auto"))));
        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));
        assertThat(loans, hasItem(hasProperty("name", is("Mortagage"))));

    }

}
