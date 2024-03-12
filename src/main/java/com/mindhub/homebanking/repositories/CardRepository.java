package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card,Long> {

    boolean existsCardByCardTypeAndCardColorAndClient(CardType cardType, CardColor cardColor, Client client);

}
