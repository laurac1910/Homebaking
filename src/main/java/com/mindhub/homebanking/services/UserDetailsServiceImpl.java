package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClientRepository clientRepository;

    @Override // SOBREESCRIBIMOS EL METODO loadUserByUsername QUE NOS DEVUELVE EL USERDETAILS
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
      Client client =  clientRepository.findByEmail(username);
        if (client == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return User // DEVUELVE UN OBJETO USERDETAILS
                .withUsername(username)
                .password(client.getPassword())
                .roles("CLIENT")
                .build();

                
    }
}
