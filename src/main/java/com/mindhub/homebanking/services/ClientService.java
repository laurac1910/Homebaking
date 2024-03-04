package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ClientService {

    List <ClientDTO> getClients();

    Client getClientById (Long id);

    Client getClientByEmail (String email);

    void saveClient (Client client);
}
